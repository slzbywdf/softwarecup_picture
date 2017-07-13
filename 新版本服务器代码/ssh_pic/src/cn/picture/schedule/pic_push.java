package cn.picture.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.sql.Timestamp;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.picture.dao.Day_pushDao;
import cn.picture.dao.HaveDao;
import cn.picture.dao.PictureDao;
import cn.picture.dao.SemanticsDao;
import cn.picture.dao.TagDao;
import cn.picture.dao.VolunteerDao;
import cn.picture.domain.hw_Day_push;
import cn.picture.domain.hw_Have;
import cn.picture.domain.hw_Picture;
import cn.picture.domain.hw_Volunteer;

/*
 * 该类的主要作用是定时在day_push表中为每位用户推送照片
 */
public class pic_push extends QuartzJobBean{
	
	public pic_push() {
		super();
		// TODO Auto-generated constructor stub
	}
	VolunteerDao Volunteerdao;
	PictureDao Picturedao;
	HaveDao Havedao;
	Day_pushDao Day_pushdao;
	SemanticsDao Semanticsdao;
	TagDao Tagdao;
	
	
	
	public VolunteerDao getVolunteerdao() {
		return Volunteerdao;
	}
	public void setVolunteerdao(VolunteerDao volunteerdao) {
		Volunteerdao = volunteerdao;
	}
	public PictureDao getPicturedao() {
		return Picturedao;
	}
	public void setPicturedao(PictureDao picturedao) {
		Picturedao = picturedao;
	}
	public HaveDao getHavedao() {
		return Havedao;
	}
	public void setHavedao(HaveDao havedao) {
		Havedao = havedao;
	}
	public Day_pushDao getDay_pushdao() {
		return Day_pushdao;
	}
	public void setDay_pushdao(Day_pushDao day_pushdao) {
		Day_pushdao = day_pushdao;
	}
	public SemanticsDao getSemanticsdao() {
		return Semanticsdao;
	}
	public void setSemanticsdao(SemanticsDao semanticsdao) {
		Semanticsdao = semanticsdao;
	}
	public TagDao getTagdao() {
		return Tagdao;
	}
	public void setTagdao(TagDao tagdao) {
		Tagdao = tagdao;
	}
	@Override
	public void executeInternal(JobExecutionContext ctx)throws JobExecutionException{
		
	}
	public String getpersonInter(int pid){
		//得到该用户兴趣
		List<hw_Have> lt = Havedao.getvolunInter(pid);
		String Inter="";
		Iterator<hw_Have> itt =lt.iterator();
		while(itt.hasNext()){
			int I_id =itt.next().getI_id();
			Inter=Inter+String.valueOf(I_id)+"-";
		}
		if(!Inter.isEmpty())
			Inter=Inter.substring(0,Inter.length()-1);
		return Inter;
	}
	/*1.基于用户推荐  2.基于图片推荐
	1.计算用户兴趣相似度(三级)，以用户积分加权重
	2.将相似度排序，选择相似度前几的用户打过标签的图片进行推荐
	2.从选出的图片中(暂定50张)
	3.在选出的图片中，将图片已有标签与用户打过的所有标签进行相似度计算
	4.选出前10进行推送*/
	public void work(){
		Date tt = new Date();
		Timestamp ts = new Timestamp(tt.getTime());
		//得到所有图片及人
		List<hw_Volunteer> plist = Volunteerdao.getallPerson();
		List<hw_Picture> allpic = Picturedao.getallunPic();//得到所有图片信息
		String[] pic_havatag = Tagdao.getPic().split("-");//有标签的图片的ID集
		String[] pic_notag = new String[100000] ;
		int lenof_no = 0;
		Iterator<hw_Picture> itl = allpic.iterator();
		while(itl.hasNext()){
			hw_Picture temp =itl.next();
			String picid =String.valueOf(temp.getPic_id());
			boolean flag = true;
			for(int i=0;i<pic_havatag.length;i++){
				if(pic_havatag[i].equals(picid)){
					flag=false;
					break;
				}
			}
			if(flag){
				pic_notag[lenof_no++]=picid;
			}
		}
		//List<picture_all> pic_notag = picDao.getpichavanotag(); //没标签图片集
		int len0=pic_havatag.length;//有标签图片长度
		int len1=allpic.size()-len0;//没有标签图片长度
		int len =len0+len1;
		Iterator<hw_Volunteer> it = plist.iterator();//外迭代
		while(it.hasNext()){
			Iterator<hw_Volunteer> il = plist.iterator();//内迭代
			//得到用户手机号，以及推送历史,打过标签图片，用户兴趣
			hw_Volunteer p=it.next();
			int pid=p.getV_id();
			System.out.println(pid);
			String phone =p.getV_telephone();
			
			//得到该用户兴趣
			String Inter=getpersonInter(pid);
			//得到该用户历史推送图片id
			String his_pus = Day_pushdao.getPicIDbyVID(pid);
			//得到该用户打过标签的图片id
			String his_tag = Tagdao.getPicbyVID(pid);
			String[] his_push = null; //打过图片的集合
			if(his_pus!=null&&!his_pus.isEmpty())
				his_push = his_pus.split("-");

			Set<String> set = new TreeSet(); //中间结果集
			Set<String> personset = new TreeSet(); //用户打过标签图片
			Set<String> fin = new TreeSet();  //最终结果集
			int his_pus_len;
			if(his_pus==null||his_pus.isEmpty())
				his_pus_len=0;
			else
				his_pus_len=his_push.length;
			for(int i=0;i<his_pus_len;i++){
				personset.add(his_push[i]);
			}
			int count=0;	//中间结果集图片数计数
			int cnt=0;
			int pic_count_first=50<len0-his_pus_len?50:len0-his_pus_len;  //中间结果集最多有几张图片
			int pic_count_final=10<len0-his_pus_len?10:len0-his_pus_len;
			int cntt=10<len-his_pus_len?10:len-his_pus_len;//最终结果集
			//用户兴趣相似度计算
			HashMap<Integer,Double> similar = new HashMap<Integer,Double>() ;
			if(!(Inter==null||Inter.isEmpty())){ //若当前用户有兴趣值
				String[] inter =Inter.split("-");
				while(il.hasNext()){//遍历所有用户
					hw_Volunteer temp = il.next();
					int pid_temp = temp.getV_id();
					if(pid_temp == pid )
						continue;
					String inte_temp = getpersonInter(pid_temp);//兴趣
					String his_pus_temp = Day_pushdao.getPicIDbyVID(pid_temp);//历史推送
					String his_ta_tag = Tagdao.getPicbyVID(pid);//历史打过标签图片
					int jifen = temp.getV_credits(); //用户积分值
					if(inte_temp==null||inte_temp.isEmpty())//该用户无兴趣,不用计算兴趣相似度
						continue;
					String[] inter_temp = inte_temp.split("-");
					double sim=0.0;
					int sum=0;
					for(int i=0;i<inter.length;i++){
						for(int j=0;j<inter_temp.length;j++){
							if(inter[i].equals(inter_temp[j]))
								sum++;
						}
					}
					sim=sum*1.0*(jifen+1)/(inter.length+inter_temp.length);  //兴趣相似度计算公式
					similar.put(pid_temp, sim);
				}
				//用户兴趣相似度排序
				List<Entry<Integer,Double>> list=new ArrayList<Entry<Integer,Double>>(similar.entrySet());
				Collections.sort(list,new Comparator<Map.Entry<Integer,Double>>(){
					public int compare(Map.Entry<Integer,Double> o1,Map.Entry<Integer,Double> o2){
						return (o2.getValue()).compareTo(o1.getValue());
					}
				});
				//暂定选择前50张图片，最终只推荐10张tupian
				Iterator<Entry<Integer,Double>> tll =list.iterator();
				while(tll.hasNext()){
					if(count>=pic_count_first)
						break;
					Entry<Integer,Double> temp=tll.next();
					Integer ptempid=temp.getKey();
					String hisstag = Tagdao.getPicbyVID(ptempid);//hisstag 表示他打过标签的图片
					if(hisstag==null||hisstag.isEmpty())
						continue;
					String[] histag = hisstag.split("-");
					for(int k=0;k<histag.length;k++){
						if(!set.contains(histag[k])&&!personset.contains(histag[k])){
							set.add(histag[k]);
							if(cnt<10){
								fin.add(histag[k]);
								cnt++;
							}
							count++;
						}
					}
				}
			}
			//如果打过标签的图片数少于50张，将先填充打过标签的图片
			while(count<pic_count_first){
				int number = (int ) (Math.random()*len0);
				//选择有标签的图片
				String f=pic_havatag[number];
				if(!personset.contains(f)&&!set.contains(f)){
					set.add(f);
					if(cnt<10){
						fin.add(f);
						cnt++;
					}
					count++;
				}
			}
			//System.out.println(count);
			//在选出的图片中进行图片标签与用户所打标签相似度计算
			HashMap<Integer,Double> tag_sim =new HashMap<Integer,Double>();
			if(his_tag!=null&&!his_tag.isEmpty()){//若该用户打过标签
				String[] person_tag = Tagdao.getLabelbyVID(pid).split("-");
				Iterator<String> fg=set.iterator();
				while(fg.hasNext()){
					int id=Integer.parseInt(fg.next());
					//得到pic所有标签
					String piclabel = Semanticsdao.getbypic(id);
					if( piclabel==null||piclabel.isEmpty()){
						tag_sim.put(id, 0.0);
						continue;
					}
					String[] tag_pp = piclabel.split("-");
					int num=0;
					for(int i=0;i<person_tag.length;i++){
						for(int j=0;j<tag_pp.length;j++){
							String [] tagpp =tag_pp[j].split(",");
							if(person_tag[i].equals(tagpp[0])||person_tag[i].indexOf(tagpp[0])>=0||tagpp[0].indexOf(person_tag[i])>=0)
								num=num+Integer.parseInt(tagpp[1]);			
						}
					}
					double simm=1.0*num/(person_tag.length+Semanticsdao.getweightbypic(id));
					tag_sim.put(id, simm);
				}
				//排序，取前10
				List<Entry<Integer,Double>> llist=new ArrayList<Entry<Integer,Double>>(tag_sim.entrySet());
				Collections.sort(llist,new Comparator<Map.Entry<Integer,Double>>(){
					public int compare(Map.Entry<Integer,Double> o1,Map.Entry<Integer,Double> o2){
						return (o2.getValue()).compareTo(o1.getValue());
					}
				});
				//取前10
				Set<Integer> set_final = new TreeSet();
				Iterator<Entry<Integer,Double>> tllg =llist.iterator();
				int countt=0;
				while(tllg.hasNext()){
					if(countt>=cntt)
						break;
					Entry<Integer,Double> temp=tllg.next();
					Integer picid=temp.getKey();
					if(!set_final.contains(picid)){
						set_final.add(picid);
						countt++;
					}
				}
				//若剩余没有10张，则随机推送没有打过标签的图片
				//System.out.println(countt);
				while(countt<cntt){
					int number = (int ) (Math.random()*len1);
					Integer f=Integer.parseInt(pic_notag[number]);
					String ff=f.toString();
					if(!personset.contains(ff)&&!set_final.contains(f)){
						set_final.add(f);
						countt++;
						//System.out.println(countt);
					}
				}
				//选择结束，进行数据库操作
				Iterator<Integer> ilgg = set_final.iterator();
				while(ilgg.hasNext()){
					int picid = ilgg.next().intValue();
					hw_Day_push day_push = new hw_Day_push();
					day_push.setV_id(pid);
					day_push.setPic_id(picid);
					day_push.setD_isvis(0);
					day_push.setD_time(ts);
					Day_pushdao.save(day_push);
				}
			}else{
				//若该用户没有打过标签，则不能按标签推荐，使用按用户兴趣推荐结果
				//若剩余没有10张，则随机推送没有打过标签的图片
				while(cnt<cntt){
					int number = (int ) (Math.random()*len1);
					//System.out.println(cnt);
					//System.out.println(cntt);
					String f = pic_notag[number];
					if(!personset.contains(f)&&!fin.contains(f)){
						fin.add(f);
						cnt++;
					}
				}
				//选择结束，进行数据库操作
				Iterator<String> ilgy = fin.iterator();
				while(ilgy.hasNext()){
					int picid = Integer.valueOf(ilgy.next());
					hw_Day_push day_push = new hw_Day_push();
					day_push.setV_id(pid);
					day_push.setPic_id(picid);
					day_push.setD_isvis(0);
					day_push.setD_time(ts);
					Day_pushdao.save(day_push);
				}
			}
		}	
	}
}
