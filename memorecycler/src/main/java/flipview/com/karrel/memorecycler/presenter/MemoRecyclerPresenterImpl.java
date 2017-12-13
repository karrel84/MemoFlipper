package flipview.com.karrel.memorecycler.presenter;

import android.animation.Animator;
import android.view.MotionEvent;
import android.view.ViewPropertyAnimator;
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
                view.removeGloablLayoutListener();
                // 뷰의 위치 초기화
                view.arrangementViews(false);
            };
        return onGlobalLayoutListener;
    }

    private EventView.EventViewListener eventListener = new EventView.EventViewListener() {
        @Override
        public void onUp(MotionEvent e) {
            RLog.d(String.format("\nx : %s, y : %s", e.getX(), e.getY()));
            view.animOrigin();
        }

        @Override
        public void swipeLeft(long duration) {
            RLog.e("\n" + duration + "");
        }

        @Override
        public void swipeRight(long duration) {
            RLog.e("\n" + duration + "");
        }

        @Override
        public void flingToTop(int duration) {
            RLog.e("\n" + duration + "");

            ViewPropertyAnimator animator = view.flingTop(duration);
            if (animator == null) return;
            animator.setListener(new Animator.AnimatorListener() {
                boolean endAnimation = false;

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (endAnimation) return;
                    endAnimation = true;
                    RLog.d("onAnimationEnd");
                    animation.removeAllListeners();

                    // 애니메이션이 끝나면 해당 뷰를 삭제한다.
                    android.view.View deletedView = view.deleteFrontMemo();
                    // 다음에 추가할 메모가 있다면은 추가해준다.
                    view.addMemoHasNext(deletedView);

                    // 다시 정렬(마지막뷰는 애니메이션 하지않음)
                    view.arrangementViewsWithoutLastView();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

        }

        @Override
        public void flingToBottom(int duration) {
            RLog.e("\n" + duration + "");
            ViewPropertyAnimator animator = view.flingDown(duration);
            if (animator == null) return;
            animator.setListener(new Animator.AnimatorListener() {
                boolean endAnimation = false;

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (endAnimation) return;
                    endAnimation = true;
                    RLog.d("onAnimationEnd");
                    animation.removeAllListeners();

                    view.rewindLastView();
                    // 다시 정렬(마지막뷰는 애니메이션 하지않음)
                    view.arrangementViews(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

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

        @Override
        public void onTouchEvent(MotionEvent event) {
            view.memoViewOnTouchEvent(event);
        }
    };
}
