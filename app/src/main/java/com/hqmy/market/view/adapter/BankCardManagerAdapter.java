package com.hqmy.market.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hqmy.market.R;
import com.hqmy.market.bean.BankCardDto;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.GlideUtils;

public class BankCardManagerAdapter extends BaseQuickAdapter<BankCardDto, BaseViewHolder> {
    private Context mContext;

    public BankCardManagerAdapter(Context mContext) {
        super(R.layout.item_wallet_card_layout);
        this.mContext = mContext;
    }


    @Override
    protected void convert(BaseViewHolder helper, BankCardDto item) {

        String typeName = "储蓄卡";
        if ("CC".equals(item.getType())) {
            typeName = "信用卡";
        }
        String org = item.getOrg();
        String cardNumber = item.getNumber();
        if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() > 5) {
            int cardLength = cardNumber.length();
            cardNumber = cardNumber.substring(cardLength - 4, cardLength);
            cardNumber = "**** **** **** " + cardNumber;
        }
        helper.setText(R.id.tv_card_name, org)
                .setText(R.id.tv_card_type, typeName)
                .setText(R.id.tv_card_number, cardNumber);
        String bankLogo = Constants.WEB_IMG_URL_CID + item.getBank_logo();
        GlideUtils.getInstances().loadNormalImg(mContext, helper.getView(R.id.img_card_logo), bankLogo, R.mipmap.bank_card_icon_normal);
        ImageView imageView = helper.getView(R.id.img_card_bg);
//        if (!TextUtils.isEmpty(org)) {
//            if (org.indexOf("建设") > 0) {
//                imageView.setImageResource(R.mipmap.icon_bank_js);
//            } else if (org.indexOf("农业") > 0) {
//                imageView.setImageResource(R.mipmap.icon_bank_ny);
//            } else if (org.indexOf("招商") > 0) {
//                imageView.setImageResource(R.mipmap.icon_bank_zg);
//            } else {
//                imageView.setImageResource(R.mipmap.icon_bank_default);
//            }
//        } else {
//            imageView.setImageResource(R.mipmap.icon_bank_default);
//        }
        helper.addOnClickListener(R.id.card_content_del);
        helper.addOnClickListener(R.id.card_content);
    }
}
