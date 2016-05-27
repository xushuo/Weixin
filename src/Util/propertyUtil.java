package Util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class propertyUtil {

	public static void write(String path,Map<String, String> map){
		try{
			Properties p = new Properties();
			InputStream io = new FileInputStream(path);
			OutputStream outputFile = new FileOutputStream(path); 
			p.load(io);
			for (String key : map.keySet()) {
				p.setProperty(key,map.get(key));
				
			}
			p.store(outputFile,"Local _ access token"); 
			outputFile.close();
			io.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}

	public static String read(String path,String key){
		String value="";
		try{
			Properties p = new Properties();
			InputStream io = new FileInputStream(path);
			p.load(io);
			value = p.getProperty(key);
			io.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		return value;
	}
	
	public static void main(String[] args) {
		String str = read("E:\\workspace\\Weixin\\src\\source\\token.properties", "token");
		System.out.println(str);
		HashMap<String, String> map =new HashMap<>();
		map.put("token", "init2");
		map.put("time", "1464334219200");
		write("E:\\workspace\\Weixin\\src\\source\\token.properties", map);
	}
}
