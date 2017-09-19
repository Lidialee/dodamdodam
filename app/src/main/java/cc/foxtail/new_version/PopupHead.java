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

public class PopupHead extends BaseActivity {
    private TransferData action;
    ToggleButton feverHead, scratchHead, painHead, skinHead, ear, eye, nose, mouse;
    ToggleButton tinnitus, earache, hyperemia, pruritusEye, snot, nasalObstruction, toothache, coldSore;
    ToggleButton redHead, pusHead, pruritusHead, furuncleHead;
    LinearLayout line5, line6, line7, line8, line9;
    EditText fever;
    EditText treatment;
    Button registerButton;

    // 서버로 보내는 결과 string값들
    final String whereFrom = "head";
    String mainClass="";
    String subClass="";
    String detail="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_head);

        feverHead = (ToggleButton) findViewById(R.id.feverHead);//열
        scratchHead = (ToggleButton) findViewById(R.id.scratchHead);//상처
        painHead = (ToggleButton) findViewById(R.id.painHead);//통증
        skinHead = (ToggleButton) findViewById(R.id.skinHead);//피부
        ear = (ToggleButton) findViewById(R.id.ear);//귀
        eye = (ToggleButton) findViewById(R.id.eye);//눈
        nose = (ToggleButton) findViewById(R.id.nose);//코
        mouse = (ToggleButton) findViewById(R.id.mouse);//입
        registerButton = (Button) findViewById(R.id.endHead);//등록하기버튼
        treatment = (EditText) findViewById(R.id.headPlus);

        //귀 선택되었을 때
        line5 = (LinearLayout) findViewById(R.id.line5);
        tinnitus = (ToggleButton) findViewById(R.id.tinnitus);//이명
        earache = (ToggleButton) findViewById(R.id.earache);//귀앓이

        //눈 선택되었을 때
        line6 = (LinearLayout) findViewById(R.id.line6);
        hyperemia = (ToggleButton) findViewById(R.id.hyperemia);//충혈
        pruritusEye = (ToggleButton) findViewById(R.id.pruritusEye);//가려움

        //코 선택되었을 때
        line7 = (LinearLayout) findViewById(R.id.line7);
        snot = (ToggleButton) findViewById(R.id.snot);//콧물
        nasalObstruction = (ToggleButton) findViewById(R.id.nasalObstruction);//코막힘

        //입 선택되었을 때
        line8 = (LinearLayout) findViewById(R.id.line8);
        toothache = (ToggleButton) findViewById(R.id.toothache);//치통
        coldSore = (ToggleButton) findViewById(R.id.coldSore);//부르틈

        //피부 선택되었을 때
        line9 = (LinearLayout) findViewById(R.id.line9);
        redHead = (ToggleButton) findViewById(R.id.redHead);//붉어짐
        pusHead = (ToggleButton) findViewById(R.id.pusHead);//뾰루지
        pruritusHead = (ToggleButton) findViewById(R.id.pruritusHead);//가려움
        furuncleHead = (ToggleButton) findViewById(R.id.furuncleHead);//각질

        //열 선택되었을 때
        fever = (EditText) findViewById(R.id.fever);

        //처음엔 subClass 목록이 보이지 않아야 하니까
        line5.setVisibility(View.GONE);
        line6.setVisibility(View.GONE);
        line7.setVisibility(View.GONE);
        line8.setVisibility(View.GONE);
        line9.setVisibility(View.GONE);
        fever.setVisibility(View.GONE);

        // 아래는 mainClass 토글버튼 단위로 버튼리스너를 나열해놓은 것이다
        //귀 - line5, 눈 - line6, 코 - line7, 입 - line8, 피부 - line9, 열 - fever
        feverHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="fever";

                checkTrue(feverHead);

                checkFalse(scratchHead);
                checkFalse(painHead);
                checkFalse(skinHead);
                checkFalse(ear);
                checkFalse(eye);
                checkFalse(nose);
                checkFalse(mouse);

                fever.setText("");

