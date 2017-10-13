package com.interfaces;

public class database_shared_view 
{
int id;
String pid;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getPid() {
	return pid;
}
public void setPid(String pid) {
	this.pid = pid;
}
public String getTo() {
	return to;
}
public void setTo(String to) {
	this.to = to;
}
public String getFrom() {
	return from;
}
public void setFrom(String from) {
	this.from = from;
}
public String getView_status() {
	return view_status;
}
public void setView_status(String view_status) {
	this.view_status = view_status;
}
public database_to_owner_view getParent_data() {
	return parent_data;
}
public void setParent_data(database_to_owner_view parent_data) {
	this.parent_data = parent_data;
}
String to;
String from;
String view_status;
database_to_owner_view parent_data ;
}
