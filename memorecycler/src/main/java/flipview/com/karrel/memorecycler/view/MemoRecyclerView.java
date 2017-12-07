
package flipview.com.karrel.memorecycler.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Adapter;
import android.widget.FrameLayout;

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
    }

}
