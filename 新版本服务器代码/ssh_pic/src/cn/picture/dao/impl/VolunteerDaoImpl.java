package cn.picture.dao.impl;

import java.io.Serializable;
import java.util.List;

import cn.picture.dao.VolunteerDao;
import cn.picture.domain.hw_Volunteer;



public class VolunteerDaoImpl extends BaseDaoImpl<hw_Volunteer> implements VolunteerDao{
	 //保存用户信息
	   @Override
		public void savePerson(hw_Volunteer person){
			save(person);
		}
		
		//通过用户手机号和密码查询用户
	   @Override
		public hw_Volunteer getPesonByphoneandpassward(String phonenumber , String passward){
			List<hw_Volunteer> ul = (List<hw_Volunteer>)find("from  hw_Volunteer p where p.V_telephone=?0 and p.V_password=?1 " ,
					phonenumber , passward);
			// 返回查询得到的第一个person对象
			if (ul.size() >= 1)
					return (hw_Volunteer)ul.get(0);
			return null;
		}
		
		//通过用户手机号查询用户
	   @Override
		public hw_Volunteer getPesonByphone(String phonenumber){
			List<hw_Volunteer> ul = (List<hw_Volunteer>)find("from hw_Volunteer p where p.V_telephone=?0 ",phonenumber);
			// 返回查询得到的第一个hw_Volunteer对象
			if (ul.size() == 1)
				return (hw_Volunteer)ul.get(0);
			return null;
		}
		
		//更新用户信息
	   @Override
		public void updatePerson(hw_Volunteer person){
			update(person);
		}
		
	   	//得到用户总数
	   @Override
		public long getPersoncount(){
			return findCount(hw_Volunteer.class);
		}
		
		//得到所有用户
		public List<hw_Volunteer> getallPerson(){
			List<hw_Volunteer> ul = (List<hw_Volunteer>)find("from hw_Volunteer p");
			return ul;
		}
		//通过VID得到用户
		public hw_Volunteer getpersonvid(int vid){
			List<hw_Volunteer> ul = (List<hw_Volunteer>)find("from hw_Volunteer p where p.V_id = ?0",vid);
			if (ul.size() == 1)
				return (hw_Volunteer)ul.get(0);
			return null;
		}

}
