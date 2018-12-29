package com.ruixin.dao;

import java.util.List;

import com.ruixin.annotation.Dao;
import com.ruixin.annotation.Delete;
import com.ruixin.annotation.Insert;
import com.ruixin.annotation.Param;
import com.ruixin.annotation.QueryProvider;
import com.ruixin.annotation.Select;
import com.ruixin.annotation.Update;
import com.ruixin.po.User;
import com.ruixin.provider.UserProvider;
import com.ruixin.simpleDB.QueryType;

@Dao
public interface BaseDAO{

	@Insert(sql="insert into User(name,sex,role) value(#{user.name},#{user.sex},#{user.role})",useGeneratedKeys=true)
	public int insert(@Param("user")User user);
	
	@Select(sql="select * from User", returnType = User.class)
	public List<User> findAll();
	
	@Select(sql="select * from User where id=#{id}", returnType = User.class)
	public User findUserById(@Param("id")int id);

	@Update("update User set name = #{user.name} where id = #{user.id}")
	public void update(@Param("user")User user);
	
	@Delete("delete from User where id=#{id}")
	public void delete(@Param("id") int id);
	
	@QueryProvider(type=UserProvider.class,method="select",queryType=QueryType.SELECT,returnType=User.class)
	public User findUserById1(@Param("id") int id);
}
