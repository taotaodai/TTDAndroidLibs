package hzst.android.form;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hzst.android.Constants;
import hzst.android.R;
import hzst.android.entity.BaseUser;
import hzst.android.form.entity.ViewCollection;
import hzst.android.form.info.BaseViewInfo;
import hzst.android.form.info.BaseViewInfo.SpecialValue;
import hzst.android.form.info.BaseViewInfo.SubmitValue;
import hzst.android.form.info.EditViewInfo;
import hzst.android.form.info.SelectableViewInfo;
import hzst.android.form.view.FMAccessoryView;
import hzst.android.form.view.FMBaseView;
import hzst.android.form.view.FMCheckGroup;
import hzst.android.form.view.FMDateSection;
import hzst.android.form.view.FMDateView;
import hzst.android.form.view.FMEvaluateView;
import hzst.android.form.view.FMFormView;
import hzst.android.form.view.FMEditText;
import hzst.android.form.view.FMProcessView;
import hzst.android.form.view.FMRichEditor;
import hzst.android.form.view.FMSpinner;
import hzst.android.form.view.FMStateView;
import hzst.android.form.view.FMTextView;
import hzst.android.form.view.FMOpinionView;
import hzst.android.form.view.FMSelectView;
import hzst.android.util.FontUtil;
import hzst.android.util.L;
import hzst.android.util.PhoneUtil;
import hzst.android.util.UnitsConversionUtil;

/**
 * 创建表单工具类。
 *
 * @author wt
 */
public class ViewCreator implements Field, Constants {
    private Context context;
    private PhoneUtil phoneHelper;
    private EventsManager eventsManager;
    private List<BaseViewInfo> infoList;//表单中的控件绑定的数据
    private List<View> viewList;
    private List<ViewCollection> viewCollections;
    private LinearLayout contentView;
    private Map<String, String> parameters;//任务相关数据
    public boolean isEditable;

    private float density;

    public static OnCreateSubmitDataListener onCreateSubmitDataListener;

    public ViewCreator(Context context, String json, BaseUser user, LinearLayout contentView) {
        this.context = context;
        this.contentView = contentView;
        phoneHelper = new PhoneUtil(context);

        density = phoneHelper.getDensity();
        ViewInfoProcessor processor = new ViewInfoProcessor();
        infoList = processor.getViewInfo(json, user);
        this.eventsManager = new EventsManager(infoList);
        parameters = processor.getParameters();
        viewList = new ArrayList<>();
        viewCollections = new ArrayList<>();
        /**
         * 计算标题单元格的宽度。
         * 最大限定字数+1(冒号)的长度 + 左右边距
         */

        titleWidth = (int) (UnitsConversionUtil.sp2px(context, 18 * (TITLE_TEXT_LENGTH + 1)) + 15 * density);
        L.showLogInfo(L.TAG_OTHER_INFO, String.valueOf(titleWidth));
    }

    public LinearLayout getContentView() {
        return contentView;
    }

    public List<BaseViewInfo> getInfoList() {
        return infoList;
    }

    @Deprecated
    public List<View> getViewList() {
        return viewList;
    }

    public List<ViewCollection> getViewCollections() {
        return viewCollections;
    }

