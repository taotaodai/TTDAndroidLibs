package hzst.android.form;

import hzst.android.BaseActivity;
import hzst.android.Constants;
import hzst.android.R;
import hzst.android.adapter.CommonAdapter;
import hzst.android.form.info.SelectSource;
import hzst.android.form.info.SelectSource.SourceValue;
import hzst.android.form.info.SelectViewInfo;
import hzst.android.form.view.FMSelectView;
import hzst.android.util.L;
import hzst.android.util.NetworkDataUtil;
import hzst.android.view.FooterListView;
import hzst.android.view.FooterListView.OnSlideToBottomListener;
import hzst.android.view.MyCheckBox;
import hzst.android.view.TopNavigationBar;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 作为{@link FMSelectView}的选择数据列表，当选择了一条或多条数据时，会返回{@link List<SelectSource>}数据，
 * 条目的名称和id需要单独提取出来作为{@link FMSelectView}的传递参数，其他的相关参数用于匹配表单中相同字段的控件(也有可能没有匹配字段)，并给其赋值。
 *
 * @author wt
 *         传值：{@link Bundle} key:"host" {@link #host}
 */
public class SelectSourceListActivity extends BaseActivity {
    protected TopNavigationBar tnbSource;
    protected FooterListView lvSource;

    protected SelectViewInfo
            info;

    protected String host;
    protected int pageIndex = 1;
    protected int pageSize = 10;

    protected CommonAdapter<SelectSource> adapter;
    protected List<SelectSource> selectSources;

    public static FMSelectView.OnConfirmClickListener onConfirmClickListener;
    protected int itemLayout = R.layout.adapter_source_item;

    private OnCreateItemListener onCreateItemListener;

    protected Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            updateViews();
        }
    };

    public OnCreateItemListener getOnCreateItemListener() {
        return onCreateItemListener;
    }

    public void setOnCreateItemListener(OnCreateItemListener onCreateItemListener) {
        this.onCreateItemListener = onCreateItemListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_source);

        initBaseData();
        initView();
        initEvents();

        if (TextUtils.isEmpty(info.getSelectSource())) {
            getData();
        } else {
            selectSources = info.getSourceList();
            updateViews();
        }
    }

    @Override
    public void initBaseData() {
        info = (SelectViewInfo) getIntent().getSerializableExtra("viewInfo");
        host = getIntent().getStringExtra("host");
    }

    @Override
    public void initView() {
        tnbSource = (TopNavigationBar) findViewById(R.id.tnb_source);
        tnbSource.setTitle(info.getName());
        if (info.isMultiple()) {
            tnbSource.setRightBtnText("确定");
        }
        lvSource = (FooterListView) findViewById(R.id.lv_source);
        lvSource.setPageSize(pageSize);
    }

    @Override
    public void initEvents() {
        lvSource.setOnSlideToBottomListener(new OnSlideToBottomListener() {

            @Override
            public void arriveBottom() {
                pageIndex++;
                getData();
            }
        });
        lvSource.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                SelectSource item = selectSources.get(position);
                if (info.isMultiple()) {//多选
                    MyCheckBox cb = (MyCheckBox) view.findViewById(R.id.cb_source);
                    cb.setChecked(!cb.isChecked());
                    item.setSelected(cb.isChecked());
                } else {
                    if (onConfirmClickListener != null) {
                        onConfirmClickListener.confirm(selectSources.subList(position, position + 1));
                    }
                    onConfirmClickListener = null;
                    finish();
                }
            }
        });

        tnbSource.setOnClickRightBtnListener(new TopNavigationBar.OnClickRightBtnListener() {
            @Override
            public void clickRightBtn() {
                List<SelectSource> temp = new ArrayList<>();
                for (SelectSource source :
                        selectSources) {
                    if (source.isSelected()) {
                        temp.add(source);
                    }
                }
                if (onConfirmClickListener != null) {
                    onConfirmClickListener.confirm(temp);
                }
                onConfirmClickListener = null;
                finish();
            }
        });
    }

    protected void updateViews() {
        if (adapter == null) {
            adapter = new CommonAdapter<SelectSource>(mContext, selectSources, itemLayout) {

                @Override
                public void convert(ViewHolder helper, SelectSource item,
                                    int position) {
                    if(onCreateItemListener != null){
                        onCreateItemListener.convert(helper,item,position);
                    }
                    TextView tvTitle = helper.getView(R.id.tv_title);
                    if(TextUtils.isEmpty(item.getItemName())){
                        tvTitle.setVisibility(View.GONE);
                    }else {
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText(item.getItemName());
                    }
                    LinearLayout llay = helper.getView(R.id.llay_source_content);
                    if (llay.getChildCount() == 0) {
                        List<SourceValue> sourceValues = item.getSourceValues();
                            /*
							 * 动态生成条目的信息
							 */
                        for (int i = 0; i < sourceValues.size(); i++) {
                            SourceValue value = sourceValues.get(i);
                            TextView tv = new TextView(mContext);
                            tv.setTextSize(16);
                            if (!TextUtils.isEmpty(value.getTitle())) {
                                tv.setText(value.getTitle() + "：" + value.getContent());
                            } else {
                                tv.setText(value.getContent());
                            }

                            llay.addView(tv);
                        }
                    }

                    if (info.isMultiple()) {
                        MyCheckBox cb = helper.getView(R.id.cb_source);
                        cb.setVisibility(View.VISIBLE);
                    }
                }
            };
            lvSource.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        lvSource.loadFooterView(false);
    }

    protected void getData() {
        new Thread() {

            @Override
            public void run() {
                try {
                    String url = host + info.getSourceUrl() + "?PageSize=" + pageSize + "&PageIndex=" + pageIndex;
                    L.showLogInfo(L.TAG_NET_REQUEST, "选择数据列表：" + url);
                    String json = NetworkDataUtil.getJsonData(url);
                    //TODO
//					selectSources = FMSelectView.parseSource(json);
                    handler.sendEmptyMessage(0x123);
                } catch (Exception e) {
                    L.showLogInfo(L.TAG_EXCEPTION, e.toString());
                }
            }

        }.start();
    }

    public interface OnCreateItemListener {
		void convert(CommonAdapter.ViewHolder helper, SelectSource item,
                     int position);
    }

}
