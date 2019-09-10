package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rzb on 2019/6/18.
 */
public class ShopCartInfoDto implements Serializable {
     public List<ShopCartListDto> shops;
     private String                total;
     private String                score_total;
     private String                count;

     public List<ShopCartListDto> getShops() {
          return shops;
     }

     public void setShops(List<ShopCartListDto> shops) {
          this.shops = shops;
     }

     public String getTotal() {
          return total;
     }

     public void setTotal(String total) {
          this.total = total;
     }

     public String getScore_total() {
          return score_total;
     }

     public void setScore_total(String score_total) {
          this.score_total = score_total;
     }

     public String getCount() {
          return count;
     }

     public void setCount(String count) {
          this.count = count;
     }
}
