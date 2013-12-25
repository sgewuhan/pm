package com.sg.business.commons;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.mobnut.db.model.DocumentModelDefinition;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.ApplicationActionBarAdvisor;
import com.sg.widgets.UIConstants;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.editor.DataObjectWizard;

/**
 * Configures the initial size and appearance of a workbench window.
 */
public class DirectWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private IMemento memento;

	public DirectWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShellStyle(SWT.NONE);

		final IWorkbenchWindow window = configurer.getWindow();
		window.addPageListener(new IPageListener() {

			@Override
			public void pageOpened(final IWorkbenchPage page) {
				page.addPartListener(new IPartListener() {

					@Override
					public void partOpened(IWorkbenchPart part) {
					}

					@Override
					public void partDeactivated(IWorkbenchPart part) {
					}

					@Override
					public void partClosed(IWorkbenchPart part) {
						IWorkbenchPage[] pages = window.getPages();
						for (int i = 0; i < pages.length; i++) {
							IEditorReference[] er = pages[i]
									.getEditorReferences();
							if (er == null || er.length == 0) {
								// Òþ²Ø±à¼­Çø
								pages[i].setEditorAreaVisible(false);
							}
						}
					}

					@Override
					public void partBroughtToTop(IWorkbenchPart part) {
					}

					@Override
					public void partActivated(IWorkbenchPart part) {
					}
				});
			}

			@Override
			public void pageClosed(IWorkbenchPage page) {
			}

			@Override
			public void pageActivated(IWorkbenchPage page) {
				IEditorReference[] er = page.getEditorReferences();
				if (er == null || er.length == 0) {
					// Òþ²Ø±à¼­Çø
					page.setEditorAreaVisible(false);
				}
			}
		});

	}

	@Override
	public void postWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		IWorkbenchWindow window = configurer.getWindow();
		window.getShell().setMaximized(true);
		// setupKeys(window.getShell().getDisplay());

		// ÏìÓ¦²ÎÊý
		restore();

		super.postWindowOpen();
	}

	private void restore() {
		if(memento == null){
			return;
		}
		try {
			String _id = memento.getString("id"); //$NON-NLS-1$
			ObjectId id = new ObjectId(_id);
			
			String _class = memento.getString("class"); //$NON-NLS-1$
			DocumentModelDefinition dmd = ModelService
					.getDocumentModelDefinition(_class);
			
			Class<? extends PrimaryObject> clas = dmd.getModelClass();
			PrimaryObject po = ModelService.createModelObject(clas, id);
			boolean editable = memento.getBoolean("editable"); //$NON-NLS-1$
			Integer val = memento.getInteger("editype"); //$NON-NLS-1$
			int editype;
			if (val == null) {
				editype = 1;
			} else {
				editype = val.intValue();
			}

			String _editor = memento.getString("editor"); //$NON-NLS-1$
			
			switch (editype) {
			case UIConstants.EDITING_BY_DIALOG:
				DataObjectDialog.openDialog(po, _editor, editable, null);
				break;
			case UIConstants.EDITING_BY_EDITOR:
				DataObjectEditor.open(po, _editor, editable, null);
				break;
			case UIConstants.EDITING_BY_WIZARD:
				DataObjectWizard.open(po, _editor, editable, null);
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
	}

	// if( event.stateMask == 0 && event.character == 'C' ) {
	// handleCompose();
	// } else if( event.stateMask == 0 && event.character == 'X' ) {
	// handleDelete();
	// } else if( event.stateMask == SWT.CTRL && event.keyCode == SWT.F11 ) {
	// doSomeAdvancedStuff();
	// }
	// private void setupKeys(Display display) {
	// display.setData(RWT.ACTIVE_KEYS, new String[] { "CTRL+N", "CTRL+M",
	// "F5" });
	// display.addFilter(SWT.KeyDown, new Listener() {
	//
	// @Override
	// public void handleEvent(Event event) {
	// System.out.println(event.keyCode);
	// }
	// });
	// }
	@Override
	public IStatus saveState(IMemento memento) {
		this.memento = memento;
		return super.saveState(memento);
	}
}
