package cn.picture.dao.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.picture.dao.TagDao;
import cn.picture.dao.Tag_pushDao;
import cn.picture.domain.hw_Tag;
import cn.picture.domain.hw_Tag_push;
import cn.picture.domain.hw_Volunteer;


public class Tag_pushDaoImpl extends BaseDaoImpl<hw_Tag_push> implements Tag_pushDao {
	//通过VID，PIC_ID得到LABLE
	@Override
	public String getlablebyvidpic(int vid,int pic){
		List<hw_Tag_push> ll =(List<hw_Tag_push>) find("from hw_Tag_push t where t.V_id = ?0 and t.Pic_id = ?1",vid,pic);
		if(ll==null)
			return "";
		return ll.get(0).getLabel();
	}	
	//通过VID得到所有的TAG_push记录
	@Override
	public List<hw_Tag_push> getlablebyvid(int vid){
		List<hw_Tag_push> ll =(List<hw_Tag_push>) find("from hw_Tag_push t where t.V_id = ?0",vid);
		return ll;
	}
	
	//更新信息
	@Override
	public void updatetagpush(hw_Tag_push tagpush){
		update(tagpush);
	}
	//得到所有记录
	public List<hw_Tag_push> getall(){
		List<hw_Tag_push> ll =(List<hw_Tag_push>) find("from hw_Tag_push t ");
		return ll;
	}
}
