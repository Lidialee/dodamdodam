package cc.foxtail.new_version;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MembershipText extends LinearLayout {

    private TextView text;

    public MembershipText(Context context, MembershipItem item) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.membership_item, this, true);

        Typeface mytype = Typeface.createFromAsset(context.getAssets(), "BMJUA_ttf.ttf");
        text = (TextView) findViewById(R.id.item);
        text.setTypeface(mytype);
        text.setText(item.getname()+" <"+item.getnumber()+">");
    }




}
