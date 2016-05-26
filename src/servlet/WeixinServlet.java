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
import vo.TextMsg;

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
		
		PrintWriter out=response.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.println(echostr);
		}
		
		try {
			Map<String, String> map=MsgUtil.xmlToMap(request);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String createTime = map.get("CreateTime");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			String msgId = map.get("MsgId");
			
			String msg="";
			if(MsgUtil.MSG_TEXT.equals(msgType)){
				if("1".equals(content)){
					
				}
				TextMsg textMsg=new TextMsg();
				textMsg.setFromUserName(toUserName);
				textMsg.setToUserName(fromUserName);
				textMsg.setMsgType(MsgUtil.MSG_TEXT);
				textMsg.setCreateTime(new Date().getTime());
				textMsg.setContent("您发送的消息是"+content);
				msg=MsgUtil.textToXml(textMsg);
			}else if(MsgUtil.MSG_EVENT.equals(msgType)){
				String event = map.get("event");
				if(MsgUtil.MSG_SUBSCRIBE.equals(event)){
					msg = MsgUtil.initText(toUserName, fromUserName, content);
				}
			}
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
