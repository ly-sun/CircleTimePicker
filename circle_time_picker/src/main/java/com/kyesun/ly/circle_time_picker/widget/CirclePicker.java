package com.kyesun.ly.circle_time_picker.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.kyesun.ly.circle_time_picker.R;
import com.kyesun.ly.circle_time_picker.callback.OnTimeChangeListener;

import static android.os.Build.VERSION_CODES.M;


/**
 * @version V1.0
 * @Author ly
 * @Description
 * @Date 2017/12/6
 */
public class CirclePicker extends View {
    private final Context context;
    private float mStartDegree; //开始按钮的进度
    private float mEndDegree; //结束按钮的进度
    private int mRingDefaultColor;//设置底色
    private int mBtnSize; //按键的宽度
    private float mStartBtnAngle;  //开始按钮的旋转角度
    private float mEndBtnAngle; //结束按钮的旋转角度

    private int mBtnImgSize;  //按钮图片的宽度
    private float mStartBtnCurX, mStartBtnCurY; //开始按钮中心的位置
    private float mEndBtnCurX, mEndBtnCurY; //结束按钮中心的位置

    private Paint mCirclePaint; //圆环的画笔
    private Paint mProgressPaint;//选中的画笔
    private Bitmap mClockBg;
    private int mDegreeCycle;

    private Bitmap mStartBtnBg;//开始按钮背景图
    private Bitmap mEndBtnBg;//结束按钮背景图

    private int mMinViewSize;//控件的最小尺寸
    private int mClockSize;//时钟的最小尺寸
    private Paint mDefaultPaint;
    private int mCenterY;
    private int mCenterX;
    private int mWheelRadius;
    private int mMoveFlag;//1,代表开始按钮,2,代表结束按钮
    private OnTimeChangeListener mOnTimeChangeListener;
    private int mStartBtnColor;
    private int mEndBtnColor;
    private Paint mStartBtnPaint;
    private Paint mEndBtnPaint;
    private float mLastEventX;
    private float mLastEventY;

    public CirclePicker(Context context) {
        this(context, null);
    }

    public CirclePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        自定义属性
        initAttrs(attrs, defStyleAttr);
//      初始化画笔
        initPaints();
//        初始化
        initValue();
    }

    private void initValue() {
        mMoveFlag = -1;
    }


    //初始化属性
    private void initAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.Circle_Picker, defStyle, 0);

//       角度的最大周期
        mDegreeCycle = typedArray.getInt(R.styleable.Circle_Picker_Degree_Cycle, 720);
//      开始时间的圆盘角度
        mStartDegree = typedArray.getFloat(R.styleable.Circle_Picker_Start_Degree, 0);
//        结束时间的圆盘角度
        mEndDegree = typedArray.getFloat(R.styleable.Circle_Picker_End_Degree, 45);

        if (mStartDegree > mDegreeCycle)
            mStartDegree = mStartDegree % mDegreeCycle;

        if (mEndDegree > mDegreeCycle)
            mEndDegree = mEndDegree % mDegreeCycle;

//        开始按钮的背景
        int startBtnBgId = typedArray.getResourceId(R.styleable.Circle_Picker_Start_Btn_Bg, R.mipmap.icon_circle_picker_start_btn);
        mStartBtnBg = BitmapFactory.decodeResource(getResources(), startBtnBgId);

        int endBtnBgId = typedArray.getResourceId(R.styleable.Circle_Picker_End_Btn_Bg, R.mipmap.icon_circle_picker_end_btn);
        mEndBtnBg = BitmapFactory.decodeResource(getResources(), endBtnBgId);

//        按钮图片的宽度
        mBtnImgSize = Math.max(Math.max(mStartBtnBg.getWidth(), mStartBtnBg.getHeight()), Math.max(mEndBtnBg.getWidth(), mEndBtnBg.getHeight()));
//      按钮图片与背景的差值
        int mBtnOffset = typedArray.getInt(R.styleable.Circle_Picker_Btn_Offset_Size, 8);
//        按键的宽度
        mBtnSize = typedArray.getInt(R.styleable.Circle_Picker_Btn_Width, mBtnImgSize + mBtnOffset);

