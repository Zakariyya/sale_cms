package org.slsale.common;

import java.util.ArrayList;
import java.util.List;

public class PageSupport {

	private Integer totalCount = 0; //总记录数/总条数/总数据量
	private Integer pageCount; //总页数
	private Integer pageSize = 2;//每页显示多少条
	private Integer page = 1;//当前页
	private Integer num = 3;//当前页之前和之后显示页个数
	private List items = new ArrayList();//当前页列表（数据列表）
	
	/**
	 * 获取总记录数
	 * @return
	 */
	public Integer getTotalCount() {
		return totalCount;
	}
	/**
	 * 计算总页数
	 * @param totalCount
	 */
	public void setTotalCount(Integer totalCount) {
		if(totalCount > 0){
			this.totalCount = totalCount;
			if(this.totalCount % this.pageSize == 0){
				this.pageCount = this.totalCount / this.pageSize;
			}else if(this.totalCount % this.pageSize > 0){
				this.pageCount = this.totalCount / this.pageSize + 1;
			}else{
				this.pageCount = 0;
			}
		}
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	/**
	 * 获取当前页之前、之后显示的当前个数
	 * @return
	 */
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * 设置当前页的集合
	 * @return
	 */
	public List getItems() {
		return items;
	}
	public void setItems(List items) {
		this.items = items;
	}
	
	/**
	 * 获取前一页
	 * @return
	 */
	public Integer getPrev(){
		return page-1;
	}

	/**
	 * 获取下一页
	 * @return
	 */
	public Integer getNext(){
		return page+1;
	}

	/**
	 * 获取最后一页
	 * @return
	 */
	public Integer getLast(){
		return pageCount;
	}

	/**
	 * 判断是否有前一页
	 * @return
	 */
	public boolean getIsPrev(){
		if(page > 1)
			return true;
		return false;
	}
	
	/**
	 * 判断是否有后一页
	 * @return
	 */
	public boolean getIsNext(){
		if(page != null && page < pageCount)
			return true;
		return false;
	}

	/**
	 * 当前页的前num条页，
	 * eg:
	 * 		 - num ：3
	 * 		 - page(当前页)：6	 > 首..345 6 789..末
	 * 		 - 那么：
	 *  		 - _endCount(结束页)就是：9
	 *  
	 *********************************
	 *********************************
	 **                             **
	 ** 该方法作用，将页码封装到一个list集合中    **
	 **                             **
	 *********************************
	 *********************************
	 * @return
	 */
	public List<Integer> getNextPages(){
		List<Integer> list = new ArrayList<Integer>();
		Integer _endCount = num;//结束页()

		/**
		 * eg: 
		 * 		pageCount = 20, 
		 * 		page = 6, 
		 * 		num = 3 
		 * .>>>> 首...345 6 789...尾页
		 */
		if(num < pageCount && (num+page) < pageCount){ 
			_endCount = page + num;
		}else{
			/**
			 * eg: 
			 * 		pageCount = 20, 
			 * 		page = 18, 
			 * 		num = 3 
			 * .>>>> 首...15 16 17, (18), 19 20
			 * _endCount就变成pageCount，既 "尾页"
			 */
			_endCount = pageCount;
		}
		
		//把页码放入list对象中
		for(Integer i = page+1;i <= _endCount;i++){
			list.add(i);
		}
		return list;
	}
	
	/**
	 * 当前页的后num条页，
	 * eg:
	 * 		 - num ：3
	 * 		 - 当前页：6	 > 首..345 6 789..末
	 * 		 - 那么：
	 *  		 - 开始页就是：7
	 *  
	 *********************************
	 ** 该方法作用，将页码封装到一个list集合中    **
	 *********************************
	 *
	 * @return
	 */
	public List<Integer> getPrevPages(){
		List<Integer> list = new ArrayList<Integer>();
		Integer _frontStart = 1;//开始页()
		if(page > num){
			_frontStart = page - num;
		}
		for(Integer i = _frontStart;i < page;i++){
			list.add(i);
		}
		return list;
	}

}








