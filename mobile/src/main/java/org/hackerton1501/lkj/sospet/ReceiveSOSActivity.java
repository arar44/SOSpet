package org.hackerton1501.lkj.sospet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ReceiveSOSActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receive_sos);

        TextView userName = (TextView) findViewById(R.id.userNameText);
        TextView userTel = (TextView) findViewById(R.id.userTelText);
        TextView petAge = (TextView) findViewById(R.id.petbirthText);
        TextView petGender = (TextView) findViewById(R.id.petGender);

        userName.setText("이상협");
        userTel.setText("010-1111-2222");
        petAge.setText("14년10월");
        petGender.setText("수");


        Button sendCall = (Button) findViewById(R.id.btnCall);
        sendCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityToCall("010-2288-4132");
            }
        });
    }

    public void startActivityToCall(String telNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
        startActivity(intent);
    }


}
