package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.IRegularClassDao;
import dao.impl.RegularClassDao;
import net.sf.json.JSONObject;
import po.Class_more;

/**
 * Servlet implementation class GetClassMoreByIDServlet
 */
@WebServlet("/GetClassMoreByIDServlet")
public class GetClassMoreByIDServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetClassMoreByIDServlet() {
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
		
		String classid;
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			classid = jsonObject.get("classid").toString();
		}
		else
		{
			classid= request.getParameter("classid").trim();
		}
		
		IRegularClassDao dao = new RegularClassDao();
		Class_more cm = new Class_more();
		try {
			cm = dao.GetClassMoreByID(Integer.parseInt(classid));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		JSONObject j=new JSONObject();
		if(cm == null) //该课程的详细信息
			j.put("status", "100");
		else
		{
			j.put("status", "200");
			j.put("class", cm);
		}
		response.getWriter().write(j.toString()); 
		dao.close();
	}

}
