package cn.picture.schedule;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.picture.dao.PictureDao;
import cn.picture.dao.SemanticsDao;
import cn.picture.dao.TagDao;
import cn.picture.dao.Tag_pushDao;
import cn.picture.domain.hw_Semantics;
import cn.picture.domain.hw_Tag;
import cn.picture.domain.hw_Tag_push;

/*
 * 该类的主要作用是定时在tag表中推送tag标签
 */
public class tag_push extends QuartzJobBean{
	
	SemanticsDao Semanticsdao;
	TagDao Tagdao;
	PictureDao Picturedao;
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

	public TagDao getTagdao() {
		return Tagdao;
	}

	public void setTagdao(TagDao tagdao) {
		Tagdao = tagdao;
	}

	public PictureDao getPicturedao() {
		return Picturedao;
	}

	public void setPicturedao(PictureDao picturedao) {
		Picturedao = picturedao;
	}

	@Override
	public void executeInternal(JobExecutionContext ctx)throws JobExecutionException{
		
	}
	
	//定时类
	public void work(){
		//某人使用某标签的次数
		HashMap<Integer,HashMap<String,Integer> > user_tag = new HashMap<Integer,HashMap<String,Integer> >() ; 
		//某图使用某标签的次数
		HashMap<Integer,HashMap<String,Integer> > pic_tag = new HashMap<Integer,HashMap<String,Integer> >();
		
		List<hw_Tag> lu = Tagdao.getPic1();//得到所有标签记录
		Iterator<hw_Tag> itt = lu.iterator();
		while(itt.hasNext()){
			hw_Tag ht = itt.next();
			int VID = ht.getV_id();
			int picid = ht.getPic_id();
			String label = ht.getLabel();
			//user_tag
			if(user_tag.containsKey(VID)){
				HashMap<String,Integer> temp =user_tag.get(VID);
				if(temp.containsKey(label)){
					temp.put(label,temp.get(label)+1);
				}else{
					temp.put(label, 1);
				}
				user_tag.put(VID, temp);
			}else{
				HashMap<String,Integer> temp = new HashMap<String,Integer> ();
				temp.put(label, 1);
				user_tag.put(VID, temp);
			}
		}
		//pic_tag
		List<hw_Semantics> se =Semanticsdao.getALL();
		Iterator<hw_Semantics> it = se.iterator();
		while(it.hasNext()){
			hw_Semantics temp = it.next();
			int picid = temp.getPic_id();
			String label = temp.getLabel_handle();
			int num =temp.getNum();
			if(pic_tag.containsKey(picid)){
				HashMap<String,Integer> tempp =pic_tag.get(picid);
				if(tempp.containsKey(label)){
						tempp.put(label,tempp.get(label)+num);
				}else{
						tempp.put(label, num);
				}
				pic_tag.put(picid, tempp);
			}else{
				HashMap<String,Integer> tempp =new HashMap<String,Integer>();
				tempp.put(label, num);
				pic_tag.put(picid, tempp);
			}
		}
		

		//给tag表中所有为判定的图片进行推送
		//推送N个标签
		int N=6;
		double alpha=0.5;
		List<hw_Tag_push> push = Tag_pushdao.getall();	
		Iterator<hw_Tag_push> myit = push.iterator();
		while(myit.hasNext()){
			//保存最后结果
			HashMap<String ,Double> ret =new HashMap<String ,Double>();
			hw_Tag_push temp =myit.next();
			int vid = temp.getV_id();
			int picid = temp.getPic_id();
			//得到vid对picid打过的标签
			String fm =Tagdao.getLabelbyPicidvid(vid, picid);
			
			//找该用户使用最多的标签
			int max_user_tag_weight =1;
			if(user_tag.containsKey(vid)){
				HashMap<String,Integer> user = user_tag.get(vid);
				Iterator<String> itl =user.keySet().iterator();
				while(itl.hasNext()){
					int ci=user.get(itl.next());
					if(ci>=max_user_tag_weight)
						max_user_tag_weight=ci;
				}
				Iterator<String> itll =user.keySet().iterator();
				while(itll.hasNext()){
					String tag_=itll.next();
					int weight=user.get(tag_);
					double zhi=(1-alpha)*weight*1.0/ max_user_tag_weight;
					ret.put(tag_, zhi);
				}
			}
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
					double zhi=alpha*weight*1.0/ max_pic_tag_weight;
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
			if(fm!=null&&!fm.isEmpty()){
				String []tagg=fm.split("-");
				for(int i=0;i<tuisong;i++){
					String  fll = tl.next().getKey();
					boolean flag=true;
					for(int j=0;j<tagg.length;j++)
						if(tagg[j].equals(fll)){
							flag=false;
							break;
						}
					if(!flag)
						break;
					fl=fl+fll+"-";
				}
			}else{
				for(int i=0;i<tuisong;i++){
					String  fll = tl.next().getKey();
					fl=fl+fll+"-";
				}
			}
			if(fl!=null&&!fl.isEmpty())
				fl=fl.substring(0,fl.length()-1);
			temp.setLabel(fl);
			Tag_pushdao.update(temp);
		}
	}
}
