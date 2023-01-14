package com.merty.manageyourbudget;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class ContentProvider  extends android.content.ContentProvider {



    static  final  String PROVIDER_NAME="com.merty.manageyourbudget.ContentProvider";
    static  final  String URL = "content://"+PROVIDER_NAME+"/expenses";
    static  final  Uri CONTENT_URI = Uri.parse(URL);

    static  final  String NAME = "name";
    static  final  String AMOUNT = "amount";

    static  final  int EXPENSES = 1;
    static  final UriMatcher uriMatcher;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"expenses",EXPENSES);
    }

    private  static HashMap<String,String> EXPENSE_PROJECTION_MAP;


    //-----------------------------------------------DATABASE------------------------------------------------

    private  SQLiteDatabase sqLiteDatabase;
    static final String DATABASE_NAME =  "Expenses";
    static final String EXPENSES_TABLE_NAME = "expenses";
    static  final  int DATABASE_VERSION = 1;
    static final String CREATE_DATABASE_TABLE = "CREATE TABLE "+
            EXPENSES_TABLE_NAME+ "(name TEXT NOT NULL,"+ "amount INTEGER NOT NULL);";



    private  static  class  DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(@Nullable Context context ) {
            super(context , DATABASE_NAME , null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_DATABASE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ EXPENSES_TABLE_NAME );
            onCreate(sqLiteDatabase);

        }
    }
//-----------------------------------------------DATABASE------------------------------------------------

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        sqLiteDatabase = databaseHelper.getWritableDatabase();

        return sqLiteDatabase != null;
    }
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {


        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(EXPENSES_TABLE_NAME);


        switch (uriMatcher.match(uri)){
            case EXPENSES:
                sqLiteQueryBuilder.setProjectionMap(EXPENSE_PROJECTION_MAP);
                break;

            default:
        }

        if (s1==null || s1.matches("")){
            s1 = NAME;
        }

        Cursor cursor = sqLiteQueryBuilder.query(sqLiteDatabase,strings,s,strings1,null,null,s1);

        cursor.setNotificationUri(getContext().getContentResolver(),uri);


        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        long rowID = sqLiteDatabase.insert(EXPENSES_TABLE_NAME,"",contentValues);

        if (rowID >0){
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI,rowID);
            getContext().getContentResolver().notifyChange(newUri,null);
            return  newUri;

        }


       throw  new SQLException("SQL Error!");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
