package com.greycellofp.tastytoast.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.greycellofp.tastytoast.R;
import com.greycellofp.tastytoast.library.TastyToast;

public class MainActivity extends Activity implements OnClickListener{
	private static final String TAG = MainActivity.class.getSimpleName();

	private Button alertButton;
	private Button confirmButton;
	private Button messageButton;
	private CheckBox canSwipeCheckbox;
	private CheckBox enableVariableDuration;
	private TextView textInput;
	
	
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
		enableVariableDuration = (CheckBox) findViewById(R.id.enable_variable_duration);
		textInput = (TextView) findViewById(R.id.text_input);
	}
	
	private void setUiListeners(){
		alertButton.setOnClickListener(this);
		confirmButton.setOnClickListener(this);
		messageButton.setOnClickListener(this);
		enableVariableDuration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					textInput.setVisibility(View.VISIBLE);
				}else{
					textInput.setVisibility(View.GONE);
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		TastyToast tastyToast = null;
		switch(id){
			case R.id.alert_button:{
				String text = "Example Alert message";
				if(enableVariableDuration.isChecked()){
					text = textInput.getText().toString();
					tastyToast = TastyToast.makeText(this, text, TastyToast.STYLE_ALERT).enableVariableDuration();
				}else{
					tastyToast = TastyToast.makeText(this, text, TastyToast.STYLE_ALERT);
				}
				
				if(canSwipeCheckbox.isChecked())
					tastyToast.enableSwipeDismiss();
				
				break;
			}
			case R.id.confirm_button:{
				String text = "Example Confirm message";
				if(enableVariableDuration.isChecked()){
					text = textInput.getText().toString();
					tastyToast = TastyToast.makeText(this, text, TastyToast.STYLE_CONFIRM).enableVariableDuration();
				}else{
					tastyToast = TastyToast.makeText(this, text, TastyToast.STYLE_CONFIRM);
				}
				
				if(canSwipeCheckbox.isChecked())
					tastyToast.enableSwipeDismiss();
				
				break;
			}
			case R.id.message_button:{
				String text = "Example simple message";
				if(enableVariableDuration.isChecked()){
					text = textInput.getText().toString();
					tastyToast = TastyToast.makeText(this, text, TastyToast.STYLE_MESSAGE).enableVariableDuration();
				}else{
					tastyToast = TastyToast.makeText(this, text, TastyToast.STYLE_MESSAGE);
				}
				
				if(canSwipeCheckbox.isChecked())
					tastyToast.enableSwipeDismiss();
				
				break;
			}
		}
		if(tastyToast != null){
			tastyToast.show();
		}
	}
}
