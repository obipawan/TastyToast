package com.greycellofp.tastytoast.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.greycellofp.tastytoast.R;


public class TastyToast {
	public static final int LENGTH_SHORT = 3000;
	public static final int LENGTH_LONG = 5000;
	
	private static final int LENGTH_VAR_MAX = 10000;
	private static final int LENGTH_VAR_MIN = LENGTH_SHORT;
	
	private static final int VAR_DURATION_TEXT_LOWER_THRESHOLD = 120;

	public static final Style STYLE_ALERT = new Style(LENGTH_LONG, R.color.alert);
	public static final Style STYLE_CONFIRM = new Style(LENGTH_SHORT, R.color.confirm);
	public static final Style STYLE_MESSAGE = new Style(LENGTH_SHORT, R.color.message);

	private final Activity mContext;
	private int mDuration = LENGTH_SHORT;
	private View mView;
	private LayoutParams mLayoutParams;
	private boolean mFloating;

	public TastyToast(Activity context) {
		mContext = context;
	}

	public static TastyToast makeText(Activity context, CharSequence text, Style style) {
		return makeText(context, text, style, R.layout.toasty_toast_layout);
	}

	public static TastyToast makeText(Activity context, CharSequence text, Style style, float textSize) {
		return makeText(context, text, style, R.layout.toasty_toast_layout, textSize);
	}

	public static TastyToast makeText(Activity context, CharSequence text, Style style, int layoutId) {
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(layoutId, null);
		return makeText(context, text, style, v, true);
	}

	public static TastyToast makeText(Activity context, CharSequence text, Style style, int layoutId, float textSize) {
		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflate.inflate(layoutId, null);
		return makeText(context, text, style, v, true, textSize);
	}

	public static TastyToast makeText(Activity context, CharSequence text, Style style, View customView) {
		return makeText(context, text, style, customView, false);
	}

	private static TastyToast makeText(Activity context, CharSequence text, Style style, View view, boolean floating) {
		return makeText(context, text, style, view, floating, 0);
	}

	private static TastyToast makeText(Activity context, CharSequence text,	Style style, View view, boolean floating, float textSize) {
		TastyToast result = new TastyToast(context);

		view.setBackgroundResource(style.background);

		TextView tv = (TextView) view.findViewById(android.R.id.message);
		if (textSize > 0)
			tv.setTextSize(textSize);
		tv.setText(text);

		result.mView = view;
		result.mDuration = style.duration;
		result.mFloating = floating;

		return result;
	}

	public static TastyToast makeText(Activity context, int resId, Style style,	View customView, boolean floating) {
		return makeText(context, context.getResources().getText(resId), style, customView, floating);
	}

	public static TastyToast makeText(Activity context, int resId, Style style)	throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId), style);
	}

	public static TastyToast makeText(Activity context, int resId, Style style, int layoutId) throws Resources.NotFoundException {
		return makeText(context, context.getResources().getText(resId), style, layoutId);
	}

	public void show(){
		ToastQueueMgr manager = ToastQueueMgr.getInstance();
		manager.add(this);
	}

	public boolean isShowing(){
        if (mFloating) 
            return mView != null && mView.getParent() != null;
		else 
            return mView.getVisibility() == View.VISIBLE;
    }
	
    public void cancel(){
        ToastQueueMgr.getInstance().clearMsg(this);

    }
    
    public static void cancelAll(){
        ToastQueueMgr.getInstance().clearAllMsg();
    }
    
    public Activity getActivity(){
        return mContext;
    }
    
    public void setView(View view){
        mView = view;
    }
	
    public View getView(){
        return mView;
    }
    
    public void setDuration(int duration){
        mDuration = duration;
    }
    
    public int getDuration(){
        return mDuration;
    }
    
    public void setText(int resId){
        setText(mContext.getText(resId));
    }
    
    public void setText(CharSequence s){
        if (mView == null) 
            throw new RuntimeException("This TastyToast was not created with TastyToast.makeText()");
        TextView tv = (TextView) mView.findViewById(android.R.id.message);
        if (tv == null) 
            throw new RuntimeException("This TastyToast was not created with TastyToast.makeText()");
        tv.setText(s);
    }
    
    public LayoutParams getLayoutParams(){
        if (mLayoutParams == null) 
            mLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        return mLayoutParams;
    }
    
    public TastyToast setLayoutParams(LayoutParams layoutParams){
        mLayoutParams = layoutParams;
        return this;
    }
    
    public TastyToast setLayoutGravity(int gravity){
        mLayoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, gravity);
        return this;
    }
    
    public boolean isFloating(){
        return mFloating;
    }
    
    public void setFloating(boolean mFloating){
        this.mFloating = mFloating;
    }
    
    public TastyToast enableSwipeDismiss(){
    	View v = getView();
		v.setOnTouchListener(new SwipeDismissTouchListener
				(v,null,
					new SwipeDismissTouchListener.DismissCallbacks(){
	                    @Override
	                    public boolean canDismiss(Object token){
	                        return true;
	                    }

	                    @Override
	                    public void onDismiss(View view, Object token){
	                    	getView().setVisibility(View.GONE);
	                    	ToastQueueMgr.getInstance().clearMsg(TastyToast.this);
	                    }
					}
				));
		return this;
	}
    
    public TastyToast enableVariableDuration(){
    	TextView tv = (TextView) getView().findViewById(android.R.id.message);
    	int charLength = tv.getText().toString().length();
    	int duration = charLength * LENGTH_SHORT / VAR_DURATION_TEXT_LOWER_THRESHOLD;
    	if(duration < LENGTH_VAR_MIN)
    		duration = LENGTH_VAR_MIN;
    	if(duration > LENGTH_VAR_MAX)
    		duration = LENGTH_VAR_MAX;
    	
    	mDuration = duration;
    	return this;
    }
    
    public TastyToast disableVariableDuration(int replacementDuration){
    	this.mDuration = replacementDuration;
    	return this;
    }

	public static class Style{
		private final int duration;
		private final int background;

		public Style(int duration, int resId){
			this.duration = duration;
			this.background = resId;
		}

		public int getDuration(){
			return duration;
		}

		public int getBackground(){
			return background;
		}

		@Override
		public boolean equals(Object o){
			if (!(o instanceof TastyToast.Style))
				return false;
			Style style = (Style) o;
			return style.duration == duration && style.background == background;
		}
	}
}