package com.bokun.bkjcb.on_siteinspection.Domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by BKJCB on 2017/3/30.
 */

public class SerializableList implements Serializable {
    private ArrayList<CheckResult> list;

    public SerializableList() {
    }

    public ArrayList<CheckResult> getList() {
        return list;
    }

    public void setList(ArrayList<CheckResult> list) {
        this.list = list;
    }
}
