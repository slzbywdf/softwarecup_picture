package cn.picture.dao;

import java.io.Serializable;
import java.util.List;

import cn.picture.domain.hw_Administrator;



public interface AdministratorDao extends BaseDao<hw_Administrator> {
	//保存用户信息
	public void savePerson(hw_Administrator person);
		
	//通过用户手机号和密码查询用户
	public hw_Administrator getPesonByphoneandpassward(String phonenumber , String passward);	
		
	//通过用户手机号查询用户
	public hw_Administrator getPesonByphone(String phonenumber);
		
	//更新用户信息
	public void updatePerson(hw_Administrator person);
		
	//得到用户总数
	public long getPersoncount();
		
	//得到所有用户
	public List<hw_Administrator> getallPerson();
	
}
