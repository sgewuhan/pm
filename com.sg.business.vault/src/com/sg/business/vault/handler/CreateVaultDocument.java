package com.sg.business.vault.handler;

import java.util.Map;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPart;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Document;
import com.sg.business.model.Folder;
import com.sg.business.vault.nls.Messages;
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
		Folder folder = null;
		if (selected == null) {
			PrimaryObject master = vc.getMaster();
			if (master instanceof Folder) {
				folder = (Folder) master;
			}
		} else if (selected instanceof Folder) {
			folder = (Folder) selected;

		} else if (selected instanceof Document) {
			Document doc = (Document) selected;
			folder = doc.getFolder();
		}

		if (folder != null) {
			Document doc = folder
					.makeCreateDocument(new CurrentAccountContext());
			if (doc == null) {
				MessageUtil.showToast(Messages.get().CreateVaultDocument_0,
						SWT.ICON_WARNING);
			} else {
				try {
					DataObjectEditor.open(doc, "editor.forder.document", //$NON-NLS-1$
							true, null);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
			}
		}
	}

	@Override
	protected boolean nullSelectionContinue(IWorkbenchPart part,
			ViewerControl vc, Command command) {
		return true;
	}

}
