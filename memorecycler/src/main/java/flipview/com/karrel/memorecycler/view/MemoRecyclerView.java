
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
import android.widget.RelativeLayout;

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
        RelativeLayout parent = binding.parent;

        // 뷰를 중앙에 추가 해야한다
        // 다음 추가할 뷰는 중앙에서 조금 이동해서 추가해야한다
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        View view = adapter.getView(0, null, parent);
        parent.addView(view, params);


    }
}
