package hzst.android.form.view;

import hzst.android.R;
import hzst.android.form.ViewCreator;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.info.BaseViewInfo.SubmitValue;
import hzst.android.form.info.SelectableViewInfo;
import hzst.android.util.L;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class FMSpinner extends FMBaseView {
    private Spinner sp;
    private RelativeLayout rlayContent;
    private List<String> dataList;
    private OnItemCheckedListener onItemCheckedListener;
    private int selectedIndex;

    public FMSpinner(Context context) {
        super(context);
    }

    public void setOnItemCheckedListener(OnItemCheckedListener onItemCheckedListener) {
        this.onItemCheckedListener = onItemCheckedListener;
    }

    public int getPosition() {
        return selectedIndex;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setAdapter(BaseAdapter adapter) {
        sp.setAdapter(adapter);
    }

    @Override
    public void setInfo(BaseViewInfo info, ViewCreator viewCreator) {
        super.setInfo(info, viewCreator);
        dataList = ((SelectableViewInfo) info).getListText();
    }

    @Override
    public void showView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_fm_spinner, null);
        sp = (Spinner) view.findViewById(R.id.sp_form);
        ArrayAdapter<String> adapter = new MyArrayAdapter(context, R.layout.adapter_spinner_form, dataList);
        sp.setAdapter(adapter);

        final List<String> listValue = ((SelectableViewInfo) info).getListValue();
        final List<String> listText = ((SelectableViewInfo) info).getListText();

        String value = info.getSubmitValues().get(0).getValue();//下拉选只有一个value
        /*
         * 遍历value，选中与默认值相同的下标的选项
		 */
        for (int i = 0; i < listValue.size(); i++) {
            if (value.equals(listValue.get(i))) {
                selectedIndex = i;
                sp.setSelection(i);
                break;
            }
            if (value.equals(listText.get(i))) {
                selectedIndex = i;
                sp.setSelection(i);
                break;
            }
            if ("".equals(value)) {
                sp.setSelection(0);
                break;
            }
        }

        if (info.isReadOnly()) {
            view.setEnabled(false);
        }

        sp.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                TextView tv = (TextView) view;
//                tv.setTextColor(getResources().getColor(ViewCreator.TEXT_COLOR_CONTENT));
//				BaseViewInfo info = viewList.get((int) parent.getTag());
                /*
				 * 当选中一项时，获取下标对应的value，并保存修改
				 */
                List<SubmitValue> submitValues = info.getSubmitValues();
                String value = listValue.get(position);
                String text = listText.get(position);
                if (submitValues.size() == 2) {//当下拉选需要同时上传下标和值时
                    if (BaseViewInfo.VALUE_TYPE_SV.equals(submitValues.get(0).getValueType())) {
                        viewCreator.setSubmitValue(value, info, 0);
                        viewCreator.setSubmitValue(text, info, 1);
                    } else {
                        viewCreator.setSubmitValue(value, info, 1);
                        viewCreator.setSubmitValue(text, info, 0);
                    }
                } else {
                    viewCreator.setSubmitValue(value, info);
                }

                setPassBackData(position);
                ((SelectableViewInfo) info).setPosition(position);
//				viewCreator.updateViewInfo(info, true);
                if (onItemCheckedListener != null) {
                    onItemCheckedListener.onItemChecked(position, view);
                }
                selectedIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (info.isReadOnly()) {
            sp.setEnabled(false);
        }
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        view.setLayoutParams(params);
//        setLayoutParams(params);
        addView(view);
    }

    public void setSelection(int position){
        sp.setSelection(position);
    }

    private void setPassBackData(int position) {
        if (info.getAllViewData() != null) {
            for (int i = 0; i < submitValues.size(); i++) {
                SubmitValue submitValue = submitValues.get(i);
                try {
                    JSONObject obj = info.getAllViewData().getJSONObject(position);
                    if (obj.has(submitValue.getField())) {
                        viewCreator.setSubmitValue(obj.getString(submitValue.getField()), info, i);
                    }
                } catch (JSONException e) {
                    L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                }
            }
        }
    }

    @Override
    public void setContent(CharSequence content) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).equals(content)) {
                sp.setSelection(i);
                break;
            }
        }
    }

    public class MyArrayAdapter extends ArrayAdapter<String> {

        public MyArrayAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
            tv.setPadding((int) (5 * density), 0, 0, 0);
            tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            return tv;
        }
    }

    public interface OnItemCheckedListener {
        void onItemChecked(int position, View view);
    }
}
