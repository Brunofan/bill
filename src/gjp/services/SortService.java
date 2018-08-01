package gjp.services;

import java.util.List;

import gjp.dao.SortDao;
import gjp.domain.Sort;

/**
 * 
 * @author Ivan 分类功能的业务层
 * 
 * 分类的控制层controller调用service功能
 * service调用dao层功能
 */
public class SortService {
	//创建dao层，sortDao类的对象
	private  SortDao sortDao = new SortDao();
	
	/*
	 * 定义方法，调用dao层SortDao.querySortNameByParent()方法查询所有的分类数据  
	 * 返回List集合
	 */
	public List<Object> querySortNameByParent(String parent){
		return sortDao.querySortNameByName(parent);
	}
	
	/*
	 * 定义方法，调用dao层SortDao.querySortNameAll()方法查询所有的分类数据  
	 * 返回List集合
	 */
	public List<Object> querySortNameAll(){
		return sortDao.querySortNameAll();
	}
	
	/*
	 * 定义方法，调用dao层SortDao.deleteSort()方法，实现编辑分类
	 * 传递sort对象
	 * 是contorller传递的
	 */
	public void deleteSort(Sort sort) {
		sortDao.deleteSort(sort);
	}
	
	
	/*
	 * 定义方法，调用dao层SortDao.editSort()方法，实现编辑分类
	 * 传递sort对象
	 * 是contorller传递的
	 */
	public void editSort(Sort sort) {
		sortDao.editSort(sort);
	}
	
	
	/*
	 * 定义方法，调用dao层SortDao.querySortAll()方法，获取所有的分类数据 
	 * 返回的是List集合，存储Sort对象
	 */
	public List<Sort> querySortAll(){
		return sortDao.querySortAll();
	}
	
	
	/*
	 * 定义方法，调用dao层SortDao.addSort()添加分类
	 * 传递Sort对象
	 * 本层的Sort对象是contorller传递的
	 */
	public void addSort(Sort sort) {
		sortDao.addSort(sort);
	}
}
