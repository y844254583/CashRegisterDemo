package com.dbframework;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;


/**
 * @author Jelly
 */
public class Table {
	
	private final String tableName;
	private final Field[] fields;
	private final Field lastModifyDate;
	private String[] columns = null;
	
	public Table( String tableName, Field[] fields, Field lastModifyDate ){
		this.tableName = tableName;
		this.fields = fields;
		this.lastModifyDate = lastModifyDate;
	}
	
	public Field[] fields(){
		return fields;
	}
	
	public String tableName(){
		return tableName;
	}
	
	public String sql2Drop(){
		return "DROP TABLE IF EXISTS " + tableName() + ";";
	}
	
	public String sql2Create(){
		StringBuffer sb = new StringBuffer();
		sb.append( "CREATE TABLE " ).append( tableName()).append( " (" );
		boolean first = true;
		for( Field t : fields ){
			if( !first ){
				sb.append( "," );
			}
			first = false;
			sb.append( t.name()).append( " " ).append( t.type() );
		}
		return sb.append( ")" ).toString();
	}
	
	public String[] columns(){
		if( columns == null ){
			columns = new String[fields.length];
			for( int i = 0 ; i < fields.length; i++ ){
				columns[i] = fields[i].name();
			}
		}
		return columns;
	}
	
	public void dropTable( SQLiteDatabase db ){
		db.execSQL( sql2Drop() );
	}
	
	public void createTable( SQLiteDatabase db ){
		db.execSQL( sql2Create() );
	}
	
	public long create( ContentValues values,DBHelper dbHelper ){
		if (values.get(lastModifyDate.name()) == null) {
			values.put(lastModifyDate.name(), System.currentTimeMillis());
		}
		return dbHelper.getWritableDatabase().insert(tableName, null, values);
	}

	public int update(ContentValues values, String whereClause, String[] whereArgs, DBHelper dbHelper) {
		values.put(lastModifyDate.name(), System.currentTimeMillis());
		return dbHelper.getWritableDatabase().update(tableName, values, whereClause, whereArgs);
	}

	public Cursor query(String selection, String[] selectionArgs, Field orderBy, DBHelper dbHelper) {
		return dbHelper.getWritableDatabase().query(tableName(), columns(), selection, selectionArgs, null, null,
				orderBy == null ? null : orderBy.name());
	}
	
	public Cursor query(String selection, String[] selectionArgs, String orderBy, DBHelper dbHelper) {
		return dbHelper.getWritableDatabase().query(tableName(), columns(), selection, selectionArgs, null, null,
				orderBy == null ? null : orderBy);
	}
	
	public Cursor query(String selection, String[] selectionArgs, Field orderBy, DBHelper dbHelper,String limit) {
		return dbHelper.getWritableDatabase().query(tableName(), columns(), selection, selectionArgs, null, null,
				orderBy == null ? null : orderBy.name(),limit);
	}
	
	public void deleteById(Field idField, Long id, DBHelper dbHelper) {
		String sql = "DELETE from " + tableName() + " where " + idField.name() + " = " + id;
		dbHelper.getWritableDatabase().execSQL(sql);
	}

	public void deleteByField(Field idField, String value, DBHelper dbHelper) {
		String sql = "DELETE from " + tableName() + " where " + idField.name() + " = " + value;
		dbHelper.getWritableDatabase().execSQL(sql);
	}

	public void deleteByFields( List<Field> files , List<String> values , DBHelper dbHelper ){

		String sql = "DELETE from " + tableName() + " where ";
		for( int i = 0 ; i < files.size() ; i++ ){
			Field field = files.get( i );
			String value =  values.get( i );

			if( i == 0 ){
				sql += field.name() + " = '" + value + "'";
			}
			else{
				sql += " and " + field.name() + " = '" + value + "'";
			}
		}
		dbHelper.getWritableDatabase().execSQL(sql);
	}
	
	public void deleteAll( DBHelper dbHelper ){
		String sql = "DELETE from " + tableName();
		dbHelper.getWritableDatabase().execSQL(sql);
	}
}
