package cn.picture.dao.impl;

import java.util.Iterator;
import java.util.List;

import cn.picture.dao.Day_pushDao;
import cn.picture.domain.hw_Day_push;


public class Day_pushDaoImpl extends BaseDaoImpl<hw_Day_push> implements Day_pushDao {
	//通过VID得到所有的PICID
	@Override
	public String getPicIDbyVID(int VID){
		String picids = "";
		List<hw_Day_push> ll = (List<hw_Day_push>)find("from hw_Day_push d where d.V_id = ?0",VID);
		Iterator<hw_Day_push> it = ll.iterator();
		while(it.hasNext()){
			hw_Day_push temp = it.next();
			picids = picids + temp.getPic_id()+"-";
		}
		if(!picids.isEmpty())
			picids=picids.substring(0,picids.length()-1);
		return picids;
			
	}
	@Override
	public List<hw_Day_push> getday_pushbyphone(int vid){
		List<hw_Day_push> ul =(List<hw_Day_push>)find("from hw_Day_push t where t.V_id =?0 and t.D_isvis=0 and unix_timestamp(t.D_time)>=unix_timestamp(current_date())",vid);
		return ul;
	}
	//通过VID、pid得到相应图片推送
	@Override
	public hw_Day_push getpushbyVIDPID(int VID,int pid){
		List<hw_Day_push> ll = (List<hw_Day_push>)find("from hw_Day_push d where d.V_id = ?0 and d.Pic_id = ?1",VID,pid);
		if (ll.size() >= 1)
		{
			return (hw_Day_push)ll.get(0);
		}
		return null;
	}
}
