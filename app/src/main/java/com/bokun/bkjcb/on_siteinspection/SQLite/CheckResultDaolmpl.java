package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;

import java.sql.PreparedStatement;

/**
 * Created by BKJCB on 2017/3/23.
 */

public class CheckResultDaolmpl extends CheckResultDao{
    private SQLiteDatabase database;
    private PreparedStatement statement;
    public CheckResultDaolmpl(Context context) {
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        database = util.getWritableDatabase();
    }

    @Override
    public void addCheckResult(CheckResult result) {
        database.execSQL("insert into checkresult values(null,2133,12312,1,'sadasdas','dasdas','wad','sad')");
    }

    @Override
    public void findCheckResult(CheckResult result) {

    }

    @Override
    public void updateCheckResult(CheckResult result) {

    }

    @Override
    public void changeCheckResult(CheckResult result) {

    }
    public void colseDateBase(){
        database.close();
    }
}
