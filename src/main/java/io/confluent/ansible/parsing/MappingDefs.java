package io.confluent.ansible.parsing;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.base.Preconditions;

import io.confluent.ansible.model.MappingDef;

public enum MappingDefs {
	CP_Ansible_5_4_0("/mapping_cp-ansible_5.4.0.yaml");
	
	protected static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	static {
		mapper
			.findAndRegisterModules()
			.registerModule(new Jdk8Module())
			.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}
	
	final private String yaml;
	private MappingDefs(String yaml) {
		this.yaml = yaml;
	}
	
	public MappingDef getDef() throws JsonParseException, JsonMappingException, IOException {
		final InputStream yaml = MappingDefs.class.getResourceAsStream(this.yaml);
		return MappingDefs.mapper.readValue(Preconditions.checkNotNull(yaml, "Failed to read yaml"), MappingDef.class);
	}
}