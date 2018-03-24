package hzst.android.form.view;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hzst.android.R;
import hzst.android.adapter.CommonAdapter;
import hzst.android.form.entity.ProcessLink;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.info.ProcessViewInfo;
import hzst.android.util.FontUtil;
import hzst.android.util.L;
import hzst.android.util.SharedPreferencesUtil;
import hzst.android.view.FlowLayout;
import hzst.android.view.ListViewForScrollView;

/**
 * Created by wt on 2017/3/10.
 * 流程控件
 */

public class FMProcessView extends FMBaseView {
    private EditText etOpinion;
    private Spinner spRejectMembers;
    private ListViewForScrollView lvLinks;
    private LinearLayout llayProcess = null;
    private FlowLayout flowlay = null;
    private int linksIndex = 0;//当前使用的流程

    private FontUtil fontUtil;
    private ProcessViewInfo viewInfo;

    public FMProcessView(Context context) {
        super(context);
        this.context = context;
    }

    public int getLinksIndex() {
        return linksIndex;
    }

    public void setLinksIndex(int linksIndex) {
        this.linksIndex = linksIndex;
        updateViews();
    }

    public void updateViews() {
        List<ProcessLink> dataList = (List<ProcessLink>) SharedPreferencesUtil.deepCopy(viewInfo.getProcessLinks().get(linksIndex));

        int status = ((ProcessViewInfo) info).getBusinessType();
        switch (status) {
            case ProcessViewInfo.BUSI_STATUS_ADD:
//                ProcessLink start = new ProcessLink("开始", ProcessLink.STATUS_START);
                ProcessLink end = new ProcessLink("结束", ProcessLink.STATUS_END);
//                dataList.add(0, start);
                dataList.add(end);

//                ViewGroup.MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(0, 0, 0, 0);

                ViewGroup.MarginLayoutParams paramsLlay = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

                LinearLayout.LayoutParams paramsArrow = new LinearLayout.LayoutParams(((int) density * 25), ((int) density * 6));
                paramsArrow.setMargins((int) (10 * density), 0, 0, 0);
                paramsArrow.gravity = Gravity.CENTER_VERTICAL;

                flowlay.removeAllViews();

                for (int i = 0; i < dataList.size(); i++) {
                    ProcessLink link = dataList.get(i);

                    if (!link.isHidden()) {
                        LinearLayout llayTv = new LinearLayout(context);
                        llayTv.setPadding(0, 0, 0, (int) (5 * density));
                        TextView tv = new TextView(context);

                        tv.setTextColor(context.getResources().getColor(R.color.text_white));
                        tv.setPadding((int) (5 * density), (int) (5 * density), (int) (5 * density), (int) (5 * density));
                        switch (link.getStatus()) {
                            case ProcessLink.STATUS_INITIATOR:
                                tv.setBackgroundResource(R.drawable.shape_link_start);
                                break;
                            case ProcessLink.STATUS_END:
                                tv.setBackgroundResource(R.drawable.shape_link_end);
                                break;
                            default:
                                tv.setBackgroundResource(R.drawable.shape_link_other);
                                break;
                        }

                        String name = link.getName();
                        if (!TextUtils.isEmpty(name)) {
                            tv.setText(name);
                        } else {
                            tv.setText(link.getDuty());
                        }
//                        tv.setLayoutParams(params);

                        llayTv.addView(tv);
                        flowlay.addView(llayTv);
                        if (i != dataList.size() - 1) {
                            LinearLayout llay = new LinearLayout(context);
                            llay.setLayoutParams(paramsLlay);
                            ImageView iv = new ImageView(context);
                            iv.setBackground(context.getResources().getDrawable(R.drawable.ic_workflow_arrow));
                            iv.setLayoutParams(paramsArrow);
                            llay.addView(iv);
                            flowlay.addView(llay);
                        }
                    }
                }
                break;

            default:
                createWokFlowView(status);
                break;

        }
    }

    @Override
    public void showView() {
        viewInfo = (ProcessViewInfo) info;
        llayProcess = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_fm_process_add, null);
        flowlay = (FlowLayout) llayProcess.findViewById(R.id.flowlay_process);

        updateViews();

        addView(llayProcess);

    }

    private List<ProcessLink> listLinks = new ArrayList<>();
    private CommonAdapter<ProcessLink> adapter;

    private void createWokFlowView(int status) {
        fontUtil = new FontUtil(context);
        llayProcess = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_fm_process, null);
        etOpinion = (EditText) llayProcess.findViewById(R.id.et_opinion);
        spRejectMembers = (Spinner) llayProcess.findViewById(R.id.sp_reject_members);
        lvLinks = (ListViewForScrollView) llayProcess.findViewById(R.id.lv_links);
        LinearLayout llayOpinion = (LinearLayout) llayProcess.findViewById(R.id.llay_opinion);
        LinearLayout llayReject = (LinearLayout) llayProcess.findViewById(R.id.llay_reject);
        switch (status) {
            case ProcessViewInfo.BUSI_STATUS_APPLY:
            case ProcessViewInfo.BUSI_STATUS_DONE:
                llayOpinion.setVisibility(View.GONE);
                llayReject.setVisibility(View.GONE);
                View vDivider1 = llayProcess.findViewById(R.id.v_divider_1);
                View vDivider2 = llayProcess.findViewById(R.id.v_divider_2);
                vDivider1.setVisibility(View.GONE);
                vDivider2.setVisibility(View.GONE);
                break;
            case ProcessViewInfo.BUSI_STATUS_TODO:
                List<BaseViewInfo.SubmitValue> submitValues = viewInfo.getSubmitValues();
                etOpinion.setHint("请填写意见");
                etOpinion.setText(submitValues.get(0).getValue());//意见
                etOpinion.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        viewCreator.setSubmitValue(etOpinion.getText().toString(), info, 0);
                    }
                });
                String tempMembers = submitValues.get(1).getValue();//驳回人选择
                final String states = submitValues.get(2).getValue();
