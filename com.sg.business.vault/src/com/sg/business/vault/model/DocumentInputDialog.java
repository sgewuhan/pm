package com.sg.business.vault.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.mobnut.db.DBActivator;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.part.CurrentAccountContext;

public class DocumentInputDialog extends Dialog {



	public DocumentInputDialog(Shell shell) {
		super(shell);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("请输入查询的文档名称或文档编号");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		Combo combo = new Combo(composite, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, false, false);
		gd.widthHint = 80;
		
		List<String> docList=new ArrayList<String>();
		// 获得文档名称和文档编号
		DBCollection collection = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_DOCUMENT);
		String userId = new CurrentAccountContext().getAccountInfo()
				.getConsignerId();
		User currentUser = UserToolkit.getUserById(userId);
		Organization organization = currentUser.getFunctionOrganization();
		String fileBase = organization.getFileBase();

		DBCursor cursor = collection.find(new BasicDBObject().append(
				Document.F_FILEBASE_NAMESPACE, fileBase));
		while(cursor.hasNext()){
			String docDesc = (String) cursor.next().get(Document.F_DESC);
			String docNumber = (String) cursor.next().get(Document.F_DOCUMENT_NUMBER);
			docList.add(docDesc);
			docList.add(docNumber);
		}
		cursor.close();
		combo.setItems((String[]) docList.toArray());
		
		
		return composite;
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

}
