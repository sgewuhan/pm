package com.sg.business.model;


/**
 * 角色和用户的关系<p/>
 * 将角色赋予某个用户，用户和角色为多对多的关系
 * @author jinxitao
 *
 */
public class RoleAssignment extends AbstractRoleAssignment{

	
	@Override
	public String getTypeName() {
		return "角色指派";
	}
}
