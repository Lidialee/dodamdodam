package cc.foxtail.new_version;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class PopupStomahe extends BaseActivity {
    private TransferData action;
    ToggleButton painStomahe, diarrhea, constipation, skinStomahe;
    ToggleButton redStomahe, pusStomahe, pruritusStomahe, furuncleStomahe;
    LinearLayout line10;
    Button registerButton;
    EditText treatment;

    // 서버로 보내는 결과 string값들
    final String whereFrom = "belly";
    String mainClass="";
    String subClass="";
    String detail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_stomach);

        painStomahe = (ToggleButton) findViewById(R.id.painStomahe);//통증
        diarrhea = (ToggleButton) findViewById(R.id.diarrhea);//설사
        constipation = (ToggleButton) findViewById(R.id.constipation);//변비
        skinStomahe = (ToggleButton) findViewById(R.id.skinStomahe);//피부
        registerButton = (Button) findViewById(R.id.endStomach);//등록하기
        treatment = (EditText) findViewById(R.id.bellyPlus);

        //피부가 선택되었을 때
        line10 = (LinearLayout) findViewById(R.id.line10);
        redStomahe = (ToggleButton) findViewById(R.id.redStomahe);//붉어짐
        pusStomahe = (ToggleButton) findViewById(R.id.pusStomahe);//뾰루지
        pruritusStomahe = (ToggleButton) findViewById(R.id.pruritusStomahe);//가려움
        furuncleStomahe = (ToggleButton) findViewById(R.id.furuncleStomahe);//각질

        //처음엔 subClass 목록이 보이지 않아야 하니까
        line10.setVisibility(View.GONE);

        // 아래는 mainClass 토글버튼 단위로 버튼리스너를 나열해놓은 것이다
        painStomahe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="colic";
                checkTrue(painStomahe);

                checkFalse(diarrhea);
                checkFalse(constipation);
                checkFalse(skinStomahe);

                line10.setVisibility(View.GONE);
            }
        });
        diarrhea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="diarrhea";

                checkTrue(diarrhea);

                checkFalse(painStomahe);
                checkFalse(constipation);
                checkFalse(skinStomahe);

                line10.setVisibility(View.GONE);
            }
        });
        constipation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="constipation";
                checkTrue(constipation);

                checkFalse(painStomahe);
                checkFalse(diarrhea);
                checkFalse(skinStomahe);

                line10.setVisibility(View.GONE);
            }
        });
        skinStomahe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="skin";
                checkTrue(skinStomahe);

                checkFalse(painStomahe);
                checkFalse(diarrhea);
                checkFalse(constipation);

                checkFalse(redStomahe);
                checkFalse(pusStomahe);
                checkFalse(pruritusStomahe);
                checkFalse(furuncleStomahe);

                line10.setVisibility(View.VISIBLE);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (setData()) {
                    sendData();
                    finish();
                    Toast.makeText(getBaseContext(),"건강 기록이 등록되었습니다", Toast.LENGTH_LONG).show();
                }
            }
        });
    }// onCreate() end

    private void checkFalse(ToggleButton tb) {
        tb.setChecked(false);
        tb.setTextColor(Color.parseColor("#696969"));
    }
    private void checkTrue(ToggleButton tb) {
        tb.setChecked(true);
        tb.setTextColor(Color.parseColor("#ff6e57"));
    }
    private boolean setData() {
        if (mainClass.equals("")) {
            Toast.makeText(getApplicationContext(), "증상을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mainClass.equals("skin")) {
            boolean[] bellySkin = {redStomahe.isChecked(), pusStomahe.isChecked(),
                    pruritusStomahe.isChecked(), furuncleStomahe.isChecked()};
            return makeSubClass(bellySkin);
        }
        detail = treatment.getText().toString();
        return true;
    }
    private boolean makeSubClass(boolean[] array) {
        int count = 0;

        // subclass 버튼 중 아무것도 눌리지 않았을 떄
        if (!(array[0] || array[1] || array[2] || array[3])) {
            Toast.makeText(this, "피부 세부 증상버튼을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                if (count > 0) {subClass += ",";}   // 중간에 , 추가하는 작업이다
                count++;
                if (mainClass.equals("skin")){
                    switch (i){
                        case 0:
                            subClass += "붉어짐";
                            break;
                        case 1:
                            subClass += "뾰루지";
                            break;
                        case 2:
                            subClass += "가려움";
                            break;
                        case 3:
                            subClass += "각질";
                            break;
                    }
                }else{
                    Toast.makeText(this, "only mainClass=='skin'can get in here", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } // if(array[i]) end()
        } // for end
        return true;
    }

    public void sendData() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        String[] info = {UserInfo.childID + "", year + "", month + "", whereFrom, mainClass,subClass,detail};
        try {
            action = new TransferData(11, info);
            String r = UserInfo.server.submit(action).get();
            Log.e("서버", r);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
