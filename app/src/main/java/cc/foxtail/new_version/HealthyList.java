package cc.foxtail.new_version;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

public class HealthyList extends BaseActivity {
    private TransferData action;
    HealthyListAdapter healthyListAdapter;
    ListView healthyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_list);

        healthyList = (ListView) findViewById(R.id.healthyList);
        healthyListAdapter = new HealthyListAdapter(this);

        // 정보가져오고 셋팅합니다
        try {
            String json = getData();
            setHealtyInfo(json);
            healthyList.setAdapter(healthyListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    } // onCreate() end



    /*
    * 건강기록보기 getData()
    *
    * 결과값 주의사항
    * 1. manage = 가정, 병원, 원내, 귀가 중 하나가 옵니다 (한글로)
    * 2. part, mainClass, subClass는 '건강기록등록'의 버튼명대로 옵니다 (한글로)
    * 3. subClass와 detail은 아무런 내용이 없을 수도 있습니다
    * 4. id는 그다지 중요하지 않습니다
    */
    public String getData() {
        /*
         * 통신코드 12번
         * 서버에서 건강기록 리스트 받아오기
         * 인자 : { child ID }
         * 결과값 : { num : 갯수, list : {
         *               id: 기록id, manage : 조치(어디서), part : 부위, mainClass : 큰버튼, subClass : 작은버튼, detail : 상세설명 }배열(num) }
         * */
        String[] info = {UserInfo.childID + ""};
        try {
            action = new TransferData(12, info);
            return UserInfo.server.submit(action).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // 셋팅하는 함수
    public void setHealtyInfo(String json) {
        try {
            healthyListAdapter.removeAll();

            JSONObject object = new JSONObject(json);
            JSONArray list = object.getJSONArray("list");
            int length = list.length()-1;

            for(int a=length;a>=0;--a){
                JSONObject set = list.getJSONObject(a);
                String id = set.getString("id");
                String manage_where = set.getString("manage");
                String first_part = set.getString("part");
                String secend_part = set.getString("mainClass");
                String third_part = set.getString("subClass");
                String detail = set.getString("detail");
                // 어뎁터에 정보저장
                healthyListAdapter.addItem(new HealthyCardItem(id,manage_where,first_part,secend_part,third_part,detail));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
