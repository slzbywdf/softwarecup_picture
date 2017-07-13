package cn.picture.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import cn.picture.dao.HaveDao;
import cn.picture.dao.VolunteerDao;
import cn.picture.domain.hw_Have;
import cn.picture.domain.hw_Volunteer;


public class HaveDaoImpl extends BaseDaoImpl<hw_Have> implements HaveDao {
	//删除某个志愿者兴趣
	@Override
	public void deletebyvid(int vid){
		deletehql("delete hw_Have h where h.V_id = ?0",vid);
	}
	//保存某个拥有兴趣信息
	@Override
	public void saveHave(hw_Have have){
		save(have);
	}
	//通过Vid得到某个人所有兴趣
	@Override
	public List<hw_Have> getvolunInter(int vid){
		List<hw_Have> lt =(List<hw_Have>)find("from hw_Have h where h.V_id = ?0",vid);	
		return lt;
	}
	
}
