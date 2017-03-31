package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/23.
 */

public class CheckResultDaolmpl extends CheckResultDao {
    private SQLiteDatabase database;

    public CheckResultDaolmpl(Context context) {
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        database = util.getWritableDatabase();
    }

    @Override
    public boolean insertCheckResult(CheckResult result) {
//      database.execSQL("insert into checkresult values(null,2133,12312,1,'sadasdas','dasdas','wad','sad')");
        ContentValues values = new ContentValues();
        values.put("identifier", result.getIdentifier());
        values.put("num", result.getNum());
        values.put("checkresult", result.getResult());
        values.put("comment", result.getComment());
        values.put("audio", result.getAudioUrls());
        values.put("image", result.getImageUrls());
        values.put("video", result.getVideoUrls());
        long isSuccess = database.insert("checkresult", "id", values);
        return isSuccess != -1;
    }

    /*
    *  "id Integer primary key,"
       "identifier int(10),"
       "num int(10)," +
       "checkresult int(1),"
       "comment text," +
       "audio char(100),"
       "image char(100),"
       "video char(100)"
     */
    @Override
    public List<CheckResult> queryCheckResult(int Identifier) {
        List<CheckResult> list = new ArrayList<>();
        CheckResult result;
        Cursor cursor = database.query("checkresult", null, "identifier = ", new String[]{String.valueOf(Identifier)}, null, null, "num");
        while (cursor.moveToNext()) {
            result = new CheckResult();
            result.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            result.setNum(cursor.getInt(cursor.getColumnIndex("num")));
            result.setComment(cursor.getString(cursor.getColumnIndex("comment")));
            result.setResult(cursor.getInt(cursor.getColumnIndex("checkresult")));
            result.setImageUrls(cursor.getString(cursor.getColumnIndex("image")));
            result.setAudioUrls(cursor.getString(cursor.getColumnIndex("audio")));
            result.setVideoUrls(cursor.getString(cursor.getColumnIndex("video")));
            list.add(result);
        }
        return list;
    }

    @Override
    public void updateCheckResult(CheckResult result) {

    }

    @Override
    public void changeCheckResult(CheckResult result) {

    }

    public void colseDateBase() {
        database.close();
    }
}
