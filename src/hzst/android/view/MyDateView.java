package hzst.android.view;

import hzst.android.R;
import hzst.android.util.BackgroundUtil;
import hzst.android.util.DateOrTimeUtil;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;


/**
 * 选择日期(包括时分)控件
 *
 * @author wt
 */
public class MyDateView extends LinearLayout implements Formatter {
    private Context context;
    private Dialog dialogDial;

    private LinearLayout llayDate;
    private TextView tvDate;
    //	private ImageView ivCalendar;
    /*
	 * 表盘中的控件
	 */
    private DatePicker mDatePicker;
    private LinearLayout llayTime;
    private NumberPicker npHour;
    private NumberPicker npMinute;
    private Button btnConfirm;

    private String title = "";
    private String date = "";
    private String time = "";
    private int timeMode = MODE_TIME_NORMAL;
    private long minDate = 0;
    private long maxDate = 0;

    private boolean isTimeEnable = true;
    private boolean isDateEnable = true;
    private boolean enable = true;
    private OnDateTimeChangedListener onDateTimeChangedListener;

    private String[] minuteValues;
    //	public static final int MODE_YMD = 1;
//	public static final int MODE_YMD_HMS = 2;
    public static final int MODE_TIME_NORMAL = 1;
    public static final int MODE_TIME_15M = 2;

    public MyDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        initView();
    }

    public MyDateView(Context context) {
        super(context);
        this.context = context;

        initView();
    }

    public void setMinDate(long minDate) {
        this.minDate = minDate;
    }

    public void setMaxDate(long maxDate) {
        this.maxDate = maxDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @deprecated 请用{@link #getDatet()}
     */
    @Deprecated
    public String getText() {
        return tvDate.getText().toString();
    }

    /**
     * @param text
     * @deprecated 请用{@link #setDate(String)}
     */
    @Deprecated
    public void setText(String text) {
        tvDate.setText(text);
    }

    public String getDate() {
        return tvDate.getText().toString();
    }

    public void setDate(String date) {
        tvDate.setText(date);
    }

    public String getTime() {
        return time;
    }

    public void setTimeMode(int timeMode) {
        this.timeMode = timeMode;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        llayDate.setClickable(enable);
    }

    public void setTimeEnable(boolean enable) {
        isTimeEnable = enable;
    }

    public void setDateEnable(boolean enable) {
        this.isDateEnable = enable;
    }

    public void setOnDateTimeChangedListener(
            OnDateTimeChangedListener onDateTimeChangedListener) {
        this.onDateTimeChangedListener = onDateTimeChangedListener;
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_date_skin, null);
        llayDate = (LinearLayout) view.findViewById(R.id.llay_date);

        llayDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (dialogDial == null) {
                    showDial();
                } else {
                    if (!dialogDial.isShowing()) {
                        dialogDial.show();
                    } else {
                        dialogDial.dismiss();
                    }
                }
            }
        });
        tvDate = (TextView) view.findViewById(R.id.tv_date);

        tvDate.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (onDateTimeChangedListener != null) {
                    onDateTimeChangedListener.dateTimeChanged(s.toString());
                }
            }
        });
//		ivCalendar = (ImageView) view.findViewById(R.id.iv_calendar);

        addView(view);
    }

    /**
     * 显示表盘
     */
    private void showDial() {
        dialogDial = new Dialog(context);
        dialogDial.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.view_date_dial, null);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        if (!"".equals(title)) {
            tvTitle.setText(title);
        }
        mDatePicker = (DatePicker) view.findViewById(R.id.dp_custom);
        llayTime = (LinearLayout) view.findViewById(R.id.llay_time);
        npHour = (NumberPicker) view.findViewById(R.id.np_hour);
        npMinute = (NumberPicker) view.findViewById(R.id.np_minute);

        if (!isTimeEnable) {
            llayTime.setVisibility(View.GONE);
        }
        if (!isDateEnable) {
            mDatePicker.setVisibility(View.GONE);
        }
        initPickers();

        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        BackgroundUtil backgroundUtil = new BackgroundUtil(context);
        backgroundUtil.setPressColor(btnConfirm, R.color.bg_green, 0);
        btnConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!"".equals(date) && !"".equals(time)) {
                    tvDate.setText(date + " " + time);
                } else if ("".equals(date)) {
                    tvDate.setText(time);
                } else if ("".equals(time)) {
                    tvDate.setText(date);
                }

                dialogDial.dismiss();
            }
        });
        dialogDial.setContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        dialogDial.show();
    }

    /**
     * 初始化两个表盘
     */
    private void initPickers() {
        if (isDateEnable) {
            try {
                if (minDate > 0 && maxDate > 0 && maxDate > minDate) {
                    mDatePicker.setMinDate(minDate);
                    mDatePicker.setMaxDate(maxDate);
                    mDatePicker.setCalendarViewShown(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int monthOfYear = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            date = mDatePicker.getYear() + "-" + (format(mDatePicker.getMonth() + 1)) + "-" + (format(mDatePicker.getDayOfMonth()));

//			date_time = getCurrentTime();//设置默认时间
            /**
             *  init (int year, int monthOfYear, int dayOfMonth,
             *   DatePicker.OnDateChangedListener onDateChangedListener)
             *   将日历初始化为当前系统时间，并设置其事件监听
             */

            mDatePicker.init(year, monthOfYear, dayOfMonth,
                    new OnDateChangedListener() {
                        public void onDateChanged(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                            //设置标题，注意月份是从0开始的，所以我们在显示的时候要+1
                            date = year + "-" + (format(monthOfYear + 1)) + "-" + format(dayOfMonth);
                        }
                    });
        }

        if (isTimeEnable) {
            time = "00:00";
            npHour.setMinValue(0);
            npHour.setMaxValue(23);

            npHour.setFormatter(this);

            switch (timeMode) {
                case MODE_TIME_NORMAL:
                    npMinute.setMinValue(0);
                    npMinute.setMaxValue(59);
                    break;
                case MODE_TIME_15M:
                    npMinute.setMinValue(0);
                    npMinute.setMaxValue(3);
                    minuteValues = new String[]{"00", "15", "30", "45"};
                    npMinute.setDisplayedValues(minuteValues);
                    break;
            }


            npMinute.setFormatter(this);
            npHour.setOnValueChangedListener(new OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    updateTime();
                }
            });
            npMinute.setOnValueChangedListener(new OnValueChangeListener() {

                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    updateTime();
                }
            });
        }

//		mTimePicker.setIs24HourView(true);//是否显示24小时制？默认false
//		mTimePicker.setCurrentHour(DEFAULT_HOUR);     //设置当前小时
//		mTimePicker.setCurrentMinute(DEFAULT_MINUTE); //设置当前分钟

//		mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
//			
//			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//				isTimeChanged = true;
//				time = hourOfDay+":"+minute;
//			}
//		});
    }

    private void updateTime() {
        if (timeMode == MODE_TIME_NORMAL) {
            time = format(npHour.getValue()) + ":" + format(npMinute.getValue());
        } else {
            time = format(npHour.getValue()) + ":" + minuteValues[npMinute.getValue()];
        }
    }


    @Override
    public String format(int value) {
        String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
    }

    public interface OnDateTimeChangedListener {
        void dateTimeChanged(String date);
    }
}
