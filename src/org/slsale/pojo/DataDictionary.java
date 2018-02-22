package org.slsale.pojo;

/**
 * 数据字典
 * 用途：证件类型
 * @author anan
 *
 */
public class DataDictionary {
	private Integer id;
	private String typeCode;
	private String typeName;
	private Integer valueId;
	private String valueName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getValueId() {
		return valueId;
	}
	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}
	public String getValueName() {
		return valueName;
	}
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	@Override
	public String toString() {
		return "DataDictionary [id=" + id + ", typeCode=" + typeCode
				+ ", typeName=" + typeName + ", valueId=" + valueId
				+ ", valueName=" + valueName + "]";
	}
	
	
	
	
}
