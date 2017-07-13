package cn.picture.domain;

import java.io.Serializable;


public class hw_Volunteer implements Serializable{
	private int V_id;
	private String V_telephone;
	private String V_password;
	private String V_nick;
	private int V_icon;
	private String V_emial;
	private int V_credits;
	private String V_major;
	private int V_renwu;
	public int getV_id() {
		return V_id;
	}
	public void setV_id(int v_id) {
		V_id = v_id;
	}
	public String getV_telephone() {
		return V_telephone;
	}
	public void setV_telephone(String v_telephone) {
		V_telephone = v_telephone;
	}
	public String getV_password() {
		return V_password;
	}
	public void setV_password(String v_password) {
		V_password = v_password;
	}
	public String getV_nick() {
		return V_nick;
	}
	public void setV_nick(String v_nick) {
		V_nick = v_nick;
	}
	public int getV_icon() {
		return V_icon;
	}
	public void setV_icon(int v_icon) {
		V_icon = v_icon;
	}
	public String getV_emial() {
		return V_emial;
	}
	public void setV_emial(String v_emial) {
		V_emial = v_emial;
	}
	public int getV_credits() {
		return V_credits;
	}
	public void setV_credits(int v_credits) {
		V_credits = v_credits;
	}
	public String getV_major() {
		return V_major;
	}
	public void setV_major(String v_major) {
		V_major = v_major;
	}
	public int getV_renwu() {
		return V_renwu;
	}
	public void setV_renwu(int v_renwu) {
		V_renwu = v_renwu;
	}
}
