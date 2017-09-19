package cc.foxtail.new_version;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HealthyCard extends LinearLayout {

    private TextView healthyWhere, topButton, midButton, botButton, healthyDetail;

    public HealthyCard(Context context, HealthyCardItem item) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.healty_card, this, true);

        healthyWhere = (TextView) findViewById(R.id.healthyWhere);
        topButton = (TextView) findViewById(R.id.topButton);
        midButton = (TextView) findViewById(R.id.midButton);
        botButton = (TextView) findViewById(R.id.botButton);
        healthyDetail = (TextView) findViewById(R.id.healthyDetail);

        Typeface mytype = Typeface.createFromAsset(context.getAssets(), "BMJUA_ttf.ttf");
        healthyWhere.setTypeface(mytype);
        topButton.setTypeface(mytype);
        midButton.setTypeface(mytype);
        botButton.setTypeface(mytype);
        healthyDetail.setTypeface(mytype);

        healthyWhere.setText(item.getHealthyWhere());
        topButton.setText(item.getTopButton());
        midButton.setText(item.getMidButton());
        botButton.setText(item.getBotButtone());
        healthyDetail.setText(item.getHealthyDetail());

    }

}
