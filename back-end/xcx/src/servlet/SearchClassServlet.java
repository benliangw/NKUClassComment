package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.IRegularClassDao;
import dao.impl.RegularClassDao;
import net.sf.json.JSONObject;
import po.Class_basic;

/**
 * Servlet implementation class SearchClassServlet
 */
@WebServlet("/SearchClassServlet")
public class SearchClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchClassServlet() {
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
		
		String keyword;
		String cursor;
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			keyword = jsonObject.get("keyword").toString();
			cursor = jsonObject.get("cursor").toString();
		}
		else
		{
			keyword= request.getParameter("keyword").trim();
			cursor= request.getParameter("cursor").trim();
		}
		
		IRegularClassDao dao = new RegularClassDao();
		List<Class_basic> result = null;
		try {
			result = dao.SearchClasses(keyword, Integer.parseInt(cursor));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		JSONObject j=new JSONObject();
		if(result.size() == 1 && result.get(0).getClass_id() == -1)
		{
			j.put("status", "150");
		}
		else if(result.size() == 0) //没有更多的课程了
			j.put("status", "100");
		else
		{
			j.put("status", "200");
			j.put("class", result);
		}
		response.getWriter().write(j.toString()); 
		dao.close();
	}

}
