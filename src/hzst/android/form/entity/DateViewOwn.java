package hzst.android.form.entity;

import java.io.Serializable;

/**
 * Created by wt on 2017/6/30.
 */
public class DateViewOwn implements Serializable{
    private long minDate;
    private long maxDate;

    public DateViewOwn(long minDate,long maxDate){
        this.minDate = minDate;
        this.maxDate = maxDate;
    }
    public long getMinDate() {
        return minDate;
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public long getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }
}
