package cc.foxtail.new_version;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by lidia on 2017. 4. 28..
 */

public class MonthlySchedule extends BaseActivity {

    private TransferData action;
    TextView calendar;
    TextView monthNum;
    String pkgName ;
    int year,month, yoil, maxday, day;
    GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
    GregorianCalendar today = (GregorianCalendar) Calendar.getInstance();
    Calendar tmp;
    TextView [] date = new TextView[42];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthlyschedule);

        monthNum = (TextView) findViewById(R.id.calendar_date_display);

        customCalendar();
        setSchedule(getData());
    }

    void customCalendar(){
        int k=1, j=0;

        cal.set(cal.YEAR,cal.get(Calendar.MONTH), 1);//올해, 이번달의 1일로 강제로 세팅
        year = cal.get(cal.YEAR);
        month = cal.get(cal.MONTH)+1; //이번달
        yoil = cal.get ( cal.DAY_OF_WEEK ); // 1일 요일을 알 수 있음(0~6, 0:일요일)
        maxday = cal.getActualMaximum ( ( cal.DAY_OF_MONTH ) );//오늘 달의 마지막 일
        day = today.get(today.DAY_OF_MONTH); // 진짜 오늘 요일
        monthNum.setText(month+"");
        pkgName = getPackageName();

        for(int i=0 ; i<42 ; i++)
        {
            String textViewID = "textView" + (i+1);
            int resID = getResources().getIdentifier(textViewID, "id", pkgName);
            calendar = (TextView)findViewById(resID);
            calendar.setText("");                       //TextView 비우기
            calendar.setBackgroundColor(Color.parseColor("#eaeaea"));

            if(i>(yoil-1)){
                if(j<maxday){
                    calendar.setText(k+"");
                    k++;
                    j++;

                    if(j==day){
                        calendar.setTextColor(Color.parseColor("#ff6e57"));
                    }
                }
                else {
                    if(i>28){
                        calendar.setVisibility(View.INVISIBLE);
                    }
                    else if(i>35) {
                        calendar.setVisibility(View.GONE);
                    }
                }
            } //TextView 날짜 채우기

        }
    }

    public String getData(){
        try {
            //통신코드 2번
            //인자 : year, month
            // 여기 버그 2016 더하는 걸로 야매로 했어여!!!!!!!!!!
            String[] info = {year+2016+"",month+""};
            Log.d("보내지는 년달 ",year+"/"+month);
            action = new TransferData(2, info);
            return UserInfo.server.submit(action).get();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public void setSchedule(String json) {
        try {
            int illjung_plus;
            //정보를 json으로 받아오기 때문에 먼저 object로 파싱 (무조건)
            JSONObject object = new JSONObject(json);

            //정보 각각 파싱
            JSONArray schedule = object.getJSONArray("schedule");

            //뷰 세팅
            for (int a = 0; a < schedule.length(); a++) {
                JSONObject set = schedule.getJSONObject(a);
                illjung_plus = Integer.parseInt(set.getString("day"));      // 일정 날짜
                Log.d("day ",illjung_plus+"" );
                String write_cal_day = "textView"+(yoil+illjung_plus);
                Log.d("write_cal_day ",write_cal_day);
                int made_id = getResources().getIdentifier(write_cal_day, "id", pkgName);
                calendar = (TextView)findViewById(made_id);
                String d = calendar.getText().toString();
                String schdul = set.getString("name");
                calendar.setText(d+"\n"+schdul);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
