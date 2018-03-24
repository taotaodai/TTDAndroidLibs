package hzst.android.entity;

import java.io.Serializable;

import hzst.android.entity.base.ServerResponse;

/**
 * 用户基类
 * Created by Administrator on 2016/6/24.
 */
public class BaseUser extends ServerResponse{
	private static final long serialVersionUID = 1L;
	protected String userName;
    protected String passWord;
    protected String userId;
    protected String memberName;
    protected String departId;
    protected String departName;
    protected String rootDeptId;
    protected String rootDeptName;
    protected String position;//职位
    
    private String phoneNumber;//电话号码，多个用","隔开
    private String cornet;//短号

    public BaseUser() {}

	public BaseUser(String memberName,String phoneNumber) {
    	this.memberName = memberName;
    	this.phoneNumber = phoneNumber;
	}
	
	public BaseUser(String memberName,String phoneNumber,String cornet) {
    	this.memberName = memberName;
    	this.phoneNumber = phoneNumber;
    	this.cornet = cornet;
	}
	
	public BaseUser(String memberName,String phoneNumber,String cornet,String position) {
    	this.memberName = memberName;
    	this.phoneNumber = phoneNumber;
    	this.cornet = cornet;
    	this.position = position;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getRootDeptId() {
        return rootDeptId;
    }

    public void setRootDeptId(String rootDeptId) {
        this.rootDeptId = rootDeptId;
    }

    public String getRootDeptName() {
        return rootDeptName;
    }

    public void setRootDeptName(String rootDeptName) {
        this.rootDeptName = rootDeptName;
    }

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCornet() {
		return cornet;
	}

	public void setCornet(String cornet) {
		this.cornet = cornet;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
    
}
