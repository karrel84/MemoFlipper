package flipview.com.karrel.memorecycler.presenter;

import android.view.MotionEvent;
import android.view.ViewTreeObserver;

import com.karrel.mylibrary.RLog;

import flipview.com.karrel.memorecycler.view.EventView;

/**
 * Created by Rell on 2017. 12. 7..
 */

public class MemoRecyclerPresenterImpl implements MemoRecyclerPresenter {
    private MemoRecyclerPresenter.View view;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    public MemoRecyclerPresenterImpl(View view) {
        this.view = view;
    }

    @Override
    public EventView.EventViewListener eventListener() {
        return eventListener;
    }

    @Override
    public void addAdapter() {
        // 아답터가 세팅되었다.
        // 아답터에 들어갈 뷰들을 세팅하자
        view.initChildViews();
    }

    @Override
    public ViewTreeObserver.OnGlobalLayoutListener onGlobalLayout() {
        if (onGlobalLayoutListener == null)
            onGlobalLayoutListener = () -> {
                view.setupMemoViews();
                view.removeGloablLayoutListener();
            };
        return onGlobalLayoutListener;
    }

    private EventView.EventViewListener eventListener = new EventView.EventViewListener() {
        @Override
        public void onUp(MotionEvent e) {
            RLog.d(String.format("\nx : %s, y : %s", e.getX(), e.getY()));
        }

        @Override
        public void swipeLeft(long duration) {
            RLog.d("\n" + duration + "");
        }

        @Override
        public void swipeRight(long duration) {
            RLog.d("\n" + duration + "");
        }

        @Override
        public void swipeTop(int duration) {
            RLog.d("\n" + duration + "");
        }

        @Override
        public void swipeBottom(int duration) {
            RLog.d("\n" + duration + "");
        }

        @Override
        public void onScroll(MotionEvent e1, MotionEvent e2) {
            RLog.d("\n" + String.format("x1 : %s, y1 : %s\nx2 : %s, y2 %s", e1.getX(), e1.getY(), e2.getX(), e2.getY()));

            float gapY = e2.getY() - e1.getY();
            view.moveMemoView(gapY);
        }

        @Override
        public void onDown(MotionEvent e) {
            RLog.d("\n" + String.format("x : %s, y : %s", e.getX(), e.getY()));
        }
    };
}
