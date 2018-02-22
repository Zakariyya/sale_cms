package org.slsale.pojo;


/**
 * Base
 * @author bdqn_hl
 * @date 2014-2-21
 */
public class Base {
	private Integer id;
	private Integer startNum;//分页的起始行
	private Integer pageSize;//页面容量，每页显示几行
	

	public Integer getStartNum() {
		return startNum;
	}

	public void setStartNum(Integer startNum) {
		this.startNum = startNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
}
