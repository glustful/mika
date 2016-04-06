package com.miicaa.home.ui.common.repeatRule;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.miicaa.home.R;

/**
 * Created by apple on 13-12-6.
 */
public class RepeatRuleSubmitTimeActivity extends Activity {
    private Button returnButton;
    private Button submitButton;
    private EditText inputEditText;
    private String repeatKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeat_rule_submit_time_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            repeatKey = bundle.getString("repeatKey");
        }
        init();
    }

    private void init() {//

        returnButton = (Button) findViewById(R.id.repeat_rule_submit_time_activity_back_button);
        returnButton.setOnClickListener(returnButtonClick);

        submitButton = (Button) findViewById(R.id.repeat_rule_submit_time_activity_save_button);
        submitButton.setOnClickListener(submitButtonClick);

        inputEditText = (EditText) findViewById(R.id.repeat_rule_submit_time_activity_edit);
    }

    View.OnClickListener returnButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };
    View.OnClickListener submitButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            bundle.putInt("repeatTimes", Integer.parseInt(inputEditText.getText().toString()));
            bundle.putString("repeatKey",repeatKey);
            setResult(RESULT_OK, getIntent().putExtras(bundle));
            finish();
            //Toast.makeText(RepeatRuleSubmitTimeActivity.this, "", Toast.LENGTH_LONG).show();
        }
    };
}
