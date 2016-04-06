package com.miicaa.home.ui.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.common.base.BottomScreenPopup;
import com.miicaa.common.base.OnMessageListener;
import com.miicaa.common.base.PopupItem;
import com.miicaa.home.R;
import com.miicaa.home.data.OnFinish;
import com.miicaa.home.data.business.OnBusinessResponse;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.ui.calendar.CustomDate.State;
import com.miicaa.home.ui.home.FramMainActivity;
import com.miicaa.home.ui.matter.MatterBuilder;
import com.miicaa.home.ui.org.DateTimePopup;
import com.miicaa.home.ui.org.DateTimePopup.DateTimeStyle;
import com.miicaa.home.ui.org.DateTimePopup.OnDateTimeChange;
import com.yxst.epic.yixin.view.CalendarIUScrollView;
import com.yxst.epic.yixin.view.CalendarIUScrollView.OnFirstChildOnTopListener;
import com.yxst.epic.yixin.view.CalendarIUScrollView.OnGetHeightListener;
import com.yxst.epic.yixin.view.CalendarIUScrollView.OnScrollChangeListener;
import com.yxst.epic.yixin.view.CalendarIUScrollView.OnScrollEndListener;
import com.yxst.epic.yixin.view.CalendarView;
import com.yxst.epic.yixin.view.CalendarView.CalendarCallBack;

