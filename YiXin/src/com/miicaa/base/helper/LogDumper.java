package com.miicaa.base.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

 public class LogDumper extends Thread {  
	  
    private Process logcatProc;  
    private BufferedReader mReader = null;  
    private boolean mRunning = true;  
    String cmds = null;  
    private String mPID;  
    String logError;
//    private FileOutputStream out = null;  

    public LogDumper(String pid, String dir) {  
        mPID = pid;  
//        try {  
//            out = new FileOutputStream(new File(dir, "GPS-"  
//                    + MyDate.getFileName() + ".log"));  
//        } catch (FileNotFoundException e) {  
//            // TODO Auto-generated catch block  
//            e.printStackTrace();  
//        }  

        /** 
         *  
         * 日志等级：*:v , *:d , *:w , *:e , *:f , *:s 
         *  
         * 显示当前mPID程序的 E和W等级的日志. 
         *  
         * */  

        // cmds = "logcat *:e *:w | grep \"(" + mPID + ")\"";  
        // cmds = "logcat  | grep \"(" + mPID + ")\"";//打印所有日志信息  
        // cmds = "logcat -s way";//打印标签过滤信息  
        /*
         * 只打出错误日志
         */
        cmds = "logcat *:e | grep \"(" + mPID + ")\"";  

    }  

    public void stopLogs() {  
        mRunning = false;  
    }  

    @Override  
    public void run() {  
        try {  
            logcatProc = Runtime.getRuntime().exec(cmds);  
            mReader = new BufferedReader(new InputStreamReader(  
                    logcatProc.getInputStream()), 1024);  
            String line = null;  
            while (mRunning && (line = mReader.readLine()) != null) {  
                if (!mRunning) {  
                    break;  
                }  
                if (line.length() == 0) {  
                    continue;  
                }  
                /*
                 * 暂时不保存在本地
                 */
//                if (out != null && line.contains(mPID)) {  
//                    out.write((MyDate.getDateEN() + "  " + line + "\n")  
//                            .getBytes());  
//                }  
                
            }  

        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (logcatProc != null) {  
                logcatProc.destroy();  
                logcatProc = null;  
            }  
            if (mReader != null) {  
                try {  
                    mReader.close();  
                    mReader = null;  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
//            if (out != null) {  
//                try {  
//                    out.close();  
//                } catch (IOException e) {  
//                    e.printStackTrace();  
//                }  
//                out = null;  
//            }  

        }  

    }  

}  


