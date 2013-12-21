package com.sg.business.model;

import java.util.Date;

import org.bson.types.ObjectId;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.model.nls.Messages;

/**
 * ��ĿԤ��<p/>
 * @author jinxitao
 *
 */
public class ProjectBudget extends PrimaryObject implements IProjectRelative {
	
	/**
	 * �¼�Ԥ��
	 */
	public static final String F_CHILDREN = "children"; //$NON-NLS-1$
	
	/**
	 * Ԥ����
	 */
	public static final String F_BUDGET_VALUE = "budgetvalue"; //$NON-NLS-1$
	
	/**
	 * �ϼ�Ԥ��
	 */
	private ProjectBudget parent;

	/**
	 * �Ƿ�����
	 */
	public static final String F_LOCK = "islocked"; //$NON-NLS-1$

	/**
	 * ������
	 */
	public static final String F_LOCKED_BY = "lockedby"; //$NON-NLS-1$

	/**
	 * ����ʱ��
	 */
	public static final String F_LOCKED_ON = "lockdate"; //$NON-NLS-1$

	/**
	 * �����¼�Ԥ��
	 * @return ProjectBudget[]
	 */
	public ProjectBudget[] getChildren() {
		BasicDBList childrenData = (BasicDBList) getValue(F_CHILDREN);
		if (childrenData != null) {
			ProjectBudget[] result = new ProjectBudget[childrenData.size()];
			for (int i = 0; i < childrenData.size(); i++) {
				result[i] = ModelService.createModelObject(
						(DBObject) childrenData.get(i), ProjectBudget.class);
				result[i].setParent(this);
			}
			return result;
		} else {
			return new ProjectBudget[0];
		}
	}

	/**
	 * �����ϼ�Ԥ��
	 * @param budgetItem
	 *            ,Ԥ���Ŀ
	 */
	private void setParent(ProjectBudget budgetItem) {
		this.parent = budgetItem;
	}
	
	/**
	 * ������������
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().ProjectBudget_0;
	}

	/**
	 * �����ϼ�Ԥ��
	 * @return ProjectBudget
	 */
	public ProjectBudget getParent() {
		return parent;
	}

	/**
	 * �ж��Ƿ�����¼�Ԥ��
	 * @return boolean
	 */
	public boolean hasChildren() {

		return getChildren().length > 0;
	}

	/**
	 * ����Ԥ���Ŀֵ
	 * @return Double
	 */
	public Double getBudgetValue() {
		return (Double) getValue(F_BUDGET_VALUE);
	}

	/**
	 * ����Ԥ���Ŀ��ֵ
	 * @param val
	 *         ,ֵ
	 */
	public void inputBudgetValue(Double val) {
		if (val == null) {
			return;
		}
		setValue(F_BUDGET_VALUE, val);
		calculateValue();
	}

	/**
	 * ����Ԥ���Ŀ��ֵ���ϼ�Ԥ���ֵ���¼�Ԥ�������ۼ�
	 */
	private void calculateValue() {
		ProjectBudget parentItem = getParent();
		if (parentItem != null) {
			ProjectBudget[] children = parentItem.getChildren();
			double summary = 0f;
			for (ProjectBudget child : children) {
				Double bv = child.getBudgetValue();
				if (bv != null) {
					summary += bv.doubleValue();
				}
			}
			parentItem.setValue(F_BUDGET_VALUE, new Double(summary));
			parentItem.calculateValue();
		}
	}
	
	/**
	 * ������Ŀ
	 * @return Project
	 */
	@Override
	public Project getProject() {
		ObjectId ptId = (ObjectId) getValue(F_PROJECT_ID);
		if (ptId != null) {
			return ModelService.createModelObject(Project.class, ptId);
		} else {
			return null;
		}
	}

	public void doLock(IContext context) {
		if (isLocked()) {
			return;
		}
		setValue(F_LOCK , Boolean.TRUE);
		setValue(F_LOCKED_BY, context.getAccountInfo().getConsignerId());
		Date newValue = new Date();
		setValue(F_LOCKED_ON, newValue);
		DBCollection col = getCollection();
		col.update(new BasicDBObject().append(F__ID, get_id()),
				new BasicDBObject().append(
						"$set", //$NON-NLS-1$
						new BasicDBObject()
								.append(F_LOCK, Boolean.TRUE)
								.append(F_LOCKED_BY,
										context.getAccountInfo()
												.getConsignerId())
								.append(F_LOCKED_ON, newValue)));		
	}

	public boolean isLocked() {
		return Boolean.TRUE.equals(getValue(F_LOCK));
	}
}
