package org.hackerton1501.lkj.sospet;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.hackerton1501.lkj.sospet.gcm.GCMUtil;


public class ResuqestSOSActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_sos);

        Button sosBtn = (Button) findViewById(R.id.sendSOS);
        sosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GCMUtil.sendGCMPush("강아지 응급환자입니다.\n이상협 : 010-1111-2222\n개(수)14년10월");
            }
        });
    }



}
