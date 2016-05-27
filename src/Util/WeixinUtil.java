package Util;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.xml.crypto.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;
import vo.AccessToken;

public class WeixinUtil {
	
	private static final String APPID="wxb4d339a0ef0f7002";
	private static final String APPSECRET="d5c42d8139aa202cec8af7f226a79ee2";

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	
	/**
	 * get请求
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		HttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url
	 * @param outStr
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpost.setEntity(new StringEntity(outStr,"UTF-8"));
		HttpResponse response = client.execute(httpost);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		jsonObject = JSONObject.fromObject(result);
		return jsonObject;
	}
	
	/*
	 * 获取access token
	 * 
	 * */
	public static void getAccessToken() throws ParseException, IOException{
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject=doGetStr(url);
		if(jsonObject!=null){
			String token = jsonObject.getString("access_token");
			HashMap<String, String> map =new HashMap<>();
			map.put("token", token);
			long expires_in = Long.parseLong(jsonObject.getString("expires_in"));
			map.put("time", (new Date().getTime()+expires_in)+"");
			propertyUtil.write("E:\\workspace\\Weixin\\src\\source\\token.properties", map);
		}
	}
	
	public static String getToken() throws ParseException, IOException {
		String time = propertyUtil.read("E:\\workspace\\Weixin\\src\\source\\token.properties", "time");
		long datePre = Long.parseLong(time);
		long dateNow = new Date().getTime();
		if(dateNow>=datePre){
			getAccessToken();
		}
		String token = propertyUtil.read("E:\\workspace\\Weixin\\src\\source\\token.properties", "token");
		return token;
	}
	
	public static void main(String[] args) throws ParseException, IOException {
		getAccessToken();
		System.out.println(new Date().getTime());
	}
}
