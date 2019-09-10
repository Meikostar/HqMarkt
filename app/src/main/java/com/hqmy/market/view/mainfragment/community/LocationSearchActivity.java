//package com.hqmy.market.view.mainfragment.community;
//
//import android.content.Context;
//import android.location.LocationManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.amap.api.services.core.AMapException;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.core.PoiItem;
//import com.amap.api.services.core.SuggestionCity;
//import com.amap.api.services.poisearch.PoiResult;
//import com.amap.api.services.poisearch.PoiSearch;
//
//import com.hqmy.market.R;
//import com.hqmy.market.base.BaseActivity;
//import com.hqmy.market.bean.LocationAddressInfo;
//import com.hqmy.market.view.adapter.LocationListAdapter;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import butterknife.BindView;
//
//public class LocationSearchActivity extends BaseActivity {//implements PoiSearch.OnPoiSearchListener
//
//    @BindView(R.id.layout_top)
//    RelativeLayout layout_top;
//    @BindView(R.id.tv_title_text)
//    TextView  mTitleText;
//    @BindView(R.id.tv_title_right)
//    TextView  mTitleRight;
//    @BindView(R.id.et_search_location)
//    EditText  et_search_location;
//    @BindView(R.id.lc_recycleview)
//    RecyclerView lc_recycleview;
//
//    private static final int REQUEST_PERMISSION_LOCATION = 0;
//    private String keyWord = "";// 要输入的poi搜索关键字
//    private PoiResult poiResult; // poi返回的结果
//    private int currentPage = 0;// 当前页面，从0开始计数
//    private PoiSearch.Query query;// Poi查询条件类
//    private PoiSearch poiSearch;// POI搜索
//
//    //声明AMapLocationClient类对象
//    AMapLocationClient mLocationClient = null;
//    //声明AMapLocationClientOption对象
//    public AMapLocationClientOption mLocationOption = null;
//    private LocationListAdapter listAdapter;
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.activity_location_search;
//    }
//
//    @Override
//    public void initView() {
//        lc_recycleview.setLayoutManager(new LinearLayoutManager(this));
//        mTitleText.setText("所在位置");
//        mTitleRight.setVisibility(View.VISIBLE);
//        mTitleRight.setText("完成");
//        mTitleRight.setTextColor(getResources().getColor(R.color.my_color_d60029));
//    }
//
//    @Override
//    public void initData() {
//        getCurrentLocationLatLng();
//    }
//
//    @Override
//    public void initListener() {
//        et_search_location.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                keyWord = String.valueOf(charSequence);
//                if ("".equals(keyWord)) {
//                    Toast.makeText(LocationSearchActivity.this,"请输入搜索关键字",Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    doSearchQuery(keyWord);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }
//
//    /**
//     * 开始进行poi搜索
//     */
//    protected void doSearchQuery(String key) {
//        currentPage = 0;
//        //不输入城市名称有些地方搜索不到
//        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//        query = new PoiSearch.Query(key, "", "");
//        // 设置每页最多返回多少条poiitem
//        query.setPageSize(10);
//        // 设置查询页码
//        query.setPageNum(currentPage);
//        //构造 PoiSearch 对象，并设置监听
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.setOnPoiSearchListener(this);
//        //调用 PoiSearch 的 searchPOIAsyn() 方法发送请求。
//        poiSearch.searchPOIAsyn();
//    }
//
//    /**
//     * POI信息查询回调方法
//     */
//    @Override
//    public void onPoiSearched(PoiResult result, int rCode) {
//        //rCode 为1000 时成功,其他为失败
//        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
//            // 解析result   获取搜索poi的结果
//            if (result != null && result.getQuery() != null) {
//                if (result.getQuery().equals(query)) {  // 是否是同一条
//                    poiResult = result;
//                    ArrayList<LocationAddressInfo> data = new ArrayList<LocationAddressInfo>();//自己创建的数据集合
//                    // 取得第一页的poiitem数据，页数从数字0开始
//                    //poiResult.getPois()可以获取到PoiItem列表
//                    List<PoiItem> poiItems = poiResult.getPois();
//
//                    //若当前城市查询不到所需POI信息，可以通过result.getSearchSuggestionCitys()获取当前Poi搜索的建议城市
//                    List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();
//                    //如果搜索关键字明显为误输入，则可通过result.getSearchSuggestionKeywords()方法得到搜索关键词建议。
//                    List<String> suggestionKeywords =  poiResult.getSearchSuggestionKeywords();
//
//                    //解析获取到的PoiItem列表
//                    for(PoiItem item : poiItems){
//                        //获取经纬度对象
//                        LatLonPoint llp = item.getLatLonPoint();
//                        double lon = llp.getLongitude();
//                        double lat = llp.getLatitude();
//                        //返回POI的名称
//                        String title = item.getTitle();
//                        //返回POI的地址
//                        String text = item.getSnippet();
//                        data.add(new LocationAddressInfo(String.valueOf(lon), String.valueOf(lat), title, text));
//                    }
//                    listAdapter = new LocationListAdapter(this, data);
//                    lc_recycleview.setAdapter(listAdapter);
//                }
//            } else {
//                Toast.makeText(LocationSearchActivity.this,"无搜索结果",Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(LocationSearchActivity.this,"错误码"+rCode,Toast.LENGTH_SHORT).show();
//        }
//
//    }
//    /**
//     * POI信息查询回调方法
//     */
//    @Override
//    public void onPoiItemSearched(PoiItem item, int rCode) {
//
//    }
//
//
//
//    /**
//     * 初始化定位并设置定位回调监听
//     */
//    private void getCurrentLocationLatLng() {
//        //初始化定位
//        mLocationClient = new AMapLocationClient(getApplicationContext());
//        //设置定位回调监听
//        mLocationClient.setLocationListener(mLocationListener);
//        //初始化AMapLocationClientOption对象
//        mLocationOption = new AMapLocationClientOption();
//
//     /* //设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景） 设置了场景就不用配置定位模式等
//        option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
//        if(null != locationClient){
//            locationClient.setLocationOption(option);
//            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//            locationClient.stopLocation();
//            locationClient.startLocation();
//        }*/
//
//        //选择定位模式:高德定位服务包含GPS和网络定位（Wi-Fi和基站定位）两种能力。
//        // 定位SDK将GPS、网络定位能力进行了封装，以三种定位模式对外开放，SDK默认选择使用高精度定位模式。
//        /* //只会使用网络定位
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//        //只使用GPS进行定位
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);*/
//        // 同时使用网络定位和GPS定位,优先返回最高精度的定位结果,以及对应的地址描述信息
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//
//        // 设置为单次定位  : 默认为false
//        mLocationOption.setOnceLocation(true);
//        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。;
//        //mLocationOption.setInterval(3500);
//
//
//        //设置是否返回地址信息（默认返回地址信息）
//    /*mLocationOption.setNeedAddress(true);*/
//
//        //设置定位请求超时时间 : 单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.setHttpTimeOut(20000);
//
//
//        //设置是否开启定位缓存机制: 关闭缓存机制 默认开启 ，
//        // 在高精度模式和低功耗模式下进行的网络定位结果均会生成本地缓存,不区分单次定位还是连续定位。GPS定位结果不会被缓存。
//        mLocationOption.setLocationCacheEnable(false);
//
//        //启动地位:
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
//    }
//
//    /**
//     * 定位回调监听器
//     */
//    public AMapLocationListener mLocationListener = new AMapLocationListener() {
//        @Override
//        public void onLocationChanged(AMapLocation amapLocation) {
//            if (!isGpsEnabled(getApplicationContext())) {
//                Toast toast = Toast.makeText(getApplicationContext(), "未开启GPS", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.CENTER, 0, 0);
//                toast.show();
//            } else {
//                if (amapLocation != null) {
//                    if (amapLocation.getErrorCode() == 0) {
//                        //定位成功回调信息，设置相关消息
//                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                        double currentLat = amapLocation.getLatitude();//获取纬度
//                        double currentLon = amapLocation.getLongitude();//获取经度
//                        LatLonPoint latLonPoint = new LatLonPoint(currentLat, currentLon);  // latlng形式的
//                        amapLocation.getAccuracy();//获取精度信息
//
//                        //获取定位时间
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date date = new Date(amapLocation.getTime());
//                        df.format(date);
//                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                        amapLocation.getCountry();//国家信息
//                        amapLocation.getProvince();//省信息
//                        amapLocation.getCity();//城市信息
//                        amapLocation.getDistrict();//城区信息
//                        amapLocation.getStreet();//街道信息
//                        amapLocation.getStreetNum();//街道门牌号信息
//                        amapLocation.getCityCode();//城市编码
//                        amapLocation.getAdCode();//地区编码
//                        amapLocation.getAoiName();//获取当前定位点的AOI信息
//                        amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
//                        amapLocation.getFloor();//获取当前室内定位的楼层
//                        amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
//
//
//                        if(mLocationClient!=null) {
//                            mLocationClient.stopLocation();//停止定位
//                        }
//                        doSearchQuery(amapLocation.getAoiName());
//
//
//                    } else {
//                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError", "location Error, ErrCode:"
//                                + amapLocation.getErrorCode() + ", errInfo:"
//                                + amapLocation.getErrorInfo());
//                    }
//                }
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mLocationClient != null) {
//            //销毁定位客户端，同时销毁本地定位服务。
//            mLocationClient.onDestroy();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mLocationClient != null) {
//            //停止定位后，本地定位服务并不会被销毁
//            mLocationClient.stopLocation();
//        }
//    }
//
//
//    //判断GPS是否开启
//    private boolean isGpsEnabled(Context context) {
//        LocationManager locationManager = (LocationManager) context
//                .getSystemService(Context.LOCATION_SERVICE);
//        // 判断GPS模块是否开启
//        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//    }
//}
