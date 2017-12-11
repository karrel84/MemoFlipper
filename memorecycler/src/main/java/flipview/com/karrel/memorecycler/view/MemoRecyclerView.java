
package flipview.com.karrel.memorecycler.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
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

        // 뷰를 중앙에 추가 해야한다
        // 다음 추가할 뷰는 중앙에서 조금 이동해서 추가해야한다

        RelativeLayout parent = binding.parent;
        int count = adapter.getCount() < 5 ? adapter.getCount() : 5;

        for (int i = 0; i < count; i++) {
            View view = adapter.getView(i, null, parent);
            parent.addView(view);
        }
        parent.getViewTreeObserver().addOnGlobalLayoutListener(presenter.onGlobalLayout());
    }

    @Override
    public void setupMemoViews() {
        RelativeLayout parent = binding.parent;

        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View memoView = parent.getChildAt(i);

            int left = parent.getWidth() / 2 - memoView.getWidth() / 2;
            int top = parent.getHeight() / 2 - memoView.getHeight() / 2;
            int right = left + memoView.getWidth();
            int bottom = top + memoView.getHeight();

            left += i * 10;
            top += i * 10;
            right += i * 10;
            bottom += i * 10;

            memoView.setX(left);
            memoView.setY(top);
            Rect rect = new Rect(left, top, right, bottom);
            memoView.setTag(rect);
        }
        bringToFrontViews();
    }

    private void bringToFrontViews() {
        RelativeLayout parent = binding.parent;
        int count = parent.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            View memoView = parent.getChildAt(i);
            memoView.bringToFront();
        }
    }

    @Override
    public void removeGloablLayoutListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ViewTreeObserver.OnGlobalLayoutListener listener = presenter.onGlobalLayout();
            if (listener != null)
                binding.parent.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    @Override
    public void moveMemoView(float gapY) {
        RelativeLayout parent = binding.parent;
        View memoView = parent.getChildAt(parent.getChildCount() - 1);
        Rect rect = (Rect) memoView.getTag();
        memoView.setY(rect.top + gapY);
    }
}