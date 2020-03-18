package io.confluent.ansible.parsing;

import java.io.IOException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nerdynick.commons.configuration.utils.FileConfigUtils;

public class ParserTest {
	private static final Logger LOG = LoggerFactory.getLogger(ParserTest.class);
	
	@Test
	public void testZookeeperParse() throws JsonParseException, JsonMappingException, IOException {
		final Parser parser = new Parser(MappingDefs.CP_Ansible_5_4_0);
		
		LOG.debug("Parser: {}", parser.mappingDef.toString());
		
		parser.addZookeeperConfiguration(FileConfigUtils.newFileConfig("zookeeper.properties"));
		
		LOG.debug("Output: {}", parser.result);
	}
}
