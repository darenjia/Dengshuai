package com.bokun.bkjcb.on_siteinspection.SQLite;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;

/**
 * Created by BKJCB on 2017/3/31.
 */

public abstract class CheckPlanDao {
    public abstract void insertCheckPlan(CheckPlan plan);
    public abstract boolean updateCheckPlan(CheckPlan plan);
    public abstract CheckPlan queryCheckPlan(int identifier);
    public abstract boolean updateCheckPlanState(CheckPlan plan);
}
