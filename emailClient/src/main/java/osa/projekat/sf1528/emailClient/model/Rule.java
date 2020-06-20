package osa.projekat.sf1528.emailClient.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rules")
public class Rule implements Serializable{
	
	private static final long serialVersionUID = -1535541801150292198L;
	public static enum Condition {TO, FROM, CC, SUBJECT}
	public static enum Operation {MOVE, COPY, DELETE}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rule_id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "value", unique = false, nullable = false)
	private String value;
	
	@Column(name = "condition", unique = false, nullable = false)
	private Condition condition;
	
	@Column(name = "operation", unique = false, nullable = false)
	private Operation operation;
	
	@ManyToOne
	@JoinColumn(name = "folder_id", referencedColumnName = "folder_id", nullable = false)
	private Folder destination;
	
	public Rule() {}
	
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
	
	public Condition getCondition() {
		return condition;
	}
	
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
	
	public Operation getOperation() {
		return operation;
	}
	
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	
	public Folder getDestination() {
		return destination;
	}
	
	public void setDestination(Folder destination) {
		this.destination = destination;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
