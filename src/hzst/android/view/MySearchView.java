package hzst.android.view;

import hzst.android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 搜索框控件
 * 
 * @author wt
 * 
 */
public class MySearchView extends LinearLayout {
	public EditText etKey;
	private Button btnSearch;
	private OnSearchListener listener;

	public MySearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MySearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	public void setOnSearchListener(OnSearchListener listener) {
		this.listener = listener;
	}
	public void setSetEdText(String onSetEdText) {
		etKey.setText(onSetEdText);
	}
	private void initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.view_search_view, null);
		etKey = (EditText) view.findViewById(R.id.et_key);
		btnSearch = (Button) view.findViewById(R.id.btn_search);
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.doSearch(etKey.getText().toString().trim());
			}
		});

		addView(view);
	}

	public interface OnSearchListener {
		void doSearch(String key);
	}

}
