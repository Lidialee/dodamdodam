package cc.foxtail.new_version;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONObject;

/**
 * Created by lidia on 2017. 4. 28..
 */

public class Vaccine extends BaseActivity {

    ImageView pistol_8[];
    Button VaccineEnd, Editbtn;
    private TransferData action;
    private boolean[] isClicked = new boolean[8];           // 이미지의 클릭여부를 확인하는

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);

        // 리소스 가져오기
        VaccineEnd = (Button) findViewById(R.id.VaccineEnd);
        Editbtn = (Button) findViewById(R.id.vaccineEdit);
        pistol_8 = new ImageView[8];
        for(int a=0;a<8;a++){
            int ID = getResources().getIdentifier("pis"+(a+1),"id",getPackageName());
            pistol_8[a] = (ImageView)findViewById(ID);
        }

        //  데이터 가져오기
        setVaccineinfo(getData());
        // 수정완료버튼 보이지 않게 하기
        VaccineEnd.setVisibility(View.GONE);
        // 수정하기 버튼
        Editbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editbtnclicked();
            }
        });
        // 수정완료 버튼
        VaccineEnd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                VaccineBtnClicked();
            }
        });

    } // onCreate() 완료


    public String getData() {
        try {
            //통신코드 : 7번
            //인자 : id
            String[] info = {UserInfo.childID+""};
            action = new TransferData(7, info);
            String json = UserInfo.server.submit(action).get();
            Log.d(json,"정보들어옴 ***");
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public void setVaccineinfo(String json){
        try {

            JSONObject object = new JSONObject(json);
            int DTaP = Integer.parseInt(object.getString("DTaP"));
            int OPV = Integer.parseInt(object.getString("OPV"));
            int MMR = Integer.parseInt(object.getString("MMR"));
            int JEV = Integer.parseInt(object.getString("JEV"));
            int HAV = Integer.parseInt(object.getString("HAV"));
            // 1. 클릭된 횟수를 받으면 그 수에 맞춰서 그림셋팅
            // 2. 그리고 isClicked 설정셋팅
            // DTaP =0,1
            if(DTaP==1){
                pistol_8[0].setImageResource(R.drawable.vaccine_true);
                isClicked[0]=true;
            }
            // OPV =0,1
            if(OPV==1){
                pistol_8[1].setImageResource(R.drawable.vaccine_true);
                isClicked[1]=true;
            }
            // MMR = 0,1
            if(MMR==1){
                pistol_8[2].setImageResource(R.drawable.vaccine_true);
                isClicked[2]=true;
            }
            // JEV=0,1,2,3
            for(int a=0;a<JEV;a++){
                pistol_8[a+3].setImageResource(R.drawable.vaccine_true);
                isClicked[a+3]=true;
            }
            // JEV=0,1,2
            for(int a=0;a<HAV;a++){
                pistol_8[a+6].setImageResource(R.drawable.vaccine_true);
                isClicked[a+6]=true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void sendData() {

        // 예방접종 종류별 isClicked된 갯수
        int pistolNum[] = new int[5];

        // 접종 별 isClicked = true 개수 계산
        if(isClicked[0]==true) pistolNum[0]=1;
        //else pistolNum[0]=0;
        if(isClicked[1]==true) pistolNum[1]=1;
        //else pistolNum[1]=0;
        if(isClicked[2]==true) pistolNum[2]=1;
        //else pistolNum[2]=0;
        for(int i=0;i<3;i++){
            if(isClicked[i+3]==true) pistolNum[3]+=1;
        }
        for(int i=0;i<2;i++){
            if(isClicked[i+6]==true) pistolNum[4]+=1;
        }
        try {
            //통신코드 : 8번
            //인자 : id, 디프테리아/파상풍/백일해 횟수, 폴리오 횟수, 홍역/이하선염 횟수, 일본뇌염 횟수, A형간염 횟수
            String[] info = {UserInfo.childID+"", pistolNum[0]+"",
                    pistolNum[1]+"", pistolNum[2]+"",
                    pistolNum[3]+"", pistolNum[4]+""};
            action = new TransferData(8, info);
            UserInfo.server.submit(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void editbtnclicked(){
        // 수정완료(보이기) , 수정하기(사라짐)
        VaccineEnd.setVisibility(View.VISIBLE);
        Editbtn.setVisibility(View.GONE);

        // 클릭할 수 있는 상태로 만들어 주기
        for(int a=0;a<8;a++){
            pistol_8[a].setPressed(true);
        }

        // 각각의 피스톨이 눌리면
        // 1. 이미지 셋팅
        // 2. isClicked => true
        for(int a=0;a<8;a++){
            Log.d("   T/F- "+isClicked[a],"edit클릭시 그림별 체크상황***");
        }
        pistol_8[0].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView Image = (ImageView) v;
                if(isClicked[0]==true){
                    isClicked[0]=false;
                    Image.setImageResource(R.drawable.vaccine_false);
                    Log.d("***","isChecked = true로 들어옴");
                }
                else {
                    isClicked[0]=true;
                    Image.setImageResource(R.drawable.vaccine_true);
                    Log.d("***","isChecked = false로 들어옴");
                }
            }
        });
        pistol_8[1].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView Image = (ImageView) v;
                if(isClicked[1]==true){
                    Image.setImageResource(R.drawable.vaccine_false);
                    Log.d("***","isChecked = true로 들어옴");
                    isClicked[1]=false;
                }
                else {
                    Image.setImageResource(R.drawable.vaccine_true);
                    Log.d("***","isChecked = false로 들어옴");
                    isClicked[1]=true;
                }
            }
        });
        pistol_8[2].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView Image = (ImageView) v;
                if(isClicked[2]==true){
                    Image.setImageResource(R.drawable.vaccine_false);
                    Log.d("***","isChecked = true로 들어옴");
                    isClicked[2]=false;
                }
                else {
                    Image.setImageResource(R.drawable.vaccine_true);
                    Log.d("***","isChecked = false로 들어옴");
                    isClicked[2]=true;
                }
            }
        });
        pistol_8[3].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView Image = (ImageView) v;
                if(isClicked[3]==true){
                    Image.setImageResource(R.drawable.vaccine_false);
                    Log.d("***","isChecked = true로 들어옴");
                    isClicked[3]=false;
                }
                else {
                    Image.setImageResource(R.drawable.vaccine_true);
                    Log.d("***","isChecked = false로 들어옴");
                    isClicked[3]=true;
                }
            }
        });
        pistol_8[4].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView Image = (ImageView) v;
                if(isClicked[4]==true){
                    Image.setImageResource(R.drawable.vaccine_false);
                    Log.d("***","isChecked = true로 들어옴");
                    isClicked[4]=false;
                }
                else {
                    Image.setImageResource(R.drawable.vaccine_true);
                    Log.d("***","isChecked = false로 들어옴");
                    isClicked[4]=true;
                }
            }
        });
        pistol_8[5].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView Image = (ImageView) v;
                if(isClicked[5]==true){
                    Image.setImageResource(R.drawable.vaccine_false);
                    Log.d("***","isChecked = true로 들어옴");
                    isClicked[5]=false;
                }
                else {
                    Image.setImageResource(R.drawable.vaccine_true);
                    Log.d("***","isChecked = false로 들어옴");
                    isClicked[5]=true;
                }
            }
        });
        pistol_8[6].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView Image = (ImageView) v;
                if(isClicked[6]==true){
                    Image.setImageResource(R.drawable.vaccine_false);
                    Log.d("***","isChecked = true로 들어옴");
                    isClicked[6]=false;
                }
                else {
                    Image.setImageResource(R.drawable.vaccine_true);
                    Log.d("***","isChecked = false로 들어옴");
                    isClicked[6]=true;
                }
            }
        });
        pistol_8[7].setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView Image = (ImageView) v;
                if(isClicked[7]==true){
                    Image.setImageResource(R.drawable.vaccine_false);
                    Log.d("***","isChecked = true로 들어옴");
                    isClicked[7]=false;
                }
                else {
                    Image.setImageResource(R.drawable.vaccine_true);
                    Log.d("***","isChecked = false로 들어옴");
                    isClicked[7]=true;
                }
            }
        });

    }
    public void VaccineBtnClicked(){
        // 더 이상 눌리지 않도록 만들어 준다
        for(int i=0;i<8;i++){
            pistol_8[i].setClickable(false);
        }
        // 수정완료(사라짐) , 수정하기(보여주기)
        Editbtn.setVisibility(View.VISIBLE);
        VaccineEnd.setVisibility(View.INVISIBLE);

        // 수정된 정보를 서버에 보내기
        sendData();
    }
}
