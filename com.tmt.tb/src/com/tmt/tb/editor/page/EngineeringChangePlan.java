package com.tmt.tb.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.jbpm.task.Task;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.portal.Portal;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.part.WorkListCreater;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.IDeliverable;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.tmt.tb.nls.Messages;

public class EngineeringChangePlan extends AbstractFormPageDelegator {

	private WorkListCreater workListCreater;
	private IContext context;
	private String eca;
	private String menuId = "popup:project.change.document";
	private Button createDeliverableButton;
	private boolean buttonEnable;

	public EngineeringChangePlan() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {

		context = new CurrentAccountContext();
		setFormInput(input);
		parent.setLayout(new FormLayout());
		workListCreater = new WorkListCreater(parent, SWT.BORDER);
		workListCreater.setContext(context);

		TaskForm taskform = (TaskForm) input.getData();
		List<Work> ecaList = new ArrayList<Work>();
		Object value;
		try {
			Task task = taskform.getExecuteTask(context);
			List<Work> ecnList = new ArrayList<Work>();
			String taskName;
			if (task != null) {
				taskName = task.getNames().get(0).getText();
				value = taskform.getProcessInstanceVarible("ecn", context); //$NON-NLS-1$
				ecnList.addAll(appendWorkDataList(value));
				for (Work ecn : ecnList) {
					if (taskName != null && taskName.equals(ecn.getDesc())) {
						eca = (String) ecn.getValue(Work.F_INTERNAL_ECAPARA);
						// value = taskform.getProcessInstanceVarible(eca,
						// context);
						value = taskform.getValue(eca);
						ecnList.addAll(appendWorkDataList(value));
					}
				}
				buttonEnable = true;
			} else {
				taskName = taskform.getUserTask().getDesc();
				eca = (String) taskform.getValue(Work.F_INTERNAL_ECAPARA);
				// value = taskform.getProcessInstanceVarible(eca,
				// context);
				buttonEnable = false;
				value = taskform.getValue(eca);
				ecaList.addAll(appendWorkDataList(value));
			}

		} catch (Exception e) {
			if (Portal.getDefault().isDevelopMode()) {
				e.printStackTrace();
			}
		}
		
		
		Button b = createToolBar(parent);
		
		FormData fd = new FormData();
		workListCreater.setLayoutData(fd);
		fd.left = new FormAttachment(0, 2);
		fd.top = new FormAttachment(b, 2);
		fd.right = new FormAttachment(100, -2);
		fd.bottom = new FormAttachment(100, -2);
		fd.height = 400;

		workListCreater.setInput(ecaList);
		return workListCreater;
	}

	private List<Work> appendWorkDataList(Object value) {
		List<Work> primaryObjectList = new ArrayList<Work>();
		if (value instanceof List<?>) {
			List<?> list = (List<?>) value;
			for (Object dbo : list) {
				Work work = ModelService.createModelObject((DBObject) dbo,
						Work.class);
				primaryObjectList.add(work);
			}
		} else if (value instanceof Object[]) {
			Object[] array = (Object[]) value;
			for (Object dbo : array) {
				Work work = ModelService.createModelObject((DBObject) dbo,
						Work.class);
				primaryObjectList.add(work);
			}
		}
		return primaryObjectList;
	}

