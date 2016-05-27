package Util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

import vo.Image;
import vo.ImageMsg;
import vo.Music;
import vo.MusicMsg;
import vo.NewMsg;
import vo.News;
import vo.TextMsg;

public class MsgUtil {

	public static final String MSG_TEXT="text";
	public static final String MSG_IMAGE="image";
	public static final String MSG_VOICE="voice";
	public static final String MSG_LINK="link";
	public static final String MSG_LOCATION="location";
	public static final String MSG_EVENT="event";
	public static final String MSG_SUBSCRIBE="subscribe";
	public static final String MSG_UNSUBSCRIBE="unsubscribe";
	public static final String MSG_CLICK="CLICK";
	public static final String MSG_VIEW="VIEW";
	public static final String MSG_NEWS="news";
	public static final String MSG_MUSIC="music";
	
	/**
	 * xml 转换成 map
	 * 
	 * */
	public static Map<String,String> xmlToMap(HttpServletRequest request)throws Exception {
		Map<String,String> map=new HashMap<>();
		SAXReader reader=new SAXReader();
		
		InputStream ins=request.getInputStream();
		Document doc = reader.read(ins);
		
		Element root = doc.getRootElement();
		List<Element> list = root.elements();
		for (Element element : list) {
			map.put(element.getName(), element.getText());
		}
		ins.close();
		
		return map;
	}
	
	/*
	 * 将文本对象转换成xml
	 * 
	 * */
	public static String textToXml(TextMsg textMsg) {
		// TODO Auto-generated method stub
		XStream xStream =new XStream();
		xStream.alias("xml",textMsg.getClass());
		String str = xStream.toXML(textMsg);
		return str;
	}
	
	/*
	 * 将图文对象转换成xml
	 * */
	public static String newToXml(NewMsg newMsg) {
		// TODO Auto-generated method stub
		XStream xStream =new XStream();
		xStream.alias("xml",newMsg.getClass());
		xStream.alias("item", new News().getClass());
		String str = xStream.toXML(newMsg);
		return str;
	}
	
	/*
	 * 将图片对象转换成xml
	 * */
	public static String imageToXml(ImageMsg imageMsg) {
		// TODO Auto-generated method stub
		XStream xStream =new XStream();
		xStream.alias("xml",imageMsg.getClass());
		String str = xStream.toXML(imageMsg);
		return str;
	}
	
	/*
	 * 将音乐对象转换成xml
	 * */
	public static String musicToXml(MusicMsg musicMsg) {
		// TODO Auto-generated method stub
		XStream xStream =new XStream();
		xStream.alias("xml",musicMsg.getClass());
		String str = xStream.toXML(musicMsg);
		return str;
	}
	
	
	public static String initText(String ToUserName,String FromUserName,String Content){
		TextMsg textMsg=new TextMsg();
		textMsg.setFromUserName(ToUserName);
		textMsg.setToUserName(FromUserName);
		textMsg.setMsgType(MSG_TEXT);
		textMsg.setCreateTime(new Date().getTime());
		textMsg.setContent(Content);
		return MsgUtil.textToXml(textMsg);
	}
	
	public static String initImage(String ToUserName,String FromUserName){
		Image image=new Image();
		ImageMsg imageMsg=new ImageMsg();
		image.setMediaId("Wn-37JLJLH0TkMtROCcjXHweCLlpnXNqvhDMOZCbzCWfmrIR0t-ql7WLHOdxcC18");
		imageMsg.setImage(image);
		imageMsg.setCreateTime(new Date().getTime());
		imageMsg.setFromUserName(ToUserName);
		imageMsg.setToUserName(FromUserName);
		imageMsg.setMsgType(MSG_IMAGE);
		
		return MsgUtil.imageToXml(imageMsg);
	}
	
