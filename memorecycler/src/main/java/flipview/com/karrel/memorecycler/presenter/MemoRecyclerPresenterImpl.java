package flipview.com.karrel.memorecycler.presenter;

import android.view.MotionEvent;

import com.karrel.mylibrary.RLog;

import flipview.com.karrel.memorecycler.view.EventView;

/**
 * Created by Rell on 2017. 12. 7..
 */

public class MemoRecyclerPresenterImpl implements MemoRecyclerPresenter {
    private MemoRecyclerPresenter.View view;

    public MemoRecyclerPresenterImpl(View view) {
        this.view = view;
    }

    @Override
    public EventView.EventViewListener eventListener() {
        return eventListener;
    }

    private EventView.EventViewListener eventListener = new EventView.EventViewListener() {
        @Override
        public void onUp(MotionEvent event) {
            RLog.d(event.toString());
        }

        @Override
        public void swipeLeft(long duration) {
            RLog.d(duration + "");
        }

        @Override
        public void swipeRight(long duration) {
            RLog.d(duration + "");
        }

        @Override
        public void swipeTop(int duration) {
            RLog.d(duration + "");
        }

        @Override
        public void swipeBottom(int duration) {
            RLog.d(duration + "");
        }

        @Override
        public void onScroll(MotionEvent e1, MotionEvent e2) {
            RLog.d(String.format("e1 : %s \ne2 : %s", e1.toString(), e2.toString()));
        }

        @Override
        public void onDown(MotionEvent e) {
            RLog.d(e.toString());
        }
    };
}
