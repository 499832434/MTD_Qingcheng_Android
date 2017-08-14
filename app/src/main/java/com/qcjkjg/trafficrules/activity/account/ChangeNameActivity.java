package com.qcjkjg.trafficrules.activity.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.qcjkjg.trafficrules.R;
import com.qcjkjg.trafficrules.activity.BaseActivity;
import com.qcjkjg.trafficrules.event.ChangeNickNameEvent;
import com.qcjkjg.trafficrules.event.CircleDataUpEvent;
import de.greenrobot.event.EventBus;

/**
 * Created by zongshuo on 2017/8/4.
 */
public class ChangeNameActivity extends BaseActivity{
    private EditText nameET;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        initView();
    }

    private void initView(){
        findViewById(R.id.closeIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nameET= (EditText) findViewById(R.id.nameET);
        nameET.setText(getUserInfo(0));

        findViewById(R.id.commitTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(nameET.getText().toString().trim())){
                    EventBus.getDefault().post(new ChangeNickNameEvent(nameET.getText().toString().trim()));
                    finish();
                }
            }
        });
    }
}
