package com.sidakmanchanda.zookdnsagent;

public interface ServiceRetriever {
	
	public String getInstance();
	public String getService();
	public String getDomain();
	public int getPort();
	public int getWeight();
	public int getPriority();
	public String getTargetHost();
	public String getTargetHostAddress();
}
