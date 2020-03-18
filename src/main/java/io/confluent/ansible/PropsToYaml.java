package io.confluent.ansible;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.commons.configuration2.YAMLConfiguration;

import com.google.common.base.Strings;
import com.nerdynick.commons.configuration.utils.FileConfigUtils;

import io.confluent.ansible.model.MappingDef;
import io.confluent.ansible.parsing.MappingDefs;
import io.confluent.ansible.parsing.Parser;

public class PropsToYaml {
	protected static final Options options = new Options();
	static {
		options.addOption("b", "broker", true, "Properties File for Kafka Brokers or Confluent Server");
		options.addOption("z", "zookeeper", true, "Properties File for Zookeeper");
		options.addOption("c", "connect", true, "Properties File for Kafka Connect");
		options.addOption("s", "schema-reg", true, "Properties File for Schema Registry");
		options.addOption("a", "ansible", true, "Ansible Mapping Def to use. Available Options: "+ Arrays.toString(MappingDefs.values()) +" (Defaults to Ansible_5_4_0) ");
		options.addOption("h", "help", false, "Print Help");
	}
	
	final Configuration zookeeper;
	final Configuration broker;
	final Configuration connect;
	final Configuration schemaReg;
	
	final MappingDef mappingDef;
	
	public PropsToYaml(final Configuration zookeeper, final Configuration broker, final Configuration connect, final Configuration sr, final MappingDefs ansibleDef) throws IOException{
		this.zookeeper = zookeeper;
		this.broker = broker;
		this.connect = connect;
		this.schemaReg = sr;
		
		try {
			this.mappingDef = ansibleDef.getDef();
		} catch (Exception e) {
			throw new IOException("Failed to handle Mapping Def", e);
		}
	}
	
	public YAMLConfiguration generateYamlConfig() {
		Parser parser = new Parser(this.mappingDef);
		if(this.zookeeper != null) {
			parser.addZookeeperConfiguration(zookeeper);
		}
		if(this.broker != null) {
			parser.addKafkaBrokerConfiguration(broker);
		}
		if(this.connect != null) {
			parser.addKafkaConnectConfiguration(connect);
		}
		if(this.schemaReg != null) {
			parser.addSchemaRegistryConfiguration(schemaReg);
		}
		
		YAMLConfiguration config = new YAMLConfiguration();
		
		//TODO: Do Logic parsing
		
		return config;
	}
	
	protected static void printHelp() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(200);
		formatter.printHelp(PropsToYaml.class.getSimpleName(), options);
	}
	
	public static Configuration getConfigOrDefault(String config) throws IOException {
		return !Strings.isNullOrEmpty(config) ? FileConfigUtils.newFileConfig(config) : new MapConfiguration(Collections.emptyMap());
	}
	
	public static void main(String[] args) throws Exception {
		final CommandLineParser parser = new DefaultParser();
		try {
			// parse the command line arguments
			final CommandLine line = parser.parse(options, args);
			if (line.hasOption('h')) {
				printHelp();
				System.exit(0);
			}
			
			final Configuration zookeeper = getConfigOrDefault(line.getOptionValue('z'));
			final Configuration broker = getConfigOrDefault(line.getOptionValue('b'));
			final Configuration connect = getConfigOrDefault(line.getOptionValue('c'));
			final Configuration schemaReg = getConfigOrDefault(line.getOptionValue('s'));
		} catch (ParseException exp) {
			// oops, something went wrong
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			printHelp();
			System.exit(1);
		}
	}

}
