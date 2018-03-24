package hzst.android.view.event;

import hzst.android.util.BackgroundUtil;
import android.content.Context;
import android.widget.PopupWindow;
/**
 * PopupWindow关闭事件监听
 * @author wt
 *
 */
public class MyPopDismissListener implements PopupWindow.OnDismissListener{
	private Context context;
	
	public MyPopDismissListener(Context context) {
		this.context = context;
	}

	@Override
	public void onDismiss() {
		BackgroundUtil backgroundUtil = new BackgroundUtil(context);
		backgroundUtil.setWindowAlpha(1f);
	}

}
