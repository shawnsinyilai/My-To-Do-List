package com.shawn.phpadvance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DbAdapter {

    public static final String KEY_ID="_id";
    public static final String KEY_DATE="date";
    public static final String KEY_MEMO="memo";
    public static final String KEY_REMINDER="reminder";
    public static final String KEY_BACKGROUNDCOLOUR="backgroundColour";
    public static final String DATABASE_NAME="Memos";
    private static final String TABLE_NAME="memoTable";
    private DbHelper mDbHelper;
    private SQLiteDatabase mdb;
    private final Context mContext;
    private ContentValues values;

    public DbAdapter (Context context){
        this.mContext=context;
        openDatabase();
    }

    public void openDatabase(){
        mDbHelper= new DbHelper(mContext);
        mdb=mDbHelper.getWritableDatabase();
        /*
        getWritableDatabase
        is to open the access
        into database
        */
        Log.i("DB=",mdb.toString());
    }

    public long createMemo(String date,
                           String memo,
                           String reminder,
                           String backgroundColour ){

        try {
            values=new ContentValues();
            values.put(KEY_DATE,date);
            values.put(KEY_MEMO,memo);
            values.put(KEY_REMINDER,reminder);
            values.put(KEY_BACKGROUNDCOLOUR,backgroundColour);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Toast.makeText(mContext,"successfully added",
                    Toast.LENGTH_SHORT).show();
        }


        return  mdb.insert(
                TABLE_NAME,
                null,
                values);
    }
    public Cursor listMemos(){
        Cursor mCursor= mdb.query(TABLE_NAME,new String []{KEY_ID,
        KEY_DATE,KEY_MEMO,KEY_REMINDER,KEY_BACKGROUNDCOLOUR},null,
        null,null,null,null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor queryById(int item_id){
        Cursor mCursor = mdb.query(TABLE_NAME,
                new String []{KEY_ID,KEY_DATE,KEY_MEMO,KEY_REMINDER,KEY_BACKGROUNDCOLOUR},
                KEY_ID + "=" + item_id,null, null,
                null,null,null);

        if (mCursor != null){
            mCursor.moveToFirst();
        }
        return  mCursor;
    }

    public long updateMemo(int id, String date,String memo,String reminder,String backgroundColour){
        long update=0;
        try {
            values= new ContentValues();
            values.put(KEY_DATE,date);
            values.put(KEY_MEMO,memo);
            values.put(KEY_REMINDER,reminder);
            values.put(KEY_BACKGROUNDCOLOUR,backgroundColour);
            update=mdb.update(TABLE_NAME,values,"_id=" + id,null);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Toast.makeText(mContext,"updated successfully",Toast.LENGTH_SHORT).show();
        }

        return update;
    }

    public boolean deleteMemo(int id){
        String [] args={Integer.toString(id)};
        mdb.delete(TABLE_NAME,"_id= ?",args);
        return true;
    }
}
