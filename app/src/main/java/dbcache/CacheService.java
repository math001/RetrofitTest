package dbcache;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 缓存数据服务
 * 
 * @author chenxiangming
 * 
 */
public class CacheService {

	/**
	 * 缓存的表的名称
	 */
	private final String TABLE = "T_CACHE";

	/**
	 * 字段 key
	 */
	private final String F_KEY = "F_KEY";

	/**
	 * 字段 value
	 */
	private static final String F_VALUE = "F_VALUE";

	/**
	 * 字段更新时间
	 */
	private final String F_UPDATETIME = "F_UPDATETIME";

	/**
	 * 字段tag, 1表示数据库记录数过大时会删除该记录
	 */
	private final String F_TAG = "F_TAG";

	/**
	 * SQL: 根据key查询内容
	 */
	private final String QUERY_SQL = "SELECT * FROM " + TABLE + " WHERE " + F_KEY + "=?";

	/**
	 * 获取表的记录数
	 */
	private final String QUERY_COUNT_SQL = "SELECT COUNT(*) FROM " + TABLE;

	/**
	 * SQL: 根据key查询个数
	 */
	private final String QUERY_COUNT_BY_KEY_SQL = "SELECT COUNT(*) FROM " + TABLE + " WHERE " + F_KEY + "=?";

	/**
	 * SQL：删除所有内容
	 */
	private final String DELETE_SQL = "DELETE FROM " + TABLE;

	/**
	 * SQL：删除部分数据
	 */
	private final String DELETE_SOMETHING_DATA_SQL = "DELETE FROM " + TABLE + " WHERE " + F_TAG + "=?";

	/**
	 * SQL: 删除数据
	 */
	private final String DELETE_DATA_BY_KEY_SQL = "DELETE FROM " + TABLE + " WHERE " + F_KEY + "=?";

	/**
	 * SQL: 插入内容
	 */
	private final String INSERT_SQL = "INSERT INTO " + TABLE + "(" + F_KEY + ", " + F_VALUE + ", " + F_UPDATETIME + ", " + F_TAG + ") VALUES (?,?,?,?)";

	/**
	 * SQL：更新内容
	 */
	private final String UPDATE_SQL = "UPDATE " + TABLE + " SET " + F_VALUE + "=?, " + F_UPDATETIME + "=? WHERE " + F_KEY + "=?";

	/**
	 * 数据库帮助类
	 */
	private CacheDbHelper mDbHelper;

