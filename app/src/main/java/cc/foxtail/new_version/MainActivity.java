package cc.foxtail.new_version;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends BaseActivity {
    private TransferData action;
    private String[] infolist= new String[11];
    private ArrayList<String>mentlist = new ArrayList<String>();
    private TextView mento, kidName;
    private BackgroundTask task;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button monthlySchedule = (Button) findViewById(R.id.monthlySchedule);
        Button dailyMenu = (Button) findViewById(R.id.dailyMenu);
        Button Medicine = (Button) findViewById(R.id.Medicine);
        Button kidInfo = (Button) findViewById(R.id.kidInfo);
        Button healthy = (Button) findViewById(R.id.health);
        Button premium = (Button) findViewById(R.id.premium);
        mento = (TextView) findViewById(R.id.notice);
        kidName = (TextView)findViewById(R.id.kidName);

        monthlySchedule.setOnClickListener(listener);
        dailyMenu.setOnClickListener(listener);
        Medicine.setOnClickListener(listener);
        kidInfo.setOnClickListener(listener);
        healthy.setOnClickListener(listener);
        premium.setOnClickListener(listener);

        try {
            // 기본 정보가져오기
            setInfo(getInfoForMent());
            // 아이이름 세팅하기
            kidName.setText(UserInfo.getChildName());
            // 멘트 설정하기
            setMentstring(calcuBMI(infolist[2],infolist[3],infolist[5],infolist[4]));
            // 멘트 실행하기
            task=new BackgroundTask();
            task.execute();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    } // onCreate() end

    @Override
    protected void onResume() {
        try {
            setInfo(getInfoForMent());
            setMentstring(calcuBMI(infolist[2],infolist[3],infolist[5],infolist[4]));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    // 버튼리스너
    Button.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.monthlySchedule:
                    intent = new Intent(MainActivity.this, MonthlySchedule.class);
                    startActivity(intent);
                    break;
                case R.id.dailyMenu:
                    intent = new Intent(MainActivity.this, DailyMenu.class);
                    startActivity(intent);
                    break;
                case R.id.Medicine:
                    intent = new Intent(MainActivity.this, MedicineReqActivity.class);
                    startActivity(intent);
                    break;
                case R.id.kidInfo:
                    intent = new Intent(MainActivity.this, KidInfo.class);
                    startActivity(intent);
                    break;
                case R.id.health:
                    intent = new Intent(MainActivity.this, Healthy.class);
                    startActivity(intent);
                    break;
                case R.id.premium:
                    intent = new Intent(MainActivity.this, Premium.class);
                    startActivity(intent);
                    break;
            }
        }
    };  // 리스너 end

    // back 키 이벤트
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertDialogBuilder
                    = new AlertDialog.Builder(MainActivity.this);

            alertDialogBuilder.setTitle("프로그램 종료");
            alertDialogBuilder
                    .setMessage("프로그램을 종료하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("종료",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    moveTaskToBack(true);
                                    finish();
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    // 원래 여기에 로그아웃함수부름
                                }
                            })
                    .setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return true;
    }

    // 로그아웃함수
    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public String getInfoForMent() {
        try {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int hour = Calendar.getInstance().get(Calendar.HOUR);
            String[] info = { UserInfo.childID+"", year+"", month+"", day+"", hour+"" };
            action = new TransferData(20, info);
            String re = UserInfo.executeServer(action);
            return re;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setInfo(String json) throws JSONException {

        JSONObject object = new JSONObject(json);
        infolist[0]= object.getString("eyeOrNose");
        infolist[1]= object.getString("skinIll");
        infolist[2]= object.getString("weight");
        infolist[3]= object.getString("height");
        infolist[4]= object.getString("stdHeight");
        infolist[5]= object.getString("stdWeight");
        infolist[6]= object.getString("skinWho");
        infolist[7]= object.getString("fsnLife");
        infolist[8]= object.getString("ultrvLife");
        infolist[9]= object.getString("ctprvn");
        infolist[10]= object.getString("dayDiff");

    }
    public String[] calcuBMI(String we, String he,String stand_we, String stand_he){
        // bmi / ratio-weight / ratio-height
        String[] list = new String[6];
        float f_we = Float.parseFloat(we);
        float f_he = Float.parseFloat(he)/100; //미터로 단위바꾸기
        float bmi = f_we/(f_he * f_he);
        list[0]=String.format("%.2f", bmi);        // list[0]=bmi
        Log.d("***f_we",""+f_we);
        Log.d("***f_he",""+f_he);
        Log.d("***stand_we",""+stand_we);
        Log.d("***stand_he",""+stand_he);

        // 아이가 몇세 이하면 표준이 없다
        if(stand_we.equals("")&&stand_he.equals("")){
            list[1]="";
            list[2]="";
            list[3]="없다";
        }
        else{

            float f_stand_we = Float.parseFloat(stand_we);
            float f_stand_he = Float.parseFloat(stand_he)/100;

            // 몸무게stand
            float mid= f_stand_we - f_we;
            if(mid<0) {
                mid=mid*(-1);
                list[4]="높다";
            }
            list[4]="낮다";
            float result = (mid/f_stand_we)*100;
            String str = String.format("%.2f", result);
            list[1]= str;   // list[1]= we%

            // 키 stand
            mid= f_stand_he-f_he;
            if(mid<0) {
                mid=mid*(-1);
                list[5]="높다";
            }
            list[5]="낮다";
            result = (mid/f_stand_he)*100;
            str = String.format("%.2f", result);
            list[2]= str;   // list[2] = he%

            list[3]="있다";
        }
        return list;
    }



    // 아이의 정보에 따라 미리 멘트를 설정해놓는 함수
    public void setMentstring(String[] bmi_he_we){

        // 수치/ 수치별 추가 멘트
        String plus_ment="";

        // 1. 일교차 ->무조건
        if(Integer.parseInt(infolist[10])>=8){
            mentlist.add("오늘의 일교차는 "+infolist[10]+"℃ 입니다"+"\n"+" 일교차가 크니 옷차림에 신경써주세요");
        }else
            mentlist.add("오늘의 일교차는 "+infolist[10]+"℃ 입니다");


        // 2. 식중독 ->무조건
        int level= Integer.parseInt(infolist[7]);
        switch (level){
            case 1:
                plus_ment="오늘의 식중독은 ‘낮음’입니다.";
                break;
            case 2:
                plus_ment="오늘의 식중독은 ‘중간’입니다.\n" +
                           "밥 먹기 전에 손을 깨끗이 씻어주세요.";
                break;
            case 3:
                plus_ment="오늘의 식중독은 ‘높음’입니다.\n" +
                        "손을 꼭 씻고, 익혀먹기와 끓여먹기를 실천해주세요";
                break;
            case 4:
                plus_ment=" 오늘의 식중독은 ‘매우높음’입니다.\n" +
                        "채소류, 육류, 어패류, 완제품은 칼과 도마를 구분해 사용해주세요.\n";
                break;
        }
        mentlist.add(plus_ment);

        String bmi = "현재 BMI 지수는 "+bmi_he_we[0]+"입니다\n";
        // 0 =  bmi / 1 = 몸무게 / 2 = 키 / 3 = 표준의 여부 / 4 = 몸무게 (낮다) / 5 = 키 (낮다)
        // 3. bmi지수 ->무조건
        if(bmi_he_we[3].equals("있다"))
        {
            if(bmi_he_we[4].equals("높다")) {
                bmi+="현재 몸무게는 표준보다 "+bmi_he_we[1]+"% 큽니다\n";
            }else{
                bmi+="현재 몸무게는 표준보다 "+bmi_he_we[1]+"% 작습니다\n";
            }

            if(bmi_he_we[5].equals("높다")){
                bmi+="현재 키는 표준보다 "+bmi_he_we[2]+"% 큽니다\n";
            }else{
                bmi+="현재 키는 표준보다 "+bmi_he_we[2]+"% 작습니다\n";
            }
            mentlist.add(bmi);
        }
        else{
            mentlist.add(bmi);
        }

        // 4. 자외선
        level= Integer.parseInt(infolist[8]);
        if(level>=2){
            switch (level){
                case 2:
                    plus_ment="오늘의 자외선 지수는 ‘보통’입니다.\n" +
                            "야외시간을 2~3시간으로 한정시켜주세요.";
                    break;
                case 3:
                    plus_ment="오늘의 자외선 지수는 ‘높음’입니다.\n" +
                            "야외시간을 1~2시간으로 제한하고, 자외선차단제를 꾸준히 발라주세요.";
                    break;
                case 4:
                    plus_ment="오늘의 자외선 지수는 ‘매우높음’입니다.\n" +
                            "10시부터 15시는 외출을 피하고, 외출할 때 모자, 선글라스, 자외선차단제를 챙겨주세요.";
                    break;
                case 5:
                    plus_ment="오늘의 자외선 지수는 ‘위험’입니다.\n" +
                            "가능한 외출을 피하고 실내에 있어주세요.";
                    break;
            }
            mentlist.add(plus_ment);
        }else{
            if(Integer.parseInt(infolist[1])==1){
                mentlist.add("오늘의 자외선지수는 ‘낮음’입니다.\n"+
                        "만약을 위해 자외선 차단제를 발라주세요.");
            }
        }
        // 미세먼지 --> 조건부
        level= Integer.parseInt(infolist[9]);
        if(level>=2){
            switch (level){
                case 2:
                    plus_ment="오늘의 미세먼지는 ‘나쁨’입니다.\n" +
                            "장시간 또는 무리한 실외활동은 하지 말아주세요.\n" +
                            "특히 천식환자나 눈/코가 아픈 아이는 각별히 주의해주세요.\n";
                    break;
                case 3:
                    plus_ment="오늘의 미세먼지는 ‘매우나쁨’입니다.\n" +
                            "가급적 야외활동은 피하고, 실내에 있어주세요.\n";
                            break;
            }
            mentlist.add(plus_ment);

        }else if(level==1){
            if(Integer.parseInt(infolist[0])==1){
                mentlist.add("오늘의 미세먼지는 ‘보통’입니다.\n" +
                        "만약을 대비해 야외활동 시 몸상태에 따라 유의하여 활동해주세요.\n");
            }
        }
        // 6
        // 피부
        level= Integer.parseInt(infolist[6]);
        if(level>=2){
            switch (level){
                case 2:
                    plus_ment="오늘의 피부질환지수는 ‘높음’입니다.\n" +
                            "충분한 수분을 섭취하고, 보습 및 피부 관리에 주의해주세요.";
                    break;
                case 3:
                    plus_ment="오늘의 피부질환지수는 ‘매우높음’입니다.\n" +
                            "야외활동은 가급적 피하고, 쾌적한 환경을 유지해주세요.";
                    break;
            }
            mentlist.add(plus_ment);
        }else if(level==1){
            if(Integer.parseInt(infolist[1])==1){
                mentlist.add("오늘의 피부질환지수는 ‘보통’입니다.\n" +
                        "피부질환 환자들은 보습에 주의해주세요.");
            }
        }
    } // 멘트만들기 완료


    class BackgroundTask extends AsyncTask<Integer,Integer,Integer> {
        // 새로 만든 스레드에서 백그라운드 작업을 수행한다
        @Override
        protected Integer doInBackground(Integer... params) {
            while(true){
                try{
                    if(num==mentlist.size()){num=0;}
                    publishProgress(num);
                    Thread.sleep(5000);
                    num+=1;
                }catch (Exception e){}
            }
        }

        // 백그라운드 작업을 수행하기 전에 호출된다, 메인 스레드에서 실행되며 초기화작업에 사용
        @Override
        protected void onPreExecute() {
            Log.d("onPreExecute","확인");
        }

        // 백그라운드 진행 상태를 표시하기 위해 호출, 작업 수행 중간중간에
        // ui객체에 접근하는 경우 사용
        @Override
        protected void onProgressUpdate(Integer ...values) {
            Log.d("onPreExecute","확인");
            mento.setText(mentlist.get(num));
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

}
