package cc.foxtail.new_version;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by lidia on 2017. 5. 16..
 */

public class RegisterActivity extends BaseActivity {
    private TransferData action;
    EditText id_edittext,pass_edittext;
    Button check_id,gotosecond;
    CheckBox agreecheck;
    String idstring, passstring;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);

        id_edittext = (EditText) findViewById(R.id.ID);
        pass_edittext = (EditText) findViewById(R.id.PW);
        check_id = (Button) findViewById(R.id.button);
        gotosecond = (Button) findViewById(R.id.button2);
        agreecheck = (CheckBox) findViewById(R.id.checkBox);

        // 리스너 등록
        check_id.setOnClickListener(listener);
        gotosecond.setOnClickListener(listener);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.button:
                    // 중복확인 버튼 여기선 단순히 토스트로 아이디 입력상태만 알려줄뿐
                    // 특별히 이후 흐름을 막거나 하지는 않는다

                    idstring = id_edittext.getText().toString();
                    if (idstring.equals(""))
                        Toast.makeText(getApplicationContext(),"아이디란에 아무것도 입력하지 않으셨습니다",Toast.LENGTH_SHORT).show();
                    else {
                        if (checkID().equals("possible")) Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다", Toast.LENGTH_LONG).show();
                        else Toast.makeText(getApplicationContext(), "이미 사용하고 있는 아이디입니다", Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.button2:
                    // 여기서 각 단계별로 조건을 만족하지 못하면 break을 통해서 중단되서
                    // 다음 2단계로 넘어가지 못한다.
                    idstring = id_edittext.getText().toString();
                    passstring = pass_edittext.getText().toString();
                    // 1. 아이디 체크
                    if(idstring.equals("")){
                        Toast.makeText(getApplicationContext(),"아이디란에 아무것도 입력하지 않으셨습니다",Toast.LENGTH_SHORT).show();
                        break;
                    }else{
                        //아이디 중복확인을 합니다
                        if(!(checkID().equals("possible"))) {
                            Toast.makeText(getApplicationContext(), "중복확인을 다시 한번해주시기 바랍니다", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    // 2. 비밀번호
                    if(passstring.equals("")){
                        Log.d("패스워드_진입",passstring);
                        Toast.makeText(getApplicationContext(), "비밀번호를 입력해주시기 바랍니다", Toast.LENGTH_LONG).show();
                        break;
                    }
                    // 3. 체크상자
                    if(!agreecheck.isChecked()){
                        Log.d("동의_진입",""+agreecheck.isChecked());
                        Toast.makeText(getApplicationContext(), "동의 버튼을 클릭해주시기 바랍니다", Toast.LENGTH_LONG).show();
                        break;
                    }
                    // 2단계
                    // 위에서 한번도 break가 걸리지 않았다면 여기에 도달한다
                    Intent intent = new Intent(getApplicationContext(),RegisterActivityTwo.class);
                    intent.putExtra("FROM","Self");
                    intent.putExtra("ID",idstring);
                    intent.putExtra("PASS",passstring);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };  // 버튼 리스너 end

    // 아이디
    public String checkID() {
        String[] info = {idstring};
        try {
            action = new TransferData(15, info);
            String re =UserInfo.server.submit(action).get();
            Log.d("제발",""+re);
            return re;
        }catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
