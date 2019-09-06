package com.hqmy.market.bean;

import java.io.Serializable;
import java.util.List;
/**
 * Created by rzb on 2019/6/28.
 */
public class BannerInfoDto implements Serializable {
      private List<BannerItemDto>  union_mall_top; //联盟商城顶部
      private List<BannerItemDto>  loving_family_top; //爱心家庭顶部
      private List<BannerItemDto>  index_top; //消费首页顶部
      private List<BannerItemDto>  index_middle;//消费首页中部
      private List<BannerItemDto>  love_factory_top;//爱心厂家顶部
      private List<BannerItemDto>  physical_store_top;//实体店铺顶部
      private List<BannerItemDto>  study_course_top;//课件顶部
      private List<BannerItemDto>  love_headlines_top;//爱心头条顶部
      private List<BannerItemDto>  love_factory_categories_top;
      private List<BannerItemDto>  news_detail_middle;//新闻详情中部
      public List<BannerItemDto>  country_list_below_country;//新闻详情中部
      public List<BannerItemDto>  country_list_top;//新闻详情中部



      public List<BannerItemDto> getUnion_mall_top() {
            return union_mall_top;
      }

      public void setUnion_mall_top(List<BannerItemDto> union_mall_top) {
            this.union_mall_top = union_mall_top;
      }

      public List<BannerItemDto> getLoving_family_top() {
            return loving_family_top;
      }

      public void setLoving_family_top(List<BannerItemDto> loving_family_top) {
            this.loving_family_top = loving_family_top;
      }

      public List<BannerItemDto> getIndex_top() {
            return index_top;
      }

      public void setIndex_top(List<BannerItemDto> index_top) {
            this.index_top = index_top;
      }

      public List<BannerItemDto> getIndex_middle() {
            return index_middle;
      }

      public void setIndex_middle(List<BannerItemDto> index_middle) {
            this.index_middle = index_middle;
      }

      public List<BannerItemDto> getLove_factory_top() {
            return love_factory_top;
      }

      public void setLove_factory_top(List<BannerItemDto> love_factory_top) {
            this.love_factory_top = love_factory_top;
      }

      public List<BannerItemDto> getPhysical_store_top() {
            return physical_store_top;
      }

      public void setPhysical_store_top(List<BannerItemDto> physical_store_top) {
            this.physical_store_top = physical_store_top;
      }

      public List<BannerItemDto> getStudy_course_top() {
            return study_course_top;
      }

      public void setStudy_course_top(List<BannerItemDto> study_course_top) {
            this.study_course_top = study_course_top;
      }


      public List<BannerItemDto> getLove_headlines_top() {
            return love_headlines_top;
      }

      public void setLove_headlines_top(List<BannerItemDto> love_headlines_top) {
            this.love_headlines_top = love_headlines_top;
      }

      public List<BannerItemDto> getLove_factory_categories_top() {
            return love_factory_categories_top;
      }

      public void setLove_factory_categories_top(List<BannerItemDto> love_factory_categories_top) {
            this.love_factory_categories_top = love_factory_categories_top;
      }

      public List<BannerItemDto> getNews_detail_middle() {
            return news_detail_middle;
      }

      public void setNews_detail_middle(List<BannerItemDto> news_detail_middle) {
            this.news_detail_middle = news_detail_middle;
      }
}

