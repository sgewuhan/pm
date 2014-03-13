package com.sg.business.pm2;

import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.AccountInfo;
import com.mobnut.design.ext.IHeadAreaSupport;
import com.mobnut.portal.user.UserSessionContext;
import com.sg.business.commons.ui.UIFrameworkUtils;
import com.sg.business.model.User;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.ImageResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.Widgets;
import com.sg.widgets.bug.BugTools;
import com.sg.widgets.commons.model.IEditorSaveHandler;
import com.sg.widgets.part.IRefreshablePart;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.registry.config.DataEditorConfigurator;

public class HeadArea implements IHeadAreaSupport {

	private CLabel headerPic;
	private Label welcomeMessage;
	private Composite content;
	private String imageURL;
	private CLabel drop;
	private Composite headPicContainer;

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

		headPicContainer = new Composite(parent, SWT.NONE);
		Display display = parent.getDisplay();
		headPicContainer.setBackground(Widgets.getColor(display, 0x00, 0x99,
				0xcc));
		FormData fd = new FormData();
		headPicContainer.setLayoutData(fd);
		fd.top = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		headPicContainer.setLayout(new FormLayout());

		headerPic = new CLabel(headPicContainer, SWT.NONE);
		UIFrameworkUtils.enableMarkup(headerPic);
		//		headerPic.setData(RWT.CUSTOM_VARIANT, "headpic"); //$NON-NLS-1$
		resetImageURL();

		fd = new FormData();
		headerPic.setLayoutData(fd);
		fd.right = new FormAttachment(100, -2);
		fd.top = new FormAttachment();

		welcomeMessage = new Label(headPicContainer, SWT.NONE);
		welcomeMessage.setData(RWT.CUSTOM_VARIANT, "welcomemessage"); //$NON-NLS-1$
		try {
			setWelcomeMessage();
		} catch (Exception e1) {
		}
		fd = new FormData();
		welcomeMessage.setLayoutData(fd);
		fd.top = new FormAttachment(50, -8);
		fd.right = new FormAttachment(headerPic, -40);
		fd.left = new FormAttachment(0, 20);

		drop = new CLabel(headPicContainer, SWT.NONE);
		drop.setData(RWT.CUSTOM_VARIANT, "headpic"); //$NON-NLS-1$

