package cn.tianyuan.user.addr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.tianyuan.R;


/**
 * Created by Administrator on 2017/10/18.
 */

public class AddrAdapter extends RecyclerView.Adapter<AddrAdapter.AddrViewHolder>{

    List<AddrDataBeen> mAddrs;

    public AddrAdapter() {
        mAddrs = new ArrayList<>();
    }

    public void setData(List<AddrDataBeen> addrs) {
        if (addrs == null)
            return;
        mAddrs = addrs;
        notifyDataSetChanged();
    }

    @Override
    public AddrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_myaddr_item, parent, false);
        AddrViewHolder holder = new AddrViewHolder(view);
        holder.mModify.setOnClickListener(mModifyListener);
        holder.mDelete.setOnClickListener(mDeleteListener);
        view.setOnClickListener(mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddrViewHolder holder, int position) {
        AddrDataBeen item = mAddrs.get(position);
        holder.mModify.setTag(position);
        holder.mDelete.setTag(position);
        holder.itemView.setTag(position);
        holder.mAddr.setText(item.fullAddress);
    }

    @Override
    public int getItemCount() {
        if (mAddrs == null)
            return 0;
        return mAddrs.size();
    }

    private View.OnClickListener mDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            mOnItemClickListener.onDeteleItem(mAddrs.get(position), position);
        }
    };

    private View.OnClickListener mModifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            mOnItemClickListener.onModifyItem(mAddrs.get(position), position);
        }
    };

    private View.OnClickListener mItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            mOnItemClickListener.onClickItem(mAddrs.get(position), position);
        }
    };

    public class AddrViewHolder extends RecyclerView.ViewHolder {

        public TextView mAddr;
        public RelativeLayout mModify;
        public RelativeLayout mDelete;

        public AddrViewHolder(View itemView) {
            super(itemView);
            mAddr = (TextView) itemView.findViewById(R.id.addr);
            mModify = (RelativeLayout) itemView.findViewById(R.id.modify);
            mDelete = (RelativeLayout) itemView.findViewById(R.id.delete);
        }
    }

    //item click
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onClickItem(AddrDataBeen item, int position);
        void onDeteleItem(AddrDataBeen item, int position);
        void onModifyItem(AddrDataBeen item, int position);
    }
    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        mOnItemClickListener = pOnItemClickListener;
    }
}
