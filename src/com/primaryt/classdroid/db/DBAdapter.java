package com.primaryt.classdroid.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.primaryt.classdroid.bo.Post;
import com.primaryt.classdroid.bo.Pupil;
import com.primaryt.classdroid.bo.PupilServices;

public class DBAdapter {
	private SQLiteDatabase sqlDB;
	private DatabaseHelper dbHelper;

	public DBAdapter(Context context) {
		dbHelper = new DatabaseHelper(context);
	}

	// Opens the database
	public DBAdapter open() throws SQLException {
		sqlDB = dbHelper.getWritableDatabase();
		return this;
	}

	// Closes the database
	public void close() {
		dbHelper.close();
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, IDBConstants.DATABASE_NAME, null,
					IDBConstants.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(IDBConstants.SQL_PUPIL);
			db.execSQL(IDBConstants.SQL_POSTS);
			db.execSQL(IDBConstants.SQL_PSERV);
			db.execSQL(IDBConstants.SQL_SERVICES);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}

	public long addPupil(String pupilName) {
		ContentValues values = new ContentValues();
		values.put(IDBConstants.PUPIL_NAME, pupilName);
		return sqlDB.insert(IDBConstants.TABLE_PUPIL, null, values);
	}

	public ArrayList<Pupil> getAllPupils() {
		ArrayList<Pupil> pupils = new ArrayList<Pupil>();
		Cursor cursor = sqlDB.query(IDBConstants.TABLE_PUPIL, null, null, null,
				null, null, null);
		Pupil pupil = null;
		while (cursor.moveToNext()) {
			pupil = new Pupil();
			pupil.setId(cursor.getLong(cursor
					.getColumnIndex(IDBConstants.COL_KEY_ROW)));
			pupil.setName(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PUPIL_NAME)));
			if(!pupil.getName().equals("")){
				pupils.add(pupil);
			}else{
				trashPupil(pupil.getId());
			}
		}
		cursor.close();
		return pupils;
	}

	private void trashPupil(long id){
		deletePupil(id);
	}
	public Post addPost(Post post) {
		ContentValues values = new ContentValues();
		values.put(IDBConstants.POST_PUPIL_ID, post.getPupilId());
		values.put(IDBConstants.POST_IS_POSTED, post.getIsPosted());
		values
				.put(IDBConstants.POST_LOCAL_IMAGE_PATH, post
						.getLocalImagePath());
		values.put(IDBConstants.POST_RETURNED_STRING, post.getReturnedString());
		values.put(IDBConstants.POST_TIMESTAMP, post.getTimestamp());
		values.put(IDBConstants.POST_GRADE, post.getGrade());
		post.setId(sqlDB.insert(IDBConstants.TABLE_POSTS, null, values));
		return post;
	}
	
	public Post getPostById(long postId){
		Post post = new Post();
		Cursor cursor = sqlDB.query(IDBConstants.TABLE_POSTS, null, IDBConstants.COL_KEY_ROW+"="+postId, null, null, null, null);
		while(cursor.moveToNext()){
			post.setId(postId);
			post.setPupilId(cursor.getLong(cursor.getColumnIndex(IDBConstants.POST_PUPIL_ID)));
			post.setIsPosted(cursor.getInt(cursor.getColumnIndex(IDBConstants.POST_IS_POSTED)));
			post.setLocalImagePath(cursor.getString(cursor.getColumnIndex(IDBConstants.POST_LOCAL_IMAGE_PATH)));
			post.setReturnedString(cursor.getString(cursor.getColumnIndex(IDBConstants.POST_RETURNED_STRING)));
			post.setTimestamp(cursor.getString(cursor.getColumnIndex(IDBConstants.POST_TIMESTAMP)));
			post.setGrade(cursor.getString(cursor.getColumnIndex(IDBConstants.POST_GRADE)));
		}
		cursor.close();
		return post;
	}

	public Pupil getPupilById(long pupilId) {
		Pupil pupil = null;
		Cursor cursor = sqlDB.query(IDBConstants.TABLE_PUPIL, null,
				IDBConstants.COL_KEY_ROW + "=" + pupilId, null, null, null,
				null);
		while (cursor.moveToNext()) {
			pupil = new Pupil();
			pupil.setId(pupilId);
			pupil.setName(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PUPIL_NAME)));
		}
		cursor.close();
		return pupil;
	}

	public void updatePupil(long pupilId, String name) {
		ContentValues values = new ContentValues();
		values.put(IDBConstants.PUPIL_NAME, name);
		sqlDB.update(IDBConstants.TABLE_PUPIL, values, IDBConstants.COL_KEY_ROW
				+ "=" + pupilId, null);
	}

	public void deletePupil(long pupilId) {
		sqlDB.delete(IDBConstants.TABLE_PUPIL, IDBConstants.COL_KEY_ROW + "="
				+ pupilId, null);
	}

	public void updatePupilService(PupilServices service) {
		ContentValues values = new ContentValues();
		values.put(IDBConstants.PSERV_PUPIL_ID, service.getPupilId());
		values.put(IDBConstants.PSERV_SERV_ID, service.getServiceId());
		values.put(IDBConstants.PSERV_ENABLED, service.getIsEnabled());
		values.put(IDBConstants.PSERV_NICK, service.getNickname());
		values.put(IDBConstants.PSERV_URL, service.getUrl());
		values.put(IDBConstants.PSERV_USERNAME, service.getUsername());
		values.put(IDBConstants.PSERV_PASSWORD, service.getPassword());
		values.put(IDBConstants.PSERV_USEDEFAULT, service.getUseDefault());
		sqlDB.update(IDBConstants.TABLE_PUPIL_SERVICES, values,
				IDBConstants.COL_KEY_ROW + "=" + service.getId(),
				null);
	}

	public PupilServices addPupilService(PupilServices service) {
		ContentValues values = new ContentValues();
		values.put(IDBConstants.PSERV_PUPIL_ID, service.getPupilId());
		values.put(IDBConstants.PSERV_SERV_ID, service.getServiceId());
		values.put(IDBConstants.PSERV_ENABLED, service.getIsEnabled());
		values.put(IDBConstants.PSERV_NICK, service.getNickname());
		values.put(IDBConstants.PSERV_URL, service.getUrl());
		values.put(IDBConstants.PSERV_USERNAME, service.getUsername());
		values.put(IDBConstants.PSERV_PASSWORD, service.getPassword());
		values.put(IDBConstants.PSERV_USEDEFAULT, service.getUseDefault());
		service.setId(sqlDB.insert(IDBConstants.TABLE_PUPIL_SERVICES, null,
				values));
		return service;
	}

	public void deletePupilService(long id) {
		sqlDB.delete(IDBConstants.TABLE_PUPIL_SERVICES,
				IDBConstants.COL_KEY_ROW + "=" + id, null);
	}

	public void deletePupilServiceByPupilId(long pupilId) {
		sqlDB.delete(IDBConstants.TABLE_PUPIL_SERVICES,
				IDBConstants.PSERV_PUPIL_ID + "=" + pupilId, null);
	}

	public PupilServices getPupilServicesById(long id) {
		PupilServices service = new PupilServices();
		Cursor cursor = sqlDB.query(IDBConstants.TABLE_PUPIL_SERVICES, null,
				IDBConstants.COL_KEY_ROW + "=" + id, null, null, null, null);
		if (cursor.moveToNext()) {
			service.setId(id);
			service.setPupilId(cursor.getLong(cursor
					.getColumnIndex(IDBConstants.PSERV_PUPIL_ID)));
			service.setServiceId(cursor.getLong(cursor
					.getColumnIndex(IDBConstants.PSERV_SERV_ID)));
			service.setIsEnabled(cursor.getInt(cursor
					.getColumnIndex(IDBConstants.PSERV_ENABLED)));
			service.setNickname(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PSERV_NICK)));
			service.setUrl(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PSERV_URL)));
			service.setUsername(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PSERV_USERNAME)));
			service.setPassword(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PSERV_PASSWORD)));
			service.setUseDefault(cursor.getInt(cursor
					.getColumnIndex(IDBConstants.PSERV_USEDEFAULT)));
		}
		cursor.close();
		return service;
	}

	public PupilServices getPupilServiceByPupilId(long pupilId, int serviceId) {
		PupilServices service = new PupilServices();
		Cursor cursor = sqlDB.query(IDBConstants.TABLE_PUPIL_SERVICES, null,
				IDBConstants.PSERV_PUPIL_ID + "=" + pupilId + " AND "
						+ IDBConstants.PSERV_SERV_ID + "=" + serviceId, null,
				null, null, null);
		if (cursor.moveToNext()) {
			service.setId(cursor.getLong(cursor
					.getColumnIndex(IDBConstants.COL_KEY_ROW)));
			service.setPupilId(cursor.getLong(cursor
					.getColumnIndex(IDBConstants.PSERV_PUPIL_ID)));
			service.setServiceId(cursor.getLong(cursor
					.getColumnIndex(IDBConstants.PSERV_SERV_ID)));
			service.setIsEnabled(cursor.getInt(cursor
					.getColumnIndex(IDBConstants.PSERV_ENABLED)));
			service.setNickname(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PSERV_NICK)));
			service.setUrl(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PSERV_URL)));
			service.setUsername(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PSERV_USERNAME)));
			service.setPassword(cursor.getString(cursor
					.getColumnIndex(IDBConstants.PSERV_PASSWORD)));
			service.setUseDefault(cursor.getInt(cursor
					.getColumnIndex(IDBConstants.PSERV_USEDEFAULT)));
		}
		cursor.close();
		return service;
	}

	public void updatePost(Post post) {
		ContentValues values = new ContentValues();
		values.put(IDBConstants.POST_IS_POSTED, post.getIsPosted());
		values.put(IDBConstants.POST_RETURNED_STRING, post.getReturnedString());
		values.put(IDBConstants.POST_TIMESTAMP, post.getTimestamp());
		values.put(IDBConstants.POST_GRADE, post.getGrade());
		sqlDB.update(IDBConstants.TABLE_POSTS, values, IDBConstants.COL_KEY_ROW
				+ "=" + post.getId(), null);
	}
}
