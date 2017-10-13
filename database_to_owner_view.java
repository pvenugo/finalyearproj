package com.interfaces;

import java.util.Base64;

public class database_to_owner_view 
{
	private boolean is_img=false;
	private String user_name;
	private int id;
	private byte data[];
	private String data_String;
	private String content_type;
	public String getContent_type() {
		return content_type;
	}
	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}
	public boolean isIs_img() {
		return is_img;
	}
	public String getData_String() 
	{
		if(is_img)
		{
			return Base64.getEncoder().encodeToString(data);
		}
		else
		{
			return new String(data);
		}
		
	}
	public void setData_String(String data_String) {
		this.data_String = data_String;
	}
	public void setIs_img(boolean is_img) {
		this.is_img = is_img;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	

}
