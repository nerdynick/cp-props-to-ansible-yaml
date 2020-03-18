package io.confluent.ansible.parsing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;

import org.apache.commons.configuration2.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.confluent.ansible.model.MappingDef;
import io.confluent.ansible.model.PropertyLookup;

public class Parser {
	private static final Logger LOG = LoggerFactory.getLogger(Parser.class);
	
	final MappingDef mappingDef;
	final Map<String, Object> result = new HashMap<>();
	
	
	public Parser(final MappingDefs mappingDef) throws JsonParseException, JsonMappingException, IOException {
		this(mappingDef.getDef());
	}
	public Parser(final MappingDef mappingDef) {
		this.mappingDef = mappingDef;
	}
	
	public void addZookeeperConfiguration(Configuration conf) {
		this.addConfig(conf, mappingDef.getZookeeperDefault(), mappingDef.getZookeeperLookups());
	}
	public void addKafkaBrokerConfiguration(Configuration conf) {
		this.addConfig(conf, mappingDef.getKafkaBrokerDefault(), mappingDef.getKafkaBrokerLookups());
	}
	public void addKafkaConnectConfiguration(Configuration conf) {
		this.addConfig(conf, mappingDef.getKafkaConnectDefault(), mappingDef.getKafkaConnectLookups());
	}
	public void addSchemaRegistryConfiguration(Configuration conf) {
		this.addConfig(conf, mappingDef.getSchemaRegistryDefault(), mappingDef.getSchemaRegistryLookups());
	}
	
	protected void addConfig(Configuration conf, PropertyLookup def, List<PropertyLookup> lookups) {
		Iterator<String> keys = conf.getKeys();
		while(keys.hasNext()) {
			boolean resolved = false;
			final String key = keys.next();
			final String value = conf.getString(key);
			final String combo = String.format("%s=%s", key, value);
			
			LOG.trace("Parsing: {}={}", key, value);
			
			for(PropertyLookup lookup: lookups) {
				LOG.trace("Checking {} with {}", key, lookup);
				Optional<Pair> pair = evalLookup(lookup, combo, key, value);
				if(pair.isPresent()) {
					resolved = true;
					LOG.trace("Parsed to: {}={}", pair.get().key, pair.get().value);
					this.add(pair.get().key, pair.get().value);
				}
			}
			
			if(!resolved) {
				LOG.trace("Checking {} with default {}", key, def);
				Optional<Pair> pair = evalLookup(def, combo, key, value);
				if(pair.isPresent()) {
					LOG.trace("Parsed to: {}={}", pair.get().key, pair.get().value);
					this.add(pair.get().key, pair.get().value);
				}
			}
		}
	}
	
	protected Optional<Pair> evalLookup(PropertyLookup lookup, String combo, String key, String value){
		if(lookup.getExpression().isPresent()) {
			final Matcher m = lookup.getExpressionPattern().matcher(combo);
			if(m.matches()) {
				LOG.trace("Found Match: {}", m);
				final String newKey = m.replaceAll(lookup.getKeyFormatter());
				
				if(lookup.getValueFormatter().isPresent()) {
					return Optional.of(new Pair(newKey, m.replaceAll(lookup.getValueFormatter().get())));
				} else if(!lookup.isDrop()) {
					return Optional.of(new Pair(newKey, value));
				}
			}
		} else if(lookup.getProperty().isPresent()) {
			if(lookup.getProperty().get().equals(key)) {
				LOG.trace("Found Property: {}", key);
				if(!lookup.isDrop()) {
					return Optional.of(new Pair(lookup.getKeyFormatter(), value));
				}
			}
		} else {
			throw new RuntimeException("Invalid Mapping Def. One or 'expression' or 'property' must be defined for a Property Lookup.");
		}
		return Pair.EMPTY_PAIR;
	}
	
	protected void add(String key, String value) {
		LOG.debug("Parsed to: {}={}", key, value);
		StringBuilder tmpKey = new StringBuilder();
		boolean inEscape = false;
		Map<String, Object> currentPos = result;
		for(int i=0; i<key.length(); i++) {
			char c = key.charAt(i);
			if('.' == c) {
				if(inEscape) {
					tmpKey.append(c);
				} else {
					final String _tmpKey = tmpKey.toString();
					tmpKey.setLength(0);
					
					if(i+1 == key.length()) {
						//At end
						currentPos.put(_tmpKey, value);
					} else {
						currentPos = this.getSubOrCreate(currentPos, _tmpKey);
					}
				}
			} else if('[' == c) {
				inEscape = true;
			} else if(']' == c) {
				inEscape = false;
				
				final String _tmpKey = tmpKey.toString();
				tmpKey.setLength(0);
				
				if(i+1 == key.length()) {
					//At end
					currentPos.put(_tmpKey, value);
				} else {
					currentPos = this.getSubOrCreate(currentPos, _tmpKey);
				}
			} else {
				tmpKey.append(c);
			}
		}
		
		final String _tmpKey = tmpKey.toString();
		tmpKey.setLength(0);
		currentPos.put(_tmpKey, value);
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String, Object> getSubOrCreate(Map<String, Object> currentPos, String key){
		return (Map<String, Object>) currentPos.computeIfAbsent(key, Parser::createMap);
	}
	
	protected static Map<String, Object> createMap(String key){
		return new HashMap<String, Object>();
	}
}
