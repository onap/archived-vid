package org.onap.sdc.ci.tests.devObjects;


import java.io.Serializable;
import java.util.List;

public class CategoryDataDefinition extends ToscaDataDefinition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2882352060242714427L;

	private String name;
	private String normalizedName;
	private String uniqueId;
	private List<String> icons;

	public CategoryDataDefinition() {

	}

	public CategoryDataDefinition(CategoryDataDefinition c) {
		this.name = c.name;
		this.normalizedName = c.normalizedName;
		this.uniqueId = c.uniqueId;
		this.icons = c.icons;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNormalizedName() {
		return normalizedName;
	}

	public void setNormalizedName(String normalizedName) {
		this.normalizedName = normalizedName;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public List<String> getIcons() {
		return icons;
	}

	public void setIcons(List<String> icons) {
		this.icons = icons;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((normalizedName == null) ? 0 : normalizedName.hashCode());
		result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
		result = prime * result + ((icons == null) ? 0 : icons.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryDataDefinition other = (CategoryDataDefinition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (normalizedName == null) {
			if (other.normalizedName != null)
				return false;
		} else if (!normalizedName.equals(other.normalizedName))
			return false;
		if (uniqueId == null) {
			if (other.uniqueId != null)
				return false;
		} else if (!uniqueId.equals(other.uniqueId))
			return false;
		if (icons == null) {
			if (other.icons != null)
				return false;
		} else if (!icons.equals(other.icons))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CategoryDataDefinition [name=" + name + ", normalizedName=" + normalizedName + ", uniqueId=" + uniqueId
				+ ", icons=" + icons + "]";
	}

}