	private Button createToolBar(final Composite parent) {
		Button createWorkButton = new Button(parent, SWT.PUSH);
		createWorkButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_CREATEWORK_24));
		createWorkButton.setToolTipText(Messages.get().EngineeringChangePlan_1);
		createWorkButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");//$NON-NLS-1$
		createWorkButton.setEnabled(buttonEnable);
		createWorkButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createWork();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		FormData fd = new FormData();
		createWorkButton.setLayoutData(fd);
		fd.top = new FormAttachment(0, 2);
		fd.left = new FormAttachment(0, 2);

		createDeliverableButton = new Button(parent, SWT.PUSH);
		createDeliverableButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_DELIVERABLECREATE_24));
		createDeliverableButton
				.setToolTipText(Messages.get().EngineeringChangePlan_6);
		createDeliverableButton.setData(RWT.CUSTOM_VARIANT, "whitebutton"); //$NON-NLS-1$
		createDeliverableButton.setEnabled(buttonEnable);
		createDeliverableButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				showPopup(parent);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		fd = new FormData();
		createDeliverableButton.setLayoutData(fd);
		fd.top = new FormAttachment(0, 2);
		fd.left = new FormAttachment(createWorkButton, 2);

		Button editWorkButton = new Button(parent, SWT.PUSH);
		editWorkButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_EDITWORK_24));
		editWorkButton.setToolTipText(Messages.get().EngineeringChangePlan_8);
		editWorkButton.setData(RWT.CUSTOM_VARIANT, "whitebutton"); //$NON-NLS-1$
		editWorkButton.setEnabled(buttonEnable);
		editWorkButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				editWork();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		fd = new FormData();
		editWorkButton.setLayoutData(fd);
		fd.top = new FormAttachment(0, 2);
		fd.left = new FormAttachment(createDeliverableButton, 2);

		Button deleteButton = new Button(parent, SWT.PUSH);
		deleteButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_REMOVE_24));
		deleteButton.setToolTipText(Messages.get().EngineeringChangePlan_10);
		deleteButton.setData(RWT.CUSTOM_VARIANT, "whitebutton"); //$NON-NLS-1$
		deleteButton.setEnabled(buttonEnable);
		deleteButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					remove();
				} catch (Exception exception) {
					MessageUtil.showToast(exception);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		fd = new FormData();
		deleteButton.setLayoutData(fd);
		fd.top = new FormAttachment(0, 2);
		fd.left = new FormAttachment(editWorkButton, 2);

		Button refreshButton = new Button(parent, SWT.PUSH);
		refreshButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_REFRESH_24));
		refreshButton.setToolTipText(Messages.get().EngineeringChangePlan_12);
		refreshButton.setData(RWT.CUSTOM_VARIANT, "whitebutton"); //$NON-NLS-1$
		refreshButton.setEnabled(buttonEnable);
		refreshButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshData();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		fd = new FormData();
		refreshButton.setLayoutData(fd);
		fd.top = new FormAttachment(0, 2);
		fd.left = new FormAttachment(deleteButton, 2);

		return refreshButton;
	}

	private void showPopup(final Composite parent) {
		MenuManager menuManager = new MenuManager();
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IMenuService mSvc = (IMenuService) window
				.getService(IMenuService.class);
		mSvc.populateContributionManager(menuManager, menuId);
		Menu menu = menuManager.createContextMenu(createDeliverableButton);
		// final Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
		menu.setVisible(true);
		menu.setData("treeViewer", workListCreater);
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("从项目中选择..."); //$NON-NLS-1$
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createDeliverableWithProject();
			}
		});
		menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText(Messages.get().EngineeringChangePlan_4);
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createDeliverableWithVault();
			}
		});

		Point point = createDeliverableButton.toDisplay(0,
				createDeliverableButton.getBounds().height);
		menu.setLocation(point);

	}

	protected void refreshData() {
		workListCreater.refresh();
	}

	protected void remove() throws Exception {
		IStructuredSelection sel = workListCreater.getSelection();
		if (sel != null && !sel.isEmpty()) {
			Object element = sel.getFirstElement();
			if (element instanceof Deliverable) {
				workListCreater.remove((Deliverable) element);
			} else if (element instanceof Work) {
				workListCreater.remove((Work) element);
			}
			setDirty(true);
		} else {
			MessageUtil.showToast(Messages.get().EngineeringChangePlan_14,
					SWT.ICON_WARNING);
			return;
		}

	}

	protected void editWork() {
		IStructuredSelection sel = workListCreater.getSelection();
		if (sel != null && !sel.isEmpty()) {
			Object element = sel.getFirstElement();
			if (element instanceof Work) {

				Work work = (Work) element;

				try {
					IDataObjectDialogCallback handler = new IDataObjectDialogCallback() {

						@Override
						public boolean doSaveBefore(
								PrimaryObjectEditorInput input,
								IProgressMonitor monitor, String operation)
								throws Exception {
							return true;
						}

						@Override
						public boolean doSaveAfter(
								PrimaryObjectEditorInput input,
								IProgressMonitor monitor, String operation)
								throws Exception {
							return false;
						}

						@Override
						public boolean okPressed() {
							return false;
						}

						@Override
						public void cancelPressed() {

						}

						@Override
						public boolean needSave() {
							return false;
						}
					};
					DataObjectDialog.openDialog(work, "edit.work.plan.4", //$NON-NLS-1$
							true, handler);
					workListCreater.refresh();
					setDirty(true);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
				return;
			}
		}
		MessageUtil.showToast(Messages.get().EngineeringChangePlan_16,
				SWT.ICON_WARNING);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void createDeliverableWithProject() {
		IStructuredSelection sel = workListCreater.getSelection();
		if (sel != null && !sel.isEmpty()) {
			Object element = sel.getFirstElement();
			final Work work;
			if (element instanceof Work) {
				work = (Work) element;
			} else if (element instanceof Deliverable) {
				Deliverable deliverable = (Deliverable) element;
				work = (Work) deliverable.getParentPrimaryObjectCache();
			} else {
				work = null;
			}
			if (work != null) {
				// 彈出选择

				NavigatorSelector ns = new NavigatorSelector(
						"project.documents.released", Messages.get().EngineeringChangePlan_18) { //$NON-NLS-1$
					@Override
					protected void doOK(IStructuredSelection is) {
						List docList = is.toList();
						try {
							workListCreater.createDeliverable(work, docList,
									IDeliverable.TYPE_OUTPUT);
							setDirty(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						super.doOK(is);
					}

					@Override
					protected boolean isSelectEnabled(IStructuredSelection is) {
						if (!super.isSelectEnabled(is)) {
							return false;
						}
						Object firstElement = is.getFirstElement();
						return firstElement instanceof Document;
					}
				};
				TaskForm taskform = (TaskForm) getInputData();
				try {
					Object value = taskform.getProcessInstanceVarible(
							"project", context); //$NON-NLS-1$
					Assert.isLegal(value instanceof String);
					ObjectId _id = new ObjectId((String) value);
					Project master = ModelService.createModelObject(
							Project.class, _id);
					ns.setMaster(master);
					ns.show();

				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
				return;
			}
		}
		MessageUtil.showToast(Messages.get().EngineeringChangePlan_20,
				SWT.ICON_WARNING);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void createDeliverableWithVault() {
		IStructuredSelection sel = workListCreater.getSelection();
		if (sel != null && !sel.isEmpty()) {
			Object element = sel.getFirstElement();
			final Work work;
			if (element instanceof Work) {
				work = (Work) element;
			} else if (element instanceof Deliverable) {
				Deliverable deliverable = (Deliverable) element;
				work = (Work) deliverable.getParentPrimaryObjectCache();
			} else {
				work = null;
			}
			if (work != null) {
				NavigatorSelector ns = new NavigatorSelector(
						"vault.document.selector", Messages.get().EngineeringChangePlan_22) { //$NON-NLS-1$
					@Override
					protected void doOK(IStructuredSelection is) {
						List docList = is.toList();
						try {
							workListCreater.createDeliverable(work, docList,
									IDeliverable.TYPE_OUTPUT);
							setDirty(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						super.doOK(is);
					}

					@Override
					protected boolean isSelectEnabled(IStructuredSelection is) {
						if (!super.isSelectEnabled(is)) {
							return false;
						}
						Object firstElement = is.getFirstElement();
						return firstElement instanceof Document;
					}
				};
				ns.show();
				return;
			}
		}
		MessageUtil.showToast(Messages.get().EngineeringChangePlan_23,
				SWT.ICON_WARNING);

	}

	protected void createWork() {
		NavigatorSelector ns = new NavigatorSelector(
				"organization.navigaor.projectfunctionorg", Messages.get().EngineeringChangePlan_26) { //$NON-NLS-1$

			@Override
			protected void doOK(IStructuredSelection is) {
				WorkDefinition workd = (WorkDefinition) is.getFirstElement();
				try {
					Work work = workd.makeStandloneWork(null, context);
					work.setValue(Work.F_CHARGER, context.getAccountInfo()
							.getConsignerId());
					work.setValue(Work.F_DESC, workd.getDesc());
					workListCreater.createWork(work);
					setDirty(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.doOK(is);
			}

			@Override
			protected boolean isSelectEnabled(IStructuredSelection is) {
				if (!super.isSelectEnabled(is)) {
					return false;
				}
				Object firstElement = is.getFirstElement();
				return firstElement instanceof WorkDefinition;
			}
		};
		ns.show();

	}

	@Override
	public void commit(boolean onSave) {

		TaskForm taskform = (TaskForm) getInputData();
		if (eca != null) {
			// 转换成DBObject存入到TaskForm中 2014/04/04 yangjun
			// taskform.setValue(eca, workListCreater.getInput());
			Object value = workListCreater.getInput();
			List<DBObject> ecaList = new ArrayList<DBObject>();
			if (value instanceof List<?>) {
				List<?> list = (List<?>) value;
				for (Object obj : list) {
					if (obj instanceof PrimaryObject) {
						PrimaryObject po = (PrimaryObject) obj;
						ecaList.add(po.get_data());
					}
				}
			}
			taskform.setValue(Work.F_INTERNAL_ECAPARA, eca);
			taskform.setValue(eca, ecaList);
		}
		setDirty(false);

	}

	@Override
	public void setFocus() {
		workListCreater.setFocus();
	}

}
