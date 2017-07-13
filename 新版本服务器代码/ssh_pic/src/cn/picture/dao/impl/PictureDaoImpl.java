package cn.picture.dao.impl;

import java.util.Iterator;
import java.util.List;

import cn.picture.dao.PictureDao;
import cn.picture.domain.hw_Picture;


public class PictureDaoImpl extends BaseDaoImpl<hw_Picture> implements PictureDao{
	
		//保存图片信息
	    @Override
		public void savepicture(hw_Picture pic){
	    	save(pic);
	    }
		
		//更新图片信息
	    @Override
		public void updatepicture(hw_Picture pic){
	    	update(pic);
	    }
		
		//得到图片信息
	    @Override
		public hw_Picture getpicbypicid(int picid){
	    	List<hw_Picture> ul = (List<hw_Picture>)find("from hw_Picture p where p.Pic_id=?0 " , picid);
			if (ul.size() == 1){
					return (hw_Picture)ul.get(0);
			}
			return null;
	    }
	    
		@Override
		public List<hw_Picture> getfinishedpic(){
			List<hw_Picture> ul = (List<hw_Picture>)find("from hw_Picture p where p.Pic_isvis = 1" );
			return ul;
		}
		
		//得到所有图片
		@Override
		public List<hw_Picture> getallPic(){
			return findAll(hw_Picture.class);
		}
		//得到所有图片
		@Override
		public List<hw_Picture> getallunPic(){
			List<hw_Picture> ul = (List<hw_Picture>)find("from hw_Picture p where p.Pic_isvis = 0" );
			return ul;
		}
	    
	    
	    @Override
	    public boolean getpicbypassway(String passway){
	    	List<hw_Picture> ul = (List<hw_Picture>)find("from hw_Picture p where p.Pic_passway=?0 " , passway);
			if (ul.size() >= 1){
				return true;
			}
			return false;	
	    }
	    @Override
	    public hw_Picture getpicbypass(String passway){
	    	List<hw_Picture> ul = (List<hw_Picture>)find("from hw_Picture p where p.Pic_passway=?0 " , passway);
			return ul.get(0);
					
	    }
	    
	  //得到未完成所有图片
	  	@Override
	  	public List<hw_Picture> getallPic1(){
	 		List<hw_Picture> ul = (List<hw_Picture>)find("from hw_Picture p where p.Pic_isvis = 0" );
	 		return ul;
	  	}
	    
	   	//得到用户总数
	   @Override
		public long getPiccount(){
			return findCount(hw_Picture.class);
		}
		

}
