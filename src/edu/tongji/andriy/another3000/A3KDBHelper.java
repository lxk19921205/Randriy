package edu.tongji.andriy.another3000;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class A3KDBHelper extends SQLiteOpenHelper {

	private static final String A3K_DB_NAME = "randriy_a3k_db";
	private static final int A3K_DB_VERSION = 1;

	private static final String A3K_RECITE_ORDER_TABLE_NAME = "randriy_a3k_recite_order_table";
	private static final String A3K_RECITED_LIST_TABLE_NAME = "randriy_a3k_recited_list_table";

	private static final String FIELD_ID = "_id";
	private static final String FIELD_UNIT_INDEX = "_unit_index";

	public A3KDBHelper(Context context) {
		super(context, A3K_DB_NAME, null, A3K_DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.CreateReciteOrderTable(db);
		this.CreateRecitedListTable(db);
	}
	
	private void CreateReciteOrderTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + A3K_RECITE_ORDER_TABLE_NAME + " (" + FIELD_ID
				+ " INTEGER primary key autoincrement,  " + FIELD_UNIT_INDEX
				+ " INTEGER)";
		db.execSQL(sql);		
	}
	
	private void CreateRecitedListTable(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + A3K_RECITED_LIST_TABLE_NAME + " (" + FIELD_ID
				+ " INTEGER primary key autoincrement,  " + FIELD_UNIT_INDEX
				+ " INTEGER)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.DropReciteOrderTable(db);
		this.DropRecitedListTable(db);
		onCreate(db);
	}
	
	private void DropReciteOrderTable(SQLiteDatabase db) {
		String sql = "DROP TABLE IF EXISTS " + A3K_RECITE_ORDER_TABLE_NAME;
		db.execSQL(sql);
	}
	
	private void DropRecitedListTable(SQLiteDatabase db) {
		String sql = "DROP TABLE IF EXISTS " + A3K_RECITED_LIST_TABLE_NAME;
		db.execSQL(sql);
	}		
	
	/**
	 * 返回全部的背诵顺序
	 * @return
	 */
	public List<A3KIndex> LoadReciteOrder() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(A3K_RECITE_ORDER_TABLE_NAME, null, null, null, null, null, null);

		List<A3KIndex> indexList = new ArrayList<A3KIndex>(cursor.getCount());
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {
			indexList.add(new A3KIndex(cursor.getInt(1)));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return indexList;
	}
	
	/**
	 * 将全部的背诵顺序存入数据库
	 * @param indexList
	 */
	public void SaveReciteOrder(List<A3KIndex> indexList) {
		SQLiteDatabase db = this.getWritableDatabase();
		this.DropReciteOrderTable(db);
		this.CreateReciteOrderTable(db);

		db.beginTransaction();
		for (A3KIndex index : indexList) {
			ContentValues cv = new ContentValues();
			cv.put(FIELD_UNIT_INDEX, index.GetTotalIndex());
			db.insert(A3K_RECITE_ORDER_TABLE_NAME, null, cv);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	
//	public void delete(int id) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		String where = FIELD_ID + " = ?";
//		String[] whereValue = {Integer.toString(id)};
//		
//		db.delete(TABLE_NAME, where, whereValue);
//	}
//	
//	public void update (int id, String name, String singer, String path) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		String where = FIELD_ID + " = ?";
//		String[] whereValue = {Integer.toString(id)};
//		
//		ContentValues cv = new ContentValues();
//		cv.put(FIELD_NAME, name);
//		cv.put(FIELD_SINGER, singer);
//		cv.put(FIELD_PATH, path);
//
//		db.update(TABLE_NAME, cv, where, whereValue);
//	}

}
