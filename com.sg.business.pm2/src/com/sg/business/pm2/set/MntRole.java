package com.sg.business.pm2.set;

import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.IProcessControl;
import com.sg.business.model.RoleDefinition;
import com.sg.business.model.WorkDefinition;

public class MntRole implements Runnable {

	@Override
	public void run() {
		// 遍历工作定义检查工作定义的角色数据是否完整

		DBCollection col = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_WORK_DEFINITION);

		DBCursor cur = col.find();
		while (cur.hasNext()) {
			DBObject dbo = cur.next();
			WorkDefinition workd = ModelService.createModelObject(dbo,
					WorkDefinition.class);
			
			check(workd);
		}

	}

	private void check(WorkDefinition workd) {
		List<PrimaryObject> rolesd = workd.getParticipateRoles();
		if(rolesd!=null){
			for(int i=0;i<rolesd.size();i++){
				RoleDefinition rd = (RoleDefinition) rolesd.get(i);
				try {
					checkRoled(rd,workd+"参与者");
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}
		RoleDefinition rd = workd.getChargerRoleDefinition(RoleDefinition.class);
		if(rd!=null){
			try {
				checkRoled(rd,workd+"负责人");
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		
		IProcessControl ipc = workd.getAdapter(IProcessControl.class);
		DBObject data = ipc.getProcessRoleAssignmentData(WorkDefinition.F_WF_EXECUTE);
		if(data!=null){
			Iterator<String> iter = data.keySet().iterator();
			while(iter.hasNext()){
				String key = iter.next();
				ObjectId _rdId = (ObjectId) data.get(key);
				rd = ModelService.createModelObject(RoleDefinition.class, _rdId);
				if(rd!=null){
					DBObject rddata = rd.get_data();
					if(rddata==null){
						System.err.println(workd+",流程"+key+":"+_rdId);
					}else{
						rd.getLabel();
					}
				}
			}
		}
	}

	private void checkRoled(RoleDefinition rd, String message) throws Exception {
		if(rd.isOrganizatioRole()){
//			//检查组织角色是否存在
//			Role orole = rd.getOrganizationRole();
//			if(orole.isEmpty()){
//				System.out.println(orole.get_id());
//			}
			try{
				rd.getLabel();
			}catch(Exception e){
				throw new Exception(message+rd.get_id());
			}
		}else{
			try{
				rd.getLabel();
			}catch(Exception e){
				throw new Exception(message+rd.get_id());
			}
		}		
	}

}
