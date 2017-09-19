package cc.foxtail.new_version;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.GregorianCalendar;

public final class PopupKidInfoPlus extends BaseActivity {

    public static int month;
    public static String heightNumber = "";
    public static String weightNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Activity aActivity = Graph.GraghActivity;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_kidinfoplus);

        final EditText heightNum = (EditText) findViewById(R.id.heightNumber);
        final EditText weightNum = (EditText) findViewById(R.id.weightNumber);

        Button endKidInfoPlus = (Button) findViewById(R.id.endKidInfoPlus);

        endKidInfoPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GregorianCalendar cal = new GregorianCalendar ( );
                month = cal.get(Calendar.MONTH)+1; //이번달

                heightNumber = heightNum.getText().toString();
                weightNumber = weightNum.getText().toString();

                Intent intent = new Intent(PopupKidInfoPlus.this, Graph.class);
                startActivity(intent);
                aActivity.finish();
                finish();
            }
        });
    }

    public static void resetNumber() {
        heightNumber = "";
        weightNumber = "";
    }


}