    public EventsManager getEventsManager() {
        return eventsManager;
    }
    /**
     * 与{@link BaseViewInfo}中的viewType匹配，动态生成表单中的控件。
     */
    /**
     * 任务相关数据{@link #parameters}
     */
    public static final String VIEW_TYPE_TASK = "TransferParameter";
    /**
     * 从表数据(控件相关数据保存在从表中)
     */
    public static final String VIEW_TYPE_TABLE = "Table";
    /*
     * 控件类型，对应各个控件。
	 */
    /**
     * {@link FMTextView}
     */
    public static final String VIEW_TYPE_TV = "TextView";
    /**
     * {@link FMSpinner}
     */
    public static final String VIEW_TYPE_SP = "Spinner";
    /**
     * {@link FMCheckGroup}
     */
    public static final String VIEW_TYPE_CG = "CheckGroup";
    /**
     * {@link FMDateView}
     */
    public static final String VIEW_TYPE_DV = "DateView";
    /**
     * {@link FMDateSection}
     */
    public static final String VIEW_TYPE_DS = "DateSection";
    /**
     * {@link FMEditText}
     */
    public static final String VIEW_TYPE_ET = "EditText";
    /**
     * {@link FMAccessoryView}
     */
    public static final String VIEW_TYPE_AV = "AccessoryView";//附件选择控件
    /**
     * {@link FMOpinionView}
     */
    public static final String VIEW_TYPE_OPINION = "OpinionView";//意见控件
    /**
     * {@link FMSelectView}
     */
    public static final String VIEW_TYPE_SV = "SelectView";//选择新页面(包括人员、课题等)列表中的一条或多条项目，并回写到文本框
    /**
     * {@link FMFormView}
     */
    public static final String VIEW_TYPE_FV = "FormView";//表格控件
    /**
     * {@link hzst.android.form.view.FMProcessView}
     */
    public static final String VIEW_TYPE_PV = "ProcessView";//流程控件
    /**
     * {@link hzst.android.form.view.FMStateView}
     */
    public static final String VIEW_TYPE_STV = "StateView";//状态控件
    /**
     * {@link hzst.android.form.view.FMRichEditor}
     */
    public static final String VIEW_TYPE_REV = "RichEditor";//富文本编辑框

    public static final String VIEW_TYPE_EVALUATE = "EvaluateView";//员工考核评分表格

    /**
     * 标题和内容宽度
     */
    public int titleWidth;
    public int contentWidth;
    public int titlePaddingX;//标题左右边距(一行的左右边距)
    public int titlePaddingY;//标题上下边距

    public static final int TITLE_TEXT_LENGTH = 4;


    /*
     * 标题和内容的背景色，表单中的一个单元格分为标题(左)和内容(右)两部分
     */
//    public static final int COLOR_NAME = R.color.bg_form_name;
//    public static final int COLOR_CONTENT = R.color.bg_form_content;
    public static final int COLOR_NAME = R.color.bg_white;
    public static final int COLOR_CONTENT = R.color.bg_white;

    //    public static final int TEXT_COLOR_TITLE = R.color.form_title_color;
    public static final int TEXT_COLOR_CONTENT = R.color.form_content_color;
    public static final int TEXT_SIZE_TITLE = 16;
    public static final int TEXT_SIZE_CONTENT = 16;
    public static final double TITLE_PADDING_DP = 14.4;
    public int titleTextColor;//标题默认颜色
    public int contentTextColor;

    public Map<String, String> getParameters() {
        return parameters;
    }

    private void initBaseData() {
        if (parameters.containsKey(APPROVE_ISEDITABLE)) {
            isEditable = TRUE.equals(parameters.get(APPROVE_ISEDITABLE));
        }
        titlePaddingX = (int) (TITLE_PADDING_DP * density);
        if (isEditable) {
            titlePaddingY = (int) (8 * density);
        } else {
            titlePaddingY = (int) (3 * density);
        }

        if (isEditable) {
            titleTextColor = context.getResources().getColor(R.color.form_title_color);
        } else {
            titleTextColor = context.getResources().getColor(R.color.form_title_color_r);
        }

        contentTextColor = context.getResources().getColor(TEXT_COLOR_CONTENT);
    }

