package com.ruixin.simpleDB;
/**
 * @Author:ruixin
 * @Date: 2018年12月13日 上午8:47:37
 * @Description:SQL拼接类
 */
public class SQL {

	private StringBuffer sql=new StringBuffer();
	
	public SQL select(String select){
		sql.append("SELECT ").append(select);
		return this;
	}
	
	public SQL from(String table){
		sql.append(" FROM ").append(table);
		return this;
	}
	
	public SQL where(String where){
		sql.append(" WHERE ").append(where);
		return this;
	}
	
	public SQL and(String and){
		sql.append(" AND ").append(and);
		return this;
	}
	
	public SQL orderBy(String orderBy){
		sql.append(" ORDER BY ").append(orderBy);
		return this;
	}
	
	public SQL asc(){
		sql.append(" ASC ");
		return this;
	}
	
	public SQL desc(){
		sql.append(" DESC ");
		return this;
	}
	
	public SQL limit(int to,int from){
		sql.append(" LIMIT ").append(to).append(",").append(from);
		return this;
	}
	
	public SQL groupBy(String groupBy){
		sql.append(" GROUP BY ").append(groupBy);
		return this;
	}
	
	public SQL delete(String delete){
		sql.append("DELETE ").append(delete);
		return this;
	}
	
	public SQL delete(){
		sql.append("DELETE ");
		return this;
	}
	
	public SQL update(String update){
		sql.append("UPDATE ").append(update);
		return this;
	}
	
	public SQL set(String set){
		sql.append(" SET ").append(set);
		return this;
	}
	
	public SQL insertInto(String insert){
		sql.append(" INSERT INTO ").append(insert);
		return this;
	}
	
	public SQL value(String value){
		sql.append(" VALUE(").append(value).append(") ");
		return this;
	}
	
	public SQL values(String values){
		sql.append(" VALUES(").append(values).append(") ");
		return this;
	}
	
	public SQL leftJoin(String table){
		sql.append(" LEFT JOIN ").append(table);
		return this;
	}
	
	public SQL innerJoin(String table){
		sql.append(" INNER JOIN ").append(table);
		return this;
	}
	
	public SQL rightJoin(String table){
		sql.append(" RIGHT JOIN ").append(table);
		return this;
	}
	
	public SQL on(String on){
		sql.append(" ON ").append(on);
		return this;
	}
	
	public SQL as(String as){
		sql.append(" AS ").append(as);
		return this;
	}
	
	/**
	 * @return
	 * @Description:获取拼接的SQL
	 */
	public String toSQL(){
		return sql.toString();
	}
}
