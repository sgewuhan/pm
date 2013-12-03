package com.sg.business.pm2;

import java.util.GregorianCalendar;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.AccountInfo;
import com.mobnut.design.ext.IHeadAreaSupport;
import com.mobnut.portal.user.IAccountChangeListener;
import com.mobnut.portal.user.IAccountEvent;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.ImageResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class HeadArea implements IHeadAreaSupport, IAccountChangeListener {

	private CLabel headerPic;
	private Label welcomeMessage;
	private Composite content;
	private String imageURL;

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
		final User user = UserToolkit.getUserById(account.getUserId());
		loadHeadPic(user);

		Composite headPicContainer = new Composite(parent, SWT.NONE);

		headPicContainer.setLayout(new FormLayout());

		FormData fd = new FormData();
		headPicContainer.setLayoutData(fd);
		fd.top = new FormAttachment(0);
		fd.left = new FormAttachment(0);

		headerPic = new CLabel(headPicContainer, SWT.NONE);
		headerPic.setData(RWT.MARKUP_ENABLED, Boolean.TRUE);
		headerPic.setData(RWT.CUSTOM_VARIANT, "headpic");
		resetImageURL();

		fd = new FormData();
		headerPic.setLayoutData(fd);
		fd.left = new FormAttachment(0, 2);
		fd.top = new FormAttachment(0, 2);
		fd.height = 46;
		fd.width = 46;

		final Shell shell = headerPic.getShell();
		// 创建菜单
		final Menu dropDownMenu = new Menu(shell, SWT.POP_UP);
		MenuItem item = new MenuItem(dropDownMenu, SWT.PUSH);
		item.setText("更改我的个人信息");
		item.setImage(BusinessResource.getImage(BusinessResource.IMAGE_USER_24));
		List<String[]> consigners = UserSessionContext.getSession()
				.getConsignerList();
		if (consigners.size() > 0) {
			item = new MenuItem(dropDownMenu, SWT.SEPARATOR);
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					editUserProfile(user);
				}
			});
			for (int i = 0; i < consigners.size(); i++) {
				item = new MenuItem(dropDownMenu, SWT.PUSH);
				final String[] cs = consigners.get(i);
				item.setText("代管 " + cs[0] + "|" + consigners.get(i)[1]);
				item.setImage(BusinessResource
						.getImage(BusinessResource.IMAGE_ASSIGNMENT_24));
				item.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						int yes = MessageUtil
								.showMessage(
										shell,
										"切换至代管账户",
										"切换至代管账户将中止某些您当前的操作进程。\n请确保您已经保存了重要的数据。选择YES切换至托管账户",
										SWT.YES | SWT.NO | SWT.ICON_QUESTION);
						if (yes == SWT.YES) {
							UserSessionContext.getSession()
									.setCurrentConsigner(cs[0], cs[1]);
						}
					}
				});
			}
			item = new MenuItem(dropDownMenu, SWT.PUSH);
			item.setText("取消代管");
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					UserSessionContext.getSession().cancelCurrentConsigner();
				}
			});
		}

		// 添加个人设置
		item = new MenuItem(dropDownMenu, SWT.PUSH);
		item.setText("用户设置");
		item.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_VARIABLE_24));
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					page.showView("com.mobnut.admin.personalsetting");
				} catch (PartInitException pe) {
					MessageUtil.showToast(pe);
				}
			}
		});

		headerPic.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				Point point = headerPic.toDisplay(headerPic.getBounds().x,
						headerPic.getBounds().y + headerPic.getBounds().height);
				dropDownMenu.setLocation(point);
				dropDownMenu.setVisible(true);
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

		// // 在welCome上面添加一个 小的消息图标
		//
		// Label messageButton = new Label(parent,SWT.NONE);
		// messageButton.setImage(Widgets.getImage(ImageResource.MESSAGE_24));
		//
		// fd = new FormData();
		// messageButton.setLayoutData(fd);
		// fd.bottom = new FormAttachment(welcomeMessage, -4);
		// fd.left = new FormAttachment(headPicContainer, 6);
		// fd.top = new FormAttachment(0,2);

		UserSessionContext.getSession().addAccountChangeListener(this);
		content.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent event) {
				UserSessionContext.getSession().removeAccountChangeListener(
						HeadArea.this);
			}
		});
		return headPicContainer;
	}

	private void loadHeadPic(User user) {
		List<RemoteFile> headpics = user.getGridFSFileValue(User.F_HEADPIC);
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

	}

	private void setWelcomeMessage(String welcomeMessageText) {
		welcomeMessage.setText(welcomeMessageText);
		welcomeMessage.getParent().layout();
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
	// // 将用户名转换为小写
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


	protected void editUserProfile(User user) {
		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator("editor.user");
		try {
			IEditorSaveHandler save = new IEditorSaveHandler() {

				@Override
				public boolean doSaveBefore(PrimaryObjectEditorInput input,
						IProgressMonitor monitor, String operation)
						throws Exception {
					return true;
				}

				@Override
				public boolean doSaveAfter(PrimaryObjectEditorInput input,
						IProgressMonitor monitor, String operation)
						throws Exception {
					User user = (User) input.getData();
					loadHeadPic(user);
					resetImageURL();
					setWelcomeMessage(getWelcomeMessage(user.getUsername()));
					content.layout();
					return true;
				}
			};
			DataObjectDialog.openDialog(user, conf, true, save, "编辑用户资料");
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
			return username + " 您好";
		case 6:
		case 7:
			return username + " 早上好 ";
		case 8:
		case 9:
		case 10:
		case 11:
			return username + " 上午好";
		case 12:
		case 13:
			return username + " 中午好";
		case 14:
		case 15:
		case 16:
		case 17:
		case 18:

			return username + " 下午好";
		case 19:
		case 20:
		case 21:
		case 22:
			return username + " 晚上好";
		case 23:
		case 24:
			return username + "zzZ..  ";
		}
		return "";
	}

	@Override
	public Image getCenterLogo() {
		return null;
	}

	@Override
	public void accountChanged(IAccountEvent event) {
		String code = event.getEventCode();
		if (UserSessionContext.EVENT_CONSIGNER_CHANGED.equals(code)) {
			try {
				AccountInfo user = UserSessionContext.getAccountInfo();
				if (user.isConsigning()) {
					setWelcomeMessage(getWelcomeMessage(user.getUserName())
							+ " 代表:" + user.getconsignerName());
				} else {
					setWelcomeMessage(getWelcomeMessage(user.getUserName()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setImageURL(String url) {
		headerPic.setText(url);
	}

	@Override
	public void resetImageURL() {
		headerPic.setText("<img src='" + imageURL
				+ "' style='float:left' width='48' height='48' />");
	}

}