    /**
     * 创建所有表单控件
     *
     * @param viewList 所有控件集合。部分控件需要递归调用
     */
    public void createViews(List<BaseViewInfo> viewList) {
        initBaseData();
        //设置背景色为边框颜色，用来反衬出边框
        contentView.setBackgroundColor(context.getResources().getColor(R.color.bg_form_theme));
        /*
         * 设置单元格名称的背景色和比例
		 */
        int titleColor = context.getResources().getColor(COLOR_NAME);
        LinearLayout.LayoutParams paramsTitle = new LinearLayout.LayoutParams(titleWidth, LayoutParams.MATCH_PARENT);
//        paramsTitle.weight = WEIGHT_TITLE;

        for (int j = 0; j < viewList.size(); j++) {
            BaseViewInfo info = viewList.get(j);

            contentWidth = phoneHelper.getWidth() - titleWidth;

            View view = createViewByType(info, j);

            /*
             * 创建行标题。
			 * 为从表类型则不创建。
			 */
            if (!VIEW_TYPE_TABLE.equals(info.getViewType())) {
                LinearLayout llay = new LinearLayout(context);
                llay.setOrientation(LinearLayout.HORIZONTAL);
//                llay.setLayoutParams(paramsLine);
//                llay.setMinimumHeight((int) (40 * density));

                if (isNeedTitle(info)) {
                    TextView tvName = new TextView(context);

                    String title = spacingTitle(info.getName());

                    if (!TextUtils.isEmpty(info.getName().trim())) {
                        tvName.setText(title);
                    }

                    tvName.setTextSize(TEXT_SIZE_TITLE);
                    tvName.setTextColor(titleTextColor);
                    tvName.setLayoutParams(paramsTitle);
                    tvName.setBackgroundColor(titleColor);
                    tvName.setPadding(titlePaddingX, titlePaddingY, (int) (5 * density), titlePaddingY);

                    setTitleGravity(tvName, info);

                    llay.addView(tvName);
                }

				/*
                 * 创建单元格的内容
				 */
                llay.addView(view);
                contentView.addView(llay);
                /*
                 * 可编辑状态下控件间流出间距
                 */
                View viewMargin = null;

                switch (info.getMarginType()) {
                    case BaseViewInfo.MARGIN_NONE:
                        break;
                    case BaseViewInfo.MARGIN_PL_LINE:
                        viewMargin = LayoutInflater.from(context).inflate(R.layout.view_form_margin_plline, null);
                        View linePadding = viewMargin.findViewById(R.id.v_line_padding);
                        linePadding.setLayoutParams(new LinearLayout.LayoutParams(titlePaddingX, (int) (0.5 * density)));
                        break;
                    case BaseViewInfo.MARGIN_LAYER_CAKE:
                        viewMargin = LayoutInflater.from(context).inflate(R.layout.view_form_margin_layer_cake, null);
                        break;

                    case BaseViewInfo.MARGIN_LINE:
                        viewMargin = LayoutInflater.from(context).inflate(R.layout.view_form_margin_divider, null);
                        break;
                    case BaseViewInfo.MARGIN_CAKE:
                        viewMargin = LayoutInflater.from(context).inflate(R.layout.view_form_margin_cake, null);
                        break;
                    case BaseViewInfo.MARGIN_CAKE_TOP:
                        viewMargin = LayoutInflater.from(context).inflate(R.layout.view_form_margin_cake_top, null);
                        break;
                    case BaseViewInfo.MARGIN_CAKE_BOTTOM:
                        viewMargin = LayoutInflater.from(context).inflate(R.layout.view_form_margin_cake_bottom, null);
                        break;
                }
                if (viewMargin != null) {
                    contentView.addView(viewMargin);
                }

                ViewCollection viewCollection = new ViewCollection();
                viewCollection.setFmView(((LinearLayout) view).getChildAt(0));
                viewCollection.setParentView(llay);
                viewCollection.setParentIndex(j);
                viewCollection.setViewMargin(viewMargin);
                viewCollections.add(viewCollection);
            }
        }
    }

    private void setTitleGravity(TextView tv, BaseViewInfo info) {
        tv.setGravity(Gravity.TOP);
//        switch (info.getViewType()) {
//            case VIEW_TYPE_ET:
//                EditViewOwn own = ((EditViewInfo) info).getOwn();
//                if (own != null) {
//                    if (own.getLine() > 1) {
//                        tv.setGravity(Gravity.TOP);
//                        break;
//                    }
//                }
//                tv.setGravity(Gravity.CENTER_VERTICAL);
//                break;
//            default:
//                tv.setGravity(Gravity.CENTER_VERTICAL);
//                break;
//        }
    }

    /**
     * @param info
     * @return
     */
    private boolean isNeedTitle(BaseViewInfo info) {
        switch (info.getViewType()) {
            case VIEW_TYPE_FV:
            case VIEW_TYPE_SV:
            case VIEW_TYPE_AV:
            case VIEW_TYPE_PV:
            case VIEW_TYPE_STV:
            case VIEW_TYPE_REV:
            case VIEW_TYPE_EVALUATE:
                return false;
        }
        return info.isNeedTitle();
    }

