package com.hqmy.market.bean;

import java.io.Serializable;
/**
 * 商品详情 属性
 * Created by rzb on 2019/6/18.
 */
public class CommodityDetailAttrItemDto implements Serializable {
     private String id;
     private String mall_product_id;
     private String image;
     private String market_price;
     private String price;
     private String score;
     private String stock;
     private String sales_count;
     private String ext;
     private String attr_str;

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public String getMall_product_id() {
          return mall_product_id;
     }

     public void setMall_product_id(String mall_product_id) {
          this.mall_product_id = mall_product_id;
     }

     public String getImage() {
          return image;
     }

     public void setImage(String image) {
          this.image = image;
     }

     public String getMarket_price() {
          return market_price;
     }

     public void setMarket_price(String market_price) {
          this.market_price = market_price;
     }

     public String getPrice() {
          return price;
     }

     public void setPrice(String price) {
          this.price = price;
     }

     public String getScore() {
          return score;
     }

     public void setScore(String score) {
          this.score = score;
     }

     public String getStock() {
          return stock;
     }

     public void setStock(String stock) {
          this.stock = stock;
     }

     public String getSales_count() {
          return sales_count;
     }

     public void setSales_count(String sales_count) {
          this.sales_count = sales_count;
     }

     public String getExt() {
          return ext;
     }

     public void setExt(String ext) {
          this.ext = ext;
     }

     public String getAttr_str() {
          return attr_str;
     }

     public void setAttr_str(String attr_str) {
          this.attr_str = attr_str;
     }
}
