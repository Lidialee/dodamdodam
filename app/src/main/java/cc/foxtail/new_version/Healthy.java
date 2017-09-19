package cc.foxtail.new_version;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Healthy extends BaseActivity {

    Button kidHead, kidHand, kidStomach, kidLeg, healthyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy2);
        // 뷰가져오기
        healthyList = (Button) findViewById(R.id.healthyList);
        kidHead = (Button) findViewById(R.id.kidHead);
        kidHand = (Button) findViewById(R.id.kidHand);
        kidStomach = (Button) findViewById(R.id.kidStomach);
        kidLeg = (Button) findViewById(R.id.kidLeg);

        // 리스너 등록
        healthyList.setOnClickListener(listener);
        kidHead.setOnClickListener(listener);
        kidHand.setOnClickListener(listener);
        kidStomach.setOnClickListener(listener);
        kidLeg.setOnClickListener(listener);
    }

    // 5개 버튼 리스너 한번에 처리하기
    Button.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.healthyList:
                    intent = new Intent(Healthy.this, HealthyList.class);
                    startActivity(intent);
                    break;
                case R.id.kidHead:
                    intent = new Intent(Healthy.this, PopupHead.class);
                    intent.putExtra("FISRT","head");
                    startActivity(intent);
                    break;
                case R.id.kidHand:
                    intent = new Intent(Healthy.this, PopupHand.class);
                    startActivity(intent);
                    intent.putExtra("FISRT","hand");
                    break;
                case R.id.kidStomach:
                    intent = new Intent(Healthy.this, PopupStomahe.class);
                    startActivity(intent);
                    intent.putExtra("FISRT","belly");
                    break;
                case R.id.kidLeg:
                    intent = new Intent(Healthy.this, PopupLeg.class);
                    startActivity(intent);
                    intent.putExtra("FISRT","leg");
                    break;
            }
        }
    };
}
