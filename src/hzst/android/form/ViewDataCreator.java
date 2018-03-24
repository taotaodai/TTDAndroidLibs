package hzst.android.form;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hzst.android.Constants;
import hzst.android.form.entity.DateViewOwn;
import hzst.android.form.entity.EditViewOwn;
import hzst.android.form.entity.Evaluate;
import hzst.android.form.entity.ProcessLink;
import hzst.android.form.entity.ProcessState;
import hzst.android.form.entity.PublicViewData;
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

/**
 * 表单控件数据生成类。
 * 当服务器返回的控件数据格式不符合我们的要求时，才会用它来创建表单控件。
 * Created by wt on 2017/4/12.
 */
public class ViewDataCreator implements Field, Constants {
    private JSONArray dataArray;
    private boolean isEditable;

    public ViewDataCreator(boolean isEditable) {
        this.isEditable = isEditable;
        dataArray = new JSONArray();
    }

    public void addTextView(BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        addTextView(obj, submitValue, publicViewData);
    }

    private void addTextView(JSONObject obj, BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData) {
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_TV);
            JSONArray submitArray = new JSONArray();
            JSONObject data1 = new JSONObject();
            data1.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
            data1.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
            data1.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
            submitArray.put(data1);
            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            addPublicData(obj, publicViewData);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addEditText(BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        addEditText(obj, submitValue, publicViewData);
    }

    public void addEditText(BaseViewInfo.SubmitValue submitValue, String inputType, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(EditViewInfo.FIELD_INPUT_TYPE, inputType);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        addEditText(obj, submitValue, publicViewData);
    }

    public void addEditText(BaseViewInfo.SubmitValue submitValue, EditViewOwn ownParams, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(EditViewInfo.FIELD_OWN, JsonDataParser.getJsonBean(EditViewOwn.class, ownParams));
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        addEditText(obj, submitValue, publicViewData);
    }

    public void addEditText(BaseViewInfo.SubmitValue submitValue, EditViewOwn ownParams, String inputType, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(EditViewInfo.FIELD_OWN, JsonDataParser.getJsonBean(EditViewOwn.class, ownParams));
            obj.put(EditViewInfo.FIELD_INPUT_TYPE, inputType);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        addEditText(obj, submitValue, publicViewData);
    }

    private void addEditText(JSONObject obj, BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData) {
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_ET);
            JSONArray submitArray = new JSONArray();
            JSONObject data1 = new JSONObject();
            data1.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
            data1.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
            data1.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
            submitArray.put(data1);
            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            addPublicData(obj, publicViewData);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addSpinner(List<BaseViewInfo.SubmitValue> submitValues, List<SelectableViewInfo.ListItem> selectData, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        addSpinner(obj, submitValues, selectData, publicViewData);
    }

    public void addSpinner(List<BaseViewInfo.SubmitValue> submitValues, List<SelectableViewInfo.ListItem> selectData, String allViewData, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(BaseViewInfo.FIELD_ALL_VIEW_DATA, allViewData);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        addSpinner(obj, submitValues, selectData, publicViewData);
    }

    private void addSpinner(JSONObject obj, List<BaseViewInfo.SubmitValue> submitValues, List<SelectableViewInfo.ListItem> selectData, PublicViewData publicViewData) {

        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_SP);
            JSONArray submitArray = new JSONArray();
            for (BaseViewInfo.SubmitValue submitValue :
                    submitValues) {
                JSONObject data = new JSONObject();
                data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
                data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
                data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
                submitArray.put(data);
            }

