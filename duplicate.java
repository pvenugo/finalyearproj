package com.interfaces;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.interfaces.Split_Data;

import sun.misc.BASE64Encoder;

public class duplicate 
{
	private static final long serialVersionUID = 1L;
	private String output;
	byte part1[]=null;
	byte part2[]=null;
	byte part3[]=null;
    public duplicate() 
    {
        
        
    }
	protected Split_Data split() 
	{
		
    		 try
    	        {
    	        File f1=new File("D:\\android projects\\2016 projects\\.mars 2\\duplicate\\file3.txt");
    	        int length=(int) f1.length();
    	        System.out.println("file3 length::"+length);
    	        part3=new byte[length];
    	        FileInputStream inf=new FileInputStream(f1);
    	       inf.read(part3);
    	       inf.close();
    	      
    	        }
    	        catch(Exception e)
    	        {
    	            e.printStackTrace();
    	        }
    	        
    		
    		 try
    	        {
    	        File f1=new File("D:\\android projects\\2016 projects\\.mars 2\\duplicate\\file2.txt");
    	        int length=(int) f1.length();
    	        System.out.println("file2 length::"+length);
    	        part2=new byte[length];
    	        FileInputStream inf=new FileInputStream(f1);
    	       inf.read(part2);
    	       inf.close();
    	      
    	        }
    	        catch(Exception e)
    	        {
    	            e.printStackTrace();
    	        }
    		 
    		 
    		 try
    	        {
    	        File f1=new File("D:\\android projects\\2016 projects\\.mars 2\\duplicate\\file1.txt");
    	        int length=(int) f1.length();
    	        System.out.println("file1 length::"+length);
    	        part1=new byte[length];
    	        FileInputStream inf=new FileInputStream(f1);
    	       inf.read(part1);
    	       inf.close();
    	      
    	        }
    	        catch(Exception e)
    	        {
    	            e.printStackTrace();
    	        }
    		 Split_Data data=new Split_Data(part1, part2, part3);
    		 return data;
    		
		
	}
	
	//file split
	
	public Split_Data split_and_store(byte[] file)
    {
	  
        int remainter=(file.length)%(file.length/3);
       
        int split=file.length/3;
       
        System.out.println("Remainter values::"+remainter);
       
        String [] file_name={"","D:\\android projects\\2016 projects\\.mars 2\\duplicate\\file3.txt","D:\\android projects\\2016 projects\\.mars 2\\duplicate\\file2.txt","D:\\android projects\\2016 projects\\.mars 2\\duplicate\\file1.txt"};
       
        for(int incount=1, pre=0, b=3, a=file.length/b;incount<4;a=(file.length/3)*incount)
        {
             try
             {
                 if(incount==3)
                    {
                     if(remainter>0)
                     {
                           split+=remainter-1;
                     }
                           System.out.println("Fianle A count::"+split);
                    }
                 
            File file_obj=new File(file_name[b]);
            FileOutputStream f_out=new FileOutputStream(file_obj, false);
            f_out.write(file, pre, split);
            b--;
            pre=a;
           
            incount++;
            f_out.flush();f_out.close();
             }
             catch(Exception e)
             {
                 e.printStackTrace();
                 return null;
             }
           
        }
        return split();
       
       
      
       
    }
	
}