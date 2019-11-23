package com.hqmy.market.common;

import com.hqmy.market.base.BaseApplication;

/**
 * 通用标签工具
 * Created by rzb on 2019/04/18.
 */
public class Constants {
    /**
     * 是否打开调试信息
     */
    public static final boolean open_log = true;

//    public final static String BASE_URL = "http://ax.jmlax.com/";
    public final static String BASE_URL = "http://app.b-market.shop/";
    /**
     * 请求http网络图片前缀url。 后台上传
     */
    public static final String WEB_IMG_URL_UPLOADS = BASE_URL + "uploads/";
    /**
     * 请求http网络图片前缀url， 评论，头像,海边幻灯片
     */
    public static final String WEB_IMG_URL_STORAGE = BASE_URL + "storage/";
    /**
     * 请求http网络图片前缀url 银行卡
     */
    public static final String WEB_IMG_URL_CID = BASE_URL;
    public static final String WX_APP_ID = "wx3d60d453e26acc76";
    public static final String WX_LOGIN_BROADCAST = "wx_login_broadcast";
    public static final String INTENT_CODE = "code";
    /**
     * 保存本地图片基地址
     */
    public static final String IMG_BASE_PATH = BaseApplication.getInstance().getExternalCacheDir() + "/putImg/";
    /**
     * 程序登录成功
     */
    public static final String NO_LOGIN_SUCCESS = "login_success";
    /**
     * 是否是第一次登录
     */
    public static final String IS_FIRST_RUN = " isFirstRun";
    /**
     * 当前用户凭证
     */
    public final static String APP_USER_KEY = "app_user_key";
    public final static int PAGE_SIZE = 15;
    /**
     * 用户登录TOKEN
     */
    public final static String USER_TOKEN = "user_token";
    /**
     * 用户名称
     */
    public final static String USER_NAME = "user_name";
    /**
     * 用户头像
     */
    public final static String USER_HEAD = "user_head";
    /**
     * 用户名id
     */
    public final static String USER_ID = "user_id";
    /**
     * 用户登录UID
     */
    public final static String USER_UID = "user_uid";
    /**
     * 用户登录UID
     */
    public final static String USER_PHONE  = "user_phone";
    /**
     * 用户登录信息
     */
    public final static String USER_INFO  = "user_info";
    /**
     * 记住密码
     */
    public final static String REMEMBER_PASSWORD  = "remember_password";
    public final static String IS_PASS  = "isPass";
    /**
     * 用户账号
     */
    public final static String USER_ACCOUNT_NUMBER  = "user_account_number";
    /**
     * 用户密码
     */
    public final static String USER_PASSWORD  = "user_password";
    public static final int INTENT_REQUESTCODE_SEL_ADDRESS = 120;
    /**
     * 全部订单
     */
    public static final String TREE_ORDER_TYPE_ALL = "type_all";
    /**
     * 待付款
     */
    public static final String TREE_ORDER_TYPE_PAYMENT = "type_payment";
    /**
     * 待发货
     */
    public static final String TREE_ORDER_TYPE_DELIVERY = "type_delivery";
    /**
     * 待收货
     */
    public static final String TREE_ORDER_TYPE_RECEIVE = "type_receive";
    /**
     * 待评价
     */
    public static final String TREE_ORDER_TYPE_EVALUATE = "type_evaluate";
    public static final String TYPE_ALL = "type_all";
    public static final String TYPE_PAYMENT = "type_payment";
    public static final String TYPE_DELIVERY = "type_delivery";
    public static final String TYPE_BUY_RECEIVE = "type_buy_receive";

    public static final int INTENT_REQUESTCODE_VERIFIED_IMG1 = 50;
    public static final int INTENT_REQUESTCODE_VERIFIED_IMG2 = 60;
    public static final int INTENT_REQUESTCODE_VERIFIED_IMG3 = 70;
    public static final int INTENT_REQUESTCODE_VERIFIED_IMG4 = 80;
    public static final int INTENT_REQUESTCODE_VERIFIED_IMG5 = 90;

    public static final String INTENT_DATA = "data";
    public static final String INTENT_AREA_NAME = "area_name";
    public static final int INTENT_REQUESTCODE_AREA = 10;

    public static final int INTENT_ADD_FRIEND = 160;
    public static final int INTENT_REQUESTCODE_PUBLISH = 1010;
    public static final int INTENT_REQUESTCODE_PUBLISH_IMG = 80;
    public static final String IMAGEITEM_DEFAULT_ADD = "default_add";

    public static final String ADD_FRIEND_MESSAGE = "添加好友:我是";
    public static final String BROADCAST_CHAT = "broadcast_chat";
    public static final String INTENT_WEB_URL = "web_url";
    public static final String INTENT_WEB_TITLE = "web_title";
	    public static final String INTENT_LOGISTICS_NO = "logistics_no";
    public static final String ADDRESS_ID = "address_id";
    /*群ID*/
    public static final String GROUP_ID = "group_Id";
    /*群头像*/
    public static final String GROUP_HEAD = "group_head";
    /*群公告*/
    public static final String GROUP_REMARK = "group_remark";
    /*本群昵称*/
    public static final String GROUP_NICK = "group_nick";
    /*群名*/
    public static final String GROUP_NAME = "group_name";
    /*类型*/
    public static final String TYPE = "type";
    public static final int HOME_REQUEST_CODE_SCAN = 1003;
    public static final int CHAT_REQUEST_CODE_SCAN = 1007;
    /**
     * 传值类型
     */
    public static final String INTENT_TYPE = "intent_type";
    public static final int PAGE_NUM = 1;

    public static final String INTENT_ID = "id";
    public static final String INTENT_ORDER_NO = "order_id";
    public static final String IMAGEITEM_IMG_URL = "img_url";

    public static final String INTENT_ORDER_STATUS = "order_status";
    public static final String INTENT_MONEY = "money";
    public static final String INTENT_FREIGHT = "freight";
    public static final String INTENT_FLAG = "flag";

    public static final String CHOICE_CITY = "choice_city";

}
