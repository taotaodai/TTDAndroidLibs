package hzst.android.form.info;

import hzst.android.form.entity.DateViewOwn;

/**
 * Created by wt on 2017/4/27.
 */
public class DateViewInfo extends BaseViewInfo{

    private String dateFormat;

    private DateViewOwn own;

    public static final String FIELD_DATE_FORMAT = "dateFormat";

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DateViewOwn getOwn() {
        return own;
    }

    public void setOwn(DateViewOwn own) {
        this.own = own;
    }
}
