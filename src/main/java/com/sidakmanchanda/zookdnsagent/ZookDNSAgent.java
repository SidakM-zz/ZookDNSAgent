package com.sidakmanchanda.zookdnsagent;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ZookDNSAgent {
	private static final Logger log = LogManager.getLogger(ZookDNSAgent.class);
	private static final String ZOOKEEPER_ADDRESS_KEY = "ZOOKDNS_ZOOKEEPER_ADDRESS";
	static final int WAIT_BUFFER = 60 * 1000;

	public static void main(String[] args) {
		// Register service
		Registrar rg = new Registrar();
		EnvironmentServiceRetriever ers = new EnvironmentServiceRetriever();
		String zookeeperAddress = System.getenv(ZOOKEEPER_ADDRESS_KEY);
		if (zookeeperAddress == null) {
			zookeeperAddress = "localhost";
		}
		
		try {
			registerService(rg, ers, zookeeperAddress);
		} catch(InterruptedException e) {
			log.error("sleep interrupted", e);
		}
	}

	private static void registerService(Registrar rg, ServiceRetriever ers, String zookeeperAddress) throws InterruptedException {
		while(true) {
			if (!rg.isConnected()) {
				try {
					log.info("establishing connection to zookeeper");
					rg.connect(zookeeperAddress);
					rg.registerService(ers);
				} catch (IOException e) {
					log.error(e);
				}
			}
			Thread.sleep(WAIT_BUFFER);
		}
	}
}
