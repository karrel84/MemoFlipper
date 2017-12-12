package flipview.com.karrel.memorecycler.presenter;

import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;

import flipview.com.karrel.memorecycler.view.EventView;

/**
 * Created by Rell on 2017. 12. 7..
 */

public interface MemoRecyclerPresenter {
    EventView.EventViewListener eventListener();

    void addAdapter();

    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayout();

    interface View {

        void initChildViews();

        void removeGloablLayoutListener();

        void moveMemoView(float gapY);

        void animOrigin();

        ViewPropertyAnimator flingTop(int duration);

        ViewPropertyAnimator flingDown(int duration);

        android.view.View deleteFrontMemo();

        void arrangementViews(boolean isAnimation);

        void addMemoHasNext(android.view.View deletedView);

        void arrangementViewsWithoutLastView();

        void rewindLastView();
    }
}