//        时钟的背景
        int clockBgId = typedArray.getResourceId(R.styleable.Circle_Picker_Clock_Bg, R.mipmap.setalarm_colock_bg);
        mClockBg = BitmapFactory.decodeResource(getResources(), clockBgId);
        mClockSize = Math.max(mClockBg.getWidth(), mClockBg.getHeight());

        mMinViewSize = mBtnSize * 2 + mClockSize;


//        进度条的颜色
        mRingDefaultColor = typedArray.getColor(R.styleable.Circle_Picker_Ring_Default_Color, getColor(R.color.ring_default_color));
//        开始按钮的颜色
        mStartBtnColor = typedArray.getColor(R.styleable.Circle_Picker_Start_Btn_Color, Color.parseColor("#7c85e6"));
//        结束按钮的颜色
        mEndBtnColor = typedArray.getColor(R.styleable.Circle_Picker_End_Btn_Color, Color.parseColor("#bf7be0"));

        typedArray.recycle();
    }

    private void initPaints() {

        mDefaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDefaultPaint.setDither(false);

        /*
         * 圆环的默认画笔
         */
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(false);
        mCirclePaint.setColor(mRingDefaultColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeWidth(mBtnSize);

        /*
        选中区域的画笔(进度条)
         */
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(false);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mBtnSize);


        mStartBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStartBtnPaint.setDither(false);
        mStartBtnPaint.setColor(mStartBtnColor);
        mStartBtnPaint.setStyle(Paint.Style.FILL);

        mEndBtnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEndBtnPaint.setDither(false);
        mEndBtnPaint.setColor(mEndBtnColor);
        mEndBtnPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = Math.max(mMinViewSize, heightSize);
        } else {
            width = Math.max(mMinViewSize, widthSize);
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            height = Math.max(mMinViewSize, widthSize);
        } else {
            height = Math.max(mMinViewSize, heightSize);
        }
        setMeasuredDimension(width, height);
        refreshBtnPosition();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        圆心坐标
        mCenterX = canvas.getWidth() / 2;
        mCenterY = canvas.getHeight() / 2;

//        默认的圆环进度条的半径
        mWheelRadius = (mMinViewSize - mBtnSize) / 2;
        canvas.drawCircle(mCenterX, mCenterY, mWheelRadius, mCirclePaint);

        canvas.drawBitmap(mClockBg, mCenterX - mClockSize / 2, mCenterY - mClockSize / 2, mDefaultPaint);

//      画选中区域
        float begin = 0; //圆弧的起点位置
        float sweep = 0;
        if (mStartBtnAngle > 180 && mStartBtnAngle > mEndBtnAngle) {   //180  -- 360
            begin = -Math.abs(mStartBtnAngle - 360) - 90;
            sweep = Math.abs(Math.abs(mStartBtnAngle - 360) + mEndBtnAngle);
        } else if (mStartBtnAngle > mEndBtnAngle) {
            begin = mStartBtnAngle - 90;
            sweep = 360 - (mStartBtnAngle - mEndBtnAngle);
        } else {
            begin = mStartBtnAngle - 90;
            sweep = Math.abs(mStartBtnAngle - mEndBtnAngle);
        }
        mProgressPaint.setShader(new LinearGradient(mStartBtnCurX, mStartBtnCurY, mEndBtnCurX, mEndBtnCurY, mStartBtnColor, mEndBtnColor, Shader.TileMode.CLAMP));
        canvas.drawArc(new RectF(mCenterX - mWheelRadius, mCenterY - mWheelRadius, mCenterX + mWheelRadius, mCenterY + mWheelRadius), begin, sweep, false, mProgressPaint);


//        结束按钮
        canvas.drawCircle(mEndBtnCurX, mEndBtnCurY, mBtnSize / 2, mEndBtnPaint);
        canvas.drawBitmap(mEndBtnBg, mEndBtnCurX - mBtnImgSize / 2, mEndBtnCurY - mBtnImgSize / 2, mDefaultPaint);


