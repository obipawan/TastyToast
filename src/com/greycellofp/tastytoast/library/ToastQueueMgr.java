package com.greycellofp.tastytoast.library;

import java.util.LinkedList;
import java.util.Queue;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

class ToastQueueMgr extends Handler {
	private static final int MESSAGE_DISPLAY = 0x00001;
	private static final int MESSAGE_ADD_VIEW = 0x00002;
	private static final int MESSAGE_REMOVE = 0x00003;

	private static ToastQueueMgr mInstance;

	private Queue<TastyToast> msgQueue;
	private Animation inAnimation, outAnimation;

	private ToastQueueMgr(){
		msgQueue = new LinkedList<TastyToast>();
	}

	static synchronized ToastQueueMgr getInstance(){
		if (mInstance == null) 
			mInstance = new ToastQueueMgr();
		return mInstance;
	}

	void add(TastyToast TastyToast){
		msgQueue.add(TastyToast);
		if (inAnimation == null) 
			inAnimation = AnimationUtils.loadAnimation(TastyToast.getActivity(), android.R.anim.fade_in);
		if (outAnimation == null) 
			outAnimation = AnimationUtils.loadAnimation(TastyToast.getActivity(), android.R.anim.fade_out);
		displayMsg();
	}

	void clearMsg(TastyToast TastyToast){
		if (msgQueue.contains(TastyToast)){
			// Avoid the message from being removed twice.
			removeMessages(MESSAGE_REMOVE);
			msgQueue.remove(TastyToast);
			removeMsg(TastyToast);
		}
	}

	void clearAllMsg(){
		if (msgQueue != null) 
			msgQueue.clear();
		removeMessages(MESSAGE_DISPLAY);
		removeMessages(MESSAGE_ADD_VIEW);
		removeMessages(MESSAGE_REMOVE);
	}

	private void displayMsg(){
		if (msgQueue.isEmpty()) 
			return;

		// First peek whether the TastyToast is being displayed.
		final TastyToast TastyToast = msgQueue.peek();
		// If the activity is null we throw away the TastyToast.
		if (TastyToast.getActivity() == null) 
			msgQueue.poll();
		final Message msg;
		if (!TastyToast.isShowing()){
			// Display the TastyToast
			msg = obtainMessage(MESSAGE_ADD_VIEW);
			msg.obj = TastyToast;
			sendMessage(msg);
		} 
		else{
			msg = obtainMessage(MESSAGE_DISPLAY);
			sendMessageDelayed(msg, TastyToast.getDuration() + inAnimation.getDuration() + outAnimation.getDuration());
		}
	}

	private void removeMsg(final TastyToast TastyToast){
		ViewGroup parent = ((ViewGroup) TastyToast.getView().getParent());
		if (parent != null){
			outAnimation.setAnimationListener(new OutAnimationListener(TastyToast));
			TastyToast.getView().startAnimation(outAnimation);
			// Remove the TastyToast from the queue.
			msgQueue.poll();
			if (TastyToast.isFloating()) 
				parent.removeView(TastyToast.getView());
			else 
				TastyToast.getView().setVisibility(View.INVISIBLE);

			Message msg = obtainMessage(MESSAGE_DISPLAY);
			sendMessage(msg);
		}
	}

	private void addMsgToView(TastyToast TastyToast){
		View view = TastyToast.getView();
		if (view.getParent() == null) 
			TastyToast.getActivity().addContentView(view, TastyToast.getLayoutParams());

		view.startAnimation(inAnimation);
		if (view.getVisibility() != View.VISIBLE) 
			view.setVisibility(View.VISIBLE);
		final Message msg = obtainMessage(MESSAGE_REMOVE);
		msg.obj = TastyToast;
		sendMessageDelayed(msg, TastyToast.getDuration());
	}

	@Override
	public void handleMessage(Message msg){
		final TastyToast TastyToast;
		switch (msg.what){
			case MESSAGE_DISPLAY:
				displayMsg();
				break;
			case MESSAGE_ADD_VIEW:
				TastyToast = (TastyToast) msg.obj;
				addMsgToView(TastyToast);
				break;
			case MESSAGE_REMOVE:
				TastyToast = (TastyToast) msg.obj;
				removeMsg(TastyToast);
				break;
			default:
				super.handleMessage(msg);
				break;
		}
	}

	private static class OutAnimationListener implements Animation.AnimationListener{
		private TastyToast TastyToast;

		private OutAnimationListener(TastyToast TastyToast){
			this.TastyToast = TastyToast;
		}

		@Override
		public void onAnimationStart(Animation animation){}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (!TastyToast.isFloating()) 
				TastyToast.getView().setVisibility(View.GONE);
		}

		@Override
		public void onAnimationRepeat(Animation animation){}
	}
}