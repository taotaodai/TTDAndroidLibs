package hzst.android.form.view;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import java.util.List;

import hzst.android.form.info.SelectableViewInfo;
import hzst.android.view.FlowLayout;

/**
 * 多选框组
 *
 * @author wt
 */
public class FMCheckGroup extends FMBaseView {

    private List<SelectableViewInfo.ListItem> items;

    public FMCheckGroup(Context context) {
        super(context);
    }


    @Override
    public void showView() {

        String[] selectedList = info.getSubmitValues().get(0).getValue().split(",");

        items = ((SelectableViewInfo) info).getListItems();

        final FlowLayout flay = new FlowLayout(context);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(viewCreator.titlePaddingX, 0, 0, 0);
        flay.setLayoutParams(params);
//        flay.setPadding(0, 0, viewCreator.titlePaddingX, 0);

        if(((SelectableViewInfo) info).isMultiple()){
            for (int i = 0; i < items.size(); i++) {
                SelectableViewInfo.ListItem item = items.get(i);
                CheckBox cb = new CheckBox(context);
                cb.setText(item.getText());
                cb.setTextColor(viewCreator.contentTextColor);
                cb.setTag(item);

                for (String selected :
                        selectedList) {
                    if(selected.equals(item.getText())){
                        cb.setChecked(true);
                        item.setIsChecked(true);
                    }
                }

                flay.addView(cb);

                if (info.isReadOnly()) {
                    cb.setEnabled(false);
                    continue;
                }

                cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SelectableViewInfo.ListItem checkedItem = (SelectableViewInfo.ListItem) buttonView.getTag();
                        checkedItem.setIsChecked(isChecked);
                        StringBuilder sb = new StringBuilder();
                        for (SelectableViewInfo.ListItem item :
                                items) {
                            if (item.isChecked()) {
                                sb.append(item.getValue());
                                sb.append(",");
                            }
                        }
                        if (sb.length() > 0) {
                            sb.setLength(sb.length() - 1);
                        }
                        viewCreator.setSubmitValue(sb.toString(), info);
                    }
                });
            }
        }else {
            for (int i = 0; i < items.size(); i++) {
                SelectableViewInfo.ListItem item = items.get(i);
                RadioButton rb = new RadioButton(context);
                rb.setText(item.getText());
                rb.setTextColor(viewCreator.contentTextColor);
                rb.setTag(item);

                for (String selected :
                        selectedList) {
                    if(selected.equals(item.getText())){
                        rb.setChecked(true);
                        item.setIsChecked(true);
                    }
                }

                flay.addView(rb);

                if (info.isReadOnly()) {
                    rb.setEnabled(false);
                    continue;
                }

                rb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            int rbCount = flay.getChildCount();
                            for (int i = 0; i <rbCount; i++) {
                                View v = flay.getChildAt(i);
                                if(v instanceof RadioButton && v != buttonView){
                                    ((RadioButton)v).setChecked(false);
                                    items.get(i).setIsChecked(false);
                                }
                            }
                            SelectableViewInfo.ListItem checkedItem = (SelectableViewInfo.ListItem) buttonView.getTag();
                            checkedItem.setIsChecked(true);
                            StringBuilder sb = new StringBuilder();
                            for (SelectableViewInfo.ListItem item :
                                    items) {
                                if (item.isChecked()) {
                                    sb.append(item.getValue());
                                    sb.append(",");
                                }
                            }
                            if (sb.length() > 0) {
                                sb.setLength(sb.length() - 1);
                            }
                            viewCreator.setSubmitValue(sb.toString(), info);
                        }
                    }
                });
            }
        }

        addView(flay);
    }

    @Override
    public void updateViews() {
        viewCreator.setSubmitValue("", info);
        removeAllViews();
        showView();
    }
}
