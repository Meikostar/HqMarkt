package com.hqmy.market.view.mainfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import com.hqmy.market.base.BaseFragment;
import com.hqmy.market.bean.CountOrderBean;
import com.hqmy.market.bean.CountStatisticsBean;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.bean.ServiceMenuBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.eventbus.LogoutEvent;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.ShareUtil;
import com.hqmy.market.utils.UserHelper;
import com.hqmy.market.view.activity.AccountSettingActivity;
import com.hqmy.market.view.activity.AttentionActivity;
import com.hqmy.market.view.activity.BankCardManagerActivity;
import com.hqmy.market.view.activity.CollectActivity;
import com.hqmy.market.view.activity.CommentCenterActivity;
import com.hqmy.market.view.activity.HelpCenterActivity;
import com.hqmy.market.view.activity.IntegralBalanceActivity;
import com.hqmy.market.view.activity.LoginActivity;
import com.hqmy.market.view.activity.MessageCenterActivity;
import com.hqmy.market.view.activity.MyBuyGoodActivity;
import com.hqmy.market.view.activity.MyCardBagActivity;
import com.hqmy.market.view.activity.MyEarningsActivity;
import com.hqmy.market.view.activity.MyFootprintActivity;
import com.hqmy.market.view.activity.MyIssueActivity;
import com.hqmy.market.view.activity.MyPublishActivity;
import com.hqmy.market.view.activity.MyQRcodeActivity;
import com.hqmy.market.view.activity.OrderActivity;
import com.hqmy.market.view.activity.RechargeWebActivity;
import com.hqmy.market.view.activity.RefundAfterSalesActivity;
import com.hqmy.market.view.activity.UserInfoActivity;
import com.hqmy.market.view.adapter.ServiceMenuAdapter;
import com.hqmy.market.view.mainfragment.consume.ShopCartActivity;
import com.hqmy.market.view.widgets.CircleImageView;
import com.hqmy.market.view.widgets.RedDotLayout;

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
    ImageView       ivSetting;
    @BindView(R.id.iv_user_msg)
    ImageView       ivUserMsg;
    @BindView(R.id.iv_user_red_point)
    ImageView       ivUserRedPoint;
    @BindView(R.id.rl_mine_user_info)
    RelativeLayout  rlMineUserInfo;
    @BindView(R.id.tv_name)
    TextView        tvName;
    @BindView(R.id.tv_sc)
    TextView        tvSc;
    @BindView(R.id.tv_gz)
    TextView        tvGz;
    @BindView(R.id.tv_zj)
    TextView        tvZj;
    @BindView(R.id.card)
    CardView        card;
    @BindView(R.id.iv_order_daifukuan)
    ImageView       ivOrderDaifukuan;
    @BindView(R.id.re_order_daifukuan)
    RedDotLayout    reOrderDaifukuan;
    @BindView(R.id.ll_order_daifukuan)
    LinearLayout    llOrderDaifukuan;
    @BindView(R.id.iv_order_daifahuo)
    ImageView       ivOrderDaifahuo;
    @BindView(R.id.re_order_daifahuo)
    RedDotLayout    reOrderDaifahuo;
    @BindView(R.id.ll_order_daifahuo)
    LinearLayout    llOrderDaifahuo;
    @BindView(R.id.iv_order_daishouhuo)
    ImageView       ivOrderDaishouhuo;
    @BindView(R.id.re_order_daishouhuo)
    RedDotLayout    reOrderDaishouhuo;
    @BindView(R.id.ll_order_daishouhuo)
    LinearLayout    llOrderDaishouhuo;
    @BindView(R.id.iv_order_daipingjia)
    ImageView       ivOrderDaipingjia;
    @BindView(R.id.re_order_daipingjia)
    RedDotLayout    reOrderDaipingjia;
    @BindView(R.id.ll_order_daipingjia)
    LinearLayout    llOrderDaipingjia;
    @BindView(R.id.iv_order_tuikuan)
    ImageView       ivOrderTuikuan;
    @BindView(R.id.re_order_tuikuan)
    RedDotLayout    reOrderTuikuan;
    @BindView(R.id.ll_refund_after_sales)
    LinearLayout    llRefundAfterSales;
    @BindView(R.id.card)
    CardView        card;
    @BindView(R.id.iv_banner)
    ImageView       ivBanner;
    @BindView(R.id.ll_sm)
    LinearLayout    llSm;
    @BindView(R.id.ll_dp)
    LinearLayout    llDp;
    @BindView(R.id.ll_kf)
    LinearLayout    llKf;
    @BindView(R.id.ll_yh)
    LinearLayout    llYh;
    @BindView(R.id.tv_state)
    TextView        tvState;
    @BindView(R.id.tv_sh)
    TextView        tvSh;
    @BindView(R.id.ll_ewm)
    LinearLayout    llEwm;
    @BindView(R.id.ll_hy)
    LinearLayout    llHy;
    @BindView(R.id.ll_sy)
    LinearLayout    llSy;
    @BindView(R.id.card)
    CardView        card;
    @BindView(R.id.civ_user_avatar)
    CircleImageView civUserAvatar;
    Unbinder unbinder;
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

    @Override
    protected void initData() {
        getMenu();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void userCountStatistics() {
        DataManager.getInstance().userCountStatistics(new DefaultSingleObserver<HttpResult<CountStatisticsBean>>() {
            @Override
            public void onSuccess(HttpResult<CountStatisticsBean> countStatisticsBean) {
                if (countStatisticsBean != null && countStatisticsBean.getData() != null) {

                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    private void getAllUserOrdersCount() {
        DataManager.getInstance().getAllUserOrdersCount(new DefaultSingleObserver<HttpResult<CountOrderBean>>() {
            @Override
            public void onSuccess(HttpResult<CountOrderBean> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {

                }
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    private void getAlOrdersRefundCount() {
        DataManager.getInstance().getAlOrdersRefundCount(new DefaultSingleObserver<HttpResult<String>>() {
            @Override
            public void onSuccess(HttpResult<String> countOrderBean) {
                if (countOrderBean != null && countOrderBean.getData() != null) {

                }

            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    private void getUserInfo() {
        DataManager.getInstance().getUserInfo(new DefaultSingleObserver<PersonalInfoDto>() {
            @Override
            public void onSuccess(PersonalInfoDto personalInfoDto) {
                userCountStatistics();
                getAllUserOrdersCount();
                getAlOrdersRefundCount();
                mPersonalInfoDto = personalInfoDto;
                ShareUtil.getInstance().save(Constants.USER_HEAD, personalInfoDto.getAvatar());
                if (!TextUtils.isEmpty(personalInfoDto.getName())) {
                    ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getName());
                } else {
                    ShareUtil.getInstance().save(Constants.USER_NAME, personalInfoDto.getPhone());
                }
                setUserInfo();
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    private void setUserInfo() {
    }

    @OnClick({R.id.civ_user_avatar
            , R.id.iv_user_msg
            , R.id.iv_setting
            , R.id.ll_collect_attention
            , R.id.ll_mine_issue
            , R.id.ll_mine_comment
            , R.id.ll_mine_buy
            , R.id.ll_mine_sell
            , R.id.ll_mine_publish
            , R.id.ll_mine_footprint
            , R.id.tv_mine_all_order
            , R.id.ll_order_daifukuan
            , R.id.ll_order_daifahuo
            , R.id.ll_order_daishouhuo
            , R.id.ll_order_daipingjia
            , R.id.ll_refund_after_sales
            , R.id.ll_mine_bank_card
            , R.id.ll_mine_earnings
            , R.id.ll_mine_integral_balance
            , R.id.ll_mine_card_bag
            //            , R.id.ll_mine_help_center
            , R.id.ll_mine_qrcode
            , R.id.iv_mine_gwc
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
                gotoActivity(CollectActivity.class);
                break;
            case R.id.ll_collect_attention://关注
                gotoActivity(AttentionActivity.class);
                break;
            case R.id.ll_mine_issue://发布
                gotoActivity(MyIssueActivity.class);
                break;
            case R.id.ll_mine_comment://评论
                gotoActivity(CommentCenterActivity.class);
                break;
            case R.id.ll_mine_buy://我买到的
                goToMyBuyGoodActivity(0);
                break;
            case R.id.ll_mine_sell://我卖出的
                goToMyBuyGoodActivity(1);
                break;
            case R.id.ll_mine_publish://我发布的
                goToMyPublishActivity();
                break;
            case R.id.ll_mine_footprint://我的足迹
                gotoActivity(MyFootprintActivity.class);
                break;
            case R.id.tv_mine_all_order://全部订单
                gotoOrderActivity(0);
                break;
            case R.id.ll_order_daifukuan://待付款
                gotoOrderActivity(1);
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
            case R.id.ll_mine_bank_card://银行卡
                bindClickIsLoginJumpUiEvent(BankCardManagerActivity.class);
                break;
            case R.id.ll_mine_earnings://收益
                bindClickIsLoginJumpUiEvent(MyEarningsActivity.class);
                break;
            case R.id.ll_mine_integral_balance://积分余额
                bindClickIsLoginJumpUiEvent(IntegralBalanceActivity.class);
                break;
            case R.id.ll_mine_card_bag://我的卡包
                bindClickIsLoginJumpUiEvent(MyCardBagActivity.class);
                break;
            //            case R.id.ll_mine_help_center://帮助中心
            //                gotoActivity(HelpCenterActivity.class);
            //                break;
            case R.id.ll_mine_qrcode:
                bindClickIsLoginJumpUiEvent(MyQRcodeActivity.class);
                break;
            case R.id.iv_mine_gwc:
                bindClickIsLoginJumpUiEvent(ShopCartActivity.class);
                break;
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