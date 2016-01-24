package com.abhi_shri.placebo.giveandtake;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.sql.SQLException;
import java.util.HashMap;

public class GiveAndTakeStatusProvider extends ContentProvider {

    static final String PROVIDER_NAME="com.app.giveandtake.statusprovider";
    static final String URL="content://"+PROVIDER_NAME+"/status";
    static final Uri CONTENT_URI=Uri.parse(URL);

    static final String databaseName="gt_database";
    static final String tableName="gt_table";
    static final String tableColumnOneName="id_name";
    static final String tableColumnTwoName="status_give";
    static final String tableColumnThreeName="status_take";
    static final int DATABASE_VERSION=1;
    static final String CREATE_DB_TABLE="" +
            " CREATE TABLE " + tableName +
            " (id_name PRIMARY KEY, " +
            " status_give TEXT, " +
            " status_take TEXT); ";

    static final int status=0;
    static final int status_id=1;

    static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,"status",status);
        uriMatcher.addURI(PROVIDER_NAME,"status/*",status_id);
    }

    private SQLiteDatabase db;

    private static class DatabaseHelper extends SQLiteOpenHelper{

        public DatabaseHelper(Context context) {
            super(context, databaseName, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+tableName);
            onCreate(sqLiteDatabase);
        }
    }

    public GiveAndTakeStatusProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId=db.insert(tableName,null,values);

        if(rowId > 0){
            Uri _uri= ContentUris.withAppendedId(CONTENT_URI,rowId);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper databaseHelper=new DatabaseHelper(context);

        db = databaseHelper.getWritableDatabase();

        return db==null?false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder=new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(tableName);

        switch (uriMatcher.match(uri)){
            case status_id:
                sqLiteQueryBuilder.appendWhere(tableColumnOneName + " = '" + uri.getPathSegments().get(1) + "'");
                break;
        }

        Cursor cursor = sqLiteQueryBuilder.query(db,null,null,null,null,null,null);

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count=0;
        switch(uriMatcher.match(uri)){
            case status_id:
                count=db.update(tableName,values,tableColumnOneName + " = " +
                        " '" + uri.getPathSegments().get(1) + "'" + (!TextUtils.isEmpty(selection)?" AND (" +
                        ""+selection+")":""),null);
                break;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }
}
