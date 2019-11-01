package com.hqmy.market.view.mainfragment.consume;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.utils.TextUtil;
import com.lwkandroid.imagepicker.utils.BroadcastManager;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AllCityDto;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.CityDto;
import com.hqmy.market.bean.HotCityDto;
import com.hqmy.market.bean.SecondLevelCityCto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.InputUtil;
import com.hqmy.market.common.utils.PinyinUtils;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.QGridView;
import com.hqmy.market.view.adapter.CYBChangeCityGridViewAdapter;
import com.hqmy.market.view.adapter.ContactAdapter;
import com.hqmy.market.view.widgets.autoview.ClearEditText;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableHeaderAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * 选择城市页面
 */
public class SelectCityActivity extends BaseActivity {
    public static final String CURRENT_CITY = "current_city";
    @BindView(R.id.iv_title_back)
    ImageView iv_title_back;
    @BindView(R.id.tv_title_right)
    TextView tv_title_right;
    @BindView(R.id.indexableLayout)
    IndexableLayout indexableLayout;

    private ContactAdapter mAdapter;
    private BannerHeaderAdapter mBannerHeaderAdapter;
    private CYBChangeCityGridViewAdapter cybChangeCityGridViewAdapter;
    private List<AllCityDto> allCityList = new ArrayList<AllCityDto>();
    private List<CityDto> cList = new ArrayList<>();
    private List<HotCityDto> hList = new ArrayList<>();
    private String cName;
    private String fromStr;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_city;
    }

    @Override
    public void initView() {
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
           fromStr = bundle.getString("from");
        }
        initAdapter();
        getLocation();
        getHotCityList();
        getAllCityList();
    }

    @Override
    public void initListener() {
        bindClickEvent(iv_title_back, () -> {
            finish();
        });

        bindClickEvent(tv_title_right, () -> {
            finish();
        });

        mAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<CityDto>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, CityDto entity) {
                if (originalPosition >= 0) {
                    String cityName = entity.getCityName();
//                    if(fromStr.equals("entityStore")) {

//                        Intent intent = new Intent(SelectCityActivity.this, EntityStoreActivity.class);
//                        intent.putExtra("city", cityName);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }else {
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cityName);
                        finish();

                }
            }
        });
    }

    public void initAdapter(){
        mAdapter = new ContactAdapter(this);
        indexableLayout.setAdapter(mAdapter);
        indexableLayout.setOverlayStyle_Center();
        // indexableLayout.setOverlayStyle_MaterialDesign(Color.RED);
        // 全字母排序。  排序规则设置为：每个字母都会进行比较排序；速度较慢
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);
        // indexableLayout.addHeaderAdapter(new SimpleHeaderAdapter<>(mAdapter, "☆",null, null));
        //构造函数里3个参数,分别对应 (IndexBar的字母索引, IndexTitle, 数据源), 不想显示哪个就传null, 数据源传null时,代表add一个普通的View
        //mMenuHeaderAdapter = new MenuHeaderAdapter("↑", null, initMenuDatas());
        //indexableLayout.addHeaderAdapter(mMenuHeaderAdapter);
        // 这里BannerView只有一个Item, 添加一个长度为1的任意List作为第三个参数
        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
        mBannerHeaderAdapter = new BannerHeaderAdapter("", null, bannerList);
        indexableLayout.addHeaderAdapter(mBannerHeaderAdapter);
    }

    /**
     * 自定义的Banner Header
     */
    class BannerHeaderAdapter extends IndexableHeaderAdapter {
        private static final int TYPE = 1;

        public BannerHeaderAdapter(String index, String indexTitle, List datas) {
            super(index, indexTitle, datas);
        }

        @Override
        public int getItemViewType() {
            return TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(SelectCityActivity.this).inflate(R.layout.item_city_header, parent, false);
            VH holder = new VH(view);
            return holder;
        }

        @Override
        public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {
            // 数据源为null时, 该方法不用实现
            final VH vh = (VH) holder;
            //cList = new ArrayList<>();
            //for(int i = 0; i<city.length; i++){
            //    hList.add(city[i]);
            //}
            cybChangeCityGridViewAdapter=new CYBChangeCityGridViewAdapter(SelectCityActivity.this, hList);
            vh.head_home_change_city_gridview.setAdapter(cybChangeCityGridViewAdapter);
            vh.item_header_city_dw.setText(cName);
            vh.head_home_change_city_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(TextUtil.isNotEmpty(fromStr)){
                        if(fromStr.equals("entityStore")){
//                            Intent intent = new Intent(SelectCityActivity.this, EntityStoreActivity.class);
//                            intent.putExtra("city", hList.get(position).getName());
//                            setResult(RESULT_OK, intent);
//                            finish();
                            BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, hList.get(position).getName());
                            finish();
                        }else {
                            String cityName = hList.get(position).getName();
                            BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cityName);
                            finish();
                        }
                    }

                }
            });
            vh.item_header_city_dw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fromStr.equals("entityStore")){
//                        Intent intent = new Intent(SelectCityActivity.this, EntityStoreActivity.class);
                        //                        intent.putExtra("city", vh.item_header_city_dw.getText().toString());
                        //                        setResult(RESULT_OK, intent);
                        //                        finish();
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, vh.item_header_city_dw.getText().toString());
                        finish();
                    }else {
                        String cityName = vh.item_header_city_dw.getText().toString();
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cityName);
                        finish();
                    }
                }
            });

            vh.tv_re_loction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getLocation();
                    vh.item_header_city_dw.setText(cName);
                    if(fromStr.equals("entityStore")){
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cName);
                        finish();
