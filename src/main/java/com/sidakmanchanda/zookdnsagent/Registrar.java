package com.sidakmanchanda.zookdnsagent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

public class Registrar {
    private ZooKeeper zk;
    private ZKConnection zkConnection;
    private ServiceSerializer serializer;
    
	private static String RECORDS_PATH = "/records";
	private static String VERSION_PREFIX = "version";
	private static String RECORD_PREFIX = "record";
	private static String PATH_DELIMETER = "/";

	private static final ArrayList<ACL> OPEN_ACL = ZooDefs.Ids.OPEN_ACL_UNSAFE;
    
	public Registrar() {
		this.zkConnection = new ZKConnection();
		this.serializer = new JsonSerializer();
	}
	
	/**
	 * Checks whether the instance is connected from zookeeper
	 * @return boolean: connected to zookeeper
	 */
	public boolean isConnected() {
		if (zk == null) return false;
		ZooKeeper.States state = zk.getState();
		return state == ZooKeeper.States.CONNECTED || state == ZooKeeper.States.CONNECTING;
	}
	
	public void connect(String address) throws IOException {
		try {
			zk = zkConnection.connect(address);
		} catch (IOException | InterruptedException e) {
			throw new IOException("could not connect to zookeeper", e);
		}
	}
	
	public void registerService(ServiceRetriever service) throws IOException {
		try {
			// register a service record
			String srvHost = service.getInstance() + "." + service.getService() + "." + service.getDomain() + ".";
			String srvPath = getRecordPath(srvHost, "SRV");
			createEphemeralSequentialNode(srvPath + PATH_DELIMETER + RECORD_PREFIX, serializer.serializeSRVRecord(service));
			
			// register a ptr record to the service record
			String ptrHost = service.getService() + "." + service.getDomain() + ".";
			String ptrPath = getRecordPath(ptrHost, "PTR");
			createEphemeralSequentialNode(ptrPath + PATH_DELIMETER + RECORD_PREFIX, serializer.serializePTRRecord(service));
			
			// if the target host address is defined register an A record
			if (service.getTargetHostAddress() != null) {
				String aHost = service.getTargetHost();
				String aPath = getRecordPath(aHost, "A");
				createEphemeralSequentialNode(aPath + PATH_DELIMETER + RECORD_PREFIX, serializer.serializeARecord(service));
			}
		} catch(KeeperException | InterruptedException e) {
			throw new IOException(e);
		}
	}
	
	private String getRecordPath(String host, String recordType) throws KeeperException, InterruptedException {
		String hostPath = getHostPath(host);
		if (!pathExists(hostPath)){
			createPersistentNode(hostPath, null);
		}
		
		// create new sequential node at versioned path
		String versionedPath = hostPath + PATH_DELIMETER + VERSION_PREFIX;
		createPersistentSequentialNode(versionedPath, null);
		
		// update versioned path with latest version 
		versionedPath = versionedPath  + String.format(Locale.ENGLISH, "%010d", getSequentialNodeVersion(hostPath));
		
		// create a record type path
		String recordTypePath = versionedPath + PATH_DELIMETER + recordType;
		if (!pathExists(recordTypePath)) {
			createPersistentNode(recordTypePath, null);
		}
		
		return recordTypePath;
	}
	
	private int getSequentialNodeVersion(String parentPath) throws KeeperException, InterruptedException {
		Stat stat = getNodeStats(parentPath);
		return stat.getCversion() - 1;
	}
	
	private Stat getNodeStats(String path) throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, true);
		return stat;
	}
	
	private String getHostPath(String recordName) {
		return RECORDS_PATH + PATH_DELIMETER + recordName;
	}
	
	private boolean pathExists(String path) throws KeeperException, InterruptedException {
		Stat stat = zk.exists(path, true);
		if (stat != null) return true;
		return false;
	}
	
	private void createPersistentSequentialNode(String versionedPath, byte[] data) throws KeeperException, InterruptedException {
		zk.create(versionedPath, data, OPEN_ACL, CreateMode.PERSISTENT_SEQUENTIAL);
	}

	private void createEphemeralSequentialNode(String path, byte[] data) throws KeeperException, InterruptedException {
		zk.create(path, data, OPEN_ACL, CreateMode.EPHEMERAL_SEQUENTIAL);
	}
	
	private void createPersistentNode(String path, byte[] data) throws KeeperException, InterruptedException {
		zk.create(path, data, OPEN_ACL, CreateMode.PERSISTENT);
	}
}
