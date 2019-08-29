package com.hqmy.market.db;

import com.hqmy.market.db.bean.SearchHistory;
import com.hqmy.market.db.greendao.gen.SearchHistoryDao;

import java.util.List;


/**
 * desc:
 * last modified time:2018/8/21 17:20
 *
 * @author dahai.zhou
 * @since 2018/8/21
 */
public class SeachHistotyDBUtil {
    private static final byte[] slnstanceLock = new byte[0];
    private static SeachHistotyDBUtil mInstance;

    private SearchHistoryDao searchHistoryDao ;
    private SeachHistotyDBUtil(){
        searchHistoryDao = DaoManager.getInstance().getDaoSession().getSearchHistoryDao();
    }
    public static SeachHistotyDBUtil getInstance(){
        if(mInstance == null){
            synchronized (slnstanceLock) {
                mInstance = new SeachHistotyDBUtil();
            }
        }
        return mInstance;
    }

    public List<SearchHistory> loadAll(){
        return searchHistoryDao.loadAll();
    }

    public void deleteAll(){
        searchHistoryDao.deleteAll();
    }

    public void delete(SearchHistory searchHistory){
        searchHistoryDao.delete(searchHistory);
    }

    public void save(SearchHistory searchHistory) {
        searchHistoryDao.save(searchHistory);
    }

    public SearchHistory query(String key){
        return searchHistoryDao.queryBuilder().where(SearchHistoryDao.Properties.Name.eq(key)).build().unique();
    }
}
