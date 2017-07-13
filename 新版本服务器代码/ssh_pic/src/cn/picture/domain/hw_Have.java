package cn.picture.domain;

import java.io.Serializable;


public class hw_Have implements Serializable{
	private int H_id;//主键
	private int V_id;
	private int I_id;
	
	public int getH_id() {
		return H_id;
	}
	public void setH_id(int h_id) {
		H_id = h_id;
	}
	public int getV_id() {
		return V_id;
	}
	public void setV_id(int v_id) {
		V_id = v_id;
	}
	public int getI_id() {
		return I_id;
	}
	public void setI_id(int i_id) {
		I_id = i_id;
	}
	
}
