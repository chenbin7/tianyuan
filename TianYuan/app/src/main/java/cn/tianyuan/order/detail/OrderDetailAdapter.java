package cn.tianyuan.order.detail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.tianyuan.R;
import cn.tianyuan.TYApplication;
import cn.tianyuan.orderModel.response.BookData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/10/18.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.BookViewHolder> implements View.OnClickListener {

    List<BookData> books;

    public OrderDetailAdapter() {
        books = new ArrayList<>();
    }

    public void setData(List<BookData> addrs) {
        if (addrs == null)
            return;
        books = addrs;
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    notifyDataSetChanged();
                });
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail_book, parent, false);
        BookViewHolder holder = new BookViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        BookData item = books.get(position);
        holder.itemView.setTag(position);
        ImageLoader.getInstance().displayImage(item.picture, holder.mImage, TYApplication.getInstance().getOptions());
        holder.mName.setText(item.name);
        holder.mType.setText(item.type);
        holder.mCount.setText(item.count+"本");
        holder.mPrice.setText("￥:"+item.price/100 +".00");
    }

    @Override
    public int getItemCount() {
        if (books == null)
            return 0;
        return books.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            int position = (int) v.getTag();
            mOnItemClickListener.onRecyclerItemClick(books.get(position), position);
        }
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        public ImageView mImage;
        public TextView mName;
        public TextView mType;
        public TextView mCount;
        public TextView mPrice;

        public BookViewHolder(View itemView) {
            super(itemView);
            mImage = (ImageView) itemView.findViewById(R.id.image);
            mName = (TextView) itemView.findViewById(R.id.name);
            mType = (TextView) itemView.findViewById(R.id.type);
            mCount = (TextView) itemView.findViewById(R.id.count);
            mPrice = (TextView) itemView.findViewById(R.id.price);
        }
    }

    //item click
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onRecyclerItemClick(BookData item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        mOnItemClickListener = pOnItemClickListener;
    }
}
