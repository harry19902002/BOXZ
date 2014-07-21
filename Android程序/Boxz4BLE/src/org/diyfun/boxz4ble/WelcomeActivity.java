package org.diyfun.boxz4ble;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.diyfun.boxz4ble.util.CommandDefine;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Welcome
 * 
 * @author benny Yang at 2014-2-12
 * 
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;

        int height = dm.heightPixels;

        Log.d("benny", "手机屏幕分辨率：" + width + "*" + height + "   ----  " + dm.densityDpi);
        
        
        HashMap<String, String> op = new HashMap<String, String>();
        op.put(CommandDefine.HP, "100");
        op.put(CommandDefine.MP, "99");
        CommandDefine.generateCommand(CommandDefine.CONFIG, op);
        
        

        timer.schedule(task, 2000);


        try {
            JSONObject jsObj = new JSONObject("{\"property\":{\"HP\":100,\"MP\":98}}");
            JSONObject property = jsObj.getJSONObject("property");
            System.out.println(property.get("MP") + "////////////////////////////////////////////");
            System.out.println(property.get("HP"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private final int SPLASH_OK = 0;

    Handler upHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
            case SPLASH_OK:
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.what = SPLASH_OK;
            upHandler.sendMessage(msg);
        }
    };

}
