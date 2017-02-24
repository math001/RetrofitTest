package dbcache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 缓存数据库类
 * 
 * @author xiangming
 * 
 */
public class CacheDbHelper extends SQLiteOpenHelper {
	
	public static final String DB_CACHE_NAME = "womai_db_cache";
	public static final int DB_CACHE_VERSION = 3;
	
	/**
	 * 
	 * 构造函数
	 * 
	 * @param context
	 *            上下文
	 */
	protected CacheDbHelper(Context context) {
		super(context, DB_CACHE_NAME, null, DB_CACHE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS T_CACHE(_ID INTEGER PRIMARY KEY AUTOINCREMENT " + ", F_KEY TEXT, F_VALUE TEXT, F_UPDATETIME NUMERIC, F_TAG TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS T_CACHE");
		onCreate(db);
	}
}
