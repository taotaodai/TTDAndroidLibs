package hzst.android.form.info;

import hzst.android.form.entity.ProcessState;

/**
 * Created by wt on 2017/5/31.
 */
public class StateViewInfo extends BaseViewInfo{
    private ProcessState processState;

    public static final String FIELD_PROCESS_STATE = "processState";

    public ProcessState getProcessState() {
        return processState;
    }

    public void setProcessState(ProcessState processState) {
        this.processState = processState;
    }
}