    /**
     * @param info  控件绑定的数据
     * @param index 控件在表单中的位置
     * @return 单元格内的控件
     */
    private View createViewByType(final BaseViewInfo info, int index) {
        FMBaseView view = null;
//        L.showLogInfo(L.TAG_OTHER_INFO, "表单控件类型:" + info.getViewType());
        if (VIEW_TYPE_TV.equals(info.getViewType())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            if (!info.isNeedTitle()) {
                params.setMargins(titlePaddingX, 0, titlePaddingX, 0);
            } else {
                params.setMargins(0, 0, titlePaddingX, 0);
            }

            view = new FMTextView(context);
            view.setLayoutParams(params);
            view.setInfo(info, this);
            view.showView();

        } else if (VIEW_TYPE_SP.equals(info.getViewType())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, titlePaddingX, 0);
            view = new FMSpinner(context);
            view.setLayoutParams(params);
            view.setInfo(info, this);
            view.showView();

        } else if (VIEW_TYPE_CG.equals(info.getViewType())) {
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
//            params.setMargins(0, 0, titlePaddingX, 0);
            view = new FMCheckGroup(context);
//            view.setLayoutParams(params);
            view.setInfo(info, this);
            view.showView();

        } else if (VIEW_TYPE_DV.equals(info.getViewType())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.setMargins(0, 0, titlePaddingX, 0);
            view = new FMDateView(context);
            view.setLayoutParams(params);
            view.setInfo(info, this);
            view.showView();

        } else if (VIEW_TYPE_DS.equals(info.getViewType())) {
            view = new FMDateSection(context);
            view.setInfo(info, this);
            ((FMDateSection) view).setReadOnly(info.isReadOnly());
            ((FMDateSection) view).showView(this);

        } else if (VIEW_TYPE_ET.equals(info.getViewType())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            params.setMargins(0, 0, titlePaddingX, 0);
            view = new FMEditText(context);
            view.setLayoutParams(params);
            view.setInfo(info, this);
            view.showView();

        } else if (VIEW_TYPE_AV.equals(info.getViewType())) {
            info.setMustFill(false);//附件默认为非必填
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.setMargins(titlePaddingX, 0, 0, 0);
            view = new FMAccessoryView(context);
//            eventsManager.setOnAccessoryOperateListener(((FMAccessoryView) view));
            eventsManager.setOnActivityResultListener(((FMAccessoryView) view));
            ((FMAccessoryView) view).setOnAccessoryUploadListener(eventsManager);
            view.setLayoutParams(params);
            view.setInfo(info, this);
            view.showView();
            if (info.isReadOnly()) {
                ((FMAccessoryView) view).setEnable(false);
            }

        } else if (VIEW_TYPE_OPINION.equals(info.getViewType())) {
            view = new FMOpinionView(context);
            view.setInfo(info, this);
            view.showView();

        } else if (VIEW_TYPE_SV.equals(info.getViewType())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            params.setMargins(titlePaddingX, 0, 0, 0);
            view = new FMSelectView(context);
            view.setLayoutParams(params);
            view.setInfo(info, this);

        } else if (VIEW_TYPE_FV.equals(info.getViewType())) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            view = new FMFormView(context);
            view.setLayoutParams(params);
            view.setInfo(info, this);
            view.showView();
        } else if (VIEW_TYPE_TABLE.equals(info.getViewType())) {//表格控件，进行递归调用
            createViews(infoList.get(index).getSlaveTableInfos());
            return null;
        } else if (VIEW_TYPE_PV.equals(info.getViewType())) {
            view = new FMProcessView(context);
            view.setInfo(info, this);
            view.showView();
            ViewGroup vg = (ViewGroup) view.getChildAt(0);
            for (int i = 0; i < vg.getChildCount(); i++) {
                vg.getChildAt(i).setPadding(titlePaddingX, 0, 0, 0);
            }
        } else if (VIEW_TYPE_STV.equals(info.getViewType())) {
            view = new FMStateView(context);
            view.setInfo(info, this);
            view.showView();
        } else if (VIEW_TYPE_REV.equals(info.getViewType())) {
            view = new FMRichEditor(context);
            view.setInfo(info, this);
            view.showView();
        } else if (VIEW_TYPE_EVALUATE.equals(info.getViewType())) {
            view = new FMEvaluateView(context);
            view.setInfo(info, this);
            view.showView();
        }

        int cw = contentWidth;
        if (!isNeedTitle(info)) {
            cw = phoneHelper.getWidth();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cw, LayoutParams.MATCH_PARENT);

        LinearLayout llay = new LinearLayout(context);
        llay.setLayoutParams(params);
        llay.setBackgroundColor(context.getResources().getColor(COLOR_CONTENT));

        if (view != null) {

            viewList.add(view);
            view.setTag(index);
            llay.addView(view);
        }

        return llay;
    }

    /**
     * 根据提交数据的字段名称来保存数据。
     *
     * @param field 字段名
     * @param value 字段对应的值
     * @param info  控件绑定的数据
     */
    public void setSubmitValue(String field, String value, BaseViewInfo info) {
        List<SubmitValue> submitValues = info.getSubmitValues();
        for (int i = 0; i < submitValues.size(); i++) {
            SubmitValue submitValue = submitValues.get(i);
            if (submitValue.getField().equals(field)) {
                submitValue.setValue(value);
                submitValues.set(i, submitValue);
                break;
            }
        }
    }

    /**
     * 控件只有一个提交数据时,可以用该方法来保存数据。
     *
     * @param value 字段对应的值
     * @param info  控件绑定的数据
     */
    public void setSubmitValue(String value, BaseViewInfo info) {
        List<SubmitValue> submitValues = info.getSubmitValues();
        SubmitValue submitValue = submitValues.get(0);
        submitValue.setValue(value);
        submitValues.set(0, submitValue);
    }

    /**
     * 根据提交数据的下标(在json中的下标)来保存数据。
     *
     * @param value 字段对应的值
     * @param info  控件绑定的数据
     * @param index 控件数据所在集合中的下标
     */
    public void setSubmitValue(String value, BaseViewInfo info, int index) {
        List<SubmitValue> submitValues = info.getSubmitValues();
        SubmitValue submitValue = submitValues.get(index);
        submitValue.setValue(value);
        submitValues.set(index, submitValue);
    }

    @Deprecated
    /**
     * 添加需要上传的特殊事件相关的数据
     *
     * @param value
     * @param info
     */
    public void setSpecialValue(String value, BaseViewInfo info) {
        List<SpecialValue> specialValues = info.getSpecialValues();
        SpecialValue specialValue = specialValues.get(0);
        specialValue.setValue(value);
        specialValues.set(0, specialValue);
    }

    @Deprecated
    public void setSpecialValue(String value, BaseViewInfo info, int index) {
        List<SpecialValue> specialValues = info.getSpecialValues();
        SpecialValue specialValue = specialValues.get(index);
        specialValue.setValue(value);
        specialValues.set(index, specialValue);
    }

    /**
     * 把需要上传的数据拼成json，
     * 需要上传的数据有两部分，常规控件的数据；特殊控件的数据(非常规的操作下产生的数据)。
     *
     * @param viewList 控件集合
     * @return 提交到服务器的json数据
     */
    @Deprecated
    public String createSubmitData(List<BaseViewInfo> viewList, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder("");
        sb.append("[");
        sb.append("{");

		/*
         * 拼接任务相关数据
		 */
        Set<String> keySet = parameters.keySet();
        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            sb.append("\"");
            sb.append(key);
            sb.append("\":\"");
            try {
                sb.append(URLEncoder.encode(parameters.get(key), Constants.CHARSET_UTF8));
            } catch (UnsupportedEncodingException e) {
                L.showLogInfo(L.TAG_EXCEPTION, e.toString());
            }
            sb.append("\"");
            sb.append(",");
        }

        for (int i = 0; i < viewList.size(); i++) {
            BaseViewInfo info = viewList.get(i);
            //有从表数据时
            if (VIEW_TYPE_TABLE.equals(info.getViewType())) {
                if (!(sb.lastIndexOf("]") == sb.length())) {
                    if (sb.length() > 1) {
                        sb.setLength(sb.length() - 1);
                    }
                    sb.append("}");
                    sb.append("]");
                }
                sb.append("_$$_");//连接表之间的数据
                String slaveData = createSubmitData(info.getSlaveTableInfos(), info.getParameters());
                sb.append(slaveData);
            } else {
                /*
                 * 非空校验
				 */
                List<SubmitValue> submitValues = info.getSubmitValues();
                if (!(info instanceof SelectableViewInfo)) {
                    for (int j = 0; j < submitValues.size(); j++) {
                        String value = submitValues.get(j).getValue();
                        if ((value == null || "".equals(value)) && info.isVisible() && info.isMustFill()) {
                            Toast.makeText(context, info.getName() + "不能为空", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    }
                }
                /*
                 * 最大字数校验
				 */
                if (info instanceof EditViewInfo) {
                    String value = submitValues.get(0).getValue();
                    if (value != null) {
                        if (FontUtil.isBeyondTextLimit(value, ((EditViewInfo) info).getMaxEms()) && ((EditViewInfo) info).getMaxEms() > 0) {
                            Toast.makeText(context, info.getName() + "字数超出限制", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    }
                }
                /*
                 * 拼接控件数据
				 */
                for (int j = 0; j < submitValues.size(); j++) {
                    SubmitValue submitValue = submitValues.get(j);
                    String value = submitValue.getValue();
                    if (value == null) {
                        value = "";
                    }
                    sb.append("\"");
                    sb.append(submitValue.getField());
                    sb.append("\":\"");
                    try {
                        sb.append(URLEncoder.encode(value, Constants.CHARSET_UTF8));
                    } catch (UnsupportedEncodingException e) {
                        L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                    }
                    sb.append("\"");
                    sb.append(",");
                }
                /*
                 * 拼接特殊事件数据
				 */
                List<SpecialValue> specialValues = info.getSpecialValues();
                for (int j = 0; j < specialValues.size(); j++) {
                    SpecialValue specialValue = specialValues.get(j);
                    String value = specialValue.getValue();
                    sb.append("\"");
                    sb.append(specialValue.getFieldName());
                    sb.append("\":\"");
                    try {
                        sb.append(URLEncoder.encode(value, Constants.CHARSET_UTF8));
                    } catch (UnsupportedEncodingException e) {
                        L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                    }
                    sb.append("\"");
                    sb.append(",");
                }
            }
        }
        if (sb.lastIndexOf("]") != sb.length() - 1) {
            sb.setLength(sb.length() - 1);
            sb.append("}");
            sb.append("]");
        }

        L.showLogInfo(L.TAG_OTHER_INFO, "保存审批：" + sb);
        return sb.toString();
    }

    public void createSubmitData(List<BaseViewInfo> viewList) {
    }

    public interface OnValueChangeListener {
        //控件中的值改变时的回调函数
        void valueChanged(List<BaseViewInfo> viewInfo);
    }

    public interface OnCreateSubmitDataListener {
        void createSubmitData(List<BaseViewInfo> viewList);
    }

    /**
     * 添加标题字边距，使标题之间对齐
     *
     * @param text
     * @return
     */
    public String spacingTitle(String text) {
        StringBuilder sb = new StringBuilder("");
        int spaceNum = 4;//每个中文4个空格

        sb.append(text);

        if (!isEditable) {
            sb.append(":");
        }
        if (text.length() <= TITLE_TEXT_LENGTH) {
            for (int i = 0; i < spaceNum * (TITLE_TEXT_LENGTH - text.length()); i++) {
                sb.append(" ");
            }
        }

//        if (text.length() < TITLE_TEXT_LENGTH && text.length() > 1) {
//            int addNum = (TITLE_TEXT_LENGTH - text.length()) * spaceNum / (text.length() - 1);
//            for (int i = 0; i < text.length(); i++) {
//                sb.append(text.substring(i, i + 1));
//                if (i != text.length() - 1) {
//                    for (int j = 0; j < addNum; j++) {
//                        sb.append(" ");
//                    }
//                }
//            }
//        } else if (text.length() == 1) {
//            for (int i = 0; i < spaceNum * (TITLE_TEXT_LENGTH - 1); i++) {
//                sb.append(" ");
//            }
//            sb.append(text);
//        } else {
//            return text;
//        }

        return sb.toString();
    }

}