            JSONArray selectArray = new JSONArray();
            for (SelectableViewInfo.ListItem listItem :
                    selectData) {
                JSONObject data = new JSONObject();
                data.put(SelectableViewInfo.FEILD_TEXT, listItem.getText());
                data.put(SelectableViewInfo.FEILD_VALUE, listItem.getValue());
                selectArray.put(data);
            }
            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            obj.put(BaseViewInfo.FIELD_LIST_ITEMS, selectArray);
            addPublicData(obj, publicViewData);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addSelectView(List<BaseViewInfo.SubmitValue> submitValues, String sourceType,
                              String selectSource, String selectType, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_SV);
            JSONArray submitArray = new JSONArray();
            for (BaseViewInfo.SubmitValue submitValue :
                    submitValues) {
                JSONObject data = new JSONObject();
                data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
                data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
                data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
                data.put(BaseViewInfo.SUBMIT_JOINT, submitValue.getJoint());
                submitArray.put(data);
            }

            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            obj.put(SelectViewInfo.FIELD_SOURCE_TYPE, sourceType);
            obj.put(SelectViewInfo.FIELD_SELECT_SOURCE, selectSource);
            obj.put(SelectViewInfo.FIELD_SELECT_TYPE, selectType);
            addPublicData(obj, publicViewData);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addFormView(List<List<FormViewInfo.FormSubmitValue>> submitValues, boolean fissionable, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        addFormView(obj, submitValues, fissionable, publicViewData);
    }

    @Deprecated
    public void addFormView(List<List<FormViewInfo.FormSubmitValue>> submitValues, FormViewInfo.FRF frf, boolean fissionable, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(FormViewInfo.FIELD_FRF, JsonDataParser.getJsonBean(FormViewInfo.FRF.class, frf));
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        addFormView(obj, submitValues, fissionable, publicViewData);
    }

    private void addFormView(JSONObject obj, List<List<FormViewInfo.FormSubmitValue>> submitValues, boolean fissionable, PublicViewData publicViewData) {
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_FV);
            JSONArray submitArray = new JSONArray();

            for (List<FormViewInfo.FormSubmitValue> formValues :
                    submitValues) {
                for (FormViewInfo.FormSubmitValue submitValue :
                        formValues) {
                    JSONObject data = new JSONObject();
                    data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
                    data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
                    data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
                    data.put(FormViewInfo.SUBMIT_BROAD, submitValue.getBroadName());
                    data.put(FormViewInfo.SUBMIT_ITEM, submitValue.getItemName());
                    data.put(FormViewInfo.SUBMIT_INPUT_TYPE, submitValue.getInputType());
                    data.put(FormViewInfo.SUBMIT_MATH_EXPRESSION, submitValue.getExpression());
                    data.put(FormViewInfo.SUBMIT_ISFISSION, submitValue.isFission() ? 1 : 0);
                    submitArray.put(data);
                }
            }

            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            obj.put(FormViewInfo.FIELD_FISSIONABLE, fissionable ? 1 : 0);
            addPublicData(obj, publicViewData);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addAccessoryView(String accessoryList, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_AV);
            obj.put(BaseViewInfo.FIELD_ACCESSORY_LIST, accessoryList);
            JSONArray submitArray = new JSONArray();

