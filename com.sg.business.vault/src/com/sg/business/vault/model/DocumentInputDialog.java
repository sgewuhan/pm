package com.sg.business.vault.model;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.mobnut.db.DBActivator;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;

public class DocumentInputDialog extends Dialog {

	private String docDesc;
	private String docNumber;

	public String getDocDesc() {
		return docDesc;
	}

	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

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
		composite.setLayout(new GridLayout());
		Text text = new Text(composite, SWT.BORDER);
		// Combo combo = new Combo(composite, SWT.BORDER);
		GridData gd = new GridData();
		text.setLayoutData(gd);
		gd.widthHint = 300;

		// 获得文档名称和文档编号
		DBCollection collection = DBActivator.getCollection(IModelConstants.DB,
				IModelConstants.C_DOCUMENT);
		DBCursor find = collection.find();
		while (find.hasNext()) {
			String docDesc = (String) find.next().get(Document.F_DESC);
			String docNumber = (String) find.next().get(
					Document.F_DOCUMENT_NUMBER);
			if (getDocDesc().equals(docDesc)
					|| getDocNumber().equals(docNumber)) {
				System.out.println(docDesc);
			}
		}

		// String userId = new CurrentAccountContext().getAccountInfo()
		// .getConsignerId();
		// User currentUser = UserToolkit.getUserById(userId);
		// Organization organization = currentUser.getFunctionOrganization();
		// String fileBase = organization.getFileBase();

		// DBCursor cursor = collection.find(new BasicDBObject().append(
		// Document.F_LIFECYCLE, new BasicDBObject().append("in",
		// new String[] { Document.STATUS_RELEASED_ID,
		// Document.STATUS_WORKING_ID })));
		// List<String> docList=new ArrayList<String>();
		// String[] array=new String[docList.size()];
		// while (cursor.hasNext()) {
		// String docDesc = (String) cursor.next().get(Document.F_DESC);
		// String docNumber = (String) cursor.next().get(
		// Document.F_DOCUMENT_NUMBER);
		// docList.add(docDesc);
		// docList.add(docNumber);
		// }
		// cursor.close();
		// String [] array=new
		// String[]{"1","2","3","4","5","6","7","8","9","10"};
		// combo.setItems(array);
		// for (int i = 0; i < array.length; i++) {
		// combo.select(i);
		// }

		return composite;
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

}
