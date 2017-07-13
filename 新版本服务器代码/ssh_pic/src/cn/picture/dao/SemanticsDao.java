package cn.picture.dao;

import java.io.Serializable;
import java.util.List;

import cn.picture.domain.hw_Semantics;





public interface SemanticsDao extends BaseDao<hw_Semantics> {
	//得到所有信息
	public List<hw_Semantics> getALL();
	//通过pid得到其所有标签，权zhong
	public String getbypic(int pic);
	//通过PICID删除图片
	public void deletebypicid(int picid);
	//通过pid得到其标签总个数
	public int getweightbypic(int pic);
}