//        开始按钮
        canvas.drawCircle(mStartBtnCurX, mStartBtnCurY, mBtnSize / 2, mStartBtnPaint);
        canvas.drawBitmap(mStartBtnBg, mStartBtnCurX - mBtnImgSize / 2, mStartBtnCurY - mBtnImgSize / 2, mDefaultPaint);

    }


    //获取颜色
    @TargetApi(M)
    private int getColor(int colorId) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return getContext().getColor(colorId);
        } else {
            return ContextCompat.getColor(getContext(), colorId);
        }
    }


    private void refreshBtnPosition() {
        refreshStartBtnPositon();
        refreshEndBtnPosition();
    }


    /**
     * 刷新开始按钮的位置
     */
    public void refreshStartBtnPositon() {
        //转换为360度
        mStartBtnAngle = mStartDegree % 360;
        double startCos = Math.cos(Math.toRadians(mStartBtnAngle));
        MakeCurPosition(startCos);
    }

    /**
     * 刷新结束按钮的位置
     */
    public void refreshEndBtnPosition() {
        mEndBtnAngle = mEndDegree % 360;
        double endCos = Math.cos(Math.toRadians(mEndBtnAngle));
        MakeCurPosition2(endCos);
    }


    private void MakeCurPosition(double cos) {
        //根据旋转的角度来确定圆的位置
        //确定x点的坐标
        mStartBtnCurX = calcXLocationInWheel(mStartBtnAngle, cos);
        //确定y点的坐标
        mStartBtnCurY = calcYLocationInWheel(cos);
    }

    private void MakeCurPosition2(double cos2) {
        //根据旋转的角度来确定圆的位置
        //确定x点的坐标
        mEndBtnCurX = calcXLocationInWheel(mEndBtnAngle, cos2);
        //确定y点的坐标
        mEndBtnCurY = calcYLocationInWheel(cos2);
    }

    //确定x点的坐标
    private float calcXLocationInWheel(double angle, double cos) {
//        if (angle < 180) {
//            return (float) (getMeasuredWidth() / 2 + Math.sqrt(1 - cos * cos) * ring_Radius); //Math.sqrt正平分根  9-3
//        } else {
//            return (float) (getMeasuredWidth() / 2 - Math.sqrt(1 - cos * cos) * ring_Radius);
//        }

        if (angle < 180) {
            return (float) (getMeasuredWidth() / 2 + Math.sqrt(1 - cos * cos) * (mMinViewSize - mBtnSize) / 2);
        } else {
            return (float) (getMeasuredWidth() / 2 - Math.sqrt(1 - cos * cos) * (mMinViewSize - mBtnSize) / 2);
        }
    }

    //确定y点的坐标
    private float calcYLocationInWheel(double cos) {
//        return getMeasuredHeight() / 2 + ring_Radius * (float) cos;
        return (float) (getMeasuredHeight() / 2 - cos * (mMinViewSize - mBtnSize) / 2);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getParent() != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (getDistance(event.getX(), event.getY(), mCenterX, mCenterY) > mMinViewSize / 2 + mBtnSize) {
            return super.onTouchEvent(event);
        }

        float eventX = event.getX();
        float eventY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isMoveStartBtn(eventX, eventY)) {
                    mMoveFlag = 1;
                } else if (isMoveEndBtn(eventX, eventY)) {
                    mMoveFlag = 2;
                } else if (isMoveSelectedArea(eventX, eventY)) {
                    mMoveFlag = 3;
                }
                mLastEventX = eventX;
                mLastEventY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:

