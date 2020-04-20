package kr.lfin.travital;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SelectLangActivity extends Activity implements OnClickListener{
    String[] languages={"언어를 선택해주세요.","한국어"};
    String[] countries={"어느 나라를 가시나요?","일본"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_lang_layout);

        Spinner spinner1=(Spinner) findViewById(R.id.spinner_lang);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,languages);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        Spinner spinner2=(Spinner) findViewById(R.id.spinner_coun);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,countries);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
    }
    @Override
    public void onClick(View v){
        Intent intent = new Intent (SelectLangActivity.this, AgreeActivity.class);
        startActivity(intent); //다음화면으로 넘어감
        finish();
    }

}