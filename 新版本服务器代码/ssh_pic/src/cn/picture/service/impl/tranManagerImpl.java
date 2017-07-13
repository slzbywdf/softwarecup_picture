package cn.picture.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import cn.picture.dao.AdministratorDao;
import cn.picture.dao.Day_pushDao;
import cn.picture.dao.HaveDao;
import cn.picture.dao.InterestDao;
import cn.picture.dao.PictureDao;
import cn.picture.dao.SemanticsDao;
import cn.picture.dao.TagDao;
import cn.picture.dao.Tag_pushDao;
import cn.picture.dao.VolunteerDao;
import cn.picture.domain.hw_Administrator;
import cn.picture.domain.hw_Day_push;
import cn.picture.domain.hw_Have;
import cn.picture.domain.hw_Interest;
import cn.picture.domain.hw_Picture;
import cn.picture.domain.hw_Semantics;
import cn.picture.domain.hw_Tag;
import cn.picture.domain.hw_Tag_push;
import cn.picture.domain.hw_Volunteer;
import cn.picture.exception.tranException;
import cn.picture.schedule.pic_push;
import cn.picture.service.tranManager;

public class tranManagerImpl implements tranManager{
	
	static Logger log = Logger.getLogger(tranManagerImpl.class.getName());
	
	VolunteerDao Volunteerdao;
	InterestDao Interestdao;
	HaveDao Havedao;
	Day_pushDao Day_pushdao;
	TagDao Tagdao;
	AdministratorDao Administratordao;
	PictureDao Picturedao;
	SemanticsDao Semanticsdao;
	Tag_pushDao Tag_pushdao;
	
	
	
	public Tag_pushDao getTag_pushdao() {
		return Tag_pushdao;
	}

	public void setTag_pushdao(Tag_pushDao tag_pushdao) {
		Tag_pushdao = tag_pushdao;
	}

	

	public SemanticsDao getSemanticsdao() {
		return Semanticsdao;
	}

	public void setSemanticsdao(SemanticsDao semanticsdao) {
		Semanticsdao = semanticsdao;
	}

	public PictureDao getPicturedao() {
		return Picturedao;
	}

	public void setPicturedao(PictureDao picturedao) {
		Picturedao = picturedao;
	}

	public AdministratorDao getAdministratordao() {
		return Administratordao;
	}

	public void setAdministratordao(AdministratorDao administratordao) {
		Administratordao = administratordao;
	}

	public TagDao getTagdao() {
		return Tagdao;
	}

	public void setTagdao(TagDao tagdao) {
		Tagdao = tagdao;
	}

	public Day_pushDao getDay_pushdao() {
		return Day_pushdao;
	}

	public void setDay_pushdao(Day_pushDao day_pushdao) {
		Day_pushdao = day_pushdao;
	}

	public InterestDao getInterestdao() {
		return Interestdao;
	}

	public void setInterestdao(InterestDao interestdao) {
		Interestdao = interestdao;
	}

	public HaveDao getHavedao() {
		return Havedao;
	}

	public void setHavedao(HaveDao havedao) {
		Havedao = havedao;
	}

	public VolunteerDao getVolunteerdao() {
		return Volunteerdao;
	}

	public void setVolunteerdao(VolunteerDao volunteerdao) {
		Volunteerdao = volunteerdao;
	}

