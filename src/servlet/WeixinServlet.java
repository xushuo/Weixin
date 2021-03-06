package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Util.CheckUtil;
import Util.MsgUtil;

/**
 * Servlet implementation class WeixinServlet
 */
@WebServlet("/WeixinServlet")
public class WeixinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WeixinServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String signature= request.getParameter("signature");
		String timestamp= request.getParameter("timestamp");
		String nonce= request.getParameter("nonce");
		String echostr= request.getParameter("echostr");
	/*	System.out.println("signature:"+signature);
		System.out.println("timestamp:"+timestamp);
		System.out.println("nonce:"+nonce);
		System.out.println("echostr:"+echostr);*/
		PrintWriter out=response.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}
		
		try {
			Map<String, Object> map=MsgUtil.xmlToMap(request);
			String toUserName = map.get("ToUserName").toString();
			String fromUserName = map.get("FromUserName").toString();
			String createTime = map.get("CreateTime").toString();
			String msgType = map.get("MsgType").toString();
			//String msgId = map.get("MsgId").toString();
			String msg="";
			System.out.println("msgType:"+msgType);
			if(MsgUtil.MSG_TEXT.equals(msgType)){
				String content = map.get("Content").toString();
				if("1".equals(content)){
					msg = MsgUtil.initText(toUserName, fromUserName, MsgUtil.firstText());
				}
				if("2".equals(content)){
					msg = MsgUtil.initText(toUserName, fromUserName, MsgUtil.secondText());
				}
				if("3".equals(content)){
					msg = MsgUtil.initNew(toUserName, fromUserName); 
				}
				if("4".equals(content)){
					msg = MsgUtil.initImage(toUserName, fromUserName); 
				}
				if("5".equals(content)){
					msg = MsgUtil.initMusic(toUserName, fromUserName); 
				}
				else if("?".equals(content)||"？".equals(content)){
					msg = MsgUtil.initText(toUserName, fromUserName, MsgUtil.menuText());
				}
			}else if(MsgUtil.MSG_EVENT.equals(msgType)){
				String event = map.get("Event").toString();
				System.out.println("event:"+event);
				if(MsgUtil.MSG_SUBSCRIBE.equals(event)){
					System.out.println("MsgUtil.menuText():"+MsgUtil.menuText());
					msg = MsgUtil.initText(toUserName, fromUserName, MsgUtil.menuText());
				}
				if(MsgUtil.MSG_CLICK.equals(event)){
					System.out.println("MsgUtil.menuText():"+MsgUtil.menuText());
					msg = MsgUtil.initText(toUserName, fromUserName, MsgUtil.menuText());
				}
				if(MsgUtil.MSG_VIEW.equals(event)){
					String url = map.get("EventKey").toString();
					System.out.println("url:"+url);
					msg = MsgUtil.initText(toUserName, fromUserName, url);
				}
				if(MsgUtil.MSG_SCANCODE.equals(event)){
					String key = map.get("EventKey").toString();
					//String ScanCodeInfo = map.get("ScanCodeInfo");
					System.out.println("key:"+key);
					System.out.println("map:"+map);
					msg = MsgUtil.initText(toUserName, fromUserName, key);
				}
				if(MsgUtil.MSG_PIC_SYSPHOTO.equals(event)){
					String key = map.get("EventKey").toString();
					//String SendPicsInfo = map.get("SendPicsInfo");
					System.out.println("key:"+key);
					System.out.println("map:"+map);
					msg = MsgUtil.initText(toUserName, fromUserName, key);
				}
			}else if(MsgUtil.MSG_LOCATION.equals(msgType)){
				String label = map.get("Label").toString();//开发文档有错误
				System.out.println("map:"+map);
				msg = MsgUtil.initText(toUserName, fromUserName, label);
			}
			System.out.println("msg"+msg);
			out.print(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			out.close();
		}
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
