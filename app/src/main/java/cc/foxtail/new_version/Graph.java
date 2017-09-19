package cc.foxtail.new_version;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public final class Graph extends BaseActivity {
    //차트에 필요한 정보 저장하는 변수
    private ArrayList<Entry> height = new ArrayList<>();       //아이 키 값 (0~11, 0:1월)
    private ArrayList<Entry> weight = new ArrayList<>();       //아이 몸무게 값 (0~11, 0:1월)
    private ArrayList<String> labels = new ArrayList<String>();     //기록한 달

    //차트를 띄우는 액티비티. 팝업때문에 public화
    public static Activity GraghActivity;
    private LineChart lineChart;
    private TransferData action;
    private int collectNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        /*
        *  1. 액티비티와 View 연결하기
        * */
        GraghActivity = Graph.this;
        lineChart = (LineChart) findViewById(R.id.chart);
        Button graphPlus = (Button) findViewById(R.id.graphPlus);

        graphPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Graph.this, PopupKidInfoPlus.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

         /*
        * 2. 정보 세팅하기
        * 서버에서 정보 불러오기 : getData()
        * 이 정보를 ArrayList 3가지 변수에 각각 저장하기 : setArrayList()
        * */
        setArrayList(getData());

        /*
        * 3. 정보 업데이트하기
        * 팝업이 켜지고 꺼지면 해당 정보 업데이트하기
        * */
        if (!PopupKidInfoPlus.heightNumber.equals("")) {
            updateChart();
            PopupKidInfoPlus.resetNumber();
        }

        LineDataSet datasetH = new LineDataSet(height, "키");
        LineDataSet datasetW = new LineDataSet(weight, "몸무게");

        ArrayList<LineDataSet> data = new ArrayList<>();
        data.add(datasetH);
        data.add(datasetW);

        datasetH.setColors(new int[]{Color.parseColor("#97FFB2")});
        datasetH.setCircleColor(Color.parseColor("#ffffff"));
        datasetH.setCircleSize(3);
        datasetW.setColors(new int[]{Color.parseColor("#35CC31")});
        datasetW.setCircleColor(Color.parseColor("#ffffff"));
        datasetW.setCircleSize(3);

        lineChart.setData(new LineData(labels, data));
        lineChart.animateX(1000);
    }

    // 서버에서 아이의 키와 몸무게 정보를 불러오는 함수
    public String getData() {
        try {
            //통신코드 : 5번
            //인자 : id
            String[] info = {UserInfo.childID+""};
            action = new TransferData(5, info);
            return UserInfo.server.submit(action).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    * 서버에서 온 String을 파싱해서 ArrayList에다가 저장하는 함수
    * 1) JSONObject에서 JSONArray 각각 뽑아내기
    * 2) JSONArray에 있는 정보를 ArrayList로 옮기기
    * */
    public void setArrayList(String json) {
        try {
            //정보를 json으로 받아오기 때문에 먼저 object로 파싱 (무조건)
            JSONObject object = new JSONObject(json);

            //정보가 각각 array로 묶여 오므로 array로 파싱
            collectNum = Integer.parseInt(object.getString("num"));
            JSONArray h = object.getJSONArray("height");
            JSONArray w = object.getJSONArray("weight");
            JSONArray m = object.getJSONArray("month");

            //ArrayList에 키 저장하기
            height.clear();
            for (int i = 0; i < collectNum; i++) {
                float tmp = Float.parseFloat(h.getString(i));
                height.add(new Entry(tmp, i));
            }

            //ArrayList에 몸무게 저장하기
            weight.clear();
            for (int i = 0; i < collectNum; i++) {
                float tmp = Float.parseFloat(w.getString(i));
                weight.add(new Entry(tmp, i));
            }

            //ArrayList에 달 저장하기
            labels.clear();
            for (int i = 0; i < collectNum; i++) {
                labels.add(m.getString(i)+"월");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * 업데이트 혹은 추가된 정보를 차트에 반영하는 함수
    * 1) 아까 업데이트할 때 쓴 달과 현재 기록된 마지막 달(collectNum)과 비교
    * 2-1) 만약 같으면 업데이트
    * 2-2) 만약 다르면 추가
    * */
    public void updateChart() {
        int num = PopupKidInfoPlus.month;
        float h = Integer.parseInt(PopupKidInfoPlus.heightNumber);
        float w = Integer.parseInt(PopupKidInfoPlus.weightNumber);

        if (num == collectNum) {
            height.set(num-1, new Entry(h, num));
            weight.set(num-1, new Entry(w, num));
        } else {
            height.add(new Entry(h, num));
            weight.add(new Entry(w, num));
            labels.add(num+"월");
        }

        try {
            String[] info = {UserInfo.getChildIdToString(), num + "", h + "", w + ""};
            action = new TransferData(6, info);
            UserInfo.server.submit(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
