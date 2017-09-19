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

public class PopupLeg extends BaseActivity {
    private TransferData action;
    ToggleButton painLeg, fractureLeg, scratchLeg, skinLeg;
    ToggleButton toe, ankle, pelvis, leg;
    ToggleButton redLeg, pusLeg, pruritusLeg, furuncleLeg;
    LinearLayout line3, line4;
    Button registerButton;
    EditText treatment;

    // 서버로 보내는 결과 string값들
    final String whereFrom = "leg";
    String mainClass="";
    String subClass="";
    String detail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_leg);

        painLeg = (ToggleButton) findViewById(R.id.painLeg);//통증
        fractureLeg = (ToggleButton) findViewById(R.id.fractureLeg);//골절
        scratchLeg = (ToggleButton) findViewById(R.id.scratchLeg);//상처
        skinLeg = (ToggleButton) findViewById(R.id.skinLeg);//피부
        registerButton = (Button) findViewById(R.id.endLeg);//등록하기
        treatment = (EditText) findViewById(R.id.legPlus);

        //통증, 골절, 상처가 선택되었을 때
        line3 = (LinearLayout) findViewById(R.id.line3);
        toe = (ToggleButton) findViewById(R.id.toe);//발
        ankle = (ToggleButton) findViewById(R.id.ankle);//발목
        pelvis = (ToggleButton) findViewById(R.id.pelvis);//골반
        leg = (ToggleButton) findViewById(R.id.leg);//다리

        //피부 선택되었을 때
        line4 = (LinearLayout) findViewById(R.id.line4);
        redLeg = (ToggleButton) findViewById(R.id.redLeg);//붉어짐
        pusLeg = (ToggleButton) findViewById(R.id.pusLeg);//뾰루지
        pruritusLeg = (ToggleButton) findViewById(R.id.pruritusLeg);//가려움
        furuncleLeg = (ToggleButton) findViewById(R.id.furuncleLeg);//

        //처음엔 subClass 목록이 보이지 않아야 하니까
        line3.setVisibility(View.GONE);
        line4.setVisibility(View.GONE);

        // 아래는 mainClass 토글버튼 단위로 버튼리스너를 나열해놓은 것이다
        painLeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="ache";
                checkTrue(painLeg);

                checkFalse(fractureLeg);
                checkFalse(scratchLeg);
                checkFalse(skinLeg);

                checkFalse(toe);
                checkFalse(ankle);
                checkFalse(pelvis);
                checkFalse(leg);

                line3.setVisibility(View.VISIBLE);
                line4.setVisibility(View.GONE);
            }
        });
        fractureLeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="fracture";

                checkTrue(fractureLeg);

                checkFalse(painLeg);
                checkFalse(scratchLeg);
                checkFalse(skinLeg);

                checkFalse(toe);
                checkFalse(ankle);
                checkFalse(pelvis);
                checkFalse(leg);

                line3.setVisibility(View.VISIBLE);
                line4.setVisibility(View.GONE);
            }
        });
        scratchLeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="hurt";

                checkTrue(scratchLeg);

                checkFalse(painLeg);
                checkFalse(fractureLeg);
                checkFalse(skinLeg);

                checkFalse(toe);
                checkFalse(ankle);
                checkFalse(pelvis);
                checkFalse(leg);

                line3.setVisibility(View.VISIBLE);
                line4.setVisibility(View.GONE);
            }
        });
        skinLeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="skin";

                checkTrue(skinLeg);

                checkFalse(painLeg);
                checkFalse(fractureLeg);
                checkFalse(scratchLeg);

                checkFalse(redLeg);
                checkFalse(pusLeg);
                checkFalse(pruritusLeg);
                checkFalse(furuncleLeg);

                line3.setVisibility(View.GONE);
                line4.setVisibility(View.VISIBLE);
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


    } // onCreate() end

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
        switch (mainClass) {
            case "skin":
                boolean[] legSkin = {redLeg.isChecked(), pusLeg.isChecked(),
                        pruritusLeg.isChecked(), furuncleLeg.isChecked()};
                return makeSubClass(legSkin);
            default:
                boolean[] subPart = {toe.isChecked(), ankle.isChecked(), pelvis.isChecked(), leg.isChecked()};
                return makeSubClass(subPart);
        }
    }
    private boolean makeSubClass(boolean[] array){
        int count = 0;

        // subclass 버튼 중 아무것도 눌리지 않았을 떄
        if (!(array[0] || array[1] || array[2] || array[3])) {
            Toast.makeText(this, "세부 증상버튼을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                if (count > 0) {subClass += ",";}   // 중간에 , 추가하는 작업이다
                count++;
                if (mainClass.equals("skin")) {
                    switch (i) {
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
                } // mainClass = 피부로 선택한 경우
                else{
                    switch (i) {
                        case 0:
                            subClass += "발";
                            break;
                        case 1:
                            subClass += "발목";
                            break;
                        case 2:
                            subClass += "골반";
                            break;
                        case 3:
                            subClass += "다리";
                            break;
                    }
                }
            } // if(array[i]) end()
        } // 전체 for문 end()
        detail = treatment.getText().toString();
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
