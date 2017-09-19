package cc.foxtail.new_version;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class MedicineSetting extends BaseActivity {
    private TransferData action;
    GregorianCalendar calen = (GregorianCalendar) Calendar.getInstance();
    GregorianCalendar today = (GregorianCalendar) Calendar.getInstance();
    int year, month,date;
    DatePickerDialog dialog;

    TextView selectedDay;
    EditText medicineName,period,howMuch,toTeach;
    ToggleButton breakfast,lunch,dinner;
    CanvasView canvasView;
    Button editFinish;
    String selectedTime="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_setting2);

        // 아이템 가져오기
        selectedDay = (TextView) findViewById(R.id.selected_day);
        medicineName = (EditText) findViewById(R.id.medicineNameEdit);
        period = (EditText) findViewById(R.id.periodEdit);
        howMuch = (EditText) findViewById(R.id.howmuchEdit);
        toTeach = (EditText) findViewById(R.id.toteacherEdit);
        breakfast = (ToggleButton) findViewById(R.id.breakfast);
        lunch = (ToggleButton) findViewById(R.id.lunch);
        dinner = (ToggleButton) findViewById(R.id.dinner);
        canvasView = (CanvasView) findViewById(R.id.signature_canvas);
        editFinish = (Button) findViewById(R.id.endButton);

        // 당일 년 / 달 / 일 가져와서 다이알로그에 셋팅하기
        year = calen.get(calen.YEAR);
        month = calen.get(calen.MONTH);
        date = today.get(today.DAY_OF_MONTH);
        dialog = new DatePickerDialog(this, listener, year, month, date);

        // 토글버튼에 리스너 등록하기
        breakfast.setOnClickListener(clickListener);
        lunch.setOnClickListener(clickListener);
        dinner.setOnClickListener(clickListener);

        // 선택되면 다이알로그가 나타날수 있도록 콜벡함수등록
        selectedDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        // 약 이름 터치하는 순간 기본에 있는 텍스트없애버려.
        medicineName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                medicineName.setText("");
                return false;
            }
        });

        editFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkState()){
                    sendData();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getBaseContext(),"투약 요청이 완료되었습니다", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    // 데이터피커 이벤트 리스너 ( 콜백함수인듯 따로 onCreate()로 부르지 않아도 된다 )
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {
            monthOfYear+=1;
            Toast.makeText(getApplicationContext(), year + "." + monthOfYear + "." + dayOfMonth+"으로 선택하셨습니다", Toast.LENGTH_LONG).show();
            selectedDay.setText(year + "." + monthOfYear + "." + dayOfMonth);

        }
    };

    // 토글버튼 클릭 리스너 ( 2중 터치막기 )
    public ToggleButton.OnClickListener clickListener = new ToggleButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            ToggleButton button = (ToggleButton) v;
            String result;
            switch (button.getId()){
                case R.id.breakfast:
                    selectedTime="B";
                    lunch.setChecked(false);
                    dinner.setChecked(false);
                    break;
                case R.id.lunch:
                    selectedTime="L";
                    breakfast.setChecked(false);
                    dinner.setChecked(false);
                    break;
                case R.id.dinner:
                    selectedTime="D";
                    lunch.setChecked(false);
                    breakfast.setChecked(false);
                    break;
            }
        }
    };

    // 서명여부를 확인
    public boolean checkPenState(){
        ArrayList<Pen> pens = canvasView.getPens();
        if(pens.isEmpty()){
            return true;
        }else return false;
    }

    // 정보보내기
    public void sendData() {
        // 5. 용법용량+주의사항 합쳐서 보내기 ( 구분은 *로 합니다 )
        String total_info = howMuch.getText().toString()+"*"+toTeach.getText().toString();

        try {
            String[] info = { UserInfo.childID+"",
                    medicineName.getText().toString(),
                    selectedDay.getText().toString(),
                    period.getText().toString(),
                    selectedTime,
                    total_info};

            action = new TransferData(10, info);
            UserInfo.server.submit(action);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean checkState(){
        String a = medicineName.getText().toString();
        String b = selectedDay.getText().toString();
        String c = period.getText().toString();

        if(checkPenState()){
            Toast.makeText(this,"서명해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(selectedTime.equals("")){
            Toast.makeText(this,"복용 시간을 선택해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(a.equals("")){
            Toast.makeText(this,"약 이름을 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(b.equals("")){
            Toast.makeText(this,"복용 시작일을 선택해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(c.equals("")){
            Toast.makeText(this,"몇일간 복용해야 하는지 입력해주세요",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
