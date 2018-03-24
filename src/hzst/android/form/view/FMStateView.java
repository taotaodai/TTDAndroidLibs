package hzst.android.form.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import hzst.android.R;
import hzst.android.form.entity.ProcessState;
import hzst.android.form.info.StateViewInfo;

/**
 * Created by wt on 2017/5/27.
 */
public class FMStateView extends FMBaseView {
    private StateViewInfo stateViewInfo;

    private TextView tvProposer;
    private TextView tvState;

    public FMStateView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        stateViewInfo = (StateViewInfo) info;
        View view = LayoutInflater.from(context).inflate(R.layout.view_fm_state, null);
        LinearLayout llayContent = (LinearLayout) view.findViewById(R.id.llay_content);
        llayContent.setPadding(viewCreator.titlePaddingX, (int) (5 * density), viewCreator.titlePaddingX, (int) (5 * density));
        tvProposer = (TextView) view.findViewById(R.id.tv_proposer);
        tvState = (TextView) view.findViewById(R.id.tv_state);
        ProcessState state = stateViewInfo.getProcessState();
        tvProposer.setText(state.getProposer());
        tvState.setText("状态：" + state.getState());

        addView(view);
    }

    @Override
    public void showView() {
        initView();
    }
}
