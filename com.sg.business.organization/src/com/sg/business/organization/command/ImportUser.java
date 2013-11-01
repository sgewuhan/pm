package com.sg.business.organization.command;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.sg.sqldb.utility.SQLResult;
import com.sg.sqldb.utility.SQLRow;
import com.sg.sqldb.utility.SQLUtil;

public class ImportUser implements Runnable {

	@Override
	public void run() {
		//检查角色指派中的用户
		
		
//		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
//				IModelConstants.C_USER);

//		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
//				IModelConstants.C_ORGANIZATION);
//		
//		DBCursor cur = col.find();
//		while(cur.hasNext()){
//			DBObject dbo = cur.next();
//			Organization org = ModelService.createModelObject(dbo, Organization.class);
//			try {
//				org.doSave(new CurrentAccountContext());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
//		while (cur.hasNext()) {
//			DBObject userdata = cur.next();
//			Object orgnumber = userdata.get("_temp");
//			DBObject orgdata = colOrg.findOne(new BasicDBObject().append(Organization.F_ORGANIZATION_NUMBER, orgnumber));
//			if(orgdata!=null){
//				userdata.put(User.F_ORGANIZATION_ID, orgdata.get("_id"));
//				userdata.put(User.F_ORGANIZATION_NAME, orgdata.get("desc"));
//			}else{
//				userdata.put(User.F_ACTIVATED, Boolean.FALSE);
//			}
//			col.save(userdata);
//		}
//
//		// Set<UserExchange> users = getBySqlUsers();
//		// Iterator<UserExchange> iter = users.iterator();
//		// DBObject[] usearr = new DBObject[users.size()];
//		// int i=0;
//		// while(iter.hasNext()){
//		// UserExchange ue = iter.next();
//		// usearr[i] = new BasicDBObject();
//		// usearr[i].put(User.F_ACTIVATED, Boolean.TRUE);
//		// usearr[i].put(User.F_EMAIL, ue.geteMail());
//		// usearr[i].put(User.F_NICK,ue.getUserName());
//		// usearr[i].put(User.F_USER_ID, ue.getUserId());
//		// usearr[i].put(User.F_USER_NAME,ue.getUserName());
//		// usearr[i].put(User.F_SCENARIO, new String[]{"project"});
//		// usearr[i].put("_temp", ue.getUnitId());
//		//
//		// i++;
//		// }
//		//
//		// col.insert(usearr, WriteConcern.NORMAL);
		System.out.println("ok");
	}

	/**
	 * 获取HR系统用户
	 * 
	 * @param hrOrgId
	 * @return
	 */
	public Set<UserExchange> getBySqlUsers() {
		Set<UserExchange> childrenSet = new HashSet<UserExchange>();
		SQLResult result;
		SQLRow row;
		try {
			// 获取用户
			result = SQLUtil.SQL_QUERY("hr", "select * from tb_nczz.pm_emp");

			if (!result.isEmpty()) {
				// 循环构造用户
				Iterator<SQLRow> iter = result.iterator();
				while (iter.hasNext()) {
					row = iter.next();
					UserExchange userExchange = new UserExchange();
					userExchange.seteMail("" + row.getValue("email"));
					userExchange.setUnitId("" + row.getValue("unit"));
					userExchange.setUserId("" + row.getValue("code"));
					userExchange.setUserName("" + row.getValue("name"));
					childrenSet.add(userExchange);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return childrenSet;
	}
}
