package com.example.application;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.application.MyDatabaseHelper;

public class DatabaseProvider extends ContentProvider {

    public static final int CONTACT_DIR = 0;
    public static final int CONTACT_ITEM = 1;

    public static final String AUTHORITY = "com.example.databasetest.provider";

    public static UriMatcher uriMatcher;

    private MyDatabaseHelper dbHelper;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "contact",CONTACT_DIR);
        uriMatcher.addURI(AUTHORITY, "contact/#",CONTACT_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(),"Contacts.db",null,1);
        return true;

    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)){
            case CONTACT_DIR:
                cursor = db.query("Contact",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CONTACT_ITEM:
                String id = uri.getPathSegments().get(1);
                cursor = db.query("Contact",projection,"id = ?",new String[]{id},null,null,sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case CONTACT_DIR:
                return "vnd.android.cursor.dir/vnd."+ AUTHORITY+".contact";
            case CONTACT_ITEM:
                return "vnd.android.cursor.item/vnd."+ AUTHORITY+".contact";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)){
            case CONTACT_DIR:
            case CONTACT_ITEM:
                long id = db.insert("Contact", null, values);
                uriReturn = Uri.parse("content://"+AUTHORITY+"/contact/"+id);
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)){
            case CONTACT_DIR:
                deleteRows = db.delete("Contact",selection,selectionArgs);
                break;
            case CONTACT_ITEM:
                String id = uri.getPathSegments().get(1);
                deleteRows = db.delete("Contact", "id = ?", new String[]{id});
                break;
        }
        return deleteRows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)){
            case CONTACT_DIR:
                updateRows = db.update("Contact",values,selection,selectionArgs);
                break;
            case CONTACT_ITEM:
                String id = uri.getPathSegments().get(1);
                updateRows = db.update("Contact", values, "id=?",new String[]{id});
                break;
        }
        return updateRows;
    }
}
