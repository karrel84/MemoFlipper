package flipview.com.karrel.memorecyclerview.view;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import flipview.com.karrel.memorecyclerview.R;
import flipview.com.karrel.memorecyclerview.databinding.ActivityMainBinding;
import flipview.com.karrel.memorecyclerview.model.MemoData;
import flipview.com.karrel.memorecyclerview.presenter.MainPresenter;
import flipview.com.karrel.memorecyclerview.presenter.MainPresenterImpl;
import flipview.com.karrel.memorecyclerview.view.adapter.MemoAdapter;

public class MainActivity extends AppCompatActivity implements MainPresenter.View {

    private ActivityMainBinding binding;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        presenter = new MainPresenterImpl(this);

        setupAdapter();
    }

    private void setupAdapter() {
        // create dummy data
        List<MemoData> memoData = new ArrayList<>();
        memoData.add(new MemoData("Swift", ""));
        memoData.add(new MemoData("꼼꼼한 재은 씨의", ""));
        memoData.add(new MemoData("실전편", ""));
        memoData.add(new MemoData("안녕하세요", ""));
        memoData.add(new MemoData("이주영입니다.", ""));
        memoData.add(new MemoData("커스텀", ""));
        memoData.add(new MemoData("메모를 만들고 있어요", ""));

        BaseAdapter adapter = new MemoAdapter(memoData);

        binding.memoRecyclerView.setAdapter(adapter);
    }
}
