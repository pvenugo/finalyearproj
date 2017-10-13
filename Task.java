package task;


import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.interfaces.Split_Data;
import com.interfaces.database_shared_view;
import com.interfaces.database_to_owner_view;

import algorithm.Algorithm;
import database.Database;

public class Task 
{
	
private Database data;
private byte[] file;

public Task(java.sql.Connection con) 
{
 data=new Database(con);
}

public void store_uploaded_file(HttpServletRequest req, HttpServletResponse resp) 
{
	DiskFileItemFactory disk=new DiskFileItemFactory();
	
	ServletFileUpload upload=new ServletFileUpload(disk);
	try {
		List<FileItem> list=upload.parseRequest(req);
		Iterator<FileItem> it=list.iterator();
		String user=null;
		String content_type=null;
		
		while (it.hasNext()) 
		{
			FileItem fileItem = (FileItem) it.next();
		if(fileItem.isFormField())
		{
			String name=fileItem.getFieldName();
			switch (name) 
			{
			case "user":
				user=fileItem.getString();
				break;

			default:
				break;
			}
		}
		else
		{
			 file=fileItem.get();
			content_type=fileItem.getContentType();
		}
			
		}
		if(user!=null && content_type!=null)
		{
			PrintWriter write=resp.getWriter();
			
			String signature=getSignature(file);
			Hashtable<String, String> id=data.check_signature_isvalid(signature);
			if(id==null)
			{
				SecretKey key=Algorithm.get_Key("AES");
				Split_Data full_data=Algorithm.Encryption(file, key);
			
		if(data.store_upload_data(user,content_type,signature,full_data,key))
		{
			req.setAttribute("data", "Data stored success");
			req.getRequestDispatcher("error.jsp").forward(req, resp);
		}
		else
		{
			req.setAttribute("data", "Data stored failed");
			req.getRequestDispatcher("error.jsp").forward(req, resp);
		}
		}
			else
			{
				int in_id=Integer.parseInt(id.get("id"));
				String database_user=id.get("user");
				if(!database_user.contains(user))
				{
				System.out.println("duplication data");
				String result=data.add_owner_to_id(in_id,user);
				if(result!=null)
				{
					write.write(result);
				}
				else
				{
					write.write("Data storage error ");
				}
									
			    }
				else
				{
					System.out.println("duplicate data in your database");
					write.write("Duplicate Data in Your Database");
				}
			}
			
		}
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}

public String getSignature(byte[] b)
{
	MessageDigest md;
	try {
		 	md = MessageDigest.getInstance("SHA-256");
	
		 
	byte[] hash=md.digest(b);
	BigInteger bi=new BigInteger(1,hash);
	String output=bi.toString(16);
	System.out.println("Message:"+output);
	return output;
	}catch(Exception r)
	{
		r.printStackTrace();
		return null;
	}
}

public void share_data(HttpServletRequest req, HttpServletResponse resp) 
{
	String who=req.getParameter("who");
	String id=req.getParameter("id");
	if(data.add_in_share_dataset(who,id))
	{
		try
		{
		req.setAttribute("response","shared success");
		req.getRequestDispatcher("view_your_data").forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	else
	{
		try
		{
		req.setAttribute("response","Shared Data failed");
		req.getRequestDispatcher("view_your_data").forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}

public void share_remove(HttpServletRequest req, HttpServletResponse resp) 
{
	String who=req.getParameter("who");
	String id=req.getParameter("id");
	if(data.remove_owner_datas(who,id))
	{
		try
		{
		req.setAttribute("response","Remove Success");
		req.getRequestDispatcher("view_your_data").forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	else
	{
		try
		{
		req.setAttribute("response","Remove Failed");
		req.getRequestDispatcher("view_your_data").forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}

public void view_your_data(HttpServletRequest req, HttpServletResponse resp) 
{

	String user=req.getParameter("user");
	List<database_to_owner_view> list=data.view_your_data(user);
	if(list!=null)
	{
		try
		{
			req.setAttribute("data", list);
		    req.getRequestDispatcher("view_your_datajsp.jsp").forward(req, resp);
		    System.out.println("view-your-data_Successfully");
		}
		catch(Exception e)
		{
			System.out.println("View_data_failed");
			e.printStackTrace();
		}
	}
}

public void view_shared_data(HttpServletRequest req, HttpServletResponse resp)
{
	String user=req.getParameter("user");
	List<database_shared_view> list=data.view_shared_data(user);
	if(list!=null)
	{
		try
		{
			req.setAttribute("data", list);
		    req.getRequestDispatcher("view_shared_data.jsp").forward(req, resp);
		    System.out.println("view-your-data_Successfully");
		}
		catch(Exception e)
		{
			System.out.println("View_data_failed");
			e.printStackTrace();
		}
	}
	
}

public void request_to_view(HttpServletRequest req, HttpServletResponse resp) 
{
	String user=req.getParameter("who");
	String id=req.getParameter("id");
	if(data.request_to_view(id,user))
	{
		try
		{
		req.setAttribute("data", "one time password sent to Your mail");
		req.getRequestDispatcher("success.jsp").forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	else
	{
		try
		{
		req.setAttribute("data", "Server Error");
		req.getRequestDispatcher("success.jsp").forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}

public void one_time_validate(HttpServletRequest req, HttpServletResponse resp) 
{
	String id=req.getParameter("id");
	String password=req.getParameter("password");
	String user=req.getParameter("user");
	if(data.one_time_validate(id,password,user))
	{
		try
		{
		
		req.getRequestDispatcher("view_shared_data?user="+user).forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	else
	{
		try
		{
		req.setAttribute("data","invalid one time password");
		req.getRequestDispatcher("success.jsp").forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}

public void remove_share(HttpServletRequest req, HttpServletResponse resp)
{
	String id=req.getParameter("id");
	
	String user=req.getParameter("who");
	if(data.remove_share(id))
	{
		try
		{
			req.setAttribute("response","Data removed Success");
		req.getRequestDispatcher("view_shared_data?user="+user).forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	else
	{
		try
		{
		req.setAttribute("data","Server Error");
		req.getRequestDispatcher("success.jsp").forward(req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}





}
