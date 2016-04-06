package com.miicaa.home.ui.org;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miicaa.home.R;

/**
 * Created by Administrator on 13-12-27.
 */
public class ArrangementComment extends Activity
{
    String mTitle;
    int mStar;
    ImageView mOButton;
    ImageView mTButton;
    ImageView mThButton;
    ImageView mFButton;
    ImageView mFiButton;
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrange_comment_activity);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mTitle = bundle.getString("title");
        initUI();
        changeStar(3);
    }

    @Override
    public void finish()
    {
        super.finish();
        overridePendingTransition(R.anim.my_slide_in_left, R.anim.my_slide_out_right);
    }

    private void initUI()
    {

        TextView textView = (TextView)findViewById(R.id.arrange_comment_id_titile);
        textView.setText("点评" + mTitle);
        Button button = (Button)findViewById(R.id.arrange_comment_id_back);
        button.setOnClickListener(onBackClick);
        button = (Button)findViewById(R.id.arrange_comment_id_commit);
        button.setOnClickListener(onCommitClick);

        mOButton= (ImageView)findViewById(R.id.arrange_comment_id_o);
        mOButton.setOnClickListener(onOClick);
        mTButton= (ImageView)findViewById(R.id.arrange_comment_id_t);
        mTButton.setOnClickListener(onTClick);
        mThButton= (ImageView)findViewById(R.id.arrange_comment_id_th);
        mThButton.setOnClickListener(onThClick);
        mFButton= (ImageView)findViewById(R.id.arrange_comment_id_f);
        mFButton.setOnClickListener(onFClick);
        mFiButton= (ImageView)findViewById(R.id.arrange_comment_id_fi);
        mFiButton.setOnClickListener(onFiClick);
    }

    private void changeStar(int star)
    {
        mStar =star;
        switch (star)
        {
            case 0:
                mOButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mTButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mThButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mFButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mFiButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                break;
            case 1:
                mOButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mTButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mThButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mFButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mFiButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                break;
            case 2:
                mOButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mTButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mThButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mFButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mFiButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                break;
            case 3:
                mOButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mTButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mThButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mFButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mFiButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                break;
            case 4:
                mOButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mTButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mThButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mFButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mFiButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                break;
            case 5:
                mOButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mTButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mThButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mFButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mFiButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                break;
            default:
                mOButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mTButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mThButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_f));
                mFButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mFiButton.setImageDrawable(getResources().getDrawable(R.drawable.star_large_uf));
                mStar = 3;
                break;
        }
    }

    View.OnClickListener onOClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mStar >= 1)
            {
                changeStar(0);
            }
            else
            {
                changeStar(1);
            }
        }
    };

    View.OnClickListener onTClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mStar >= 2)
            {
                changeStar(1);
            }
            else
            {
                changeStar(2);
            }
        }
    };

    View.OnClickListener onThClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mStar >= 3)
            {
                changeStar(2);
            }
            else
            {
                changeStar(3);
            }
        }
    };

    View.OnClickListener onFClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mStar >= 4)
            {
                changeStar(3);
            }
            else
            {
                changeStar(4);
            }
        }
    };

    View.OnClickListener onFiClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(mStar >= 5)
            {
                changeStar(4);
            }
            else
            {
                changeStar(5);
            }
        }
    };

    View.OnClickListener onBackClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    View.OnClickListener onCommitClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}