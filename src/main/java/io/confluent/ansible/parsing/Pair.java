package io.confluent.ansible.parsing;

import java.util.Optional;

public class Pair {
	public static Optional<Pair> EMPTY_PAIR = Optional.empty();
	
	final public String key;
	final public String value;
	
	public Pair(String key, String value) {
		this.key = key;
		this.value = value;
	}
}