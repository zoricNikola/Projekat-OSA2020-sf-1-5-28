package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;

import osa.projekat.sf1528.emailClient.model.Folder;

public class FolderDTO implements Serializable {

	private static final long serialVersionUID = 2188634478104553741L;
	
	private Long id;
	
	private String name;
	
	private int numberOfMessages;
	
	public FolderDTO() {}
	
	public FolderDTO(Long id, String name, int numberOfMessages) {
		super();
		this.id = id;
		this.name = name;
		this.numberOfMessages = numberOfMessages;
	}
	
	public FolderDTO(Folder folder) {
		this(folder.getId(), folder.getName(), folder.getMessages().size());
		
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

	public int getNumberOfMessages() {
		return numberOfMessages;
	}

	public void setNumberOfMessages(int numberOfMessages) {
		this.numberOfMessages = numberOfMessages;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
