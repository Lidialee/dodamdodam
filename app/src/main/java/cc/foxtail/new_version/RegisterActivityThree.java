package cc.foxtail.new_version;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterActivityThree extends BaseActivity {
    private TransferData action;
    Spinner spinner;
    ArrayAdapter adapter;
    ListView listView;
    Button end_button;
    MembershipListAdapter listAdapter;
    String kindergarden_name,kindergarden_id,class_name;
    String final_kindergardenID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_kindergarten);

        spinner = (Spinner) findViewById(R.id.spinner);
        listView = (ListView) findViewById(R.id.kindergarten);
        end_button = (Button) findViewById(R.id.regiEnd);
        listAdapter = new MembershipListAdapter(this);

        adapter = ArrayAdapter.createFromResource(this, R.array.city, R.layout.layout);
        spinner.setAdapter(adapter);

        // 스피너 리스너 등록
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String str = (String) spinner.getSelectedItem();
                try {
                    setKindergardenInfo(getData(),str);
                    listView.setAdapter(listAdapter);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"지역을 선택해주세요",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"아무것도 선택하지 않았습니다",Toast.LENGTH_LONG).show();
            }
        });

        // 리스트뷰 리스너 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MembershipItem tmp = (MembershipItem) listAdapter.getItem(position);
                final_kindergardenID =tmp.getId();
                Toast.makeText(getApplicationContext(),
                        tmp.getname()+" "+tmp.getnumber()+"을 선택하셨습니다",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 회원가입 종료버튼
        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(final_kindergardenID.equals("")) {
                    Toast.makeText(getApplicationContext(), "유치원은 선택해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    // 아이의 아이디와 선택한 유치원의 아이디를 보내준다
                    sendData();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    } // onCraete() end

    /*
 * 통신코드 17번
 * * 서버에서 가입되어있는 유치원 리스트 가져오기
 * * 결과값 : { suwon : 수원지역 유치원 배열, seoul : 서울지역 유치원 배열, anyang : 안양지원 유치원 배열,
 *              gawcheon : 과천지역 유치원 배열, yongin : 용인지역 유치원 배열 }
 *           * 유치원 배열 형태 : { id : 유치원ID, center : 유치원이름, class : 반이름 }의 배열
 * */
    private String getData() {
        action = new TransferData(17);
        try {
            String result = UserInfo.server.submit(action).get();
            Log.d("getData",result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // getData()에서 받은 정보에서 스피너 값에 리스트뷰에 뿌리자
    private void setKindergardenInfo(String from_getData,String kor_city){
        try {
            listAdapter.removeAll();
            JSONObject object = new JSONObject(from_getData);
            String eng_name;
            JSONArray jsonCity=null;

            switch (kor_city){
                case "서울":
                    eng_name="seoul";
                    jsonCity = object.getJSONArray(eng_name);
                    break;
                case "수원":
                    eng_name="sowon";
                    jsonCity = object.getJSONArray(eng_name);
                    break;
                case "안양":
                    eng_name="anyang";
                    jsonCity = object.getJSONArray(eng_name);
                    break;
                case "과천":
                    eng_name="gwacheon";
                    jsonCity = object.getJSONArray(eng_name);
                    break;
                case "용인":
                    eng_name="yongin";
                    jsonCity = object.getJSONArray(eng_name);
                    break;
            }
            for (int i = 0; i < jsonCity.length(); i++) {
                JSONObject set = jsonCity.getJSONObject(i);
                kindergarden_name = set.getString("center");
                kindergarden_id = set.getString("id");
                class_name = set.getString("class");
                listAdapter.addItem(new MembershipItem(kindergarden_id,kindergarden_name,class_name));
            }
        }catch (Exception e){}
    }

    /*
    * 통신코드 18번
    * * 서버에 아이의 유치원 반을 등록하기
    * * 인자 : 아이ID, 유치원ID (유치원ID는 리스트 아이템 안에서 얻어오기)
    * */
    private void sendData() {
        String[] info = { UserInfo.childID+"", final_kindergardenID };
        try {
            action = new TransferData(18, info);
            UserInfo.server.submit(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
