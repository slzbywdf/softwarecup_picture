package cn.picture.dao;

import java.io.Serializable;
import java.util.List;

import cn.picture.domain.hw_Have;
import cn.picture.domain.hw_Volunteer;


public interface HaveDao extends BaseDao<hw_Have> {
	//删除某个志愿者兴趣
	public void deletebyvid(int vid);
	//保存某个拥有兴趣信息
	public void saveHave(hw_Have person);
	//通过Vid得到某个人所有兴趣
	public List<hw_Have> getvolunInter(int vid);
}
