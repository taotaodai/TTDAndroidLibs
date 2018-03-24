package hzst.android.form.entity;

import java.io.Serializable;

import hzst.android.form.info.BaseViewInfo;

/**
 * 由于创建表单控件方法中参数太多，所以把公共参数整合到一起
 * Created by wt on 2017/6/14.
 */
public class PublicViewData implements Serializable{
    private String title;//该项对应的标题
    private boolean isMustFill;
    private boolean readOnly;
    private boolean isNeedTitle = true;
    private int landHold = BaseViewInfo.LAND_HOLD_FULL;
    private int marginType;//边框线类型


    public PublicViewData(String title,boolean isMustFill,boolean readOnly,int marginType){
        this.title = title;
        this.isMustFill = isMustFill;
        this.readOnly = readOnly;
        this.marginType = marginType;
    }
    public PublicViewData(String title,boolean isMustFill,boolean readOnly,int marginType,boolean isNeedTitle){
        this.title = title;
        this.isMustFill = isMustFill;
        this.readOnly = readOnly;
        this.marginType = marginType;
        this.isNeedTitle = isNeedTitle;
    }

    public PublicViewData(String title,boolean isMustFill,boolean readOnly,int marginType,boolean isNeedTitle,int landHold){
        this.title = title;
        this.isMustFill = isMustFill;
        this.readOnly = readOnly;
        this.marginType = marginType;
        this.isNeedTitle = isNeedTitle;
        this.landHold = landHold;
    }

    public int getLandHold() {
        return landHold;
    }

    public void setLandHold(int landHold) {
        this.landHold = landHold;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMustFill() {
        return isMustFill;
    }

    public void setIsMustFill(boolean isMustFill) {
        this.isMustFill = isMustFill;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getMarginType() {
        return marginType;
    }

    public void setMarginType(int marginType) {
        this.marginType = marginType;
    }

    public boolean isNeedTitle() {
        return isNeedTitle;
    }

    public void setIsNeedTitle(boolean isNeedTitle) {
        this.isNeedTitle = isNeedTitle;
    }
}
