package com.ruixin.controller;

import java.util.List;

import com.ruixin.annotation.All;
import com.ruixin.annotation.Args;
import com.ruixin.annotation.Autowired;
import com.ruixin.annotation.GetMapping;
import com.ruixin.annotation.JsonReturn;
import com.ruixin.annotation.Web;
import com.ruixin.config.ModelMap;
import com.ruixin.po.User;
import com.ruixin.service.BaseService;

@Web(preUrl="user")
public class BaseController {

	@Autowired
	private BaseService baseService;
	
	/**
	 * @param name
	 * @return
	 * @Description:测试添加
	 */
	@GetMapping("/insert")
	@JsonReturn
	public User insert(@Args("name")String name,@Args("sex")String sex,@Args("role") int role){
		User user=new User();
		user.setName(name);
		user.setRole(role);
		user.setSex(sex);
		baseService.insert(user);
		return user;
	}
	
	/**
	 * @param name
	 * @return
	 * @Description:测试查找
	 */
	@GetMapping("/findAll")
	@JsonReturn
	public List<User> findAll(){
		return baseService.findAll();
	}
	
	/**
	 * @param name
	 * @return
	 * @Description:测试更新
	 */
	@GetMapping("/update")
	@JsonReturn
	public User update(@Args("name")String name){
		User user=new User();
		user.setId(1);
		user.setName(name);
		baseService.update(user);
		return baseService.findUserById(user.getId());
	}
	
	/**
	 * @param id
	 * @return
	 * @Description:测试删除
	 */
	@JsonReturn
	@GetMapping("/delete")
	public String delete(@Args("id")int id){
		baseService.delete(id);
		return "success";
	}
	
	@JsonReturn
	@GetMapping("/findUserById")
	public User findUserById(@Args("id")int id){
		return baseService.findUserById1(id);
	}
	
	
	@All("/test1")
	public String test(){
		return "test";
	}
	
	@All("/test12")
	public ModelMap test3(ModelMap map){
		map.setView("test");
		return map;
	}
}
