package com.sidakmanchanda.zookdnsagent;

import org.json.JSONObject;

public class JsonSerializer implements ServiceSerializer {
	
	@Override
	public byte[] serializePTRRecord(ServiceRetriever service) {
		String ptrName = service.getService() + "." + service.getDomain() + ".";
		String serviceName = service.getInstance() + "." + service.getService() + "." + service.getDomain() + ".";
		
		JSONObject json = new JSONObject();
		json.put("name", getNameJson(ptrName));
		json.put("hostName", getNameJson(serviceName));
		json.put("type", "PTR");
		json.put("dClass", "IN");
		json.put("ttl", 0);
		return json.toString().getBytes();
	}
	
	@Override
	public byte[] serializeARecord(ServiceRetriever service) {
		JSONObject json = new JSONObject();
		json.put("name",getNameJson(service.getTargetHost()));
		json.put("address", service.getTargetHostAddress());
		json.put("type", "A");
		json.put("dClass", "IN");
		json.put("ttl", 0);
		
		return json.toString().getBytes();	
	}
	
	@Override
	public byte[] serializeSRVRecord(ServiceRetriever service) {
		String serviceName = service.getInstance() + "." + service.getService() + "." + service.getDomain() + ".";
		
		JSONObject json = new JSONObject();
		json.put("type", "SRV");
		json.put("priority", service.getPriority());
		json.put("weight", service.getWeight());
		json.put("port", service.getPort());
		json.put("dClass", "IN");
		json.put("ttl", 0);
		json.put("target", getNameJson(service.getTargetHost()));
		json.put("name",getNameJson(serviceName));
		
		return json.toString().getBytes();
		
	}
	
	private JSONObject getNameJson(String name) {
		JSONObject json = new JSONObject();
		json.put("name", name.split("\\."));
		json.put("stringName", name);
		json.put("fqdn", true);
		return json;
	}

}
