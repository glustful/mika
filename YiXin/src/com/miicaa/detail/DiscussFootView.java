package com.miicaa.detail;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.TextChange;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint.FontMetrics;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.miicaa.home.R;
import com.miicaa.home.ui.org.AtSreachPopup;
import com.miicaa.home.ui.org.ExpressPageAdpater;
import com.miicaa.home.ui.org.Expression;
import com.miicaa.home.ui.org.ExpressionImageAdapter;
import com.miicaa.utils.AllUtils;

@EViewGroup(R.layout.discuss_foot_view)
public class DiscussFootView extends LinearLayout {

	public static DiscussFootView instance;
	OnDiscussClickListener linstener;
	String name;
	int position;
	/*
	 * 回复某人的评论
	 */
	Boolean isDiscussDicuss = false;
	int mExpColumns = 6;
	int mExpRows = 3;
	int mExpCount = mExpColumns * mExpRows;
	ExpressPageAdpater expressadapter;
	static List<Expression> mExpIconArray;
	ArrayList<GridView> mGrids;

	Context context;
	@ViewById
	ImageView add;
	@ViewById
	EditText input;
	@ViewById(R.id.express)
	LinearLayout express;
	@ViewById(R.id.expressPage)
	ViewPager expressPage;
	@ViewById(R.id.expressLayout)
	LinearLayout expressLayout;
	@ViewById(R.id.function)
	LinearLayout function;
	@ViewById(R.id.atButton)
	ImageButton atButton;
	@ViewById(R.id.expreButton)
	ImageButton expreButton;

	public DiscussFootView(Context context) {
		super(context);
		this.context = context;
		instance = this;
		// TODO Auto-generated constructor stub
	}

