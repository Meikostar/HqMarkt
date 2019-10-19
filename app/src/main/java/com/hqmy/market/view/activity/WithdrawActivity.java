package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.BalanceDto;
import com.hqmy.market.bean.BankCardDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;
import com.hqmy.market.http.response.HttpResult;
import com.hqmy.market.utils.TextUtil;
import com.hqmy.market.view.SettingPasswordActivity;
import com.hqmy.market.view.widgets.dialog.InputPasswordDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现
 */
public class WithdrawActivity extends BaseActivity {
    public static final int CHECK_BANK_CARD = 100;

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.et_withdraw_money)
    EditText et_withdraw_money;
    @BindView(R.id.add_band_card)
    View add_band_card;
    @BindView(R.id.rl_withdraw_bank_card_container)
    View rl_withdraw_bank_card_container;
    @BindView(R.id.iv_withdraw_bank_icon)
    ImageView iv_withdraw_bank_icon;
    @BindView(R.id.iv_withdraw_bank_card)
    TextView iv_withdraw_bank_card;
    @BindView(R.id.iv_withdraw_bank_num)
    TextView iv_withdraw_bank_num;


    double money = 0;
    BankCardDto bankCardDto;

    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @Override
    public void initView() {
        mTitleText.setText("提现");
    }

    @Override
    public void initData() {
        getDefaultCard();
    }

    @Override
    public void initListener() {

    }

    @OnClick({R.id.iv_title_back
            , R.id.rl_withdraw_bank_card_container
            , R.id.tv_withdraw_all
            , R.id.add_band_card
            , R.id.tv_withdraw_affirm
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.rl_withdraw_bank_card_container://选择银行卡
                checkBankCard(1);
                break;
            case R.id.add_band_card:
                checkBankCard(0);
                break;
            case R.id.tv_withdraw_all://全部提现
                et_withdraw_money.setText(money + "");
                break;
            case R.id.tv_withdraw_affirm://确认提现
                if (TextUtils.isEmpty(et_withdraw_money.getText().toString().trim())) {
                    ToastUtil.showToast("请输入提现金额");
                    return;
                }
                if (bankCardDto == null ) {
                    ToastUtil.showToast("请添加银行卡");
                    return;
                }
                checkWallet(et_withdraw_money.getText().toString());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
        getDefaultCard();
    }
    private void checkWallet(String string) {
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                if (balanceDto != null && balanceDto.isPay_password()) {

                    new InputPasswordDialog(WithdrawActivity.this, new InputPasswordDialog.InputPasswordListener() {
                        @Override
                        public void callbackPassword(String password) {
                            //提现
                            Map map = new HashMap();
                            map.put("card_id",bankCardDto.getId());
                            map.put("money",string);
                            map.put("pay_password",password);
                            withdraw(map);
                        }
                    }).show();
                } else {
                    gotoActivity(SettingPasswordActivity.class);
                    ToastUtil.toast("请先设置支付密码");
                }

            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }
    private void withdraw(Map map){
        DataManager.getInstance().withdraw(new DefaultSingleObserver<Object>() {
            @Override
            public void onSuccess(Object balanceDto) {
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                if(ApiException.getInstance().isSuccess()){
                    finish();
                }else {
                    ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
                }
            }
        },map);
    }

    private void getData() {
        DataManager.getInstance().getBalance(new DefaultSingleObserver<BalanceDto>() {
            @Override
            public void onSuccess(BalanceDto balanceDto) {
                super.onSuccess(balanceDto);
                money = balanceDto.getMoney();
                tv_money.setText("可提现金额：" + money + "元");
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);

            }
        });
    }

    private void getDefaultCard() {
        DataManager.getInstance().getBankCardDefault(new DefaultSingleObserver<HttpResult<BankCardDto>>() {
            @Override
            public void onSuccess(HttpResult<BankCardDto> result) {
                bankCardDto = result.getData();
                setCardView();
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        });
    }

    private void setCardView() {
        if (bankCardDto != null&&TextUtil.isNotEmpty(bankCardDto.getOrg())) {
            add_band_card.setVisibility(View.GONE);
            rl_withdraw_bank_card_container.setVisibility(View.VISIBLE);
            String typeName = "储蓄卡";
            if ("CC".equals(bankCardDto.getType())) {
                typeName = "信用卡";
            }
            iv_withdraw_bank_card.setText(bankCardDto.getOrg() + "(" + typeName + ")");

            String cardNumber = bankCardDto.getNumber();
            if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() > 5) {
                int cardLength = cardNumber.length();
                cardNumber = cardNumber.substring(cardLength - 4, cardLength);
                cardNumber = "**** **** **** " + cardNumber;
            }
            iv_withdraw_bank_num.setText(cardNumber);
            String bankLogo = Constants.WEB_IMG_URL_CID + bankCardDto.getBank_logo();
            GlideUtils.getInstances().loadNormalImg(this, iv_withdraw_bank_icon, bankLogo, R.mipmap.bank_card_icon_normal);

        } else {
            rl_withdraw_bank_card_container.setVisibility(View.GONE);
            add_band_card.setVisibility(View.VISIBLE);
        }
    }

    private void checkBankCard(int type) {
        if(type==0){
            Intent intent = new Intent(WithdrawActivity.this, AddBandCardActivity.class);
            intent.putExtra("type",1);
            startActivity(intent);
//            gotoActivity(AddBandCardActivity.class);
        }else {
            Intent intent = new Intent(this, BankCardManagerActivity.class);
            intent.putExtra("selCard", true);
            startActivityForResult(intent, CHECK_BANK_CARD);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHECK_BANK_CARD && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                bankCardDto = (BankCardDto) data.getSerializableExtra("cardItem");
                setCardView();
            }
        }
    }
}
