package cc.foxtail.new_version;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

class Pen {
    // 하나의 Pen 셋팅 이후 값을 변경하지 못하게 하기 위해 final로 선언해준다
    final Path mPath;
    final int mColor;

    Pen(Path path, int color) {
        this.mPath = path;
        this.mColor = color;
    }
}
public class CanvasView extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;
    private Context mContext;
    public ArrayList<Pen> mPens = new ArrayList<>();
    private Path mPath;
    private int mColor;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        mContext = c;
        mColor =Color.BLACK;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        for (Pen e : mPens) {
            mPaint.setColor(e.mColor);
            canvas.drawPath(e.mPath, mPaint);
        }
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mX = x;
        mY = y;
        mPath = new Path();
        mPath.moveTo(x, y);
        mPens.add(new Pen(mPath, mColor));
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);        // 이동한 차이
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            if (mPath != null) {
                // 움직일때마다 거기까지 그려주는 lineTo를 실행
                mPath.lineTo(x, y);
            }
        }
    }
    // when ACTION_UP stop touch
    private void upTouch() {  }

    // 투약에서 어레이리스트 접근하기 위한 getter
    public ArrayList<Pen> getPens(){
        return mPens;
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();     // 터치 시작의 x y
        float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                startTouch(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x,y);
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                break;
        }
        invalidate();
        return true;
    }
}
