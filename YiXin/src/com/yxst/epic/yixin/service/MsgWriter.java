package com.yxst.epic.yixin.service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.util.Log;

import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.data.dto.request.PushRequest;
import com.yxst.epic.yixin.data.dto.response.BaseResponse;
import com.yxst.epic.yixin.data.dto.response.PushResponse;
import com.yxst.epic.yixin.data.rest.Appmsgsrv8093;
import com.yxst.epic.yixin.db.DBManager;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.rest.Appmsgsrv8093Proxy;
import com.yxst.epic.yixin.utils.CacheUtils;

public class MsgWriter {

	private static final String TAG = "MsgWriter";
	
	public static final int QUEUE_SIZE = 500;
	private final ArrayBlockingQueueWithShutdown<Message> queue = new ArrayBlockingQueueWithShutdown<Message>(QUEUE_SIZE, true);

	private Thread writerThread;
	private Appmsgsrv8093 writer;
	
	volatile boolean done;
	
	AtomicBoolean shutdownDone = new AtomicBoolean(false);
	
	private Context context;
	
	public MsgWriter(Context context) {
		this.context = context;
		init();
	}
	
	protected void init() {
		writer = Appmsgsrv8093Proxy.create();
        done = false;
        shutdownDone.set(false);

        queue.start();
        writerThread = new Thread() {
            @Override
			public void run() {
                writePackets(this);
            }
        };
        writerThread.setName("Msg Writer (" + writer + ")");
        writerThread.setDaemon(true);
    }
	
	public void sendMsgs(List<Message> msgs) {
		if (msgs != null) {
			for (Message msg : msgs) {
				try {
					sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendMsg(Msg msg) {
		Message message = DBMessage.retriveMessageFromMsg(msg, DBMessage.INOUT_OUT, DBMessage.STATUS_PENDING);
		sendMessage(message);
	}
	
	public void sendMessage(Message packet) /*throws NotConnectedException*/ {
		
		if (packet != null) {
			if (packet.getId() == null || packet.getId() <= 0) {
				DBManager.getInstance(context).insertMessage(packet);
			} else {
				packet.setExtTime(System.currentTimeMillis());
				packet.setExtStatus(DBMessage.STATUS_PENDING);
				DBManager.getInstance(context).update(packet);
			}
		}
		
        if (done) {
//            throw new NotConnectedException();
        	return;
        }

        try {
        	/*
        	 * 加入队列等待发送
        	 */
            queue.put(packet);
        }
        catch (InterruptedException ie) {
        	ie.printStackTrace();
//            throw new NotConnectedException();
        }
    }
	
	public void startup() {
        writerThread.start();
    }
	
	public void shutdown() {
        done = true;
        queue.shutdown();
        synchronized(shutdownDone) {
            if (!shutdownDone.get()) {
                try {
//                    shutdownDone.wait(connection.getPacketReplyTimeout());
                    shutdownDone.wait(10 * 1000);
                }
                catch (InterruptedException e) {
                }
            }
        }
    }
	
	private Message nextMsg() {
        if (done) {
            return null;
        }

        Message packet = null;
        try {
            packet = queue.take();
        }
        catch (InterruptedException e) {
            // Do nothing
        }
        return packet;
    }

    private void writePackets(Thread thisThread) {
        try {
            // Write out packets from the queue.
            while (!done && (writerThread == thisThread)) {
            	Message packet = nextMsg();
            	push(packet);
            }
            // Flush out the rest of the queue. If the queue is extremely large, it's possible
            // we won't have time to entirely flush it before the socket is forced closed
            // by the shutdown process.
            try {
                while (!queue.isEmpty()) {
                	Message packet = queue.remove();
                	push(packet);
                }
            }
            catch (Exception e) {
            	e.printStackTrace();
//                LOGGER.log(Level.WARNING, "Exception flushing queue during shutdown, ignore and continue", e);
            }

            // Delete the queue contents (hopefully nothing is left).
            queue.clear();

            shutdownDone.set(true);
            synchronized(shutdownDone) {
                shutdownDone.notify();
            }
        }
        catch (/*IO*/Exception ioe) {
            // The exception can be ignored if the the connection is 'done'
            // or if the it was caused because the socket got closed
//            if (!(done || connection.isSocketClosed())) {
            if (!(done)) {
                shutdown();
//                connection.notifyConnectionError(ioe);
//                *.notifyConnectionError(ioe);
            }
        }
    }
    
    private void push(Message message) {
    	if (message == null) {
    		return;
    	}
    	
    	message.setExtStatus(DBMessage.STATUS_SENDING);
		DBManager.getInstance(context).update(message);
    	
    	try {
			PushRequest request = new PushRequest();
			request.BaseRequest = CacheUtils.getBaseRequest(context);
			request.Msg = DBMessage.retriveMsgFromMessage(message);
			Log.d(TAG, "request:" + request);
			PushResponse response = writer.push(request);
//			response.BaseResponse.Ret = 65533;
			Log.d(TAG, "writer.getRootUrl():"+writer.getRootUrl());
			Log.d(TAG, "response:" + response);
			
			if (response != null && response.BaseResponse.Ret == BaseResponse.RET_SUCCESS) {
				message.setExtStatus(DBMessage.STATUS_SUCCESS);
				message.setMid(response.mid);
				DBManager.getInstance(context).update(message);
			} else {
				message.setExtStatus(DBMessage.STATUS_ERROR);
				DBManager.getInstance(context).update(message);
			}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    		
    		if (message != null) {
    			message.setExtStatus(DBMessage.STATUS_ERROR);
    			DBManager.getInstance(context).update(message);
    		}
    	}
    }
}
