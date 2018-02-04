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

/**
 * Created by Administrator on 2018/1/24.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BooksHolderView> {
    private static final String TAG = BookAdapter.class.getSimpleName();

    private List<BookData> books;
    private ModifyCountInterface modifyCountInterface;
    private CheckInterface checkInterface;
    Context mcontext;

    public BookAdapter(Context context) {
        this.mcontext = context;
    }

    public void setData(List<BookData> books) {
        this.books = books;
        Log.d(TAG, "setData: ");
        notifyDataSetChanged();
    }

    public BookData getChild(int position) {
        if (books == null)
            return null;
        if (position < 0 || position >= books.size())
            return null;
        return books.get(position);
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public BooksHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopcat_product, parent, false);
        BooksHolderView holder = new BooksHolderView(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final BooksHolderView holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final BookData book = books.get(position);
        holder.goodsName.setText(book.descriptor);
        holder.goodsPrice.setText("￥" + book.price + "");
        holder.goodsNum.setText(String.valueOf(book.count));
        holder.goodsImage.setImageResource(R.drawable.cmaz);
        holder.goods_size.setText("类型:" + book.typeid);
        holder.singleCheckBox.setChecked(book.isChoosed);
        holder.singleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                book.isChoosed = checked;
                holder.singleCheckBox.setChecked(checked);
                checkInterface.checkChild(position, checked);
            }
        });
        holder.increaseGoodsNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, holder.goodsNum, holder.singleCheckBox.isChecked());
            }
        });
        holder.reduceGoodsNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, holder.goodsNum, holder.singleCheckBox.isChecked());
            }
        });
        holder.goodsNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(position, holder.goodsNum, holder.singleCheckBox.isChecked(), book);
            }
        });
        holder.delGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mcontext)
                        .setMessage("确定要删除该商品吗")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modifyCountInterface.childDelete(position);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    /**
     * 店铺的复选框
     */
    public interface CheckInterface {

        /**
         * 子选框状态改变触发的事件
         *
         * @param position  子元素的位置
         * @param isChecked 子元素的选中与否
         */
        void checkChild(int position, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      元素的位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        void doDecrease(int position, View showCountView, boolean isChecked);

        void doUpdate(int position, View showCountView, boolean isChecked);

        /**
         * 删除子Item
         *
         * @param position
         */
        void childDelete(int position);
    }

    @Override
    public int getItemCount() {
        if (books == null)
            return 0;
        return books.size();
    }

    private int count = 0;

    private void showDialog(final int position, final View showCountView, final boolean isChecked, final BookData child) {
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
                if (number == 0) {
                    dialog.dismiss();
                } else {
                    Log.i(TAG, "数量=" + number + "");
                    num.setText(String.valueOf(number));
                    child.count = number;
                    modifyCountInterface.doUpdate(position, showCountView, isChecked);
                    dialog.dismiss();
                }
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
}
