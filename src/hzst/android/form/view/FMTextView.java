package hzst.android.form.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hzst.android.R;
import hzst.android.form.ViewCreator;

public class FMTextView extends FMBaseView {

    private TextView tv;
    private OnTextChangeListener onTextChangeListener;

    public FMTextView(Context context) {
        super(context);
    }

    public void setTextColor(int color) {
        tv.setTextColor(color);
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    @Override
    protected void initView() {
    }

    @Override
    public void showView() {
        tv = new TextView(context);
        tv.setPadding(0, (int) (5 * density), 0, (int) (5 * density));
        tv.setTextSize(ViewCreator.TEXT_SIZE_CONTENT);
        tv.setTextColor(context.getResources().getColor(R.color.text_gray3));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(params);
        String value = info.getSubmitValues().get(0).getValue();//文本只有一个value
        tv.setText(value);
        if (info.isReadOnly()) {
            tv.setEnabled(false);
        }
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setGravity();
                viewCreator.setSubmitValue(s.toString(), info);
//				viewCreator.updateViewInfo(info, true);
                if(onTextChangeListener != null){
                    onTextChangeListener.textChanged(s);
                }
            }
        });

        addView(tv);
    }

    private void setGravity() {
        if (viewCreator.isEditable) {
            tv.setGravity(Gravity.RIGHT);
        } else {
            tv.setGravity(Gravity.LEFT);
        }
    }

    @Override
    public Object getContent() {
        return tv.getText();
    }

    @Override
    public void setContent(CharSequence content) {
        tv.setText(content);
    }

    public interface OnTextChangeListener {
        void textChanged(CharSequence text);
    }
}
