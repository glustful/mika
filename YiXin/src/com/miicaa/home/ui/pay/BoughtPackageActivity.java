package com.miicaa.home.ui.pay;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.miicaa.home.R;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.home.data.net.ResponseData.ResultState;

@EActivity(R.layout.layout_pay_bought_package)
public class BoughtPackageActivity extends Activity {

	public static final int CURRENT_PACKAGE = 0;
	public static final int BOUGHT_PACKAGE = 1;
	@Extra
	int type;
	
	@ViewById(R.id.package_list)
	ListView list;
	@ViewById(R.id.pay_cancleButton)
	Button cancel;
	@ViewById(R.id.pay_headTitle)
	TextView title;
	@ViewById(R.id.tip)
	TextView tip;
	Context mContext;
	PackageAdapter mAdapter;
	
	@Click(R.id.pay_cancleButton)
	void cancel(){
		finish();
	}
	
	@AfterInject
	void initData(){
		this.mContext = this;
		
	}
	
	@AfterViews
	void initUI(){
		cancel.setText("返回");
		if(type==CURRENT_PACKAGE){
			tip.setVisibility(View.VISIBLE);
		title.setText("正在使用的产品");
		requestPackage(mContext.getString(R.string.pay_current_package_url));
		}else{
			tip.setVisibility(View.GONE);
			title.setText("其他已购买产品");
			requestPackage(mContext.getString(R.string.pay_bought_package_url));
		}
	}
	
	private void requestPackage(String url){
		
		PayUtils.showDialog(mContext);
		new RequestAdpater() {
			
			@Override
			public void onReponse(ResponseData data) {
				PayUtils.closeDialog();
				
				if(data.getResultState()== ResultState.eSuccess){
				
					mAdapter = new PackageAdapter(mContext,data.getJsonArray());
					list.setAdapter(mAdapter);
				}else{
					PayUtils.showToast(mContext, ""+data.getMsg(), 1000);
				}
				
			}
			
			

			@Override
			public void onProgress(ProgressMessage msg) {
				// TODO Auto-generated method stub
				
			}
		}
		.setUrl(url)
		.notifyRequest();
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.my_slide_in_left,
				R.anim.my_slide_out_right);
	}
}

class PackageAdapter extends BaseAdapter{

	JSONArray jData;
	Context mContext;
	
	public PackageAdapter(Context context,JSONArray arr){
		jData = arr;
		this.mContext = context;
	}
	
	@Override
	public int getCount() {
		
		return jData==null?0:jData.length();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return jData.opt(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_pay_bought_package_item, null);
			holder = new ViewHolder();
			holder.kind = (TextView) convertView.findViewById(R.id.pay_package_kind);
			holder.people = (TextView) convertView.findViewById(R.id.pay_package_people);
			holder.store = (TextView) convertView.findViewById(R.id.pay_package_store);
			holder.timelength = (TextView) convertView.findViewById(R.id.pay_package_timelength);
			holder.lenght_label = (TextView) convertView.findViewById(R.id.pay_package_timelength_label);
			holder.start = (TextView) convertView.findViewById(R.id.pay_package_timestart);
			holder.end = (TextView) convertView.findViewById(R.id.pay_package_timeend);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		initData(holder,jData.optJSONObject(position));
		return convertView;
	}
	
	private void initData(ViewHolder holder, JSONObject jsonObject) {
		String desc = jsonObject.isNull("goodName")?jsonObject.optString("additionalInformation"):jsonObject.optString("goodName","为空");
		
		/*if (desc != null && desc.endsWith(")个")) {
			desc = desc.replaceAll("\\)个", "个)");
			Spannable WordtoSpan = new SpannableString(desc);

			WordtoSpan.setSpan(
					new AbsoluteSizeSpan((int) holder.kind.getTextSize() -4),
					desc.lastIndexOf("("), desc.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY),
					desc.lastIndexOf("("), desc.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			holder.kind.setText(WordtoSpan);
		} else {*/
			holder.kind.setText(desc);
		//}
		holder.start.setText(PayUtils.formatData("yyyy-MM-dd", jsonObject.optLong("goodStartDate",0)));
		holder.end.setText(PayUtils.formatData("yyyy-MM-dd", jsonObject.optLong("goodEndDate",0)));
		/*if(!jsonObject.isNull("decidePersonLimit")&&jsonObject.optBoolean("decidePersonLimit")){
			holder.people.setText("<="+String.valueOf(jsonObject.optInt("upperLimit", 0)));
		}else{
			holder.people.setText("不限");
		}
		if(!jsonObject.isNull("leftSpace")){
			holder.store.setText(String.format("%.1f", jsonObject.optDouble("leftSpace")/1024));
			
		}else{
			holder.store.setText("不限");
		}*/
		if(jsonObject.optString("goodThePriceCalculation").equals("0")){
			holder.lenght_label.setText("开通时长（月）：");
			
		}else if(jsonObject.optString("goodThePriceCalculation").equals("1")){
			holder.lenght_label.setText("开通时长（年）：");
		}else if(jsonObject.optString("goodThePriceCalculation").equals("2")){
			holder.lenght_label.setText("开通时长（天）：");
		}
		holder.timelength.setText(String.valueOf(jsonObject.optInt("goodTimeQuality",0)));
	}

	class ViewHolder{
		TextView kind;
		TextView people;
		TextView store;
		TextView lenght_label;
		TextView timelength;
		TextView start;
		TextView end;
	}
}
