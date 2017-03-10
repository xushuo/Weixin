package Util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
import vo.Button;
import vo.ClickButton;
import vo.Menu;
import vo.ViewButton;

public class WeixinUtil {
	
	private static final String APPID="wxb4d339a0ef0f7002";
	private static final String APPSECRET="d5c42d8139aa202cec8af7f226a79ee2";

	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	//上传临时消息
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	//新建菜单
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	//查询菜单
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	//删除菜单
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
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
	 * 并且存入到properties文件
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
			propertyUtil.write("E:\\Workspace\\Weixin\\src\\source\\token.properties", map);
		}
	}
	/*
	 * 本地从本地properties文件中读取access token,减少请求次数
	 * 如果token过期则刷新。
	 * */
	public static String getToken() throws ParseException, IOException {
		String time = propertyUtil.read("E:\\Workspace\\Weixin\\src\\source\\token.properties", "time");
		long datePre = Long.parseLong(time);
		long dateNow = new Date().getTime();
		if(dateNow>=datePre){
			getAccessToken();
		}
		String token = propertyUtil.read("E:\\Workspace\\Weixin\\src\\source\\token.properties", "token");
		return token;
	}
	
	
	/**
	 * 文件上传
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 * 
	 * 上传的临时多媒体文件有格式和大小限制，如下：
	    图片（image）: 1M，支持JPG格式
	    语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
	    视频（video）：10MB，支持MP4格式
	    缩略图（thumb）：64KB，支持JPG格式
	    
	 */
	public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}

		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST"); 
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); 

		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		//设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);

		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}
	
	/*
	 * 初始化菜单
	 * 自定义菜单最多包括3个一级菜单，每个一级菜单最多包含5个二级菜单。
	 * */
	public static Menu initMenu(){
		Menu menu=new Menu();
		
		ClickButton button1=new ClickButton();
		button1.setName("click菜单");
		button1.setType("click");
		button1.setKey("11");
		
		ViewButton button2=new ViewButton();
		button2.setName("view菜单");
		button2.setType("view");
		button2.setUrl("http://www.dytt8.net/");
		
		ClickButton button3=new ClickButton();
		button3.setName("扫码事件");
		button3.setType("scancode_push");
		button3.setKey("31");
		
		ClickButton button4=new ClickButton();
		button4.setName("扫码消息接收");
		button4.setType("scancode_waitmsg");
		button4.setKey("41");
		
		ClickButton button5=new ClickButton();
		button5.setName("拍照事件");
		button5.setType("pic_sysphoto");
		button5.setKey("51");
		
		ClickButton button6=new ClickButton();
		button6.setName("拍照选择事件");
		button6.setType("pic_photo_or_album");
		button6.setKey("61");
		
		ClickButton button7=new ClickButton();
		button7.setName("微信相册事件");
		button7.setType("pic_weixin");
		button7.setKey("71");
		
		ClickButton button8=new ClickButton();
		button8.setName("地理位置事件");
		button8.setType("location_select");
		button8.setKey("81");
		
		Button button=new Button();
		button.setName("菜单");
		button.setSub_button(new Button[]{button3,button5,button7,button8});
		
		menu.setButton(new Button[]{button1,button2,button});
		return menu;
	}
	
	public static int createMenu(String token,String menu) throws ParseException, IOException {
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject !=null){
			result=jsonObject.getInt("errcode");
		}
		return result;
	}
	
	public static JSONObject queryMenu(String token) throws ParseException, IOException {
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}
	
	public static int deleteMenu(String token) throws ParseException, IOException {
		int result = 0;
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject !=null){
			result=jsonObject.getInt("errcode");
		}
		return result;
	}
	
	public static void main(String[] args) throws ParseException, IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		//getAccessToken();
		//System.out.println(new Date().getTime());
		//upload("E:\\Workspace\\Weixin\\WebContent\\images\\thumb2.jpg", getToken(), "thumb");
		String menu =JSONObject.fromObject(WeixinUtil.initMenu()).toString();
		int result= WeixinUtil.createMenu(WeixinUtil.getToken(), menu);
		/*JSONObject jsonObject= WeixinUtil.queryMenu(WeixinUtil.getToken());
		int result= WeixinUtil.deleteMenu(WeixinUtil.getToken());
		System.out.println(jsonObject);
		*/if(result==0){
			System.out.println("创建菜单成功");
		}else{
			System.out.println(""+result);
		}
	}
}
