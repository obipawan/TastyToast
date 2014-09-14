package com.greycellofp.tastytoast.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

import com.greycellofp.tastytoast.R;
import com.greycellofp.tastytoast.library.TastyToast;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = MainActivity.class.getSimpleName();

	private Button alertButton;
	private Button confirmButton;
	private Button messageButton;
	private CheckBox canSwipeCheckbox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUiElements();
		setUiListeners();
	}
	
	private void initUiElements(){
		alertButton = (Button) findViewById(R.id.alert_button);
		confirmButton = (Button) findViewById(R.id.confirm_button);
		messageButton = (Button) findViewById(R.id.message_button);
		canSwipeCheckbox = (CheckBox) findViewById(R.id.can_swipe_checkbox);
	}
	
	private void setUiListeners(){
		alertButton.setOnClickListener(this);
		confirmButton.setOnClickListener(this);
		messageButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch(id){
			case R.id.alert_button:
				if(canSwipeCheckbox.isChecked())
					TastyToast.makeText(this, "Example Alert message", TastyToast.STYLE_ALERT).enableSwipeDismiss().show();
				else
					TastyToast.makeText(this, "Example Alert message", TastyToast.STYLE_ALERT).show();
				break;
			case R.id.confirm_button:
				if(canSwipeCheckbox.isChecked())
					TastyToast.makeText(this, "Example Confirm message", TastyToast.STYLE_CONFIRM).enableSwipeDismiss().show();
				else
					TastyToast.makeText(this, "Example Confirm message", TastyToast.STYLE_CONFIRM).show();
				break;
			case R.id.message_button:
				if(canSwipeCheckbox.isChecked())
					TastyToast.makeText(this, "Example simple message", TastyToast.STYLE_MESSAGE).enableSwipeDismiss().show();
				else
					TastyToast.makeText(this, "Example simple message", TastyToast.STYLE_MESSAGE).show();
				break;
		}
	}
}
