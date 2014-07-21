package org.diyfun.boxz4ble;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.diyfun.boxz4ble.util.Constants;

/**
 * Created by Adol on 2014/5/29.
 */
public class ConfigDetailActivity extends Activity {

    private TextView tvHighValue;
    private TextView tvLowValue;
    private TextView tvDelayValue;

    private SeekBar skHigh;
    private SeekBar skLow;
    private SeekBar skDelay;

    private int highValue;
    private int lowValue;
    private int delayValue;

    private ImageView ivBack;

    private String buttonName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_detail);
        initViews();
        setControls();

        Intent i = getIntent();
        if (i != null) {
            Bundle extras = i.getExtras();
            buttonName = extras.getString(Constants.KEY_BUTTON_NAME);
        } else {
            buttonName = Constants.BUTTON_NAME_A;
        }
        setValues(buttonName);
    }

    private void initViews() {

        skHigh = (SeekBar) findViewById(R.id.sk_high);
        skLow = (SeekBar) findViewById(R.id.sk_low);
        skDelay = (SeekBar) findViewById(R.id.sk_delay);

        tvHighValue = (TextView) findViewById(R.id.tv_highValue);
        tvLowValue = (TextView) findViewById(R.id.tv_lowValue);
        tvDelayValue = (TextView) findViewById(R.id.tv_delayValue);

        ivBack = (ImageView) findViewById(R.id.iv_back);

        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                doBack();
                Intent intent = new Intent(ConfigDetailActivity.this, ConfigActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setControls() {
        skHigh.setMax(Constants.DEFAULT_VALUE_HIGH);
        skLow.setMax(Constants.DEFAULT_VALUE_HIGH);
        skDelay.setMax(6);

        skHigh.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener(tvHighValue));
        skLow.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener(tvLowValue));
        skDelay.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener(tvDelayValue));
    }

    private void setValues(String buttonName) {
        SharedPreferences setting = getSharedPreferences("Setting", MODE_PRIVATE);
        if (Constants.BUTTON_NAME_A.equals(buttonName)) {
            highValue = setting.getInt(Constants.KEY_HIGH_A, Constants.DEFAULT_VALUE_HIGH);
            lowValue = setting.getInt(Constants.KEY_LOW_A, Constants.DEFAULT_VALUE_LOW);
            delayValue = setting.getInt(Constants.KEY_DELAY_A, Constants.DEFAULT_VALUE_DELAY);
        } else if (Constants.BUTTON_NAME_B.equals(buttonName)) {
            highValue = setting.getInt(Constants.KEY_HIGH_B, Constants.DEFAULT_VALUE_HIGH);
            lowValue = setting.getInt(Constants.KEY_LOW_B, Constants.DEFAULT_VALUE_LOW);
            delayValue = setting.getInt(Constants.KEY_DELAY_B, Constants.DEFAULT_VALUE_DELAY);
        } else if (Constants.BUTTON_NAME_C.equals(buttonName)) {
            highValue = setting.getInt(Constants.KEY_HIGH_C, Constants.DEFAULT_VALUE_HIGH);
            lowValue = setting.getInt(Constants.KEY_LOW_C, Constants.DEFAULT_VALUE_LOW);
            delayValue = setting.getInt(Constants.KEY_DELAY_C, Constants.DEFAULT_VALUE_DELAY);
        } else if (Constants.BUTTON_NAME_D.equals(buttonName)) {
            highValue = setting.getInt(Constants.KEY_HIGH_D, Constants.DEFAULT_VALUE_HIGH);
            lowValue = setting.getInt(Constants.KEY_LOW_D, Constants.DEFAULT_VALUE_LOW);
            delayValue = setting.getInt(Constants.KEY_DELAY_D, Constants.DEFAULT_VALUE_DELAY);
        }
        skHigh.setProgress(highValue);
        skLow.setProgress(lowValue);
        skDelay.setProgress(delayValue);
        tvHighValue.setText(String.valueOf(highValue));
        tvLowValue.setText(String.valueOf(lowValue));
        tvDelayValue.setText(String.valueOf(delayValue));
    }

    private void doBack() {

        highValue = skHigh.getProgress();
        lowValue = skLow.getProgress();
        delayValue = skDelay.getProgress();
        SharedPreferences setting = getSharedPreferences("Setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        if (Constants.BUTTON_NAME_A.equals(buttonName)) {
            editor.putInt(Constants.KEY_HIGH_A, highValue);
            editor.putInt(Constants.KEY_LOW_A, lowValue);
            editor.putInt(Constants.KEY_DELAY_A, delayValue);
        } else if (Constants.BUTTON_NAME_B.equals(buttonName)) {
            editor.putInt(Constants.KEY_HIGH_B, highValue);
            editor.putInt(Constants.KEY_LOW_B, lowValue);
            editor.putInt(Constants.KEY_DELAY_B, delayValue);
        } else if (Constants.BUTTON_NAME_C.equals(buttonName)) {
            editor.putInt(Constants.KEY_HIGH_C, highValue);
            editor.putInt(Constants.KEY_LOW_C, lowValue);
            editor.putInt(Constants.KEY_DELAY_C, delayValue);
        } else if (Constants.BUTTON_NAME_D.equals(buttonName)) {
            editor.putInt(Constants.KEY_HIGH_D, highValue);
            editor.putInt(Constants.KEY_LOW_D, lowValue);
            editor.putInt(Constants.KEY_DELAY_D, delayValue);
        }
        editor.commit();
    }

    @Override
    public void onBackPressed() {

        doBack();
        Intent intent = new Intent(ConfigDetailActivity.this, ConfigActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        private TextView tvValue;

        MyOnSeekBarChangeListener(TextView tvValue) {
            this.tvValue = tvValue;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            tvValue.setText(String.valueOf(i));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }

    }
}
