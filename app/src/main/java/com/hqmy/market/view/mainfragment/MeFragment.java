package com.hqmy.market.view.mainfragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hqmy.market.R;
import com.hqmy.market.base.BaseApplication;
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BannerItemDto;
import com.hqmy.market.bean.CountOrderBean;
import com.hqmy.market.bean.CountStatisticsBean;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.bean.ServiceMenuBean;
import com.hqmy.market.bean.ShopInfoDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.eventbus.LogoutEvent;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.utils.UserHelper;
import com.hqmy.market.view.activity.AccountSettingActivity;
import com.hqmy.market.view.activity.AttentionActivity;
import com.hqmy.market.view.activity.BankCardManagerActivity;
import com.hqmy.market.view.activity.CollectActivity;
import com.hqmy.market.view.activity.ConturyActivity;
import com.hqmy.market.view.activity.EnterprisePermissionActivity;
import com.hqmy.market.view.activity.HelpCenterActivity;
import com.hqmy.market.view.activity.InviteFriendsActivity;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.MessageCenterActivity;
import com.hqmy.market.view.activity.MyBuyGoodActivity;
import com.hqmy.market.view.activity.MyEarningsActivity;
import com.hqmy.market.view.activity.MyFootprintActivity;
import com.hqmy.market.view.activity.MyPublishActivity;
import com.hqmy.market.view.activity.MyQRcodeActivity;
import com.hqmy.market.view.activity.OrderActivity;
import com.hqmy.market.view.activity.RechargeWebActivity;
import com.hqmy.market.view.activity.RefundAfterSalesActivity;
import com.hqmy.market.view.activity.RequestLivePermissionActivity;
import com.hqmy.market.view.activity.ShopStoreDetailActivity;
import com.hqmy.market.view.activity.UserInfoActivity;
import com.hqmy.market.view.adapter.ServiceMenuAdapter;
import com.hqmy.market.view.mainfragment.consume.BrandShopDetailActivity;
import com.hqmy.market.view.mainfragment.consume.CommodityDetailActivity;
import com.hqmy.market.view.widgets.CircleImageView;
import com.hqmy.market.view.widgets.RedDotLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的
 */
public class MeFragment extends BaseFragment {


    @BindView(R.id.iv_setting)
    ImageView      ivSetting;
    @BindView(R.id.iv_user_msg)
    ImageView      ivUserMsg;
    @BindView(R.id.iv_user_red_point)
    ImageView      ivUserRedPoint;
    @BindView(R.id.rl_mine_user_info)
    RelativeLayout rlMineUserInfo;
    @BindView(R.id.tv_name)
    TextView       tvName;
    @BindView(R.id.tv_sc)
    TextView       tvSc;
    @BindView(R.id.tv_gz)
    TextView       tvGz;
    @BindView(R.id.tv_zj)
    TextView       tvZj;
    @BindView(R.id.iv_order_daifukuan)
    ImageView      ivOrderDaifukuan;
    @BindView(R.id.re_order_daifukuan)
    RedDotLayout   reOrderDaifukuan;
    @BindView(R.id.ll_order_daifukuan)
    LinearLayout   llOrderDaifukuan;
    @BindView(R.id.iv_order_daifahuo)
    ImageView      ivOrderDaifahuo;
    @BindView(R.id.re_order_daifahuo)
    RedDotLayout   reOrderDaifahuo;
    @BindView(R.id.ll_order_daifahuo)
    LinearLayout   llOrderDaifahuo;
    @BindView(R.id.iv_order_daishouhuo)
    ImageView      ivOrderDaishouhuo;
    @BindView(R.id.re_order_daishouhuo)
    RedDotLayout   reOrderDaishouhuo;
    @BindView(R.id.ll_order_daishouhuo)
    LinearLayout   llOrderDaishouhuo;
    @BindView(R.id.iv_order_daipingjia)
    ImageView      ivOrderDaipingjia;
    @BindView(R.id.re_order_daipingjia)
    RedDotLayout   reOrderDaipingjia;
    @BindView(R.id.ll_order_daipingjia)
    LinearLayout   llOrderDaipingjia;
    @BindView(R.id.iv_order_tuikuan)
    ImageView      ivOrderTuikuan;
    @BindView(R.id.re_order_tuikuan)
    RedDotLayout   reOrderTuikuan;
    @BindView(R.id.ll_refund_after_sales)
    LinearLayout   llRefundAfterSales;

