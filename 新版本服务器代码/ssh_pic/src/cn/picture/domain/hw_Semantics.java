package cn.picture.domain;

import java.io.Serializable;


public class hw_Semantics implements Serializable{
	private int S_id;
	private int Pic_id;
	private String Label_handle;
	private int Num;
	public int getPic_id() {
		return Pic_id;
	}
	public void setPic_id(int pic_id) {
		Pic_id = pic_id;
	}
	public String getLabel_handle() {
		return Label_handle;
	}
	public void setLabel_handle(String label_handle) {
		Label_handle = label_handle;
	}
	public int getNum() {
		return Num;
	}
	public void setNum(int num) {
		Num = num;
	}
	public int getS_id() {
		return S_id;
	}
	public void setS_id(int s_id) {
		S_id = s_id;
	}
	
}
