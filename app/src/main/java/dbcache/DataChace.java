package dbcache;

/**
 * Created by math on 2017/1/23.
 * 数据缓存类
 */

public class DataChace {
    public  DataAccess dataAccess;
    public int  cacheTime;
    public DataValid dataValid;
    public DataChace(DataAccess dataAccess,int cacheTime,DataValid dataValid){
        this.cacheTime=cacheTime;
        this.dataAccess=dataAccess;
        this.dataValid=dataValid;
    }
}
