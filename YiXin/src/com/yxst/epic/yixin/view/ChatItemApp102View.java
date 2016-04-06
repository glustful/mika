package com.yxst.epic.yixin.view;

import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;
import com.yxst.epic.yixin.activity.ContactDetailActivity_;
import com.yxst.epic.yixin.data.dto.model.Msg;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Body;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Head;
import com.yxst.epic.yixin.data.dto.model.ObjectContentApp102.Operation;
import com.yxst.epic.yixin.db.DBMessage;
import com.yxst.epic.yixin.db.Message;
import com.yxst.epic.yixin.listener.OnOperationClickListener;
import com.yxst.epic.yixin.utils.Utils;

@EViewGroup(R.layout.list_item_chat_app_102)
public class ChatItemApp102View extends ChatItem implements OnItemClickListener {

	public static final String TAG = "ChatItemApp102View";

	@ViewById
	LinearLayout layoutHead;

	@ViewById
	WrapContentHeightListView listBody;

	// @ViewById
	// HorizontalListView listOperation;

	@ViewById
	WrapContentHeightGridView gridOperation;

	// @ViewById
	// View viewDivider;

	BodyAdapter bodyAdapter;
	OperationsAdapter2 operationsAdapter2;

	public ChatItemApp102View(Context context) {
		super(context);
	}

	@AfterViews
	void afterViews() {
		gridOperation.setOnItemClickListener(this);

		initViewHolderHead(layoutHead);
		bodyAdapter = new BodyAdapter();
		listBody.setAdapter(bodyAdapter);
		operationsAdapter2 = new OperationsAdapter2();
		gridOperation.setAdapter(operationsAdapter2);
	}

	@Override
	protected void bind(Message message) {
		Msg msg = DBMessage.retriveMsgFromMessage(message);
		ObjectContentApp102 oc = (ObjectContentApp102) msg
				.getObjectContentAsObjectContent();

		bindViewHolderHead(oc.head);
		bodyAdapter.chageData(oc.body);
		operationsAdapter2.chageData(oc.operations);

		if (oc.operations == null || oc.operations.size() == 0) {
			gridOperation.setVisibility(View.GONE);
		} else {
			gridOperation.setVisibility(View.VISIBLE);
		}
	}

	@Click(R.id.iv_icon)
	void onClickIcon(View view) {
		ContactDetailActivity_.intent(getContext())
				.userName(message.getExtRemoteUserName()).start();
	}

	private class BodyAdapter extends BaseAdapter {

		private List<Body> body;

		public BodyAdapter() {
		}

		public BodyAdapter(List<Body> body) {
			this.body = body;
			// this.body.addAll(this.body.subList(0, this.body.size()));
		}

		public void chageData(List<Body> body) {
			this.body = body;
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return body == null ? 0 : body.size();
		}

		@Override
		public Object getItem(int position) {
			return body.get(0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Body item = (Body) getItem(position);

			App102BodyItem view = (App102BodyItem) convertView;
			if (view == null) {
				view = App102BodyItem_.build(parent.getContext());
			}
			
			view.bind(item);
			
			return view;
		}

	}

	// private class OperationsAdapter extends BaseAdapter {
	//
	// private List<Operation> operations;
	//
	// public OperationsAdapter(List<Operation> operations) {
	// this.operations = operations;
	// // this.operations.addAll(this.operations.subList(0,
	// // this.operations.size()));
	// }
	//
	// private static final int MAX_COUNT = 3;
	//
	// @Override
	// public int getCount() {
	// // return this.operations == null ? 0 : this.operations.size();
	// return this.operations == null ? 0 : Math.min(
	// this.operations.size(), MAX_COUNT);
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // return this.operations.get(position);
	//
	// Object obj = null;
	// if (this.operations.size() > MAX_COUNT) {
	// if (position < MAX_COUNT - 1) {
	// obj = this.operations.get(position);
	// } else {
	// obj = this.operations;
	// }
	// } else {
	// obj = this.operations.get(position);
	// }
	// return obj;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// App102Operation view;
	//
	// if (convertView == null) {
	// view = App102Operation_.build(parent.getContext());
	// setupLayoutParams(view, parent);
	// } else {
	// view = (App102Operation) convertView;
	// }
	//
	// Object object = getItem(position);
	// if (object instanceof Operation) {
	// Operation operation = (Operation) object;
	// // view.bind(position != 0, operation);
	// view.bind(operation);
	// } else if (object instanceof List) {
	// // List<Operation> operations = (List<Operation>) object;
	// Operation operation = new Operation();
	// operation.content = "更多";
	// // view.bind(position != 0, operation);
	// view.bind(operation);
	// }
	//
	// return view;
	// }
	//
	// private void setupLayoutParams(View convertView, ViewGroup parent) {
	// // Log.d(TAG, "getView() parent.getWidth():" + parent.getWidth());
	// // Log.d(TAG,
	// // "getView() parent.getMeasuredWidth():"
	// // + parent.getMeasuredWidth());
	//
	// int N = getCount();
	//
	// ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
	// (parent.getWidth() - (N - 1) * 1) / getCount(),
	// ViewGroup.LayoutParams.MATCH_PARENT);
	// convertView.setLayoutParams(params);
	// }
	// }