	public DiscussFootView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		instance = this;
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	public DiscussFootView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		instance = this;
		// TODO Auto-generated constructor stub
	}

	public static DiscussFootView getInstance() {
		return instance;
	}

	@AfterViews
	void afterview() {
		// expressPage.setOnPageChangeListener(new OnPageChangeListh());
		expressLayout.setVisibility(View.GONE);
		initExpressionIcon();
		initExpresGrid();
	}

	@TextChange(R.id.input)
	void textchange() {

	}

	@Click(R.id.send)
	void sendClick() {
		AllUtils.hiddenSoftBorad(context);
		if (function.isShown()) {
			function.setVisibility(View.GONE);
		}if(expressLayout.isShown()){
			expressLayout.setVisibility(View.GONE);
		}
		String content = input.getText().toString();
		if ("".equals(content.trim())) {
			return;
		}
		if (isDiscussDicuss) {
			linstener.sendDiscussDiscussClick(position, content);
			isDiscussDicuss = !isDiscussDicuss;
		} else {
			linstener.sendClick(input.getText().toString());
		}
		input.setText("");
	}

	@Click(R.id.add)
	void addClick() {
		/*
		 * 收起键盘
		 */
		AllUtils.hiddenSoftBorad(context);
		if (function.isShown()) {
			function.setVisibility(View.GONE);
		} else {
			function.setVisibility(View.VISIBLE);
			expreButton.setVisibility(View.VISIBLE);
			atButton.setVisibility(View.VISIBLE);
		}
		if (express.isShown()) {
			expressLayout.setVisibility(View.GONE);
		}
		linstener.addClick();
	}

	@Click(R.id.expreButton)
	void expreButtonClick() {
		AllUtils.hiddenSoftBorad(context);
		atButton.setVisibility(View.GONE);
		expreButton.setVisibility(View.GONE);
		expressLayout.setVisibility(View.VISIBLE);
	}

	@Click(R.id.atButton)
	void atButtonClick() {
		// atButton.setVisibility(View.GONE);
		atClick();
	}

	public interface OnDiscussClickListener {
		void sendClick(String content);

		void sendDiscussDiscussClick(int position, String content);

		void addClick();
	}

	public void setOnDiscussClickListener(OnDiscussClickListener listener) {
		this.linstener = listener;
	}

	public static void initExpressionIcon() {

		if (mExpIconArray != null) {
			return;
		}
		mExpIconArray = new ArrayList<Expression>();
		Expression exp1 = new Expression(R.drawable.e_1, "[#i_f01]");
		Expression exp2 = new Expression(R.drawable.e_2, "[#i_f02]");
		Expression exp3 = new Expression(R.drawable.e_3, "[#i_f03]");
		Expression exp4 = new Expression(R.drawable.e_4, "[#i_f04]");
		Expression exp5 = new Expression(R.drawable.e_5, "[#i_f05]");
		Expression exp6 = new Expression(R.drawable.e_6, "[#i_f06]");
		Expression exp7 = new Expression(R.drawable.e_7, "[#i_f07]");
		Expression exp8 = new Expression(R.drawable.e_8, "[#i_f08]");
		Expression exp9 = new Expression(R.drawable.e_9, "[#i_f09]");
		Expression exp10 = new Expression(R.drawable.e_10, "[#i_f10]");

		Expression exp11 = new Expression(R.drawable.e_11, "[#i_f11]");
		Expression exp12 = new Expression(R.drawable.e_12, "[#i_f12]");
		Expression exp13 = new Expression(R.drawable.e_13, "[#i_f13]");
		Expression exp14 = new Expression(R.drawable.e_14, "[#i_f14]");
		Expression exp15 = new Expression(R.drawable.e_15, "[#i_f15]");
		Expression exp16 = new Expression(R.drawable.e_16, "[#i_f16]");
		Expression exp17 = new Expression(R.drawable.e_17, "[#i_f17]");
		Expression exp18 = new Expression(R.drawable.e_18, "[#i_f18]");
		Expression exp19 = new Expression(R.drawable.e_19, "[#i_f19]");
		Expression exp20 = new Expression(R.drawable.e_20, "[#i_f20]");

		Expression exp21 = new Expression(R.drawable.e_21, "[#i_f21]");
		Expression exp22 = new Expression(R.drawable.e_22, "[#i_f22]");
		Expression exp23 = new Expression(R.drawable.e_23, "[#i_f23]");
		Expression exp24 = new Expression(R.drawable.e_24, "[#i_f24]");
		Expression exp25 = new Expression(R.drawable.e_25, "[#i_f25]");
		Expression exp26 = new Expression(R.drawable.e_26, "[#i_f26]");
		Expression exp27 = new Expression(R.drawable.e_27, "[#i_f27]");
		Expression exp28 = new Expression(R.drawable.e_28, "[#i_f28]");
		Expression exp29 = new Expression(R.drawable.e_29, "[#i_f29]");
		Expression exp30 = new Expression(R.drawable.e_30, "[#i_f30]");

		Expression exp31 = new Expression(R.drawable.e_31, "[#i_f31]");
		Expression exp32 = new Expression(R.drawable.e_32, "[#i_f32]");
		Expression exp33 = new Expression(R.drawable.e_33, "[#i_f33]");
		Expression exp34 = new Expression(R.drawable.e_34, "[#i_f34]");
		Expression exp35 = new Expression(R.drawable.e_35, "[#i_f35]");
		Expression exp36 = new Expression(R.drawable.e_36, "[#i_f36]");
		Expression exp37 = new Expression(R.drawable.e_37, "[#i_f37]");
		Expression exp38 = new Expression(R.drawable.e_38, "[#i_f38]");
		Expression exp39 = new Expression(R.drawable.e_39, "[#i_f39]");
		Expression exp40 = new Expression(R.drawable.e_40, "[#i_f40]");

		Expression exp41 = new Expression(R.drawable.e_41, "[#i_f41]");
		Expression exp42 = new Expression(R.drawable.e_42, "[#i_f42]");
		Expression exp43 = new Expression(R.drawable.e_43, "[#i_f43]");
		Expression exp44 = new Expression(R.drawable.e_44, "[#i_f44]");
		Expression exp45 = new Expression(R.drawable.e_45, "[#i_f45]");
		Expression exp46 = new Expression(R.drawable.e_46, "[#i_f46]");
		Expression exp47 = new Expression(R.drawable.e_47, "[#i_f47]");
		Expression exp48 = new Expression(R.drawable.e_48, "[#i_f48]");
		Expression exp49 = new Expression(R.drawable.e_49, "[#i_f49]");
		Expression exp50 = new Expression(R.drawable.e_50, "[#i_f50]");

		Expression exp51 = new Expression(R.drawable.e_51, "[#i_f51]");

		mExpIconArray.add(exp1);
		mExpIconArray.add(exp2);
		mExpIconArray.add(exp3);
		mExpIconArray.add(exp4);
		mExpIconArray.add(exp5);
		mExpIconArray.add(exp6);
		mExpIconArray.add(exp7);
		mExpIconArray.add(exp8);
		mExpIconArray.add(exp9);
		mExpIconArray.add(exp10);

		mExpIconArray.add(exp11);
		mExpIconArray.add(exp12);
		mExpIconArray.add(exp13);
		mExpIconArray.add(exp14);
		mExpIconArray.add(exp15);
		mExpIconArray.add(exp16);
		mExpIconArray.add(exp17);
		mExpIconArray.add(exp18);
		mExpIconArray.add(exp19);
		mExpIconArray.add(exp20);

		mExpIconArray.add(exp21);
		mExpIconArray.add(exp22);
		mExpIconArray.add(exp23);
		mExpIconArray.add(exp24);
		mExpIconArray.add(exp25);
		mExpIconArray.add(exp26);
		mExpIconArray.add(exp27);
		mExpIconArray.add(exp28);
		mExpIconArray.add(exp29);
		mExpIconArray.add(exp30);

		mExpIconArray.add(exp31);
		mExpIconArray.add(exp32);
		mExpIconArray.add(exp33);
		mExpIconArray.add(exp34);
		mExpIconArray.add(exp35);
		mExpIconArray.add(exp36);
		mExpIconArray.add(exp37);
		mExpIconArray.add(exp38);
		mExpIconArray.add(exp39);
		mExpIconArray.add(exp40);

		mExpIconArray.add(exp41);
		mExpIconArray.add(exp42);
		mExpIconArray.add(exp43);
		mExpIconArray.add(exp44);
		mExpIconArray.add(exp45);
		mExpIconArray.add(exp46);
		mExpIconArray.add(exp47);
		mExpIconArray.add(exp48);
		mExpIconArray.add(exp49);
		mExpIconArray.add(exp50);

		mExpIconArray.add(exp51);

	}

	@UiThread
	void initExpresGrid() {
		List<List<Expression>> lists = initGridViewData();
		mGrids = new ArrayList<GridView>();

		int gv_padding_lr = (int) getResources().getDimension(
				R.dimen.chat_gv_padding_lr);
		int gv_padding_bt = (int) getResources().getDimension(
				R.dimen.chat_gv_padding_bt);
		int gv_spacing = (int) getResources().getDimension(
				R.dimen.chat_gv_spacing);
		int chat_dot_margin_lr = (int) getResources().getDimension(
				R.dimen.chat_dot_margin_lr);
		int chat_dot_wh = (int) getResources()
				.getDimension(R.dimen.chat_dot_wh);

		for (int i = 0; i < lists.size(); i++) {
			List<Expression> l = lists.get(i);
			if (null != l) {
				final GridView gv = new GridView(context);
				gv.setLayoutParams(new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT));
				gv.setNumColumns(mExpColumns);
				gv.setGravity(Gravity.CENTER);
				gv.setPadding(gv_padding_lr, gv_padding_bt, gv_padding_lr, 0);
				gv.setHorizontalSpacing(gv_spacing);
				gv.setVerticalSpacing(gv_spacing);
				ExpressionImageAdapter expressionImageAdapter = new ExpressionImageAdapter(
						context, l);
				gv.setAdapter(expressionImageAdapter);
				gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						Expression e = (Expression) gv.getItemAtPosition(arg2);
						int cusor = input.getSelectionStart();
						Editable edit = input.getEditableText();
						
						if (e.getCode().equals("[#i_f00]")) {
							deleteLast(cusor, edit);
						} else {
							String content_all = edit.toString();
							 TextPaint mPaint = input.getPaint();
						        FontMetrics fm = mPaint.getFontMetrics();         
						        int m_iFontHeight = (int) ((int) Math.ceil(fm.descent - fm.ascent)*context.getResources().getDisplayMetrics().density);
						     Bitmap resource = BitmapFactory
										.decodeResource(getResources(), e.drableId);   
							Bitmap bp = ThumbnailUtils.extractThumbnail(resource,m_iFontHeight, m_iFontHeight);
							
							
							@SuppressWarnings("deprecation")
							ImageSpan imageSpan = new ImageSpan(bp,DynamicDrawableSpan.ALIGN_BOTTOM);
							
							SpannableString spannableString = new SpannableString(
									e.code);
							spannableString.setSpan(imageSpan, 0,
									e.code.length(),
									Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

							if (cusor < 0 || cusor >= edit.length()) {
								edit.append(spannableString);
							} else {
								edit.insert(cusor, spannableString);
							}
						}
					}
				});

				mGrids.add(gv);

				ImageView iv = new ImageView(context);
				android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
						chat_dot_wh, chat_dot_wh);
				lp.leftMargin = chat_dot_margin_lr;
				lp.rightMargin = chat_dot_margin_lr;
				iv.setLayoutParams(lp);
				if (i == 0) {
					iv.setBackgroundResource(R.drawable.page_focused);
				} else {
					iv.setBackgroundResource(R.drawable.page_unfocused);
				}
				express.addView(iv);

			}
		}
		expressadapter = new ExpressPageAdpater(mGrids);
		expressPage.setAdapter(expressadapter);
	}

	protected void deleteLast(int cusor, Editable edit) {
		if (cusor <= 0)
			return;
		if (cusor < 8) {
			edit.delete(cusor - 1, cusor);
			return;
		}
		char[] dest = new char[8];
		edit.getChars(cusor - 8, cusor, dest, 0);
		String s = String.valueOf(dest);
		if (s.matches("\\[#i_f[0-9]{2}\\]")) {
			edit.delete(cusor - 8, cusor);
		} else {
			edit.delete(cusor - 1, cusor);
		}
	}

	private List<List<Expression>> initGridViewData() {
		List<List<Expression>> lists = new ArrayList<List<Expression>>();
		List<Expression> list = null;
		for (int i = 0; i < mExpIconArray.size(); i++) {
			if (i % (mExpCount - 1) == 0) {
				if (lists.size() > 0) {
					lists.get(lists.size() - 1).add(
							new Expression(R.drawable.e_0, "[#i_f00]"));
				}
				list = new ArrayList<Expression>();
				lists.add(list);
			}
			list.add(mExpIconArray.get(i));
		}
		if (lists.size() > 0) {
			lists.get(lists.size() - 1).add(
					new Expression(R.drawable.e_0, "[#i_f00]"));
		}
		return lists;
	}

	public void setName(int position, String name) {
		SpannableString sp = atStr(name);
		int index = input.getSelectionStart();
		Editable edit = input.getEditableText();
		edit.clear();
		edit.append(sp);
		this.name = name;
		this.position = position;
		isDiscussDicuss = true;
	}

	/*
	 * 老版本代码
	 */
	void handleAtUser(AtSreachPopup.AtUserInfo userInfo, EditText mMsgEdit) {
		int index = mMsgEdit.getSelectionStart();
		Editable edit = mMsgEdit.getEditableText();
		if (userInfo.mAtType == "custom") {
			edit.insert(index, userInfo.mAtName);
		} else {
			AtUserEdit ue = new AtUserEdit();
			ue.mInfo = userInfo;
			ue.mSp = userInfo.mAtName;
			SpannableString sp = atStr("@" + ue.mSp);
			// sp.setSpan(new AtEditSpan(ue), 0, sp.length() - 1,
			// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			edit.insert(index, sp);
		}
	}

	void atClick() {
		AtSreachPopup.builder(context)
				.setOnDismissListener(new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						AllUtils.hiddenSoftBorad(context);
					}
				}).setOnSreachListener(new AtSreachPopup.OnSreachListener() {
					@Override
					public void onClick(AtSreachPopup.AtUserInfo r) {
						handleAtUser(r, input);
					}
				}).show();
		// setFunctionType("discuss");
	}

	SpannableString atStr(String name) {
		SpannableString sp = new SpannableString(name + " ");
		// sp.setSpan(new AtEditSpan(ue), 0, sp.length() - 1,
		// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new AtClickSpan(null, null), name.indexOf("@"),
				sp.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}

	void setAtName(String name, EditText msgEdit) {

	}

	class AtUserEdit {
		AtSreachPopup.AtUserInfo mInfo;
		public String mSp;
	}

	class SoftHandler extends Handler {
		public final static int msg_hidden = 1;

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stup
			switch (msg.what) {
			case msg_hidden:
				if (function.isShown())
					function.setVisibility(View.GONE);
				if (expressLayout.isShown()) {
					expressLayout.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}

	}

	SoftHandler handler = new SoftHandler();
	Boolean isShowSoftBorad = false;

	void howTodo() {
		Message msg = new Message();
		msg.what = SoftHandler.msg_hidden;
		handler.sendMessage(msg);
	}

	public void setIsHidden(Boolean b) {
		isShowSoftBorad = b;
		if (isShowSoftBorad) {
			howTodo();
		}
	}

}
