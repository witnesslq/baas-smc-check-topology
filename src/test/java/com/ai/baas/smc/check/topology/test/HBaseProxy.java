package com.ai.baas.smc.check.topology.test;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HBaseProxy {
	
	private static Connection connection;
	
	static{
		Configuration configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.property.clientPort", "49181");
		configuration.set("hbase.zookeeper.quorum", "node0,node1,node2");
		configuration.set("hbase.master", "node0");
		try {
			connection = ConnectionFactory.createConnection(configuration);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		return connection;
	}
}
