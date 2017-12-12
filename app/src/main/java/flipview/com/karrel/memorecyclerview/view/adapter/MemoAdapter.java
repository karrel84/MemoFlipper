package flipview.com.karrel.memorecyclerview.view.adapter;

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

            ViewCardABinding aBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_card_a, null, false);
            binding.flipCard.setCardA(aBinding.getRoot());


            ViewCardBBinding bBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_card_b, null, false);
            binding.flipCard.setCardB(bBinding.getRoot());
        }

        // binding
        ViewMemoBinding binding = (ViewMemoBinding) convertView.getTag();
        if (convertView.getTag() == null) return convertView;
        if (!(convertView.getTag() instanceof ViewMemoBinding)) return convertView;

        // data model
        MemoData data = memoData.get(position);
        // setup data
//        binding.text.setText(data.front);

        return convertView;
    }
}
