package cn.picture.dao;

import java.io.Serializable;
import java.util.List;



import cn.picture.domain.hw_Tag;
import cn.picture.domain.hw_Tag_push;



public interface Tag_pushDao extends BaseDao<hw_Tag_push> {
	//通过VID，PIC_ID得到LABLE
	public String getlablebyvidpic(int vid,int pic);
	//通过VID得到所有的TAG_push记录
	public List<hw_Tag_push> getlablebyvid(int vid);
	
	public void updatetagpush(hw_Tag_push tagpush);
	
	//得到所有记录
	public List<hw_Tag_push> getall();
}
