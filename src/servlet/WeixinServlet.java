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

import com.sun.swing.internal.plaf.metal.resources.metal;

import Util.CheckUtil;
import Util.XMLUtil;
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
		String signature= request.getParameter("signature");
		String timestamp= request.getParameter("timestamp");
		String nonce= request.getParameter("nonce");
		String echostr= request.getParameter("echostr");
		
		PrintWriter out=response.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			System.out.println(0);
			out.println(echostr);
		}
		
		try {
			Map<String, String> map=XMLUtil.xmlToMap(request);
			String ToUserName = map.get("ToUserName");
			String FromUserName = map.get("FromUserName");
			String CreateTime = map.get("CreateTime");
			String MsgType = map.get("MsgType");
			String Content = map.get("Content");
			String MsgId = map.get("MsgId");
			String msg="";
			if("text".equals(MsgType)){
				TextMsg textMsg=new TextMsg();
				textMsg.setFromUserName(ToUserName);
				textMsg.setToUserName(FromUserName);
				textMsg.setMsgType("text");
				textMsg.setCreateTime(new Date().getTime());
				textMsg.setContent("您发送的消息是"+Content);
				msg=XMLUtil.textToXml(textMsg);
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
