package derongan.upper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.CheckedTextView;

import org.w3c.dom.Text;

public class CheckedLinearLayout extends LinearLayout implements Checkable {

    private boolean mChecked;
    private Checkable mCheckable;
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    public static interface OnCheckedChangeListener {

        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param checkableView The view whose state has changed.
         * @param isChecked     The new checked state of checkableView.
         */
        void onCheckedChanged(View checkableView, boolean isChecked);
    }

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public CheckedLinearLayout(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public CheckedLinearLayout(Context context){
        super(context);
    }

    @Override
    public boolean isChecked(){
        return mChecked;
    }


    @Override
    public void setChecked(boolean isChecked){
        mCheckable.setChecked(isChecked);
        if (isChecked != mChecked) {
            mChecked = isChecked;

            refreshDrawableState();

            //Not really clear if this does anything
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mChecked);
            }
        }
    }

    @Override
    public void toggle(){
        setChecked(!mChecked);
    }

    //Not really clear if this does anything
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    //Not really clear if this is needed
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        mCheckable = (Checkable)findViewById(R.id.radio_button);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        toggle();
        return true;
    }

}
