package com.miicaa.home.ui.person;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-11-27.
 */
public class PhoneCall extends Activity
{
    String mCallPhoneNum;
    String mPhoneNum;

    LinearLayout mCallPhoneLayout;
    TextView mCallPhoneText;
    LinearLayout mPhoneLayout;
    TextView mPhoneText;

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_phone_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mCallPhoneNum = bundle.getString("callPhone");
        mPhoneNum = bundle.getString("phone");
        initUI();
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left,R.anim.my_slide_out_right);


    }

    private void initUI()
    {
        Button button = (Button)findViewById(R.id.phone_id_back);
        button.setOnClickListener(onBackClick);

        mCallPhoneLayout = (LinearLayout)findViewById(R.id.phone_id_call_phone_layout);
        mCallPhoneText = (TextView)findViewById(R.id.phone_id_call_phone);
        mCallPhoneLayout.setOnClickListener(onCallPhoneClick);
        mCallPhoneText.setText(mCallPhoneNum);

        mPhoneLayout = (LinearLayout)findViewById(R.id.phone_id_phone_layout);
        mPhoneText = (TextView)findViewById(R.id.phone_id_phone);
        mPhoneLayout.setOnClickListener(onPhoneClick);
        mPhoneText.setText(mPhoneNum);
    }

    View.OnClickListener onBackClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            PhoneCall.this.finish();
        }
    };

    View.OnClickListener onCallPhoneClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(
                    Intent.ACTION_DIAL, Uri.parse("tel:" + mCallPhoneNum));
            startActivity(intent);
        }
    };

    View.OnClickListener onPhoneClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(
                    Intent.ACTION_DIAL, Uri.parse("tel:" + mPhoneNum));
            startActivity(intent);
        }
    };
}