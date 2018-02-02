package cn.tianyuan.order;

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
import cn.tianyuan.bookmodel.response.BookBeen;
import cn.tianyuan.common.util.TimeFormatUtils;
import cn.tianyuan.orderModel.response.OrderResponse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/10/18.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.BookViewHolder> implements View.OnClickListener {

    List<OrderResponse.Order> orders;

    public OrderAdapter() {
        orders = new ArrayList<>();
    }

    public void setData(List<OrderResponse.Order> addrs) {
        if (addrs == null)
            return;
        orders = addrs;
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    notifyDataSetChanged();
                });
    }

    public void clear() {
        if (orders == null)
            return;
        orders.clear();
        notifyDataSetChanged();
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_item, parent, false);
        BookViewHolder holder = new BookViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    Date tempDate = new Date();

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        OrderResponse.Order item = orders.get(position);
        holder.itemView.setTag(position);
        holder.mName.setText(item.name);
        holder.mPhone.setText(item.phone);
        tempDate.setTime(item.time);
        holder.mTime.setText(TimeFormatUtils.format_yyyy_MM_dd(tempDate));
        holder.mAddr.setText(item.addr);
        holder.mPrice.setText(item.price/100 +".00");
    }

    @Override
    public int getItemCount() {
        if (orders == null)
            return 0;
        return orders.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            int position = (int) v.getTag();
            mOnItemClickListener.onRecyclerItemClick(orders.get(position), position);
        }
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public TextView mPhone;
        public TextView mTime;
        public TextView mAddr;
        public TextView mPrice;

        public BookViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mPhone = (TextView) itemView.findViewById(R.id.phone);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mAddr = (TextView) itemView.findViewById(R.id.addr);
            mPrice = (TextView) itemView.findViewById(R.id.price);
        }
    }

    //item click
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onRecyclerItemClick(OrderResponse.Order item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        mOnItemClickListener = pOnItemClickListener;
    }
}
