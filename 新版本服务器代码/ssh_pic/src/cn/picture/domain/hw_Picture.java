package cn.picture.domain;

import java.io.Serializable;
import java.sql.Timestamp;


public class hw_Picture implements Serializable{
	private int Pic_id;
	private String Pic_name;
	private String Pic_passway;
	private int Pic_isvis; //是否完成标签化
	private Timestamp Pic_finishtime;
	private String Final_label;
	private Timestamp Up_time;
	public int getPic_id() {
		return Pic_id;
	}
	public void setPic_id(int pic_id) {
		Pic_id = pic_id;
	}
	public String getPic_name() {
		return Pic_name;
	}
	public void setPic_name(String pic_name) {
		Pic_name = pic_name;
	}
	public String getPic_passway() {
		return Pic_passway;
	}
	public void setPic_passway(String pic_passway) {
		Pic_passway = pic_passway;
	}
	public int getPic_isvis() {
		return Pic_isvis;
	}
	public void setPic_isvis(int pic_isvis) {
		Pic_isvis = pic_isvis;
	}
	public Timestamp getPic_finishtime() {
		return Pic_finishtime;
	}
	public void setPic_finishtime(Timestamp pic_finishtime) {
		Pic_finishtime = pic_finishtime;
	}
	public String getFinal_label() {
		return Final_label;
	}
	public void setFinal_label(String final_label) {
		Final_label = final_label;
	}
	public Timestamp getUp_time() {
		return Up_time;
	}
	public void setUp_time(Timestamp up_time) {
		Up_time = up_time;
	}
	
}
