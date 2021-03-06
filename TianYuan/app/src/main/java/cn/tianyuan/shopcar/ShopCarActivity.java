package cn.tianyuan.shopcar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;

public class ShopCarActivity extends BaseActivity implements View.OnClickListener, BookAdapter.CheckInterface, BookAdapter.ModifyCountInterface {
    private static final String TAG = ShopCarActivity.class.getSimpleName();

    @BindView(R.id.listView)
    RecyclerView listView;
    @BindView(R.id.all_checkBox)
    CheckBox allCheckBox;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.go_pay)
    TextView goPay;
    @BindView(R.id.order_info)
    LinearLayout orderInfo;
    @BindView(R.id.share_goods)
    TextView shareGoods;
    @BindView(R.id.collect_goods)
    TextView collectGoods;
    @BindView(R.id.del_goods)
    TextView delGoods;
    @BindView(R.id.share_info)
    LinearLayout shareInfo;
    @BindView(R.id.ll_cart)
    LinearLayout llCart;
    @BindView(R.id.shoppingcat_num)
    TextView shoppingcatNum;
    @BindView(R.id.actionBar_edit)
    Button actionBarEdit;
    @BindView(R.id.layout_empty_shopcart)
    LinearLayout empty_shopcart;

    private Context mcontext;
    private double mtotalPrice = 0.00;
    private int mtotalCount = 0;

    //false就是编辑，ture就是完成
    private boolean flag = false;
    private BookAdapter adapter;
    private List<BookData> goods; //子元素的列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopcar);
        ButterKnife.bind(this);
        initData();
        initEvents();
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个list中，对应着组元素的下辖子元素被放在Map中
     * 其Key是组元素的Id
     */
    private void initData() {
        mcontext = this;
        goods = new ArrayList<BookData>();
        Random random = new Random();
        for (int j = 0; j < 10; j++) {
            int[] img = {R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz, R.drawable.cmaz};
            //i-j 就是商品的id， 对应着第几个店铺的第几个商品，1-1 就是第一个店铺的第一个商品
            goods.add(new BookData("id" + j, "user"+j, "type"+j, "书本"+(j+1),"第"+j+"本书-出头天者", random.nextInt(50), random.nextInt(5), 0, "XXX"));
        }
    }

    private void initEvents() {
        actionBarEdit.setOnClickListener(this);
        adapter = new BookAdapter(mcontext, goods);
        adapter.setCheckInterface(this);//关键步骤1：设置复选框的接口
        adapter.setModifyCountInterface(this); //关键步骤2:设置增删减的接口
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartNum();
    }

    /**
     * 设置购物车的数量
     */
    private void setCartNum() {
        int count = 0;
        if (goods != null) {
            count = goods.size();
        }
        //购物车已经清空
        if (count == 0) {
            clearCart();
        } else {
            shoppingcatNum.setText("购物车(" + count + ")");
        }

    }

    private void clearCart() {
        shoppingcatNum.setText("购物车(0)");
        actionBarEdit.setVisibility(View.GONE);
        llCart.setVisibility(View.GONE);
        empty_shopcart.setVisibility(View.VISIBLE);//这里发生过错误
    }

    /**
     * 删除操作
     * 1.不要边遍历边删除,容易出现数组越界的情况
     * 2.把将要删除的对象放进相应的容器中，待遍历完，用removeAll的方式进行删除
     */
    private void doDelete() {
        List<BookData> toBeDeleteChilds = new ArrayList<BookData>();//待删除的子元素
        List<BookData> child = goods;
        for (int j = 0; j < child.size(); j++) {
            if (child.get(j).isChoosed) {
                toBeDeleteChilds.add(child.get(j));
            }
        }
        child.removeAll(toBeDeleteChilds);
        //重新设置购物车
        setCartNum();
        adapter.notifyDataSetChanged();

    }

    /**
     * @param position  组元素的位置
     * @param isChecked 子元素的选中与否
     */
    @Override
    public void checkChild(int position, boolean isChecked) {
        adapter.notifyDataSetChanged();
        calulate();

    }

    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        BookData good = (BookData) adapter.getChild(position);
        int count = good.count;
        count++;
        good.count = count;
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @param position
     * @param showCountView
     * @param isChecked
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        BookData good = (BookData) adapter.getChild(position);
        int count = good.count;
        if (count == 1) {
            return;
        }
        count--;
        good.count = count;
        ((TextView) showCountView).setText("" + count);
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * @param position
     */
    @Override
    public void childDelete(int position) {
        goods.remove(position);
        adapter.notifyDataSetChanged();
        calulate();
    }

    public void doUpdate(int position, View showCountView, boolean isChecked) {
        BookData good = (BookData) adapter.getChild(position);
        int count = good.count;
        Log.i(TAG, "进行更新数据，数量" + count + "");
        ((TextView) showCountView).setText(String.valueOf(count));
        adapter.notifyDataSetChanged();
        calulate();


    }

    private void setVisiable() {
        Log.e(TAG, "setVisiable: ");
        flag = !flag;
        if (flag) {
            orderInfo.setVisibility(View.GONE);
            shareInfo.setVisibility(View.VISIBLE);
            actionBarEdit.setText("完成");
        } else {
            orderInfo.setVisibility(View.VISIBLE);
            shareInfo.setVisibility(View.GONE);
            actionBarEdit.setText("编辑");
        }
    }

    @OnClick({R.id.all_checkBox, R.id.go_pay, R.id.share_goods, R.id.collect_goods, R.id.del_goods, R.id.actionBar_edit})
    public void onClick(View view) {
        Log.e(TAG, "onClick: "+view);
        AlertDialog dialog;
        switch (view.getId()) {
            case R.id.all_checkBox:
                doCheckAll();
                break;
            case R.id.go_pay:
                if (mtotalCount == 0) {
                    toast(mcontext, "请选择要支付的商品");
                    return;
                }
                dialog = new AlertDialog.Builder(mcontext).create();
                dialog.setMessage("总计:" + mtotalCount + "种商品，" + mtotalPrice + "元");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "支付", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.show();
                break;
            case R.id.share_goods:
                if (mtotalCount == 0) {
                    toast(mcontext, "请选择要分享的商品");
                    return;
                }
                toast(mcontext, "分享成功");
                break;
            case R.id.collect_goods:
                if (mtotalCount == 0) {
                    toast(mcontext, "请选择要收藏的商品");
                    return;
                }
                toast(mcontext, "收藏成功");
                break;
            case R.id.del_goods:
                if (mtotalCount == 0) {
                    toast(mcontext, "请选择要删除的商品");
                    return;
                }
                dialog = new AlertDialog.Builder(mcontext).create();
                dialog.setMessage("确认要删除该商品吗?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.show();
                break;
            case R.id.actionBar_edit:
                setVisiable();
                break;
        }
    }

    /**
     * 全选和反选
     * 错误标记：在这里出现过错误
     */
    private void doCheckAll() {
        List<BookData> child = goods;
        for (int j = 0; j < child.size(); j++) {
            child.get(j).isChoosed = (allCheckBox.isChecked());//这里出现过错误
        }
        adapter.notifyDataSetChanged();
        calulate();
    }

    /**
     * 计算商品总价格，操作步骤
     * 1.先清空全局计价,计数
     * 2.遍历所有的子元素，只要是被选中的，就进行相关的计算操作
     * 3.给textView填充数据
     */
    private void calulate() {
        mtotalPrice = 0.00;
        mtotalCount = 0;
        List<BookData> child = goods;
        for (int j = 0; j < child.size(); j++) {
            BookData good = child.get(j);
            if (good.isChoosed) {
                mtotalCount++;
                mtotalPrice += good.price * good.count;
            }
        }
        totalPrice.setText("￥" + mtotalPrice + "");
        goPay.setText("去支付(" + mtotalCount + ")");
        if (mtotalCount == 0) {
            setCartNum();
        } else {
            shoppingcatNum.setText("购物车(" + mtotalCount + ")");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter = null;
        goods.clear();
        mtotalPrice = 0.00;
        mtotalCount = 0;
    }

}
