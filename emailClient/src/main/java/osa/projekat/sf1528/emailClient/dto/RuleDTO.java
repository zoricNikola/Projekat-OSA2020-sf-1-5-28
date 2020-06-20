package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;
import java.lang.management.OperatingSystemMXBean;

import osa.projekat.sf1528.emailClient.model.Rule;
import osa.projekat.sf1528.emailClient.model.Rule.Condition;
import osa.projekat.sf1528.emailClient.model.Rule.Operation;;

public class RuleDTO implements Serializable {

	private static final long serialVersionUID = -7599570197341391702L;
	
	private Long id;
	private String value;
	private Rule.Condition condition;
	private Rule.Operation operation;
	private FolderDTO destination;
	
	public RuleDTO() {}
	
	public RuleDTO(Long id, String value, Condition condition, Operation operation, FolderDTO destination) {
		super();
		this.id = id;
		this.value = value;
		this.condition = condition;
		this.operation = operation;
		this.destination = destination;
	}
	
	public RuleDTO(Rule rule) {
		this(rule.getId(), rule.getValue(), rule.getCondition(), rule.getOperation(), new FolderDTO(rule.getDestination()));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Rule.Condition getCondition() {
		return condition;
	}

	public void setCondition(Rule.Condition condition) {
		this.condition = condition;
	}

	public Rule.Operation getOperation() {
		return operation;
	}

	public void setOperation(Rule.Operation operation) {
		this.operation = operation;
	}

	public FolderDTO getDestination() {
		return destination;
	}

	public void setDestination(FolderDTO destination) {
		this.destination = destination;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
