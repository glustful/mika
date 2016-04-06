package com.miicaa.home.ui.pay;

import java.util.ArrayList;
import java.util.Date;

import org.androidannotations.annotations.EViewGroup;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.miicaa.home.R;

@EViewGroup
public class TableLayoutPackageView extends LinearLayout {

	LinearLayout mRoot;
	ArrayList<CompoundButton> rbs;
	Context mContext;
	TextView label;
	LayoutInflater inflater;
	int selectType = -1;
	int pageType = BuyPackageActivity.PACKAGE;
	Object jsonObject;
	ArrayList<TableLayoutPackageView> relationWidgets;
	ArrayList<View> views;
	public static final int TYPE_MONTH = 0;
	public static final int TYPE_YEAR = 1;
	public static final int TYPE_DAY = 2;
	ClickListener listener;
	private long endTime = 0;

	public void setListener(ClickListener listener) {
		this.listener = listener;
	}

	public void setEndTime(long time) {
		this.endTime = time;
	}
	
	public TableLayoutPackageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		rbs = new ArrayList<CompoundButton>();
		this.mRoot = this;
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.mRoot.setOrientation(LinearLayout.VERTICAL);
		this.mRoot.setLayoutParams(new ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public TableLayoutPackageView(Context context) {
		super(context);
		rbs = new ArrayList<CompoundButton>();
		this.mRoot = this;
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.mRoot.setOrientation(LinearLayout.VERTICAL);
		this.mRoot.setLayoutParams(new ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public TableLayoutPackageView(Context context, AttributeSet attrs, int def) {
		super(context, attrs, def);
		rbs = new ArrayList<CompoundButton>();
		this.mRoot = this;
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.mRoot.setOrientation(LinearLayout.VERTICAL);
		this.mRoot.setLayoutParams(new ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}

	public void addChilds(JSONArray childs, int type) {
		if (childs == null || childs.length() < 1)
			return;

		if (rbs == null) {
			rbs = new ArrayList<CompoundButton>();
		}
		pageType = type;
		for (int i = 0; i < childs.length(); i++) {
			View line = new View(mContext);
			line.setBackgroundResource(R.color.payLine);
			this.mRoot.addView(line, new ViewGroup.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, 2));
			JSONObject obj = childs.optJSONObject(i);
			View child = null;
			switch (type) {
			case BuyPackageActivity.PACKAGE:
				child = inflate(mContext, R.layout.layout_pay_package_info,
						null);
				TextView title = (TextView) child.findViewById(R.id.title);
				TextView peopleCount = (TextView) child
						.findViewById(R.id.peopleCount);
				RadioButton peopleRB = (RadioButton) child
						.findViewById(R.id.peopleRB);
				TextView store = (TextView) child.findViewById(R.id.store);
				RadioButton storeRB = (RadioButton) child
						.findViewById(R.id.storeRB);
				String name = obj.optString("name");
				title.setText(name);
				if (name.equals("稳定型")) {
					peopleCount.setText("不限");
				} else {
					peopleCount.setText("<=" + obj.optInt("upperLimit", 0));
				}
				peopleRB.setText(mContext.getString(R.string.symbol)
						+ PayUtils.cleanZero(obj.optDouble("perMonth", 0)) + "/月");
				if (obj.isNull("space")) {
					store.setText("不限");
				} else {
					store.setText(String.format("%.2f",
							obj.optDouble("space", 0) / 1024)
							+ "GB");
				}
				storeRB.setText(mContext.getString(R.string.symbol)
						+ PayUtils.cleanZero(obj.optDouble("perYear", 0)) + "/年");
				peopleRB.setTag(R.id.tag_json, obj);
				storeRB.setTag(R.id.tag_json, obj);
				peopleRB.setTag(R.id.tag_type, TYPE_MONTH);
				storeRB.setTag(R.id.tag_type, TYPE_YEAR);
				peopleRB.setTag(R.id.tag_text, name);
				storeRB.setTag(R.id.tag_text, name);
				rbs.add(peopleRB);
				rbs.add(storeRB);
				break;
			case BuyPackageActivity.FIVEPACKAGE:
				child = inflate(mContext, R.layout.layout_pay_fivepackage_info,
						null);
				TextView ftitle = (TextView) child.findViewById(R.id.title);
				CompoundButton fiverb = (CompoundButton) child
						.findViewById(R.id.fiveRB);
				ftitle.setText(obj.optString("name"));
				fiverb.setText(mContext.getString(R.string.symbol)
						+ PayUtils.cleanZero(obj.optDouble("perMonth", 0)) + "/"+obj.optInt("upperLimit", 0)+"人/月");
				fiverb.setEnabled(false);
				fiverb.setTag(R.id.tag_json, obj);
				fiverb.setTag(R.id.tag_type, TYPE_MONTH);
				fiverb.setTag(R.id.tag_text, obj.optString("name"));
				rbs.add(fiverb);
				break;

			}
			if (child != null)
				this.mRoot.addView(child, new ViewGroup.LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		}
		View line = new View(mContext);

		line.setBackgroundResource(R.color.payLine);
		this.mRoot.addView(line, new ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, 2));

		initOnclick();
	}

	private void initOnclick() {
		for (CompoundButton rb : rbs) {
			rb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (v instanceof CompoundButton) {
						CompoundButton tmp = (CompoundButton) v;
						if (tmp.isChecked()) {
							jsonObject = tmp.getTag(R.id.tag_json);
							selectType = (Integer) tmp.getTag(R.id.tag_type);
						} else {
							jsonObject = null;
							selectType = -1;
						}

						for (CompoundButton rb : rbs) {
							if (rb != tmp)
								rb.setChecked(false);

						}
						
						if (relationWidgets != null) {
							String title = (String) tmp.getTag(R.id.tag_text);
							if (title != null && title.equals("稳定型")) {
								for (TableLayoutPackageView widget : relationWidgets) {
									widget.disEnabled(selectType);
								}
							} else {
								for (TableLayoutPackageView widget : relationWidgets) {
									widget.enabled(selectType);
								}
							}
						}
						if (label != null) {
							switch (selectType) {
							case TYPE_YEAR:
								label.setText("开通时长（年）：");
								break;
							case TYPE_MONTH:
								label.setText("开通时长（月）：");
								break;
							default:
								break;
							}
						}
						if (views != null && tmp.isChecked())
							for (View view : views) {
								view.setEnabled(true);
								view.setClickable(true);
							}
						if (views != null && !tmp.isChecked()) {
							for (View view : views) {
								view.setEnabled(false);
								view.setClickable(false);
							}
						}
						if (listener != null) {
							listener.onClick(tmp);
						}
					}

				}
			});
		}

	}

