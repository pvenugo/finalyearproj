package database;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.crypto.SecretKey;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.ConnectionPoolDataSource;

import org.apache.commons.lang.ArrayUtils;

import com.interfaces.Split_Data;
import com.interfaces.database_shared_view;
import com.interfaces.database_to_owner_view;

import algorithm.Algorithm;



public class Database 
{
	Random ran=new Random();
Connection connection;
	public Database(java.sql.Connection con) 
	{
		connection=con;
	}
	public boolean store_upload_data(String user, String content_type, String signature, Split_Data full_data, SecretKey key) 
	{
		try
		{
			PreparedStatement statement=connection.prepareStatement("insert into parent (owners,content_type,file1,file2,file3,signature,key1) values(?,?,?,?,?,?,?)");
			statement.setString(1,user);
			statement.setString(2, content_type);
			statement.setBytes(3, full_data.getFile1());
			statement.setBytes(4, full_data.getFile2());
			statement.setBytes(5, full_data.getFile3());
			statement.setString(6, signature);
			statement.setObject(7, key);
			if(statement.executeUpdate()>0)
			{
				return true;
			}
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	public Hashtable<String, String> check_signature_isvalid(String signature) 
	{
		try
		{
		PreparedStatement statement=connection.prepareStatement("select * from parent where signature=?");	
		statement.setString(1,signature);
		ResultSet result=statement.executeQuery();
		while(result.next())
		{
			int id=result.getInt(1);
			String user =result.getString(2);
			Hashtable<String,String> hashtable=new Hashtable<>();
			hashtable.put("id", ""+id);
			hashtable.put("user", user);
			return hashtable;
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public String add_owner_to_id(int id, String user) 
	{
		String final_user=null;
		if(user.equals("user1"))
		{
			user=user+",user2";
		}
		else
		{
			user=user+",user1";
		}
		try
		{
		PreparedStatement statement=connection.prepareStatement("update  parent set owners = ? where id="+id);
		statement.setString(1,user);
	  if(statement.executeUpdate()>0)
	  {
		  System.out.println("owner ship add in parent:: "+user);
		  return "duplicate data so Ownership add in parent dataset";
	  }
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public boolean add_in_share_dataset(String who, String id) 
	{
		String to=null;
		if(who.equals("user1"))
		{
			to="user2";
		}
		else
		{
			to="user1";
		}
		try
		{
		PreparedStatement statement=connection.prepareStatement("insert into child (pid,to1,from1,view_status) values(?,?,?,?)");
		statement.setString(1,id);
		statement.setString(2, to);
		statement.setString(3,who);
		statement.setString(4, "false");
		if(statement.executeUpdate()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	public List<database_to_owner_view> view_your_data(String user) 
	{
		try
		{
		PreparedStatement statement=connection.prepareStatement("select * from parent where owners like '%"+user+"%'");	
		ResultSet result=statement.executeQuery();
		List<database_to_owner_view> list=new ArrayList<>();
		while(result.next())
		{
			database_to_owner_view data=new database_to_owner_view();
			
			int id=result.getInt(1);
			String owner=result.getString(2);
			String content_type=result.getString(3);
			byte[] file1=result.getBytes(4);
			byte[] file2=result.getBytes(5);
			byte[] file3=result.getBytes(6);
			byte[] bkey=result.getBytes(8);
			ByteArrayInputStream in=new ByteArrayInputStream(bkey);
			ObjectInputStream obj_in=new ObjectInputStream(in);
			Object obj=obj_in.readObject();
			SecretKey key=(SecretKey)obj;
			
			byte[] two=ArrayUtils.addAll(file1,file2);
			byte[] final_byte=ArrayUtils.addAll(two,file3);
			
		   // String f1=new String(file1);
		   // String f2=new String(file2);
		  //  String f3=new String(file3);
		   // String final_file=f1+f2+f3;
		    System.out.println("Length of Data::"+final_byte.length);
			
			byte[] result_data=Algorithm.Decryption(final_byte, key);
			
			data.setContent_type(content_type);
			data.setData(result_data);
			data.setId(id);
			if(content_type.contains("image"))
			{
			data.setIs_img(true);
			}
			else
			{
				data.setIs_img(false);
			}
			data.setUser_name(user);
			
			list.add(data);
			
		}
		
		return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public boolean remove_owner_datas(String who, String id) 
	{
		String other_user=null;
		if(who.equals("user1"))
		{
			other_user="user2";
		}
		else
		{
			other_user="user1";
		}
		
		try
		{
			PreparedStatement chile_remove=connection.prepareStatement("delete from  child where from1 =? and pid=?");
			chile_remove.setString(1, who);
			chile_remove.setString(2, id);
			if(chile_remove.executeUpdate()>0)
			{
				System.out.println("Chile data removed Successfully::"+who+" id::"+id);
			}
			
			
			PreparedStatement statement=connection.prepareStatement("delete from parent where id="+id+" and owners=?");
			statement.setString(1, who);
			if(statement.executeUpdate()>0)
               {
				System.out.println("parent Data removed:"+who);
				return true;
               }
			else
			{
				PreparedStatement update_ownership=connection.prepareStatement("update parent set owners=? where id=? ");
				update_ownership.setString(1, other_user);
				update_ownership.setInt(2, Integer.parseInt(id));
				if(update_ownership.executeUpdate()>0)
				{
					System.out.println("one owner removed :"+who);
					return true;
				}
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	public List<database_shared_view> view_shared_data(String user) 
	{
		try
		{
		PreparedStatement statement=connection.prepareStatement("select * from child where to1=?");	
		statement.setString(1, user);
		ResultSet result=statement.executeQuery();
		List<database_shared_view> list=new ArrayList<>();
		while(result.next())
		{
			database_shared_view share=new database_shared_view();
			
			int id=result.getInt(1);
			int pid=result.getInt(2);
			String to=result.getString(3);
			String from=result.getString(4);
			String view_status=result.getString(5);
			
			
			database_to_owner_view data = view_your_data_from_id(pid,user);
			share.setFrom(from);
			share.setId(id);
			share.setParent_data(data);
			share.setPid(""+pid);
			share.setTo(to);
			share.setView_status(view_status);
					
			
			list.add(share);
			
		}
		
		return list;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public database_to_owner_view view_your_data_from_id(int id,String user) 
	{
		try
		{
		PreparedStatement statement=connection.prepareStatement("select * from parent where id="+id);	
		ResultSet result=statement.executeQuery();
		List<database_to_owner_view> list=new ArrayList<>();
		while(result.next())
		{
			database_to_owner_view data=new database_to_owner_view();
			
			int iid=result.getInt(1);
			String owner=result.getString(2);
			String content_type=result.getString(3);
			byte[] file1=result.getBytes(4);
			byte[] file2=result.getBytes(5);
			byte[] file3=result.getBytes(6);
			byte[] bkey=result.getBytes(8);
			ByteArrayInputStream in=new ByteArrayInputStream(bkey);
			ObjectInputStream obj_in=new ObjectInputStream(in);
			Object obj=obj_in.readObject();
			SecretKey key=(SecretKey)obj;
			
			byte[] two=ArrayUtils.addAll(file1,file2);
			byte[] final_byte=ArrayUtils.addAll(two,file3);
			
		   // String f1=new String(file1);
		   // String f2=new String(file2);
		  //  String f3=new String(file3);
		   // String final_file=f1+f2+f3;
		    System.out.println("Length of Data::"+final_byte.length);
			
			byte[] result_data=Algorithm.Decryption(final_byte, key);
			
			data.setContent_type(content_type);
			data.setData(result_data);
			data.setId(iid);
			if(content_type.contains("image"))
			{
			data.setIs_img(true);
			}
			else
			{
				data.setIs_img(false);
			}
			data.setUser_name(user);
			
			//list.add(data);
			return data;
			
		}
		
		return null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	public boolean request_to_view(String id, String user) 
	{
		try 
		{
			long samples=ran.nextLong();
			
			PreparedStatement statement=connection.prepareStatement("update child set  otp=? where id="+id);
			
			statement.setString(1, ""+samples);
			if(statement.executeUpdate()>0)
			{
				System.out.println("view status true success");
				send_mail("triossoftwareteam@gmail.com", id, ""+samples,user);
				return true;
			}
			else
			{
				System.out.println("View status error::");
				return false;
			}
			
			
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	

	private boolean send_mail(String to, String id,String pass, String user) 
	{
		System.out.println("Mail send called");
		
			
			 final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
			 	     Properties props = System.getProperties();
			     props.setProperty("mail.smtp.host", "smtp.gmail.com");
			     props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
			     props.setProperty("mail.smtp.socketFactory.fallback", "false");
			     props.setProperty("mail.smtp.port", "465");
			     props.setProperty("mail.smtp.socketFactory.port", "465");
			     props.put("mail.smtp.auth", "true");
			     props.put("mail.debug", "true");
			     props.put("mail.store.protocol", "pop3");
			     props.put("mail.transport.protocol", "smtp");
			     final String username = "triossoftwareteam@gmail.com";//
			     final String password = "triossoft";
			     try{
			     Session session = Session.getDefaultInstance(props, 
			                          new Authenticator(){
			                             protected PasswordAuthentication getPasswordAuthentication() {
			                                return new PasswordAuthentication(username, password);
			                             }});

			   // -- Create a new message --
			     Message msg = new MimeMessage(session);

			  // -- Set the FROM and TO fields --
			     msg.setFrom(new InternetAddress("triossoftwareteam@gmail.com"));
			     msg.setRecipients(Message.RecipientType.TO, 
			                      InternetAddress.parse(to,false));
			     msg.setSubject("Hello");
			     
			     msg.setText(" One Time Password Is:"+pass+"\n\n\t http://"+"localhost"+":8080/duplicate/onetime.jsp?id="+id+"&user="+user);
			     msg.setSentDate(new Date());
			     Transport.send(msg);
			     System.out.println("Mail sent.");
			     return true;
			  }
			     catch (MessagingException e)
			     { System.out.println("Erreur d'envoi, cause: " + e);
			     return false;
			     }
			
			
		
		
	}
	public boolean one_time_validate(String id, String password, String user) 
	{
		try
		{
			PreparedStatement statement=connection.prepareStatement("select view_status from child where id="+id+" and otp=?");
		statement.setString(1, password);
		ResultSet result=statement.executeQuery();
		if(result.next())
		{
			System.out.println("otp is valid");
			PreparedStatement pre=connection.prepareStatement("update child set view_status=? where id="+id);
			pre.setString(1, "true");
			if(pre.executeUpdate()>0)
			{
			   System.out.println("view status set true");
			   return true;
			}
			else
			{
				return false;
			}
			
		}
		else
		{
			System.out.println("invalid otp");
			return false;
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	public boolean remove_share(String id) 
	{
		try
		{
			PreparedStatement statement=connection.prepareStatement("delete from child where id="+id);
			if(statement.executeUpdate()>0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}


	

}
