package cn.picture.domain;

import java.io.Serializable;


public class hw_Tag_push implements Serializable{
	private int T_id;
	private int V_id;
	private int Pic_id;
	private String Label;  //可存多个标签
	
	public int getT_id() {
		return T_id;
	}
	public void setT_id(int t_id) {
		T_id = t_id;
	}
	public int getV_id() {
		return V_id;
	}
	public void setV_id(int v_id) {
		V_id = v_id;
	}
	public int getPic_id() {
		return Pic_id;
	}
	public void setPic_id(int pic_id) {
		Pic_id = pic_id;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
}
