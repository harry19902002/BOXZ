package org.diyfun.boxz4ble;


import java.util.HashMap;

import org.diyfun.boxz4ble.library.BlunoLibrary;
import org.diyfun.boxz4ble.util.CommandDefine;
import org.diyfun.boxz4ble.util.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends BlunoLibrary {


//	private Button buttonScan;
//	private Button buttonSerialSend;
//	private EditText serialSendText;
//	private EditText serialReceivedText;

    private ImageView ivStatus;
    private ImageView ivBack;
    private ImageView ctrller;
    private TextView tvHP;
    private TextView tvHPBack;
    private TextView tvHpValue;
    private StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onCreateProcess();                                                        //onCreate Process by BlunoLibrary

        // 接收返回json字符串的缓存
        sb = new StringBuffer();

        serialBegin(115200);                                                    //set the Uart Baudrate on BLE chip to 115200
//        serialReceivedText=(EditText) findViewById(R.id.serialReveicedText);	//initial the EditText of the received data
//        serialSendText=(EditText) findViewById(R.id.serialSendText);			//initial the EditText of the sending data
//        
//        buttonSerialSend = (Button) findViewById(R.id.buttonSerialSend);		//initial the button for sending the data
//       

        ctrller = (ImageView) findViewById(R.id.ctrller);
        ivStatus = (ImageView) findViewById(R.id.iv_status);
        tvHP = (TextView) findViewById(R.id.tv_hp);
        tvHPBack = (TextView) findViewById(R.id.tv_hp_back);
        tvHpValue = (TextView) findViewById(R.id.tv_hp_value);
        final RelativeLayout fl = (RelativeLayout) findViewById(R.id.rl_op);
        ivBack = (ImageView) findViewById(R.id.iv_back);

        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        
        buttonScanOnClickProcess();

        //控制器
        ctrller.setOnTouchListener(new OnTouchListener() {
            private int left
                    ,
                    top
                    ,
                    right
                    ,
                    bottom;
            private int x
                    ,
                    y;
            private int mx
                    ,
                    my;
            private int powerValue;
            private int angleValue;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int dx, dy;
                int flWidth = fl.getWidth();
                int maxPower = (int) (flWidth / 3.15F);
//                //根据不同分辨率设置最大移动距离
//                switch (flWidth) {
//                case 630 : maxPower = 208; break;
//                case 470 : maxPower = 156; break;
//                case 310 : maxPower = 104; break;
//                case 230 : maxPower = 78; break;
//                }
                //取得灵敏度
//                int sensiValue = sensiBar.getProgress();
                int sensiDegree = 30;
//                switch (sensiValue) {
//                case 0 : sensiDegree = 90; break;
//                case 1 : sensiDegree = 30; break;
//                case 2 : sensiDegree = 18; break;
//                case 3 : sensiDegree = 10; break;
//                case 4 : sensiDegree = 6; break;
//                case 5 : sensiDegree = 0; break;
//                }

//                sensiDegree = 10;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //屏幕上的位置
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        //相对v的位置
                        dx = (int) event.getX();
                        dy = (int) event.getY();
                        dx = v.getWidth() / 2 - dx;
                        dy = v.getHeight() / 2 - dy;
                        //把x,y修正成v的中心点
                        x = x + dx;
                        y = y + dy;
                        left = v.getLeft();
                        top = v.getTop();
                        right = v.getRight();
                        bottom = v.getBottom();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mx = (int) event.getRawX() - x;
                        my = (int) event.getRawY() - y;
                        //移动距离
                        int distance = culDistance(mx, my);
                        //移动角度。0-360.上方为0度。
                        angleValue = culDegree(mx, -my); //将坐标变为标准坐标系，即第一象限x,y均为正
                        if (my > 0) {
                            angleValue = angleValue + 180;
                        } else if (mx < 0) {
                            angleValue = angleValue + 360;
                        }
                        if (angleValue == 360) {
                            angleValue = 0;
                        }
                        if (sensiDegree > 0) {
                            angleValue = (int) (Math.round((double) angleValue / (double) sensiDegree) * sensiDegree);
                            if (angleValue == 360) {
                                angleValue = 0;
                            }
                        }
                        if (distance <= maxPower) { //触摸点在控制范围内
                            v.layout(left + mx, top + my, right + mx, bottom + my);
                            powerValue = (distance * 100) / maxPower;
//                        tvPower.setText(powerValue+"%");
//                        tvDegree.setText(String.valueOf(angleValue));
                        } else {  //触摸点在控制范围外
                            double per = (double) maxPower / (double) distance;
                            int mxr = (int) (per * mx);
                            int myr = (int) (per * my);
                            v.layout(left + mxr, top + myr, right + mxr, bottom + myr);
                            powerValue = 100;
//                        tvPower.setText("100%");
//                        tvDegree.setText(String.valueOf(angleValue));
                        }
                        //如果是已链接状态，发送控制数据
//                    if (serviceState == CONNECTED) {
//                        sendDataToArduino(angleValue, powerValue);
//                    }

                        calcDirectionWithAngle(angleValue);
                        Log.d("benny", angleValue + "");

                        break;
                    case MotionEvent.ACTION_UP:
                        //位置还原
                        v.layout(left, top, right, bottom);
                        angleValue = 0;
                        powerValue = 0;
//                    tvPower.setText("0%");
//                    tvDegree.setText("0");
//                    if (serviceState == CONNECTED) {
//                        sendDataToArduino(angleValue, powerValue);
//                    }

                        HashMap<String, String> hmAction = new HashMap<String, String>();
                        hmAction.put(CommandDefine.K1, CommandDefine.CENTER);
                        String stop = CommandDefine.generateCommand(CommandDefine.ACTION, hmAction);
                        sendDataToArduino(stop);

                        break;
                }
                return true;
            }
        });