		drop.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_DOWN_W_16));
		fd = new FormData();
		drop.setLayoutData(fd);
		fd.top = new FormAttachment(50, -8);
		fd.left = new FormAttachment(welcomeMessage, 10);
		createMenu(user);

		Button search = createSearch(parent);
		fd = new FormData();
		search.setLayoutData(fd);
		fd.top = new FormAttachment(0);
		fd.right = new FormAttachment(headPicContainer);
		fd.bottom = new FormAttachment(100);
		fd.width = 60;

		Button refresh = createRefresh(parent);
		fd = new FormData();
		refresh.setLayoutData(fd);
		fd.top = new FormAttachment(0);
		fd.right = new FormAttachment(search);
		fd.bottom = new FormAttachment(100);
		fd.width = 60;

		return headPicContainer;
	}

	private Button createRefresh(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setData(RWT.CUSTOM_VARIANT, "metro_green_active");
		button.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_REFRESH_W_24));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});
		return button;
	}

	protected void refresh() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IViewReference[] vrs = page.getViewReferences();
		for (int i = 0; i < vrs.length; i++) {
			IViewPart view = vrs[i].getView(false);
			if (view instanceof IRefreshablePart) {
				IRefreshablePart refreshablePart = (IRefreshablePart) view;
				if (refreshablePart.canRefresh()) {
					refreshablePart.doRefresh();
				}
			}
		}
	}
	
	private Button createSearch(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setData(RWT.CUSTOM_VARIANT, "metro_green_active");
		button.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_SEARCH_W_24));
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				search();
			}
		});
		return button;
	}

	protected void search() {
		// TODO Auto-generated method stub

	}

	private void createMenu(final User user) {
		final Shell shell = headerPic.getShell();
		// �����˵�
		final Menu dropDownMenu = new Menu(shell, SWT.POP_UP);
		MenuItem item = new MenuItem(dropDownMenu, SWT.PUSH);
		item.setText(Messages.get().HeadArea_1);
		item.setImage(BusinessResource.getImage(BusinessResource.IMAGE_USER_24));
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				editUserProfile(user);
			}
		});

		List<String[]> consigners = UserSessionContext.getSession()
				.getConsignerList();
		if (consigners.size() > 0) {
			item = new MenuItem(dropDownMenu, SWT.SEPARATOR);
			for (int i = 0; i < consigners.size(); i++) {
				item = new MenuItem(dropDownMenu, SWT.PUSH);
				final String[] cs = consigners.get(i);
				item.setText(Messages.get().HeadArea_2 + cs[0]
						+ "|" + consigners.get(i)[1]); //$NON-NLS-2$
				item.setImage(BusinessResource
						.getImage(BusinessResource.IMAGE_ASSIGNMENT_24));
				item.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						int yes = MessageUtil.showMessage(shell,
								Messages.get().HeadArea_4,
								Messages.get().HeadArea_5, SWT.YES | SWT.NO
										| SWT.ICON_QUESTION);
						if (yes == SWT.YES) {
							try {
								UserSessionContext.getSession()
										.setCurrentConsigner(cs[0], cs[1]);
								UserSessionContext.getSession().logout();
							} catch (Exception e1) {
								MessageUtil.showToast(e1);
							}
						}
					}
				});
			}
			item = new MenuItem(dropDownMenu, SWT.PUSH);
			item.setText(Messages.get().HeadArea_6);
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					int yes = MessageUtil.showMessage(shell,
							Messages.get().HeadArea_4,
							Messages.get().HeadArea_5, SWT.YES | SWT.NO
									| SWT.ICON_QUESTION);
					if (yes == SWT.NO) {
						return;
					}
					try {
						UserSessionContext.getSession()
								.cancelCurrentConsigner();
						MessageUtil.showToast("�ʻ�����", "�Ѿ����Ϊ������˴����ʺ�״̬" + "\n"
								+ "�����´ε�¼ϵͳ������Ľ������Լ����ʻ�", SWT.ICON_INFORMATION);
						UserSessionContext.getSession().logout();
					} catch (Exception e1) {
						MessageUtil.showToast(e1);
					}

				}
			});
		}

		// ��Ӹ�������
		item = new MenuItem(dropDownMenu, SWT.PUSH);
		item.setText(Messages.get().HeadArea_7);
		item.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_VARIABLE_24));
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage();
				try {
					page.showView("com.mobnut.admin.personalsetting"); //$NON-NLS-1$
				} catch (PartInitException pe) {
					MessageUtil.showToast(pe);
				}
			}
		});

		item = new MenuItem(dropDownMenu, SWT.PUSH);
		item.setText(Messages.get().HeadArea_9);
		item.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_REPORT_ADD_24));
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BugTools.bookBug();

			}
		});

		Menu submenu = new Menu(dropDownMenu);
		MenuItem item2 = new MenuItem(dropDownMenu, SWT.CASCADE);
		item2.setText(Messages.get().HeadArea_Perspective_Switch);
		item2.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_PERSPECTIVE_24));
		item2.setMenu(submenu);
		IPerspectiveDescriptor[] pers = UserSessionContext.getSession()
				.getScenarioPerspectives();
		MenuItem subItem;
		for (int i = 0; i < pers.length; i++) {
			final IPerspectiveDescriptor p = pers[i];
			subItem = new MenuItem(submenu, SWT.PUSH);
			subItem.setText(p.getLabel());
			subItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						PlatformUI.getWorkbench().showPerspective(
								p.getId(),
								PlatformUI.getWorkbench()
										.getActiveWorkbenchWindow());
					} catch (WorkbenchException e1) {
					}
				}
			});
		}

		item = new MenuItem(dropDownMenu, SWT.SEPARATOR);
		item = new MenuItem(dropDownMenu, SWT.PUSH);
		item.setText(Messages.get().HeadArea_Logout_title);
		item.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_LOGOUT_24));
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int yes = MessageUtil.showMessage(shell,
						Messages.get().HeadArea_Logout_title,
						Messages.get().HeadArea_Logout_message, SWT.YES
								| SWT.NO | SWT.ICON_QUESTION);
				if (yes == SWT.NO) {
					return;
				}
				try {
					UserSessionContext.getSession().logout();
				} catch (Exception e1) {
					MessageUtil.showToast(e1);
				}

			}
		});

		drop.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
				Point point = headPicContainer.toDisplay(
						headPicContainer.getBounds().x,
						headPicContainer.getBounds().y
								+ headPicContainer.getBounds().height);
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
			imageURL = FileUtil.getImageURL(ImageResource.USER_GRAY_60,
					Widgets.PLUGIN_ID, "image"); //$NON-NLS-1$
		}

	}

	private void setWelcomeMessage() throws Exception {
		AccountInfo user = UserSessionContext.getAccountInfo();
		String message;
		if (user.isConsigning()) {
			message = getWelcomeMessage(user.getUserName())
					+ Messages.get().HeadArea_22 + user.getconsignerName();
		} else {
			message = getWelcomeMessage(user.getUserName());
		}

		welcomeMessage.setText(message);
		welcomeMessage.getParent().layout();
	}

	protected void editUserProfile(User user) {
		DataEditorConfigurator conf = (DataEditorConfigurator) Widgets
				.getEditorRegistry().getConfigurator("editor.user"); //$NON-NLS-1$
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
					// setWelcomeMessage(getWelcomeMessage(user.getUsername()));
					content.layout();
					return true;
				}
			};
			DataObjectDialog.openDialog(user, conf, true, save,
					Messages.get().HeadArea_13);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getWelcomeMessage(String username) {

		return username;

		// GregorianCalendar ca = new GregorianCalendar();
		// int h = ca.get(GregorianCalendar.HOUR_OF_DAY);
		// switch (h) {
		// case 0:
		// case 1:
		// case 2:
		// case 3:
		// case 4:
		// case 5:
		// return username + Messages.get().HeadArea_14;
		// case 6:
		// case 7:
		// return username + Messages.get().HeadArea_15;
		// case 8:
		// case 9:
		// case 10:
		// case 11:
		// return username + Messages.get().HeadArea_16;
		// case 12:
		// case 13:
		// return username + Messages.get().HeadArea_17;
		// case 14:
		// case 15:
		// case 16:
		// case 17:
		// case 18:
		//
		// return username + Messages.get().HeadArea_18;
		// case 19:
		// case 20:
		// case 21:
		// case 22:
		// return username + Messages.get().HeadArea_19;
		// case 23:
		// case 24:
		//			return username + "zzZ..  "; //$NON-NLS-1$
		// }
		//		return ""; //$NON-NLS-1$
	}

	@Override
	public Image getCenterLogo() {
		return null;
	}

	@Override
	public void setImageURL(String url) {
		headerPic.setText(url);
	}

	@Override
	public void resetImageURL() {
		headerPic.setText("<img src='" + imageURL //$NON-NLS-1$
				+ "' style='float:left' width='60' height='60' />"); //$NON-NLS-1$
	}

	@Override
	public void creatLeftPart(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		UIFrameworkUtils.enableMarkup(label);
		StringBuffer sb = new StringBuffer();
		sb.append("<img src='" + FileUtil.getImageURL("logo_w.png", BusinessResource.PLUGIN_ID) + "' style='float:left; left:0; top:0; display:block;' width='140' height='30' />"); //$NON-NLS-1$ //$NON-NLS-2$
		label.setText(sb.toString());
		FormData fd = new FormData();
		label.setLayoutData(fd);
		fd.left = new FormAttachment(0, 4);
		fd.top = new FormAttachment(50, -15);

		Label sep = new Label(parent, SWT.NONE);
		sep.setBackground(Widgets.getColor(parent.getDisplay(), 0xff, 0xbb,
				0x00));
		fd = new FormData();
		sep.setLayoutData(fd);
		fd.left = new FormAttachment(label, 4);
		fd.top = new FormAttachment(50, -20);
		fd.height = 40;
		fd.width = 2;

		Label text = new Label(parent, SWT.NONE);
		fd = new FormData();
		text.setLayoutData(fd);
		text.setData(RWT.CUSTOM_VARIANT, "welcomemessage"); //$NON-NLS-1$
		text.setText("��Ч����\n��Ŀ����ƽ̨");
//		text.setText("ҵ�����\n��ƽ̨");
		fd.left = new FormAttachment(sep, 4);
		fd.top = new FormAttachment(50, -20);
		fd.height = 40;
		fd.right = new FormAttachment(100, -20);
	}
	
	

}
