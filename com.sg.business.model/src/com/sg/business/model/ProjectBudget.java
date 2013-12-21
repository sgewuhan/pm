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
 * 项目预算<p/>
 * @author jinxitao
 *
 */
public class ProjectBudget extends PrimaryObject implements IProjectRelative {
	
	/**
	 * 下级预算
	 */
	public static final String F_CHILDREN = "children"; //$NON-NLS-1$
	
	/**
	 * 预算额度
	 */
	public static final String F_BUDGET_VALUE = "budgetvalue"; //$NON-NLS-1$
	
	/**
	 * 上级预算
	 */
	private ProjectBudget parent;

	/**
	 * 是否锁定
	 */
	public static final String F_LOCK = "islocked"; //$NON-NLS-1$

	/**
	 * 锁定者
	 */
	public static final String F_LOCKED_BY = "lockedby"; //$NON-NLS-1$

	/**
	 * 锁定时间
	 */
	public static final String F_LOCKED_ON = "lockdate"; //$NON-NLS-1$

	/**
	 * 返回下级预算
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
	 * 设置上级预算
	 * @param budgetItem
	 *            ,预算科目
	 */
	private void setParent(ProjectBudget budgetItem) {
		this.parent = budgetItem;
	}
	
	/**
	 * 返回类型名称
	 * @return String
	 */
	@Override
	public String getTypeName() {
		return Messages.get().ProjectBudget_0;
	}

	/**
	 * 返回上级预算
	 * @return ProjectBudget
	 */
	public ProjectBudget getParent() {
		return parent;
	}

	/**
	 * 判断是否具有下级预算
	 * @return boolean
	 */
	public boolean hasChildren() {

		return getChildren().length > 0;
	}

	/**
	 * 返回预算科目值
	 * @return Double
	 */
	public Double getBudgetValue() {
		return (Double) getValue(F_BUDGET_VALUE);
	}

	/**
	 * 设置预算科目的值
	 * @param val
	 *         ,值
	 */
	public void inputBudgetValue(Double val) {
		if (val == null) {
			return;
		}
		setValue(F_BUDGET_VALUE, val);
		calculateValue();
	}

	/**
	 * 计算预算科目的值，上级预算的值由下级预算向上累计
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
	 * 返回项目
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
