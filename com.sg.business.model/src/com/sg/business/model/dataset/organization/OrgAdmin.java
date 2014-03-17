package com.sg.business.model.dataset.organization;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.mobnut.db.model.mongodb.SingleDBCollectionDataSetFactory;
import com.mongodb.BasicDBObject;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.Role;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class OrgAdmin extends SingleDBCollectionDataSetFactory {

	public OrgAdmin() {
		// ������֯���ϵĴ������ݿ⼰���ݴ洢��
		super(IModelConstants.DB, IModelConstants.C_ORGANIZATION);
		// ���ò�ѯ����
		String userId = new CurrentAccountContext().getUserId();
		User user = UserToolkit.getUserById(userId);
		if (user.isAdmin()) {
			setQueryCondition(new BasicDBObject().append(
					Organization.F_PARENT_ID, null));
		} else {
			List<PrimaryObject> roles = user
					.getRoles(Role.ROLE_ORGANIZATION_ADMIN_ID);
			if (roles == null || roles.isEmpty()) {
				setQueryCondition(new BasicDBObject().append(
						Organization.F__ID, null));
			} else {
				List<ObjectId> ids = new ArrayList<ObjectId>();
				for (PrimaryObject primaryObject : roles) {
					ids.add(((Role) primaryObject).getOrganization_id());
				}
				setQueryCondition(new BasicDBObject().append(
						Organization.F__ID,
						new BasicDBObject().append("$in", ids)));
			}
		}
	}

}
