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
import Pojo.FeedbackPojo;


public class Feedback extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name="Feedback";

		ServletContext sc=request.getServletContext();
		DataBase baseUtil=(DataBase) sc.getAttribute("sqlEmployee");
		Connection connection=baseUtil.getConnector(name);
		Gson gson=new Gson(); 
		PrintWriter out=response.getWriter();
		UserMethods uM=new UserMethods();
		
		
		FeedbackPojo fp=gson.fromJson(request.getReader(), FeedbackPojo.class);
		int id=uM.tokenValidation(fp.getToken(), connection);
		if(id>0 && uM.checkStock(fp.getRid(), connection)) {
			uM.feedback(fp,id,connection) ;
			out.print("Thank you for feedback");

		}
		else {
			out.print("Session has expired or Product with id "+fp.getRid()+" does not exists");
		}
	}

}
