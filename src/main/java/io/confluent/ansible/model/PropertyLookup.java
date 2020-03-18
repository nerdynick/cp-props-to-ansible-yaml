package io.confluent.ansible.model;

import java.util.Optional;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyLookup {
	private static final Logger LOG = LoggerFactory.getLogger(PropertyLookup.class);
	
	private Optional<String> expression = Optional.empty();
	private Optional<String> property = Optional.empty();
	private Pattern _expressionPattern;
	
	private String keyFormatter;
	private Optional<String> valueFormatter = Optional.empty();
	private boolean drop = false;
	
	public Optional<String> getValueFormatter() {
		return valueFormatter;
	}

	public void setValueFormatter(Optional<String> valueFormatter) {
		this.valueFormatter = valueFormatter;
	}

	public Optional<String> getExpression() {
		return expression;
	}

	public void setExpression(Optional<String> expression) {
		this.expression = expression;
	}

	public Optional<String> getProperty() {
		return property;
	}

	public void setProperty(Optional<String> property) {
		this.property = property;
	}

	public String getKeyFormatter() {
		return keyFormatter;
	}

	public void setKeyFormatter(String keyFormatter) {
		this.keyFormatter = keyFormatter;
	}
	
	public boolean isDrop() {
		return drop;
	}

	public void setDrop(boolean drop) {
		this.drop = drop;
	}

	public Pattern getExpressionPattern() {
		if(_expressionPattern == null) {
			if(this.expression.isPresent()) {
				_expressionPattern = Pattern.compile(this.expression.get());
			}
		}
		return _expressionPattern;
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		b
			.append("Lookup[")
			.append("Exp=").append(this.expression)
			.append(" Prop=").append(this.property)
			.append(" Key=").append(this.keyFormatter)
			.append(" Val=").append(this.valueFormatter)
			.append(']');
		
		return b.toString();
	}
}
