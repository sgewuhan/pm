package com.sg.business.vault.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.command.AbstractNavigatorHandler;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.viewer.ViewerControl;

public class CreateVaultDocument extends AbstractNavigatorHandler {

	@Override
	protected void execute(PrimaryObject selected, IWorkbenchPart part,
			ViewerControl vc, Command command, Map<String, Object> parameters,
			IStructuredSelection selection) {
		if (selected instanceof Folder) {
			Folder folder = (Folder) selected;
			Document doc = folder.makeCreateDocument(new CurrentAccountContext());
			if (doc == null) {
				MessageUtil.showToast("选择的交付物不包含文档", SWT.ICON_WARNING);
			} else {
				try {
					DataObjectEditor.open(doc, "editor.forder.document",
							true, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		}		
	}

}
