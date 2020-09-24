package osa.projekat.sf1528.emailClient.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterDTO implements Serializable {

	private static final long serialVersionUID = 3499048387510708996L;
	
	private String searchText;
	
	private List<TagDTO> filteringTags = new ArrayList<TagDTO>();
	
	public FilterDTO () {}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public List<TagDTO> getFilteringTags() {
		return filteringTags;
	}

	public void setFilteringTags(List<TagDTO> filteringTags) {
		this.filteringTags = filteringTags;
	}

}
