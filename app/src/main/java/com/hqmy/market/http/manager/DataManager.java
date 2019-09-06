package com.hqmy.market.http.manager;

import com.hqmy.market.bean.ActionBean;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.AddressModel;
import com.hqmy.market.bean.AllCityDto;
import com.hqmy.market.bean.AnchorInfo;
import com.hqmy.market.bean.AreaDto;
import com.hqmy.market.bean.AttentionCommunityBean;
import com.hqmy.market.bean.BalanceDto;
import com.hqmy.market.bean.BankCardDto;
import com.hqmy.market.bean.BannerDto;
import com.hqmy.market.bean.BannerInfoDto;
import com.hqmy.market.bean.BaseDto2;
import com.hqmy.market.bean.BrandListItemDto;
import com.hqmy.market.bean.CaptchaImgDto;
import com.hqmy.market.bean.CategorieBean;
import com.hqmy.market.bean.CategoryListdto;
import com.hqmy.market.bean.CheckOutOrderResult;
import com.hqmy.market.bean.CommentDto;
import com.hqmy.market.bean.CommentListBean;
import com.hqmy.market.bean.CommentTopicBean;
import com.hqmy.market.bean.CommodityDetailInfoDto;
import com.hqmy.market.bean.ConfigDto;
import com.hqmy.market.bean.CountOrderBean;
import com.hqmy.market.bean.CountStatisticsBean;
import com.hqmy.market.bean.CouponBean;
import com.hqmy.market.bean.CouponDto;
import com.hqmy.market.bean.DetailDto;
import com.hqmy.market.bean.DynamicBean;
import com.hqmy.market.bean.ExpressList;
import com.hqmy.market.bean.FootInfoDto;
import com.hqmy.market.bean.FriendListItemDto;
import com.hqmy.market.bean.FriendPageDto;
import com.hqmy.market.bean.GiftBean;
import com.hqmy.market.bean.GroupInfoDto;
import com.hqmy.market.bean.GroupListDto;
import com.hqmy.market.bean.HeadLineDetailDto;
import com.hqmy.market.bean.HeadLineDto;
import com.hqmy.market.bean.HotCityDto;
import com.hqmy.market.bean.HotSearchInfo;
import com.hqmy.market.bean.InviteFriendBean;
import com.hqmy.market.bean.LearnRecordInfo;
import com.hqmy.market.bean.LiveCatesBean;
import com.hqmy.market.bean.LiveMessageInfo;
import com.hqmy.market.bean.LiveVideoInfo;
import com.hqmy.market.bean.LoginDto;
import com.hqmy.market.bean.MyOrderDto;
import com.hqmy.market.bean.MyOrderLogisticsDto;
import com.hqmy.market.bean.NewListItemDto;
import com.hqmy.market.bean.NewsDetailDto;
import com.hqmy.market.bean.NewsOtherListItemDto;
import com.hqmy.market.bean.NewsRecommendListDto;
import com.hqmy.market.bean.NoticeDto;
import com.hqmy.market.bean.OnlineApplyInfo;
import com.hqmy.market.bean.OnlineLiveFinishBean;
import com.hqmy.market.bean.OrderCheckoutBean;
import com.hqmy.market.bean.OrderPreviewDto;
import com.hqmy.market.bean.PersonalInfoDto;
import com.hqmy.market.bean.ProductBean;
import com.hqmy.market.bean.ProductDto;
import com.hqmy.market.bean.PublishInfo;
import com.hqmy.market.bean.RecommendListDto;
import com.hqmy.market.bean.RedPacketDto;
import com.hqmy.market.bean.RedPacketInfoDto;
import com.hqmy.market.bean.RegisterDto;
import com.hqmy.market.bean.RoomUserBean;
import com.hqmy.market.bean.ScoreBean;
import com.hqmy.market.bean.ScoreIncomeBean;
import com.hqmy.market.bean.ServiceMenuBean;
import com.hqmy.market.bean.ShopCartInfoDto;
import com.hqmy.market.bean.ShopCartListItemDto;
import com.hqmy.market.bean.ShopInfoDto;
import com.hqmy.market.bean.StoreCategoryDto;
import com.hqmy.market.bean.TagBean;
import com.hqmy.market.bean.TopicDetailDto;
import com.hqmy.market.bean.TopicListItemDto;
import com.hqmy.market.bean.UploadFilesDto;
import com.hqmy.market.bean.UrlInfoDto;
import com.hqmy.market.bean.UserInfoDto;
import com.hqmy.market.bean.VideoBean;
import com.hqmy.market.bean.VideoLiveBean;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.LogUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.RetrofitHelper;
import com.hqmy.market.http.RetrofitService;
import com.hqmy.market.http.request.BaseRequestModel;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.http.response.HttpResultMapper;
import com.hqmy.market.http.response.HttpResultMapper.HttpResultData;
import com.hqmy.market.utils.ShareUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.QueryMap;

/**
 * 接口数据管理类
 * Created by rzb on 2019/4/18.
 */
public class DataManager {

    private static final String          TAG             = DataManager.class.getSimpleName();
    public               RetrofitHelper  retrofitHelper  = RetrofitHelper.getInstance();
    private              RetrofitService retrofitService = retrofitHelper.getServer();
    private static final DataManager     INSTANCE        = new DataManager();

    //获取单例
    public static DataManager getInstance() {
        return INSTANCE;
    }

    private <T> Disposable subscribe(Single<T> observable, DefaultSingleObserver<T> observer) {
        LogUtil.i("-- RXLOG-Thread: subscribe()", Long.toString(Thread.currentThread().getId()));
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return observer;
    }

