package cn.picture.dao;

import java.io.Serializable;
import java.util.List;

import cn.picture.domain.hw_Volunteer;


public interface VolunteerDao extends BaseDao<hw_Volunteer> {
	//保存用户信息
	public void savePerson(hw_Volunteer person);
		
	//通过用户手机号和密码查询用户
	public hw_Volunteer getPesonByphoneandpassward(String phonenumber , String passward);	
		
	//通过用户手机号查询用户
	public hw_Volunteer getPesonByphone(String phonenumber);
		
	//更新用户信息
	public void updatePerson(hw_Volunteer person);
		
	//得到用户总数
	public long getPersoncount();
		
	//得到所有用户
	public List<hw_Volunteer> getallPerson();
	
	//通过VID得到用户
	public hw_Volunteer getpersonvid(int vid);
	
}
