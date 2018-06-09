package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.IRegularClassDao;
import dao.impl.RegularClassDao;
import net.sf.json.JSONObject;
import po.Class_basic;
import po.Class_more;

/**
 * Servlet implementation class AddClassDetailServlet
 */
@WebServlet("/AddClassDetailServlet")
public class AddClassDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddClassDetailServlet() {
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
		String score_mean;
		String score_max;
		String score_min;
		String score_above90;
		String score_fail;
		String people_num;
		
		String requestHeader = request.getHeader("user-agent");
		
		if(sBuilder != null && !sBuilder.toString().equals(""))
		{
			JSONObject jsonObject = JSONObject.fromObject(sBuilder.toString());
			
			class_name = jsonObject.get("classname").toString();
			class_teacher = jsonObject.get("classteacher").toString();
			class_dept = jsonObject.get("classdept").toString();
			score_mean = jsonObject.get("mean").toString();
			score_max = jsonObject.get("max").toString();
			score_min = jsonObject.get("min").toString();
			score_above90 = jsonObject.get("above90").toString();
			score_fail = jsonObject.get("fail").toString();
			people_num = jsonObject.get("num").toString();
		}
		else
		{
			class_name = request.getParameter("classname").trim();
			class_teacher = request.getParameter("classteacher").trim();
			class_dept = request.getParameter("classdept").trim();
			score_mean = request.getParameter("mean").trim();
			score_max = request.getParameter("max").trim();
			score_min = request.getParameter("min").trim();
			score_above90 = request.getParameter("above90").trim();
			score_fail = request.getParameter("fail").trim();
			people_num = request.getParameter("num").trim();
		}
		
		Class_basic cb = new Class_basic();
		Class_more cm = new Class_more();
		
		cb.setClass_dept(class_dept);
		cb.setClass_name(class_name);
		cb.setClass_teacher(class_teacher);
		
		cm.setNum_above90(Double.parseDouble(score_above90));
		cm.setScore_mean(Double.parseDouble(score_mean));
		cm.setScore_max(Double.parseDouble(score_max));
		cm.setScore_min(Double.parseDouble(score_min));
		cm.setNum_failed(Double.parseDouble(score_fail));
		cm.setNum_total(Double.parseDouble(people_num));
		
		IRegularClassDao dao = new RegularClassDao();
		int insert_result = 0;
		try {
			insert_result = dao.InsertClassMore(cb, cm);
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
