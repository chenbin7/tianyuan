package cn.tianyuan.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.List;

import cn.tianyuan.R;

/**
 * Created by Administrator on 2017/10/18.
 */

public class AddrAdapter extends RecyclerView.Adapter<AddrAdapter.AddrViewHolder> implements View.OnClickListener {

    List<PoiItem> mAddrs;

    public AddrAdapter() {
        mAddrs = new ArrayList<>();
    }

    public void setData(List<PoiItem> addrs) {
        if (addrs == null)
            return;
        mAddrs = addrs;
        notifyDataSetChanged();
    }

    public void clear(){
        if(mAddrs == null)
            return;
        mAddrs.clear();
        notifyDataSetChanged();
    }

    @Override
    public AddrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_addr_item, parent, false);
        AddrViewHolder holder = new AddrViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddrViewHolder holder, int position) {
        PoiItem item = mAddrs.get(position);
        holder.itemView.setTag(position);
        holder.mTitle.setText(item.getTitle());
        String detail = item.getSnippet();
        if(detail.trim().equals("")){
            detail = item.getTitle();
        }
        holder.mDetail.setText(detail);
    }

    @Override
    public int getItemCount() {
        if (mAddrs == null)
            return 0;
        return mAddrs.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            int position = (int) v.getTag();
            mOnItemClickListener.onRecyclerItemClick(mAddrs.get(position), position);
        }
    }

    public class AddrViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mDetail;

        public AddrViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mDetail = (TextView) itemView.findViewById(R.id.detail);
        }
    }

    //item click
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onRecyclerItemClick(PoiItem item, int position);
    }
    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        mOnItemClickListener = pOnItemClickListener;
    }
}
