package com.sidakmanchanda.zookdnsagent;

public interface ServiceSerializer {

	byte[] serializePTRRecord(ServiceRetriever service);

	byte[] serializeARecord(ServiceRetriever service);

	byte[] serializeSRVRecord(ServiceRetriever service);

}
