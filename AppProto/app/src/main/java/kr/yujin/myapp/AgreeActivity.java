package kr.yujin.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AgreeActivity extends Activity implements OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agree);
    }
    @Override
    public void onClick(View v){
        Intent intent = new Intent (AgreeActivity.this, MainActivity.class);
        startActivity(intent); //다음화면으로 넘어감
        finish();
    }

}