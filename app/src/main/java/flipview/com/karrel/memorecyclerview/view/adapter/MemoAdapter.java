package flipview.com.karrel.memorecyclerview.view.adapter;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import flipview.com.karrel.memorecyclerview.R;
import flipview.com.karrel.memorecyclerview.databinding.ViewCardABinding;
import flipview.com.karrel.memorecyclerview.databinding.ViewCardBBinding;
import flipview.com.karrel.memorecyclerview.databinding.ViewMemoBinding;
import flipview.com.karrel.memorecyclerview.model.MemoData;

/**
 * Created by Rell on 2017. 12. 8..
 */

public class MemoAdapter extends BaseAdapter {

    private List<MemoData> memoData;

    DataSetObservable mDataSetObservable = new DataSetObservable(); // DataSetObservable(DataSetObserver)의 생성


    public MemoAdapter(List<MemoData> memoData) {
        this.memoData = memoData;
    }

    @Override
    public int getCount() {
        return memoData.size();
    }

    @Override
    public MemoData getItem(int position) {
        return memoData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ViewMemoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_memo, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        }

        // binding
        ViewMemoBinding binding = (ViewMemoBinding) convertView.getTag();
        if (convertView.getTag() == null) return convertView;
        if (!(convertView.getTag() instanceof ViewMemoBinding)) return convertView;

        // data model
        MemoData data = memoData.get(position);
        // setup data
        binding.text.setText(data.front);

        return convertView;
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) { // DataSetObserver의 등록(연결)
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) { // DataSetObserver의 해제
        mDataSetObservable.unregisterObserver(observer);
    }

    @Override
    public void notifyDataSetChanged() { // 위에서 연결된 DataSetObserver를 통한 변경 확인
        mDataSetObservable.notifyChanged();
    }
}
