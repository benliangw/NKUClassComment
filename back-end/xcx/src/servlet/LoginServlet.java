package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.IRegularUserDao;
import dao.impl.RegularUserDao;
import net.sf.json.JSONObject;
import po.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		StringBuilder sBuilder = new StringBuilder();
		BufferedReader bReader = request.getReader();
		try {
			String line;
			while((line = bReader.readLine()) != null)
			{
				sBuilder.append(line).append('\n');
			}
		} finally {
			bReader.close();
		}
		
		String userid;
		String password;
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			userid = jsonObject.get("userid").toString();
			password = jsonObject.get("password").toString();
		}
		else
		{
			userid= request.getParameter("userid").trim();
			password = request.getParameter("password").trim();
		}
		
		IRegularUserDao dao=new RegularUserDao();
		User u=new User();
		u.setUserId(Integer.parseInt(userid));
		u.setPassword(password);
		User user=dao.login(u);
		JSONObject j=new JSONObject();
		if(user==null)
			j.put("status", "500");
		else if(user.getUserId()==-1)
			j.put("status", "404");
		else
		{
			j.put("status", "200");
			j.put("user", user);
		}
		response.getWriter().write(j.toString()); 
		dao.close();
	}
}
