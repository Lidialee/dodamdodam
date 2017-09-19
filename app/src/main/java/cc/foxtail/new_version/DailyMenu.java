package cc.foxtail.new_version;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.GregorianCalendar;

/**
 * Created by lidia on 2017. 4. 28..
 */

public class DailyMenu extends BaseActivity{

    private TransferData action;
    // 오브젝트값 받아오기
    TextView menuTitle1, year, month ,day ;
    TextView menu1,menu2,menu3,menu4,menu5,menu6;
    TextView allergy ;
    GregorianCalendar cal = new GregorianCalendar( );
    int yearNum, monthNum, dayNum;



    // Oncreate 시작
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dailymenu);

        menuTitle1 = (TextView) findViewById(R.id.menuTitle);
        year = (TextView) findViewById(R.id.year);
        month = (TextView) findViewById(R.id.month);
        day = (TextView) findViewById(R.id.day);
        menu1 = (TextView) findViewById(R.id.menu1);
        menu2 = (TextView) findViewById(R.id.menu2);
        menu3 = (TextView) findViewById(R.id.menu3);
        menu4 = (TextView) findViewById(R.id.menu4);
        menu5 = (TextView) findViewById(R.id.menu5);
        menu6 = (TextView) findViewById(R.id.menu6);
        allergy = (TextView) findViewById(R.id.allergy);

        // 현재 년도/달.일 받아오기
        yearNum = cal.get(cal.YEAR);
        monthNum = cal.get(cal.MONTH)+1; //이번달
        dayNum = cal.get(cal.DAY_OF_MONTH);

        year.setText(yearNum+"년 ");
        month.setText(monthNum+"월 ");
        day.setText(dayNum+"일");

        setMenuAndAllergy(getData());

    }

    //서버와 통신하도록 명령하는 함수
    public String getData(){
        try {
            // 통신코드 1번
            String server_to_year =  year.getText().toString();
            String server_to_month = month.getText().toString();
            String server_to_date = day.getText().toString();
            String[] info = {server_to_year, server_to_month, server_to_date};
            action = new TransferData(1, info);
            // 여기서 서버에게 통신 1을 넘겨주고
            // TrasferData의 1번 getTodayMenu을 실행시켜 해당 날짜의 메뉴를 받아온다.
            return UserInfo.server.submit(action).get();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public void setMenuAndAllergy(String json) {
        try {
            //정보를 json으로 받아오기 때문에 먼저 object로 파싱 (무조건)
            JSONObject object = new JSONObject(json);

            //정보가 각각 array로 묶여 오므로 array로 파싱
            //즉 메뉴정보와 알러지 정보가 menulist/allergylist에 저장되어 있다.
            JSONArray menuList = object.getJSONArray("menu");
            JSONArray allergyList = object.getJSONArray("allergy");

            //뷰 세팅
            // 여기서는 메뉴, 알러지 셋팅
            // menu1~6까지 받아온 정보를 넣어주면 된다
            String[] total = new String[6];
            for(int a=0;a<6;a++){
                total[a] = menuList.getString(a) +" "+ allergyList.getString(a);
            }
            menu1.setText(total[0]);
            menu2.setText(total[1]);
            menu3.setText(total[2]);
            menu4.setText(total[3]);
            menu5.setText(total[4]);
            menu6.setText(total[5]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
