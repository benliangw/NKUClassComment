package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.IRegularClassDao;
import dao.IRegularCommentDao;
import dao.impl.RegularClassDao;
import dao.impl.RegularCommentDao;
import net.sf.json.JSONObject;
import po.Class_basic;
import po.Comment;

/**
 * Servlet implementation class AddClassServlet
 */
@WebServlet("/AddClassServlet")
public class AddClassServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddClassServlet() {
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
		
		String class_name;
		String class_teacher;
		String class_dept;
		
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			class_name = jsonObject.get("classname").toString();
			class_teacher = jsonObject.get("classteacher").toString();
			class_dept = jsonObject.get("classdept").toString();
		}
		else
		{
			class_name = request.getParameter("classname").trim();
			class_teacher = request.getParameter("classteacher").trim();
			class_dept = request.getParameter("classdept").trim();
		}
		
		Class_basic cb = new Class_basic();
		
		cb.setClass_dept(class_dept);
		cb.setClass_name(class_name);
		cb.setClass_teacher(class_teacher);
		
		IRegularClassDao dao = new RegularClassDao();
		int insert_result = 0;
		try {
			insert_result = dao.InsertClassBasic(cb);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JSONObject j=new JSONObject();
		if(insert_result == 1)
			j.put("status", "200");
		else
			j.put("status", "500");
		
		response.getWriter().write(j.toString()); 
		dao.close();
	}

}
