package cn.tianyuan.common.view.picker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/15.
 */


public class BasePickerView extends View {

    public static final String TAG = BasePickerView.class.getSimpleName();
    /**
     * text之间间距和minTextSize之比
     */
    public static final float MARGIN_ALPHA = 2.5f;
    /**
     * 自动回滚到中间的速度
     */
    public static final float SPEED = 2;

    private List<String> mDataList;
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private int mLastSelected;
    private Paint mPaint;

    private float mMaxTextSize = 80;
    private float mMinTextSize = 40;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;

    private int mColorText = 0x333333;

    private int mViewHeight;
    private int mViewWidth;

    private float mLastDownY;
    /**
     * 滑动的距离
     */
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private Timer timer;
    private MyTimerTask mTask;

    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                if (mTask != null) {
                    mTask.cancel();
                    mTask = null;
                    performSelect();
                }
            } else {
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
            }
            invalidate();
        }

    };

    public BasePickerView(Context context) {
        super(context);
        init();
    }

    public BasePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null) {
            if(mCurrentSelected == mLastSelected){
                return;
            }
            mLastSelected = mCurrentSelected;
            mSelectListener.onSelectChanged(mDataList.get(mCurrentSelected), mCurrentSelected);
        }
    }

    public void setData(List<String> datas) {
        Log.d(TAG, "setData: "+datas);
        mDataList = datas;
        mCurrentSelected = 0;
        mLastSelected = 0;
        invalidate();
    }

    public void setSelected(int selected) {
        mCurrentSelected = selected;
        mLastSelected = selected;
    }

    private boolean moveHeadToTail() {
        ++mCurrentSelected;
        if (mCurrentSelected >= mDataList.size()) {
            mCurrentSelected = mDataList.size() - 1;
            if (mCurrentSelected < 0) {
                mCurrentSelected = 0;
            }
            return false;
        }
        return true;
    }

    private boolean moveTailToHead() {
        --mCurrentSelected;
        if (mCurrentSelected < 0) {
            mCurrentSelected = 0;
            return false;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
        mMaxTextSize = mViewHeight / 4.0f;
        mMinTextSize = mMaxTextSize / 2f;
        isInit = true;
        invalidate();
    }

    private void init() {
        timer = new Timer();
        mDataList = new ArrayList<String>();
        mCurrentSelected = 0;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mColorText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 根据index绘制view
        if (isInit) {
            if(mDataList != null && mDataList.size() > 0) {
                drawData(canvas);
            }
        }
    }

    private void drawData(Canvas canvas) {
        // 先绘制选中的text再往上往下绘制其余的text
        float scale = parabola(mViewHeight / 4.0f, mMoveLen);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));

        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0 && i < 3; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size() && i < 3; i++) {
            drawOtherText(canvas, i, 1);
        }

    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type
                * mMoveLen);
        float scale = parabola(mViewHeight / 4.0f, d);
        float size = (mMaxTextSize - mMinTextSize) * scale + mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) ((mMaxTextAlpha - mMinTextAlpha) * scale + mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));
        canvas.drawText(mDataList.get(mCurrentSelected + type * position),
                (float) (mViewWidth / 2.0), baseline, mPaint);
    }

    /**
     * 抛物线
     *
     * @param zero 零点坐标
     * @param x    偏移量
     * @return scale
     */
    private float parabola(float zero, float x) {
        float f = (float) (1 - Math.pow(x / zero, 2));
        return f < 0 ? 0 : f;
    }

    private Point clickPoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                clickPoint = new Point(event.getX(), event.getY(), System.currentTimeMillis());
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                if(clickPoint.isClick(event.getX(), event.getY(), System.currentTimeMillis())){
                    float offsetY = (mViewHeight/2) -  event.getY();
                    if(Math.abs(offsetY) < mViewHeight / 4){
                        performSelect();
                    } else {
                        if(offsetY < 0){
                            if((mCurrentSelected + 1) < mDataList.size()){
                                setSelected(mCurrentSelected+1);
                                performSelect();
                            }
                        } else {
                            if(mCurrentSelected > 0){
                                setSelected(mCurrentSelected-1);
                                performSelect();
                            }
                        }
                    }
                }
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {
        mMoveLen += (event.getY() - mLastDownY);
        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            if (moveTailToHead()) {
                mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
            } else {
                mMoveLen -= (event.getY() - mLastDownY);
            }
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            if(moveHeadToTail()){
                mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
            } else {
                mMoveLen -= (event.getY() - mLastDownY);
            }
        }
        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 10);
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }
    }

    public class Point{
        public float x,y;
        public long time;

        public Point(float x, float y, long time){
            this.x = x;
            this.y = y;
            this.time = time;
        }

        public boolean isClick(float x, float y, long time){
            if(Math.abs(this.time - time) > 200){
                return false;
            }
            if(Math.abs(this.x - x) > 10){
                return false;
            }
            if(Math.abs(this.y - y) > 10){
                return false;
            }
            return true;
        }
    }

    public interface onSelectListener {
        void onSelectChanged(String text, int position);
    }
}
