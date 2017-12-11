package flipview.com.karrel.memorecycler.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.karrel.mylibrary.RLog;

/**
 * Created by Rell on 2017. 12. 7..
 * <p>
 * 사용자의 터치이벤트를 가지고 플링과 스크롤의 이벤트를 전달해주는 뷰이다.
 */
public class EventView extends View {


    public interface EventViewListener {

        void onUp(MotionEvent event);

        void swipeLeft(long duration);

        void swipeRight(long duration);

        void swipeTop(int duration);

        void swipeBottom(int duration);

        void onScroll(MotionEvent e1, MotionEvent e2);

        void onDown(MotionEvent e);
    }

    private GestureDetector detector;

    // 플링 이벤트시 최저 이동 거리
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_THRESHOLD_VELOCITY = 400;

    /**
     * 최소 가속도
     */
    private float mMinVelocity;

    /**
     * 최대 가속도
     */
    private float mMaxVelocity;

    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /**
     * maximum velocity that will be detected as a fling
     */
    private static final int MAX_FLING_VELOCITY = 8000; // dips per second

    public static final int BASE_SETTLE_DURATION = 200; // ms
    public static final int MAX_SETTLE_DURATION = 300; // ms

    private EventViewListener listener;

    public void setListener(EventViewListener listener) {
        this.listener = listener;
    }

    public EventView(Context context) {
        super(context);
        init();
    }

    public EventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        final float density = getResources().getDisplayMetrics().density;
        mMinVelocity = MIN_FLING_VELOCITY * density;
        mMaxVelocity = MAX_FLING_VELOCITY * density;

        detector = new GestureDetector(getContext(), gestureListener);
    }

    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            listener.onDown(e);
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            listener.onScroll(e1, e2);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            RLog.e("onFling");
            RLog.d(String.format("gap x : %s", e1.getRawX() - e2.getRawX()));

            if (e1.getRawY() - e2.getRawY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                swipeTop(getDuration(e1, e2, (int) velocityX, (int) velocityY));
                return true;
            } else if (e2.getRawY() - e1.getRawY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                swipeBottom(getDuration(e1, e2, (int) velocityX, (int) velocityY));
                return true;
            }

            return true;
        }
    };

    private void swipeBottom(int duration) {
        listener.swipeBottom(duration);
    }

    private void swipeTop(int duration) {
        listener.swipeTop(duration);
    }

    private void swipeLeft(long duration) {
        listener.swipeLeft(duration);
    }

    private void swipeRight(long duration) {
        listener.swipeRight(duration);
    }

    private int getDuration(MotionEvent e1, MotionEvent e2, int velocityX, int velocityY) {
        int dx = (int) (e1.getRawX() - e2.getRawX());
        int dy = (int) (e1.getRawY() - e2.getRawY());
        int xvel = velocityX;
        int yvel = velocityY;
        return computeSettleDuration(dx, dy, xvel, yvel);
    }

    private int computeSettleDuration(int dx, int dy, int xvel, int yvel) {
        xvel = clampMag(xvel, (int) mMinVelocity, (int) mMaxVelocity);
        yvel = clampMag(yvel, (int) mMinVelocity, (int) mMaxVelocity);
        final int absDx = Math.abs(dx);
        final int absDy = Math.abs(dy);
        final int absXVel = Math.abs(xvel);
        final int absYVel = Math.abs(yvel);
        final int addedVel = absXVel + absYVel;
        final int addedDistance = absDx + absDy;

        final float xweight = xvel != 0 ? (float) absXVel / addedVel :
                (float) absDx / addedDistance;
        final float yweight = yvel != 0 ? (float) absYVel / addedVel :
                (float) absDy / addedDistance;

        int xduration = computeAxisDuration(dx, xvel, getWidth());
        int yduration = computeAxisDuration(dy, yvel, 0);

        return (int) (xduration * xweight + yduration * yweight);
    }

    private int clampMag(int value, int absMin, int absMax) {
        final int absValue = Math.abs(value);
        if (absValue < absMin) return 0;
        if (absValue > absMax) return value > 0 ? absMax : -absMax;
        return value;
    }

    private int computeAxisDuration(int delta, int velocity, int motionRange) {
        if (delta == 0) {
            return 0;
        }

        final int width = getWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, (float) Math.abs(delta) / width);
        final float distance = halfWidth + halfWidth
                * distanceInfluenceForSnapDuration(distanceRatio);

        int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(MAX_SETTLE_DURATION * Math.abs(distance / velocity));
        } else {
            final float range = (float) Math.abs(delta) / motionRange;
            duration = (int) ((range + 1) * BASE_SETTLE_DURATION);
        }
        return Math.min(duration, MAX_SETTLE_DURATION);
    }

    private float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5f; // center the values about 0.
        f *= 0.3f * Math.PI / 2.0f;
        return (float) Math.sin(f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            listener.onUp(event);
        }

        return detector.onTouchEvent(event);
    }
}