                line5.setVisibility(View.GONE);
                line6.setVisibility(View.GONE);
                line7.setVisibility(View.GONE);
                line8.setVisibility(View.GONE);
                line9.setVisibility(View.GONE);
                fever.setVisibility(View.VISIBLE);
            }
        });
        scratchHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="hurt";

                checkTrue(scratchHead);

                checkFalse(feverHead);
                checkFalse(painHead);
                checkFalse(skinHead);
                checkFalse(ear);
                checkFalse(eye);
                checkFalse(nose);
                checkFalse(mouse);

                line5.setVisibility(View.GONE);
                line6.setVisibility(View.GONE);
                line7.setVisibility(View.GONE);
                line8.setVisibility(View.GONE);
                line9.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
            }
        });
        painHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="headache";

                checkTrue(painHead);

                checkFalse(feverHead);
                checkFalse(scratchHead);
                checkFalse(skinHead);
                checkFalse(ear);
                checkFalse(eye);
                checkFalse(nose);
                checkFalse(mouse);

                line5.setVisibility(View.GONE);
                line6.setVisibility(View.GONE);
                line7.setVisibility(View.GONE);
                line8.setVisibility(View.GONE);
                line9.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
            }
        });
        skinHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="skin";

                checkTrue(skinHead);

                checkFalse(feverHead);
                checkFalse(scratchHead);
                checkFalse(painHead);
                checkFalse(ear);
                checkFalse(eye);
                checkFalse(nose);
                checkFalse(mouse);

                checkFalse(redHead);
                checkFalse(pusHead);
                checkFalse(pruritusHead);
                checkFalse(furuncleHead);

                line5.setVisibility(View.GONE);
                line6.setVisibility(View.GONE);
                line7.setVisibility(View.GONE);
                line8.setVisibility(View.GONE);
                line9.setVisibility(View.VISIBLE);
                fever.setVisibility(View.GONE);
            }
        });
        ear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="ear";

                checkTrue(ear);

                checkFalse(feverHead);
                checkFalse(scratchHead);
                checkFalse(painHead);
                checkFalse(skinHead);
                checkFalse(eye);
                checkFalse(nose);
                checkFalse(mouse);

                checkFalse(tinnitus);
                checkFalse(earache);

                line5.setVisibility(View.VISIBLE);
                line6.setVisibility(View.GONE);
                line7.setVisibility(View.GONE);
                line8.setVisibility(View.GONE);
                line9.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
            }
        });
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="eye";

                checkTrue(eye);

                checkFalse(feverHead);
                checkFalse(scratchHead);
                checkFalse(painHead);
                checkFalse(skinHead);
                checkFalse(ear);
                checkFalse(nose);
                checkFalse(mouse);

                checkFalse(hyperemia);
                checkFalse(pruritusEye);

                line5.setVisibility(View.GONE);
                line6.setVisibility(View.VISIBLE);
                line7.setVisibility(View.GONE);
                line8.setVisibility(View.GONE);
                line9.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
            }
        });
        nose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="nose";

                checkTrue(nose);

                checkFalse(feverHead);
                checkFalse(scratchHead);
                checkFalse(painHead);
                checkFalse(skinHead);
                checkFalse(eye);
                checkFalse(ear);
                checkFalse(mouse);

                checkFalse(snot);
                checkFalse(nasalObstruction);

                line5.setVisibility(View.GONE);
                line6.setVisibility(View.GONE);
                line7.setVisibility(View.VISIBLE);
                line8.setVisibility(View.GONE);
                line9.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
            }
        });
        mouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="mouse";

                checkTrue(mouse);

                checkFalse(feverHead);
                checkFalse(scratchHead);
                checkFalse(painHead);
                checkFalse(skinHead);
                checkFalse(eye);
                checkFalse(nose);
                checkFalse(ear);

                checkFalse(toothache);
                checkFalse(coldSore);

                line5.setVisibility(View.GONE);
                line6.setVisibility(View.GONE);
                line7.setVisibility(View.GONE);
                line8.setVisibility(View.VISIBLE);
                line9.setVisibility(View.GONE);
                fever.setVisibility(View.GONE);
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

    private boolean setData(){
        if (mainClass.equals("")) {
            Toast.makeText(getApplicationContext(), "증상을 선택해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        switch (mainClass)
        {
            case "fever":
                if (fever.getText().toString().equals("")) {
                    Toast.makeText(this, "체온을 기록해주세요", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    subClass = fever.getText().toString();
                    break;
                }
            case "skin":
                boolean[] headSkin = {redHead.isChecked(), pusHead.isChecked(),
                        pruritusHead.isChecked(), furuncleHead.isChecked()};
                boolean result = makeSubClass(headSkin);
                if(result==false) return false;
                break;
            case "ear":
                if (tinnitus.isChecked() && earache.isChecked()) {
                    subClass += "이명,귀앓이";
                } else if (tinnitus.isChecked()) {
                    subClass += "이명";
                } else if (earache.isChecked()) {
                    subClass += "귀앓이";
                } else {
                    Toast.makeText(this, "증상버튼을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "eye":
                if (hyperemia.isChecked() && pruritusEye.isChecked()) {
                    subClass += "충혈,가려움";
                } else if (hyperemia.isChecked()) {
                    subClass += "충혈";
                } else if (pruritusEye.isChecked()) {
                    subClass += "가려움";
                } else {
                    Toast.makeText(this, "증상버튼을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "nose":
                if (snot.isChecked() && nasalObstruction.isChecked()) {
                    subClass += "콧물,코막힘";
                } else if (snot.isChecked()) {
                    subClass += "콧물";
                } else if (nasalObstruction.isChecked()) {
                    subClass += "코막힘";
                } else {
                    Toast.makeText(this, "증상버튼을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return false;
                }
                break;
            case "mouse":
                if (toothache.isChecked() && coldSore.isChecked()) {
                    subClass += "치통,부르틈";
                } else if (toothache.isChecked()) {
                    subClass += "치통";
                } else if (coldSore.isChecked()) {
                    subClass += "부르틈";
                } else {
                    Toast.makeText(this, "증상버튼을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return false;
                }
        }
        // 에러가 이것때매 생길 수도 있어
        detail = treatment.getText().toString();
        return true;
    } // setDate() end
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
                }else{
                    Toast.makeText(this, "only mainClass=='skin'can get in here", Toast.LENGTH_SHORT).show();
                    return false;}
            } // if(array[i]) end()
        } // for end
        return true;
    } // makeSubClass()

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
