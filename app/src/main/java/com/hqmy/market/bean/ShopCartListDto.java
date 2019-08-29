package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/18.
 */
public class ShopCartListDto implements Serializable {
     private List<ShopCartListItemDto> products;
     private String count;
     private String shop_name;
     private String shop_id;
     private boolean isSelect; //是否选中

     public List<ShopCartListItemDto> getProducts() {
          return products;
     }

     public void setProducts(List<ShopCartListItemDto> products) {
          this.products = products;
     }

     public String getCount() {
          return count;
     }

     public void setCount(String count) {
          this.count = count;
     }

     public String getShop_name() {
          return shop_name;
     }

     public void setShop_name(String shop_name) {
          this.shop_name = shop_name;
     }

     public boolean isSelect() {
          return isSelect;
     }

     public void setSelect(boolean select) {
          isSelect = select;
     }

     public String getShop_id() {
          return shop_id;
     }

     public void setShop_id(String shop_id) {
          this.shop_id = shop_id;
     }
}