	public void setLabel(TextView tv) {
		this.label = tv;
	}

	protected void disEnabled(int type) {
		this.jsonObject = null;
		this.selectType = -1;
		for (CompoundButton rb : rbs) {
			rb.setEnabled(false);
			rb.setChecked(false);
			try {
				JSONObject obj = (JSONObject) rb.getTag(R.id.tag_json);
				rb.setTag(R.id.tag_type, type);
				switch (type) {
				case TableLayoutPackageView.TYPE_MONTH:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(obj.optDouble("perMonth", 0)) + "/"+obj.optInt("upperLimit",0)+"人/月");
					break;
				case TableLayoutPackageView.TYPE_YEAR:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(String.format("%.2f",
									obj.optDouble("perMonth", 0) * 12))
							+ "/"+obj.optInt("upperLimit",0)+"人/年");
					break;
				case TableLayoutPackageView.TYPE_DAY:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(obj.optDouble("perDay", 0)) + "/"+obj.optInt("upperLimit",0)+"人/天");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(listener!=null){
				listener.onClick(rb);
			}
		}

		if (views != null) {
			for (View v : views) {
				v.setEnabled(false);
				v.setClickable(false);
				if (v instanceof EditText) {
					((EditText) v).setText("1");
				}
			}
		}

	}

	public Object getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(Object jsonObject) {
		this.jsonObject = jsonObject;
	}

	public void enabled(int type) {

		selectType = type;
		for (CompoundButton rb : rbs) {
			rb.setEnabled(true);
			try {
				JSONObject obj = (JSONObject) rb.getTag(R.id.tag_json);
				rb.setTag(R.id.tag_type, type);
				switch (type) {
				case TableLayoutPackageView.TYPE_MONTH:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(obj.optDouble("perMonth", 0)) + "/"+obj.optInt("upperLimit",0)+"人/月");
					break;
				case TableLayoutPackageView.TYPE_YEAR:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(String.format("%.2f",
									obj.optDouble("perMonth", 0) * 12))
							+ "/"+obj.optInt("upperLimit",0)+"人/年");
					break;
				case TableLayoutPackageView.TYPE_DAY:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(obj.optDouble("perDay", 0)) + "/"+obj.optInt("upperLimit",0)+"人/天");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void defaultEnable(int type) {

		selectType = type;
		for (CompoundButton rb : rbs) {
			rb.setEnabled(true);
			if (rbs.size() == 1) {
				rb.setChecked(true);
				jsonObject = rb.getTag(R.id.tag_json);
				if (listener != null) {
					listener.onClick(rb);
				}
				if (views != null) {
					for (View v : views) {
						v.setEnabled(true);
						v.setClickable(true);
						if (v instanceof EditText) {
							EditText et = (EditText) v;
							et.setText("1");
						}
					}
				}
			}
			try {
				JSONObject obj = (JSONObject) rb.getTag(R.id.tag_json);
				rb.setTag(R.id.tag_type, type);
				switch (type) {
				case TableLayoutPackageView.TYPE_MONTH:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(obj.optDouble("perMonth", 0)) + "/"+obj.optInt("upperLimit",0)+"人/月");
					break;
				case TableLayoutPackageView.TYPE_YEAR:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(String.format("%.2f",
									obj.optDouble("perMonth", 0) * 12))
							+ "/"+obj.optInt("upperLimit",0)+"人/年");
					break;
				case TableLayoutPackageView.TYPE_DAY:
					rb.setText(mContext.getString(R.string.symbol)
							+ PayUtils.cleanZero(obj.optDouble("perDay", 0)) + "/"+obj.optInt("upperLimit",0)+"人/天");
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void addRelationWidget(TableLayoutPackageView widget) {
		if (relationWidgets == null)
			relationWidgets = new ArrayList<TableLayoutPackageView>();
		relationWidgets.add(widget);
	}

	public void addRelationWidget(View widget) {
		if (views == null)
			views = new ArrayList<View>();
		widget.setEnabled(false);
		widget.setClickable(false);
		views.add(widget);
	}

	public double getValue(int count) {
		double total = 0f;

		if (jsonObject == null || selectType == -1)
			return 0;
		if (jsonObject instanceof JSONObject) {
			JSONObject json = (JSONObject) jsonObject;

			switch (selectType) {
			case TYPE_MONTH:
				total = json.optDouble("perMonth") * count;
				break;
			case TYPE_YEAR:
				
				if (pageType==BuyPackageActivity.FIVEPACKAGE) {
					total = json.optDouble("perMonth") * 12 * count;
				} else {
					total = json.optDouble("perYear") * count;
				}
				break;
			case TYPE_DAY:
				long nowTime = new Date().getTime();
				long tmp = endTime - nowTime;
				tmp = tmp / (24 * 60 * 60 * 1000) + 1;
				total = json.optDouble("perDay") * count * tmp;
				break;
			default:
				break;
			}
		}
		return total;
	}

	public boolean isReady() {
		if (jsonObject == null || selectType == -1)
			return false;
		return true;
	}

	public interface ClickListener {
		void onClick(CompoundButton cb);
	}
}
