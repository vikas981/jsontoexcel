package com.vikash.jsontoexcel;

public class ServerBean {
	
	private String serverName;
	private String  [] path;
	private String  [] size;
	public ServerBean(String serverName, String [] path, String [] size) {
		super();
		this.serverName = serverName;
		this.path = path;
		this.size = size;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String[] getPath() {
		return path;
	}
	public void setPath(String[] path) {
		this.path = path;
	}
	public String[] getSize() {
		return size;
	}
	public void setSize(String[] size) {
		this.size = size;
	}
	
	
	
	

}
