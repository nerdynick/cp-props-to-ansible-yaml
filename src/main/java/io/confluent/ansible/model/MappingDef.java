package io.confluent.ansible.model;

import java.util.List;

public class MappingDef {
	protected PropertyLookup zookeeperDefault;
	protected PropertyLookup kafkaBrokerDefault;
	protected PropertyLookup kafkaConnectDefault;
	protected PropertyLookup schemaRegistryDefault;
	
	protected List<PropertyLookup> zookeeperLookups;
	protected List<PropertyLookup> kafkaBrokerLookups;
	protected List<PropertyLookup> kafkaConnectLookups;
	protected List<PropertyLookup> schemaRegistryLookups;
	
	public List<PropertyLookup> getZookeeperLookups() {
		return zookeeperLookups;
	}
	public void setZookeeperLookups(List<PropertyLookup> zookeeperLookups) {
		this.zookeeperLookups = zookeeperLookups;
	}
	public List<PropertyLookup> getKafkaBrokerLookups() {
		return kafkaBrokerLookups;
	}
	public void setKafkaBrokerLookups(List<PropertyLookup> kafkaBrokerLookups) {
		this.kafkaBrokerLookups = kafkaBrokerLookups;
	}
	public List<PropertyLookup> getKafkaConnectLookups() {
		return kafkaConnectLookups;
	}
	public void setKafkaConnectLookups(List<PropertyLookup> kafkaConnectLookups) {
		this.kafkaConnectLookups = kafkaConnectLookups;
	}
	public List<PropertyLookup> getSchemaRegistryLookups() {
		return schemaRegistryLookups;
	}
	public void setSchemaRegistryLookups(List<PropertyLookup> schemaRegistryLookups) {
		this.schemaRegistryLookups = schemaRegistryLookups;
	}
	public PropertyLookup getZookeeperDefault() {
		return zookeeperDefault;
	}
	public void setZookeeperDefault(PropertyLookup zookeeperDefault) {
		this.zookeeperDefault = zookeeperDefault;
	}
	public PropertyLookup getKafkaBrokerDefault() {
		return kafkaBrokerDefault;
	}
	public void setKafkaBrokerDefault(PropertyLookup kafkaBrokerDefault) {
		this.kafkaBrokerDefault = kafkaBrokerDefault;
	}
	public PropertyLookup getKafkaConnectDefault() {
		return kafkaConnectDefault;
	}
	public void setKafkaConnectDefault(PropertyLookup kafkaConnectDefault) {
		this.kafkaConnectDefault = kafkaConnectDefault;
	}
	public PropertyLookup getSchemaRegistryDefault() {
		return schemaRegistryDefault;
	}
	public void setSchemaRegistryDefault(PropertyLookup schemaRegistryDefault) {
		this.schemaRegistryDefault = schemaRegistryDefault;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b
			.append(this.getClass().getSimpleName())
			.append('[')
			.append("Zookeeper: Default=(").append(this.zookeeperDefault).append(") Lookups=(").append(this.zookeeperLookups).append(")")
			.append("Broker: Default=(").append(this.kafkaBrokerDefault).append(") Lookups=(").append(this.kafkaBrokerLookups).append(")")
			.append("Connect: Default=(").append(this.kafkaConnectDefault).append(") Lookups=(").append(this.kafkaConnectLookups).append(")")
			.append("Schema Registry: Default=(").append(this.schemaRegistryDefault).append(") Lookups=(").append(this.schemaRegistryLookups).append(")")
			.append(']');
		
		return b.toString();
	}
}
