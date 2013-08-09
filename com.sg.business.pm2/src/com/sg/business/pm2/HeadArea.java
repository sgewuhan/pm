package com.sg.business.pm2;

import java.util.GregorianCalendar;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.AccountInfo;
import com.mobnut.design.ext.IHeadAreaSupport;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.User;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class HeadArea implements IHeadAreaSupport {

	private Label headerPic;
	private Label welcomeMessage;
	private Composite content;

	public HeadArea() {

	}

	@Override
	public Composite creatHeadAreaPart(Composite parent) {
		content = parent;
		AccountInfo account;
		try {
			account = UserSessionContext.getAccountInfo();
		} catch (Exception e) {
			return null;
		}
		final User user = User.getUserById(account.getUserId());

		Composite headPicContainer = new Composite(parent, SWT.NONE);

		headPicContainer.setLayout(new FormLayout());

		FormData fd = new FormData();
		headPicContainer.setLayoutData(fd);
		fd.top = new FormAttachment(0);
		fd.left = new FormAttachment(0);

		headerPic = new Label(headPicContainer, SWT.NONE);
		headerPic.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		headerPic.setData(RWT.CUSTOM_VARIANT, "headpic");
		setHeadPic(user);

		fd = new FormData();
		headerPic.setLayoutData(fd);
		fd.left = new FormAttachment(0, 2);
		fd.top = new FormAttachment(0, 2);
		fd.height = 46;
		fd.width = 46;
		headerPic.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				editUserProfile(user);
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});

		welcomeMessage = new Label(parent, SWT.NONE);
		welcomeMessage.setData(RWT.CUSTOM_VARIANT, "welcomemessage");
		setWelcomeMessage(getWelcomeMessage(user.getUsername()));
		fd = new FormData();
		welcomeMessage.setLayoutData(fd);
		fd.bottom = new FormAttachment(100, -1);
		fd.left = new FormAttachment(headPicContainer, 6);

		// // ��welCome�������һ�� С����Ϣͼ��
		//
		// Label messageButton = new Label(parent,SWT.NONE);
		// messageButton.setImage(Widgets.getImage(ImageResource.MESSAGE_24));
		//
		// fd = new FormData();
		// messageButton.setLayoutData(fd);
		// fd.bottom = new FormAttachment(welcomeMessage, -4);
		// fd.left = new FormAttachment(headPicContainer, 6);
		// fd.top = new FormAttachment(0,2);

		return headPicContainer;
	}

	private void setWelcomeMessage(String welcomeMessageText) {
		welcomeMessage.setText(welcomeMessageText);		
	}

	// protected void editUserProfile(DBObject user) {
	//
	// SingleObjectEditorInput editInput = new SingleObjectEditorInput(new
	// SingleObject(DBActivator.getDefaultDBCollection(IDBConstants.COLLECTION_USER),user));
	// ISingleObjectEditorDialogCallback call = new
	// SingleObjectEditorDialogCallback() {
	//
	// @Override
	// public boolean saveBefore(ISingleObjectEditorInput input) {
	// SingleObject data = (SingleObject) input.getInputData();
	//
	// // ���û���ת��ΪСд
	// String userName = (String) data.getValue(IDBConstants.FIELD_DESC);
	// if (userName == null) {
	// userName = "";
	// }
	// data.setValue(IDBConstants.FIELD_DESC, userName);
	//
	// return super.saveBefore(input);
	// }
	// };
	// Shell shell =
	// PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	// SingleObjectEditorDialog.OPEN(shell, UIConstants.EDITOR_USER_SELF_EDIT,
	// editInput, call, false);
	//
	// }

	private void setHeadPic(User user) {
		List<RemoteFile> headpics = user.getFileValue(User.F_HEADPIC);
		String imageURL = null;
		if (headpics != null && headpics.size() > 0) {
			try {
				imageURL = FileUtil.getImageURL(headpics.get(0).getNamespace(),
						new ObjectId(headpics.get(0).getObjectId()), headpics
								.get(0).getDbName());
			} catch (Exception e) {
			}
		}
		if (imageURL == null) {
			imageURL = FileUtil.getImageURL(ImageResource.LOGO_GRAY_48,
					Widgets.PLUGIN_ID, "image");
		}

		headerPic.setText("<img src='" + imageURL
				+ "' style='float:left' width='46' height='46' />");		
	}

	protected void editUserProfile(User user) {
		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator("editor.user");
		try {
			IEditorSaveHandler save = new IEditorSaveHandler() {
				
				@Override
				public boolean doSaveBefore(PrimaryObjectEditorInput input,
						IProgressMonitor monitor, String operation) throws Exception {
					return true;
				}
				
				@Override
				public boolean doSaveAfter(PrimaryObjectEditorInput input,
						IProgressMonitor monitor, String operation) throws Exception {
					User user = (User) input.getData();
					setHeadPic(user);
					setWelcomeMessage(getWelcomeMessage(user.getUsername()));
					content.layout();
					return true;
				}
			};
			DataObjectDialog.openDialog(user, conf, true, save,"�༭�û�����");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getWelcomeMessage(String username) {

		GregorianCalendar ca = new GregorianCalendar();
		int h = ca.get(GregorianCalendar.HOUR_OF_DAY);
		switch (h) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			return username + " ����";
		case 6:
		case 7:
			return username + " ���Ϻ� ";
		case 8:
		case 9:
		case 10:
		case 11:
			return username + " �����";
		case 12:
		case 13:
			return username + " �����";
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:
			
			return username + " �����";
		case 19:
		case 20:
		case 21:
		case 22:
			return username + " ���Ϻ�";
		case 23:
		case 24:
			// return "zzZ..  ";
		}
		return "";
	}

	@Override
	public Image getCenterLogo() {
		return null;
	}

}
