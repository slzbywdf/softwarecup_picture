package cn.picture.dao;

import java.util.List;

import cn.picture.domain.hw_Picture;

public interface PictureDao extends BaseDao<hw_Picture> {
	
	//保存图片信息
	public void savepicture(hw_Picture pic);
	
	//更新图片信息
	public void updatepicture(hw_Picture pic);
	
	//得到图片信息
	public hw_Picture getpicbypicid(int picid);
	
	//得到所有标签化后的图片信息
	public List<hw_Picture> getfinishedpic();
		
	public List<hw_Picture> getallunPic();
   	//得到用户总数
	public long getPiccount();
	
	//得到所有用户
	public List<hw_Picture> getallPic();
	
	//判断该图片名有无上传
	public boolean getpicbypassway(String passway);
	
	//通过路径得到图片
	public hw_Picture getpicbypass(String passway);
	
	//得到所有为完成的图片
	public List<hw_Picture> getallPic1();
}
