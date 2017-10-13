package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import task.Task;





public class Main extends HttpServlet
{
	
	private Task task;

	@Override
	public void init(ServletConfig config) throws ServletException 
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/duplicate", "root", "root");
		task=new Task(con);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		{
		System.out.println("get methos called");
		String uri=req.getRequestURI();
		int l=uri.lastIndexOf("/");
		uri=uri.substring(l+1, uri.length());
		
		switch(uri)
		{
		
		case "view_your_data":
		{
			task.view_your_data(req,resp);
			break;
		}
		case "view_shared_data":
		{
			task.view_shared_data(req,resp);
			break;
		}
		case "share_data":
		{
			task.share_data(req,resp);
			break;
		}
		
		case "remove":
		{
			task.share_remove(req,resp);
			break;
		}
		case "requesttoview":
		{
			task.request_to_view(req,resp);
			break;
		}
		
		case "one_time_validate":
		{
			task.one_time_validate(req,resp);
			break;
		}
		
		case "remove_share":
		{
			task.remove_share(req,resp);
			break;
		}
		
		}
	}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		System.out.println("post methos called");
		String uri=req.getRequestURI();
		int l=uri.lastIndexOf("/");
		uri=uri.substring(l+1, uri.length());
		
		switch(uri)
		{
		case "login":
		{
			String usernmae=req.getParameter("username");
			String password=req.getParameter("password");
			
			if(usernmae.equals("user1")&&password.equals("123"))
			{
				req.getRequestDispatcher("profile.jsp").forward(req, resp);
			}
			else if(usernmae.equals("user2")&&password.equals("123"))
			{
				req.getRequestDispatcher("profile.jsp").forward(req, resp);
			}
			else
			{
				req.setAttribute("data","Invalid Username And Password");
				req.getRequestDispatcher("error.jsp").forward(req, resp);
			}
			break;
			
		}
		case "upload":
		{
		    task.store_uploaded_file(req,resp);
			
			break;
		}
		
		}
	}
}