public class DitectionCalendar extends Activity implements CalendarCallBack,
		OnTouchListener {

	private Context mContext;
	private CalendarIUScrollView scrollView;
	private ListView listView;
	private CalendarAdapter adapter;
	private CalendarView canlendarView;
	private boolean flag = false;
	private LinearLayout list_parent;
	private RelativeLayout calendar_day_paretn;
	private TextView tvDate, day_date;
	private View empty;
	private Button back_btn;
	private RelativeLayout day_date_parent;
	private ArrayList<CalendarEntity> entitys = null;
	private ArrayList<CalendarEntity> adapterData = null;
	private Calendar selectedTime = null;
	private CustomDate mclickDate = null;

	private OnFinish onFinish = new OnFinish() {

		@Override
		public void onSuccess(JSONObject res) {
			((TextView) empty).setText("今天暂无计划，休息一下");

		}

		@Override
		public void onFailed(String msg) {
			((TextView) empty).setText("加载数据出错");
			Toast.makeText(DitectionCalendar.this, "请求出错，请稍后重试",
					Toast.LENGTH_SHORT).show();

		}
	};
	private OnBusinessResponse onResponse = new OnBusinessResponse() {
		@Override
		public void onProgress(ProgressMessage msg) {
		}

		@Override
		public void onResponse(ResponseData data) {

			onFinish.onSuccess(null);
			if (data.getResultState() == ResponseData.ResultState.eSuccess) {
				try {
					JSONArray array = data.getJsonArray();
					entitys = new ArrayList<CalendarEntity>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						CalendarEntity entity = new CalendarEntity();
						entity.setTitle(obj.getString("title"));
						try {
							entity.setPlanTime(obj.getLong("planTime"));
						} catch (Exception e) {
							entity.setPlanTime(0);
						}
						try {
							entity.setPlanTimeEnd(obj.getLong("planTimeEnd"));
						} catch (Exception e) {
							entity.setPlanTimeEnd(0);
						}
						String status = obj.getString("status");
						long endTime = 0;
						try {
							endTime = obj.getLong("planTimeEnd");
						} catch (Exception e) {
							endTime = 0;
						}
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						System.out.println(entity.getTitle()+"="+sdf.format(new Date(endTime)));
						entity.setmColor(status, endTime);
						entity.setId(obj.getString("id"));
						entity.setDataType(obj.getString("dataType"));
						entity.setOperateGroup(obj.getString("operateGroup"));
						entity.setStatus(status);
						entitys.add(entity);
					}
					if (!listView.getTag().toString()
							.equals(canlendarView.getEarlyDate()))
						return;
					if (DateUtil.isCurrentMonth(new CustomDate())) {
						notifyDataChange(mclickDate == null ? new CustomDate()
								: mclickDate, false);
					} else {
						notifyDataChange(null, false);
					}
				} catch (Exception e) {

				}
			} else {
				onFinish.onFailed(data.getMsg());
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.detection_calendar);
		mContext = this;
		init();

	}

	private void init() {
		this.canlendarView = (CalendarView) findViewById(R.id.detection_calendar_calendarview);
		this.canlendarView.setCallBack(this);
		scrollView = (CalendarIUScrollView) findViewById(R.id.detection_calendar_scroll);
		tvDate = (TextView) findViewById(R.id.detection_calendar_date);
		tvDate.setText(this.canlendarView.getMShowDate());
		day_date_parent = (RelativeLayout) findViewById(R.id.calendar_day_parent);
		
		day_date = (TextView) findViewById(R.id.calendar_day_date);
		day_date.setText(this.canlendarView.getMShowTimeDate());
		listView = (ListView) findViewById(R.id.detection_calendar_list);
		list_parent = (LinearLayout) findViewById(R.id.list_parent);
		calendar_day_paretn = (RelativeLayout) findViewById(R.id.calendar_day_parent);
		empty = findViewById(android.R.id.empty);
		listView.setEmptyView(empty);

		listView.setAdapter(null);

		back_btn = (Button) findViewById(R.id.cancleButton);
		back_btn.setVisibility(View.VISIBLE);
		back_btn.setOnTouchListener(this);
		findViewById(R.id.commitButton).setOnTouchListener(this);
		findViewById(R.id.detection_calendar_day).setOnTouchListener(this);
		findViewById(R.id.calendar_month_left).setOnTouchListener(this);
		findViewById(R.id.detection_calendar_center).setOnTouchListener(this);
		findViewById(R.id.calendar_month_right).setOnTouchListener(this);
		findViewById(R.id.detection_calendar_yet).setOnTouchListener(this);
		findViewById(R.id.calendar_day_left).setOnTouchListener(this);
		// findViewById(R.id.calendar_day_date).setOnTouchListener(this);
		findViewById(R.id.calendar_day_right).setOnTouchListener(this);
		tvDate.setOnTouchListener(this);
		scrollView.setOnGetHeightListener(new OnGetHeightListener() {

			@Override
			public void onGetHeight(int height) {
				
				View view = list_parent;

				LinearLayout.LayoutParams params = (LayoutParams) view
						.getLayoutParams();
				params.height = height;
				view.setLayoutParams(params);
				
			}
		});

		scrollView
				.setOnFirstChildOnTopListener(new OnFirstChildOnTopListener() {

					@Override
					public boolean onFirstChildOnTop() {

						try {
							return listView.getFirstVisiblePosition() == 0
									&& list_parent.getChildAt(0).getTop() == 0;
						} catch (Exception e) {
							return true;
						}
					}

				});
		scrollView.setOnScrollChangeListener(new OnScrollChangeListener() {

			@Override
			public void onScrollChange(View v, View view, int l, int t,
					int oldl, int oldt) {
				int dely = t - oldt;

				if (dely > 0) {

					if (Math.abs(view.getHeight() - v.getScrollY()) < 10) {
						initHeight();
						flag = true;
					}
				} else if (dely < 0) {
					
					if (!flag) {
						flag = false;
						hidden();
					}
				}

			}
		});
		this.scrollView.setOnScrollEndListener(new OnScrollEndListener() {

			@Override
			public boolean onScrollEnd(View v, View view, int y) {

				if (y > 0) {

					initHeight();

					flag = true;
				} else if (y < 0) {

					hidden();
				} else {
					flag = false;
				}
				return true;
			}
		});
		invadeDate(false);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}

	@Override
	public void clickDate(CustomDate date) {
		this.mclickDate = date;
		System.out.println("state=" + date.getState());
		if (date.getState() == State.PAST_MONTH_DAY) {
			this.selectedTime = Calendar.getInstance();
			this.selectedTime.set(Calendar.YEAR, date.year);
			this.selectedTime.set(Calendar.MONTH, date.month - 1);
			this.selectedTime.set(Calendar.DAY_OF_MONTH, date.day);
			skipDate(false);
		} else if (date.getState() == State.NEXT_MONTH_DAY) {

			this.selectedTime = Calendar.getInstance();
			this.selectedTime.set(Calendar.YEAR, date.year);
			this.selectedTime.set(Calendar.MONTH, date.month - 1);
			this.selectedTime.set(Calendar.DAY_OF_MONTH, date.day);
			skipDate(false);
		} else {

			notifyDataChange(date, false);
		}
		day_date.setText(this.canlendarView.getMShowTimeDate());

	}

	private void notifyDataChange(CustomDate date, boolean isDay) {
		if (entitys == null) {
			invadeDate(isDay);
			return;
		}

		if (date == null) {
			this.listView.setAdapter(null);
			return;
		}
		adapterData = new ArrayList<CalendarEntity>();
		for (CalendarEntity entity : entitys) {
			if (entity.isChecked(date)) {
				adapterData.add(entity);
			}
		}
		if (adapterData.size() < 1) {
			this.listView.setAdapter(null);
			return;
		}

		adapter = new CalendarAdapter(mContext, adapterData);
		this.listView.setAdapter(adapter);
	}

	/*
	 * 点击事件，以控件的tag来标识，从上到下，从左到右从下标0开始
	 */
	public void onclick(View view) {

		switch (view.getId()) {
		case R.id.cancleButton:// 返回
			finish();
			break;
		case R.id.commitButton:// 新建
			createButtonClick();
			break;
		case R.id.detection_calendar_day:// 今天
			this.mclickDate = new CustomDate();
			this.mclickDate.setState(State.CURRENT_MONTH_DAY);
			if (!this.canlendarView.backToday()) {

				invadeDate(false);
			} else {
				this.scrollView.setScrollY(0);
				tvDate.setText(this.canlendarView.getMShowDate());
				day_date.setText(this.canlendarView.getMShowTimeDate());
				calendar_day_paretn.setVisibility(View.GONE);
				notifyDataChange(this.mclickDate, false);
			}
			break;
		case R.id.calendar_month_left:// 月左键头
			this.canlendarView.leftSilde();
			invadeDate(false);
			break;
		case R.id.detection_calendar_date:// 日期标题
			selectTime();
			break;
		case R.id.calendar_month_right:// 月右键头
			this.canlendarView.rightSilde();
			invadeDate(false);
			break;
		case R.id.detection_calendar_yet:// 未计划的
			Intent intent = new Intent(mContext, FramCalendarActivity.class);
	           
            mContext.startActivity(intent);
            ((Activity)mContext).overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
			break;
		case R.id.calendar_day_left:// 日左键头
			mclickDate = this.canlendarView.getLeftDate();
			System.out.println(mclickDate.toString());
			if (DateUtil.isMiddleMonth(mclickDate, -1)) {
				day_date.setText(this.canlendarView.getMShowTimeDate());
				notifyDataChange(mclickDate, true);
			} else {
				this.selectedTime = Calendar.getInstance();
				this.selectedTime.set(mclickDate.year, mclickDate.month - 1,
						mclickDate.day);
				skipDate(true);
			}
			break;
		case R.id.calendar_day_right:// 日右键头
			mclickDate = this.canlendarView.getRightDate();
			if (DateUtil.isMiddleMonth(mclickDate, 1)) {
				day_date.setText(this.canlendarView.getMShowTimeDate());
				notifyDataChange(mclickDate, true);
			} else {
				this.selectedTime = Calendar.getInstance();
				this.selectedTime.set(mclickDate.year, mclickDate.month - 1,
						mclickDate.day);
				skipDate(true);
			}
			break;
		default:
			break;
		}
	}

	void createButtonClick() {
		ArrayList<com.miicaa.common.base.PopupItem> items = new ArrayList<PopupItem>();
		items.add(new PopupItem("任务", "arrangement"));
		items.add(new PopupItem("审批", "approval"));
		items.add(new PopupItem("工作报告", "report"));
		items.add(new PopupItem("取消", "cancel"));
		BottomScreenPopup.builder(DitectionCalendar.this)
		.setDrawable(R.drawable.white_color_selector)
		
		.setMargin(flag)
		.setItems(items)
				.setOnMessageListener(new OnMessageListener() {
					@Override
					public void onClick(PopupItem msg) {
						if (msg.mCode.equals("arrangement")) {
							Intent intent = new Intent(DitectionCalendar.this,
									MatterBuilder.class);
							Bundle bundle = new Bundle();
							bundle.putString("dataType", "1");
							intent.putExtra("bundle", bundle);
							DitectionCalendar.this.startActivity(intent);
							((Activity) DitectionCalendar.this)
									.overridePendingTransition(
											R.anim.my_slide_in_right,
											R.anim.my_slide_out_left);
						} else if (msg.mCode.equals("approval")) {
							Intent intent = new Intent(DitectionCalendar.this,
									MatterBuilder.class);
							Bundle bundle = new Bundle();
							bundle.putString("dataType", "2");
							intent.putExtra("bundle", bundle);
							DitectionCalendar.this.startActivity(intent);
							((Activity) DitectionCalendar.this)
									.overridePendingTransition(
											R.anim.my_slide_in_right,
											R.anim.my_slide_out_left);
						}else if(msg.mCode.equals("report")){
							 Intent intent = new Intent(DitectionCalendar.this, MatterBuilder.class);
	                         Bundle bundle = new Bundle();
	                         bundle.putString("dataType","3");
	                         intent.putExtra("bundle",bundle);
	                         startActivity(intent);
	                         overridePendingTransition(R.anim.my_slide_in_right, R.anim.my_slide_out_left);
						}
					}
				}).show();
	}

	void selectTime() {

		DateTimeStyle stype = DateTimeStyle.eRemind;
		DateTimePopup.builder(DitectionCalendar.this).setDateTimeStyle(stype)
				.setOnMessageListener(new OnMessageListener() {

					@Override
					public void onClick(PopupItem msg) {

						if ("commit".equals(msg.mCode)) {
							skipDate(false);
						}
					}

				}).setOnDateTimeChangeListener(new OnDateTimeChange() {

					@Override
					public void onDateTimeChange(Calendar c, DateTimeStyle style) {
						selectedTime = c;

					}
				}).show(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	protected void skipDate(boolean isDay) {
		if (selectedTime == null)
			this.selectedTime = Calendar.getInstance();
		if (!isDay)
			if (this.scrollView.getScrollY() > 0)
				this.scrollView.setScrollY(0);

		this.entitys = null;
		if(this.adapterData!=null)
		this.adapterData.clear();
		if(this.adapter != null)
		this.adapter.notifyDataSetChanged();
		tvDate.setText(this.selectedTime.get(Calendar.YEAR) + "年"
				+ (this.selectedTime.get(Calendar.MONTH) + 1) + "月");
		mclickDate = this.canlendarView.skipDate(this.selectedTime);
		day_date.setText(this.canlendarView.getMShowTimeDate());
		invadeDate(isDay);
	}

	private void invadeDate(boolean isDay) {
		if (!isDay) {
			this.scrollView.setScrollY(0);
			calendar_day_paretn.setVisibility(View.GONE);
		}
		tvDate.setText(this.canlendarView.getMShowDate());
		day_date.setText(this.canlendarView.getMShowTimeDate());
		this.listView.setAdapter(null);
		((TextView) this.empty).setText("正在加载数据");
		this.listView.setTag(this.canlendarView.getEarlyDate());
		CalendarRequest.requestMessage(onResponse, onFinish,
				this.canlendarView.getEarlyDate(),
				this.canlendarView.getLastDate());
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			
			onclick(v);
		}
		return true;
	}

	protected void hidden() {
		
		calendar_day_paretn.setVisibility(View.GONE);
	}

	private void initHeight() {
		
		calendar_day_paretn.setVisibility(View.VISIBLE);
	}
}
