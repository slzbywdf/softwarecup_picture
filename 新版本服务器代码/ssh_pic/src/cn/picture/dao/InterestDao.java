package cn.picture.dao;

import java.io.Serializable;
import java.util.List;

import cn.picture.domain.hw_Interest;
import cn.picture.domain.hw_Volunteer;


public interface InterestDao extends BaseDao<hw_Interest> {
	//通过I_id得到Interest
	public hw_Interest getInterestById(int I_id);
	//通过兴趣得到Interest
	public hw_Interest getInterestByInter(String inter);
	
}
