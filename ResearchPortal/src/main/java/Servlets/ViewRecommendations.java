package Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Methods.DataBase;
import Methods.UserMethods;
import Pojo.RecommendationPojo;


public class ViewRecommendations extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name="View Recommendations";

		ServletContext sc=request.getServletContext();
		DataBase baseUtil=(DataBase) sc.getAttribute("sqlEmployee");
		Connection connection=baseUtil.getConnector(name);
		Gson gson=new Gson(); 
		PrintWriter out=response.getWriter();
		UserMethods uM=new UserMethods();
		RecommendationPojo rp=gson.fromJson(request.getReader(), RecommendationPojo.class);
		int id=uM.tokenValidation(rp.getSessionToken(), connection);
		if(id>0) {
			String s=gson.toJson(uM.list(id, connection));
			response.setContentType("application/JSON");
			out.print(s);			
		}
		else {
			out.print("Sesssion expired or invalid user");
		}
		
	}

	
}