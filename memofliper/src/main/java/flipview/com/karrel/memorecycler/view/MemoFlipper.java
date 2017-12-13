
package flipview.com.karrel.memorecycler.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.karrel.mylibrary.RLog;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import flipview.com.karrel.memorecycler.R;
import flipview.com.karrel.memorecycler.databinding.ViewMemorecyclerBinding;
import flipview.com.karrel.memorecycler.presenter.MemoRecyclerPresenter;
import flipview.com.karrel.memorecycler.presenter.MemoRecyclerPresenterImpl;

/**
 * Created by Rell on 2017. 12. 7..
 */

public class MemoFlipper extends FrameLayout implements MemoRecyclerPresenter.View {

    private Adapter adapter;
    private ViewMemorecyclerBinding binding;
    private MemoRecyclerPresenter presenter;
    private Queue<View> memoViews;
    private int position;

    public MemoFlipper(Context context) {
        super(context);
        initView();
    }

    public MemoFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MemoFlipper(Context context, AttributeSet attrs, int defStyleAttr) {
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
        RLog.d("initChildViews");

        // 뷰를 중앙에 추가 해야한다
        // 다음 추가할 뷰는 중앙에서 조금 이동해서 추가해야한다
        RelativeLayout parent = binding.parent;
        int count = adapter.getCount() < 5 ? adapter.getCount() : 5;

        // 뷰들을 저장할 컬렉션을 만든다.
        memoViews = new ArrayDeque<>();

        // 차일드뷰를 마들어서 저장한다.
        for (int i = 0; i < count; i++) {
            position++;
            View view = adapter.getView(i, null, parent);
            parent.addView(view);
            memoViews.add(view);
        }
        parent.getViewTreeObserver().addOnGlobalLayoutListener(presenter.onGlobalLayout());
    }

    // 큐에 들어있는 뷰의 순서에 맞게 bringToFront와 위치 이동을 시켜준다.
    @Override
    public void arrangementViews(boolean isAnimation) {
        RLog.d(String.format("arrangementViews(%s)", isAnimation));

        RLog.d(String.format("memoViews.size : %s", memoViews.size()));
        View parent = binding.parent;
        List<View> list = new ArrayList(memoViews);

        RLog.d(String.format("list.size() : %s", list.size()));
        for (int i = list.size() - 1; i >= 0; i--) {
            View memoView = list.get(i);

            int left = parent.getWidth() / 2 - memoView.getWidth() / 2;
            int top = parent.getHeight() / 2 - memoView.getHeight() / 2;

            left += i * 10;
            top += i * 10;

            memoView.bringToFront();
            if (isAnimation) {
                memoView.animate()
                        .translationX(left)
                        .translationY(top);
            } else {
                memoView.setX(left);
                memoView.setY(top);
            }
        }
    }

    @Override
    public void addMemoHasNext(android.view.View deletedView) {
        RLog.d("addMemoHasNext");
        // 다음에 추가할 메모가 있다면 추가해준다.

        // 뷰를 중앙에 추가 해야한다
        // 다음 추가할 뷰는 중앙에서 조금 이동해서 추가해야한다
        RelativeLayout parent = binding.parent;

        if (position < adapter.getCount()) {
            // 차일드뷰를 마들어서 저장한다.
            View view = adapter.getView(position++, deletedView, parent);
            memoViews.add(view);
            parent.addView(view);
        }
    }

    @Override
    public void arrangementViewsWithoutLastView() {

        RLog.d(String.format("memoViews.size : %s", memoViews.size()));
        View parent = binding.parent;
        List<View> list = new ArrayList(memoViews);

        RLog.d(String.format("list.size() : %s", list.size()));
        for (int i = list.size() - 1; i >= 0; i--) {
            View memoView = list.get(i);

            int x = parent.getWidth() / 2 - memoView.getWidth() / 2;
            int y = parent.getHeight() / 2 - memoView.getHeight() / 2;

            x += i * 10;
            y += i * 10;

            memoView.bringToFront();
            if (i == list.size() - 1) {
                memoView.setX(x);
                memoView.setY(y);
            } else {
                memoView.animate()
                        .translationX(x)
                        .translationY(y);
            }

        }
    }

    @Override
    public void rewindLastView() {
        // 첫번째 뷰를 제일 뒤로 옮긴
        memoViews.add(memoViews.poll());
    }

    @Override
    public void memoViewOnTouchEvent(MotionEvent event) {
        View memoView = memoViews.peek();
        if (memoView == null) return ;
        memoView.onTouchEvent(event);
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
        RLog.d(String.format("moveMemoView(%s)", gapY));
        RLog.d(String.format("memoViews size : %s", memoViews.size()));
        View memoView = memoViews.peek();
        if (memoView == null) return;

        View parent = binding.parent;
        int top = parent.getHeight() / 2 - memoView.getHeight() / 2;
        memoView.setY(top + gapY);
    }

    @Override
    public void animOrigin() {
        RLog.d("animOrigin");
        // 뷰를 원래의 자리로 되돌려 놓는다.
        View memoView = memoViews.peek();
        if (memoView == null) return;

        View parent = binding.parent;
        int top = parent.getHeight() / 2 - memoView.getHeight() / 2;

        memoView.animate().translationY(top);
    }

    @Override
    public ViewPropertyAnimator flingTop(int duration) {
        View memoView = memoViews.peek();
        if (memoView == null) return null;
        return memoView
                .animate()
                .translationY(-memoView.getHeight())
                .setDuration(duration);
    }

    @Override
    public ViewPropertyAnimator flingDown(int duration) {
        RelativeLayout parent = binding.parent;
        View memoView = memoViews.peek();
        if (memoView == null) return null;
        return memoView.animate()
                .translationY(parent.getHeight())
                .setDuration(duration);
    }

    @Override
    public View deleteFrontMemo() {
        RLog.d("deleteFrontMemo");
        // 제일위에 메모뷰를 삭제해야한다.
        View view = memoViews.poll();
        binding.parent.removeView(view);
        return view;
    }
}