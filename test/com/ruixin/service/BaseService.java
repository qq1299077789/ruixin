package com.ruixin.service;

import java.util.List;

import com.ruixin.annotation.Autowired;
import com.ruixin.annotation.Service;
import com.ruixin.annotation.Transaction;
import com.ruixin.dao.BaseDAO;
import com.ruixin.page.PageHelper;
import com.ruixin.po.User;
import com.ruixin.util.LoggerUtils;

@Service
@Transaction(readOnly=true)
public class BaseService{

	@Autowired
	private BaseDAO baseDAO;

	
	@Transaction(readOnly=false)
	public int insert(User user){
		return baseDAO.insert(user);
	}
	
	public List<User> findAll(){
		PageHelper.limit(10);
		List<User> findAll = baseDAO.findAll();
		LoggerUtils.debug("PageHelper测试:"+PageHelper.getPageCount());
		return findAll;
		
	}

	public void update(User user) {
		baseDAO.update(user);
	}
	
	public User findUserById(int id) {
		return baseDAO.findUserById(id);
	}
	
	public User findUserById1(int id) {
		return baseDAO.findUserById1(id);
	}
	
	@Transaction(readOnly=false)
	public void delete(int id){
		baseDAO.delete(id);
	}
	
}
