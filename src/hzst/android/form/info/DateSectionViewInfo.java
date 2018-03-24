package hzst.android.form.info;

/**
 * Created by wt on 2017/4/25.
 */
public class DateSectionViewInfo extends DateViewInfo{

    private String statisticsType;//时间统计类型

    public static final String FIELD_STATISTICS_TYPE = "statisticsType";

    public static final String STATISTICS_HOUR = "hour";
    public static final String STATISTICS_DAY = "day";


    public String getStatisticsType() {
        return statisticsType;
    }

    public void setStatisticsType(String statisticsType) {
        this.statisticsType = statisticsType;
    }

}
