package com.example.Recognition;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.example.Recognition.R;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechListener;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

public class MainActivity extends Activity implements OnClickListener,SynthesizerListener{
	private static String TAG = "Voice";
	
	private EditText editText;
	private Button button1;
	private Button button2;
	//�ϳɶ���
	private SpeechSynthesizer speechSynthesizer;
	//ʶ�𴰿�
	private RecognizerDialog recognizerDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//appid�����Լ������
		SpeechUtility.createUtility(this,SpeechConstant.APPID+"=53bcb9d6");
		init();
		setParam();
		
	}
	
	public void init()
	{
		editText = (EditText) findViewById(R.id.editText1);
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
	}
	
	public void setParam()
	{
		speechSynthesizer = SpeechSynthesizer.createSynthesizer(this,null);
		speechSynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
		speechSynthesizer.setParameter(SpeechConstant.SPEED, "50");
		speechSynthesizer.setParameter(SpeechConstant.VOLUME, "50");
		speechSynthesizer.setParameter(SpeechConstant.PITCH, "50");
	}
	
	public void setDialog()
	{
		recognizerDialog = new RecognizerDialog(this,mTtsInitListener);
		recognizerDialog.setParameter(SpeechConstant.DOMAIN, "iat");
		recognizerDialog.setParameter(SpeechConstant.SAMPLE_RATE, "16000");
		editText.setText(null);
		//��ʾDialog
		recognizerDialog.setListener(dialogListener);
		recognizerDialog.show();
	}
	
	/**
	 * ���ڻ�������
	 */
	private InitListener mTtsInitListener = new InitListener() {
		@Override
		public void onInit(int code) {
			Log.d(TAG, "InitListener init() code = " + code);		
		}
	};
	
	/**
	 * ʶ��ص�������
	 */
	private RecognizerDialogListener dialogListener = new RecognizerDialogListener() {
		//ʶ�����ص�
		@Override
		public void onResult(RecognizerResult arg0, boolean arg1) {
			// TODO Auto-generated method stub
			String text = JsonParser.parseIatResult(arg0.getResultString());
			editText.append(text);
			editText.setSelection(editText.length());
		}
		
		//ʶ������ص�
		@Override
		public void onError(SpeechError arg0) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button1:
			String text = editText.getText().toString();
			speechSynthesizer.startSpeaking(text, this);
			break;
		case R.id.button2:
			setDialog();
			break;
		default:
			break;
		}
	}
	
	//������Ȼص�֪ͨ
	@Override
	public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}
	
	//�����ص�
	@Override
	public void onCompleted(SpeechError arg0) {
		// TODO Auto-generated method stub
		
	}
	//��ʼ����
	@Override
	public void onSpeakBegin() {
		// TODO Auto-generated method stub
		
	}
	//��ͣ����
	@Override
	public void onSpeakPaused() {
		// TODO Auto-generated method stub
		
	}
	//���Ž���
	@Override
	public void onSpeakProgress(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	//��������
	@Override
	public void onSpeakResumed() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * ͨ�ûص��ӿ�
	 */
	private SpeechListener listener = new SpeechListener() {
		
		//��Ϣ�ص�
		@Override
		public void onEvent(int arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			
		}
		
		//���ݻص�
		@Override
		public void onData(byte[] arg0) {
			// TODO Auto-generated method stub
			
		}
		
		//�����ص���û�д���
		@Override
		public void onCompleted(SpeechError arg0) {
			// TODO Auto-generated method stub
			
		}
	};
}
