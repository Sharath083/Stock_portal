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
import Pojo.RecommendationPojo;

import Methods.DataBase;
import Methods.UserMethods;



public class SubmitRecommendation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name="submit Recommendarion";

		ServletContext sc=request.getServletContext();
		DataBase baseUtil=(DataBase) sc.getAttribute("sqlEmployee");
		Connection connection=baseUtil.getConnector(name);
		Gson gson=new Gson(); 
		PrintWriter out=response.getWriter();
		UserMethods uM=new UserMethods();
		RecommendationPojo rp=gson.fromJson(request.getReader(), RecommendationPojo.class);
		int uId=uM.tokenValidation(rp.getSessionToken(), connection);
		if(uId>0) {
			uM.insertDetails(rp,uId, connection);
			out.print("Details are added" );
		}
					
		else {
			out.print("Session token has expired or Invalid User");
		}

	
	}

}