    @BindView(R.id.iv_banner)
    Banner       banner;
    @BindView(R.id.ll_sm)
    LinearLayout llSm;
    @BindView(R.id.ll_dp)
    LinearLayout llDp;
    @BindView(R.id.ll_kf)
    LinearLayout llKf;
    @BindView(R.id.ll_yh)
    LinearLayout llYh;
    @BindView(R.id.tv_state)
    TextView     tvState;
    @BindView(R.id.tv_sh)
    TextView     tvSh;
    @BindView(R.id.ll_ewm)
    LinearLayout llEwm;
    @BindView(R.id.ll_hy)
    LinearLayout llHy;
    @BindView(R.id.ll_sy)
    LinearLayout llSy;

    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    Unbinder unbinder;
    @BindView(R.id.ll_sc)
    LinearLayout llSc;
    @BindView(R.id.ll_gz)
    LinearLayout llGz;
    @BindView(R.id.ll_zj)
    LinearLayout llZj;
    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;

    private PersonalInfoDto mPersonalInfoDto;
    ServiceMenuAdapter menuAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView() {

        menuAdapter = new ServiceMenuAdapter();

        menuAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ServiceMenuBean item = menuAdapter.getItem(position);
                if ("help".equals(item.getClick_event_value())) {
                    gotoActivity(HelpCenterActivity.class);
                } else {
                    Intent intent = new Intent(getActivity(), RechargeWebActivity.class);
                    intent.putExtra(Constants.INTENT_WEB_URL, item.getClick_event_value());
                    intent.putExtra(Constants.INTENT_WEB_TITLE, item.getTitle());
                    startActivity(intent);
                }

            }
        });
    }
    private void startBanner(List<BannerItemDto> data) {
        //设置banner样式(显示圆形指示器)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（指示器居右）
        banner.setIndicatorGravity(BannerConfig.CENTER);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(data);
        //设置banner动画效果
        //        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            BannerItemDto slidersDto = (BannerItemDto) path;
            String imgStr = slidersDto.getPath();
            if (imgStr != null) {
                if (imgStr.contains("http://")) {
                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), imageView, 2,imgStr, R.mipmap.img_default_3);
                } else {
                    GlideUtils.getInstances().loadRoundCornerImg(getActivity(), imageView, 2,Constants.WEB_IMG_URL_UPLOADS + imgStr, R.mipmap.img_default_3);
                }
            }
            imageView.setOnClickListener(new View.OnClickListener() {
//                product_default:商品
//                seller_default:商家
                @Override
                public void onClick(View v) {
                    if(slidersDto.getClick_event_type().equals("product_default")){
                        Bundle bundle = new Bundle();
                        bundle.putString(CommodityDetailActivity.FROM, "gc");
                        bundle.putString(CommodityDetailActivity.PRODUCT_ID, slidersDto.getClick_event_value());
                        bundle.putString(CommodityDetailActivity.MALL_TYPE, "gc");
                        gotoActivity(CommodityDetailActivity.class, bundle);
                    }else if(slidersDto.getClick_event_type().equals("seller_default")){
                        Intent intent = new Intent(context, BrandShopDetailActivity.class);
                        intent.putExtra("id",slidersDto.getClick_event_value());
                        context.startActivity(intent);
                    }
                }
            });


        }
    }

    public void gotoActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
    @Override
    protected void initData() {

        getBottenBanner();
    }

    @Override
    protected void initListener() {

    }
    private void getBottenBanner() {
        //showLoadDialog();
        DataManager.getInstance().getBannerList(new DefaultSingleObserver<HttpResult<BannerInfoDto>>() {
            @Override
            public void onSuccess(HttpResult<BannerInfoDto> result) {
                //dissLoadDialog();
                if (result != null) {
                    if (result.getData() != null&&result.getData().user_center_center.size()>0) {
                        List<BannerItemDto> bannerList = result.getData().user_center_center;
                        startBanner(bannerList);

                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                //dissLoadDialog();
            }
        }, "user_center_center");
    }
    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getUserInfo();
            getMenu();
        }
    }
    private void userCountStatistics() {
        DataManager.getInstance().userCountStatistics(new DefaultSingleObserver<HttpResult<CountStatisticsBean>>() {
            @Override
            public void onSuccess(HttpResult<CountStatisticsBean> countStatisticsBean) {
                if (countStatisticsBean != null && countStatisticsBean.getData() != null) {
                    if (!TextUtils.isEmpty(countStatisticsBean.getData().favorites_count))
                        tvSc.setText(countStatisticsBean.getData().favorites_count);
                    if (!TextUtils.isEmpty(countStatisticsBean.getData().followings_count))
                        tvGz.setText(countStatisticsBean.getData().followings_count);
                    if (!TextUtils.isEmpty(countStatisticsBean.getData().footprints_count))
                        tvZj.setText(countStatisticsBean.getData().footprints_count);

                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }
//    private String paid; //待发货
//    private String shipped;//待评价
//    private String shipping;//待收货
//    private String created;//待付款
    private void getAllUserOrdersCount() {
        DataManager.getInstance().getAllUserOrdersCount(new DefaultSingleObserver<HttpResult<CountOrderBean>>() {
            @Override
            public void onSuccess(HttpResult<CountOrderBean> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {
                    CountOrderBean data = countOrderBean.getData();
                    if(TextUtil.isNotEmpty(data.getPaid())){
                        reOrderDaifahuo.setText(data.getPaid());
                    }else {
                        reOrderDaifahuo.setText("");
                    }
                    if(TextUtil.isNotEmpty(data.getShipped())){
                        reOrderDaipingjia.setText(data.getShipped());
                    }else {
                        reOrderDaipingjia.setText("");
                    }if(TextUtil.isNotEmpty(data.getShipping())){
                        reOrderDaishouhuo.setText(data.getShipping());
                    }else {
                        reOrderDaishouhuo.setText("");
                    }if(TextUtil.isNotEmpty(data.getCreated())){
                        reOrderDaifukuan.setText(data.getCreated());
                    }else {
                        reOrderDaifukuan.setText("");
                    }
                }
            }

            @Override
            public void onError(Throwable throwable) {
                ToastUtil.showToast("");
            }
        });
    }

    private void getAlOrdersRefundCount() {
        DataManager.getInstance().getAlOrdersRefundCount(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {
                    if(Double.valueOf(countOrderBean.getData())!=0){
                        reOrderTuikuan.setText(countOrderBean.getData());
                    }else {
                        reOrderTuikuan.setText("");
                    }

                }

            }

            @Override
            public void onError(Throwable throwable) {


            }
        });
    }



    private String id;
    private int state;
    private int states;
    private int setPay_state;
    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                mPersonalInfoDto = personalInfoDto;
                id = personalInfoDto.getId();
                userCountStatistics();
                getAllUserOrdersCount();
                getAlOrdersRefundCount();
//                getShopInfo();
                if (mPersonalInfoDto != null) {
                    if(mPersonalInfoDto.real_name!=null&&TextUtils.isEmpty(mPersonalInfoDto.real_name.status)){
                        tvState.setVisibility(View.VISIBLE);
                        tvState.setVisibility(View.VISIBLE);

                        if(mPersonalInfoDto.real_name!=null&&mPersonalInfoDto.real_name.data!=null&&mPersonalInfoDto.real_name.data.status!=null){
                            BaseApplication.real_state=mPersonalInfoDto.real_name.data.status;
                            if(mPersonalInfoDto.real_name.data.status.equals("1")&&!TextUtils.isEmpty(mPersonalInfoDto.real_name.data.real_name)){
                                tvState.setText("已认证");
                                state=1;
                            }else if(mPersonalInfoDto.real_name.data.status.equals("0")&&TextUtils.isEmpty(mPersonalInfoDto.real_name.data.real_name)){
                                tvState.setText("认证中");
                                state=1;

                            }else if(mPersonalInfoDto.real_name.data.status.equals("2")&&!TextUtils.isEmpty(mPersonalInfoDto.real_name.data.real_name)){
                                tvState.setText("认证失败");
                                state=0;
                            }else if(mPersonalInfoDto.real_name.data.status.equals("-1")&&TextUtils.isEmpty(mPersonalInfoDto.real_name.data.real_name)){
                                tvState.setText("未认证");
                                state=0;
                            }else {
                                tvState.setText("认证中");
                                state=1;
                            }
                        }else {
                            state=0;
                            tvSh.setText("未认证");
                        }
                        if(mPersonalInfoDto.seller!=null&&mPersonalInfoDto.seller.data!=null&&mPersonalInfoDto.seller.data.status!=null){


                            if(mPersonalInfoDto.seller.data.status.equals("1")){
                                tvSh.setText("待审核");
                                states=1;
                            }else if(mPersonalInfoDto.seller.data.status.equals("2")){
                                tvSh.setText("审核通过");
                                states=2;
                            }else if(mPersonalInfoDto.seller.data.status.equals("3")){
                                tvSh.setText("审核拒绝");
                                states=0;
                            }else {
                                tvSh.setText("未认证");
                                states=0;
                            }
                        }else {
                            tvSh.setText("未认证");
                            states=0;
                        }

                    }
                    if(mPersonalInfoDto.wallet!=null&&mPersonalInfoDto.wallet.data!=null){
                        if(mPersonalInfoDto.wallet.data.isPay_password()){
                            BaseApplication.isSetPay =1;
                        }else{
                            BaseApplication.isSetPay =0;
                        }
                    }else {
                        BaseApplication.isSetPay =0;
                    }
                    ShareUtil.getInstance().save(Constants.USER_HEAD, personalInfoDto.getAvatar());
                    if (!TextUtils.isEmpty(personalInfoDto.getName())) {
                        ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getName());
                    } else {
                        ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getPhone());
                    }
                    setUserInfo();
                }

            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    private void setUserInfo() {
        GlideUtils.getInstances().loadUserRoundImg(getActivity(), civUserAvatar, mPersonalInfoDto.getAvatar());
        tvName.setText(mPersonalInfoDto.getName());
    }

    @OnClick({R.id.civ_user_avatar
            , R.id.iv_user_msg
            , R.id.iv_setting
            , R.id.ll_gz
            , R.id.ll_sc
            , R.id.ll_zj
            , R.id.ll_sm
            , R.id.ll_dp
            , R.id.ll_kf
            , R.id.ll_yh
            , R.id.ll_ewm
            , R.id.ll_hy
            , R.id.ll_sy
            , R.id.ll_order_daifukuan
            , R.id.ll_order_daifahuo
            , R.id.ll_order_daishouhuo
            , R.id.ll_order_daipingjia
            , R.id.ll_refund_after_sales
            , R.id.ll_bg
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.civ_user_avatar:
                clickUserInfo();
                break;
            case R.id.iv_user_msg://消息
                if (UserHelper.isLogin(getActivity())) {
                    gotoActivity(MessageCenterActivity.class);
                }
                break;
            case R.id.iv_setting://设置

                gotoActivity(AccountSettingActivity.class);
                break;
            case R.id.ll_gz://关注
                gotoActivity(AttentionActivity.class);
                break;
            case R.id.ll_sc://关注
                gotoActivity(CollectActivity.class);
                break;
            case R.id.ll_zj://关注
                gotoActivity(MyFootprintActivity.class);
                break;
            case R.id.ll_sm://实名
                if(state==0){
                    gotoActivity(RequestLivePermissionActivity.class);
                }else {
                    ToastUtil.showToast(tvState.getText().toString());
                }

                break;
            case R.id.ll_dp://店铺
                if(states==0){
                    gotoActivity(EnterprisePermissionActivity.class);
                }  else if(states==2) {

                    Bundle bundle = new Bundle();
                    bundle.putString(ShopStoreDetailActivity.SHOP_DETAIL_ID, mPersonalInfoDto.seller.data.id);
                    gotoActivity(ShopStoreDetailActivity.class, false, bundle);
                }else {

                ToastUtil.showToast(tvSh.getText().toString());
            }

                break;
            case R.id.ll_kf://客服
                gotoActivity(HelpCenterActivity.class);
                break;
            case R.id.ll_yh://银行卡
                gotoActivity(BankCardManagerActivity.class);
                break;
            case R.id.ll_ewm://银行卡
                gotoActivity(MyQRcodeActivity.class);
                break;
            case R.id.ll_hy://银行卡
                gotoActivity(InviteFriendsActivity.class);
                break;
            case R.id.ll_sy://银行卡
                gotoActivity(MyEarningsActivity.class);
                break;


            //            case R.id.ll_mine_footprint://我的足迹
            //
            //                break;
            //            case R.id.ll_mine_issue://发布
            //                gotoActivity(MyIssueActivity.class);
            //                break;
            //            case R.id.ll_mine_comment://评论
            //                gotoActivity(CommentCenterActivity.class);
            //                break;
            //            case R.id.ll_mine_buy://我买到的
            //                goToMyBuyGoodActivity(0);
            //                break;
            //            case R.id.ll_mine_sell://我卖出的
            //                goToMyBuyGoodActivity(1);
            //                break;
            //            case R.id.ll_mine_publish://我发布的
            //                goToMyPublishActivity();
            //                break;

            //            case R.id.tv_mine_all_order://全部订单
            //                gotoOrderActivity(0);
            //                break;
            case R.id.ll_order_daifukuan://待付款
                gotoOrderActivity(1);
                break;
            case R.id.ll_bg://待付款
                gotoOrderActivity(0);

                break;
            case R.id.ll_order_daifahuo://待发货
                gotoOrderActivity(2);
                break;
            case R.id.ll_order_daishouhuo://待收货
                gotoOrderActivity(3);
                break;
            case R.id.ll_order_daipingjia://待评价
                gotoOrderActivity(4);
                break;
            case R.id.ll_refund_after_sales://退款售后
                gotoActivity(RefundAfterSalesActivity.class);
                break;
            //            case R.id.ll_mine_bank_card://银行卡
            //                bindClickIsLoginJumpUiEvent(BankCardManagerActivity.class);
            //                break;
            //            case R.id.ll_mine_earnings://收益
            //                bindClickIsLoginJumpUiEvent(MyEarningsActivity.class);
            //                break;
            //            case R.id.ll_mine_integral_balance://积分余额
            //                bindClickIsLoginJumpUiEvent(IntegralBalanceActivity.class);
            //                break;
            //            case R.id.ll_mine_card_bag://我的卡包
            //                bindClickIsLoginJumpUiEvent(MyCardBagActivity.class);
            //                break;
            //            //            case R.id.ll_mine_help_center://帮助中心
            //            //                gotoActivity(HelpCenterActivity.class);
            //            //                break;
            //            case R.id.ll_mine_qrcode:
            //                bindClickIsLoginJumpUiEvent(MyQRcodeActivity.class);
            //                break;
            //            case R.id.iv_mine_gwc:
            //                bindClickIsLoginJumpUiEvent(ShopCartActivity.class);
            //                break;
        }
    }

    private void gotoOrderActivity(int page) {
        Intent intent = new Intent(getActivity(), OrderActivity.class);
        intent.putExtra("page", page);
        startActivity(intent);
    }

    private void goToMyBuyGoodActivity(int i) {
        Intent intent = new Intent(getActivity(), MyBuyGoodActivity.class);
        intent.putExtra("type", i);
        startActivity(intent);
    }

    private void goToMyPublishActivity() {
        Intent intent = new Intent(getActivity(), MyPublishActivity.class);
        startActivity(intent);
    }

    private void clickUserInfo() {
        if (TextUtils.isEmpty(ShareUtil.getInstance().get(Constants.USER_TOKEN))) {
            gotoActivity(LoginActivity.class);
        } else {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            startActivityForResult(intent, 100);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LogoutEvent event) {
        resetUI();
    }

    /**
     * 退出登录 重置UI
     */
    private void resetUI() {
        GlideUtils.getInstances().loadUserRoundImg(getActivity(), civUserAvatar, "");
        tvName.setText("");

            tvSc.setText("");
            tvGz.setText("");
            tvZj.setText("");
        reOrderTuikuan.setText("");
        reOrderDaifahuo.setText("");
        reOrderDaipingjia.setText("");
        reOrderDaishouhuo.setText("");
        reOrderDaifukuan.setText("");

    }

    private void getMenu() {
        DataManager.getInstance().serviceMenu(new DefaultSingleObserver<HttpResult<List<ServiceMenuBean>>>() {
            @Override
            public void onSuccess(HttpResult<List<ServiceMenuBean>> httpResult) {
                menuAdapter.setNewData(httpResult.getData());
            }
        });
    }
}
