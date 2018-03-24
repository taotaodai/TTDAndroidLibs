package hzst.android.form.info;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hzst.android.form.entity.ProcessLink;
import hzst.android.util.JsonDataParser;
import hzst.android.util.L;

/**
 * 流程控件数据
 * Created by wt on 2017/3/10.
 */
public class ProcessViewInfo extends BaseViewInfo{
    private List<List<ProcessLink>> processLinks;//流程环节
    private String processData;

    public static final String FIELD_PROCESS_DATA = "processData";
    public static final String FIELD_BUSINESSTYPE = "businessType";

    protected int businessType = BUSI_STATUS_ADD;

    public static final int BUSI_STATUS_ADD = 1;
    public static final int BUSI_STATUS_APPLY = 2;
    public static final int BUSI_STATUS_TODO = 3;
    public static final int BUSI_STATUS_DONE = 4;
//    public static final int BUSI_STATUS_GETBACK = 5;


    public void setProcessData(String processData) {
        this.processData = processData;
        switch (businessType){
            case BUSI_STATUS_ADD:
                int linkNum = 1;
                processLinks = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(processData);
                    for (int i = 0; i <array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        for (int j = 0; j < linkNum; j++) {
                            if(obj.has(String.valueOf(j))){
                                List<ProcessLink> li = new ArrayList<>();
                                JsonDataParser.getBeanList(obj.getString(String.valueOf(j)), ProcessLink[].class, li);
                                processLinks.add(li);
                                linkNum ++;
                            }
                        }
                    }

                } catch (JSONException e) {
                    L.showLogInfo(L.TAG_EXCEPTION,e.toString());
                }
                break;
            case BUSI_STATUS_APPLY:
            case BUSI_STATUS_TODO:
            case BUSI_STATUS_DONE:
                processLinks = new ArrayList<>();
                try {
                    JSONArray array = new JSONArray(processData);
                    for (int i = 0; i <array.length(); i++) {
                        List<ProcessLink> link = new ArrayList<>();
                        JsonDataParser.getBeanList(array.get(i).toString(), ProcessLink[].class, link);
                        processLinks.add(link);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    public List<List<ProcessLink>> getProcessLinks() {
        return processLinks;
    }

    public void setProcessLinks(List<List<ProcessLink>> processLinks) {
        this.processLinks = processLinks;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }


}