            JSONObject data1 = new JSONObject();
            data1.put(BaseViewInfo.SUBMIT_FIELD, "");
            data1.put(BaseViewInfo.SUBMIT_VALUE, "");
            data1.put(BaseViewInfo.SUBMIT_TYPE, "");
            data1.put(BaseViewInfo.SUBMIT_JOINT, "");
            submitArray.put(data1);
            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray.toString());

            addPublicData(obj, publicViewData);

        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addProcessView(List<BaseViewInfo.SubmitValue> submitValues, List<List<ProcessLink>> links, int businessType, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            JSONArray array = new JSONArray();
            for (int i = 0; i < links.size(); i++) {

                JSONObject temp = new JSONObject();
                array.put(JsonDataParser.getJsonList(links.get(i)));

            }
            obj.put(ProcessViewInfo.FIELD_PROCESS_DATA, array);
//            obj.put(ProcessViewInfo.FIELD_PROCESS_DATA, JsonDataParser.getJsonList(links));
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        addProcessView(obj, submitValues, businessType, publicViewData);
    }

    public void addProcessView(List<BaseViewInfo.SubmitValue> submitValues, int businessType, List<List<ProcessLink>> links, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < links.size(); i++) {
                JSONObject linkObj = new JSONObject();

                linkObj.put(String.valueOf(i), JsonDataParser.getJsonList(links.get(i)));
                array.put(linkObj);
            }
            obj.put(ProcessViewInfo.FIELD_PROCESS_DATA, array.toString());
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        addProcessView(obj, submitValues, businessType, publicViewData);
    }

    private void addProcessView(JSONObject obj, List<BaseViewInfo.SubmitValue> submitValues, int businessType, PublicViewData publicViewData) {
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_PV);
            obj.put(BaseViewInfo.FIELD_NAME, "");
            JSONArray submitArray = new JSONArray();
            for (BaseViewInfo.SubmitValue submitValue :
                    submitValues) {
                JSONObject data = new JSONObject();
                data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
                data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
                data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
                submitArray.put(data);
            }

            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            obj.put(ProcessViewInfo.FIELD_BUSINESSTYPE, businessType);
            addPublicData(obj, publicViewData);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addDateSection(List<BaseViewInfo.SubmitValue> submitValues, String dateFormat, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        addDateSection(obj, submitValues, dateFormat, publicViewData);
    }

    public void addDateSection(List<BaseViewInfo.SubmitValue> submitValues, String dateFormat, String statisticsType, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(DateSectionViewInfo.FIELD_STATISTICS_TYPE, statisticsType);
        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        addDateSection(obj, submitValues, dateFormat, publicViewData);
    }

    private void addDateSection(JSONObject obj, List<BaseViewInfo.SubmitValue> submitValues, String dateFormat, PublicViewData publicViewData) {
        try {
            JSONArray submitArray = new JSONArray();
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_DS);

            for (BaseViewInfo.SubmitValue submitValue :
                    submitValues) {
                JSONObject data = new JSONObject();
                data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
                data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
                data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
                submitArray.put(data);

            }
            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            obj.put(DateSectionViewInfo.FIELD_DATE_FORMAT, dateFormat);
            addPublicData(obj, publicViewData);

        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addDateView(BaseViewInfo.SubmitValue submitValue, String dateFormat, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        addDateView(obj, submitValue, dateFormat, publicViewData);
    }

    public void addDateView(BaseViewInfo.SubmitValue submitValue, String dateFormat, PublicViewData publicViewData, DateViewOwn own) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(BaseViewInfo.FIELD_OWN, JsonDataParser.getJsonBean(DateViewOwn.class, own));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addDateView(obj, submitValue, dateFormat, publicViewData);
    }

    private void addDateView(JSONObject obj, BaseViewInfo.SubmitValue submitValue, String dateFormat, PublicViewData publicViewData) {
        try {
            JSONArray submitArray = new JSONArray();
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_DV);

            JSONObject data = new JSONObject();
            data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
            data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
            data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
            submitArray.put(data);

            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            obj.put(DateViewInfo.FIELD_DATE_FORMAT, dateFormat);
            addPublicData(obj, publicViewData);

        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addStateView(BaseViewInfo.SubmitValue submitValue, ProcessState state, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();
        try {
            JSONArray submitArray = new JSONArray();
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_STV);

            JSONObject data = new JSONObject();
            data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
            data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
            data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
            submitArray.put(data);

            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);
            obj.put(StateViewInfo.FIELD_PROCESS_STATE, JsonDataParser.getJsonBean(ProcessState.class, state));
            addPublicData(obj, publicViewData);

        } catch (JSONException e) {
            L.showLogInfo(L.TAG_EXCEPTION, e.toString());
        }
        dataArray.put(obj);
    }

    public void addRichEditor(BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData) {
        JSONObject obj = new JSONObject();

        addRichEditor(obj, submitValue, publicViewData);
    }

    public void addRichEditor(BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData, EditViewOwn ownParams) {
        JSONObject obj = new JSONObject();

        try {
            obj.put(EditViewInfo.FIELD_OWN, JsonDataParser.getJsonBean(EditViewOwn.class, ownParams));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addRichEditor(obj, submitValue, publicViewData);
    }

    private void addRichEditor(JSONObject obj, BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData) {
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_REV);

            JSONArray submitArray = new JSONArray();
            JSONObject data = new JSONObject();
            data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
            data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
            data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
            submitArray.put(data);
            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);

            addPublicData(obj, publicViewData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataArray.put(obj);
    }

    public void addCheckGroup(BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData, List<SelectableViewInfo.ListItem> listInfo) {
        JSONObject obj = new JSONObject();

        addCheckGroup(obj, submitValue, publicViewData, listInfo);

    }

    public void addCheckGroup(BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData, List<SelectableViewInfo.ListItem> listInfo, boolean isMultiple) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(SelectableViewInfo.FIELD_ISMULTIPLE, isMultiple);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addCheckGroup(obj, submitValue, publicViewData, listInfo);

    }


    private void addCheckGroup(JSONObject obj, BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData, List<SelectableViewInfo.ListItem> listInfo) {
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_CG);

            JSONArray submitArray = new JSONArray();
            JSONObject data = new JSONObject();
            data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
            data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
            data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
            submitArray.put(data);
            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);

            JSONArray array = new JSONArray();
            for (SelectableViewInfo.ListItem listItem :
                    listInfo) {
                JSONObject objItem = new JSONObject();
                objItem.put(SelectableViewInfo.FEILD_VALUE, listItem.getValue());
                objItem.put(SelectableViewInfo.FEILD_TEXT, listItem.getText());
                objItem.put(SelectableViewInfo.FEILD_ISCHECKED, listItem.isChecked());

                array.put(objItem);
            }

            obj.put(BaseViewInfo.FIELD_LIST_ITEMS, array);

            addPublicData(obj, publicViewData);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        dataArray.put(obj);
    }

    public void addEvaluateView(BaseViewInfo.SubmitValue submitValue, PublicViewData publicViewData, List<List<Evaluate>> dataList,boolean isInitial) {
        JSONObject obj = new JSONObject();
        try {
            obj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_EVALUATE);

            JSONArray submitArray = new JSONArray();
            JSONObject data = new JSONObject();
            data.put(BaseViewInfo.SUBMIT_FIELD, submitValue.getField());
            data.put(BaseViewInfo.SUBMIT_VALUE, submitValue.getValue());
            data.put(BaseViewInfo.SUBMIT_TYPE, submitValue.getValueType());
            submitArray.put(data);
            obj.put(BaseViewInfo.FIELD_SUBMIT_ITEMS, submitArray);

            JSONArray array = new JSONArray();
            for (List<Evaluate> list :
                    dataList) {
                array.put(JsonDataParser.getJsonList(list));
            }

            obj.put(EvaluateViewInfo.FIELD_EVALUATE, array.toString());
            obj.put(EvaluateViewInfo.FIELD_ISINITIAL,isInitial);

            addPublicData(obj, publicViewData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        dataArray.put(obj);

    }


    public String getViewData() {
        JSONObject taskObj = new JSONObject();
        try {
            taskObj.put(BaseViewInfo.FIELD_TYPE, ViewCreator.VIEW_TYPE_TASK);
            taskObj.put("field", APPROVE_ISEDITABLE);
            taskObj.put("value", isEditable ? TRUE : FALSE);
            dataArray.put(taskObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataArray.toString();
    }

    /**
     * 添加公共数据
     *
     * @param obj
     * @param publicViewData
     */
    private void addPublicData(JSONObject obj, PublicViewData publicViewData) {
        try {
            obj.put(BaseViewInfo.FIELD_NAME, publicViewData.getTitle());
            obj.put(BaseViewInfo.FIELD_MUST_FILL, publicViewData.isMustFill() ? 1 : 0);
            obj.put(BaseViewInfo.FIELD_READONLY, publicViewData.isReadOnly() ? 1 : 0);
            obj.put(BaseViewInfo.FIELD_MARGIN_TYPE, publicViewData.getMarginType());
            obj.put(BaseViewInfo.FIELD_ISNEED_TITLE, publicViewData.isNeedTitle() ? 1 : 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
