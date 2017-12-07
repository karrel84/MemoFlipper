package flipview.com.karrel.memorecycler.presenter;

import flipview.com.karrel.memorecycler.view.EventView;

/**
 * Created by Rell on 2017. 12. 7..
 */

public interface MemoRecyclerPresenter {
    EventView.EventViewListener eventListener();

    interface View {

    }
}
