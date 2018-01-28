package cn.tianyuan.shopcar;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.R;
import cn.tianyuan.common.util.UtilTool;

/**
 * Created by Administrator on 2018/1/24.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.GoodsHolderView> {
    private static final String TAG = BookAdapter.class.getSimpleName();

    private List<BookData> goods;
    private ModifyCountInterface modifyCountInterface;
    private CheckInterface checkInterface;
    Context mcontext;

    public BookAdapter(Context context, List<BookData> goods) {
        this.mcontext = context;
        this.goods = goods;
    }

    public void setGoods(List<BookData> goods) {
        this.goods = goods;
        notifyDataSetChanged();
    }

    public BookData getChild(int position){
        if(goods == null)
            return null;
        if(position < 0 || position >= goods.size())
            return null;
        return goods.get(position);
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public GoodsHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shopcat_product, parent, false);
        GoodsHolderView holder = new GoodsHolderView(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GoodsHolderView holder, final int position) {
        final BookData child = goods.get(position);

        holder.goodsName.setText(child.descriptor);
        holder.goodsPrice.setText("￥" + child.price + "");
        holder.goodsNum.setText(String.valueOf(child.count));
        holder.goodsImage.setImageResource(R.drawable.cmaz);
        holder.goods_size.setText("类型:" + child.typeId);

        holder.singleCheckBox.setChecked(child.isChoosed);
        holder.singleCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                child.isChoosed = checked;
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
                showDialog(position, holder.goodsNum, holder.singleCheckBox.isChecked(), child);
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
         * @param position 子元素的位置
         * @param isChecked     子元素的选中与否
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
         * @param position 元素的位置
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
        if (goods == null)
            return 0;
        return goods.size();
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

    public class GoodsHolderView extends RecyclerView.ViewHolder {

        @BindView(R.id.single_checkBox)
        CheckBox singleCheckBox;
        @BindView(R.id.goods_image)
        ImageView goodsImage;
        @BindView(R.id.goods_name)
        TextView goodsName;
        @BindView(R.id.goods_size)
        TextView goods_size;
        @BindView(R.id.goods_price)
        TextView goodsPrice;
        @BindView(R.id.reduce_goodsNum)
        TextView reduceGoodsNum;
        @BindView(R.id.goods_Num)
        TextView goodsNum;
        @BindView(R.id.increase_goods_Num)
        TextView increaseGoodsNum;
        @BindView(R.id.goodsSize)
        TextView goodsSize;
        @BindView(R.id.del_goods)
        TextView delGoods;

        public GoodsHolderView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
