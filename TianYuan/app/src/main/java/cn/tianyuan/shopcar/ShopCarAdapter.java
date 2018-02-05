package cn.tianyuan.shopcar;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.tianyuan.R;
import cn.tianyuan.common.util.UtilTool;
import cn.tianyuan.orderModel.response.BookData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2018/1/24.
 */

public class ShopCarAdapter extends RecyclerView.Adapter<ShopCarAdapter.BooksHolderView> {
    private static final String TAG = ShopCarAdapter.class.getSimpleName();

    private List<BookData> books;
    Context mcontext;

    public ShopCarAdapter(Context context) {
        this.mcontext = context;
    }

    public void setData(List<BookData> books) {
        this.books = books;
        Log.d(TAG, "setData: ");
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    notifyDataSetChanged();
                });
    }

    @Override
    public BooksHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopcat_product, parent, false);
        BooksHolderView holder = new BooksHolderView(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (books == null)
            return 0;
        return books.size();
    }

    @Override
    public void onBindViewHolder(final BooksHolderView holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final BookData book = books.get(position);
        holder.goodsName.setText(book.descriptor);
        holder.goodsPrice.setText("￥" + book.price/100 + ".00");
        holder.goodsNum.setText(String.valueOf(book.count));
        holder.goodsImage.setImageResource(R.drawable.cmaz);
        holder.goods_size.setText("类型:" + book.type);
        holder.singleCheckBox.setChecked(book.isChoosed);
        holder.singleCheckBox.setTag(position);
        holder.singleCheckBox.setOnClickListener(mViewClickListener);
        holder.increaseGoodsNum.setTag(position);
        holder.increaseGoodsNum.setOnClickListener(mViewClickListener);
        holder.reduceGoodsNum.setTag(position);
        holder.reduceGoodsNum.setOnClickListener(mViewClickListener);
        holder.goodsNum.setTag(position);
        holder.goodsNum.setOnClickListener(mViewClickListener);
        holder.delGoods.setTag(position);
        holder.delGoods.setOnClickListener(mViewClickListener);
    }

    private View.OnClickListener mViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (v.getId()){
                case R.id.increase_goods_Num:
                    doIncrease(position);
                    break;
                case R.id.reduce_goodsNum:
                    doDecrease(position);
                    break;
                case R.id.del_goods:
                    doDelete(position);
                    break;
                case R.id.single_checkBox:
                    doCheckItem(position, ((CheckBox) v).isChecked());
                    break;
                case R.id.goods_Num:
                    BookData book = books.get(position);
                    showDialog(position, v, book);
                    break;
                default:
                    break;
            }
        }
    };

    private void doCheckItem(int position, boolean checked){
        BookData book = books.get(position);
        book.isChoosed = checked;
        notifyDataSetChanged();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onCheckedChange(book, position);
        }
    }

    private void doIncrease(int position){
        BookData book = books.get(position);
        book.count++;
        notifyDataSetChanged();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onUpdateCount(book, position);
        }
    }

    private void doDecrease(int position){
        BookData book = books.get(position);
        if(book.count <= 0){
            book.count = 0;
            return;
        }
        book.count--;
        notifyDataSetChanged();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onUpdateCount(book, position);
        }
    }

    private void doSetCount(int position, int count){
        BookData book = books.get(position);
        if(count < 0)
            return;
        book.count = count;
        notifyDataSetChanged();
        if(mOnItemClickListener != null){
            mOnItemClickListener.onUpdateCount(book, position);
        }
    }

    private void doDelete(int position){
        new AlertDialog.Builder(mcontext)
                .setMessage("确定要删除该商品吗")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BookData book = books.remove(position);
                        notifyDataSetChanged();
                        if(mOnItemClickListener != null){
                            mOnItemClickListener.onDelete(book, position);
                        }
                    }
                })
                .create()
                .show();

    }


    private int count = 0;
    private void showDialog(final int position, final View showCountView, final BookData child) {
        final AlertDialog.Builder alertDialog_Builder = new AlertDialog.Builder(mcontext);
        View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_change_num, null);
        final AlertDialog dialog = alertDialog_Builder.create();
        dialog.setView(view);//errored,这里是dialog，不是alertDialog_Buidler
        count = child.count;
        final EditText num = (EditText) view.findViewById(R.id.dialog_num);
        num.setText(count + "");
        //自动弹出键盘
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                UtilTool.showKeyboard(mcontext, showCountView);
            }
        });
        final TextView increase = (TextView) view.findViewById(R.id.dialog_increaseNum);
        final TextView DeIncrease = (TextView) view.findViewById(R.id.dialog_reduceNum);
        final TextView pButton = (TextView) view.findViewById(R.id.dialog_Pbutton);
        final TextView nButton = (TextView) view.findViewById(R.id.dialog_Nbutton);
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        pButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(num.getText().toString().trim());
                doSetCount(position, number);
                num.setText(String.valueOf(number));
                dialog.dismiss();
            }
        });
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                num.setText(String.valueOf(count));
            }
        });
        DeIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count > 1) {
                    count--;
                    num.setText(String.valueOf(count));
                }
            }
        });
        dialog.show();
    }


    public class BooksHolderView extends RecyclerView.ViewHolder {

        CheckBox singleCheckBox;
        ImageView goodsImage;
        TextView goodsName;
        TextView goods_size;
        TextView goodsPrice;
        TextView reduceGoodsNum;
        TextView goodsNum;
        TextView increaseGoodsNum;
        TextView goodsSize;
        TextView delGoods;

        public BooksHolderView(View itemView) {
            super(itemView);
            singleCheckBox = (CheckBox) itemView.findViewById(R.id.single_checkBox);
            goodsImage = (ImageView) itemView.findViewById(R.id.goods_image);
            goodsName = (TextView) itemView.findViewById(R.id.goods_name);
            goods_size = (TextView) itemView.findViewById(R.id.goods_size);
            goodsPrice = (TextView) itemView.findViewById(R.id.goods_price);
            reduceGoodsNum = (TextView) itemView.findViewById(R.id.reduce_goodsNum);
            goodsNum = (TextView) itemView.findViewById(R.id.goods_Num);
            increaseGoodsNum = (TextView) itemView.findViewById(R.id.increase_goods_Num);
            goodsSize = (TextView) itemView.findViewById(R.id.goodsSize);
            delGoods = (TextView) itemView.findViewById(R.id.del_goods);
        }
    }

    //item click
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onUpdateCount(BookData book, int position);
        void onCheckedChange(BookData book,int position);
        void onDelete(BookData book,int position);
    }

    public void setOnItemClickListener(OnItemClickListener pOnItemClickListener) {
        mOnItemClickListener = pOnItemClickListener;
    }
}