//                根据两坐标点求直线方程
//                A = Y2 - Y1
//                B = X1 - X2
//                C = X2*Y1 - X1*Y2


                if (mMoveFlag == 1) {
//                  坐标系的直线表达式
//                  直线l1的表达式子:过钟表中心点和开始控件中心点
                    float a1 = mCenterY - mStartBtnCurY;
                    float b1 = mStartBtnCurX - mCenterX;
                    float c1 = mStartBtnCurY * mCenterX - mCenterY * mStartBtnCurX;
                    double d1 = (a1 * eventX + b1 * eventY + c1) / (Math.sqrt(a1 * a1 + b1 * b1));
//                    Log.d("TAG", "d1==" + d1);

//                  直线l2的表达式:过钟表中心点且垂直直线l1
                    float a2 = b1;
                    float b2 = -a1;
                    float c2 = -a2 * mCenterX - b2 * mCenterY;
                    double d2 = (a2 * eventX + b2 * eventY + c2) / (Math.sqrt(a2 * a2 + b2 * b2));
//                    Log.d("TAG", "d2==" + d2);
//                    以l1为基准线,顺势针半圆为0-180度,逆时针半圆为0-负180度
                    double moveDegree = Math.toDegrees(Math.atan2(d1, d2));
//                    Log.d("Test", "moveDegree==" + moveDegree);
                    mStartDegree = (float) (mStartDegree + Math.floor(moveDegree));
                    mStartDegree = (mStartDegree < 0) ? mStartDegree + mDegreeCycle : mStartDegree % mDegreeCycle;

                    if (mOnTimeChangeListener != null) {
                        mOnTimeChangeListener.startTimeChanged(mStartDegree, mEndDegree);
                    }
                    refreshStartBtnPositon();
                    Log.d("Test", "mStartDegree==" + mStartDegree);
                    Log.d("Test", "d1==" + d1 + "\n" +
                            "d2==" + d2 + "\n" +
                            "moveDegree==" + moveDegree + "\n" +
                            "mStartBtnAngle==" + mStartBtnAngle + "\n" +
                            "mStartBtnCurX==" + mStartBtnCurX + "\n" +
                            "/mStartBtnCurY==" + mStartBtnCurY);
                    invalidate();
                } else if (mMoveFlag == 2) {

//                  坐标系的直线表达式
//                  直线l1的表达式子:过钟表中心点和结束控件中心点
                    float a1 = mCenterY - mEndBtnCurY;
                    float b1 = mEndBtnCurX - mCenterX;
                    float c1 = mEndBtnCurY * mCenterX - mCenterY * mEndBtnCurX;
                    double d1 = (a1 * eventX + b1 * eventY + c1) / (Math.sqrt(a1 * a1 + b1 * b1));
//                    Log.d("TAG", "d1==" + d1);

//                  直线l2的表达式:过钟表中心点且垂直直线l1
                    float a2 = b1;
                    float b2 = -a1;
                    float c2 = -a2 * mCenterX - b2 * mCenterY;
                    double d2 = (a2 * eventX + b2 * eventY + c2) / (Math.sqrt(a2 * a2 + b2 * b2));
//                    Log.d("TAG", "d2==" + d2);
//                    以l1为基准线,顺势针半圆为0-180度,逆时针半圆为0-负180度
                    double moveDegree = Math.toDegrees(Math.atan2(d1, d2));
//                    Log.d("Test", "moveDegree==" + moveDegree);
                    mEndDegree = (float) (mEndDegree + Math.floor(moveDegree));
                    mEndDegree = (mEndDegree < 0) ? mEndDegree + mDegreeCycle : mEndDegree % mDegreeCycle;
                    if (mOnTimeChangeListener != null) {
                        mOnTimeChangeListener.endTimeChanged(mStartDegree, mEndDegree);
                    }
                    refreshEndBtnPosition();
                    Log.d("Test", "mEndDegree==" + mEndDegree);
                    Log.d("Test", "d1==" + d1 + "\n" +
                            "d2==" + d2 + "\n" +
                            "moveDegree==" + moveDegree + "\n" +
                            "mEndBtnAngle==" + mEndBtnAngle + "\n" +
                            "mEndBtnCurX==" + mEndBtnCurX + "\n" +
                            "/mEndBtnCurY==" + mEndBtnCurY);
                    invalidate();

                } else if (mMoveFlag == 3) {

//                  坐标系的直线表达式
//                  直线l1的表达式子:过钟表中心点和上次的触摸点
                    float a1 = mCenterY - mLastEventY;
                    float b1 = mLastEventX - mCenterX;
                    float c1 = mLastEventY * mCenterX - mCenterY * mLastEventX;
                    double d1 = (a1 * eventX + b1 * eventY + c1) / (Math.sqrt(a1 * a1 + b1 * b1));

                    //                  直线l2的表达式:过钟表中心点且垂直直线l1
                    float a2 = b1;
                    float b2 = -a1;
                    float c2 = -a2 * mCenterX - b2 * mCenterY;
                    double d2 = (a2 * eventX + b2 * eventY + c2) / (Math.sqrt(a2 * a2 + b2 * b2));

//                    以l1为基准线,顺势针半圆为0-180度,逆时针半圆为0-负180度
                    double moveDegree = Math.toDegrees(Math.atan2(d1, d2));

                    mStartDegree = (float) (mStartDegree + moveDegree);
                    mStartDegree = (mStartDegree < 0) ? mStartDegree + mDegreeCycle : mStartDegree % mDegreeCycle;

                    mEndDegree = (float) (mEndDegree + moveDegree);
                    mEndDegree = (mEndDegree < 0) ? mEndDegree + mDegreeCycle : mEndDegree % mDegreeCycle;

                    if (mOnTimeChangeListener != null) {
                        mOnTimeChangeListener.onAllTimeChanaged(mStartDegree, mEndDegree);
                    }
                    Log.d("Test3", "moveDegree==" + moveDegree + "/mEndDegree==" + mEndDegree);
                    refreshBtnPosition();
                    mLastEventX = eventX;
                    mLastEventY = eventY;

                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mMoveFlag = -1;
                break;
            default:
                break;
        }

        return true;
    }


    private boolean isMoveEndBtn(float x, float y) {
        float dx = Math.abs(mEndBtnCurX - x);
        float dy = Math.abs(mEndBtnCurY - y);
        if (dx < mBtnSize / 2 && dy < mBtnSize / 2) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isMoveStartBtn(float x, float y) {
        float dx = Math.abs(mStartBtnCurX - x);
        float dy = Math.abs(mStartBtnCurY - y);
        if (dx < mBtnSize / 2 && dy < mBtnSize / 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否实在选中区域
     *
     * @param eventX
     * @param eventY
     * @return
     */
    private boolean isMoveSelectedArea(float eventX, float eventY) {
        float dx = Math.abs(mCenterX - eventX);
        float dy = Math.abs(mCenterY - eventY);
        if ((dx * dx + dy * dy) < ((mClockSize / 2) * (mClockSize / 2))) {
            return false;
        }
        if ((dx * dx + dy * dy) > ((mMinViewSize / 2) * (mMinViewSize / 2))) {
            return false;
        }

        double radian = Math.atan2(mCenterX - eventX, eventY - mCenterY);
        double degrees = Math.toDegrees(radian);
        double downDegree = degrees + 180;

        if (mEndBtnAngle > mStartBtnAngle && downDegree > mStartBtnAngle && downDegree < mEndBtnAngle) {
            Log.d("isMoveSelectedArea", "isMoveSelectedArea");
            return true;
        } else if (mEndBtnAngle < mStartBtnAngle && !(downDegree > mEndBtnAngle && downDegree < mStartBtnAngle)) {
            Log.d("isMoveSelectedArea", "isMoveSelectedArea");
            return true;
        }

        return false;
    }


    /**
     * 设置监听事件
     */
    public void setOnTimerChangeListener(OnTimeChangeListener listener) {
        if (mOnTimeChangeListener == null) {
            this.mOnTimeChangeListener = listener;
            mOnTimeChangeListener.onTimeInitail(mStartDegree, mEndDegree);
        }
    }

    /**
     * 设置初始化时间
     *
     * @param initStartDegree
     * @param initEndDegree
     */
    public void setInitialTime(float initStartDegree, float initEndDegree) {
        mStartDegree = (initStartDegree < 0) ? initStartDegree + mDegreeCycle : initStartDegree % mDegreeCycle;
        mEndDegree = (initEndDegree < 0) ? initEndDegree + mDegreeCycle : initEndDegree % mDegreeCycle;
        if (mOnTimeChangeListener != null) {
            mOnTimeChangeListener.onTimeInitail(mStartDegree, mEndDegree);
        }
        refreshBtnPosition();
    }


    /**
     * 获取两坐标点的直线距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}
