package com.bokun.bkjcb.on_siteinspection.Domain;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by BKJCB on 2017/3/30.
 */

public class SerializableHashMap implements Serializable{
    private HashMap<String,String> map;
    public SerializableHashMap(){
    }

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
