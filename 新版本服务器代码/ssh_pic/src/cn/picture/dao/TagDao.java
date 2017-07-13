package cn.picture.dao;

import java.io.Serializable;
import java.util.List;


import cn.picture.domain.hw_Tag;



public interface TagDao extends BaseDao<hw_Tag> {
	//通过VID得到他打过的所有图片PIC，结果需要去重
	public String getPicbyVID(int vid);
	//通过VID得到所有TAG信息
	public List<hw_Tag> getPicallbyVID(int vid);
	//通过VID，PIC删去相应信息
	public void deletebyvidpid(int vid,int pid);
	//通过VID，PIC信息
	public boolean existbyvidpid(int vid,int pid);
	//得到所有图片有标签的PIC，结果需要去重
	public String getPic();
	
	//通过VID得到他打过的所有标签
	public String getLabelbyVID(int vid);
	
	//通过picid得到LABEL
	public String getLabelbyPicid(int picid);
	
	//通过picid得到信息
	public List<hw_Tag> getbyPicid(int picid);
	
	//得到所有信息
	public List<hw_Tag> getPic1();
	
	//通过vidpic得到label
	public String getLabelbyPicidvid(int vid,int picid);
	
	
}
