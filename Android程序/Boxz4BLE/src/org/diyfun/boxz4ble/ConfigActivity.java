package org.diyfun.boxz4ble;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import org.diyfun.boxz4ble.util.Constants;
import org.w3c.dom.Text;

public class ConfigActivity extends Activity {

    private TableRow trA;
    private TableRow trB;
    private TableRow trC;
    private TableRow trD;

    private TextView tvHighValueA;
    private TextView tvHighValueB;
    private TextView tvHighValueC;
    private TextView tvHighValueD;

    private TextView tvLowValueA;
    private TextView tvLowValueB;
    private TextView tvLowValueC;
    private TextView tvLowValueD;

    private TextView tvDelayValueA;
    private TextView tvDelayValueB;
    private TextView tvDelayValueC;
    private TextView tvDelayValueD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        initViews();
        setValues();
    }

    private void setValues() {
        SharedPreferences setting = getSharedPreferences("Setting", MODE_PRIVATE);

        int highA = setting.getInt(Constants.KEY_HIGH_A, Constants.DEFAULT_VALUE_HIGH);
        int lowA = setting.getInt(Constants.KEY_LOW_A, Constants.DEFAULT_VALUE_LOW);
        int delayA = setting.getInt(Constants.KEY_DELAY_A, Constants.DEFAULT_VALUE_DELAY);

        tvHighValueA.setText(String.valueOf(highA));
        tvLowValueA.setText(String.valueOf(lowA));
        tvDelayValueA.setText(String.valueOf(delayA));

        int highB = setting.getInt(Constants.KEY_HIGH_B, Constants.DEFAULT_VALUE_HIGH);
        int lowB = setting.getInt(Constants.KEY_LOW_B, Constants.DEFAULT_VALUE_LOW);
        int delayB = setting.getInt(Constants.KEY_DELAY_B, Constants.DEFAULT_VALUE_DELAY);

        tvHighValueB.setText(String.valueOf(highB));
        tvLowValueB.setText(String.valueOf(lowB));
        tvDelayValueB.setText(String.valueOf(delayB));

        int highC = setting.getInt(Constants.KEY_HIGH_C, Constants.DEFAULT_VALUE_HIGH);
        int lowC = setting.getInt(Constants.KEY_LOW_C, Constants.DEFAULT_VALUE_LOW);
        int delayC = setting.getInt(Constants.KEY_DELAY_C, Constants.DEFAULT_VALUE_DELAY);

        tvHighValueC.setText(String.valueOf(highC));
        tvLowValueC.setText(String.valueOf(lowC));
        tvDelayValueC.setText(String.valueOf(delayC));

        int highD = setting.getInt(Constants.KEY_HIGH_D, Constants.DEFAULT_VALUE_HIGH);
        int lowD = setting.getInt(Constants.KEY_LOW_D, Constants.DEFAULT_VALUE_LOW);
        int delayD = setting.getInt(Constants.KEY_DELAY_D, Constants.DEFAULT_VALUE_DELAY);

        tvHighValueD.setText(String.valueOf(highD));
        tvLowValueD.setText(String.valueOf(lowD));
        tvDelayValueD.setText(String.valueOf(delayD));
    }

    private void initViews() {

        trA = (TableRow) findViewById(R.id.tr_1);
        trB = (TableRow) findViewById(R.id.tr_2);
        trC = (TableRow) findViewById(R.id.tr_3);
        trD = (TableRow) findViewById(R.id.tr_4);

        trA.setTag(Constants.BUTTON_NAME_A);
        trB.setTag(Constants.BUTTON_NAME_B);
        trC.setTag(Constants.BUTTON_NAME_C);
        trD.setTag(Constants.BUTTON_NAME_D);

        tvHighValueA = (TextView) findViewById(R.id.tv_1_2);
        tvLowValueA = (TextView) findViewById(R.id.tv_1_3);
        tvDelayValueA = (TextView) findViewById(R.id.tv_1_4);

        tvHighValueB = (TextView) findViewById(R.id.tv_2_2);
        tvLowValueB = (TextView) findViewById(R.id.tv_2_3);
        tvDelayValueB = (TextView) findViewById(R.id.tv_2_4);

        tvHighValueC = (TextView) findViewById(R.id.tv_3_2);
        tvLowValueC = (TextView) findViewById(R.id.tv_3_3);
        tvDelayValueC = (TextView) findViewById(R.id.tv_3_4);

        tvHighValueD = (TextView) findViewById(R.id.tv_4_2);
        tvLowValueD = (TextView) findViewById(R.id.tv_4_3);
        tvDelayValueD = (TextView) findViewById(R.id.tv_4_4);

        View.OnClickListener lsn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(ConfigActivity.this, ConfigDetailActivity.class);
                i.putExtra(Constants.KEY_BUTTON_NAME, view.getTag().toString());
                startActivity(i);
                finish();
            }
        };

        trA.setOnClickListener(lsn);
        trB.setOnClickListener(lsn);
        trC.setOnClickListener(lsn);
        trD.setOnClickListener(lsn);
    }

}
