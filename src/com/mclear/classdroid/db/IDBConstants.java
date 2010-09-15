package com.mclear.classdroid.db;

public interface IDBConstants {
	public static final String DATABASE_NAME = "classdroid.db";
	public static final int DATABASE_VERSION = 1;
	
	// Tables
	public static final String TABLE_PUPIL = "pupils";
	public static final String TABLE_POSTS = "posts";
	public static final String TABLE_PUPIL_SERVICES = "pservices";
	public static final String TABLE_SERVICES = "services";
	
	// Key Row Column
	public static final String COL_KEY_ROW = "_id";
	
	// Columns for Table PUPIL
	public static final String PUPIL_NAME = "name";
	
	// Columns for Table POSTS
	public static final String POST_PUPIL_ID = "pupil_id";
	public static final String POST_LOCAL_IMAGE_PATH = "image_path";
	// 0 - Not posted    1 - Posted
	public static final String POST_IS_POSTED = "is_posted";
	public static final String POST_RETURNED_STRING = "returned_string";
	public static final String POST_TIMESTAMP = "timestamp";
	public static final String POST_GRADE = "grade";
	
	// Columns for Table PUPIL_SERVICES
	public static final String PSERV_PUPIL_ID = "pupil_id";
	public static final String PSERV_SERV_ID = "serv_id";
	public static final String PSERV_ENABLED = "enabled";
	public static final String PSERV_NICK = "nick";
	public static final String PSERV_URL = "url";
	public static final String PSERV_USERNAME = "username";
	public static final String PSERV_PASSWORD = "password";
	public static final String PSERV_USEDEFAULT = "use_default";
	
	// Columns for Table SERVICES
	public static final String SERV_URL = "url";
	public static final String SERV_NAME = "service_name";
	public static final String SERV_POST_SYNTAX = "post_syntax";
	public static final String SERV_RETURNED_STRING = "returned_string";
	public static final String SERV_FIELDS_REQ = "fields_req";
	public static final String SERV_USERNAME = "username";
	public static final String SERV_PASSWORD = "password";
	
	// Create Query for Pupil Table
	public static final String SQL_PUPIL = "create table "
		+ IDBConstants.TABLE_PUPIL + "(" + IDBConstants.COL_KEY_ROW + " integer primary key autoincrement,"
		+ IDBConstants.PUPIL_NAME + " text);";
	
	// Create Query for Posts Table
	public static final String SQL_POSTS = "create table "
		+ IDBConstants.TABLE_POSTS + "(" + IDBConstants.COL_KEY_ROW + " integer primary key autoincrement,"
		+ IDBConstants.POST_PUPIL_ID + " integer," + IDBConstants.POST_LOCAL_IMAGE_PATH + " text,"
		+ IDBConstants.POST_RETURNED_STRING + " text," + IDBConstants.POST_IS_POSTED + " integer,"
		+ IDBConstants.POST_TIMESTAMP + " text,"+IDBConstants.POST_GRADE+" text);";
	
	// Create Query for Pupil Services Table
	public static final String SQL_PSERV = "create table "
		+ IDBConstants.TABLE_PUPIL_SERVICES + "(" + IDBConstants.COL_KEY_ROW + " integer primary key autoincrement,"
		+ IDBConstants.PSERV_PUPIL_ID + " integer," + IDBConstants.PSERV_SERV_ID + " integer,"
		+ IDBConstants.PSERV_ENABLED + " integer," + IDBConstants.PSERV_NICK + " text,"
		+ IDBConstants.PSERV_URL + " text," + IDBConstants.PSERV_USERNAME + " text,"
		+ IDBConstants.PSERV_PASSWORD + " text,"+IDBConstants.PSERV_USEDEFAULT +" integer);";
	
	// Create Query for Services table
	public static final String SQL_SERVICES = "create table "
		+ IDBConstants.TABLE_SERVICES + "(" + IDBConstants.COL_KEY_ROW + " integer primary key autoincrement,"
		+ IDBConstants.SERV_URL + " text," + IDBConstants.SERV_NAME + " text,"
		+ IDBConstants.SERV_POST_SYNTAX + " text," + IDBConstants.SERV_RETURNED_STRING + " text,"
		+ IDBConstants.SERV_FIELDS_REQ + " text," + IDBConstants.SERV_USERNAME + " text,"
		+ IDBConstants.SERV_PASSWORD + " text);";
}