	/**
	 * 
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 */
	public CacheService(Context context) {
		this.mDbHelper = new CacheDbHelper(context);
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * 验证缓存是否有效
	 * 
	 * @param cache
	 *            缓存对象
	 * @param minutes
	 *            时间(分钟)
	 * @return
	 */
	public boolean valid(Cache cache, int minutes) {
		if (cache != null) {
			long mIntervalTime = System.currentTimeMillis() - cache.updatetime;
			if (mIntervalTime > 0 && mIntervalTime < getMillis(minutes)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取数据库缓存
	 * 
	 * @param key
	 *            关键字
	 * @return
	 */
	public Cache getCache(String key) {
		Cache result = null;
		Cursor cursor = null;
		SQLiteDatabase readDB = null;
		try {
			readDB = mDbHelper.getReadableDatabase();
			cursor = readDB.rawQuery(QUERY_SQL, new String[] { key });
			if (cursor.moveToFirst()) {
				result = new Cache(key, cursor.getString(cursor.getColumnIndex(F_VALUE)), cursor.getLong(cursor.getColumnIndex(F_UPDATETIME)));
			}
		} catch (Exception e) {
		//	ServiceLog.e(e);
		} finally {
			closeCursor(cursor);
			closeSQLiteDatabase(readDB);
		}
		return result;
	}

	/**
	 * 保存缓存
	 * 
	 * @param cache
	 *            缓存对象
	 * @param tag
	 *            标志:0(不可删除)、1(可被删除)
	 */
	public void save(Cache cache, String tag) {
		long count = getCount(cache.key);
		if (count > 1) {
			clearByKey(cache.key);
		} else {
			if (count == 1) {
				SQLiteDatabase writeDB = null;
				try {
					writeDB = mDbHelper.getWritableDatabase();
					writeDB.execSQL(UPDATE_SQL, new Object[] { cache.value, cache.updatetime, cache.key });
				} catch (Exception e) {
				//	ServiceLog.e(e);
				} finally {
					closeSQLiteDatabase(writeDB);
				}
			} else {
				SQLiteDatabase writeDB = null;
				try {
					writeDB = mDbHelper.getWritableDatabase();
					writeDB.execSQL(INSERT_SQL, new Object[] { cache.key, cache.value, cache.updatetime, tag });
				} catch (Exception e) {
				//	ServiceLog.e(e);
				} finally {
					closeSQLiteDatabase(writeDB);
				}
			}
		}
	}

	/**
	 * 清除所有缓存
	 */
	public void clear() {
		SQLiteDatabase writeDB = null;
		try {
			writeDB = mDbHelper.getWritableDatabase();
			writeDB.execSQL(DELETE_SQL, new Object[] {});
		} catch (Exception e) {
		//	ServiceLog.e(e);
		} finally {
			closeSQLiteDatabase(writeDB);
		}
	}

	/**
	 * 清除部分数据
	 */
	public void clearSomethingData() {
		long count = getCount();
		//ServiceLog.d("CacheService.clearSomethingData()->getCount=" + count);
		if (count > 1000) {
			SQLiteDatabase writeDB = null;
			try {
				writeDB = mDbHelper.getWritableDatabase();
				writeDB.execSQL(DELETE_SOMETHING_DATA_SQL, new Object[] { "1" });
			//	ServiceLog.d("has clear data where ftag = 1->");
			} catch (Exception e) {
				//ServiceLog.e(e);
			} finally {
				closeSQLiteDatabase(writeDB);
			}
		}
	}

	/**
	 * 清除数据
	 * 
	 * @param key
	 */
	private void clearByKey(String key) {
		SQLiteDatabase writeDB = null;
		try {
			writeDB = mDbHelper.getWritableDatabase();
			writeDB.execSQL(DELETE_DATA_BY_KEY_SQL, new Object[] { key });
		} catch (Exception e) {
			//ServiceLog.e(e);
		} finally {
			closeSQLiteDatabase(writeDB);
		}
	}

	/**
	 * 获取指定关键字的缓存数
	 * 
	 * @param key
	 *            关键字
	 * @return
	 */
	private long getCount(String key) {
		long result = 0;
		Cursor cursor = null;
		SQLiteDatabase readDB = null;
		try {
			readDB = mDbHelper.getReadableDatabase();
			cursor = readDB.rawQuery(QUERY_COUNT_BY_KEY_SQL, new String[] { key });
			cursor.moveToFirst();
			result = cursor.getLong(0);
		} catch (Exception e) {
			//ServiceLog.e(e);
		} finally {
			closeCursor(cursor);
			closeSQLiteDatabase(readDB);
		}
		return result;
	}

	/**
	 * 获取表的记录数
	 * 
	 * @return
	 */
	private long getCount() {
		long result = 0;
		Cursor cursor = null;
		SQLiteDatabase readDB = null;
		try {
			readDB = mDbHelper.getReadableDatabase();
			cursor = readDB.rawQuery(QUERY_COUNT_SQL, new String[] {});
			cursor.moveToFirst();
			result = cursor.getLong(0);
		} catch (Exception e) {
			//ServiceLog.e(e);
		} finally {
			closeCursor(cursor);
			closeSQLiteDatabase(readDB);
		}
		return result;
	}

	/**
	 * 关闭游标
	 * 
	 * @param cursor
	 */
	private void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param db
	 */
	private void closeSQLiteDatabase(SQLiteDatabase db) {
		if (db != null) {
			db.close();
		}
	}

	/**
	 * 分钟转毫
	 * 
	 * @param minutes
	 * @return
	 */
	private int getMillis(int minutes) {
		return minutes * 60 * 1000;
	}
}
