package flipview.com.karrel.memorecycler.presenter;

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

        void setupMemoViews();

        void removeGloablLayoutListener();

        void moveMemoView(float gapY);

        void animOrigin();

        void moveTop(int duration);

        void moveDown(int duration);
    }
}
