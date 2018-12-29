package com.ruixin.provider;

import com.ruixin.simpleDB.SQL;

public class UserProvider {

	public String select(int id){
		return new SQL().select("*").from("User").where("id=#{id}").toSQL();
	}
	
	public String findRoleByUserId(int id){
		// select * from Role as r left join user as u on u.id=r.is where u.id=#{id} 
		return new SQL().select("*").from("Role").as("r").rightJoin("User").as("u").on("u.id=r.id")
				.where("u.id=#{id}").toSQL();
	}
	
}
