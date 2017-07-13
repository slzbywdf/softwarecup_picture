package cn.picture.schedule;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import CiLin.CiLin;
import ICTCLAS.I3S.AC.CHINAUTIL;
import cn.picture.dao.Day_pushDao;
import cn.picture.dao.HaveDao;
import cn.picture.dao.PictureDao;
import cn.picture.dao.SemanticsDao;
import cn.picture.dao.TagDao;
import cn.picture.dao.VolunteerDao;
import cn.picture.domain.hw_Picture;
import cn.picture.domain.hw_Semantics;
import cn.picture.domain.hw_Tag;
import cn.picture.domain.hw_Volunteer;
/*
 * *主要用于标签语义处理,标签判定(每次都进行)
 * 目前暂定用户标签超过100即可进行标签审判
 * sim double n>0.8即为近义词
 */
public class tag_done extends QuartzJobBean{
	//获得所有标签
	VolunteerDao Volunteerdao;
	PictureDao Picturedao;
	HaveDao Havedao;
	Day_pushDao Day_pushdao;
	SemanticsDao Semanticsdao;
	TagDao Tagdao;
	private int N=100;
	private double zhi=0.8;
	
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
	public void work(){
		//期待的词性 n 名词  nr人名 ns地名
		Set<String> expectedNature = new HashSet<String>() {{
	         add("n");add("nr");add("ns");add("nt");add("nz");add("nl");add("ng"); add("s");add("x");
	     }};
	   //获得所有未判定的图片
		List<hw_Picture> pic=Picturedao.getallPic1();
		Iterator<hw_Picture> it = pic.iterator();
		while(it.hasNext()){
			hw_Picture picc=it.next();
			int pid=picc.getPic_id();
			String tags = Tagdao.getLabelbyPicid(pid);//获取需要语义处理的图片标签
			if(tags!=null&&!tags.isEmpty()){
				String[] tagss = tags.split("-");
				int len = tagss.length;
				//对每个标签进行分词、词性分析
				Vector<String> vec =new Vector<String>();
				for(int i=0;i<len;i++){
					String result = CHINAUTIL.testICTCLAS_ParagraphProcess(tagss[i]);
					String []terms =result.split(" ");
					 for(int j=0; j<terms.length; j++) {
						 String[] temp=terms[j].split("/");
					     String word = temp[0]; //拿到词
					     if(word==null||word.isEmpty())
					    	 continue;
					     String natureStr = temp[1]; //拿到词性
					     if(expectedNature.contains(natureStr)) {
				             vec.add(word);
				         }
				     }
				}
				HashMap<String,Integer> res=new HashMap<String,Integer>();	
				res.put(vec.get(0), 1);//将需要保留词性的标签进行计数统计
				for(int i=1;i<vec.size();i++){
					Iterator<Entry<String, Integer>> il =res.entrySet().iterator();
					boolean flag =false;
					String word1=vec.get(i);
					while(il.hasNext()){
						Map.Entry<String, Integer> ff=(Map.Entry<String, Integer>)il.next();
						String word2 = ff.getKey();
						double sim = CiLin.calcWordsSimilarity(word1, word2);//计算两个词的相似度
						if(sim>zhi){
							res.put(word2, res.get(word2)+1);
							flag=true;
							break;
						}
					}
					if(!flag){
						res.put(word1, 1);
					}
				}
				//标签判定
				List<Entry<String,Integer>> list=new ArrayList<Entry<String,Integer>>(res.entrySet());
				Collections.sort(list,new Comparator<Map.Entry<String,Integer>>(){
					public int compare(Map.Entry<String,Integer> o1,Map.Entry<String,Integer> o2){
						return (o2.getValue()).compareTo(o1.getValue());
					}
				});
				Iterator<Entry<String,Integer>> tl =list.iterator();
				String result="";
				String tag_all="";
				int count=0;
				int num=6;
				int fgl=0;
				//语义处理后结果保存
				Semanticsdao.deletebypicid(pid);
				while(tl.hasNext()){
					hw_Semantics temp = new hw_Semantics();
					Entry<String,Integer> mm=tl.next();
					String tag=mm.getKey();
					int val = mm.getValue();
					temp.setLabel_handle(tag);
					temp.setNum(val);
					temp.setPic_id(pid);
					Semanticsdao.save(temp);
					if(fgl<num){
						fgl++;
						result=result+tag+"-";
					}
				}
				result=result.substring(0,result.length()-1);
				//将结果保存到picture_all中并将结果置1
				picc.setFinal_label(result);
				if(count>N){   //此时可判定图片完成
					picc.setPic_isvis(1);
					Date tt = new Date();
					Timestamp ts = new Timestamp(tt.getTime());
					picc.setPic_finishtime(ts);
					//将采纳该标签的用户积分加5分
					String[] f = result.split("-");
					//获得该图片所有tag
					List<hw_Tag> fm = Tagdao.getbyPicid(pid);
					Iterator<hw_Tag> ll=fm.iterator();
					int cnt=0;
					while(ll.hasNext()){
						hw_Tag t=ll.next();
						int VID=t.getV_id();
						String temptag=t.getLabel();
						if(temptag==null||temptag.isEmpty())
							continue;
						boolean flag =false;
						for(int i=0;i<f.length;i++){
							if(f[i].equals(temptag)){
								cnt+=5;//积分jia5分
								flag=true;
								break;
							}
						}
						if(flag){  //用户积分有变
							hw_Volunteer p =Volunteerdao.getpersonvid(VID);
							p.setV_credits(p.getV_credits()+cnt);
						}
					}
				}
				Picturedao.updatepicture(picc);
			}
		}	   
	}
}
