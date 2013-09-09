package com.sg.business.organization.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IInputProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.IMessage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ScrolledForm;


public class SyncHRDialog extends FormDialog implements IInputProvider {

	protected Object input;
	protected Font font;
	private String title;
	protected IManagedForm managedForm;
	protected ScrolledForm form;
	protected Menu messagePopup;

	public SyncHRDialog(Shell shell, Object input, String title) {
		super(shell);
		this.input = input;
		this.title = title;
	}

	@Override
	public boolean close() {
		font.dispose();
		return super.close();
	}

	private void loadDialogTitle() {
		getShell().setText(title);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID,
				IDialogConstants.get().OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.get().CANCEL_LABEL, false);
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		this.managedForm = managedForm;
		form = managedForm.getForm();
		font = new Font(form.getDisplay(), "Î¢ÈíÑÅºÚ", 16, SWT.NORMAL); //$NON-NLS-1$

		managedForm.setInput(input);

		addFormMessageLisener(managedForm);

		// ´´½¨page body
		createContent(form.getBody());
		form.reflow(true);
	}

	protected void createContent(Composite parent) {
		loadDialogTitle();

		parent.setLayout(new FillLayout());

//		MultipageEditablePanel folder = new MultipageEditablePanel(parent,
//				SWT.TOP | SWT.FLAT);
//		folder.setMessageManager(form.getMessageManager());
		// folder.createContents(managedForm, input);

	}

	private void addFormMessageLisener(IManagedForm managedForm) {
		form.getForm().addMessageHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				showMessagePopup(e);
			}
		});
	}

	private void showMessagePopup(HyperlinkEvent e) {
		if (messagePopup != null && !messagePopup.isDisposed()) {
			messagePopup.dispose();
		}
		messagePopup = new Menu(form.getShell(), SWT.POP_UP);
		IMessage[] messages = form.getForm().getChildrenMessages();
		for (int i = 0; i < messages.length; i++) {
			final IMessage message = messages[i];
			MenuItem item = new MenuItem(messagePopup, SWT.PUSH);
			item.setText(message.getPrefix() + " " + message.getMessage()); //$NON-NLS-1$
			item.setImage(getMessageImage(message.getMessageType()));
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Control c = message.getControl();
					if (c != null && !c.isDisposed()) {
						c.setFocus();
					}
				}
			});
		}
		Point hl = ((Control) e.widget).toDisplay(0, 0);
		hl.y += 16;
		messagePopup.setLocation(hl);
		messagePopup.setVisible(true);

	}

	private Image getMessageImage(int type) {
		Display display = form.getShell().getDisplay();
		switch (type) {
		case IMessageProvider.ERROR:
			return display.getSystemImage(SWT.ICON_ERROR);
		case IMessageProvider.WARNING:
			return display.getSystemImage(SWT.ICON_WARNING);
		case IMessageProvider.INFORMATION:
			return display.getSystemImage(SWT.ICON_INFORMATION);
		}
		return null;
	}

	@Override
	public Object getInput() {
		return input;
	}

}
