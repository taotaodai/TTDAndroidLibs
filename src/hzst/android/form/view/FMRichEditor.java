package hzst.android.form.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.Arrays;

import hzst.android.R;
import hzst.android.adapter.CommonAdapter;
import hzst.android.form.ViewCreator;
import hzst.android.form.entity.EditViewOwn;
import hzst.android.form.info.EditViewInfo;
import hzst.android.util.PhoneUtil;
import hzst.android.view.MyCheckBox;
import hzst.android.view.richEditor.RichEditor;

/**
 * Created by wt on 2017/6/29.
 */
public class FMRichEditor extends FMBaseView {
    private View rootView;
    private HorizontalScrollView hsvOperation;
    private RichEditor mEditor;
    private EditViewInfo info;
    private EditViewOwn own;

    public FMRichEditor(Context context) {
        super(context);
    }

    public FMRichEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void showView() {
        info = (EditViewInfo) super.info;
        rootView = LayoutInflater.from(context).inflate(R.layout.view_rich_editor, null);
        hsvOperation = (HorizontalScrollView) rootView.findViewById(R.id.hsv_operation);
        mEditor = (RichEditor) rootView.findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(ViewCreator.TEXT_SIZE_CONTENT);
//        mEditor.setEditorFontColor(Color.RED);
        mEditor.setPadding((int) ViewCreator.TITLE_PADDING_DP, 10, (int) ViewCreator.TITLE_PADDING_DP, 10);

        if (info.isReadOnly()) {
            hsvOperation.setVisibility(View.GONE);
            mEditor.setInputEnabled(false);
        }

        own = info.getOwn();
        if (own != null) {
            mEditor.setPlaceholder(own.getHint());
        }

        mEditor.setHtml(info.getSubmitValues().get(0).getValue());

        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                viewCreator.setSubmitValue(text, info);
            }
        });

        //mEditor.setInputEnabled(false);

        //加粗
        ((MyCheckBox) rootView.findViewById(R.id.action_bold)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditor.setBold();
            }
        });

        //斜体
        ((MyCheckBox) rootView.findViewById(R.id.action_italic)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditor.setItalic();
            }
        });

        //下划线
        ((MyCheckBox) rootView.findViewById(R.id.action_underline)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditor.setUnderline();
            }
        });

        vTextColor = rootView.findViewById(R.id.action_text_color);
        //文字颜色
        vTextColor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextColorSection();
            }
        });

        //编号
        ((MyCheckBox) rootView.findViewById(R.id.action_insert_numbers)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditor.setNumbers();
            }
        });

        //字体大小
        vTextSize = rootView.findViewById(R.id.action_heading1);
        vTextSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTextSizeSection();
            }
        });

        //居左
        rootView.findViewById(R.id.action_align_left).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        //居中
        rootView.findViewById(R.id.action_align_center).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        //居右
        rootView.findViewById(R.id.action_align_right).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });
        //回退
        rootView.findViewById(R.id.action_undo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });
        //前进
        rootView.findViewById(R.id.action_redo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });


        addView(rootView);
    }

    private PopupWindow popColors;
    private static final Integer[] TEXT_COLORS = {0xFFE53333, 0xFFE56600, 0xFFFF9900, 0xFF64451D, 0xFFDFC5A4, 0xFFFFE500,
            0xFF009900, 0xFF006600, 0xFF99BB00, 0xFFB8D100, 0xFF60D978, 0xFF00D5FF,
            0xFF337FE5, 0xFF003399, 0xFF4C33E5, 0xFF9933E5, 0xFFCC33E5, 0xFFEE33EE,
            0xFFFFFFFF, 0xFFCCCCCC, 0xFF999999, 0xFF666666, 0xFF333333, 0xFF000000};
    private static final int LINE_COLOR_NUM = 6;

    private View vTextColor;
    private void showTextColorSection() {
        if (popColors == null) {
            PhoneUtil phoneUtil = new PhoneUtil(context);
            popColors = new PopupWindow((int) (30 * phoneUtil.getDensity()) * LINE_COLOR_NUM, (int) (30 * phoneUtil.getDensity()) * TEXT_COLORS.length / LINE_COLOR_NUM);
            View viewPop = LayoutInflater.from(context).inflate(R.layout.view_fm_rich_colors,null);
            GridView gv = (GridView) viewPop.findViewById(R.id.gv_colors);
            gv.setNumColumns(LINE_COLOR_NUM);

            gv.setAdapter(new CommonAdapter<Integer>(context, Arrays.asList(TEXT_COLORS),R.layout.adapter_fm_rich_colors) {
                @Override
                public void convert(ViewHolder helper, final Integer item, int position) {
                    TextView tv = helper.getView(R.id.tv_text_color);
                    tv.setBackgroundColor(item);
                    tv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mEditor.setTextColor(item);
                            popColors.dismiss();
                        }
                    });
                }
            });
            popColors.setContentView(viewPop);
            popColors.setBackgroundDrawable(new BitmapDrawable());
            popColors.setOutsideTouchable(true);
//            popColors.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    ((Activity) context).getWindow().setAttributes(lp);
//                }
//            });
//            WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
//            lp.alpha = 0.7f;
//            ((Activity)context).getWindow().setAttributes(lp);
        }
        popColors.showAsDropDown(vTextColor);


    }

    private PopupWindow popSize;
    private View vTextSize;
    private static final int[] TEXT_SIZE_NUM = {1,2,3,4,5,6};
    private static final String[] TEXT_SIZE = {"h1","h2","h3","h4","h5","h6"};
    private void showTextSizeSection(){
        if(popSize == null){
            PhoneUtil phoneUtil = new PhoneUtil(context);
            popSize = new PopupWindow((int) (60*density),phoneUtil.getHeight()/3);
            View vContent = LayoutInflater.from(context).inflate(R.layout.view_fm_rich_ts,null);
            ListView lvSize = (ListView) vContent.findViewById(R.id.lv_text_size);
            lvSize.setAdapter(new CommonAdapter<String>(context,Arrays.asList(TEXT_SIZE),R.layout.adapter_fm_rich_ts) {
                @Override
                public void convert(ViewHolder helper, String item, int position) {
                    helper.setText(R.id.tv_text_size,item);
                }
            });
            lvSize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mEditor.setFontSize(TEXT_SIZE_NUM[position]);
                    popSize.dismiss();
                }
            });
            popSize.setContentView(vContent);
            popSize.setBackgroundDrawable(new BitmapDrawable());
            popSize.setOutsideTouchable(true);
//            popSize.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
//                    lp.alpha = 1f;
//                    ((Activity) context).getWindow().setAttributes(lp);
//                }
//            });
//            WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
//            lp.alpha = 0.7f;
//            ((Activity)context).getWindow().setAttributes(lp);
        }
        popSize.showAsDropDown(vTextSize);
    }

}
