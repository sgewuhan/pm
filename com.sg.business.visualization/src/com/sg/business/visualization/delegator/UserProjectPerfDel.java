package com.sg.business.visualization.delegator;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.DataSet;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.UserProjectPerf;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.viewer.IDeleteDelegator;

public class UserProjectPerfDel implements IDeleteDelegator {

	public UserProjectPerfDel() {
	}

	@Override
	public void doRemove(PrimaryObject po, ColumnViewer viewer, DataSet dataSet) {
		if(po instanceof UserProjectPerf){
			try {
				UserProjectPerf userProjectPerf = (UserProjectPerf) po;
				userProjectPerf.doRemove(new CurrentAccountContext());
				PrimaryObject parent = po.getParentPrimaryObjectCache();
				((TreeViewer) viewer).refresh(parent);
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}else{
			MessageUtil.showToast(Messages.get().UserProjectPerfDel_0, SWT.ICON_WARNING);
		}
	}
}