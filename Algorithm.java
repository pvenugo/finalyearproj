package algorithm;

import java.awt.Button;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.apache.commons.lang.ArrayUtils;

import com.interfaces.Split_Data;
import com.interfaces.duplicate;

public class Algorithm 
{
	//public static IvParameterSpec IV = new IvParameterSpec(new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
	
	public Algorithm() 
	{
		
	}
	
	static String Algorithm="DES";
	public static SecretKey get_Key(String algorithm)
	{
		SecretKey key=null;
		try
		{
		
		KeyGenerator generator=KeyGenerator.getInstance(Algorithm);
		 key=generator.generateKey();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	return key;
		
	}
	
	
	private static void database(SecretKey secredkey) 
	{
		try
		{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connction=DriverManager.getConnection("jdbc:mysql://localhost:3306/sample","root","root");
		PreparedStatement state=connction.prepareStatement("insert into main (key1) values(?)");
		state.setObject(1, secredkey);
		if(state.executeUpdate()>0)
		{
			System.out.println("obje Stored Success");
		}
		else
		{
			System.out.println("Obj Store Failed");
		}
	
	
	PreparedStatement ss=connction.prepareStatement("select * from main");
	ResultSet result=ss.executeQuery();
	while(result.next())
	{
		byte[] sk=(byte[]) result.getObject(3);
		
	ByteArrayInputStream bin=new ByteArrayInputStream(sk);
	ObjectInputStream oi=new ObjectInputStream(bin);
	SecretKey data=(SecretKey) oi.readObject();
		
		
		System.out.println("Sk:alg::"+data.getAlgorithm());
	}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static byte[] Decryption(byte[] data, SecretKey key) 
	{
		try
		{
	  Cipher de_ciper=Cipher.getInstance(Algorithm);
		de_ciper.init(Cipher.DECRYPT_MODE, key);
		
		if(key!=null&&data.length>0)
		{
			
			System.out.print("de-side values::"+data.length);
			try {
				
				byte[] df=de_ciper.doFinal(data);
				return df;
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

     
		return null;
	}
	
	public static Split_Data Encryption(byte[] file,SecretKey key)
	{
		try
		{
		Cipher en_ciper=Cipher.getInstance(Algorithm);
		en_ciper.init(Cipher.ENCRYPT_MODE, key);
		if(file.length>0&&key!=null)
	     {
	    	 
	    	 try {
				byte[] ef=en_ciper.doFinal(file);
				
			
	    duplicate full_data=new duplicate();
	    return full_data.split_and_store(ef);
	    
	    
	    
	    	 } catch (IllegalBlockSizeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	 
	     }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}