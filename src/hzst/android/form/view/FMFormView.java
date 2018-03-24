package hzst.android.form.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hzst.android.R;
import hzst.android.form.ViewCreator;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.info.EditViewInfo;
import hzst.android.form.info.FormViewInfo;
import hzst.android.form.info.FormViewInfo.FormSubmitValue;
import hzst.android.util.FormulaCalculator;
import hzst.android.util.SharedPreferencesUtil;

/**
 * 表格控件。{@link hzst.android.R.drawable.ic_fm_sample_fv}
 * 由一个或多个小表格组成，
 * 每个小表格由若干个单元格组成，每个单元格由标题(名称)和若干项(名称对应的一些信息，可填写或者只读)。
 *
 * @author wt
 */
public class FMFormView extends FMBaseView {

    private LinearLayout rooView;
    private FormViewInfo info;
    private TextView tvAdd;
    private List<List<FormSubmitValue>> formValues;

    //    private List<FormSubmitValue> original;
    private List<FormSubmitValue> tempOriginal;//原始表格数据。用于表格的分裂。
    private OnContentChangedListener onContentChangedListener;
    private OnFormAddListener onFormAddListener;
    private OnFormDeleteListener onformDeleteListener;

    //    private int CELL_HEIGHT;//单元格高度
    private static final int MARGIN_TBLR = 1;
    private static final int MARGIN_R = 2;
    private static final int MARGIN_B = 3;
    private static final int MARGIN_RB = 4;
    private static final int MARGIN_NULL = 5;
    private static final int MARGIN_TOP = 6;

    private static final int CONTENT_FORM = 1;//表格标题
    private static final int CONTENT_BROAD = 2;//行标题
    private static final int CONTENT_CELL = 3;//单元格标题

    public FMFormView(Context context) {
        super(context);

//        CELL_HEIGHT = (int) (40 * density);
    }

    public void setOnFormAddListener(OnFormAddListener onFormAddListener) {
        this.onFormAddListener = onFormAddListener;
    }

    public OnContentChangedListener getOnContentChangedListener() {
        return onContentChangedListener;
    }

    public void setOnContentChangedListener(OnContentChangedListener onContentChangedListener) {
        this.onContentChangedListener = onContentChangedListener;
    }

    public void setOnformDeleteListener(OnFormDeleteListener onformDeleteListener) {
        this.onformDeleteListener = onformDeleteListener;
    }

    public List<List<FormSubmitValue>> getFormValues() {
        return formValues;
    }

    @Override
    public void setInfo(BaseViewInfo info, ViewCreator viewCreator) {
        super.setInfo(info, viewCreator);
        this.info = (FormViewInfo) info;
        formValues = this.info.getFormValues();
    }

    /**
     * 用于记录最后一个单元格的下标
     */
    private int index = 0;

    @Override
    protected void initView() {

        rooView = createLinearLayout(LinearLayout.VERTICAL, getLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, -1, MARGIN_NULL));
        for (int i = 0; i < formValues.size(); i++) {
            List<FormSubmitValue> formSource = formValues.get(i);
            rooView.addView(CreateSingleForm(formSource, i));
        }
        addView(rooView);
    }

    private View CreateSingleForm(List<FormSubmitValue> formSource, int formPosition) {
//        LinearLayout llay = createLinearLayout(LinearLayout.HORIZONTAL, getLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, -1, MARGIN_TBLR));
        LinearLayout llay = createLinearLayout(LinearLayout.VERTICAL, getLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, -1, MARGIN_NULL));
        createTitle(llay, formSource.get(0));
