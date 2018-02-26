package cn.tianyuan.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.bookmodel.response.BookBeen;
import cn.tianyuan.bookmodel.response.TypeListResponse;
import cn.tianyuan.common.view.picker.PickerUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by chenbin on 2018/1/27.
 */

public class SearchBookActivity extends BaseActivity implements ISearchUI {
    private static final String TAG = SearchBookActivity.class.getSimpleName();

    @BindView(R.id.books)
    RecyclerView mBookList;

    TypeBookAdapter mAdapter = new TypeBookAdapter();
    SearchPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types);
        ButterKnife.bind(this);
        init();
    }

    private void init(){
        mBookList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new TypeBookAdapter();
        mBookList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new TypeBookAdapter.OnItemClickListener() {
            @Override
            public void onRecyclerItemClick(BookBeen item, int position) {
                goBookDetailActivity(item);
            }
        });
        typeNames = new ArrayList<>();
        mPresenter = new SearchPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.pullAllTypes();
        mPresenter.pullAllBooks();
    }

    @OnClick(R.id.choice_type)
    public void onChoice(){
        PickerUtils.onStringArray(this, typeNames, new PickerUtils.PickerResultListener() {
            @Override
            public void onResult(String result, Object... detailedParms) {
                Log.d(TAG, "onResult: "+detailedParms);
                if(detailedParms != null && detailedParms.length > 0){
                    int position = (int) detailedParms[0];
                    if(position >= 0 && position < types.size())
                    mPresenter.pullTypeBooks(types.get(position).id);
                }
            }
        });
    }

    @Override
    public void OnAllBooksList(List<BookBeen> books) {
        mAdapter.setData(books);
    }

    @Override
    public void OnTypeBooksList(List<BookBeen> books) {
        mAdapter.setData(books);
    }

    @Override
    public void onError(String msg) {
        Observable.just(msg)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    Toast.makeText(getApplicationContext(), t, Toast.LENGTH_SHORT).show();
                });
    }

    private List<String> typeNames;
    private List<TypeListResponse.Type> types;
    @Override
    public void onTypes(List<TypeListResponse.Type> allTypes) {
        if(allTypes != null && allTypes.size() > 0) {
            typeNames.clear();
            types = allTypes;
            for (int i = 0; i < allTypes.size(); i++) {
                typeNames.add(allTypes.get(i).name);
            }
        }
    }
}
