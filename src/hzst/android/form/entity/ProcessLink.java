package hzst.android.form.entity;

import java.io.Serializable;

/**
 * Created by wt on 2017/3/10.
 * 流程环节类
 */
public class ProcessLink implements Serializable {
    private int step;//环节位于第几步
    private String name;
    private String duty;//职务
    private String dutyId;//职务id，主要用于判断流程中某个特定步骤。
    private String statusDetail;
    private String resultName;
    private String operateTime;
    private String opinion;
    private int status;
    private boolean isHidden;

    public static final int STATUS_START = 0;
    public static final int STATUS_END = -1;//结束
    public static final int STATUS_INITIATOR = 1;//发起人
    public static final int STATUS_FINISH = 2;//已完成
    public static final int STATUS_UNDERWAY = 3;//正在审核
    public static final int STATUS_WAIT_FOR = 4;//待审核

    public ProcessLink(){}
    public ProcessLink(String name,int status){
        this.name = name;
        this.status = status;
    }
    public ProcessLink(String name,int status,String opinion,String resultName){
        this.name = name;
        this.status = status;
        this.opinion = opinion;
        this.resultName = resultName;
    }

    public ProcessLink(String name,int status,String opinion,String resultName,String operateTime){
        this.name = name;
        this.status = status;
        this.opinion = opinion;
        this.resultName = resultName;
        this.operateTime = operateTime;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setIsHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {

        return name;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public String getResultName() {
        if (resultName == null) {
            return "";
        }
        return resultName;
    }

    public String getOperateTime() {
        if (operateTime == null) {
            return "";
        }
        return operateTime;
    }

    public int getStatus() {
        return status;
    }

    public String getOpinion() {
        if (opinion == null) {
            return "";
        }
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