//                spRejectMembers.setAdapter(new ArrayAdapter<>(context, R.layout.adapter_spinner_form_process, tempMembers.split(",")));
                spRejectMembers.setAdapter(new CommonAdapter<String>(context, Arrays.asList(tempMembers.split(",")), R.layout.adapter_spinner_form_process) {
                    @Override
                    public void convert(ViewHolder helper, String item, int position) {
                        helper.setText(R.id.tv_rm, item);
                    }

                    @Override
                    public View getDropDownView(int position, View convertView, ViewGroup parent) {
                        TextView tv = (TextView) super.getDropDownView(position, convertView, parent);
                        tv.setPadding((int) (5 * density), 0, 0, 0);
                        tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                        return tv;
                    }
                });
                spRejectMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        viewCreator.setSubmitValue(states.split(",")[position], info, 2);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
        }
        listLinks.clear();
        listLinks.addAll((List<ProcessLink>) SharedPreferencesUtil.deepCopy(viewInfo.getProcessLinks().get(linksIndex)));

        for (int i = 0; i < listLinks.size(); i++) {
            ProcessLink item = listLinks.get(i);
            if (item.isHidden()) {
                listLinks.remove(item);
                i--;
            }
        }
        if (adapter == null) {
            adapter = new CommonAdapter<ProcessLink>(context, listLinks, R.layout.adapter_fm_link_item) {
                @Override
                public void convert(ViewHolder helper, ProcessLink item, int position) {

                    if (TextUtils.isEmpty(item.getName())) {
                        helper.setText(R.id.tv_name, item.getDuty());
                    } else {
                        helper.setText(R.id.tv_name, item.getName());
                    }

                    TextView tvStatus = helper.getView(R.id.tv_link_status);
                    SpannableStringBuilder detail = fontUtil.getSpanString(item.getOpinion(), R.color.text_light_green, 16);
                    if (!"".equals(item.getResultName())) {
                        tvStatus.setText(detail.append(fontUtil.getSpanString("(" + item.getResultName() + ")", R.color.text_gray4, 16)));
                    } else {
                        tvStatus.setText(detail);
                    }

                    View vLineTop = helper.getView(R.id.v_line_top);
                    View vLineBottom = helper.getView(R.id.v_line_bottom);
                    if (position == listLinks.size() - 1) {
                        vLineTop.setVisibility(View.VISIBLE);
                        vLineBottom.setVisibility(View.GONE);
                    } else if (position == 0) {
                        vLineTop.setVisibility(View.GONE);
                        vLineBottom.setVisibility(View.VISIBLE);
                    } else {
                        vLineTop.setVisibility(View.VISIBLE);
                        vLineBottom.setVisibility(View.VISIBLE);
                    }
                    helper.setText(R.id.tv_operation_time, item.getOperateTime());
                    ImageView ivStatusSmall = helper.getView(R.id.iv_link_small);
                    ImageView ivStatus = helper.getView(R.id.iv_link_status);
                    switch (item.getStatus()) {
                        case ProcessLink.STATUS_INITIATOR://发起人
                            ivStatusSmall.setBackgroundResource(R.drawable.ic_link_done_s);
                            ivStatus.setBackgroundResource(R.drawable.ic_link_proposer);
                            tvStatus.setVisibility(View.VISIBLE);
                            break;
                        case ProcessLink.STATUS_FINISH://完成
                            ivStatusSmall.setBackgroundResource(R.drawable.ic_link_done_s);
                            ivStatus.setBackgroundResource(R.drawable.ic_link_done_b);
                            tvStatus.setVisibility(View.VISIBLE);
                            break;
                        case ProcessLink.STATUS_UNDERWAY://审核中
                            ivStatusSmall.setBackgroundResource(R.drawable.ic_link_doing_s);
                            ivStatus.setBackgroundResource(R.drawable.ic_link_doing_b);
                            tvStatus.setVisibility(View.VISIBLE);
                            break;
                        case ProcessLink.STATUS_WAIT_FOR://待审批
                            ivStatusSmall.setBackgroundResource(R.drawable.ic_link_todo_s);
                            ivStatus.setBackgroundResource(R.drawable.ic_link_todo_b);
                            tvStatus.setVisibility(View.GONE);
                            break;
                    }
                }
            };
            lvLinks.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

}
