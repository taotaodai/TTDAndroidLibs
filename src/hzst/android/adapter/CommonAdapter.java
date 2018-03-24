package hzst.android.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * 万能ViewGroup适配器
 *
 * @param <T>
 * @author wt#
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected int mItemLayoutId;


    public CommonAdapter() {

    }

    public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
    }

    public void setData(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mDatas != null) {
            return mDatas.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = getViewHolder(position, convertView, parent);

        convert(viewHolder, getItem(position));
        convert(viewHolder, getItem(position), position);
        convert(viewHolder, getItem(position), position, parent);
        return viewHolder.getConvertView();

    }

    @Deprecated
    public void convert(ViewHolder helper, T item) {
    }

    public void convert(ViewHolder helper, T item, int position) {
    }

    public void convert(ViewHolder helper, T item, int position, ViewGroup parent) {
    }

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }


    public static class ViewHolder {
        private final SparseArray<View> mViews;
        private View mConvertView;
        private int position;

        private ViewHolder(Context context, ViewGroup parent, int layoutId,
                           int position) {
            this.mViews = new SparseArray<View>();
            this.position = position;
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            //setTag
            mConvertView.setTag(this);

        }

        /**
         * @return
         * @deprecated 返回的下标有时候不正确，可以在重写{@link CommonAdapter}的convert时从参数中获得。
         */
        @Deprecated
        public int getPosition() {
            return position;
        }


        /**
         * 拿到一个ViewHolder对象
         *
         * @param context
         * @param convertView
         * @param parent
         * @param layoutId
         * @param position
         * @return
         */
        private static ViewHolder get(Context context, View convertView,
                                      ViewGroup parent, int layoutId, int position) {

            if (convertView == null) {
                return new ViewHolder(context, parent, layoutId, position);
            }
            return (ViewHolder) convertView.getTag();
        }


        /**
         * 通过控件的Id获取对于的控件，如果没有则加入views
         *
         * @param viewId
         * @return
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int viewId) {

            View view = mViews.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public View getConvertView() {
            return mConvertView;
        }

        public void setText(int viewId, CharSequence text) {
            TextView view = getView(viewId);
            view.setText(text);
        }

        public void setVisible(int viewId, int visibility) {
            View view = getView(viewId);
            view.setVisibility(visibility);
        }

        public void setBackground(int viewId, int resid) {
            View view = getView(viewId);
            view.setBackgroundResource(resid);
        }

    }

}

