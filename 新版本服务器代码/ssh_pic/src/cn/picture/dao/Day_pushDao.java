package cn.picture.dao;

import java.io.Serializable;
import java.util.List;

import cn.picture.domain.hw_Day_push;


public interface Day_pushDao extends BaseDao<hw_Day_push> {
	//通过VID得到所有的PICID
	public String getPicIDbyVID(int VID);
	
	//得到今天推送的记录
	public List<hw_Day_push> getday_pushbyphone(int vid);
	
	//通过VID、pid得到相应图片推送
	public hw_Day_push getpushbyVIDPID(int VID,int pid);
}
