package com.greycellofp.tastytoast;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by pawan.kumar1 on 06/05/15.
 */
public class ToastQueueMgr extends Handler{
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

    void add(TastyToast tastyToast){
        msgQueue.add(tastyToast);
        if (inAnimation == null)
            inAnimation = AnimationUtils.loadAnimation(tastyToast.getActivity(), android.R.anim.fade_in);
        if (outAnimation == null)
            outAnimation = AnimationUtils.loadAnimation(tastyToast.getActivity(), android.R.anim.fade_out);
        displayMsg();
    }

    void clearMsg(TastyToast tastyToast){
        if (msgQueue.contains(tastyToast)){
            // Avoid the message from being removed twice.
            removeMessages(MESSAGE_REMOVE);
            msgQueue.remove(tastyToast);
            removeMsg(tastyToast);
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
        final TastyToast tastyToast = msgQueue.peek();
        // If the activity is null we throw away the TastyToast.
        if (tastyToast.getActivity() == null)
            msgQueue.poll();
        final Message msg;
        if (!tastyToast.isShowing()){
            // Display the TastyToast
            msg = obtainMessage(MESSAGE_ADD_VIEW);
            msg.obj = tastyToast;
            sendMessage(msg);
        }
        else{
            msg = obtainMessage(MESSAGE_DISPLAY);
            sendMessageDelayed(msg, tastyToast.getDuration() + inAnimation.getDuration() + outAnimation.getDuration());
        }
    }

    private void removeMsg(final TastyToast tastyToast){
        ViewGroup parent = ((ViewGroup) tastyToast.getView().getParent());
        if (parent != null){
            outAnimation.setAnimationListener(new OutAnimationListener(tastyToast));
            tastyToast.getView().startAnimation(outAnimation);
            // Remove the TastyToast from the queue.
            msgQueue.poll();
            if (tastyToast.isFloating())
                parent.removeView(tastyToast.getView());
            else
                tastyToast.getView().setVisibility(View.INVISIBLE);

            Message msg = obtainMessage(MESSAGE_DISPLAY);
            sendMessage(msg);
        }
    }

    private void addMsgToView(TastyToast tastyToast){
        View view = tastyToast.getView();
        if (view.getParent() == null)
            tastyToast.getActivity().addContentView(view, tastyToast.getLayoutParams());

        view.startAnimation(inAnimation);
        if (view.getVisibility() != View.VISIBLE)
            view.setVisibility(View.VISIBLE);
        final Message msg = obtainMessage(MESSAGE_REMOVE);
        msg.obj = tastyToast;
        sendMessageDelayed(msg, tastyToast.getDuration());
    }

    @Override
    public void handleMessage(Message msg){
        final TastyToast tastyToast;
        switch (msg.what){
            case MESSAGE_DISPLAY:
                displayMsg();
                break;
            case MESSAGE_ADD_VIEW:
                tastyToast = (TastyToast) msg.obj;
                addMsgToView(tastyToast);
                break;
            case MESSAGE_REMOVE:
                tastyToast = (TastyToast) msg.obj;
                removeMsg(tastyToast);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    private static class OutAnimationListener implements Animation.AnimationListener{
        private TastyToast tastyToast;

        private OutAnimationListener(TastyToast tastyToast){
            this.tastyToast = tastyToast;
        }

        @Override
        public void onAnimationStart(Animation animation){}

        @Override
        public void onAnimationEnd(Animation animation) {
            if (!tastyToast.isFloating())
                tastyToast.getView().setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation){}
    }
}