	@Override
	public JSONObject getPerson(String pptelephone)throws tranException
	{
		try{
			hw_Volunteer p=Volunteerdao.getPesonByphone(pptelephone);
			if(p == null) return null;
			int pid = p.getV_id();

			//得到该用户兴趣
			List<hw_Have> lt = Havedao.getvolunInter(pid);
			String Inter="";
			Iterator<hw_Have> itt =lt.iterator();
			if(itt.hasNext()){
				int I_id =itt.next().getI_id();
				Inter=Inter+Interestdao.getInterestById(I_id).getInter();
			}
			while(itt.hasNext()){
				int I_id =itt.next().getI_id();
				Inter=Inter+"-"+Interestdao.getInterestById(I_id).getInter();
			}
			//得到该用户历史推送图片id
			String historypush = Day_pushdao.getPicIDbyVID(pid);
			//得到该用户打过标签的图片id
			String histag = Tagdao.getPicbyVID(pid);
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("pid", p.getV_id()).put("pnick", p.getV_nick()).put("ppassword", p.getV_password())
			.put("ptelephone", p.getV_telephone()).put("pemail", p.getV_emial()).put("pnum", p.getV_credits())
			.put("prenwu", p.getV_renwu()).put("major", p.getV_major()).put("inter", Inter).put("history_push", historypush)
			.put("permission", 0).put("history_tag", histag).put("icon", p.getV_icon());
			return jsonObj;
		}catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"获取用户信息出现异常,请重试");
		}
	}
	
	@Override
	public String validLogin(String phonenumber , String passward) throws tranException
	{
		try
		{
			hw_Volunteer p = Volunteerdao.getPesonByphoneandpassward(phonenumber.trim(), passward.trim());
			if (p != null)
			{
				return p.getV_telephone();
			}
			return "-1";
		}
		catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"处理用户登录出现异常,请重试");
		}
	}
	@Override
	public String validLogin1(String phonenumber , String passward) throws tranException
	{
		try
		{
			hw_Administrator p = Administratordao.getPesonByphoneandpassward(phonenumber.trim(), passward.trim());
			if (p != null)
			{
				return p.getA_telephone();
			}
			return "-1";
		}
		catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"处理用户登录出现异常,请重试");
		}
	}
	@Override
	public String register(String pptelephone , String ppassword)throws tranException
	{
		try{
			hw_Volunteer p =Volunteerdao.getPesonByphone(pptelephone);
			if(p != null){
				//表示手机号码已注册
				return "-1"; 
			}else{
				hw_Volunteer temp = new hw_Volunteer();
				temp.setV_telephone(pptelephone);
				temp.setV_password(ppassword);
				Volunteerdao.savePerson(temp);
				//为刚注册的用户添加所有tag_push记录
				int VID = Volunteerdao.getPesonByphone(pptelephone).getV_id();
				List<hw_Picture> all = Picturedao.getallPic();
				Iterator<hw_Picture> it =all.iterator();
				while(it.hasNext()){
					hw_Tag_push t= new hw_Tag_push();
					hw_Picture iff =it.next();
					t.setPic_id(iff.getPic_id());
					t.setV_id(VID);
					Tag_pushdao.save(t);
				}
				workforone(pptelephone);//推送图片
				workforone1(VID);//推送标签
				return "1";
			}
		}catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"处理用户注册出现异常,请重试");
		}
	}
	@Override
	public String register1(String pptelephone , String ppassword)throws tranException
	{
		try{
			hw_Administrator p =Administratordao.getPesonByphone(pptelephone);
			if(p != null){
				//表示手机号码已注册
				return "-1"; 
			}else{
				hw_Administrator temp = new hw_Administrator();
				temp.setA_telephone(pptelephone);
				temp.setA_password(ppassword);
				Administratordao.savePerson(temp);
				return "1";
			}
		}catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"处理用户注册出现异常,请重试");
		}
	}
	//注册类,注册时运行,图片推荐
		private void workforone(String ptelephone){
			Date tt = new Date();
			Timestamp ts = new Timestamp(tt.getTime());
			hw_Volunteer p = Volunteerdao.getPesonByphone(ptelephone);
			if(p==null)
				return;
			int VID = p.getV_id();
			List<hw_Picture> pic_all = Picturedao.getallPic();
			int len=pic_all.size();
			Set<Integer> set = new TreeSet();
			int numberCount = 10<len?10:len;
			while(set.size() < numberCount){
				int number = (int ) (Math.random()*len);
				if(!set.contains(number)){
					set.add(number);
				}
			}	
			Iterator<Integer> il = set.iterator();
			String his="";
			while(il.hasNext()){
				int temp = il.next().intValue();
				int picid=pic_all.get(temp).getPic_id();
				//把推送的信息保存到Day_push中,并标记为未完成
				hw_Day_push DY = new hw_Day_push();
				DY.setPic_id(picid);
				DY.setV_id(VID);
				DY.setD_time(ts);
				DY.setD_isvis(0);
				Day_pushdao.save(DY);
			}
		}
	//注册时运行，标签推荐
	private void workforone1(int vid){
		 //某图使用某标签的次数
		HashMap<Integer,HashMap<String,Integer> > pic_tag = new HashMap<Integer,HashMap<String,Integer> >(); 
		//得到所有语义处理的图片标签
		List<hw_Semantics> hwS = Semanticsdao.getALL();
		Iterator<hw_Semantics> it =hwS.iterator();
		while(it.hasNext()){
			hw_Semantics temp = it.next();
			int picid = temp.getPic_id();
			String label =temp.getLabel_handle();
			int weight = temp.getNum();
			if(pic_tag.containsKey(picid)){ //已包含该图片
				HashMap<String,Integer> Pictemp =pic_tag.get(picid);
				if(Pictemp.containsKey(label)){
					int t =Pictemp.get(label);
					Pictemp.put(label, t+weight);
				}else{
					Pictemp.put(label, weight);
				}
				pic_tag.put(picid, Pictemp);
			}else{
				HashMap<String,Integer> Pictemp = new HashMap<String, Integer>();
				Pictemp.put(label, weight);
				pic_tag.put(picid, Pictemp);
			}
		}
		//推送N个标签
		int N=6;
		List<hw_Tag_push> push = Tag_pushdao.getlablebyvid(vid);
		Iterator<hw_Tag_push> itt =push.iterator();
		while(itt.hasNext()){
			//保存最后结果
			HashMap<String ,Double> ret =new HashMap<String ,Double>();
			hw_Tag_push temp =itt.next();
			int picid = temp.getPic_id();
			int max_pic_tag_weight =1;
			if(pic_tag.containsKey(picid)){
				HashMap<String,Integer> pp = pic_tag.get(picid);
				Iterator<String> itl =pp.keySet().iterator();
				while(itl.hasNext()){
					int ci=pp.get(itl.next());
					if(ci>=max_pic_tag_weight)
						max_pic_tag_weight=ci;
				}
				Iterator<String> itll =pp.keySet().iterator();
				while(itll.hasNext()){
					String tag_=itll.next();
					int weight=pp.get(tag_);
					double zhi=weight*1.0/ max_pic_tag_weight;
					if(ret.containsKey(tag_))
						ret.put(tag_, ret.get(tag_)+zhi);
					else
						ret.put(tag_, zhi);
				}
			}
			//排序
			List<Entry<String,Double>> list=new ArrayList<Entry<String,Double>>(ret.entrySet());
			Collections.sort(list,new Comparator<Map.Entry<String,Double>>(){
				public int compare(Map.Entry<String,Double> o1,Map.Entry<String,Double> o2){
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			//
			int tuisong = N<list.size()?N:list.size();
			String fl="";
			Iterator<Entry<String,Double>> tl =list.iterator();
			//输出list结果
			for(int i=0;i<tuisong;i++){
				String  fll = tl.next().getKey();
				boolean flag=true;
				fl=fl+fll+"-";
			}
			if(fl!=null&&!fl.isEmpty())
				fl=fl.substring(0,fl.length()-1);
			temp.setLabel(fl);
			Tag_pushdao.update(temp);
		}
	}
	@Override
	public String changemessage(String pptelephone , String pnick ,String pemail ,String pmajor ,String pinter,int icon)throws tranException
	{
		try{
			hw_Volunteer p = Volunteerdao.getPesonByphone(pptelephone);
			int V_id =p.getV_id();
			p.setV_nick(pnick);
			p.setV_emial(pemail);
			p.setV_major(pmajor);
			p.setV_icon(icon);
			Volunteerdao.updatePerson(p);
			Havedao.deletebyvid(p.getV_id());//删除目前该用户兴趣
			String[] inters = pinter.split("-");
			for(int i=0;i<inters.length;i++){
				hw_Interest t = Interestdao.getInterestByInter(inters[i]);
				if(t==null)
					continue;
				else{
					hw_Have h = new hw_Have();
					h.setI_id(t.getI_id());
					h.setV_id(V_id);
					Havedao.save(h);
				}	
			}
			return "1";  //表示成功
		}catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"用户修改信息出现异常,请重试");
		}
		
	}
	@Override
	public JSONObject getDaypushbyphone(String pptelephone)throws tranException{
		try{	
			hw_Volunteer V = Volunteerdao.getPesonByphone(pptelephone);
			int VID =V.getV_id();
			JSONObject jsonObj = new JSONObject();
			List<hw_Day_push> lu = Day_pushdao.getday_pushbyphone(VID);
			hw_Day_push daypush =new hw_Day_push();
			String dayput="";
			Iterator<hw_Day_push> il = lu.iterator();
			while(il.hasNext()){
				daypush = il.next();
				int PID =daypush.getPic_id();
				String passway = getpssbypicid(PID);
				String tagpush = gettagpush(PID,pptelephone);
				dayput=dayput+String.valueOf(PID)+','+passway+',';
				if(tagpush==null||tagpush.isEmpty()){
					dayput=dayput+"null;";
				}else
					dayput=dayput+tagpush+";";
			}
			if(!dayput.isEmpty())
				dayput=dayput.substring(0,dayput.length()-1);
			jsonObj.put("dayput",dayput);
			System.out.println(dayput);
			return jsonObj;
		}catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"获取图片推送信息异常,请重试");
		}
	}
	@Override
	public boolean  picup(String passway,String filename)throws tranException{
		//管理员上传图片时，在tag_push表中为每个人添加一条信息
		try{
			Date tt = new Date();
			Timestamp ts = new Timestamp(tt.getTime());
			if(Picturedao.getpicbypassway(passway))
				return false;
			hw_Picture pall=new hw_Picture();
			pall.setPic_isvis(0);//设置图片未完成
			pall.setPic_passway(passway);
			pall.setUp_time(ts);
			pall.setPic_name(filename);
			Picturedao.savepicture(pall); //保存到hw_Picture中
			pall=Picturedao.getpicbypass(passway);
			List<hw_Volunteer> pl=Volunteerdao.getallPerson();
			Iterator<hw_Volunteer> it=pl.iterator();
			while(it.hasNext()){ //每张图片都增加tagpush记录
				hw_Tag_push t=new hw_Tag_push();
				hw_Volunteer p=it.next();
				t.setV_id(p.getV_id());
				t.setPic_id(pall.getPic_id());
				Tag_pushdao.save(t);
			}
			return true;
		}catch(Exception e){
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"图片保存失败,请重试");
		}
	}
	@Override
	public String getpersonhistorybyphone(String pptelephone)throws tranException{
		try{
			hw_Volunteer p = Volunteerdao.getPesonByphone(pptelephone);
			int VID = p.getV_id();
			List<hw_Tag> tag = Tagdao.getPicallbyVID(VID);
			HashMap<Integer, String> tags = new HashMap<Integer, String>();
			Iterator<hw_Tag> it = tag.iterator();
			while(it.hasNext()){
				hw_Tag temp = it.next();
				int pid = temp.getPic_id();
				String label =temp.getLabel();
				if(tags.containsKey(pid)){
					String val =tags.get(pid);
					val =val+"-"+label;
					tags.put(pid, val);
				}else{
					tags.put(pid, label);
				}
			}
			Iterator iter = tags.entrySet().iterator();
			String res="";
			while(iter.hasNext()){
				Map.Entry entry = (Map.Entry)iter.next();
				int key = (Integer)entry.getKey();
				String value = (String)entry.getValue();
				hw_Picture Pic = Picturedao.getpicbypicid(key);
				String pushtag = Tag_pushdao.getlablebyvidpic(VID, key);
				res =res+String.valueOf(key)+','+Pic.getPic_passway()+','+value+','+Pic.getPic_isvis()+','+pushtag+";";
			}
			if(!res.isEmpty())
				res=res.substring(0,res.length()-1);
			return res;
		}catch(Exception e){
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"异常,请重试");
		}
	}
	@Override
	public String showpersonallpicture(String pptelephone)throws tranException{
		try{
			int vid = Volunteerdao.getPesonByphone(pptelephone).getV_id();
			//得到用户所有打过标签的pic
			String pics = Tagdao.getPicbyVID(vid);
			String[] pices =null;
			if(!pics.isEmpty()){//若该用户没有打过图片
				pices=pics.split("-");
			}
			//System.out.println(pices[0]);
			List<hw_Picture> allpics = Picturedao.getallPic();
			Iterator<hw_Picture> it =allpics.iterator();
			String res="";//结果
			while(it.hasNext()){
				hw_Picture temp =it.next();
				String Picid = String.valueOf(temp.getPic_id());
				boolean flag =false;
				if(pices!=null){
					for(int i=0;i<pices.length;i++){
						if(Picid.equals(pices[i])){//表明打过该图片
							flag=true;
							break;
						}
					}
				}
				if(flag)continue;
				String tagpush = Tag_pushdao.getlablebyvidpic(vid, Integer.parseInt(Picid));
				res=res+temp.getPic_id()+','+temp.getPic_passway()+',';
				if(tagpush==null||tagpush.isEmpty())
					res=res+"null;";
				else
					res=res+tagpush+";";
			}
			if(!res.isEmpty()){
				res=res.substring(0,res.length()-1);
			}
			return res;
		}catch(Exception e){
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"异常,请重试");
		}
	}
	
	//用户用户手机和图片id上传标签
	@Override
	public int tagup(String pptelephone,String picid,String pictag)throws tranException{
		try{
			//String res="";
			Date tt = new Date();
			Timestamp ts = new Timestamp(tt.getTime());
			hw_Volunteer volun = Volunteerdao.getPesonByphone(pptelephone);
			int VID = volun.getV_id();
			int Pic_id = Integer.parseInt(picid);
			boolean falg = Tagdao.existbyvidpid(VID, Pic_id);
			Tagdao.deletebyvidpid(VID, Pic_id);//删除旧记录
			//保存新记录
			if(pictag==null||pictag.isEmpty())
				return 1;
			String[] pictags =pictag.split("-");
			for(int i=0;i<pictags.length;i++){
				hw_Tag temp =new hw_Tag();
				temp.setLabel(pictags[i]);
				temp.setPic_id(Pic_id);
				temp.setV_id(VID);
				temp.setT_time(ts);
				Tagdao.save(temp);
			}
			if(!falg){ //如果是第一次打该图片
				hw_Day_push temp =Day_pushdao.getpushbyVIDPID(VID,Pic_id);
				if(temp!=null){
					temp.setD_isvis(1);//标为完成
					Day_pushdao.save(temp);
				}
				hw_Volunteer person =Volunteerdao.getPesonByphone(pptelephone);
				int old = person.getV_renwu();
				int num = person.getV_credits();
				old++;
				if(old==5){
					old=0;
					num++;
				}
				person.setV_credits(num);
				person.setV_renwu(old);
				Volunteerdao.updatePerson(person);//用户积分更新
			}
			return 1;
		}catch(Exception e){
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"异常,请重试");
		}
	}
	//用于密码修改
	@Override
	public int passwordchange(String pptelephone,String oldpasssword,String newpassword)throws tranException{
		try{
			hw_Volunteer p=Volunteerdao.getPesonByphoneandpassward(pptelephone, oldpasssword);
			if(p==null)
				return -1;
			p.setV_password(newpassword);
			Volunteerdao.updatePerson(p);
			return 1;
		}catch(Exception e){
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"异常,请重试");
		}
	}
	
	//P_开始表示用于管理员
	
	//用于管理员获取所用用户
	@Override
	public JSONArray P_getallPerson( )throws tranException
	{
		try{
			List<hw_Volunteer> ul=Volunteerdao.getallPerson();
			JSONArray jA = new JSONArray();
			Iterator<hw_Volunteer> it=ul.iterator();
			int i=0;
			while(it.hasNext()){
				hw_Volunteer p = it.next();
				int pid = p.getV_id();
				//得到该用户兴趣
				List<hw_Have> lt = Havedao.getvolunInter(pid);
				String Inter="";
				Iterator<hw_Have> itt =lt.iterator();
				if(itt.hasNext()){
					int I_id =itt.next().getI_id();
					Inter=Inter+Interestdao.getInterestById(I_id).getInter();
				}
				while(itt.hasNext()){
					int I_id =itt.next().getI_id();
					Inter=Inter+"-"+Interestdao.getInterestById(I_id).getInter();
				}
				//得到该用户历史推送图片id
				String historypush = Day_pushdao.getPicIDbyVID(pid);
				//得到该用户打过标签的图片id
				String histag = Tagdao.getPicbyVID(pid);
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("pid", p.getV_id()).put("pnick", p.getV_nick()).put("ppassword", p.getV_password())
				.put("ptelephone", p.getV_telephone()).put("pemail", p.getV_emial()).put("pnum", p.getV_credits())
				.put("prenwu", String.valueOf(p.getV_renwu())).put("major", p.getV_major()).put("inter", Inter).put("history_push", historypush)
				.put("permission", 0).put("history_tag", histag).put("picon", p.getV_icon());
				if(p.getV_nick()==null)
					jsonObj.put("pnick", "");
				if(p.getV_emial()==null)
					jsonObj.put("pemail", "");
				if(String.valueOf(p.getV_renwu())==null)
					jsonObj.put("prenwu", "");
				if(p.getV_major() == null)
					jsonObj.put("major", "");
				jA.put(i, jsonObj);
				i++;
			}
			return jA;
		}catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"获取用户信息出现异常,请重试");
		}
	}
	//用于管理员修改用户资料,暂定
	@Override
	public String P_changemessage(String pptelephone , String pnick ,String pemail ,String pmajor ,String pinter)throws tranException
	{
		try{
			hw_Volunteer p=Volunteerdao.getPesonByphone(pptelephone);
			p.setV_nick(pnick);
			p.setV_emial(pemail);
			p.setV_major(pmajor);
			int V_id =p.getV_id();
			Volunteerdao.updatePerson(p);
			//保存用户兴趣
			Havedao.deletebyvid(p.getV_id());//删除目前该用户兴趣
			String[] inters = pinter.split("-");
			for(int i=0;i<inters.length;i++){
				hw_Interest t = Interestdao.getInterestByInter(inters[i]);
				if(t==null)
					continue;
				else{
					hw_Have h = new hw_Have();
					h.setI_id(t.getI_id());
					h.setV_id(V_id);
					Havedao.save(h);
				}	
			}
			return "1";  //表示成功
		}catch (Exception e)
		{
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"用户修改信息出现异常,请重试");
		}
		
	}

	//用于导出结果
	//1、导出指定结果图片，导出所有图片结果
	public JSONArray P_picresult()throws tranException{
		try{
			List<hw_Picture> ul = Picturedao.getfinishedpic();
			Iterator<hw_Picture> il = ul.iterator();
			JSONArray js = new JSONArray();
			int i=0;
			while(il.hasNext()){
				hw_Picture oic=il.next();
				JSONObject jsonObj = new JSONObject();
				String pass = oic.getPic_passway();
				String[] tag = oic.getFinal_label().split("-");
				String filename = oic.getPic_name();
				Timestamp fl = oic.getPic_finishtime();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
				Date time = fl;
				String tl = format.format(time);
				jsonObj.put("pass",pass).put("picture_name", filename).put("finish_time", tl).put("labels",oic.getFinal_label()).put("isload", 0);
				js.put(i,jsonObj);
				i++;
			}
			return js;
		}catch(Exception e){
			log.debug(e.getMessage());
			throw new tranException(e.getMessage()+"图片标签化结果出现异常");
		}
	}
	
	//用于图片搜索,输出指定类型的图片,结果按相同标签个数排序,以语义处理后标签进行搜索
	public HashMap<String,HashMap<Integer,Integer>> P_search_init(){
		//初始化拥有某tag的，图片地址集
		HashMap<String,HashMap<Integer,Integer>> allpic =new HashMap<String,HashMap<Integer,Integer>>();
		//得到所有图片语义处理后标签信息
		List<hw_Semantics> ll = Semanticsdao.getALL();
		Iterator<hw_Semantics> it = ll.iterator();
		while(it.hasNext()){
			hw_Semantics temp = it.next();
			String label=temp.getLabel_handle();
			int pid = temp.getPic_id();
			int weight = temp.getNum();
			if(label==null||label.isEmpty())
				continue;
			if(allpic.containsKey(label)){ //判断是否拥有该标签
				HashMap<Integer,Integer> fll = allpic.get(label);
					if(fll.containsKey(pid))//表明处理过该信息
						continue;
					fll.put(pid, weight);
				allpic.put(label, fll);
			}else{
				HashMap<Integer,Integer> fll = new HashMap<Integer,Integer>();
				fll.put(pid, weight);
				allpic.put(label, fll);
			}
		}
		//同一标签，对图片路径按个数从大到小排序
		Iterator<Entry<String,HashMap<Integer,Integer>>> all =allpic.entrySet().iterator();
		while(all.hasNext()){
			HashMap<Integer,Integer> sorted = new LinkedHashMap<Integer,Integer>();
			Entry<String,HashMap<Integer,Integer>> tes = all.next();
			HashMap<Integer,Integer> tt =tes.getValue();
			List<Entry<Integer,Integer>> list=new ArrayList<Entry<Integer,Integer>>(tt.entrySet());
			Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>(){
				public int compare(Map.Entry<Integer,Integer> o1,Map.Entry<Integer,Integer> o2){
					return (o2.getValue()).compareTo(o1.getValue());
				}
			});
			Iterator<Map.Entry<Integer,Integer>>iter=list.iterator();
			Map.Entry<Integer,Integer>tmpEntry=null;
			while(iter.hasNext()){
				tmpEntry=iter.next();
				sorted.put(tmpEntry.getKey(),tmpEntry.getValue());
			}
			allpic.put(tes.getKey(), sorted);
		}
		return allpic;
	}
	
	  //用于图片搜索,得到用户打过标签的pic集合
	public String[] search_tag_person(String phone){
			//得到用户没有打过标签的图片pid
		hw_Volunteer temp =Volunteerdao.getPesonByphone(phone);
		String pics = Tagdao.getPicbyVID(temp.getV_id());
		String [] pic = null ;
		if(pics!=null&&!pics.isEmpty())
			pic = pics.split("-");
		return pic;
	}
	
	//获取所有图片
	public List<hw_Picture> showallpicture(){
		List<hw_Picture> t=Picturedao.getallPic();
		return t;
	}
	@Override
	public String getpssbypicid(int picid){
		hw_Picture temp = Picturedao.getpicbypicid(picid);
		if(temp == null)
			return "";
		else
			return temp.getPic_passway();
	}
	@Override
	public String gettagpush(int pid,String phone){
		int vid = Volunteerdao.getPesonByphone(phone).getV_id();
		return Tag_pushdao.getlablebyvidpic(vid, pid);
	}
	@Override
	public void  chuli(String temp){
		String[] my =temp.split(";");
		for(int i=0;i<my.length;i++){
			if(my[i].isEmpty())
				continue;
			String[] haha = my[i].split(":");
			String[] hat = haha[1].split(",");
			for(int j=0;j<hat.length;j++){
				hw_Interest ji = new hw_Interest();
				ji.setInter(hat[j]);
				Interestdao.save(ji);
			}
 		}
	}
}
