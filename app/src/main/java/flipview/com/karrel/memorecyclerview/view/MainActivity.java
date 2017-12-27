package flipview.com.karrel.memorecyclerview.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.karrel.mylibrary.RLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import flipview.com.karrel.memorecycler.view.MemoFlipper;
import flipview.com.karrel.memorecyclerview.R;
import flipview.com.karrel.memorecyclerview.databinding.ActivityMainBinding;
import flipview.com.karrel.memorecyclerview.model.MemoData;
import flipview.com.karrel.memorecyclerview.presenter.MainPresenter;
import flipview.com.karrel.memorecyclerview.presenter.MainPresenterImpl;
import flipview.com.karrel.memorecyclerview.view.adapter.MemoAdapter;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainPresenter.View, MemoFlipper.MemoFlipperListener {

    private ActivityMainBinding binding;
    private MainPresenter presenter;
    private MemoAdapter adapter;
    private ArrayList memoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        presenter = new MainPresenterImpl(this);

        // 아답터를 세팅합니다.
        setupAdapter();
        // TODO: 2017. 12. 27. 일정시간을 간격으로 테스트 데이터를 추가하는 작업을 해봅시다.
        goTest();
    }

    private void goTest() {
//        일정시간을 간격으로 테스트 데이터를 추가하는 작업을 해봅시다.

        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(aLong -> {
                    RLog.d(aLong + "");
                    adapter.addItem(aLong + "");
                });
    }

    private void setupAdapter() {
        // create dummy data
        memoData = new ArrayList<>();
        memoData.add(new MemoData("미안해 솔직하지 못한 내가", ""));
        memoData.add(new MemoData("지금 이 순간이 꿈이라면", ""));
        memoData.add(new MemoData("살며시 너에게로 다가가", ""));
        memoData.add(new MemoData("모든걸 고백할텐데", ""));
        memoData.add(new MemoData("전화도 할 수 없는 밤이 오면", ""));
        memoData.add(new MemoData("자꾸만 설레이는 내 마음", ""));
        memoData.add(new MemoData("동화속 마법에 세계로", ""));
        memoData.add(new MemoData("손짓하는 저달빛", ""));
        memoData.add(new MemoData("밤하늘 저멀리서 빛나고 있는", ""));
        memoData.add(new MemoData("꿈결 같은 우리의 사랑", ""));
        memoData.add(new MemoData("수없이 많은 별들 중에서 당신을 만날수 있는건", ""));
        memoData.add(new MemoData("결코 우연이라 할 수 없어 기적의 세일러문", ""));

        adapter = new MemoAdapter();
        binding.memoRecyclerView.setAdapter(adapter);
        binding.memoRecyclerView.setListener(this);
    }

    @Override
    public void onRemovedItem(View view) {
        adapter.removeItem(view);
    }
}