//                        Intent intent = new Intent(SelectCityActivity.this, EntityStoreActivity.class);
//                        intent.putExtra("city", cName);
//                        setResult(RESULT_OK, intent);
//                        finish();
                    }else {
                        BroadcastManager.getInstance(SelectCityActivity.this).sendBroadcast(Constants.CHOICE_CITY, cName);
                        finish();
                    }
                }
            });

            vh.et_search_str.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    //防止两次发送请求
                    if (actionId == EditorInfo.IME_ACTION_SEND ||
                            (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        switch (event.getAction()) {
                            case KeyEvent.ACTION_UP:
                                String seachStr = vh.et_search_str.getText().toString().trim();
                                filterData(seachStr);
                                InputUtil.HideKeyboard(vh.et_search_str);
                                return true;
                            default:
                                return true;
                        }
                    }
                    return false;
                }
            });

            vh.et_search_str.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    if (TextUtils.isEmpty(s.toString())){
                        vh.tv_search_contacts_view.setVisibility(View.VISIBLE);
                        mAdapter.setDatas(cList);
                    }else{
                        vh.tv_search_contacts_view.setVisibility(View.GONE);
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                }
                @Override
                public void afterTextChanged(Editable edit) {
                }
            });
        }

        private class VH extends RecyclerView.ViewHolder {
            GridView  head_home_change_city_gridview;
            TextView  item_header_city_dw;
            TextView  tv_re_loction;
            TextView  tv_search_contacts_view;
            ClearEditText et_search_str;

            public VH(View itemView) {
                super(itemView);
                head_home_change_city_gridview =(QGridView)itemView.findViewById(R.id.item_header_city_gridview);
                item_header_city_dw = itemView.findViewById(R.id.item_header_city_dw);
                tv_re_loction = itemView.findViewById(R.id.tv_re_loction);
                tv_search_contacts_view = itemView.findViewById(R.id.tv_search_contacts_view);
                et_search_str = itemView.findViewById(R.id.et_search_str);
            }
        }
    }

    private List<CityDto> initDatas() {
        for(int i=0; i<allCityList.size(); i++) {
           List<SecondLevelCityCto> sList = allCityList.get(i).getChildren();
           for (int j = 0; j < sList.size(); j++) {
               CityDto cd = new CityDto();
               cd.setCityName(sList.get(j).getName());
               cd.setCityCode(sList.get(j).getCode());
               cList.add(cd);
           }
        }
        return cList;
    }

    private void getHotCityList(){
        //showLoadDialog();
        DataManager.getInstance().getHotCityList(new DefaultSingleObserver<HttpResult<List<HotCityDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<HotCityDto>> result) {
                //dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        List<HotCityDto> hotList= result.getData();
                        if (hotList != null) {
                            hList.addAll(hotList);
                            mBannerHeaderAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    private void getAllCityList(){
        //showLoadDialog();
        DataManager.getInstance().getAllCityList(new DefaultSingleObserver<HttpResult<List<AllCityDto>>>() {
            @Override
            public void onSuccess(HttpResult<List<AllCityDto>> result) {
                //dissLoadDialog();
                if(result != null) {
                    if(result.getData() != null) {
                        List<AllCityDto> aList= result.getData();
                        if (aList != null) {
                            allCityList.addAll(aList);
                            mAdapter.setDatas(initDatas());
                        }
                    }
                }
            }
            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    private void getLocation(){
        //showLoadDialog();
        DataManager.getInstance().getLocation(new DefaultSingleObserver<HttpResult<AreaDto>>() {
            @Override
            public void onSuccess(HttpResult<AreaDto> result) {
                //dissLoadDialog();
                if(result != null){
                    if(result.getData() != null){
                        cName = result.getData().getName();
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        });
    }

    /**
     * 根据输入框中的值来过滤数据并更新RecyclerView
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<CityDto> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = cList;
        }
        else {
            filterDateList.clear();
            if (cList != null && !cList.isEmpty()) {
                for (CityDto item : cList) {
                    String name = item.getCityName() == null ? "*" : item.getCityName();
                    if (name.indexOf(filterStr.toString()) != -1 ||
                            PinyinUtils.getFirstSpell(name).startsWith(filterStr.toString())
                            //不区分大小写
                            || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr.toString())
                            || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr.toString())
                            || PinyinUtils.getPingYin(name).toLowerCase().contains(filterStr.toString())
                            || PinyinUtils.getPingYin(name).toUpperCase().contains(filterStr.toString())
                            ) {
                        filterDateList.add(item);
                    }
                }
            }
        }
        mAdapter.setDatas(filterDateList);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
