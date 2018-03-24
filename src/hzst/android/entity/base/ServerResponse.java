package hzst.android.entity.base;

import java.io.Serializable;

/**
 * 服务端返回的响应信息，大部分实体类都需要继承该类
 */
public class ServerResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private int resultCode;//结果码(成功/失败等类型)
	private String promptMessage;//提示信息

	private int status;
	private String data;

	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int result) {
		this.resultCode = result;
	}
	public String getPromptMessage() {
		return promptMessage;
	}
	public void setPromptMessage(String info) {
		this.promptMessage = info;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
