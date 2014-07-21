package org.diyfun.boxz4ble.util;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

public class CommandDefine {

    public static final String CONFIG = "config";
    public static final String ACTION = "action";
    public static final String STATUS = "status";
    public static final String PROPTY = "propty";

    public static final String UP = "w";
    public static final String DOWN = "s";
    public static final String LEFT = "a";
    public static final String RIGHT = "d";
    public static final String LEFT_UP = "q";
    public static final String LEFT_DOWN = "z";
    public static final String RIGHT_UP = "e";
    public static final String RIGHT_DOWN = "x";
    public static final String CENTER = "";

    public static final String HP = "HP";
    public static final String MP = "MP";
    public static final String ID = "ID";
    public static final String K1 = "K1";
    public static final String K2 = "K2";
    public static final String OT = "OT";
    public static final String IF = "IF";


    public static String generateCommand(String cmd, HashMap<String, String> op) {

        StringBuilder sBuilder = new StringBuilder();
        JSONObject json = new JSONObject();
        JSONObject jsonKV = new JSONObject();
        try {
            // if (CONFIG.equals(cmd)) {

            Iterator iterator = op.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                String value = op.get(key);

                jsonKV.put(key, value);

                System.out.println(key + " -- " + value);
            }

            // }else if (ACTION.equals(cmd)) {
            //
            // }

            json.put(cmd, jsonKV);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(json.toString());
        return json.toString();

    }

    public HashMap<String, String> getCommand(String cmd) {

        HashMap<String, String> op = new HashMap<String, String>();

        return op;
    }
}
