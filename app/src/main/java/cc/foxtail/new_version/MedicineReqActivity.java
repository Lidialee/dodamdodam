package cc.foxtail.new_version;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;


public class MedicineReqActivity extends BaseActivity {
    private TransferData action;
    ListView listview;
    GridView gridview;
    SingleAdapter adapter;
    GridAdapter gridAdapter;
    Button editButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        listview = (ListView) findViewById(R.id.medilistview);
        gridview = (GridView) findViewById(R.id.gridView);
        adapter = new SingleAdapter();
        gridAdapter = new GridAdapter();

        try {
            String json = getData();
            setMedicineInfo(json);
            listview.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        editButton = (Button) findViewById(R.id.medicineEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MedicineSetting.class);
                startActivity(intent);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 새로운 싱글아이템이 클릭되면 기본에 그리드어댑터에 저장된거 지우자
                    gridAdapter.removeAll();

                    // 클릭한 싱글아이템 가져오기
                    SingleItem tmp = (SingleItem) adapter.getItem(position);
                    // 위에서 가져온 싱글아이템의 period과 check상태를 가져온다
                    String[] list_check = tmp.getCheckList();
                    int list_period = tmp.getPeriod();

                    // period 수만큼 그리드뷰를 추가해준다.
                    for(int a=0;a<list_period;a++){
                        gridAdapter.addItem(new GridItem(list_check[a]));

                    }
                    // 아이템에 추가한 그리드뷰를 보여준다
                    gridview.setAdapter(gridAdapter);
            }
        });
    }


    public String getData() {
        /*
         * 통신코드 9번
         * * 서버에서 투약의뢰 기록 불러오기
         * * 인자 : { child ID }
         * * 결과값 : { num : 기록수, list : { name : 약이름, date : 날짜, period : 기간, time : 약먹는시간(B,L,D),
         *              isEnd : 현재 진행중인 의뢰인지 여부, check : 5일짜리 체크결과 }의 배열(num) }
         */
        String[] info = { UserInfo.childID+"" };
        try {
            action = new TransferData(9, info);
            return UserInfo.server.submit(action).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 셋팅하는 함수
    public void setMedicineInfo(String json) {
        try {
            // onStart() 때문에 2번 그려지는거 방지.라
            adapter.removeAll();

            JSONObject object = new JSONObject(json);
            int id =1;
            int num = Integer.parseInt(object.getString("num"));
            JSONArray list = object.getJSONArray("list");

            for(int a=list.length()-1;a>=0;--a){
                // 각 아이템별로 가져오기
                JSONObject set = list.getJSONObject(a);
                String name = set.getString("name");
                String data = set.getString("date");
                int period = Integer.parseInt(set.getString("period"));
                String time = set.getString("time");
                int isEnd = Integer.parseInt(set.getString("isEnd"));
                JSONArray check = set.getJSONArray("check");

                // 어뎁터에 정보저장
                adapter.addItem(new SingleItem(id,name,data,time,period,isEnd,check));
                id+=1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 리스트 뷰 각 아이템 클릭이벤트 처리함수
}
