package com.miicaa.home.ui.org;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-6.
 */
public class AutoDoneEdit extends Activity
{
    String mContent;

    Button mYesButton;
    Button mNoButton;
    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_done_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mContent = bundle.getString("content");
        initUI();
        changeState();
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left,R.anim.my_slide_out_right);
    }

    private void initUI()
    {
        Button button = (Button)findViewById(R.id.auto_done_id_back);
        button.setOnClickListener(onBackClick);

        mYesButton = (Button)findViewById(R.id.auto_done_id_yes);
        mYesButton.setOnClickListener(onYesClick);
        mNoButton = (Button)findViewById(R.id.auto_done_id_no);
        mNoButton.setOnClickListener(onNoClick);
    }

    private void changeState()
    {
        if(mContent.equals("yes"))
        {
            mYesButton.setSelected(true);
            mNoButton.setSelected(false);
        }
        else
        {
            mYesButton.setSelected(false);
            mNoButton.setSelected(true);
        }
    }

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view)
        {
            finish();
        }
    };

    View.OnClickListener onYesClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mContent = "yes";
            changeState();
        }
    };

    View.OnClickListener onNoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mContent = "no";
            changeState();
        }
    };
}