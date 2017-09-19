package cc.foxtail.new_version;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

// single_item에서 정의한 레이아웃을 하나의 뷰로 만들기 위한 새로운 뷰설정

public class GridItemView extends RelativeLayout {

    ImageView imageView;
    int checkImage = R.drawable.medicine_check;
    int xImage = R.drawable.medicine_check_fail;
    int noImage = R.drawable.medicine_check_no;

    public GridItemView(Context context) {
        super(context);
        init(context);
    }

    public GridItemView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context);
    }
    // 한 아이템을 표현한 레이아웃에서 각 뷰들을 참조를 따오고 있다.
    private void init(Context context) {
        LayoutInflater inflator
                = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflator.inflate(R.layout.medicine_grid,this,true);
        imageView = (ImageView) findViewById(R.id.medicineCheckBox);

    }

    // 1은 @drawable/체크이미지, 2는 @drawable/x이미지,  0은 @drawable/ 빈칸으로 setDrawableImage
    public void setImageView(String checkState){
        switch (checkState){
            case "1":
                imageView.setImageResource(checkImage);
                break;
            case "2":
                imageView.setImageResource(xImage);
                break;
            case "0":
                imageView.setImageResource(noImage);
                break;
            default:
                imageView.setImageResource(noImage);
                break;
        }
    }

}
