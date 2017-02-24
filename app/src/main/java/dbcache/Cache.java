package dbcache;

/**
 * 缓存对象类
 * 
 * @author xiangming
 * 
 */
public class Cache {
	/**
	 * 缓存的key
	 */
	public String key;
	/**
	 * 缓存的值
	 */
	public String value;
	/**
	 * 缓存更新的时间
	 */
	public long updatetime;

	/**
	 * 
	 * 构造函数
	 * 
	 */
	public Cache() {

	}

	/**
	 * 
	 * 构造函数：更新时间默认为当前时间
	 * 
	 * @param key
	 *            关键字
	 * @param value
	 *            值
	 */
	public Cache(String key, String value) {
		this(key, value, System.currentTimeMillis());
	}

	/**
	 * 
	 * 构造函数
	 * 
	 * @param key
	 *            关键字
	 * @param value
	 *            值
	 * @param updatetime
	 *            更新时间
	 */
	public Cache(String key, String value, long updatetime) {
		this.key = key;
		this.value = value;
		this.updatetime = updatetime;
	}
}
