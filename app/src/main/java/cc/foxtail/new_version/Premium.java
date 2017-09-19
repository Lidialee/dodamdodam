package cc.foxtail.new_version;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Premium extends BaseActivity {

    Button premium1, premium2;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        premium1 = (Button) findViewById(R.id.premium1);
        premium2 = (Button) findViewById(R.id.premium2);

        premium1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogProgress();
            }
        });

        premium2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogProgress();
            }
        });

    }

    private void DialogProgress() {
        dialog = ProgressDialog.show(Premium.this, "", "결제창으로 이동 중입니다.", true);
        // 창을 내린다.
        dialog.setCanceledOnTouchOutside(true);
        //dialog.dismiss();
    }
}