    /**
     * 将请求实体类转json格式
     *
     * @param baseHttpRequestModel 传入实体类对象
     * @return 返回请求体
     */
    private RequestBody getRequestBody(BaseRequestModel baseHttpRequestModel) {
        LogUtil.i(TAG, retrofitHelper.gson.toJson(baseHttpRequestModel));
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), retrofitHelper.gson.toJson(baseHttpRequestModel));
    }

    private String getToken() {
        return "Bearer " + ShareUtil.getInstance().getString(Constants.USER_TOKEN, "");
    }

    /**
     * 登录
     *
     * @param observer
     * @param baseRequestModel
     */
    public void login(DefaultSingleObserver<LoginDto> observer, BaseRequestModel baseRequestModel) {
        Single<LoginDto> observable = retrofitService.login(getRequestBody(baseRequestModel))
                .map(new HttpResultData<>(baseRequestModel));
        subscribe(observable, observer);
    }

    /**
     * 退出登录
     *
     * @param observer
     */
    public void logout(DefaultSingleObserver<Object> observer) {
        Single<Object> observable = retrofitService.logout(getToken())
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 用户注册接口
     *
     * @param observer
     * @param baseRequestModel
     */
    public void register(DefaultSingleObserver<RegisterDto> observer, BaseRequestModel baseRequestModel) {
        Single<RegisterDto> observable = retrofitService.register(getRequestBody(baseRequestModel))
                .map(new HttpResultData<>(baseRequestModel));
        subscribe(observable, observer);
    }

    /**
     * 获取验图形证码
     *
     * @param observer
     */
    public void getImageCode(DefaultSingleObserver<CaptchaImgDto> observer) {
        Single<CaptchaImgDto> observable = retrofitService.getImgCode()
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取手机验证码
     *
     * @param observer
     * @param baseRequestModel
     */
    public void getSmsCode(DefaultSingleObserver<String> observer, BaseRequestModel baseRequestModel) {
        Single<String> observable = retrofitService.getSmsCode(getRequestBody(baseRequestModel))
                .map(new HttpResultData<>(baseRequestModel));
        subscribe(observable, observer);
    }

    /**
     * 重设密码
     *
     * @param observer 由调用者传过来的观察者对象
     */
    public void resetPwd(DefaultSingleObserver<String> observer, HashMap<String, String> map) {
        Single<String> observable = retrofitService.resetPwd(getToken(), map)
                .map(new HttpResultMapper.HttpResultString(null));
        subscribe(observable, observer);
    }

    /**
     * 更新用户信息
     * @param observer
     */
    public void modifUserInfo(DefaultSingleObserver<PersonalInfoDto> observer, HashMap<String, String> map) {
        Single<PersonalInfoDto> observable = retrofitService.modifUserInfo(getToken(), map)
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取用户信息
     *
     * @param observer
     */
    public void getUserInfo(DefaultSingleObserver<PersonalInfoDto> observer) {
        Single<PersonalInfoDto> observable = retrofitService.getUserInfo(getToken(),"seller,real_name")
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 用户地址列表
     *
     * @param observer
     */
    public void getAddressesList(DefaultSingleObserver<List<AddressDto>> observer) {
        Single<List<AddressDto>> observable = retrofitService.getAddresses(getToken())
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 地区列表
     *
     * @param observer
     */
    public void getAreaList(DefaultSingleObserver<List<AreaDto>> observer, Map<String, String> map) {
        Single<List<AreaDto>> observable = retrofitService.getArea(map)
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 添加用户地址
     *
     * @param observer
     */
    public void addAddressesList(DefaultSingleObserver<AddressDto> observer, BaseRequestModel baseRequestModel) {
        Single<AddressDto> observable = retrofitService.addAddresses(getToken(), getRequestBody(baseRequestModel))
                .map(new HttpResultData<>(baseRequestModel));
        subscribe(observable, observer);
    }

    /**
     * 更新用户地址
     *
     * @param observer
     */
    public void updateAddresses(DefaultSingleObserver<AddressDto> observer, BaseRequestModel baseRequestModel, String address_id) {
        AddressModel model = (AddressModel) baseRequestModel;
        Single<AddressDto> observable = retrofitService.updateAddresses(getToken(), address_id
                , model.getName(), model.getMobile()
                , model.getArea_id(), model.getDetail(),
                model.getIs_default() + "")
                .map(new HttpResultData<>(baseRequestModel));
        subscribe(observable, observer);
    }

    /**
     * 默认用户地址
     *
     * @param observer
     */
    public void getDefaultAddress(DefaultSingleObserver<AddressDto> observer) {
        Single<AddressDto> observable = retrofitService.getDefaultAddress(getToken())
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 删除用户地址
     *
     * @param observer
     */
    public void delAddresses(DefaultSingleObserver<Object> observer, String address_id) {
        Single<Object> observable = retrofitService.delAddresses(getToken(), address_id)
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 上传文件
     *
     * @param observer
     * @param fileType
     * @param file
     */
    public void uploadFiles(DefaultSingleObserver<UploadFilesDto> observer, String fileType, MultipartBody.Part file) {
        Single<UploadFilesDto> observable = retrofitService.uploadFiles(getToken(), fileType, file)
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 我的订单列表
     *
     * @param observer 由调用者传过来的观察者对象
     */
    public void getMyOrderList(DefaultSingleObserver<HttpResult<List<MyOrderDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<MyOrderDto>>> observable = retrofitService.getMyOrderList(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 我的收藏列表
     *
     * @param observer
     */
    public void getCollectionList(DefaultSingleObserver<HttpResult<List<TopicListItemDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<TopicListItemDto>>> observable = retrofitService.getCollectionList(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 删除收藏
     *
     * @param observer
     */
    public void delCollection(DefaultSingleObserver<Object> observer, HashMap<String, String> map) {
        Single<Object> observable = retrofitService.delCollection(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 我的关注列表
     *
     * @param observer
     */
    public void getAttentionList(DefaultSingleObserver<HttpResult<List<AttentionCommunityBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<AttentionCommunityBean>>> observable = retrofitService.getAttentionList(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 我发布的 列表
     *
     * @param observer
     */
    public void getDynamicList(DefaultSingleObserver<HttpResult<List<DynamicBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<DynamicBean>>> observable = retrofitService.getDynamicList(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 发布动态
     */
    public void publishDynamic(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, Object> map) {
        Single<HttpResult<Object>> observable = retrofitService.publishDynamic(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 发布宝贝
     */
    public void publishBaby(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, Object> map) {
        Single<HttpResult<Object>> observable = retrofitService.publishBaby(getToken(),"ax", map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }


    /**
     * 动态的评论 列表
     *
     * @param observer
     */
    public void getDynamicCommentList(DefaultSingleObserver<HttpResult<List<CommentListBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<CommentListBean>>> observable = retrofitService.getDynamicCommentList(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 获取我的评论 列表
     *
     * @param observer
     */
    public void getMyCommentListData(DefaultSingleObserver<HttpResult<List<CommentTopicBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<CommentTopicBean>>> observable = retrofitService.getMyCommentListData(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 动态详情
     *
     * @param observer
     */
    public void getDynamicDetail(DefaultSingleObserver<HttpResult<DynamicBean>> observer, int id) {
        Single<HttpResult<DynamicBean>> observable = retrofitService.getDynamicDetail(getToken(), id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取余额
     *
     * @param observer
     */
    public void getBalance(DefaultSingleObserver<BalanceDto> observer) {
        Single<BalanceDto> observable = retrofitService.getBalance(getToken())
                .map(new HttpResultData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 活动列表
     * @param observer
     * @param map
     */
    public void getActionListData(DefaultSingleObserver<HttpResult<List<ActionBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<ActionBean>>> observable = retrofitService.getActionListData(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 活动详情
     * @param observer
     * @param id
     */
    public void getActionDetailData(DefaultSingleObserver<HttpResult<ActionBean>> observer, int id) {
        Single<HttpResult<ActionBean>> observable = retrofitService.getActionDetailData(getToken(), id, "extra")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 在线报名（活动）
     */
    public void onlineApply(DefaultSingleObserver<HttpResult<Object>> observer, BaseRequestModel baseRequestModel) {
        Single<HttpResult<Object>> observable = retrofitService.onlineApply(getToken(), getRequestBody(baseRequestModel))
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 话题类型标签
     *
     * @param observer
     */
    public void getTagList(DefaultSingleObserver<TagBean> observer) {
        Single<TagBean> observable = retrofitService.getTagList(getToken())
        .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     *新闻频道(每日推荐)
     * @param observer
     */
    public void getNewsRecommendList(DefaultSingleObserver<NewsRecommendListDto> observer) {
        Single<NewsRecommendListDto> observable = retrofitService.getNewsRecommendList()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     *新闻频道(其他)
     * @param observer
     */
    public void getNewsList(DefaultSingleObserver<HttpResult<List<NewsOtherListItemDto>>> observer, String withAllTags) {
        Single<HttpResult<List<NewsOtherListItemDto>>> observable = retrofitService.getNewsList(withAllTags)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 热门话题(每日推荐)
     * @param observer
     */
    public void getTopicRecommendList(DefaultSingleObserver<HttpResult<List<TopicListItemDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<TopicListItemDto>>> observable = retrofitService.getTopicRecommendList("user", map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 热门话题(其他)
     * @param observer
     */
    public void getTopicList(DefaultSingleObserver<HttpResult<List<TopicListItemDto>>> observer, String scopeWithAllTags, Map<String, String> map) {
        Single<HttpResult<List<TopicListItemDto>>> observable = retrofitService.getTopicList(scopeWithAllTags,"user", map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取用户好友信息列表
     * @param observer
     */
    public void getFriendList(DefaultSingleObserver<HttpResult<ArrayList<FriendListItemDto>>> observer) {
        Single<HttpResult<ArrayList<FriendListItemDto>>> observable = retrofitService.getFriendList(getToken(),"friend_user")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     *
     * @param observer
     */
    public void findStoreCategory(DefaultSingleObserver<HttpResult<List<StoreCategoryDto>>> observer) {
        Single<HttpResult<List<StoreCategoryDto>>> observable = retrofitService.findStoreCategory("children.children")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void findOtherCategory(DefaultSingleObserver<HttpResult<List<BaseDto2>>> observer,Map<String,String> map) {
        Single<HttpResult<List<BaseDto2>>> observable = retrofitService.findOtherCategory( map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }



    /**
     * 获取群列表
     * @param observer
     */
    public void getGroupList(DefaultSingleObserver<HttpResult<List<GroupListDto>>> observer, String type, String status) {
        Single<HttpResult<List<GroupListDto>>> observable = retrofitService.getGroupList(getToken(), type, status)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取群相关信息
     * @param observer
     */
    public void getGroupInfo(DefaultSingleObserver<HttpResult<GroupInfoDto>> observer, String groupId) {
        Single<HttpResult<GroupInfoDto>> observable = retrofitService.getGroupInfo(getToken(), groupId, "group_users.user")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }


    /**
     * 根据用户ID获取用户信息
     * @param observer
     */
    public void queryUserInfoPage(DefaultSingleObserver<FriendPageDto> observer, String id) {
        Single<FriendPageDto> observable = retrofitService.queryUserInfoPage(getToken(), id, "friend_user")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 根据电话获取用户信息
     * @param observer
     */
    public void queryUserListByPhone(DefaultSingleObserver<HttpResult<List<UserInfoDto>>> observer, String phone) {
        Single<HttpResult<List<UserInfoDto>>> observable = retrofitService.queryUserListByPhone(getToken(), phone)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 添加好友
     */
    public void addFriend(DefaultSingleObserver<HttpResult<Object>> observer, String friendId, String remark_name, String msg) {
        Single<HttpResult<Object>> observable = retrofitService.addFriend(getToken(), friendId, remark_name, msg, "android")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 验证好友申请
     */
    public void verifyFriend(DefaultSingleObserver<HttpResult<Object>> observer, String friendId, String remark_name) {
        Single<HttpResult<Object>> observable = retrofitService.verifyFriend(getToken(), friendId, remark_name)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 添加群成员
     */
    public void addGroupUser(DefaultSingleObserver<HttpResult<Object>> observer, String groupId, List<String> userIdArr) {
        Single<HttpResult<Object>> observable = retrofitService.addGroupUser(getToken(), groupId, userIdArr)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 删除群成员
     */
    public void  delGroupUser(DefaultSingleObserver<HttpResult<Object>> observer, String groupId, List<String> userIdArr) {
        Single<HttpResult<Object>> observable = retrofitService.delGroupUser(getToken(), groupId, userIdArr)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 发起群聊
     */
    public void creGroup(DefaultSingleObserver<HttpResult<Object>> observer, String groupName, List<String> userIdArr) {
        Single<HttpResult<Object>> observable = retrofitService.creGroup(getToken(), groupName, 1, userIdArr)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 新朋友验证列表
     */
    public void getNewFriendLists(DefaultSingleObserver<HttpResult<List<FriendListItemDto>>> observer) {
        Single<HttpResult<List<FriendListItemDto>>> observable = retrofitService.getNewFriendLists(getToken(), "friend_user")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 删除好友
     */
    public void delFriend(DefaultSingleObserver<HttpResult<Object>> observer, String friend_user_id) {
        Single<HttpResult<Object>> observable = retrofitService.delFriend(getToken(), friend_user_id, "1")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 广告(公用)
     */
    public void getBannerList(DefaultSingleObserver<HttpResult<BannerInfoDto>> observer, String pCode) {
        Single<HttpResult<BannerInfoDto>> observable = retrofitService.getBannerList(pCode)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getCategorisContury(DefaultSingleObserver<HttpResult<List<AreaDto>>> observer) {
        Single<HttpResult<List<AreaDto>>> observable = retrofitService.getCategorisContury()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 广告(公用)
     */
    public void getBannerList(DefaultSingleObserver<HttpResult<BannerInfoDto>> observer, String pCode, Map<String, String> map) {
        Single<HttpResult<BannerInfoDto>> observable = retrofitService.getBannerProductId(pCode,map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 联盟商城品牌列表
     */
    public void getMallsBrandList(DefaultSingleObserver<HttpResult<List<BrandListItemDto>>> observer) {
        Single<HttpResult<List<BrandListItemDto>>> observable = retrofitService.getMallsBrandList()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 联盟商城品牌列表URL
     */
    public void getMallsBrandUrl(DefaultSingleObserver<HttpResult<UrlInfoDto>> observer, String id) {
        Single<HttpResult<UrlInfoDto>> observable = retrofitService.getMallsBrandUrl(getToken(),id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 联盟商城新品列表
     */
    public void getNewList(DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer, String mall_type) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.getNewList(mall_type,"1")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 联盟商场猜你喜欢
     */
    public void getLikeList(DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer, String mall_type) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.getLikeList(mall_type)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 爱心家庭猜你喜欢
     */
    public void getFamilyLikeList(DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer, String mall_type, @QueryMap Map<String,String> map) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.getFamilyLikeList(mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 商品详情(共用)
     */
    public void getGoodsDetail(DefaultSingleObserver<HttpResult<CommodityDetailInfoDto>> observer, String mall_type, String id,HashMap<String, String> map) {
        Single<HttpResult<CommodityDetailInfoDto>> observable = retrofitService.getGoodsDetail(mall_type, id, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 商品详情(共用(登录带Token))
     */
    public void getGoodsDetailToken(DefaultSingleObserver<HttpResult<CommodityDetailInfoDto>> observer, String mall_type, String id,HashMap<String, String> map) {
        Single<HttpResult<CommodityDetailInfoDto>> observable = retrofitService.getGoodsDetailToken(getToken(), mall_type, id, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 购物车列表
     */
    public void findShoppingCartList(DefaultSingleObserver<HttpResult<ShopCartInfoDto>> observer, String mall_type) {
        Single<HttpResult<ShopCartInfoDto>> observable = retrofitService.findShoppingCartList(getToken(), mall_type, "shop_name")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 删除购物车商品
     */
    public void delShoppingCart(DefaultSingleObserver<HttpResult<Object>> observer, String mall_type, HashMap<String, String> map) {
        Single<HttpResult<Object>> observable = retrofitService.delShoppingCart(getToken(), mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 添加商品到购物车
     */
    public void addShoppingCart(DefaultSingleObserver<HttpResult<Object>> observer, String mall_type, HashMap<String, Object> map) {
        Single<HttpResult<Object>> observable = retrofitService.addShoppingCart(getToken(), mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 类别分类
     */
    public void getCategoryList(DefaultSingleObserver<HttpResult<List<CategoryListdto>>> observer, String mall_type) {
        Single<HttpResult<List<CategoryListdto>>> observable = retrofitService.getCategoryList(mall_type, "children")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 爱心家庭最新上架
     */
    public void getFamilyNewList(DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer,String mall_type, Map<String, String> map) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.getFamilyNewList(mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 爱心家庭最新上架
     */
    public void findGoodsList(Map<String, String> map,DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.findGoodsList( map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }


    /**
     * 发布宝贝
     */
    public void pubProduct(DefaultSingleObserver<HttpResult<Object>> observer, String mall_type, HashMap<String, String> map) {
        Single<HttpResult<Object>> observable = retrofitService.pubProduct(getToken(), mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 品牌推荐
     */
    public void getRecommendList(DefaultSingleObserver<HttpResult<List<RecommendListDto>>> observer, String type) {
        Single<HttpResult<List<RecommendListDto>>> observable = retrofitService.getRecommendList("1", type, "products")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 首页最下面列表
     */
    public void findGoodLists(DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer, String mall_type, Map<String, String> map) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.findGoodLists(mall_type,"1", map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 首页最下面列表
     */
    public void findHomeGoodLists(DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer, String mall_type, Map<String, String> map) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.findHomeGoodLists(mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }


    /**
     * 首页大牌好货
     */
    public void getHomeRecommendList(DefaultSingleObserver<HttpResult<List<RecommendListDto>>> observer, String type) {
        Single<HttpResult<List<RecommendListDto>>> observable = retrofitService.getHomeRecommendList("1", type)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取所有地区值列表
     */
    public void getAllCityList(DefaultSingleObserver<HttpResult<List<AllCityDto>>> observer) {
        Single<HttpResult<List<AllCityDto>>> observable = retrofitService.getAllCityList()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取热门城市
     */
    public void getHotCityList(DefaultSingleObserver<HttpResult<List<HotCityDto>>> observer) {
        Single<HttpResult<List<HotCityDto>>> observable = retrofitService.getHotCityList()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 消息通知列表
     */
   public void getNoticeList(DefaultSingleObserver<HttpResult<List<NoticeDto>>> observer) {
       Single<HttpResult<List<NoticeDto>>> observable = retrofitService.getNoticeList(getToken(),"Modules\\Project\\Notifications\\FriendNotify")
               .map(new HttpResultMapper.HttpResultOtheData<>(null));
       subscribe(observable, observer);
   }

    /**
     * 获取话题详情
     */
    public void getTopicDetail(DefaultSingleObserver<HttpResult<TopicDetailDto>> observer, String hdId) {
        Single<HttpResult<TopicDetailDto>> observable = retrofitService.getTopicDetail(getToken(), hdId, "user,comments")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void privacy_policy(DefaultSingleObserver<HttpResult<DetailDto>> observer) {


        Single<HttpResult<DetailDto>> observable = retrofitService.privacy_policy()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void tos(DefaultSingleObserver<HttpResult<DetailDto>> observer) {
        Single<HttpResult<DetailDto>> observable = retrofitService.tos()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void shop_service(DefaultSingleObserver<HttpResult<DetailDto>> observer) {
        Single<HttpResult<DetailDto>> observable = retrofitService.shop_service()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 店铺信息
     */
    public void getShopInfo(DefaultSingleObserver<HttpResult<ShopInfoDto>> observer, String id) {
        Single<HttpResult<ShopInfoDto>> observable = retrofitService.getShopInfo(getToken(), id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 店铺信息
     */
    public void following_shops_detai(DefaultSingleObserver<HttpResult<ShopInfoDto>> observer, String user_id) {
        Single<HttpResult<ShopInfoDto>> observable = retrofitService.following_shops_detai(getToken(), user_id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void getBrandInfo(DefaultSingleObserver<HttpResult<ShopInfoDto>> observer, String user_id,String include) {
        Single<HttpResult<ShopInfoDto>> observable = retrofitService.getBrandInfo(getToken(), user_id,include)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }



    /**
     * 获取新闻详情
     */
    public void getNewsDetail(DefaultSingleObserver<HttpResult<NewsDetailDto>> observer, String type, String id) {
        Single<HttpResult<NewsDetailDto>> observable = retrofitService.getNewsDetail(type,id,"extra")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getHelpData(DefaultSingleObserver<HttpResult<List<DetailDto>>> observer, String type) {
        Single<HttpResult<List<DetailDto>>> observable = retrofitService.getHelpData(type)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void getHelpDetail(DefaultSingleObserver<HttpResult<DetailDto>> observer, String type,String id) {
        Single<HttpResult<DetailDto>> observable = retrofitService.getHelpDetail(type,id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取评价列表
     */
    public void getCommentsList(DefaultSingleObserver<HttpResult<List<CommentDto>>> observer, String commented_type, String commented_id) {
        Single<HttpResult<List<CommentDto>>> observable = retrofitService.getCommentsList(getToken(), commented_type, commented_id, "user")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取评价列表
     */
    public void getCommentsList(DefaultSingleObserver<HttpResult<List<CommentDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<CommentDto>>> observable = retrofitService.getCommentsList(map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取实体店铺详情
     */
    public void getShopDetail(DefaultSingleObserver<HttpResult<RecommendListDto>> observer, String id, String lat, String lng) {
        Single<HttpResult<RecommendListDto>> observable = retrofitService.getShopDetail(getToken(),id,"area,distance",lat,lng)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取品牌店铺详情
     */
    public void getBrandShopDetail(DefaultSingleObserver<HttpResult<RecommendListDto>> observer, String id) {
        Single<HttpResult<RecommendListDto>> observable = retrofitService.getBrandShopDetail(getToken(),id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 店铺详情(全部下拉列表数据)
     */
    public void getTagsList(DefaultSingleObserver<TagBean> observer) {
        Single<TagBean> observable = retrofitService.getTagsList("1")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 自动定位
     */
    public void getLocation(DefaultSingleObserver<HttpResult<AreaDto>> observer) {
        Single<HttpResult<AreaDto>> observable = retrofitService.getLocation()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * wohahaha
     */
    public void getConfigs(DefaultSingleObserver<HttpResult<ConfigDto>> observer) {
        Single<HttpResult<ConfigDto>> observable = retrofitService.getConfigs()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }


    /**
     * 实体店铺--最下面列表
     */
    public void getShopList(DefaultSingleObserver<HttpResult<List<RecommendListDto>>> observer, String type, String scopeAreaSearch, String scopeDistanceIn, String lat, String lng, String sort) {
        Single<HttpResult<List<RecommendListDto>>> observable = retrofitService.getShopList(type, scopeAreaSearch, scopeDistanceIn, lat, lng, sort)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 店铺详情--最下面列表
     */
    public void getStProductList(DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer, String mall_type,HashMap<String, String> map) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.getStProductList("default",map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 爱心工厂分类
     */
    public void getGcTypeProductList(DefaultSingleObserver<HttpResult<List<ProductDto>>> observer, String mall_type, String cateId) {
        Single<HttpResult<List<ProductDto>>> observable = retrofitService.getGcTypeProductList("default", cateId)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 商品搜索结果
     */
    public void getSearchResultProductList(DefaultSingleObserver<HttpResult<List<ProductDto>>> observer, String mall_type, String title) {
        Single<HttpResult<List<ProductDto>>> observable = retrofitService.getSearchResultProductList("default", title)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 修改购物车商品个数
     */
    public void modifyShoppingCart(DefaultSingleObserver<HttpResult<ShopCartListItemDto>> observer, String mall_type, String rowId, String qty) {
        Single<HttpResult<ShopCartListItemDto>> observable = retrofitService.modifyShoppingCart(getToken(), "default", rowId, qty)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /*********************** 以下的为学习模块 *******************/
    /**
     * 是否主播
     */
    public void isLove(DefaultSingleObserver<HttpResult<Object>> observer) {
        Single<HttpResult<Object>> observable = retrofitService.isLive(getToken())
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 查询主播信息
     */
    public void getLiveInfo(DefaultSingleObserver<HttpResult<AnchorInfo>> observer) {
        Single<HttpResult<AnchorInfo>> observable = retrofitService.getAnchorInfo(getToken())
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 申请主播
     */
    public void applyLive(DefaultSingleObserver<HttpResult<AnchorInfo>> observer, HashMap<String, String> map) {
        Single<HttpResult<AnchorInfo>> observable = retrofitService.applyAnchor(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 申请主播
     */
    public void upSellers(DefaultSingleObserver<HttpResult<Object>> observer, HashMap<String, String> map) {
        Single<HttpResult<Object>> observable = retrofitService.upSellers(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取直播分类
     */
    public void getLiveCates(DefaultSingleObserver<HttpResult<List<LiveCatesBean>>> observer, int is_recommended) {
        Single<HttpResult<List<LiveCatesBean>>> observable = retrofitService.liveCates(is_recommended)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 获取学习首页直播分类
     */
    public void getHomeLiveCates(DefaultSingleObserver<HttpResult<List<LiveCatesBean>>> observer, int is_recommended) {
        Single<HttpResult<List<LiveCatesBean>>> observable = retrofitService.liveCates(is_recommended, "videos.room")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 课件广告轮播图
     */
    public void getSlidersList(DefaultSingleObserver<HttpResult<BannerInfoDto>> observer) {
        Single<HttpResult<BannerInfoDto>> observable = retrofitService.getSlidersList("study_course_top")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 课件分类
     */
    public void categories(DefaultSingleObserver<HttpResult<List<CategorieBean>>> observer) {
        Single<HttpResult<List<CategorieBean>>> observable = retrofitService.categories()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 课件分类
     */
    public void banCategorie(DefaultSingleObserver<HttpResult<List<BannerDto>>> observer) {
        Single<HttpResult<List<BannerDto>>> observable = retrofitService.banCategorie()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void AllCategorie(DefaultSingleObserver<HttpResult<List<BannerDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<BannerDto>>> observable = retrofitService.AllCategorie(map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 课件分类视频列表
     */
    public void getCourseProducts(DefaultSingleObserver<HttpResult<List<VideoBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<VideoBean>>> observable = retrofitService.getCourseProducts(map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 开启视频直播
     */
    public void openLive(DefaultSingleObserver<HttpResult<LiveVideoInfo>> observer, HashMap<String, String> map) {
        Single<HttpResult<LiveVideoInfo>> observable = retrofitService.liveVideosList(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 课件视频详情
     */
    public void getCourseProductsDetail(DefaultSingleObserver<HttpResult<VideoBean>> observer, String id) {
        Single<HttpResult<VideoBean>> observable = retrofitService.getCourseProductsDetail(getToken(), id, "course_info")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 首页进入直播频道列表
     */
    public void liveVideos(DefaultSingleObserver<HttpResult<List<VideoLiveBean>>> observer, HashMap<String, String> map) {
        Single<HttpResult<List<VideoLiveBean>>> observable = retrofitService.liveVideos(map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 直播公告
     */
    public void getLiveConfigs(DefaultSingleObserver<HttpResult<LiveMessageInfo>> observer) {
        Single<HttpResult<LiveMessageInfo>> observable = retrofitService.getLiveConfigs()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 课件学习记录
     */
    public void learnRecords(DefaultSingleObserver<HttpResult<List<LearnRecordInfo>>> observer, Map<String, String> map) {
        Single<HttpResult<List<LearnRecordInfo>>> observable = retrofitService.learnRecords(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 我的足迹
     */
    public void userFootprints(DefaultSingleObserver<HttpResult<List<FootInfoDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<FootInfoDto>>> observable = retrofitService.userFootprints(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void orderCheckout(DefaultSingleObserver<HttpResult<List<OrderCheckoutBean>>> observer, HashMap<String, String> map) {
        Single<HttpResult<List<OrderCheckoutBean>>> observable = retrofitService.orderCheckout(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 直播详情
     *
     * @param observer
     * @param id
     */
    public void liveVideosInfo(DefaultSingleObserver<HttpResult<VideoLiveBean>> observer, String id) {
        Single<HttpResult<VideoLiveBean>> observable = retrofitService.liveVideosInfo(id, "room,user")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 礼物列表
     *
     * @param observer
     */
    public void getLiveGift(DefaultSingleObserver<HttpResult<List<GiftBean>>> observer) {
        Single<HttpResult<List<GiftBean>>> observable = retrofitService.getLiveGift()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 打赏礼物
     *
     * @param observer
     */
    public void liveReward(DefaultSingleObserver<HttpResult<AnchorInfo>> observer, HashMap<String, String> map) {
        Single<HttpResult<AnchorInfo>> observable = retrofitService.liveReward(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 打赏积分
     *
     * @param observer
     */
    public void liveRewardScore(DefaultSingleObserver<HttpResult<AnchorInfo>> observer, HashMap<String, String> map) {
        Single<HttpResult<AnchorInfo>> observable = retrofitService.liveRewardScore(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 热门搜索
     */
    public void getHotSearch(DefaultSingleObserver<HttpResult<HotSearchInfo>> observer, String include) {
        Single<HttpResult<HotSearchInfo>> observable = retrofitService.getHotSearch(include)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 积分收入
     */
    public void liveRewardWater(DefaultSingleObserver<HttpResult<List<ScoreBean>>> observer, HashMap<String, String> map) {
        Single<HttpResult<List<ScoreBean>>> observable = retrofitService.liveRewardWater(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 聊天室成员列表
     *
     * @param observer
     */
    public void liveChatter(DefaultSingleObserver<HttpResult<List<RoomUserBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<RoomUserBean>>> observable = retrofitService.liveChatter(map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 主播积分接口
     *
     * @param observer
     */
    public void walletLog(DefaultSingleObserver<HttpResult<List<ScoreIncomeBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<ScoreIncomeBean>>> observable = retrofitService.walletLog(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 停播
     *
     * @param observer
     */
    public void liveVideosClose(DefaultSingleObserver<HttpResult<AnchorInfo>> observer, Map<String, String> map) {
        Single<HttpResult<AnchorInfo>> observable = retrofitService.liveVideosClose(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 停播成功，返回数据
     *
     * @param observer
     */
    public void liveVideosCloseSuccess(DefaultSingleObserver<HttpResult<OnlineLiveFinishBean>> observer, String id) {
        Single<HttpResult<OnlineLiveFinishBean>> observable = retrofitService.liveVideosCloseSuccess(id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 在线报名列表
     *
     * @param observer
     */
    public void onlineApplyList(DefaultSingleObserver<HttpResult<List<OnlineApplyInfo>>> observer) {
        Single<HttpResult<List<OnlineApplyInfo>>> observable = retrofitService.onlineApplyList(getToken(), "user,page")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 关注
     *
     * @param observer
     */
    public void postAttention(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, String> map) {
        Single<HttpResult<Object>> observable = retrofitService.postAttention(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 取消关注
     *
     * @param observer
     */
    public void deleteAttention(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, String> map) {
        Single<HttpResult<Object>> observable = retrofitService.deleteAttention(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 预览订单
     */
    public void getOrderPreInfo(DefaultSingleObserver<HttpResult<OrderPreviewDto>> observer, String mall_type, HashMap<String,String> map) {
        Single<HttpResult<OrderPreviewDto>> observable = retrofitService.getOrderPreInfo(getToken(), mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 下单
     */
    public void checkOutOrder(DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>> observer, String mall_type, HashMap<String, String> map) {
        Single<HttpResult<List<CheckOutOrderResult>>> observable = retrofitService.checkOutOrder(getToken(), mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 下单2
     */
    public void checkOutOrder2(DefaultSingleObserver<HttpResult<List<CheckOutOrderResult>>> observer, String mall_type, HashMap<String, String> map) {
        Single<HttpResult<List<CheckOutOrderResult>>> observable = retrofitService.checkOutOrder2(getToken(), mall_type, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     *爱心头条
     */
    public void getHeadLines(DefaultSingleObserver<HttpResult<List<HeadLineDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<HeadLineDto>>> observable = retrofitService.getHeadLines(map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getConturyProduct(DefaultSingleObserver<HttpResult<List<NewListItemDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<NewListItemDto>>> observable = retrofitService.getConturyProduct(map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     *爱心头条详情
     */
    public void getHeadLineDetail(DefaultSingleObserver<HttpResult<HeadLineDetailDto>> observer, String id) {
        Single<HttpResult<HeadLineDetailDto>> observable = retrofitService.getHeadLineDetail(id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }


    /*********************** 以下的为个人中心模块 *******************/
    public void userCountStatistics(DefaultSingleObserver<HttpResult<CountStatisticsBean>> observer) {
        Single<HttpResult<CountStatisticsBean>> observable = retrofitService.userCountStatistics(getToken())
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getFollowingShops(DefaultSingleObserver<HttpResult<List<AttentionCommunityBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<AttentionCommunityBean>>> observable = retrofitService.getFollowingShops(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getUserFollowers(DefaultSingleObserver<HttpResult<List<AttentionCommunityBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<AttentionCommunityBean>>> observable = retrofitService.getUserFollowers(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getBankCardList(DefaultSingleObserver<HttpResult<List<BankCardDto>>> observer, int page) {
        Single<HttpResult<List<BankCardDto>>> observable = retrofitService.getBankCardList(getToken(), page)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void addBankCard(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, String> map) {
        Single<HttpResult<Object>> observable = retrofitService.addBankCard(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getBankCardDefault(DefaultSingleObserver<HttpResult<BankCardDto>> observer) {
        Single<HttpResult<BankCardDto>> observable = retrofitService.getBankCardDefault(getToken())
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void putBankCardDefault(DefaultSingleObserver<HttpResult<Object>> observer, String card, Map<String, String> map) {
        Single<HttpResult<Object>> observable = retrofitService.putBankCardDefault(getToken(), card, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void deleteBankCard(DefaultSingleObserver<HttpResult<Object>> observer, String card) {
        Single<HttpResult<Object>> observable = retrofitService.deleteBankCard(getToken(), card)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 我买到的
     *
     * @param observer
     * @param map
     */
    public void getAllUserOrders(DefaultSingleObserver<HttpResult<List<MyOrderDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<MyOrderDto>>> observable = retrofitService.getAllUserOrders(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getSellerOrders(DefaultSingleObserver<HttpResult<List<MyOrderDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<MyOrderDto>>> observable = retrofitService.getSellerOrders(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getSellerProducts(DefaultSingleObserver<HttpResult<List<PublishInfo>>> observer, Map<String, String> map) {
        Single<HttpResult<List<PublishInfo>>> observable = retrofitService.getSellerProducts(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getUserChildren(DefaultSingleObserver<HttpResult<List<InviteFriendBean>>> observer) {
        Single<HttpResult<List<InviteFriendBean>>> observable = retrofitService.getUserChildren(getToken())
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getAllUserRefundList(DefaultSingleObserver<HttpResult<List<MyOrderDto>>> observer, Map<String, String> map) {
        Single<HttpResult<List<MyOrderDto>>> observable = retrofitService.getAllUserRefundList(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void getAllUserRefundDetail(DefaultSingleObserver<HttpResult<MyOrderDto>> observer, String id, Map<String, String> map) {
        Single<HttpResult<MyOrderDto>> observable = retrofitService.getAllUserRefundDetail(getToken(), id,map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void getAllUserOrdersCount(DefaultSingleObserver<HttpResult<CountOrderBean>> observer) {
        Single<HttpResult<CountOrderBean>> observable = retrofitService.getAllUserOrdersCount(getToken(),"gc,lm,st")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getAlOrdersRefundCount(DefaultSingleObserver<HttpResult<String>> observer) {
        Single<HttpResult<String>> observable = retrofitService.getAlOrdersRefundCount(getToken())
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //充值接口
    public void moneyRecharges(DefaultSingleObserver<HttpResult<String>> observer, Map<String, String> map) {
        Single<HttpResult<String>> observable = retrofitService.moneyRecharges(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //取消订单
    public void cancelOrder(DefaultSingleObserver<HttpResult<Object>> observer, String orderId) {
        Single<HttpResult<Object>> observable = retrofitService.cancelOrder(getToken(), orderId)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //催发货
    public void hurryOrder(DefaultSingleObserver<HttpResult<Object>> observer, String orderId) {
        Single<HttpResult<Object>> observable = retrofitService.hurryOrder(getToken(), orderId)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //退款原因
    public void refundReasons(DefaultSingleObserver<HttpResult<List<String>>> observer) {
        Single<HttpResult<List<String>>> observable = retrofitService.refundReasons(getToken())
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //退款
    public void refundOrder(DefaultSingleObserver<Object> observer, String orderId,Map<String, Object> map,String type) {
        Single<Object> observable = retrofitService.refundOrder(getToken(), orderId,map,type)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //评价列表
    public void commentList(DefaultSingleObserver<HttpResult<MyOrderDto>> observer, String orderId, Map<String, String> map) {
        Single<HttpResult<MyOrderDto>> observable = retrofitService.commentList(getToken(), orderId, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //去评价
    public void toComment(DefaultSingleObserver<Object> observer, Map<String, Object> map) {
        Single<Object> observable = retrofitService.toComment(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //评价完成，推荐列表
    public void getProductsRandom(DefaultSingleObserver<HttpResult<List<ProductBean>>> observer, Map<String, String> map) {
        Single<HttpResult<List<ProductBean>>> observable = retrofitService.getProductsRandom(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //物流信息
    public void getLogisticsList(DefaultSingleObserver<HttpResult<MyOrderLogisticsDto>> observer, String orderId, Map<String, String> map) {
        Single<HttpResult<MyOrderLogisticsDto>> observable = retrofitService.getLogisticsList(getToken(), orderId, map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    //确定收货
    public void confirmOrder(DefaultSingleObserver<HttpResult<Object>> observer, String orderId) {
        Single<HttpResult<Object>> observable = retrofitService.confirmOrder(getToken(), orderId)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void expressList(DefaultSingleObserver<HttpResult<List<ExpressList>>> observer) {
        Single<HttpResult<List<ExpressList>>> observable = retrofitService.expressList()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void toShip(DefaultSingleObserver<Object> observer, String id,Map<String, Object> map) {
        Single<Object> observable = retrofitService.toShip(getToken(), id,map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getUserCount(DefaultSingleObserver<HttpResult<String>> observer) {
        Single<HttpResult<String>> observable = retrofitService.getUserCount(getToken())
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getUserCoupons(DefaultSingleObserver<HttpResult<List<CouponDto>>> observer,Map<String, Object> map) {
        Single<HttpResult<List<CouponDto>>> observable = retrofitService.getUserCoupons(getToken(),map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void getPreviewCoupons(DefaultSingleObserver<HttpResult<List<CouponDto>>> observer,String mallType,Map map) {
        Single<HttpResult<List<CouponDto>>> observable = retrofitService.getPreviewCoupons(getToken(),mallType,map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void clearFootPrint(DefaultSingleObserver<Object> observer,Map<String, Object> map) {
        Single<Object> observable = retrofitService.clearFootPrint(getToken(),map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    public void withdraw(DefaultSingleObserver<Object> observer,Map<String, Object> map) {
        Single<Object> observable = retrofitService.withdraw(getToken(),map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void editProducts(DefaultSingleObserver<Object> observer,String id,Map<String, Object> map) {
        Single<Object> observable = retrofitService.editProducts(getToken(),id,map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void serviceMenu(DefaultSingleObserver<HttpResult<List<ServiceMenuBean>>> observer) {
        Single<HttpResult<List<ServiceMenuBean>>> observable = retrofitService.serviceMenu()
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    public void getOrderDetail(DefaultSingleObserver<HttpResult<MyOrderDto>> observer,String id,String type,String include) {
        Single<HttpResult<MyOrderDto>> observable = retrofitService.getOrderDetail(getToken(),id,type,include)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 添加收藏
     */
    public void addProductFavorites(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, Object> map) {
        Single<HttpResult<Object>> observable = retrofitService.addProductFavorites(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 取消收藏
     */
    public void cancleProductFavorites(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, Object> map) {
        Single<HttpResult<Object>> observable = retrofitService.cancleProductFavorites(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 提交评价
     */
    public void articleComment(DefaultSingleObserver<HttpResult<Object>> observer, BaseRequestModel baseRequestModel) {
        Single<HttpResult<Object>> observable = retrofitService.articleComment(getToken(), getRequestBody(baseRequestModel))
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     *领取红包
     */
    public void getRedPacket(DefaultSingleObserver<HttpResult<RedPacketDto>> observer, String cash_coupon_user_id) {
        Single<HttpResult<RedPacketDto>> observable = retrofitService.getRedPacket(getToken(), cash_coupon_user_id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     *发送红包
     */
    public void sendRedPacket(DefaultSingleObserver<HttpResult<RedPacketDto>> observer, String pay_password, String total, String ar_user_id, String say) {
        Single<HttpResult<RedPacketDto>> observable = retrofitService.sendRedPacket(getToken(), pay_password, "score", total, ar_user_id, say, "false")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 红包详情
     */
    public void getPacketInfo(DefaultSingleObserver<HttpResult<RedPacketInfoDto>> observer, String cash_coupon_id) {
        Single<HttpResult<RedPacketInfoDto>> observable = retrofitService.getPacketInfo(getToken(), cash_coupon_id, "cash_coupon_users.user,user")
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 店铺优惠券
     * @param observer
     * @param map
     */
    public void getShopCoupons(DefaultSingleObserver<HttpResult<List<CouponBean>>> observer,Map<String, Object> map) {
        Single<HttpResult<List<CouponBean>>> observable = retrofitService.getShopCoupons(getToken(),map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
    /**
     * 领取店铺优惠券
     * @param observer
     * @param id
     */
    public void postShopCoupons(DefaultSingleObserver<HttpResult<Object>> observer,String id) {
        Single<HttpResult<Object>> observable = retrofitService.postShopCoupons(getToken(),id)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 点赞
     */
    public void addPraise(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, Object> map) {
        Single<HttpResult<Object>> observable = retrofitService.addPraise(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }

    /**
     * 取消点赞
     */
    public void canclePraise(DefaultSingleObserver<HttpResult<Object>> observer, Map<String, Object> map) {
        Single<HttpResult<Object>> observable = retrofitService.canclePraise(getToken(), map)
                .map(new HttpResultMapper.HttpResultOtheData<>(null));
        subscribe(observable, observer);
    }
}
