package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

/**
 * Created by BKJCB on 2017/3/31.
 */

public class CheckPlanDaolmpl extends CheckPlanDao {
    private SQLiteDatabase db;

    public CheckPlanDaolmpl(Context context) {
        SQLiteOpenUtil util = new SQLiteOpenUtil(context);
        this.db = util.getWritableDatabase();
    }

    @Override
    public void insertCheckPlan(CheckPlan plan) {
        ContentValues values = new ContentValues();
        values.put("identifier", plan.getIdentifier());
        values.put("name", plan.getName());
        values.put("state", plan.getState());
        values.put("address", plan.getAddress());
        values.put("area", plan.getArea());
        values.put("manager", plan.getManager());
        values.put("user", plan.getUser());
        values.put("type", plan.getType());
        values.put("tel", plan.getTel());
        db.insert("checkplan", null, values);
    }

    @Override
    public boolean updateCheckPlan(CheckPlan plan) {
        CheckPlan checkPlan = queryCheckPlan(plan.getIdentifier());
        if (checkPlan == null) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("name", plan.getName());
        values.put("state", plan.getState());
        int isSuccess = db.update("checkplan", values, "indentifier = ", new String[]{String.valueOf(plan.getIdentifier())});

        return isSuccess != 0;
    }

    @Override
    public CheckPlan queryCheckPlan(int identifier) {
        CheckPlan plan = null;
        Cursor cursor = db.query("checkplan", null, "identifier =", new String[]{String.valueOf(identifier)}, null, null, null);
        while (cursor.moveToNext()) {
            plan = new CheckPlan();
            plan.setIdentifier(cursor.getInt(cursor.getColumnIndex("identifier")));
            plan.setName(cursor.getString(cursor.getColumnIndex("name")));
            plan.setState(cursor.getInt(cursor.getColumnIndex("state")));
        }
        LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return plan;
    }

    public int queryCheckPlanState(int identifier) {
        CheckPlan plan = null;
        Cursor cursor = db.query("checkplan", new String[]{"state"}, "identifier =", new String[]{String.valueOf(identifier)}, null, null, null);
        while (cursor.moveToNext()) {
            return cursor.getInt(cursor.getColumnIndex("state"));
        }
        LogUtil.logI("查询检查计划：" + cursor.getColumnCount());
        return -1;
    }

    @Override
    public boolean updateCheckPlanState(CheckPlan plan) {
        int state = queryCheckPlanState(plan.getIdentifier());
        if (state == -1) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("state", plan.getState());
        int isSuccess = db.update("checkplan", values, "indentifier = ", new String[]{String.valueOf(plan.getIdentifier())});

        return isSuccess != 0;
    }

}
