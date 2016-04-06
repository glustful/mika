package com.miicaa.home.ui.assessment;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;

/**
 * Created by apple on 13-12-27.
 */
public class AssessmentProcessFormActivity extends Activity {
    private Context mContext;
    private ArrayList<HashMap<String, String>> processData;
    private LinearLayout contentLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment_process_form_activity);

        contentLayout = (LinearLayout)findViewById(R.id.assessment_process_form_activity_content_layout);
        mContext = AssessmentProcessFormActivity.this;
        Bundle bundle = getIntent().getExtras();
        dataCtreat();
        viewCtreat();


        Button returnButton = (Button) findViewById(R.id.assessment_process_form_activity_back_button);
        returnButton.setOnClickListener(returnButtonClick);
    }

    private void dataCtreat() {
        processData = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 5; i++) {
            HashMap<String, String> dataHashMap = new HashMap<String, String>();
            dataHashMap.put("name", "李四");
            dataHashMap.put("comment", "同意");
            dataHashMap.put("commentContent", "同意，请做好工作交接。");
            dataHashMap.put("sendDate", "2013-12-19 10：12");
            processData.add(dataHashMap);
        }
    }

    private void viewCtreat() {
        contentLayout.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < processData.size(); i++) {
            HashMap<String, String> dataHashMap = processData.get(i);
            View itemView = layoutInflater.inflate(R.layout.assessment_process_form_cell, null);
            TextView nameTextView = (TextView) itemView.findViewById(R.id.assessment_process_form_cell_name_textview);
            nameTextView.setText(dataHashMap.get("name"));

            TextView commentTextView = (TextView) itemView.findViewById(R.id.assessment_process_form_cell_comment_textview);
            commentTextView.setText(dataHashMap.get("comment"));

            TextView contentTextView = (TextView) itemView.findViewById(R.id.assessment_process_form_cell_comment_content_textview);
            contentTextView.setText(dataHashMap.get("commentContent"));

            TextView timeTextView = (TextView) itemView.findViewById(R.id.assessment_process_form_cell_time_textview);
            timeTextView.setText(dataHashMap.get("sendDate"));

            contentLayout.addView(itemView);
        }
    }

    View.OnClickListener returnButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

}
