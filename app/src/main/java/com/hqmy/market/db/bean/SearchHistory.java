package com.hqmy.market.db.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * desc:
 * last modified time:2018/6/30 17:05
 *
 * @author dahai.zhou
 * @since 2018/6/30
 */
@Entity(nameInDb = "SearchHistory")
public class SearchHistory {
    /*数据库字段*/
    @Id(autoincrement = true)
    private Long id;
    private String name;

    @Generated(hash = 822577210)
    public SearchHistory(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public SearchHistory(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SearchHistory{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void setId(Long id) {
        this.id = id;
    }
}
