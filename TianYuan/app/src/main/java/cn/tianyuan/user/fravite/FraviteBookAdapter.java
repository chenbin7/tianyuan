package cn.tianyuan.user.fravite;

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
import cn.tianyuan.bookmodel.response.BookBeen;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/10/18.
 */

public class FraviteBookAdapter extends RecyclerView.Adapter<FraviteBookAdapter.BookViewHolder> implements View.OnClickListener {

    List<BookBeen> mBooks;

    public FraviteBookAdapter() {
        mBooks = new ArrayList<>();
    }

    public void setData(List<BookBeen> addrs) {
        if (addrs == null)
            return;
        mBooks = addrs;
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    notifyDataSetChanged();
                });
    }

    public void clear() {
        if (mBooks == null)
            return;
        mBooks.clear();
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_fravite, parent, false);
        BookViewHolder holder = new BookViewHolder(view);
        holder.mDelete.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookBeen item = mBooks.get(position);
        holder.mDelete.setTag(position);
        holder.mName.setText(item.name);
        ImageLoader.getInstance().displayImage(item.picture.trim(), holder.mImg, TYApplication.getInstance().getOptions());
        holder.mDesc.setText(item.descriptor);
        holder.mPrice.setText("￥" + item.price / 100 + ".00");
    }

    @Override
    public int getItemCount() {
        if (mBooks == null)
            return 0;
        return mBooks.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            int position = (int) v.getTag();
            mOnItemClickListener.onRecyclerItemClick(mBooks.get(position), position);
        }
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImg;
        public TextView mName;
        public TextView mDesc;
        public TextView mPrice;
        public TextView mDelete;

        public BookViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.book_img);
            mName = (TextView) itemView.findViewById(R.id.name);
            mDesc = (TextView) itemView.findViewById(R.id.desc);
            mPrice = (TextView) itemView.findViewById(R.id.price);
            mDelete = (TextView) itemView.findViewById(R.id.delete);
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
