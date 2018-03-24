package hzst.android.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * 和{@link CommonAdapter}配合使用
 * @author wt#
 * @deprecated 已经改为{@link CommonAdapter}的内部类
 */
@Deprecated
public class ViewHolder
{
	private final SparseArray<View> mViews;
	private View mConvertView;
	private int position;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position)
	{
		this.mViews = new SparseArray<View>();
		this.position = position;
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		//setTag
		mConvertView.setTag(this);	
		
	}

	/**
	 * @deprecated 返回的下标有时候不正确，可以在重写{@link CommonAdapter}的convert时从参数中获得。
	 * @return
	 */
	@Deprecated
	public int getPosition() {
		return position;
	}



	/**
	 * 拿到一个ViewHolder对象
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position)
	{

		if (convertView == null)
		{
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}


	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId)
	{
		
		View view = mViews.get(viewId);
		if (view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView()
	{
		return mConvertView;
	}
	
	public ViewHolder setText(int viewId, String text)  
	{  
		TextView view = getView(viewId);  
		view.setText(text);  
		return this;  
	} 
	
	public void setVisible(int viewId, int visibility){
		View view = getView(viewId);
		view.setVisibility(visibility);
	}
	
	public void setBackground(int viewId, int resid){
		View view = getView(viewId);
		view.setBackgroundResource(resid);
	}
	
}

