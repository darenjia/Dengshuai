package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.Context;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BKJCB on 2017/3/31.
 */

public class DateUtil {

    public static boolean saveData(Context context, List<CheckResult> results) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckResult result;
        try {
            for (int i = 0; i < results.size(); i++) {
                result = results.get(i);
                if (result.getId() != -1) {
                    daolmpl.updateCheckResult(result);
                } else {
                    daolmpl.insertCheckResult(result);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            daolmpl.colseDateBase();
            return false;
        }
        daolmpl.colseDateBase();
        return true;
    }

    public static ArrayList<CheckResult> readData(Context context, int Identifier) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckPlanDaolmpl planDaolmpl = new CheckPlanDaolmpl(context);
        ArrayList<CheckResult> results = new ArrayList<>();
        if (planDaolmpl.queryCheckPlan(Identifier) == null) {
            planDaolmpl.colseDateBase();
            return results;
        }
        results = daolmpl.queryCheckResult(Identifier);
        daolmpl.colseDateBase();
        LogUtil.logI("查询所有该计划的结果" + "identity" + Identifier + " size:" + results.size());
        return results;
    }

    public static void insertCheckPlan(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        if (daolmpl.queryCheckPlan(plan.getIdentifier()) == null) {
            daolmpl.insertCheckPlan(plan);
            LogUtil.logI("加入一条检查计划" + plan.getIdentifier());
        }
    }

    public static CheckPlan queryCheckPlan(Context context, int indentifier) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        CheckPlan plan = daolmpl.queryCheckPlan(indentifier);
        LogUtil.logI("查询一条检查计划" + plan.getIdentifier() + " state:" + plan.getState());
        return plan;
    }

    public static ArrayList<CheckPlan> queryCheckPlan(Context context) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        ArrayList<CheckPlan> plans = daolmpl.queryCheckPlan();
        daolmpl.colseDateBase();
        LogUtil.logI("查询所以检查计划" + plans.size());
        return plans;
    }

    public static int queryCheckPlanState(Context context, int indentifier) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        int state = daolmpl.queryCheckPlanState(indentifier);
        daolmpl.colseDateBase();
        LogUtil.logI("查询一条检查计划状态" + indentifier + " state:" + state);
        return state;
    }

    public static boolean updateCheckPlan(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        boolean is = daolmpl.updateCheckPlan(plan);
        daolmpl.colseDateBase();
        LogUtil.logI("更新一条检查计划" + plan.getIdentifier() + " state:" + plan.getState());
        return is;
    }

    public static boolean updateCheckPlanState(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        boolean is = daolmpl.updateCheckPlanState(plan);
        daolmpl.colseDateBase();
        LogUtil.logI("更新一条检查计划状态" + plan.getIdentifier() + " state:" + plan.getState());
        return is;
    }
}
