package Util;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

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
	
	
	public static String initText(String ToUserName,String FromUserName,String Content){
		TextMsg textMsg=new TextMsg();
		textMsg.setFromUserName(ToUserName);
		textMsg.setToUserName(FromUserName);
		textMsg.setMsgType(MSG_TEXT);
		textMsg.setCreateTime(new Date().getTime());
		textMsg.setContent(Content);
		return MsgUtil.textToXml(textMsg);
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
		
		sb.append(ReadFileUtil.read("E:\\workspace\\Weixin\\src\\source\\everyday.properties"));
		return sb.toString();
	}
	
	public static void asd(){
		String sa =ReadFileUtil.read("src\\source\\everyday.properties");
		System.out.println(sa);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		asd();
	}

}
