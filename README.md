# ZookDNSAgent

ZookDNSAgent registers a service into Zookeeper by creating DNS records as epemeral znodes. These records will be queried by clients through [ZookDNS][zookdns].

## Usage

Simply set the following env variables and run the agent on your service.
```
// Used to create SRV records. See [RFC-6763]
"ZOOKDNS_SERVICE_INSTANCE"  
"ZOOKDNS_SERVICE_NAME"
"ZOOKDNS_SERVICE_DOMAIN"
"ZOOKDNS_SERVICE_WEIGHT"
"ZOOKDNS_SERVICE_PRIORITY"
"ZOOKDNS_SERVICE_PORT"

// Host name and address of service
"ZOOKDNS_SERVICE_HOST"
"ZOOKDNS_SERVICE_HOST_ADDRESS"
```

ZookDNSAgent will create an PTR, SRV and A record with this information.

Additionaly override the default Zookeeper address by setting the `ZOOKDNS_ZOOKEEPER_ADDRESS` variable.

In the case of a disconnect ZookDNSAgent will periodically attempt to reconnect to ZooKeeper.

See [ZookDNS][zookdns]

[zookdns]:
https://github.com/SidakM/ZookDNS
[DNS-SD]:
https://www.ietf.org/rfc/rfc6763.txt

