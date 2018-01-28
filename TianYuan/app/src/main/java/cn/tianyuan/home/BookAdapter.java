package cn.tianyuan.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.List;

import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import cn.tianyuan.bookmodel.BookBeen;

/**
 * Created by Administrator on 2017/10/18.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> implements View.OnClickListener {

    List<BookBeen> mBooks;

    public BookAdapter() {
        mBooks = new ArrayList<>();
    }

    public void setData(List<BookBeen> addrs) {
        if (addrs == null)
            return;
        mBooks = addrs;
        notifyDataSetChanged();
    }

    public void clear(){
        if(mBooks == null)
            return;
        mBooks.clear();
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_book_item, parent, false);
        BookViewHolder holder = new BookViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookBeen item = mBooks.get(position);
        holder.itemView.setTag(position);
        holder.mName.setText(item.name);
        ImageLoader.getInstance().displayImage(item.picture, holder.mImg, TYApplication.getInstance().getOptions());
    }

    @Override
    public int getItemCount() {
        if (mBooks == null)
            return 0;
        return mBooks.size();
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null){
            int position = (int) v.getTag();
            mOnItemClickListener.onRecyclerItemClick(mBooks.get(position), position);
        }
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImg;
        public TextView mName;

        public BookViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.book_img);
            mName = (TextView) itemView.findViewById(R.id.name);
        }
    }

    //item click
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onRecyclerItemClick(BookBeen item, int position);
    }
    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        mOnItemClickListener = pOnItemClickListener;
    }
}
