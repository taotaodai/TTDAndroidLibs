package hzst.android.form.entity;

import java.io.Serializable;

/**
 * Created by wt on 2017/5/31.
 */
public class EditViewOwn implements Serializable{
    private String unit;
    private String hint;
    private int line = 0;

    public EditViewOwn(){}
    public EditViewOwn(int line){
        this.line = line;
    }

    public EditViewOwn(int line,String hint){
        this.line = line;
        this.hint = hint;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
