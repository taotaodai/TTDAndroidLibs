package hzst.android.form.view;

import hzst.android.R;
import hzst.android.form.entity.EditViewOwn;
import hzst.android.form.info.EditViewInfo;
import hzst.android.util.UnitsConversionUtil;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FMEditText extends FMBaseView {
    private View view;
    private EditText et;
    private TextView tvUnit;
    private OnTextChangeListener onTextChangeListener;
    private boolean isCallBck = true;
    private EditViewOwn own;

    private static final int GRAVITY_BEFORE_EDIT_S = Gravity.LEFT | Gravity.BOTTOM;
    private static final int GRAVITY_FINISH_EDIT_S = Gravity.RIGHT | Gravity.BOTTOM;
    private static final int GRAVITY_BEFORE_EDIT_M = Gravity.LEFT | Gravity.TOP;
    private static final int GRAVITY_FINISH_EDIT_M = Gravity.RIGHT | Gravity.TOP;

    public FMEditText(Context context) {
        super(context);
    }

    public OnTextChangeListener getOnTextChangeListener() {
        return onTextChangeListener;
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void showView() {
        view = LayoutInflater.from(context).inflate(R.layout.view_fm_edittext, null);
        View vContent = view.findViewById(R.id.rlay_content);
        vContent.setPadding(0, viewCreator.titlePaddingY, 0, viewCreator.titlePaddingY);
        et = (EditText) view.findViewById(R.id.et_fm);
        tvUnit = (TextView) view.findViewById(R.id.et_unit);
        tvUnit.setTextColor(viewCreator.contentTextColor);
        String value = info.getSubmitValues().get(0).getValue();//EditText只有一个value
        EditViewInfo.setInputType(et, ((EditViewInfo) info).getInputType());
        et.setText(value);

        if (info.isReadOnly()) {
            et.setEnabled(false);
        }
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                viewCreator.setSubmitValue(s.toString(), info);
                if (onViewDataChangeListener != null) {
                    onViewDataChangeListener.dateChange();
                }
                if (onTextChangeListener != null) {
                    if (isCallBck) {
                        onTextChangeListener.textChanged(s.toString());
                    } else {
                        isCallBck = true;
                    }
                }
            }
        });

        own = ((EditViewInfo) info).getOwn();
        if (own == null) {
            own = new EditViewOwn();
        }

        setTextGravity(!(viewCreator.isEditable || !et.isEnabled()));

        et.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (viewCreator.isEditable) {
                    setTextGravity(hasFocus);
                }
            }
        });

        initOwn();
        addView(view);
    }

    private void setTextGravity(boolean hasFocus) {
        if (hasFocus) {
            if (own.getLine() > 1) {
                et.setGravity(GRAVITY_BEFORE_EDIT_M);
            } else {
                et.setGravity(GRAVITY_BEFORE_EDIT_S);
            }
        } else {
            if (!TextUtils.isEmpty(et.getText()) || info.isReadOnly()) {
                if (own.getLine() > 1) {
                    et.setGravity(GRAVITY_FINISH_EDIT_M);
                } else {
                    et.setGravity(GRAVITY_FINISH_EDIT_S);
                }
            }

        }
    }

    @Override
    public void setContent(CharSequence content) {
        et.setText(content);
    }

    public void setContentWithouCallback(CharSequence content) {
        isCallBck = false;
        setContent(content);
    }

    @Override
    public Object getContent() {
        return et.getText();
    }

    @Override
    public void initOwn() {
        if (own != null) {
            String unit = own.getUnit();
            if (!TextUtils.isEmpty(unit)) {
                tvUnit.setText(unit);
            } else {
                tvUnit.setVisibility(View.INVISIBLE);
            }
            String hint = own.getHint();
            if (!TextUtils.isEmpty(hint)) {
                et.setHint(hint);
            }
            int line = own.getLine();
            if (line > 1) {
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(viewCreator.contentWidth, LayoutParams.WRAP_CONTENT);
//                params.height = (int) (line * et.getTextSize()) + 2 * viewCreator.titlePaddingY;
//                view.setLayoutParams(params);
                et.setLines(line);
//                et.setGravity(Gravity.TOP);

            } else if (line == 1) {
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(viewCreator.contentWidth, LayoutParams.WRAP_CONTENT);
//                params.height = et.getLineHeight() + 2 * viewCreator.titlePaddingY;

//                params.height = (int) (density * 50);
//                view.setLayoutParams(params);

//                et.setGravity(Gravity.CENTER_VERTICAL);

            }
        }
    }

    public interface OnTextChangeListener {
        void textChanged(String text);
    }
}
