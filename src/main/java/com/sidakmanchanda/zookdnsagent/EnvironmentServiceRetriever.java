package com.sidakmanchanda.zookdnsagent;

public class EnvironmentServiceRetriever implements ServiceRetriever {
	private static final String instanceKey = "ZOOKDNS_SERVICE_INSTANCE";
	private static final String serviceKey = "ZOOKDNS_SERVICE_NAME";
	private static final String domainKey = "ZOOKDNS_SERVICE_DOMAIN";
	private static final String weightKey = "ZOOKDNS_SERVICE_WEIGHT";
	private static final String priorityKey = "ZOOKDNS_SERVICE_PRIORITY";
	private static final String portKey = "ZOOKDNS_SERVICE_PORT";
	private static final String targetHostKey = "ZOOKDNS_SERVICE_HOST";
	private static final String targetHostAddressKey = "ZOOKDNS_SERVICE_HOST_ADDRESS";

	@Override
	public String getInstance() {
		return System.getenv(instanceKey);
	}
	
	@Override
	public String getService() {
		return System.getenv(serviceKey);
	}
	
	@Override
	public String getDomain() {
		return System.getenv(domainKey);
	}
	
	@Override
	public int getPort() {
		return Integer.parseInt(System.getenv(portKey));
	}
	
	@Override
	public int getWeight() {
		return Integer.parseInt(System.getenv(weightKey));
	}
	
	@Override
	public int getPriority() {
		return Integer.parseInt(System.getenv(priorityKey));
	}

	@Override
	public String getTargetHost() {
		return System.getenv(targetHostKey);
	}
	
	@Override
	public String getTargetHostAddress() {
		return System.getenv(targetHostAddressKey);
	}
	
	
}
