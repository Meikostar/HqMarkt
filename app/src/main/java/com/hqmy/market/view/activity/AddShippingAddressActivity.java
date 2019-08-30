package com.hqmy.market.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hqmy.market.R;
import com.hqmy.market.base.BaseActivity;
import com.hqmy.market.bean.AddressDto;
import com.hqmy.market.bean.AddressModel;
import com.hqmy.market.common.Constants;
import com.hqmy.market.common.utils.ToastUtil;
import com.hqmy.market.http.DefaultSingleObserver;
import com.hqmy.market.http.error.ApiException;
import com.hqmy.market.http.manager.DataManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新增收货地址
 */
public class AddShippingAddressActivity extends BaseActivity {

    public static String areaName = "";
    public static String areaId = "";

    @BindView(R.id.tv_title_text)
    TextView mTitleText;
    @BindView(R.id.tv_where_address)
    TextView mAddress;
    @BindView(R.id.et_name)
    EditText mName;
    @BindView(R.id.et_phone)
    EditText mPhone;
    @BindView(R.id.et_detail_address)
    EditText mDetailAddress;
    @BindView(R.id.cb_default_address)
    CheckBox mCheckBox;
    private AddressDto mAddressDto;
    private String mAddressId;
    private boolean  isEdit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_shipping_address;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAddressDto = (AddressDto) bundle.getSerializable("address");
            if(mAddressDto!=null){
                mTitleText.setText("修改收货地址");
                isEdit  =true;
                mAddressId = mAddressDto.getId()+"";
                mName.setText(mAddressDto.getName());
                mPhone.setText(mAddressDto.getMobile());
                mAddress.setText(mAddressDto.getArea());
                mDetailAddress.setText(mAddressDto.getDetail());
                mCheckBox.setChecked("1".equals(mAddressDto.getIs_default()));

                if (!TextUtils.isEmpty(mAddressDto.getArea_ids()) && mAddressDto.getArea_ids().indexOf(",") > 0) {
                    String areaIdArray[] = mAddressDto.getArea_ids().split(",");
                    if (areaIdArray != null && areaIdArray.length > 0) {
                        int position = areaIdArray.length - 1;
                        areaId = areaIdArray[position];
                    }
                }
            }
        } else {
            mTitleText.setText("新增收货地址");
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {


    }

    @OnClick({  R.id.iv_title_back
            , R.id.rl_address_area_container
            , R.id.tv_confirm_address
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_title_back:
                finish();
                break;
            case R.id.rl_address_area_container:
                gotoActivity(AreaListActivity.class, false, null, Constants.INTENT_REQUESTCODE_AREA);
                break;
            case R.id.tv_confirm_address:
                confirm();
                break;
        }
    }

    private void confirm() {
        String name = mName.getText().toString();
        String mobile = mPhone.getText().toString();
        String detail = mDetailAddress.getText().toString();
        String area = mAddress.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入收货人的姓名", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
            return;
        }
//        if (mobile.length()!=11) {
//            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_LONG).show();
//            return;
//        }
        if (TextUtils.isEmpty(area)) {
            Toast.makeText(this, "请选择省区", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(detail)) {
            Toast.makeText(this, "请输入地区", Toast.LENGTH_LONG).show();
            return;
        }

        AddressModel addressModel = new AddressModel();
        addressModel.setName(name);
        addressModel.setMobile(mobile);
        addressModel.setArea_id(areaId);
        addressModel.setDetail(detail);
        if (mCheckBox.isChecked()) {
            addressModel.setIs_default("1");
        } else {
            addressModel.setIs_default("0");
        }
        if(isEdit){
            updateAddress(addressModel);
        }else {
            addAddress(addressModel);
        }
    }

    private void addAddress(AddressModel addressModel) {
        showLoadDialog();
        DataManager.getInstance().addAddressesList(new DefaultSingleObserver<AddressDto>() {
            @Override
            public void onSuccess(AddressDto addressDtos) {
                dissLoadDialog();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, addressModel);
    }

    private void updateAddress(AddressModel addressModel) {
        showLoadDialog();
        DataManager.getInstance().updateAddresses(new DefaultSingleObserver<AddressDto>() {
            @Override
            public void onSuccess(AddressDto addressDtos) {
                dissLoadDialog();
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                dissLoadDialog();
                ToastUtil.showToast(ApiException.getHttpExceptionMessage(throwable));
            }
        }, addressModel,mAddressId);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_REQUESTCODE_AREA && resultCode == Activity.RESULT_OK) {
            String allName = data.getStringExtra("areaName");
            areaId = data.getStringExtra("areaId");
            mAddress.setText(allName);
        }
    }
}
