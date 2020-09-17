package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;

import osa.projekat.sf1528.emailClient.model.Contact;
import osa.projekat.sf1528.emailClient.util.Base64;
import osa.projekat.sf1528.emailClient.util.FilesUtil;

public class ContactDTO implements Serializable {

	private static final long serialVersionUID = 6449139396725199968L;

	private Long id;
	
	private String firstName;
	
	private String lastName;
	
	private String displayName;
	
	private String email;
	
	private String note;
	
	private String encodedPhotoData;
	
	public ContactDTO() {}

	public ContactDTO(Long id, String firstName, String lastName, String displayName, String email, String note,
			String encodedPhotoData) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.displayName = displayName;
		this.email = email;
		this.note = note;
		this.encodedPhotoData = encodedPhotoData;
	}
	
	public ContactDTO(Contact contact) {
		this(contact.getId(), contact.getFirstName(), contact.getLastName(), contact.getDisplayName(), contact.getEmail(), 
				contact.getNote(), contact.getPhotoPath());
		
		if (contact.getPhotoPath() != null && !contact.getPhotoPath().isEmpty())
			this.encodedPhotoData = Base64.encodeToString(FilesUtil.readBytes(contact.getPhotoPath()));
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEncodedPhotoData() {
		return encodedPhotoData;
	}

	public void setEncodedPhotoData(String encodedPhotoData) {
		this.encodedPhotoData = encodedPhotoData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
