package cn.tianyuan.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import cn.tianyuan.common.util.TimeFormatUtils;

/**
 * Created by Administrator on 2017/10/18.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.BookViewHolder> implements View.OnClickListener {

    List<ContentInfo> mContents;

    public ContentAdapter() {
        mContents = new ArrayList<>();
    }

    public void setData(List<ContentInfo> contents) {
        if (contents == null)
            return;
        mContents = contents;
        notifyDataSetChanged();
    }

    public void clear(){
        if(mContents == null)
            return;
        mContents.clear();
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_content, parent, false);
        BookViewHolder holder = new BookViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    Date mTempDate = new Date();

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        ContentInfo item = mContents.get(position);
        holder.itemView.setTag(position);
        holder.mName.setText(item.name);
        mTempDate.setTime(item.time);
        holder.mTime.setText(TimeFormatUtils.format_yyyy_MM_dd_HH_mm(mTempDate));
        holder.mContnet.setText(item.content);

    }

    @Override
    public int getItemCount() {
        if (mContents == null)
            return 0;
        return mContents.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            int position = (int) v.getTag();
            mOnItemClickListener.onRecyclerItemClick(mContents.get(position), position);
        }
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public TextView mTime;
        public TextView mContnet;

        public BookViewHolder(View itemView) {
            super(itemView);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mName = (TextView) itemView.findViewById(R.id.user);
            mContnet = (TextView) itemView.findViewById(R.id.content);
        }
    }

    //item click
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onRecyclerItemClick(ContentInfo item, int position);
    }
    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        mOnItemClickListener = pOnItemClickListener;
    }
}
