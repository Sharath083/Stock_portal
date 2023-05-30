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
import Pojo.UserPojo;


public class DeleteAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name="Login";

		ServletContext sc=request.getServletContext();
		DataBase baseUtil=(DataBase) sc.getAttribute("sqlEmployee");
		Connection connection=baseUtil.getConnector(name);
		Gson gson=new Gson(); 
		PrintWriter out=response.getWriter();
		UserMethods uM=new UserMethods();
		UserPojo up=gson.fromJson(request.getReader(), UserPojo.class);
		if(uM.deleteAccount(up.getUsername(), up.getPassword(), connection)) {
			out.print("Your account "+up.getUsername()+" has been deleted");
		}
		else {
			out.print("Invalid username or password");
		}
	}

}
