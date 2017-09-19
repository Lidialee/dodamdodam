package cc.foxtail.new_version;

import android.os.Bundle;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class PopupHand extends BaseActivity {
    private TransferData action;
    ToggleButton painHand, fractureHand, scratchHand, skinHand;
    ToggleButton hand, wrist, shoulder, limb;
    ToggleButton redHand, pusHand, pruritusHand, furuncleHand;
    Button registerButton;
    LinearLayout line1, line2;
    EditText treatment;
    //int checkHand, checkLine1, checkLine2;

    // 서버로 보내는 결과 string값들
    final String whereFrom = "hand";
    String mainClass="";
    String subClass="";
    String detail="";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hand_popup);

        painHand = (ToggleButton) findViewById(R.id.painHand);//통증
        fractureHand = (ToggleButton) findViewById(R.id.fractureHand);//골절
        scratchHand = (ToggleButton) findViewById(R.id.scratchHand);//상처
        skinHand = (ToggleButton) findViewById(R.id.skinHand);//피부
        treatment = (EditText) findViewById(R.id.handPlus); //치료경과 EditText

        //통증, 골절, 상처가 선택되었을 때
        line1 = (LinearLayout) findViewById(R.id.line1);
        hand = (ToggleButton) findViewById(R.id.hand);//손
        wrist = (ToggleButton) findViewById(R.id.wrist);//손목
        shoulder = (ToggleButton) findViewById(R.id.shoulder);//어깨
        limb = (ToggleButton) findViewById(R.id.limb);//팔
        registerButton = (Button) findViewById(R.id.endHand);//등록하기

        //피부가 선택되었을 때
        line2 = (LinearLayout) findViewById(R.id.line2);
        redHand = (ToggleButton) findViewById(R.id.redHand);//붉어짐
        pusHand = (ToggleButton) findViewById(R.id.pusHand);//뾰루지
        pruritusHand = (ToggleButton) findViewById(R.id.pruritusHand);//가려움
        furuncleHand = (ToggleButton) findViewById(R.id.furuncleHand);//각질

        // 처음엔 subClass 목록이 보이지 않아야 하니까
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);


        // 등록하기버튼 리스너
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
        // 아래는 mainClass 토글버튼 단위로 버튼리스너를 나열해놓은 것이다
        // mainClass:통증
        painHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="ache";
                checkTrue(painHand);

                checkFalse(fractureHand);
                checkFalse(scratchHand);
                checkFalse(skinHand);

                checkFalse(hand);
                checkFalse(wrist);
                checkFalse(shoulder);
                checkFalse(limb);

                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.GONE);
            }
        });

        // mainClass:골절
        fractureHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="fracture";
                checkTrue(fractureHand);

                checkFalse(painHand);
                checkFalse(scratchHand);
                checkFalse(skinHand);

                checkFalse(hand);
                checkFalse(wrist);
                checkFalse(shoulder);
                checkFalse(limb);

                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.GONE);
            }
        });
        // mainClass:상처
        scratchHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="hurt";
                checkTrue(scratchHand);

                checkFalse(painHand);
                checkFalse(fractureHand);
                checkFalse(skinHand);

                checkFalse(hand);
                checkFalse(wrist);
                checkFalse(shoulder);
                checkFalse(limb);

                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.GONE);
            }
        });
        // mainClass:피부
        skinHand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainClass="skin";
                checkTrue(skinHand);

                checkFalse(painHand);
                checkFalse(fractureHand);
                checkFalse(scratchHand);

                checkFalse(redHand);
                checkFalse(pusHand);
                checkFalse(pruritusHand);
                checkFalse(furuncleHand);

                line2.setVisibility(View.VISIBLE);
                line1.setVisibility(View.GONE);
            }
        });
    } // onCreate() end;

    // 터치에 따라 체크여부를 알려주는 것!!!
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
        // mainClass 버튼이 어떤것이냐에 따라
        // subClass 버튼의 터치여부에 따른 배열을 만든다.
        switch (mainClass) {
            case "skin":
                boolean[] handSkin = {redHand.isChecked(), pusHand.isChecked(),
                        pruritusHand.isChecked(), furuncleHand.isChecked()};
                return makeSubClass(handSkin);
            default:
                boolean[] subPart = {hand.isChecked(), wrist.isChecked(), shoulder.isChecked(), limb.isChecked()};
                return makeSubClass(subPart);
        }
    } // setData() end

    // 위에 setData에서 결과값으로 넘어온 subClass 버튼
    // 터치여부 (ex : 1,0,1,0)에 따라 1으로 온것들만 ( = if(array[i] 이 부분에서 1만 아래작업을 하는것 )
    // String subClass에 ,을 추가하며 하나로 만들어 주는 작업
    private boolean makeSubClass(boolean[] array) {
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
                            subClass += "손";
                            break;
                        case 1:
                            subClass += "손목";
                            break;
                        case 2:
                            subClass += "어깨";
                            break;
                        case 3:
                            subClass += "팔";
                            break;
                    }
                }
            } // if(array[i]) end()
        } // 전체 for문 end()
        detail = treatment.getText().toString();
        return true;
    }


    public void sendData() {
        /*
            * 건강기록등록 setData()
            *
            * 서버에 건강기록을 등록하는 함수
            *
            * 인자 주의사항
            * 1. 부위, 큰버튼은 반드시 서버에서 정해둔 인자명으로 보내주기 바랍니다. (이미지참고)
            *   부위 : head, hand, belly, leg
            *   큰버튼 : fever, hurt, headache, skin, ear, eye, nose, mouse, ache, fracture, colic, diarrhea, constipation
            * 2. 작은버튼 및 열기록은 여러개 선택시 띄어쓰기 없이 ,로 구분지어 보내주면 됩니다.
            *   ex) 치통,부르틈
            * 3. 상세내용은 없을경우 ""로 보내주세요
            */
        /*
             * 통신코드 11번
             * * 서버에 건강기록 등록하기
             * * 인자 : child ID, 등록년, 등록월, 부위, 큰버튼, 작은버튼
        * */
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
