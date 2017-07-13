package cn.picture.dao.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cn.picture.dao.TagDao;
import cn.picture.domain.hw_Tag;


public class TagDaoImpl extends BaseDaoImpl<hw_Tag> implements TagDao {
	//通过VID得到他打过的所有图片PIC，结果需要去重
	@Override
	public String getPicbyVID(int vid){
		List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t where t.V_id = ?0",vid);
		Set myset = new HashSet();
		Iterator<hw_Tag> it = ll.iterator();
		String result = "";
		while(it.hasNext()){
			int temp = it.next().getPic_id();
			if(myset.contains(temp)){
				continue;
			}else{
				result =result+String.valueOf(temp)+"-";
				myset.add(temp);
			}
		}
		if(!result.isEmpty())
			result=result.substring(0,result.length()-1);
		return result;
	}
	@Override
	public List<hw_Tag> getPicallbyVID(int vid){
		List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t where t.V_id = ?0",vid);
		return ll;
	}
	//通过VID，PIC删去相应信息
	@Override
	public void deletebyvidpid(int vid,int pid){
		deletehql("delete hw_Tag h where h.V_id = ?0 and h.Pic_id = ?1",vid,pid);
	}
	
	//通过VID，PIC信息
	@Override
	public boolean existbyvidpid(int vid,int pid){
		List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t where t.V_id = ?0 and t.Pic_id = ?1",vid,pid);
		if(ll!=null&&ll.size()>0)
			return true;
		return false;
	}
	
	//得到所有图片有标签的PIC，结果需要去重
		public String getPic(){
			List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t");
			Set myset = new HashSet();
			Iterator<hw_Tag> it = ll.iterator();
			String result = "";
			while(it.hasNext()){
				int temp = it.next().getPic_id();
				if(myset.contains(temp)){
					continue;
				}else{
					result =result+String.valueOf(temp)+"-";
					myset.add(temp);
				}
			}
			if(!result.isEmpty())
				result=result.substring(0,result.length()-1);
			return result;
		}
	//通过VID得到他打过的所有标签
	@Override
	public String getLabelbyVID(int vid){
		List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t where t.V_id = ?0",vid);
		Iterator<hw_Tag> it = ll.iterator();
		String result = "";
		while(it.hasNext()){
			result =result+it.next().getLabel()+"-";
		}
		if(!result.isEmpty())
			result=result.substring(0,result.length()-1);
		return result;
	}
	
	//通过picid得到LABEL
	public String getLabelbyPicid(int picid){
		List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t where t.Pic_id = ?0",picid);
		Iterator<hw_Tag> it = ll.iterator();
		String result = "";
		while(it.hasNext()){
			result =result+it.next().getLabel()+"-";
		}
		if(!result.isEmpty())
			result=result.substring(0,result.length()-1);
		return result;
	}
	
	//通过picid得到信息
	@Override
	public List<hw_Tag> getbyPicid(int picid){
		List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t where t.Pic_id = ?0",picid);
		return ll;
	}
	
	//得到所有信息
	@Override
	public List<hw_Tag> getPic1(){
		List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t");
		return ll;
	}
	
	//通过vidpic得到label
		public String getLabelbyPicidvid(int vid,int picid){
			List<hw_Tag> ll =(List<hw_Tag>)find("from hw_Tag t where t.Pic_id = ?0 and t.V_id = ?1",picid,vid);
			Iterator<hw_Tag> it = ll.iterator();
			String result = "";
			while(it.hasNext()){
				result =result+it.next().getLabel()+"-";
			}
			if(!result.isEmpty())
				result=result.substring(0,result.length()-1);
			return result;
		}
}
