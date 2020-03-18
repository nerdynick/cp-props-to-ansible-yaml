package io.confluent.ansible;

import java.io.IOException;

import org.apache.commons.configuration2.YAMLConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.ansible.parsing.MappingDefs;

class PropsToYamlTest {
	private static final Logger LOG = LoggerFactory.getLogger(PropsToYamlTest.class);

	@Test
	void testZookeeper() throws IOException {
		final PropsToYaml parser = new PropsToYaml(
			PropsToYaml.getConfigOrDefault("zookeeper.properties"), 
			PropsToYaml.getConfigOrDefault(""),
			PropsToYaml.getConfigOrDefault(""),
			PropsToYaml.getConfigOrDefault(""),
			MappingDefs.CP_Ansible_5_4_0);
		
		final YAMLConfiguration config = parser.generateYamlConfig();
		
	}

}