	private class OperationsAdapter2 extends BaseAdapter {
		private boolean isShowMore;

		private List<Operation> operations;

		public OperationsAdapter2() {
		}

		public void chageData(List<Operation> operations) {
			this.operations = operations;
			this.notifyDataSetChanged();
		}

		public OperationsAdapter2(List<Operation> operations) {
			this.operations = operations;
		}

		public boolean isShowMore() {
			return isShowMore;
		}

		public void setShowMore(boolean isShowMore) {
			this.isShowMore = isShowMore;
			this.notifyDataSetChanged();
		}

		private static final int NUM_COLUMNS = 3;

		public boolean positionIsShowMore(int position) {
			return hasMore() && position == NUM_COLUMNS - 1;
		}

		public boolean positionInRealCount(int position) {
			return position < getRealCount();
		}
		
		private boolean hasMore() {
			return operations.size() > NUM_COLUMNS;
		}

		@Override
		public int getCount() {
			int realCount = getRealCount();
			return realCount + (NUM_COLUMNS - realCount % NUM_COLUMNS) % NUM_COLUMNS;
		}
		
		private int getRealCount() {
			if (operations == null) {
				return 0;
			}

			if (!isShowMore) {
				return Math.min(NUM_COLUMNS, operations.size());
			} else {
				int size = operations.size();
				return size + 1;
			}
		}

		@Override
		public Operation getItem(int position) {
			if (positionInRealCount(position)) {
				if (hasMore()) {
					if (position == NUM_COLUMNS - 1) {
						Operation operation = new Operation();
						operation.content = "更多";
						return operation;
					} else if (position < NUM_COLUMNS - 1) {
						return operations.get(position);
					} else if (position > NUM_COLUMNS - 1) {
						return operations.get(position - 1);
					}
				} else {
					return operations.get(position);
				}
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			App102Operation view;
//
//			if (convertView == null) {
//				view = App102Operation_.build(parent.getContext());
//			} else {
//				view = (App102Operation) convertView;
//			}
//
//			view.bind(getItem(position));
//			view.bindStyle(NUM_COLUMNS, position);
//
//			return view;
			return null;
		}
	}

	private OnOperationClickListener mOnOperationClickListener;

	public void setOnOperationClickListener(OnOperationClickListener l) {
		mOnOperationClickListener = l;
	}

	// @ItemClick(R.id.listOperation)
	// void onItemClickOperation(Object object) {
	// if (object instanceof Operation) {
	// Operation operation = (Operation) object;
	// if (mOnOperationClickListener != null) {
	// mOnOperationClickListener.onOperationClick(operation);
	// }
	// } else if (object instanceof List) {
	// @SuppressWarnings("unchecked")
	// List<Operation> operations = (List<Operation>) object;
	// if (mOnOperationClickListener != null) {
	// mOnOperationClickListener.onOperationsClick(operations);
	// }
	// }
	// }

	// ViewHolderHead
	public TextView tvHeadContent;
	public TextView tvHeadPubTime;

	private void initViewHolderHead(LinearLayout layoutHead) {
		tvHeadContent = (TextView) layoutHead.findViewById(R.id.tvHeadContent);
		tvHeadPubTime = (TextView) layoutHead.findViewById(R.id.tvHeadPubTime);
	}

	private void bindViewHolderHead(Head head) {
		if (head != null) {
			tvHeadContent.setText(head.content);
			tvHeadPubTime.setText(Utils
					.format(head.pubTime, "yyyy年MM月dd日 HH:mm:ss"));
		} else {
			tvHeadContent.setText(null);
			tvHeadPubTime.setText(null);
		}
	}

	// private class ViewHolderHead {
	// public TextView tvHeadContent;
	// public TextView tvHeadPubTime;
	//
	// public ViewHolderHead(LinearLayout layoutHead) {
	// tvHeadContent = (TextView) layoutHead
	// .findViewById(R.id.tvHeadContent);
	// tvHeadPubTime = (TextView) layoutHead
	// .findViewById(R.id.tvHeadPubTime);
	// }
	//
	// public void bind(Head head) {
	// tvHeadContent.setText(head.content);
	// tvHeadPubTime.setText(Utils.format(head.pubTime,
	// "yyyy年MM月dd日 HH:mm:ss"));
	// }
	// }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		OperationsAdapter2 adapter = (OperationsAdapter2) parent.getAdapter();

		if (adapter.positionInRealCount(position)) {
			if (adapter.positionIsShowMore(position)) {
				adapter.setShowMore(!adapter.isShowMore());
			} else {
				if (mOnOperationClickListener != null) {
					mOnOperationClickListener.onOperationClick(adapter
							.getItem(position));
				}
			}
		}
	}

}