//        buttonSerialSend.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			    
//			    String to = "{\"config\":{\"HP\":100, \"MP\":100,\"ID\":1}}";
//
//			    //String to ="{\"action\":{\"K1\":\"w\"}}";
//			    
//				//serialSend(serialSendText.getText().toString());
//				//send the data to the BLUNO
//				
//				serialSend(to);
//			}
//		});

//        buttonScan = (Button) findViewById(R.id.buttonScan);					//initial the button for scanning the BLE device
//        buttonScan.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//
//				buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
//			}
//		});
    }

    protected void onResume() {
        super.onResume();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();                                                        //onResume Process by BlunoLibrary
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);                    //onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();                                                        //onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        onStopProcess();                                                        //onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyProcess();                                                        //onDestroy Process by BlunoLibrary
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {                                            //Four connection state
            case isConnected:
                ivStatus.setBackgroundResource(R.drawable.icon_green);
                initDevice();
                break;
            case isConnecting:
                ivStatus.setBackgroundResource(R.drawable.icon_blue);
                break;
            case isToScan:
                ivStatus.setBackgroundResource(R.drawable.icon_blue);
                break;
            case isScanning:
                ivStatus.setBackgroundResource(R.drawable.icon_blue);
                break;
            case isDisconnecting:
                ivStatus.setBackgroundResource(R.drawable.icon_red);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSerialReceived(String theString) {                            //Once connection data received, this function will be called
        // TODO Auto-generated method stub
//		serialReceivedText.append(theString);							//append the text into the EditText
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.

        Log.d("benny", "theString : " + theString);

        if (theString != null) {
            int position = theString.indexOf("}}");
            if (position > -1) {
                String leftPart = theString.substring(0, position+ 2);
                String rightPart = theString.substring(position+ 2);
                sb.append(leftPart);
                try {
                    JSONObject jo = new JSONObject(sb.toString());
                    JSONObject propty = jo.getJSONObject("propty");
                    int hp = propty.getInt("HP");
                    updateHP(hp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sb = new StringBuffer();
                sb.append(rightPart);
            } else {
                sb.append(theString);
            }
        }
    }

    private void updateHP(int hp) {
        if (hp <= 1 ){
            initDevice();
        } else {
            int width = tvHPBack.getWidth();
            int currentHpWidth = width * hp / 100;
            ViewGroup.LayoutParams layoutParams = tvHP.getLayoutParams();
            layoutParams.width = currentHpWidth;
            tvHP.setLayoutParams(layoutParams);
            tvHpValue.setText(String.valueOf(hp));
        }
    }

    private void initDevice() {
        sendDataToArduino("{\"config\":{\"HP\":100,\"MP\":100}}");
        ViewGroup.LayoutParams layoutParams = tvHPBack.getLayoutParams();
        ViewGroup.LayoutParams tvHPLayoutParams = tvHP.getLayoutParams();
        tvHPLayoutParams.width = layoutParams.width;
        tvHPLayoutParams.height = layoutParams.height;
        tvHP.setLayoutParams(tvHPLayoutParams);
    }

    /**
     * 计算距离
     *
     * @param mx
     * @param my
     * @return
     */
    private int culDistance(int mx, int my) {
        return (int) Math.round(Math.sqrt((double) (mx * mx + my * my)));
    }

    /**
     * 计算角度atan
     *
     * @param mx
     * @param my
     * @return
     */
    private int culDegree(int mx, int my) {
        double atan = Math.atan((double) mx / (double) my);
        double d = atan / Math.PI * 180;
        return (int) Math.round(d);
    }

    private void calcDirectionWithAngle(int angle) {

        HashMap<String, String> hmAction = new HashMap<String, String>();

        if (angle >= 0 && angle < 22) {
            hmAction.put(CommandDefine.K1, CommandDefine.UP);
        } else if (angle >= 22 && angle < 78) {
            hmAction.put(CommandDefine.K1, CommandDefine.RIGHT_UP);
        } else if (angle >= 78 && angle < 112) {
            hmAction.put(CommandDefine.K1, CommandDefine.RIGHT);
        } else if (angle >= 112 && angle < 158) {
            hmAction.put(CommandDefine.K1, CommandDefine.RIGHT_DOWN);
        } else if (angle >= 158 && angle < 202) {
            hmAction.put(CommandDefine.K1, CommandDefine.DOWN);
        } else if (angle >= 202 && angle < 248) {
            hmAction.put(CommandDefine.K1, CommandDefine.LEFT_DOWN);
        } else if (angle >= 248 && angle < 292) {
            hmAction.put(CommandDefine.K1, CommandDefine.LEFT);
        } else if (angle >= 292 && angle < 338) {
            hmAction.put(CommandDefine.K1, CommandDefine.LEFT_UP);
        } else {
            hmAction.put(CommandDefine.K1, CommandDefine.UP);
        }
        String to = CommandDefine.generateCommand(CommandDefine.ACTION, hmAction);
        sendDataToArduino(to);
    }


}