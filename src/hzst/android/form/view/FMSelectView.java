package hzst.android.form.view;

import hzst.android.Constants;
import hzst.android.R;
import hzst.android.form.SelectSourceListActivity;
import hzst.android.form.ViewCreator;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.info.SelectSource;
import hzst.android.form.info.SelectViewInfo;
import hzst.android.util.L;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择控件。和{@link FMSpinner} {@link FMCheckGroup}不一样，
 * 它是从新的页面中选择一项或多项数据；并且数据源不是直接从{@link BaseViewInfo.SubmitValue}获取，
 * 而是通过调用{@link BaseViewInfo.SubmitValue}中的接口获取数据源列表。
 *
 * @author wt
 */
public class FMSelectView extends FMBaseView {

    private TextView tvTitle;
    private TextView tvContent;
    private RelativeLayout rlayAdd, rlayDelete;

    private SelectViewInfo info = new SelectViewInfo();
    private String ids;
    private String names;
//	private OnAddClickListener onAddClickListener;
    private OnSelectChangeListener onSelectChangeListener;
    private Class cActivity;

    public static int currIndex;
    private List<SelectSource> selectSources;

    public FMSelectView(Context context) {
        super(context);
        initView();
    }

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.onSelectChangeListener = onSelectChangeListener;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public void setNames(String names) {
        this.names = names;
        tvContent.setText(names);
    }

    public void setcActivity(Class cActivity) {
        this.cActivity = cActivity;
    }

    public List<SelectSource> getSelectSources() {
        return selectSources;
    }

    @Override
    public void setInfo(BaseViewInfo info, ViewCreator viewCreator) {
        super.setInfo(info, viewCreator);
        this.info = (SelectViewInfo) info;
        tvTitle.setText(viewCreator.spacingTitle(info.getName()));
        tvContent.setText(submitValues.get(0).getValue());
        if (info.isReadOnly()) {
            tvContent.setEnabled(false);
            rlayAdd.setVisibility(View.INVISIBLE);
            rlayDelete.setVisibility(View.INVISIBLE);

        }
    }

    protected void initView() {
        selectSources = new ArrayList<>();
        View view = LayoutInflater.from(context).inflate(R.layout.view_selectview, this);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        tvContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                BaseViewInfo.SubmitValue submitValue = info.getSubmitValues().get(0);
                String joint = "";
                if (submitValue.getJoint() != null) {
                    switch (submitValue.getJoint()) {
                        case BaseViewInfo.JOINT_SEMICOLON:
                            joint = Constants.SYMBOL_SEMICOLON;
                            break;
                        case BaseViewInfo.JOINT_COMMA:
                            joint = Constants.SYMBOL_COMMA;
                            break;
                    }
                }
                switch (info.getPassType()) {
                    case SelectViewInfo.PASS_TYPE_ALL:
                        viewCreator.setSubmitValue(getNames(), info, 0);
                        viewCreator.setSubmitValue(getIds(), info, 1);
                        break;
                    case SelectViewInfo.PASS_TYPE_NAME:
                        viewCreator.setSubmitValue(getNames(), info, 0);
                        break;
                    case SelectViewInfo.PASS_TYPE_ID:
//                            viewCreator.setSubmitValue(getIds(), info, 0);

                        StringBuilder sb = new StringBuilder();
                        for (SelectSource ss :
                                selectSources) {
                            sb.append(ss.getId());
                            sb.append(joint);
                        }
                        if (sb.length() > 0) {
                            sb.setLength(sb.length() - 1);
                        }
                        viewCreator.setSubmitValue(sb.toString(), info, 0);
                        break;
                }
                if(onSelectChangeListener != null && selectSources.size() > 0){
                    onSelectChangeListener.selectChange(selectSources);
                }
            }
        });
        rlayAdd = (RelativeLayout) view.findViewById(R.id.rlay_add);
        rlayDelete = (RelativeLayout) view.findViewById(R.id.rlay_delete);
        rlayAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                currIndex = (int) getTag();
                Intent intent = null;
                Bundle bundle = new Bundle();
                switch (info.getSourceType()) {
                    case SelectViewInfo.SOURCE_FOLLOW:

                        if (cActivity == null) {
                            intent = new Intent(context, SelectSourceListActivity.class);
                        } else {
                            intent = new Intent(context, cActivity);
                        }

                        intent.putExtra("viewInfo", info);
                        /**
                         * 选择数据列表页面，选择完成时回调
                         */
                        SelectSourceListActivity.onConfirmClickListener = new OnConfirmClickListener() {
                            @Override
                            public void confirm(List<SelectSource> selectSources) {
                                FMSelectView.this.selectSources = selectSources;
                                tvContent.setText(getNames());
                            }
                        };
                        break;
                    default:
                        if (cActivity == null) {
                            intent = new Intent(context, SelectSourceListActivity.class);
                        } else {
                            intent = new Intent(context, cActivity);
                        }
                        bundle.putSerializable("viewInfo", info);
                        intent.putExtras(bundle);
                        break;
                }
                if (intent != null) {
                    ((Activity) context).startActivityForResult(intent, 1);
                }
            }
        });
        rlayDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                clearContent();
                if(onSelectChangeListener != null){
                    onSelectChangeListener.selectChange(selectSources);
                }
            }
        });
    }

    public void clearContent(){
        selectSources.clear();
        tvContent.setText("");
        ids = "";
        names = "";
    }

    private String getNames() {
        StringBuilder sb = new StringBuilder();
        for (SelectSource source :
                selectSources) {
            sb.append(source.getItemName());
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    private String getIds() {
        StringBuilder sb = new StringBuilder();
        for (SelectSource source :
                selectSources) {
            sb.append(source.getId());
            sb.append(",");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public Object getContent() {
        return tvContent.getText();
    }

    @Override
    public void setContent(CharSequence content) {
        tvContent.setText(content);
    }

    public interface OnConfirmClickListener {
        void confirm(List<SelectSource> selectSources);
    }

    public interface OnSelectChangeListener{
        void selectChange(List<SelectSource> selectSources);
    }

}
