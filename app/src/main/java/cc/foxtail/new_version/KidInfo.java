package cc.foxtail.new_version;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.json.JSONObject;

import java.util.ArrayList;


public class KidInfo extends BaseActivity {
    private TransferData action;

    private TextView kidName,kidBirth,kidGrowth,etcName;
    private Button editIcon, KidInfoEnd,kidGrowthGraph,kidVaccineList;
    private EditText etcText;

    private ToggleButton[] allergylist, illnesslist;     //  allergy1_9 / illnese1_6
    private TextView[] allergyN_7_9;                      //  allergylist[6]_9 이름
    private EditText[] allergyP_7_9;                      //  allergylist[6]_9 추가사항

    private ArrayList<Integer> user_allergy_list;                      //  서버에서 오는 알러지 숫자정보,
    private ArrayList<Integer> user_illness_list;                      //  서버에서 오는 질환 숫자정보

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kidinfo);

        // 뷰 연결
        kidName = (TextView) findViewById(R.id.kidName);
        kidBirth = (TextView) findViewById(R.id.kidBirth);
        kidGrowth = (TextView) findViewById(R.id.kidGrowth);
        etcName = (TextView) findViewById(R.id.etcName);

        editIcon = (Button) findViewById(R.id.editIcon);                    // 수정버튼
        kidGrowthGraph = (Button) findViewById(R.id.kidGrowthGraph);        // 그래프 보기 버튼
        kidVaccineList = (Button) findViewById(R.id.kidVaccineList);        // 백신리스트 보기 버튼
        KidInfoEnd = (Button) findViewById(R.id.KidInfoEnd);                // 수정완료 버튼

        etcText = (EditText) findViewById(R.id.etcText);                    // 기타 주의사항

        allergylist = new ToggleButton[9];                                  // allergy1~9
        illnesslist = new ToggleButton[6];                                  // illness1~6
        allergyN_7_9 = new TextView[3];                                     // allergyN_7_9[0]-9
        allergyP_7_9 = new EditText[3];                                     // allergyP_7_9[0]-9

        for(int a=0;a<9;a++){
            int ID = getResources().getIdentifier("allergy"+(a+1),"id",getPackageName());
            allergylist[a] = (ToggleButton) findViewById(ID);
        }
        for(int a=0;a<6;a++){
            int ID = getResources().getIdentifier("illness"+(a+1),"id",getPackageName());
            illnesslist[a] = (ToggleButton) findViewById(ID);
        }
        for(int a=0;a<3;a++){
            int ID_name = getResources().getIdentifier("allergyname"+(a+7),"id",getPackageName());
            int ID_plus = getResources().getIdentifier("allergyplus"+(a+7),"id",getPackageName());
            allergyN_7_9[a] = (TextView) findViewById(ID_name);
            allergyP_7_9[a] = (EditText) findViewById(ID_plus);
        }
        editIcon.setOnClickListener(new View.OnClickListener(){     // 수정하기 버튼 눌렸을 때
            @Override
            public void onClick(View v) {
                editBtn_clicked();
            }
        });
        KidInfoEnd.setOnClickListener(new View.OnClickListener(){   // 수정완료 버튼 눌렸을 때
            @Override
            public void onClick(View v) {
                KidInfoEnd_clicked();
            }
        });
        kidGrowthGraph.setOnClickListener(new View.OnClickListener(){// 성장과정 그래프 버튼
            @Override
            public void onClick(View v) {
                Graphbtn_clicked();
            }
        });
        kidVaccineList.setOnClickListener(new View.OnClickListener(){// 예방접종기록 버튼
            @Override
            public void onClick(View v) {
                Vaccinebtn_clicked();
            }
        });

        setKidInfo(getData());                                      // 서버 요청 -> 가져온 정보로 아이정보 셋팅합니다.
           }  //  onCreate() 끝


    public String getData(){
        try {
            String[] info = {UserInfo.childID+""};
            action = new TransferData(3, info);
            String tmp = UserInfo.server.submit(action).get();
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    } // getData() END;

    @Override
    protected void onResume() {
        setKidInfo(getData());
        super.onResume();
    }

    // json을 통해 얻은 아이정보를 setting
    public void setKidInfo(String json) {
        try{
            Log.d("처음","미쳐버려");
            // 버튼 상태 초기화
            editIcon.setVisibility(View.VISIBLE);               // 수정하기 버튼 (보여주기)
            KidInfoEnd.setVisibility(View.GONE);                // 수정완료 버튼 (처음엔 보여주면 안되지)

            JSONObject object = new JSONObject(json);
            kidName.setText(object.getString("name"));
            kidBirth.setText(object.getString("year")+"년 "+object.getString("month")+"월");
            kidGrowth.setText(object.getString("height")+"cm"+"/"+object.getString("weight")+"kg");

            // 알러지 토글 버튼 초기화
            user_allergy_list = new  ArrayList<Integer> ();
            String total = object.getString("allergy");
            String[] allergy = total.split(",");
            if (!allergy[0].equals("null")) {
                for (int i = 0; i < allergy.length; i++) {
                    int tmp = Integer.parseInt(allergy[i]);       // 알러지 번호 담김
                    tmp = tmp - 1;                                    // 1부터 시작하니까 여기에 넣을 땐
                    user_allergy_list.add(tmp);                   // user_allergy_list 어레이 리스트에 넣기
                    if (!allergylist[tmp].isChecked()) {
                        allergylist[tmp].setChecked(true);        // 체크상태로 바꿔줘요.
                    }
                }
            }

            for(int a=0;a<3;a++){
                allergyP_7_9[a].setFocusable(false);           // 버튼이 눌려있던 아니던 추가입력부분는
                allergyP_7_9[a].setClickable(false);           // 수정할수 없게하기 위해서 함수 처리한다.
                if(!allergylist[a + 6].isChecked()) {      // 알러지 7-9버튼 중에 눌려지지 않은
                    allergyN_7_9[a].setVisibility(View.GONE);      // 버튼은 보이지 않도록 해줘야 한다.
                    allergyP_7_9[a].setVisibility(View.GONE);}
                else {
                    allergyN_7_9[a].setVisibility(View.VISIBLE);// 7-9 중 눌려있는건 보여주자
                    allergyP_7_9[a].setVisibility(View.VISIBLE);
                }
            }

            // 7-9 알러지 추가 정보 셋팅
            allergyP_7_9[0].setText(object.getString("ainfo1"));
            allergyP_7_9[1].setText(object.getString("ainfo2"));
            allergyP_7_9[2].setText(object.getString("ainfo3"));

            etcText.setText(object.getString("add"));            // 기타주의사항 셋팅하고, 눌리지 않게
            etcText.setClickable(false);
            etcText.setFocusable(false);

            // 질환 토글 버튼 초기화
            user_illness_list = new  ArrayList<Integer> ();
            String total2 = object.getString("illness");
            String[] illness = (object.getString("illness")).split(",");
            for (int i = 0; i < illness.length; i++) {
                int tmp2= Integer.parseInt(illness[i]);        // 질환 번호 담김
                tmp2=tmp2-1;                                   // 1부터 시작하니까 여기에 넣을 땐
                user_illness_list.add(tmp2);                   // user_illness_list 어레이 리스트에 넣기
                if (!illnesslist[tmp2].isChecked()) {
                    illnesslist[tmp2].setChecked(true);
                }
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }  // setKidInfo END ;

    // 수정 버튼 function()
    public void editBtn_clicked(){

        ToggleColor();                          // 클릭되면 색이 바뀌는 리스너 모든 토글에 등록
        Toast.makeText(editIcon.getContext(),"수정을 시작하세요",Toast.LENGTH_SHORT).show();
        etcText.setFocusable(true);             // 기타주의사항 활성화
        etcText.setFocusableInTouchMode(true);
        etcText.setClickable(true);
        KidInfoEnd.setVisibility(View.VISIBLE); // 수정완료 버튼 보여주기
        editIcon.setVisibility(View.GONE);      // 수정하기 버튼을 아예 보이지 않게 한다

        for(int a=0;a<9;a++){
            allergylist[a].setClickable(true);  // 모든 알러지 클릭할 수 있게 한다
        }
        for(int a=0;a<6;a++){
            illnesslist[a].setClickable(true);  // 모든 질환 클릭할 수 있게 한다
        }


        allergylist[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allergylist[6].isChecked()){
                    allergyP_7_9[0].setVisibility(View.VISIBLE);
                    allergyN_7_9[0].setVisibility(View.VISIBLE);
                    allergyP_7_9[0].setFocusable(true);
                    allergyP_7_9[0].setFocusableInTouchMode(true);
                    allergyP_7_9[0].setClickable(true);
                }else{
                    allergyP_7_9[0].setVisibility(View.GONE);
                    allergyN_7_9[0].setVisibility(View.GONE);
                }
            }
        });
        allergylist[7].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allergylist[7].isChecked()){
                    allergyP_7_9[1].setVisibility(View.VISIBLE);
                    allergyN_7_9[1].setVisibility(View.VISIBLE);
                    allergyP_7_9[1].setFocusable(true);
                    allergyP_7_9[1].setFocusableInTouchMode(true);
                    allergyP_7_9[1].setClickable(true);
                }else{
                    allergyP_7_9[1].setVisibility(View.GONE);
                    allergyN_7_9[1].setVisibility(View.GONE);
                }
            }
        });
        allergylist[8].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allergylist[8].isChecked()){
                    allergyP_7_9[2].setVisibility(View.VISIBLE);
                    allergyN_7_9[2].setVisibility(View.VISIBLE);
                    allergyP_7_9[2].setFocusable(true);
                    allergyP_7_9[2].setFocusableInTouchMode(true);
                    allergyP_7_9[2].setClickable(true);
                }else{
                    allergyP_7_9[2].setVisibility(View.GONE);
                    allergyN_7_9[2].setVisibility(View.GONE);
                }
            }
        });
    } // editBtn_clicked END();



    public void sendData() {
        String allergyTmp = null;       // 새로받은 알러지숫자를 1,2,4 같은 형태로 저장해야 한다
        String chronicTmp = null;       // 새로받은 질환숫자를 5,2,1 같은 형태로 저장해야 한다.
        String info7 = null;            // 알러지7의 edittext를 저장한다
        String info8 = null;
        String info9 = null;
        String additional = null;       // 기타 주의 사항 edittext을 저장한다.

        // new_allergy_new에 새롭게 저장된 알러지 숫자를 저장한다.
        ArrayList<Integer> new_allergy_box = new ArrayList<Integer>();
        for (int i = 0; i < 9; i++) {
            if (allergylist[i].isChecked()) {
                int j = i+1;
                new_allergy_box.add(j);
                switch (i) {
                    case 6:
                        info7 = allergyP_7_9[0].getText().toString();
                        break;
                    case 7:
                        info8 = allergyP_7_9[1].getText().toString();
                        break;
                    case 8:
                        info9 = allergyP_7_9[2].getText().toString();
                        break;
                }
            }
        }
        if (new_allergy_box.size() > 0) {
            allergyTmp = "";
            for (int j = 0; j < new_allergy_box.size(); j++) {
                allergyTmp += new_allergy_box.get(j);
                if (j != (new_allergy_box.size()-1)) {
                    allergyTmp += ",";
                }
            }

        }

        // 새로운 질환저장하기
        // new_allergy_new에 새롭게 저장된 알러지 숫자를 저장한다.
        ArrayList<Integer> new_illness_box = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) {
            if (illnesslist[i].isChecked()) {
                int j = i+1;
                new_illness_box.add(j);
            }
        }

        if (new_illness_box.size() > 0) {
            chronicTmp = "";
            for (int j = 0; j < new_illness_box.size(); j++) {
                chronicTmp += new_illness_box.get(j);
                if (j != (new_illness_box.size()-1)) {
                    chronicTmp += ",";
                }
            }
        }
        additional = etcText.getText().toString();

        try {   //통신코드 : 4번
            //인자 : id, 알러지목록, 알러지 7번 추가정보, 알러지 8번 추가정보, 알러지 9번 추가정보, 질환목록, 기타주의사항
            String[] info = {UserInfo.childID+"", allergyTmp, info7, info8, info9, chronicTmp, additional};
            action = new TransferData(4, info);
            UserInfo.server.submit(action);
        } catch (Exception e) {
            e.printStackTrace();
        }

    } // sendData END()


    public void ToggleColor(){
        // allergylist1~6 과 illness1~6의 리스너 생성
        ToggleButton.OnClickListener allergy_and_illness = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToggleButton btn = (ToggleButton) view;
                if(btn.isChecked()){
                    btn.setTextColor(Color.parseColor("#ff6e57"));
                }else{
                    btn.setTextColor(Color.parseColor("#696969"));
                }
            }
        };
        //  리스너 등록
        for(int a=0;a<9;a++){
            allergylist[a].setOnClickListener(allergy_and_illness);
        }
        for(int b=0;b<6;b++){
            illnesslist[b].setOnClickListener(allergy_and_illness);
        }
    } // ToggleColor END()



    public void KidInfoEnd_clicked(){

        KidInfoEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editIcon.setVisibility(View.VISIBLE);       // 수정하기 버튼 다시 나타나기

                for(int a=0;a<9;a++){
                    allergylist[a].setClickable(false);
                }

                for(int a=0;a<6;a++){
                    illnesslist[a].setClickable(false);
                }

                // 눌려지지 않았다면 n,p모두 보여줄 필요 없다 --> gone
                if(allergylist[6].isChecked()==false){
                    allergyN_7_9[0].setVisibility(View.GONE);
                    allergyP_7_9[0].setVisibility(View.GONE);
                }

                if(allergylist[7].isChecked()==false){
                    allergyN_7_9[1].setVisibility(View.GONE);
                    allergyP_7_9[1].setVisibility(View.GONE);
                }

                if(allergylist[8].isChecked()==false){
                    allergyN_7_9[2].setVisibility(View.GONE);
                    allergyP_7_9[2].setVisibility(View.GONE);
                }

                allergyP_7_9[0].setClickable(false);
                allergyP_7_9[1].setClickable(false);
                allergyP_7_9[2].setClickable(false);
                allergyP_7_9[0].setFocusable(false);
                allergyP_7_9[1].setFocusable(false);
                allergyP_7_9[2].setFocusable(false);

                etcText.setFocusable(false);
                etcText.setClickable(false);

                editIcon.setClickable(true);
                KidInfoEnd.setVisibility(View.GONE);



                sendData(); // 서버통신 , 업데이트 적용
                Toast.makeText(KidInfoEnd.getContext(),"수정이 완료되었습니다.",Toast.LENGTH_SHORT).show();

            }
        });
    }   // KidInfoEnd_clicked END();

    public void Graphbtn_clicked(){
        //  백신버튼, 그래프 버튼이 눌렸을 떄 엑티비티 넘어가는 함수
        kidGrowthGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KidInfo.this, Graph.class);
                startActivity(intent);
            }
        });
    } // Graphbtn_clicked END()

    public void Vaccinebtn_clicked(){
        //  백신버튼, 그래프 버튼이 눌렸을 떄 엑티비티 넘어가는 함수
        kidVaccineList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(KidInfo.this, Vaccine.class);
                startActivity(intent);
                Log.e("접근","접근");
            }
        });
    }
}
