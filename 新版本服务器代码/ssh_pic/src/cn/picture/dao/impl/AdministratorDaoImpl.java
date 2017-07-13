package cn.picture.dao.impl;

import java.io.Serializable;
import java.util.List;

import cn.picture.dao.AdministratorDao;
import cn.picture.domain.hw_Administrator;




public class AdministratorDaoImpl extends BaseDaoImpl<hw_Administrator> implements AdministratorDao{
	 //保存用户信息
	   @Override
		public void savePerson(hw_Administrator person){
			save(person);
		}
		
		//通过用户手机号和密码查询用户
	   @Override
		public hw_Administrator getPesonByphoneandpassward(String phonenumber , String passward){
			List<hw_Administrator> ul = (List<hw_Administrator>)find("from hw_Administrator p where p.A_telephone=?0 and p.A_password=?1 " ,
					phonenumber , passward);
			// 返回查询得到的第一个person对象
			if (ul.size() >= 1)
					return (hw_Administrator)ul.get(0);
			return null;
		}
		
		//通过用户手机号查询用户
	   @Override
		public hw_Administrator getPesonByphone(String phonenumber){
			List<hw_Administrator> ul = (List<hw_Administrator>)find("from hw_Administrator p where p.A_telephone=?0 ",phonenumber);
			// 返回查询得到的第一个hw_Volunteer对象
			if (ul.size() == 1)
				return (hw_Administrator)ul.get(0);
			return null;
		}
		
		//更新用户信息
	   @Override
		public void updatePerson(hw_Administrator person){
			update(person);
		}
		
	   	//得到用户总数
	   @Override
		public long getPersoncount(){
			return findCount(hw_Administrator.class);
		}
		
		//得到所有用户
		public List<hw_Administrator> getallPerson(){
			List<hw_Administrator> ul = (List<hw_Administrator>)find("from hw_Administrator p");
			return ul;
		}

}
