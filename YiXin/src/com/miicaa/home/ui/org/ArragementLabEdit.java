package com.miicaa.home.ui.org;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miicaa.MainActivity;
import com.miicaa.common.base.CharacterParser;
import com.miicaa.home.R;
import com.miicaa.home.data.business.account.AccountInfo;
import com.miicaa.home.data.net.ProgressMessage;
import com.miicaa.home.data.net.RequestAdpater;
import com.miicaa.home.data.net.ResponseData;
import com.miicaa.utils.AllUtils;

/**
 * Created by LM on 14-6-12.
 */
public class ArragementLabEdit extends MainActivity {
    public ArragementLabEdit labSelf;
    String mId;
    Button backButton;
    Button addButton;
    ListView mListView;
    StyleDialog dialog;
    ArrayList<allEdit> textViews;
    ArrayList<LabelData> labelDatas = new ArrayList<LabelData>();
    ArrayList<String> lableEditors = new ArrayList<String>();
    ArrayList<String> labelIds = new ArrayList<String>();
    List<TwoString> searchResultStrings;

    LinearLayout listLayout;
    RelativeLayout serchLayout;
    LableAdapter lableAdapter;
    LableGroup mViewGroup;
    TextView mSerachText;
    Button mdelText;
    String[] lableId;
    String[] userCode;
    ArrayList<HashMap<String, String>> saveLableData;
    HashMap<Integer, Boolean> checkWhat = new HashMap<Integer, Boolean>();
    HashMap<Integer, ViewHolder> holderMap = new HashMap<Integer, ViewHolder>();
    CharacterParser characterParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            lableEditors = bundle.getStringArrayList("lableEdit");
            labelIds = bundle.getStringArrayList("lableId");
            if (labelIds != null && lableEditors != null) {
                for (int i = 0; i < labelIds.size(); i++) {
                    setLabelData(lableEditors.get(i), labelIds.get(i));
                }
            }
        }
        dialog = new StyleDialog(ArragementLabEdit.this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        textViews = new ArrayList<allEdit>();
        saveLableData = new ArrayList<HashMap<String, String>>();
        lableAdapter = new LableAdapter();
        characterParser = CharacterParser.getInstance();
        searchResultStrings = new ArrayList<TwoString>();
        setContentView(R.layout.lab_edit_layout);
        mViewGroup = (LableGroup) findViewById(R.id.lab_edit_layout_group);
        backButton = (Button) findViewById(R.id.lab_edit_back_button);
        addButton = (Button) findViewById(R.id.lab_edit_add_button);
      
        mListView = (ListView) findViewById(R.id.lab_edit_listview);
        listLayout = (LinearLayout) findViewById(R.id.lab_edit_listlayout);
        serchLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.contact_list_headview, null);
        mListView.addHeaderView(serchLayout);
        mSerachText = (TextView) serchLayout.findViewById(R.id.contact_list_search_cell_edit);
        mdelText = (Button) serchLayout.findViewById(R.id.contact_list_search_cell_button);
        mListView.setAdapter(lableAdapter);
        getLableText();

        listLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        try {
                            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                                    .hideSoftInputFromWindow(ArragementLabEdit.this.getCurrentFocus()
                                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            return true;
                        } catch (Exception e) {

                            return false;
                        }
                    default:
                        break;
                }
                return false;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArragementLabEdit.this, AddLabEditor.class);
                Bundle bundle = new Bundle();
                bundle.putString("edit", "add");
                intent.putExtra("bundle", bundle);
                startActivityForResult(intent, 1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] strings = new String[labelDatas.size()];
                String[] labels = new String[labelDatas.size()];
                if (labelDatas.size() > 0) {
                    for (int i = 0; i < labelDatas.size(); i++) {
                        strings[i] = labelDatas.get(i).getLabelStr();
                        labels[i] = labelDatas.get(i).getLabelId();
                    }
                }
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArray("lables", strings);
                bundle.putStringArray("lableId", labels);
                data.putExtra("bundle", bundle);
                setResult(RESULT_OK, data);
                finish();

            }
        });

        mSerachText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (mSerachText.getText().length() >= 1) {
                    mdelText.setVisibility(View.VISIBLE);
                    mdelText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mSerachText.setText("");
                            mdelText.setVisibility(View.GONE);
                            resetText();
                        }
                    });
                    searchResult(mSerachText.getText().toString());
                } else {
                    mdelText.setVisibility(View.GONE);
                    resetText();
                }
            }
        });
    }

    private void setLabelData(String label, String id) {
        LabelData labelD = new LabelData() {
            @Override
            public void delDataClick() {
                deleteV(this);
            }
        };
        labelD.setLabelStr(label);
        labelD.setLabelId(id);
        labelDatas.add(labelD);
    }

    private void resetText() {
        textViews.clear();
        for (TwoString s : searchResultStrings) {
            allEdit editLabel = new allEdit();
            textViews.add(s.resultString);
        }
        lableAdapter.setViewTexts(textViews);
    }

    private void deleteLable(String lableId, int i) {
        String url = "/home/phone/thing/deletelabel";
        if(labelDatas != null){
            for(int n=0;n<labelDatas.size();n++){
                if(labelDatas.get(n).getLabelId().equals(lableId)){
                    mViewGroup.removeView(labelDatas.get(n).getLabelGrView());
                    if(mViewGroup.getChildCount() == 0)
                    	mViewGroup.setVisibility(View.GONE);
                    labelDatas.remove(n);
                    break;
                }
            }
        }
        new RequestAdpater() {
            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    Toast toast = Toast.makeText(ArragementLabEdit.this, "删除成功", 1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    mViewGroup.removeAllViews();
                    mViewGroup.setVisibility(View.GONE);
                    getLableText();
                } else {
                    Toast toast = Toast.makeText(ArragementLabEdit.this, "删除失败", 1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .addParam("id", lableId)
                .notifyRequest();
    }

    @Override
    public void onActivityResult(int requsetCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                switch (requsetCode) {
                    case 1:
//                        mViewGroup.removeAllViews();
//                        getOldLabel();
                        mViewGroup.removeAllViews();
                        mViewGroup.setVisibility(View.GONE);
                        getLableText();
                        break;
                }


        }
    }

    class TwoString {
        private allEdit resultString;
        private String resultPinyin;
    }

    private List<TwoString> converString() {
        List<TwoString> toStrings = new ArrayList<TwoString>();
        for (int i = 0; i < textViews.size(); i++) {
            TwoString twoString = new TwoString();
            twoString.resultPinyin = characterParser.getSelling(textViews.get(i).labelStr);
            twoString.resultString = textViews.get(i);
            toStrings.add(twoString);
        }
        return toStrings;
    }

    private void searchResult(String searchString) {
        textViews.clear();
        if (TextUtils.isEmpty(searchString)) {
            for (TwoString s : searchResultStrings) {
                textViews.add(s.resultString);
            }
        } else {

            for (TwoString s : searchResultStrings) {
                if (s.resultPinyin.toUpperCase().indexOf(characterParser.getSelling(searchString).toUpperCase()) != -1) {
                    textViews.add(s.resultString);
                }
            }
            lableAdapter.setViewTexts(textViews);
        }
    }

    private void getLableText() {

        String url = "/home/phone/thing/newlabel";
        new RequestAdpater() {

            @Override
            public void onReponse(ResponseData data) {
                if (data.getResultState() == ResponseData.ResultState.eSuccess) {
                    textViews.clear();
                    JSONArray lableArray = data.getJsonArray();
                    if (lableArray != null) {
                        lableId = new String[lableArray.length()];
                        userCode = new String[lableArray.length()];
                        for (int i = 0; i < lableArray.length(); i++) {
                            String creatorName;
                            final String lable;
                            String sId;
                            JSONObject jsonObject = lableArray.optJSONObject(i);
                            sId = jsonObject.optString("id");
                            userCode[i] = jsonObject.optString("creatorCode");
                            creatorName = jsonObject.optString("creatorName");
                            lable = jsonObject.optString("label");
                            allEdit editLabel = new allEdit();
                            editLabel.labelStr = lable + "(" + creatorName + ")";
                            editLabel.labelId = sId;
                            textViews.add(editLabel);
                            if (labelDatas != null && labelDatas.size() != 0) {
                                matGroupView(lable, sId);
                            }
                        }
                    }
                    lableAdapter.setViewTexts(textViews);
                    searchResultStrings = converString();
                    dialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(ArragementLabEdit.this, "请求列表错误！", 1);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onProgress(ProgressMessage msg) {

            }
        }.setUrl(url)
                .notifyRequest();
    }

    public void matGroupView(String lable, String sId) {

        for (int coutLab = 0; coutLab < labelDatas.size(); coutLab++) {
            if (labelDatas.get(coutLab).getLabelId().equalsIgnoreCase(sId)) {
                labelDatas.get(coutLab).setLabelGrView(setLable(lable));
            }
        }
    }

	public View setLable(String lableString) {
        View v = AllUtils.getLabelView(ArragementLabEdit.this, lableString);
        if(mViewGroup.getVisibility() != View.VISIBLE)
        	mViewGroup.setVisibility(View.VISIBLE);
        mViewGroup.addView(v);
        return v;

    }

    class LableAdapter extends BaseAdapter {

        ArrayList<allEdit> viewTexts = new ArrayList<allEdit>();

        CheckBox checkBox;
        ArrayList<String> labels = new ArrayList<String>();


        public void setViewTexts(ArrayList<allEdit> texts) {
            viewTexts = texts;
            holderMap.clear();
            checkWhat.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return viewTexts.size();

        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = new ViewHolder();
            SpannableStringBuilder spbuilder = new SpannableStringBuilder("");
            if (view == null) {
                view = LayoutInflater.from(ArragementLabEdit.this).inflate(R.layout.lable_text_layout, null);
                holder.lableText = (TextView) view.findViewById(R.id.lable_text_layout_content);
                holder.lableCheck = (CheckBox) view.findViewById(R.id.lable_text_layout_checkbox);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holderMap.put(i, holder);
            String st = viewTexts.get(i).labelStr;
            spbuilder.append(st);
            spbuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#999999")), st.lastIndexOf("("),
                    st.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.lableText.setText(spbuilder);
            holder.lableCheck.setFocusable(false);
            if (checkWhat != null && checkWhat.containsKey(i)) {
                holder.lableCheck.setChecked(checkWhat.get(i));
            } else {
                holder.lableCheck.setChecked(false);
            }
            checkBox = holder.lableCheck;
            Log.v("this is checkWhat position", checkWhat.size() + "");

            for (LabelData l : labelDatas) {
                if (l.getLabelId().equalsIgnoreCase(viewTexts.get(i).labelId)) {
                    holder.lableCheck.setChecked(true);
                    checkWhat.put(i, true);
                }
            }

            holder.lableCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkWhat == null || !checkWhat.containsKey(i) || !checkWhat.get(i)) {
                        HashMap<String, String> lableData = new HashMap<String, String>();
                        if (labelDatas.size() == 3) {
                            Toast toast = Toast.makeText(ArragementLabEdit.this, "您最多只能选择三个标签", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            checkBox.setChecked(false);
                            holderMap.get(i).lableCheck.setChecked(false);
                            return;
                        }
                        checkWhat.put(i, true);
                        LabelData labelData = new LabelData() {
                            @Override
                            public void delDataClick() {
                                deleteV(this);
                            }
                        };
                        labelData.setLabelStr(viewTexts.get(i).labelStr.substring(0,
                                viewTexts.get(i).labelStr.lastIndexOf("(")));
                        labelData.setLabelId(viewTexts.get(i).labelId);
                        labelData.setLabelGrView(setLable(viewTexts.get(i).labelStr.substring(0,
                                viewTexts.get(i).labelStr.lastIndexOf("("))));
                        labelDatas.add(labelData);
                        checkWhat.put(i, true);
                        holderMap.get(i).lableCheck.setChecked(true);
                    } else {
                        for (int j = 0; j < labelDatas.size(); j++) {
                            if (labelDatas.get(j).getLabelId().equalsIgnoreCase(viewTexts.get(i).labelId)) {
                                mViewGroup.removeView(labelDatas.get(j).getLabelGrView());
                                if(mViewGroup.getChildCount() == 0)
                                	mViewGroup.setVisibility(View.GONE);
                                labelDatas.remove(j);
                                checkWhat.remove(i);
                                holderMap.get(i).lableCheck.setChecked(false);
                            }
                        }
                    }
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkWhat == null || !checkWhat.containsKey(i) || !checkWhat.get(i)) {
                        HashMap<String, String> lableData = new HashMap<String, String>();
                        if (mViewGroup.getChildCount() == 3) {
                            Toast toast = Toast.makeText(ArragementLabEdit.this, "您最多只能选择三个标签", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            checkBox.setChecked(false);
                            holderMap.get(i).lableCheck.setChecked(false);
                            return;
                        }
                        checkWhat.put(i, true);
                        LabelData labelData = new LabelData() {
                            @Override
                            public void delDataClick() {
                                deleteV(this);
                            }
                        };
                        labelData.setLabelStr(viewTexts.get(i).labelStr.substring(0,
                                viewTexts.get(i).labelStr.lastIndexOf("(")));
                        labelData.setLabelId(viewTexts.get(i).labelId);
                        labelData.setLabelGrView(setLable(viewTexts.get(i).labelStr.substring(0,
                                viewTexts.get(i).labelStr.lastIndexOf("("))));
                        labelDatas.add(labelData);
                        checkWhat.put(i, true);
                        holderMap.get(i).lableCheck.setChecked(true);
                    } else {
                        for (int j = 0; j < labelDatas.size(); j++) {
                            if (labelDatas.get(j).getLabelId().equalsIgnoreCase(viewTexts.get(i).labelId)) {
                                mViewGroup.removeView(labelDatas.get(j).getLabelGrView());
                                if(mViewGroup.getChildCount() == 0)
                                	mViewGroup.setVisibility(View.GONE);
                                labelDatas.remove(j);
                                checkWhat.remove(i);
                                holderMap.get(i).lableCheck.setChecked(false);
                            }
                        }
                    }
                }
            });

            if (AccountInfo.instance().getLastUserInfo().getCode().equalsIgnoreCase(userCode[i])) {
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(ArragementLabEdit.this)
                                .setTitle("提示")
                                .setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int c) {
                                        if (!AccountInfo.instance().getLastUserInfo().getCode().equalsIgnoreCase(userCode[i])) {
                                            Toast toast = Toast.makeText(ArragementLabEdit.this, "只能操作自己创建的标签！", 1);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();
                                            return;
                                        }
                                        Intent intent = new Intent(ArragementLabEdit.this, AddLabEditor.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("lableText", viewTexts.get(i).labelStr);
                                        bundle.putString("lableId", viewTexts.get(i).labelId);
                                        bundle.putString("edit", "editor");
                                        intent.putExtra("bundle", bundle);
                                        startActivityForResult(intent, 1);
                                    }
                                })

                                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int c) {
                                        dialogInterface.dismiss();
                                        deleteLable(viewTexts.get(i).labelId, i);
                                    }
                                })
                                .show();
                        return true;
                    }
                });
            }

            return view;
        }

    }

    class ViewHolder {
        TextView lableText;
        CheckBox lableCheck;
    }

    class allEdit {
        String labelStr;
        String labelId;
    }

    public void deleteV(LabelData labelData) {
        mViewGroup.removeView(labelData.getLabelGrView());
        if(mViewGroup.getChildCount() == 0)
        	mViewGroup.setVisibility(View.GONE);
        labelDatas.remove(labelData);
        for (int i = 0; i < textViews.size(); i++) {
            if (labelData.getLabelId().equalsIgnoreCase(textViews.get(i).labelId)) {
                checkWhat.remove(i);
                if(holderMap.get(i)!=null) {
                    holderMap.get(i).lableCheck.setChecked(false);
                }

            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(ArragementLabEdit.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                } catch (Exception e) {

                    return false;
                }

            default:
                break;
        }
        return false;
    }

	@Override
	protected void activityYMove() {
		super.activityYMove();
		AllUtils.hiddenSoftBorad(this);
	}
    
    
}
