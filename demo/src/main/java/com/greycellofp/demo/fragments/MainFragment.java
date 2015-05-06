package com.greycellofp.demo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.greycellofp.demo.R;
import com.greycellofp.tastytoast.TastyToast;

/**
 * Created by pawan.kumar1 on 06/05/15.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private Button alertButton;
    private Button confirmButton;
    private Button messageButton;
    private CheckBox canSwipeCheckbox;
    private CheckBox enableVariableDuration;
    private EditText textInput;
    private TextView inputTextSize;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUiElements(view);
        setUiListeners();
    }

    private void initUiElements(View view){
        alertButton = (Button) view.findViewById(R.id.alert_button);
        confirmButton = (Button) view.findViewById(R.id.confirm_button);
        messageButton = (Button) view.findViewById(R.id.message_button);
        canSwipeCheckbox = (CheckBox) view.findViewById(R.id.can_swipe_checkbox);
        enableVariableDuration = (CheckBox) view.findViewById(R.id.enable_variable_duration);
        textInput = (EditText) view.findViewById(R.id.text_input);
        inputTextSize = (TextView) view.findViewById(R.id.input_text_size);
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
                    inputTextSize.setVisibility(View.VISIBLE);
                }else{
                    textInput.setVisibility(View.GONE);
                    inputTextSize.setVisibility(View.GONE);
                }
            }
        });

        textInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputTextSize.setText("Size:" + s.length());
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
                    tastyToast = TastyToast.makeText(getActivity(), text, TastyToast.STYLE_ALERT).enableVariableDuration();
                }else{
                    tastyToast = TastyToast.makeText(getActivity(), text, TastyToast.STYLE_ALERT);
                }

                if(canSwipeCheckbox.isChecked())
                    tastyToast.enableSwipeDismiss();

                break;
            }
            case R.id.confirm_button:{
                String text = "Example Confirm message";
                if(enableVariableDuration.isChecked()){
                    text = textInput.getText().toString();
                    tastyToast = TastyToast.makeText(getActivity(), text, TastyToast.STYLE_CONFIRM).enableVariableDuration();
                }else{
                    tastyToast = TastyToast.makeText(getActivity(), text, TastyToast.STYLE_CONFIRM);
                }

                if(canSwipeCheckbox.isChecked())
                    tastyToast.enableSwipeDismiss();

                break;
            }
            case R.id.message_button:{
                String text = "Example simple message";
                if(enableVariableDuration.isChecked()){
                    text = textInput.getText().toString();
                    tastyToast = TastyToast.makeText(getActivity(), text, TastyToast.STYLE_MESSAGE).enableVariableDuration();
                }else{
                    tastyToast = TastyToast.makeText(getActivity(), text, TastyToast.STYLE_MESSAGE);
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
