
package flipview.com.karrel.memorecycler.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.FrameLayout;

import com.karrel.mylibrary.RLog;

import flipview.com.karrel.memorecycler.R;
import flipview.com.karrel.memorecycler.databinding.ViewMemorecyclerBinding;
import flipview.com.karrel.memorecycler.presenter.MemoRecyclerPresenter;
import flipview.com.karrel.memorecycler.presenter.MemoRecyclerPresenterImpl;

/**
 * Created by Rell on 2017. 12. 7..
 */

public class MemoRecyclerView extends FrameLayout implements MemoRecyclerPresenter.View {

    private Adapter adapter;
    private ViewMemorecyclerBinding binding;
    private MemoRecyclerPresenter presenter;

    public MemoRecyclerView(Context context) {
        super(context);
        initView();
    }

    public MemoRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MemoRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        presenter = new MemoRecyclerPresenterImpl(this);

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.view_memorecycler, this, true);
        binding.eventView.setListener(presenter.eventListener());
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        presenter.addAdapter();
    }

    @Override
    public void initChildViews() {
        RLog.d();
        FrameLayout parent = binding.frameLayout;

        parent.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            RLog.d(String.format("parent width : %s, height : %s", parent.getMeasuredWidth(), parent.getMeasuredHeight()));

            View view = adapter.getView(0, null, parent);
            parent.addView(view);
        });

    }
}
