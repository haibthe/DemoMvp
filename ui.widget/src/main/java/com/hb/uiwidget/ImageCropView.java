package com.hb.uiwidget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;


public class ImageCropView extends View implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private static final int AVATAR_MAX_SIZE = 256;

    private final int NONE = 0;
    private final int ZOOM = 1;
    private final int MOVE = 2;
    private final float DELTA_SCALE_RATE = 1f;
    private final float MAX_SCALE_RATE = 4f;
    private final int MAX_SIZE = 1500;

    private Bitmap mBMP = null;
    private Paint mPaint = null;

    private int mColor;
    private Path mPath;

    private Rect mBaseSrcRect = null;
    private Rect mSrcRect = null;
    private Rect mDstRect = null;

    private int mPrevDownX;
    private int mPrevDownY;
    private float mRateX;
    private float mRateY;

    private int halfC;

    private int sSize;

    private float rate;
    private float mScaleRate;
    private double mPrevDist;

    private int mActionMode;

    private ValueAnimator mAni = null;

    private GestureDetector gestureDetector;

    public ImageCropView(Context context) {
        super(context);
        init();
    }

    public ImageCropView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        if (width > height) width = height;
        else height = width;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        halfC = w / 2;

        mDstRect.set(0, 0, w, h);
        updateSourceRect();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mPrevDownX = (int) event.getX();
                mPrevDownY = (int) event.getY();
                mActionMode = MOVE;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mPrevDist = spacing(event);
                if (mPrevDist > 10f) mActionMode = ZOOM;
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mActionMode = NONE;
                break;
            case MotionEvent.ACTION_MOVE:
                switch (mActionMode) {
                    case ZOOM: {
                        PointF mPoint = new PointF();

                        double tempDist = spacing(event);
                        midPoint(mPoint, event);

                        centerRate(mPoint.x, mPoint.y);
                        startZoom(calScaleRate((float) (tempDist - mPrevDist) * 2 / (float) halfC, false), 10);

                        mPrevDist = tempDist;
                    }
                    case MOVE: {
                        final int prevDownX = (int) event.getX();
                        final int prevDownY = (int) event.getY();

                        moveAvailableRectWithDelta(prevDownX - mPrevDownX, prevDownY - mPrevDownY);

                        mPrevDownX = prevDownX;
                        mPrevDownY = prevDownY;
                    }
                }
                break;
            default:
                break;
        }

        return gestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBMP == null)
            return;
        super.onDraw(canvas);

        canvas.drawBitmap(mBMP, mSrcRect, mDstRect, mPaint);

        final int saveCount = canvas.save();

        mPath.reset();
        mPath.addCircle(halfC, halfC, halfC, Path.Direction.CW);
        mPath.close();
        canvas.clipPath(mPath, Region.Op.DIFFERENCE);
        canvas.drawColor(mColor);

        canvas.restoreToCount(saveCount);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mScaleRate = (float) animation.getAnimatedValue();
        scale();
    }

    public void setImage(Bitmap bmp) {
        release();

        if (bmp.getWidth() > MAX_SIZE || bmp.getHeight() > MAX_SIZE) {
            final int bmpw = bmp.getWidth();
            final int bmph = bmp.getHeight();

            int newW = 0;
            int newH = 0;

            if (bmpw > bmph && bmpw > MAX_SIZE) {
                newW = MAX_SIZE;
                newH = (int) (bmph * ((float) MAX_SIZE / (float) bmpw));
            } else if (bmph > bmpw && bmph > MAX_SIZE) {
                newH = MAX_SIZE;
                newW = (int) (bmpw * ((float) MAX_SIZE / (float) bmph));
            }

            Bitmap scalebmp = Bitmap.createScaledBitmap(bmp, newW, newH, true);
            bmp.recycle();

            bmp = scalebmp;
        }

        mBMP = bmp;
        if (mBMP != null) {
            updateSourceRect();

            invalidate();
        }
    }

    public Bitmap getCroppedBitmap() {
        if (mBMP == null) return null;

        sSize = mSrcRect.width();
        if (sSize > AVATAR_MAX_SIZE)
            sSize = AVATAR_MAX_SIZE;

        Rect dstRect = new Rect();
        dstRect.set(0, 0, sSize, sSize);

        Bitmap croppedBitmap = Bitmap.createBitmap(sSize, sSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(croppedBitmap);


        final int saveCount = canvas.save();

        final int half = sSize / 2;
        mPath.reset();
        mPath.addCircle(half, half, half, Path.Direction.CW);
        mPath.close();
        canvas.clipPath(mPath, Region.Op.INTERSECT);
        canvas.drawBitmap(mBMP, mSrcRect, dstRect, mPaint);
        canvas.restoreToCount(saveCount);

        canvas.drawCircle(half, half, half - mPaint.getStrokeWidth() / 2, mPaint);

        return croppedBitmap;
    }

    public void release() {
        if (mBMP != null) {
            mBMP.recycle();
            mBMP = null;
        }
    }

    public void rotate() {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        mScaleRate = 1;

        Bitmap rotatedBitmap = Bitmap.createBitmap(mBMP, 0, 0, mBMP.getWidth(), mBMP.getHeight(), matrix, true);
        setImage(rotatedBitmap);
    }

    private class DBTapGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            centerRate(e.getX(), e.getY());

            float scaleRate = calScaleRate(DELTA_SCALE_RATE, true);
            startZoom(scaleRate, 250);

            return true;
        }
    }

    protected void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.rgb(255, 255, 255));

        mColor = Color.argb(150, 0, 0, 0);
        mPath = new Path();

        mSrcRect = new Rect();
        mDstRect = new Rect();
        mBaseSrcRect = new Rect();

        mScaleRate = 1;
        mActionMode = NONE;

        gestureDetector = new GestureDetector(getContext(), new DBTapGestureListener());
    }

    protected void updateSourceRect() {
        if (mBMP == null) return;

        if (mBMP.getWidth() > mBMP.getHeight()) {
            rate = (float) mBMP.getHeight() / (float) getHeight();

            final int left = (mBMP.getWidth() - mBMP.getHeight()) / 2;
            mSrcRect.set(left, 0, left + mBMP.getHeight(), mBMP.getHeight());
        } else {
            rate = (float) mBMP.getWidth() / (float) getWidth();

            final int top = (mBMP.getHeight() - mBMP.getWidth()) / 2;
            mSrcRect.set(0, top, mBMP.getWidth(), top + mBMP.getWidth());
        }

        mBaseSrcRect.set(mSrcRect);
    }

    protected void moveAvailableRectWithDelta(int deltax, int deltay) {
        int sDeltax = (int) (rate * (float) deltax / mScaleRate);
        int sDeltay = (int) (rate * (float) deltay / mScaleRate);

        int left = mSrcRect.left - sDeltax;
        int top = mSrcRect.top - sDeltay;

        final int maxLeft = mBMP.getWidth() - mSrcRect.width();
        final int maxBottom = mBMP.getHeight() - mSrcRect.height();

        if (left < 0) left = 0;
        else if (left > maxLeft) left = maxLeft;

        if (top < 0) top = 0;
        else if (top > maxBottom) top = maxBottom;

        final int right = left + mSrcRect.width();
        final int bottom = top + mSrcRect.height();

        mSrcRect.set(left, top, right, bottom);

        final int bleft = (int) ((float) left / mScaleRate);
        final int btop = (int) ((float) top / mScaleRate);
        mBaseSrcRect.set(bleft, btop, bleft + mBaseSrcRect.width(), btop + mBaseSrcRect.height());

        invalidate();
    }

    protected void startZoom(float scaleRate, int duration) {
        if (mAni == null || !mAni.isRunning()) {
            mAni = ValueAnimator.ofFloat(mScaleRate, scaleRate);
            mAni.setDuration(duration);
            mAni.setInterpolator(new LinearInterpolator());
            mAni.addListener(this);
            mAni.addUpdateListener(this);
            mAni.start();
        }
    }

    protected void scale() {
        final float w = (float) mBaseSrcRect.width() / mScaleRate;
        final float delta = mSrcRect.width() - w;
        int left = mSrcRect.left + (int) (delta * mRateX);
        int top = mSrcRect.top + (int) (delta * mRateY);

        if (left < 0) left = 0;
        else if (left + w > mBMP.getWidth()) left = mBMP.getWidth() - (int) w;
        if (top < 0) top = 0;
        else if (top + w > mBMP.getHeight()) top = mBMP.getHeight() - (int) w;

        mSrcRect.set(left, top, left + (int) w, top + (int) w);

        if (mScaleRate == 1.f) mBaseSrcRect.set(mSrcRect);

        invalidate();
    }

    double spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return Math.sqrt(x * x + y * y);
    }

    void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    void centerRate(float px, float py) {
        final float x = px * rate;
        final float y = py * rate;

        mRateX = x / (float) mBaseSrcRect.width();
        mRateY = y / (float) mBaseSrcRect.height();
    }

    float calScaleRate(float delta, boolean caldown) {
        float scaleRate = mScaleRate + delta;
        if ((!caldown || mScaleRate < MAX_SCALE_RATE) && scaleRate > MAX_SCALE_RATE)
            scaleRate = MAX_SCALE_RATE;
        else if ((caldown && mScaleRate >= MAX_SCALE_RATE) || scaleRate < 1.f) scaleRate = 1;

        return scaleRate;
    }
}
