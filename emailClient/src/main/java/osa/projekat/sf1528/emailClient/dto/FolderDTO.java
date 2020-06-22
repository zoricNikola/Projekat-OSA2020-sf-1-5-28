package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;

import osa.projekat.sf1528.emailClient.model.Folder;

public class FolderDTO implements Serializable {

	private static final long serialVersionUID = 2188634478104553741L;
	
	private Long id;
	
	private String name;
	
	private FolderDTO parent;
	
	private AccountDTO account;
	
	public FolderDTO() {}
	
	public FolderDTO(Long id, String name, FolderDTO parent, AccountDTO account) {
		super();
		this.id = id;
		this.name = name;
		this.parent = parent;
		this.account = account;
	}
	
	public FolderDTO(Folder folder) {
		this(folder.getId(), folder.getName(), 
				(folder.getParent() != null && folder.getParent().getId() != null)?new FolderDTO(folder.getParent()):new FolderDTO(), 
				new AccountDTO(folder.getAccount()));
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

	public FolderDTO getParent() {
		return parent;
	}

	public void setParent(FolderDTO parent) {
		this.parent = parent;
	}

	public AccountDTO getAccount() {
		return account;
	}

	public void setAccount(AccountDTO account) {
		this.account = account;
	}

}
