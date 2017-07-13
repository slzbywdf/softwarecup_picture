package cn.picture.domain;

import java.io.Serializable;


public class hw_Administrator implements Serializable{
	private int A_id;
	private String A_telephone;
	private String A_password;
	private String A_nick;
	private int A_icon;
	public int getA_id() {
		return A_id;
	}
	public void setA_id(int a_id) {
		A_id = a_id;
	}
	public String getA_telephone() {
		return A_telephone;
	}
	public void setA_telephone(String a_telephone) {
		A_telephone = a_telephone;
	}
	public String getA_password() {
		return A_password;
	}
	public void setA_password(String a_password) {
		A_password = a_password;
	}
	public String getA_nick() {
		return A_nick;
	}
	public void setA_nick(String a_nick) {
		A_nick = a_nick;
	}
	public int getA_icon() {
		return A_icon;
	}
	public void setA_icon(int a_icon) {
		A_icon = a_icon;
	}
	
}
