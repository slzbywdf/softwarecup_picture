package cn.picture.dao.impl;

import java.io.Serializable;
import java.util.List;

import cn.picture.dao.InterestDao;
import cn.picture.dao.VolunteerDao;
import cn.picture.domain.hw_Interest;
import cn.picture.domain.hw_Volunteer;



public class InterestDaoImpl extends BaseDaoImpl<hw_Interest> implements InterestDao{
	//通过I_id得到Interest
	@Override
	public hw_Interest getInterestById(int I_id){
		List<hw_Interest> ul = (List<hw_Interest>)find("from hw_Interest I where I.I_id =?0" ,I_id);
		if (ul.size() >= 1)
				return (hw_Interest)ul.get(0);
		return null;
	}
	//通过兴趣得到Interest
	@Override
	public hw_Interest getInterestByInter(String inter){
		List<hw_Interest> ul = (List<hw_Interest>)find("from hw_Interest I where I.Inter =?0" ,inter);
		if (ul.size() >= 1)
				return (hw_Interest)ul.get(0);
		return null;
	}

}
