package cn.picture.dao.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import cn.picture.dao.SemanticsDao;
import cn.picture.domain.hw_Semantics;
import cn.picture.domain.hw_Tag;




public class SemanticsDaoImpl extends BaseDaoImpl<hw_Semantics> implements SemanticsDao {
	//得到所有信息
	@Override
	public List<hw_Semantics> getALL(){
		List<hw_Semantics> ul = (List<hw_Semantics>)find("from hw_Semantics p");
		return ul;
	}
	//通过pid得到其所有标签，权zhong
	public String getbypic(int pic){
		List<hw_Semantics> ul = (List<hw_Semantics>)find("from hw_Semantics p where p.Pic_id = ?0",pic);
		Iterator<hw_Semantics> it = ul.iterator();
		String result = "";
		while(it.hasNext()){
			hw_Semantics temp = it.next();
			result =result+temp.getLabel_handle()+","+temp.getNum()+"-";
		}
		if(!result.isEmpty())
			result=result.substring(0,result.length()-1);
		return result;
	}
	
	//通过PICID删除图片
	@Override
	public void deletebypicid(int picid){
		deletehql("delete hw_Semantics h where Pic_id = ?0",picid);
	}
	
	//通过pid得到其标签总个数
	public int getweightbypic(int pic){
		List<hw_Semantics> ul = (List<hw_Semantics>)find("from hw_Semantics p where p.Pic_id = ?0",pic);
		Iterator<hw_Semantics> it = ul.iterator();
		int res = 0;
		while(it.hasNext()){
			hw_Semantics temp = it.next();
			res =res+temp.getNum();
		}
		return res;
	}
	
}
