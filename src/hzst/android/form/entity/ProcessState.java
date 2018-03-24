package hzst.android.form.entity;

import java.io.Serializable;

/**
 * Created by wt on 2017/5/31.
 */
public class ProcessState implements Serializable{
    private String proposer;
    private String state;

    public ProcessState(String proposer,String state){
        this.proposer = proposer;
        this.state = state;
    }
    public String getProposer() {
        return proposer;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
