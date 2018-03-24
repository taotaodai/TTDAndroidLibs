package hzst.android.form;

import hzst.android.Constants;
import hzst.android.entity.Accessory;
import hzst.android.entity.BaseUser;
import hzst.android.form.entity.DateViewOwn;
import hzst.android.form.entity.EditViewOwn;
import hzst.android.form.entity.ProcessState;
import hzst.android.form.info.AccessoryViewInfo;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.info.DateSectionViewInfo;
import hzst.android.form.info.DateViewInfo;
import hzst.android.form.info.EditViewInfo;
import hzst.android.form.info.EvaluateViewInfo;
import hzst.android.form.info.FormViewInfo;
import hzst.android.form.info.ProcessViewInfo;
import hzst.android.form.info.SelectViewInfo;
import hzst.android.form.info.SelectableViewInfo;
import hzst.android.form.info.StateViewInfo;
import hzst.android.util.JsonDataParser;
import hzst.android.util.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 解析控件json，用于后续动态创建表单。
 *
 * @author wt
 */
public class ViewInfoProcessor {
    private Map<String, String> parameters;

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public List<BaseViewInfo> getViewInfo(String json, BaseUser user) {
        List<BaseViewInfo> viewList = new ArrayList<>();
        Map<String, String> temp = new HashMap<>();
        try {
            JSONArray array = new JSONArray(json.trim());
            int parameterCount = 0;
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                BaseViewInfo info = new BaseViewInfo();
                String viewType = obj.getString(BaseViewInfo.FIELD_TYPE);

                try {
                    switch (viewType) {
                        case ViewCreator.VIEW_TYPE_TASK://任务相关数据(默认放在json数据最前面，会先被解析)
                            parameterCount++;
                            temp.put(obj.getString("field"), obj.getString("value"));
                            continue;
                        case ViewCreator.VIEW_TYPE_TABLE:
                            if (parameters == null) {
                                parameters = new HashMap<>();
                                parameters.putAll(temp);
                            }
                            //递归解析从表数据
                            info.setSlaveTableInfos(getViewInfo(obj.getString("value"), user));
                            //添加从表任务相关数据
                            info.setParameters(temp);
                            break;
                        case ViewCreator.VIEW_TYPE_SP:
                            info = new SelectViewInfo();
                        case ViewCreator.VIEW_TYPE_CG:
                            info = new SelectableViewInfo();
                            info.setListInfo(obj.getString(BaseViewInfo.FIELD_LIST_ITEMS));
                            try {
                                ((SelectableViewInfo)info).setIsMultiple(obj.getBoolean(SelectableViewInfo.FIELD_ISMULTIPLE));
                            } catch (JSONException e) {
                            }
                            break;
                        case ViewCreator.VIEW_TYPE_SV:
                            info = new SelectViewInfo();
                            ((SelectViewInfo) info).setSourceType(obj.getString(SelectViewInfo.FIELD_SOURCE_TYPE));
                            ((SelectViewInfo) info).setSelectSource(obj.getString(SelectViewInfo.FIELD_SELECT_SOURCE));
                            ((SelectViewInfo) info).setSelectType(obj.getString(SelectViewInfo.FIELD_SELECT_TYPE));
//                            ((SelectViewInfo) info).setSourceUrl(obj.getString(SelectViewInfo.FIEDL_SELECT_SOURCEURL));
                            break;
                        case ViewCreator.VIEW_TYPE_ET:
                            if (!(info instanceof EditViewInfo)) {
                                info = new EditViewInfo();
                            }
                            ((EditViewInfo) info).setInputType(JsonDataParser.parseJsonElement(obj, EditViewInfo.FIELD_INPUT_TYPE));
                            try {
                                ((EditViewInfo) info).setOwn(JsonDataParser.getBean(JsonDataParser.parseJsonElement(obj, BaseViewInfo.FIELD_OWN), EditViewOwn.class));
                            } catch (Exception e) {
                                L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                            }

                            break;
                        case ViewCreator.VIEW_TYPE_AV:
                            info.setVisible(true);
                            info = new AccessoryViewInfo();
                            ((AccessoryViewInfo) info).setAccessories(parseAccessory(obj.getString(BaseViewInfo.FIELD_ACCESSORY_LIST)));
                            break;
                        case ViewCreator.VIEW_TYPE_FV:
                            info = new FormViewInfo(obj.getString(BaseViewInfo.FIELD_SUBMIT_ITEMS));
                            ((FormViewInfo) info).setFissionable(Constants.TRUE.endsWith(obj.getString(FormViewInfo.FIELD_FISSIONABLE)));
                            try {
                                ((FormViewInfo) info).setFrf(JsonDataParser.getBean(JsonDataParser.parseJsonElement(obj, FormViewInfo.FIELD_FRF), FormViewInfo.FRF.class));
                            } catch (Exception e) {
                                L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                            }
                            break;
                        case ViewCreator.VIEW_TYPE_PV:
                            info = new ProcessViewInfo();
                            ((ProcessViewInfo) info).setBusinessType(obj.getInt(ProcessViewInfo.FIELD_BUSINESSTYPE));
                            ((ProcessViewInfo) info).setProcessData(obj.getString(ProcessViewInfo.FIELD_PROCESS_DATA));
                            break;
                        case ViewCreator.VIEW_TYPE_DS:
                            info = new DateSectionViewInfo();
                            ((DateSectionViewInfo) info).setDateFormat(obj.getString(DateSectionViewInfo.FIELD_DATE_FORMAT));
                            ((DateSectionViewInfo) info).setStatisticsType(JsonDataParser.parseJsonElement(obj, DateSectionViewInfo.FIELD_STATISTICS_TYPE));
                            break;
                        case ViewCreator.VIEW_TYPE_DV:
                            info = new DateViewInfo();
                            ((DateViewInfo) info).setDateFormat(obj.getString(DateSectionViewInfo.FIELD_DATE_FORMAT));
                            try {
                                ((DateViewInfo) info).setOwn(JsonDataParser.getBean(JsonDataParser.parseJsonElement(obj, BaseViewInfo.FIELD_OWN), DateViewOwn.class));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ViewCreator.VIEW_TYPE_STV:
                            info = new StateViewInfo();
                            try {
                                ((StateViewInfo) info).setProcessState(JsonDataParser.getBean(obj.getString(StateViewInfo.FIELD_PROCESS_STATE), ProcessState.class));
                            } catch (Exception e) {
                                L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                            }
                            break;
                        case ViewCreator.VIEW_TYPE_REV:
                            info = new EditViewInfo();
                            try {
                                ((EditViewInfo)info).setOwn(JsonDataParser.getBean(JsonDataParser.parseJsonElement(obj, BaseViewInfo.FIELD_OWN), EditViewOwn.class));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case ViewCreator.VIEW_TYPE_EVALUATE:
                            info = new EvaluateViewInfo();
                            ((EvaluateViewInfo)info).setIsInitial(obj.getBoolean(EvaluateViewInfo.FIELD_ISINITIAL));
                            ((EvaluateViewInfo)info).parseEvaluateData(obj);
                            break;

                    }

                    info.setMustFill(Constants.TRUE.equals(obj.getString(BaseViewInfo.FIELD_MUST_FILL)));
                    info.setName(obj.getString(BaseViewInfo.FIELD_NAME));
//                    info.setSpecial(Constants.TRUE.equals(obj.getString(FIELD_SPECIAL)));
                    info.setSubmitInfo(obj.getString(BaseViewInfo.FIELD_SUBMIT_ITEMS));
//                    info.setSpecialValue(obj.getString("specialValue"));
                    info.setIsNeedTitle(Constants.FALSE.equals(obj.getString(BaseViewInfo.FIELD_ISNEED_TITLE)) ? false : true);
                    info.setReadOnly(Constants.TRUE.equals(obj.getString(BaseViewInfo.FIELD_READONLY)));
                    info.setAllViewData(JsonDataParser.parseJsonElement(obj, BaseViewInfo.FIELD_ALL_VIEW_DATA));
                    info.setViewType(viewType);
                    try {
                        info.setMarginType(Integer.valueOf(JsonDataParser.parseJsonElement(obj, BaseViewInfo.FIELD_MARGIN_TYPE)));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    info.setIndex(i - parameterCount);
                } catch (JSONException e) {
                    L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                    L.showLogInfo(L.TAG_EXCEPTION, viewType + "控件缺少必要字段");
                }

                info.setUser(user);

                viewList.add(info);
            }
            if (parameters == null) {
                parameters = new HashMap<>();
                parameters.putAll(temp);
            }

        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        return viewList;
    }


    /**
     * 解析附件相关json
     *
     * @param json 附件json
     * @return 附件列表
     */
    private List<Accessory> parseAccessory(String json) {
        List<Accessory> accessories = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Accessory accessory = new Accessory();
                //"fileName":"","filePath":"","ownerId":","accessoryId":""
                accessory.setAccessoryId(JsonDataParser.parseJsonElement(obj, AccessoryViewInfo.AC_ACCESSORYID));
                accessory.setAttachmentName(JsonDataParser.parseJsonElement(obj, AccessoryViewInfo.AC_FILENAME));
                accessory.setAttachmentUrl(JsonDataParser.parseJsonElement(obj, AccessoryViewInfo.AC_FILEPATH));
                accessory.setOwnerId(JsonDataParser.parseJsonElement(obj, AccessoryViewInfo.AC_OWNERID));
                accessories.add(accessory);
            }
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        return accessories;
    }

    public static String parseJsonElement(JSONObject obj, String name) {
        if (obj.has(name)) {
            try {
                return obj.getString(name);
            } catch (JSONException e) {
                L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                return "";
            }
        }
        return "";
    }

}
