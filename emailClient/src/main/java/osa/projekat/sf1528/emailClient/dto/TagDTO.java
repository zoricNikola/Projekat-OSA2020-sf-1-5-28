package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;

import osa.projekat.sf1528.emailClient.model.Tag;

public class TagDTO implements Serializable {

	private static final long serialVersionUID = -326867031145397065L;
	
	private Long id;
	
	private String name;
	
	private int color;
	
	public TagDTO() {}

	public TagDTO(Long id, String name, int color) {
		super();
		this.id = id;
		this.name = name;
		this.color = color;
	}
	
	public TagDTO(Tag tag) {
		this(tag.getId(), tag.getName(), tag.getColor());
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

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
