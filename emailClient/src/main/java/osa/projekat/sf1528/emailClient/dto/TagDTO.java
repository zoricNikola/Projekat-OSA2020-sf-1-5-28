package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;

import osa.projekat.sf1528.emailClient.model.Tag;

public class TagDTO implements Serializable {

	private static final long serialVersionUID = -326867031145397065L;
	
	private Long id;
	
	private String name;
	
	public TagDTO() {}

	public TagDTO(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public TagDTO(Tag tag) {
		this(tag.getId(), tag.getName());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
