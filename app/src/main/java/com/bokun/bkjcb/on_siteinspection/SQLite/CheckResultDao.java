package com.bokun.bkjcb.on_siteinspection.SQLite;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;

/**
 * Created by BKJCB on 2017/3/20.
 */

public abstract class CheckResultDao {
    public abstract void addCheckResult(CheckResult result);
    public abstract void findCheckResult(CheckResult result);
    public abstract void updateCheckResult(CheckResult result);
    public abstract void changeCheckResult(CheckResult result);
}
