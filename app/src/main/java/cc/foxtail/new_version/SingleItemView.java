package cc.foxtail.new_version;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SingleItemView extends RelativeLayout {

    TextView medicine_name, medicine_day, medicine_time, textViewColor;

    public SingleItemView(Context context) {
        super(context);
        init(context);
    }
    public SingleItemView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }
    // 한 아이템을 표현한 레이아웃에서 각 뷰들을 참조를 따오고 있다.
    private void init(Context context) {
        // 뷰 인플레이트하고나서 뷰 가져오기
        LayoutInflater inflator
                = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflator.inflate(R.layout.medicine_card,this,true);
        medicine_name = (TextView) findViewById(R.id.medicineName);
        medicine_day = (TextView) findViewById(R.id.medicineDay);
        medicine_time = (TextView) findViewById(R.id.medicineTime);
        textViewColor = (TextView) findViewById(R.id.checkBackground);

        Typeface mytype = Typeface.createFromAsset(context.getAssets(), "BMJUA_ttf.ttf");
        medicine_name.setTypeface(mytype);
        medicine_day.setTypeface(mytype);
        medicine_time.setTypeface(mytype);

    }
    public void setName(String name){
        medicine_name.setText(name);
    }
    public void setDate(String date)  { medicine_day.setText(date);  }
    public void setTime(String time){
        medicine_time.setText(time);
    }
    public void setCheckBackground(int check){
        textViewColor.setBackgroundColor(check);
    }

}