//        LinearLayout llayMiddle = createLinearLayout(LinearLayout.VERTICAL, getLayoutParams(ViewCreator.contentWidth, LayoutParams.WRAP_CONTENT, -1, MARGIN_NULL));
        LinearLayout llayMiddle = createLinearLayout(LinearLayout.VERTICAL, getLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, -1, MARGIN_TOP));
        llayMiddle.setFocusableInTouchMode(true);
        for (int j = 0; j < formSource.size(); j++) {
            FormSubmitValue sv = formSource.get(j);
            sv.setFormPosition(formPosition);
            sv.setCellPosition(index);

            addInnerCell(llayMiddle, sv, false, index);
//            if (j == formSource.size() - 1) {
//                addInnerCell(llayMiddle, sv, true, index);
//            } else {
//                addInnerCell(llayMiddle, sv, false, index);
//            }
            index++;
        }
        llay.addView(llayMiddle);
        return llay;
    }

    @Override
    public void updateViews() {
        removeAllViews();
        initView();
        if (info.isFissionable() && !info.isReadOnly()) {
//            original = new ArrayList<>(Arrays.asList(new FormSubmitValue[formValues.get(0).size()]));
//            Collections.copy(original, formValues.get(0));//克隆原原始表格数据
            List<FormSubmitValue> tempFormValue = (List<FormSubmitValue>) SharedPreferencesUtil.deepCopy(formValues.get(formValues.size() - 1));
            for (FormSubmitValue formSv :
                    tempFormValue) {
                if (!FormViewInfo.VALUE_TYPE_HIDE.equals(formSv.getValueType())) {
                    formSv.setValue("");
                }
            }
            //取最后一个表格作为分裂原
            tempOriginal = (List<FormSubmitValue>) SharedPreferencesUtil.deepCopy(tempFormValue);

            LinearLayout.LayoutParams paramsAdd = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            tvAdd = new TextView(context);
            tvAdd.setLayoutParams(paramsAdd);
            tvAdd.setText("添加");
            tvAdd.setTextColor(getResources().getColor(R.color.bg_form_keynote));
            tvAdd.setGravity(Gravity.CENTER);
            tvAdd.setPadding((int) (density * 5), (int) (density * 5), (int) (density * 5), (int) (density * 5));
            tvAdd.setTextSize(18);
            tvAdd.setBackgroundResource(ViewCreator.COLOR_CONTENT);

            tvAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<FormSubmitValue> original = copyFormData();
                    formValues.add(original);
                    submitValues.addAll(original);
                    rooView.addView(CreateSingleForm(original, formValues.size() - 1), (formValues.size() - 1));
                    if (onFormAddListener != null) {
                        onFormAddListener.formAdded();
                    }

                }
            });
            rooView.addView(tvAdd);
        }
    }

    @Override
    public void showView() {
        updateViews();
    }

    public String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    private List<FormSubmitValue> copyFormData() {
        List<FormSubmitValue> original = (List<FormSubmitValue>) SharedPreferencesUtil.deepCopy(tempOriginal);
        for (int i = 0; i < original.size(); i++) {
            FormSubmitValue submitValue = original.get(i);
            FormSubmitValue temp = tempOriginal.get(i);
            submitValue.setIsFission(true);
//            String field = submitValue.getField();
            String broad = submitValue.getBroadName();
//            int fieldNum = Integer.valueOf(getNumbers(field));
            int broadNum = Integer.valueOf(getNumbers(broad));
//            if (!TextUtils.isEmpty(field)) {
//                String fn = String.valueOf(fieldNum + 1);
//                submitValue.setField(field.replace(String.valueOf(fieldNum), fn));
//                temp.setField(field.replace(String.valueOf(fieldNum), fn));
//            }
            if (!TextUtils.isEmpty(broad)) {
                String bn = String.valueOf(broadNum + 1);
                submitValue.setBroadName(broad.replace(String.valueOf(broadNum), bn));
                temp.setBroadName(broad.replace(String.valueOf(broadNum), bn));
            }
        }
        return original;
    }


    /**
     * 创建表格行标题
     *
     * @param llay
     * @param formSubmitValue
     */
    private void createTitle(LinearLayout llay, final FormSubmitValue formSubmitValue) {

        LinearLayout llayTitle = createLinearLayout(LinearLayout.HORIZONTAL, getLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, -1, MARGIN_R));
        llayTitle.setPadding(viewCreator.titlePaddingX, (int) (5 * density), (int) (5 * density), (int) (5 * density));
        llayTitle.setBackgroundColor(getResources().getColor(R.color.bg_form_theme));
        TextView tvTitle = new TextView(context);
        tvTitle.setLayoutParams(getLayoutParams(0, LayoutParams.WRAP_CONTENT, 1, MARGIN_NULL));

        tvTitle.setEnabled(true);
        tvTitle.setTextSize(16);
        tvTitle.setTextColor(viewCreator.titleTextColor);
        tvTitle.setText(formSubmitValue.getBroadName());
        llayTitle.addView(tvTitle);

        if (formSubmitValue.isFission() && !info.isReadOnly()) {
            TextView tvDelete = new TextView(context);
            tvDelete.setText("删除");
            tvDelete.setTextSize(16);
            tvDelete.setTextColor(getResources().getColor(R.color.bg_form_keynote));
            llayTitle.addView(tvDelete);
            tvDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int fPosition = formSubmitValue.getFormPosition();
                    int cPosition = formSubmitValue.getCellPosition();
                    formValues.remove(fPosition);
                    /**
                     * 重新排列下标
                     */
                    index = 0;
                    for (int i = 0; i < formValues.size(); i++) {
                        for (int j = 0; j < formValues.get(i).size(); j++) {
                            FormSubmitValue fv = formValues.get(i).get(j);
                            fv.setFormPosition(i);
                            fv.setCellPosition(index);
                            index++;
                        }
                    }
//                    for (int i = 0; i < (tempOriginal.size() - 1); i++) {
//                        submitValues.remove(cPosition);
//                    }

                    rooView.removeViewAt(fPosition);

                    if(onformDeleteListener != null){
                        onformDeleteListener.formDeleted(fPosition);
                    }
                }
            });
        }

        llay.addView(llayTitle);
    }

    private void addInnerCell(LinearLayout llayMiddle, FormSubmitValue submitValue, boolean isLast, int index) {

        int margin = MARGIN_B;
        if (isLast || info.isReadOnly()) {
            margin = MARGIN_NULL;
        }
        LinearLayout llayInner = createLinearLayout(LinearLayout.HORIZONTAL, getLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, -1, margin));
        if (BaseViewInfo.VALUE_TYPE_HIDE.equals(submitValue.getValueType())) {
            llayInner.setVisibility(View.GONE);
        } else {
            /**
             * 创建小标题
             */
            View innerTitle;
            if (isLast) {
                innerTitle = createCell(submitValue, getLayoutParams(viewCreator.titleWidth, LayoutParams.MATCH_PARENT, -1, MARGIN_R), ViewCreator.COLOR_CONTENT, 0, BaseViewInfo.VALUE_TYPE_TEXT, CONTENT_BROAD);
            } else {
                innerTitle = createCell(submitValue, getLayoutParams(viewCreator.titleWidth, LayoutParams.MATCH_PARENT, -1, MARGIN_RB), ViewCreator.COLOR_CONTENT, 0, BaseViewInfo.VALUE_TYPE_TEXT, CONTENT_BROAD);
            }
            llayInner.addView(innerTitle);

            /**
             * 创建标题对应控件
             */
            View tvContent;
            if (isLast) {
                tvContent = createCell(submitValue, getLayoutParams(viewCreator.contentWidth, LayoutParams.WRAP_CONTENT, -1, MARGIN_NULL), ViewCreator.COLOR_CONTENT, index, submitValue.getValueType(), CONTENT_CELL);
            } else {
                tvContent = createCell(submitValue, getLayoutParams(viewCreator.contentWidth, LayoutParams.WRAP_CONTENT, -1, info.isReadOnly() ? MARGIN_NULL : MARGIN_B), ViewCreator.COLOR_CONTENT, index, submitValue.getValueType(), CONTENT_CELL);
            }
            llayInner.setBackgroundResource(ViewCreator.COLOR_CONTENT);
            llayInner.addView(tvContent);
        }
        llayMiddle.addView(llayInner);

    }

    private LinearLayout.LayoutParams getLayoutParams(int width, int height, int weight, int margin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        if (weight >= 0) {
            params.weight = weight;
        }
        int divider = (int) (0.5 * density);
        switch (margin) {
            case MARGIN_TBLR:
                params.setMargins(divider, divider, divider, divider);
                break;
            case MARGIN_R:
                params.setMargins(0, 0, divider, 0);
                break;
            case MARGIN_B:
                params.setMargins(0, 0, 0, divider);
                break;
            case MARGIN_RB:
                params.setMargins(0, 0, divider, divider);
                break;
            case MARGIN_TOP:
                params.setMargins(0, divider, 0, 0);
                break;
        }
        return params;
    }

    private LinearLayout createLinearLayout(int orientation, LinearLayout.LayoutParams params) {
        LinearLayout llay = new LinearLayout(context);
        llay.setOrientation(orientation);
        llay.setLayoutParams(params);
        llay.setBackgroundColor(getResources().getColor(R.color.divider_general));
        return llay;
    }

    /**
     * @param formSubmitValue
     * @param params
     * @param backRes
     * @param index
     * @param type
     * @return
     */
    private View createCell(final FormSubmitValue formSubmitValue, LinearLayout.LayoutParams params, int backRes, final int index, String type, int titleType) {
        String content = "";
        switch (titleType) {
            case CONTENT_FORM:
                content = formSubmitValue.getBroadName();
                break;
            case CONTENT_BROAD:
                content = viewCreator.spacingTitle(formSubmitValue.getItemName());
                break;
            case CONTENT_CELL:
                content = formSubmitValue.getValue();
                break;

        }
        View cell = null;
        switch (type) {
            case BaseViewInfo.VALUE_TYPE_TEXT:
                cell = new TextView(context);
                cell.setBackgroundResource(backRes);
                ((TextView) cell).setGravity(Gravity.BOTTOM);
                ((TextView) cell).setText(content);
                ((TextView) cell).setTextSize(ViewCreator.TEXT_SIZE_CONTENT);
                if (titleType == CONTENT_CELL) {
                    ((TextView) cell).setTextColor(viewCreator.contentTextColor);

                    setTextGravity(!viewCreator.isEditable, ((TextView) cell));
                } else {
                    ((TextView) cell).setTextColor(viewCreator.titleTextColor);
                }
                cell.setLayoutParams(params);
                if (titleType == CONTENT_BROAD) {
                    cell.setPadding(viewCreator.titlePaddingX, (int) (5 * density), (int) (5 * density), (int) (5 * density));
                } else if (titleType == CONTENT_CELL) {
                    cell.setPadding(0, (int) (5 * density), viewCreator.titlePaddingX, (int) (5 * density));
                } else {
                    cell.setPadding(0, (int) (5 * density), (int) (5 * density), (int) (5 * density));
                }

//			tv.setWidth((int) (60*density*weight));
//			tv.setMinHeight((int) (40*weight*density));
//			tv.setBackgroundColor(Color.WHITE);
//			tv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                ((TextView) cell).setMovementMethod(ScrollingMovementMethod.getInstance());
                break;
            case BaseViewInfo.VALUE_TYPE_EDIT:
                cell = new EditText(context);
                switch (formSubmitValue.getInputType()) {
                    case FormViewInfo.INPUT_NUM:
                        ((EditText) cell).setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    case EditViewInfo.INPUT_DECIMAL:
                        EditViewInfo.setInputType(((EditText) cell), EditViewInfo.INPUT_DECIMAL);
                        break;
                }
                ((EditText) cell).setBackground(null);
                ((EditText) cell).setGravity(Gravity.BOTTOM);
                ((EditText) cell).setTextColor(viewCreator.contentTextColor);
                ((EditText) cell).setText(content);

                setTextGravity(!viewCreator.isEditable, ((TextView) cell));
                cell.setOnFocusChangeListener(new OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        setTextGravity(hasFocus, (TextView) v);
                    }
                });
                cell.setLayoutParams(params);
                cell.setPadding(0, (int) (5 * density), viewCreator.titlePaddingX, (int) (5 * density));

                break;
            case BaseViewInfo.VALUE_TYPE_ST:
            case BaseViewInfo.VALUE_TYPE_SV:

                break;
            case "Check":
                cell = new CheckBox(context);
                ((CheckBox) cell).setText(content);
                break;

        }
        if (titleType == CONTENT_CELL && cell instanceof TextView) {
            ((TextView) cell).addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String expression = formSubmitValue.getExpression();

                    formSubmitValue.setValue(s.toString());
                    executeFrf(formSubmitValue);
                    if (!TextUtils.isEmpty(expression)) {
                        executeExpression(expression, formSubmitValue.getFormPosition());
                    }
                    if (onContentChangedListener != null) {
                        onContentChangedListener.contentChange(formSubmitValue, index);
                    }
                }
            });
        }

        return cell;
    }

    private void executeFrf(FormSubmitValue formSubmitValue) {
        FormViewInfo.FRF frf = info.getFrf();
        if (frf != null) {
            BaseViewInfo.SubmitValue submitValue = info.new SubmitValue();
            submitValue.setField(frf.getField());
            if (FormViewInfo.FRF.CALCULATE_SUM.equals(frf.getCalculateType())) {
                int sum = 0;
                if (formSubmitValue.getItemName().equals(frf.getTargetName())) {
                    for (int i = 0; i < formValues.size(); i++) {
                        TextView tv = (TextView) getCell(i, frf.getTargetIndex());
                        String s = tv.getText().toString();
                        int num = 0;
                        if (!TextUtils.isEmpty(s)) {
                            num = Integer.valueOf(s);
                        }
                        sum = sum + num;
                    }
                    submitValue.setValue(String.valueOf(sum));
                    info.setFrfData(submitValue);
                }
            }
        }
    }

    /**
     * 执行公式。这里只能进行四则运算
     *
     * @param expression
     * @param formPosition
     */
    private void executeExpression(String expression, int formPosition) {
        String[] array = expression.split("=");
        TextView tv = null;
        String result = null;
        List<FormSubmitValue> temp = formValues.get(formPosition);
        for (int i = 0; i < temp.size(); i++) {
            FormSubmitValue value = temp.get(i);
            if (array[0].contains(value.getItemName())) {
                if (result == null) {
                    result = array[0].replaceAll(value.getItemName(), value.getValue());
                } else {
                    result = result.replaceAll(value.getItemName(), value.getValue());
                }
            }
            if (array[1].equals(value.getItemName())) {
                tv = (TextView) getCell(formPosition, i);
                break;
            }
        }
        if (tv != null) {
            tv.setText(String.valueOf(FormulaCalculator.formatDoubleX(FormulaCalculator.getResult(result), 2)));
        }
    }

    private void setTextGravity(boolean hasFocus, TextView tv) {
        if (hasFocus) {
            tv.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        } else {
            tv.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        }
    }


    /**
     * @param formPosition
     * @param cellPosition
     * @return
     */
    public View getCell(int formPosition, int cellPosition) {
        return ((LinearLayout) ((LinearLayout) ((LinearLayout) rooView.getChildAt(formPosition)).getChildAt(1)).getChildAt(cellPosition)).getChildAt(1);
    }

    public interface OnContentChangedListener {
        void contentChange(FormSubmitValue submitValue, int index);
    }

    public interface OnFormAddListener {
        void formAdded();
    }

    public interface OnFormDeleteListener {
        void formDeleted(int index);
    }


}
