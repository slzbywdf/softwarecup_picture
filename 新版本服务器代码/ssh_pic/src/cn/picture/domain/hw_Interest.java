package cn.picture.domain;

import java.io.Serializable;


public class hw_Interest implements Serializable{
	private int I_id;
	private String Inter;
	private String I_level;
	public int getI_id() {
		return I_id;
	}
	public void setI_id(int i_id) {
		I_id = i_id;
	}
	public String getInter() {
		return Inter;
	}
	public void setInter(String inter) {
		Inter = inter;
	}
	public String getI_level() {
		return I_level;
	}
	public void setI_level(String i_level) {
		I_level = i_level;
	}
	
}
