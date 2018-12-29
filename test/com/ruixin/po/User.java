package com.ruixin.po;

import java.io.Serializable;

/**
 * @Author:ruixin
 * @Date: 2018年11月30日 上午11:02:46
 * @Description:测试类
 */
public class User implements Serializable{

    /**
	 * @Description:
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @Description:
	 */
	private int id;
    private String name;
    private String sex;
    private int role;
    
    public int getRole() {
		return role;
	}

	public void setRole(int roleId) {
		this.role = roleId;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", sex=" + sex + ", role=" + role + "]";
	}
    
    
}
