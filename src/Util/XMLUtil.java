package Util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;

import vo.TextMsg;

public class XMLUtil {

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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
