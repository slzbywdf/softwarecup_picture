package cn.picture.service;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.picture.domain.hw_Picture;
import cn.picture.exception.tranException;

public interface tranManager
{
	/**
	 * 根据用户key手机号，返回用户信息
	 * @param pptelephone
	 * @return
	 * @throws tranException
	 */
	JSONObject getPerson(String pptelephone)throws tranException;
	/**
	 * 根据用户名，密码验证登录是否成功
	 * @param pptelephone 登录的用户名
 	 * @param ppassword 登录的密码
	 * @return 登录成功返回用户phonenumber，否则返回"-1"
	 */
	String validLogin(String pptelephone , String ppassword)throws tranException;
	
	/**
	 * 根据用户电话号码和密码进行注册
	 * @param pptelephone
	 * @param ppassword
	 * @return
	 * @throws tranException
	 */
	String register(String pptelephone , String ppassword)throws tranException;
	
    /*
     * 根据用户手机号修改用户资料
     * @param 用户信息
     */
	String changemessage(String pptelephone , String pnick ,String pemail ,String pmajor ,String pinter,int icon)throws tranException;
	
	/*
	 * 根据用户手机号进行每日推送图片地址的获取
	 * @return  JSONObject{图片地址}
	 * 
	 */
	JSONObject getDaypushbyphone(String pptelephone)throws tranException;
	
	/*
	 * 将图片路径保存到picture_all表中
	 * 
	 */
	boolean  picup(String passway,String filename)throws tranException;
	
	/*
	 * 通过用户手机号码获取用户打过标签的图片id，passway，tag，isvis；
	 */
	 String getpersonhistorybyphone(String pptelephone)throws tranException;
	
	/*
	 * 通过用户手机号码获取曾向用户推送过但用户未打过标签的图片id，passway；
	 */
	String showpersonallpicture(String pptelephone)throws tranException;
	//根据用户phone，图片picid，上传标签
	public int tagup(String pptelephone,String picid,String tag)throws tranException;
	
	//用于密码修改
	public int passwordchange(String pptelephone,String oldpasssword,String newpassword)throws tranException;
	
	
	//管理员部分
	public JSONArray P_getallPerson( )throws tranException;
	
	public String P_changemessage(String pptelephone , String pnick ,String pemail ,String pmajor ,String pinter)throws tranException;
	
	public JSONArray P_picresult()throws tranException;
	public String register1(String pptelephone , String ppassword)throws tranException;
	public String validLogin1(String phonenumber , String passward) throws tranException;
	
	//图片搜索功能
	//Map初始化
	public HashMap<String,HashMap<Integer,Integer>> P_search_init();
	
	//得到用户打过标签的Pid数据
	public String[] search_tag_person(String phone);
	
	public List<hw_Picture> showallpicture();
	
	public String getpssbypicid(int picid);
	//通过pid，vid得到标签推送
	public String gettagpush(int pid,String vid);
	
	public void  chuli(String temp);
}
