package cn.picture.domain;

import java.io.Serializable;
import java.sql.Timestamp;


public class hw_Day_push implements Serializable{
	private int D_id;//主键
	private int V_id;
	private int Pic_id;
	private Timestamp D_time;
	private int D_isvis; //用于判定用户又没有打该张图片
	public int getV_id() {
		return V_id;
	}
	
	public int getD_id() {
		return D_id;
	}

	public void setD_id(int d_id) {
		D_id = d_id;
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
	public Timestamp getD_time() {
		return D_time;
	}
	public void setD_time(Timestamp d_time) {
		D_time = d_time;
	}
	public int getD_isvis() {
		return D_isvis;
	}
	public void setD_isvis(int d_isvis) {
		D_isvis = d_isvis;
	}
	
}
