package com.bokun.bkjcb.on_siteinspection.SQLite;

import android.content.Context;

import com.bokun.bkjcb.on_siteinspection.Domain.CheckPlan;
import com.bokun.bkjcb.on_siteinspection.Domain.CheckResult;
import com.bokun.bkjcb.on_siteinspection.Utils.LocalTools;
import com.bokun.bkjcb.on_siteinspection.Utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BKJCB on 2017/3/31.
 */

public class DateUtil {

    public static boolean SaveDate(Context context, List<Map<String, Object>> results) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckResult result = null;
        Map<String, Object> map = null;
        try {
            for (int i = 0; i < results.size(); i++) {
                result = new CheckResult();
                map = results.get(i);
                result.setIdentifier(Integer.parseInt((String) map.get("Identifier")));
                result.setNum(Integer.parseInt((String) map.get("num")));
                result.setResult(Integer.parseInt((String) map.get("result")));
                result.setComment((String) map.get("comment"));
                result.setImageUrls(LocalTools.changeToString((List<String>) map.get("imagePaths")));
                result.setAudioUrls(LocalTools.changeToString((List<String>) map.get("audioPaths")));
                result.setVideoUrls(LocalTools.changeToString((List<String>) map.get("videoPaths")));
                daolmpl.insertCheckResult(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            daolmpl.colseDateBase();
            return false;
        }
        daolmpl.colseDateBase();
        return true;
    }

    public static List<Map<String, Object>> readDate(Context context, int Identifier) {
        CheckResultDaolmpl daolmpl = new CheckResultDaolmpl(context);
        CheckResult result = null;
        Map<String, Object> map = null;
        List<Map<String, Object>> maps = new ArrayList<>();
        List<CheckResult> list = daolmpl.queryCheckResult(Identifier);
        for (int i = 0; i < list.size(); i++) {
            result = list.get(i);
            map = new HashMap<>();
            map.put("Identifier", result.getIdentifier());
            map.put("num", result.getNum());
            map.put("result", result.getResult());
            map.put("comment", result.getComment());
            map.put("imagePaths", LocalTools.changeToList(result.getImageUrls()));
            map.put("audioPaths", LocalTools.changeToList(result.getAudioUrls()));
            map.put("videoPaths", LocalTools.changeToList(result.getVideoUrls()));
            maps.add(map);
        }
        daolmpl.colseDateBase();
        return maps;
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

    public static int queryCheckPlanState(Context context, int indentifier) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        int state = daolmpl.queryCheckPlanState(indentifier);
        LogUtil.logI("查询一条检查计划" + indentifier + " state:" + state);
        return state;
    }

    public static boolean updateCheckPlan(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        LogUtil.logI("更新一条检查计划" + plan.getIdentifier() + " state:" + plan.getState());
        return daolmpl.updateCheckPlan(plan);
    }

    public static boolean updateCheckPlanState(Context context, CheckPlan plan) {
        CheckPlanDaolmpl daolmpl = new CheckPlanDaolmpl(context);
        LogUtil.logI("更新一条检查计划" + plan.getIdentifier() + " state:" + plan.getState());
        return daolmpl.updateCheckPlanState(plan);
    }
}
