package hzst.android.form.entity;

import android.view.View;

import hzst.android.form.view.FMBaseView;

/**
 * Created by wt on 2017/6/8.
 */
public class ViewCollection {
    private View fmView;
    private View parentView;
    private View viewMargin;
    private int parentIndex;
    private boolean isVisible;

    public View getViewMargin() {
        return viewMargin;
    }

    public void setViewMargin(View viewMargin) {
        this.viewMargin = viewMargin;
    }

    public View getFmView() {
        return fmView;
    }

    public void setFmView(View fmView) {
        this.fmView = fmView;
    }

    public View getParentView() {
        return parentView;
    }

    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setVisible(boolean visible){
        this.isVisible = visible;
        parentView.setVisibility(visible ? View.VISIBLE : View.GONE);
        viewMargin.setVisibility(visible ? View.VISIBLE : View.GONE);
        ((FMBaseView)fmView).getInfo().setMustFill(visible);
    }
}