	public static String initMusic(String ToUserName,String FromUserName){
		Music music=new Music();
		MusicMsg musicMsg =new MusicMsg();
		music.setTitle("T-ara-Sexy Love");
		music.setThumbMediaId("DEEqq46QczFX-AinHSsCeR4VarK0Qt_PcsWh6Dl2xMi9UhyRqJDRfXwYInjhfNu6");
		music.setDescription("《Sexy Love》是韩国女子组合T-ara于2012年9月发布第七张迷你专辑 《Mirage》中的主打歌。");
		music.setMusicUrl("http://xs2714.ngrok.cc/Weixin/music/T-ara-Sexy Love.mp3");
		music.setHQMusicUrl("http://xs2714.ngrok.cc/Weixin/music/T-ara-Sexy Love.mp3");
		
		musicMsg.setMusic(music);
		musicMsg.setCreateTime(new Date().getTime());
		musicMsg.setFromUserName(ToUserName);
		musicMsg.setToUserName(FromUserName);
		musicMsg.setMsgType(MSG_MUSIC);
		
		return MsgUtil.musicToXml(musicMsg);
	}
	
	public static String initNew(String ToUserName,String FromUserName){
		List<News> newlist=new ArrayList<>();
		News news1=new News();
		news1.setTitle("近期流行的逗比语句");
		news1.setDescription("近期流行的逗比语句");
		news1.setPicUrl("http://xs2714.ngrok.cc/Weixin/images/kidding1.jpg");
		news1.setUrl("http://epaper.subaonet.com/csz8d/html/2014-11/05/content_283272.htm");
		
		News news2=new News();
		news2.setTitle("30条让你瞬间凌乱的经典搞笑句子，太逗比了！");
		news2.setDescription("30条让你瞬间凌乱的经典搞笑句子，太逗比了！");
		news2.setPicUrl("http://xs2714.ngrok.cc/Weixin/images/kidding2.jpg");
		news2.setUrl("http://www.yikexun.cn/jingdiangaoxiaoyulu/2014/0924/24223.html");
		
		News news3=new News();
		news3.setTitle("那些让你笑成逗比的句子");
		news3.setDescription("那些让你笑成逗比的句子");
		news3.setPicUrl("http://xs2714.ngrok.cc/Weixin/images/kidding3.jpg");
		news3.setUrl("http://tieba.baidu.com/p/3027006605");
		
		newlist.add(news1);
		newlist.add(news2);
		newlist.add(news3);
		
		NewMsg newMsg=new NewMsg();
		newMsg.setArticleCount(newlist.size());
		newMsg.setArticles(newlist);
		newMsg.setFromUserName(ToUserName);
		newMsg.setToUserName(FromUserName);
		newMsg.setMsgType(MSG_NEWS);
		newMsg.setCreateTime(new Date().getTime());
		return MsgUtil.newToXml(newMsg);
	}
	
	/*
	 * 主菜单
	 * 
	 * */
	public static String menuText(){
		StringBuffer sb=new StringBuffer();
		sb.append("这是徐硕的订阅号，谢谢你的关注！\n\n");
		sb.append("1:订阅号介绍\n");
		sb.append("2:每天一笑\n\n");
		sb.append("回复'?'调出此菜单.");
		return sb.toString();
	}
	
	/*
	 * 1:订阅号介绍
	 * 
	 * */
	public static String firstText(){
		StringBuffer sb=new StringBuffer();
		sb.append("徐硕测试订阅号，用于每天的微信开发和开心。\nO(∩_∩)O~~！");
		return sb.toString();
	}
	
	/*
	 * 2:每天一笑
	 * 
	 * */
	public static String secondText(){
		StringBuffer sb=new StringBuffer(); 
		
		sb.append(FileUtil.read("E:\\workspace\\Weixin\\src\\source\\everyday.properties"));
		return sb.toString();
	}
	
	
	
	public static void asd(){
		String sa =FileUtil.read("src\\source\\everyday.properties");
		System.out.println(sa);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		asd();
	}

}
