package com.tmt.tb.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.jbpm.task.Task;

import com.mobnut.db.DBActivator;
import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.portal.Portal;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.sg.business.commons.ui.part.WorkListCreater;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.IModelConstants;
import com.sg.business.model.Organization;
import com.sg.business.model.OrganizationDistributeFileBase;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.IDataObjectDialogCallback;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;
import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.handler.PDMObjectSelector;
import com.tmt.pdm.dcpdm.sync.ImportData;

import dyna.framework.service.dos.DOSChangeable;

public class EngineeringChangePlan extends AbstractFormPageDelegator {

	private WorkListCreater workListCreater;
	private IContext context;
	private String eca;

	public EngineeringChangePlan() {
	}

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {

		context = new CurrentAccountContext();
		setFormInput(input);
		parent.setLayout(new FormLayout());
		Button b = createToolBar(parent);
		workListCreater = new WorkListCreater(parent, SWT.BORDER);
		workListCreater.setContext(context);

		TaskForm taskform = (TaskForm) input.getData();
		List<Work> ecaList = new ArrayList<Work>();
		Object value;
		try {
			Task task = taskform.getExecuteTask(context);
			String taskName = task.getNames().get(0).getText();
			List<Work> ecnList = new ArrayList<Work>();
			value = taskform.getProcessInstanceVarible("ecn", context);

			if (value instanceof List<?>) {
				List<?> list = (List<?>) value;
				for (Object dbo : list) {
					Work work = ModelService.createModelObject((DBObject) dbo,
							Work.class);
					ecnList.add(work);
				}
			} else if (value instanceof Object[]) {
				Object[] array = (Object[]) value;
				for (Object dbo : array) {
					Work work = ModelService.createModelObject((DBObject) dbo,
							Work.class);
					ecnList.add(work);
				}

			}
			for (Work ecn : ecnList) {
				if (taskName != null && taskName.equals(ecn.getDesc())) {
					eca = (String) ecn.getValue(Work.F_INTERNAL_ECAPARA);
					value = taskform.getProcessInstanceVarible(eca, context);
					if (value instanceof List<?>) {
						List<?> list = (List<?>) value;
						for (Object dbo : list) {
							Work work = ModelService.createModelObject(
									(DBObject) dbo, Work.class);
							ecnList.add(work);
						}
					} else if (value instanceof Object[]) {
						Object[] array = (Object[]) value;
						for (Object dbo : array) {
							Work work = ModelService.createModelObject(
									(DBObject) dbo, Work.class);
							ecnList.add(work);
						}
					}
				}
			}

		} catch (Exception e) {
			if (Portal.getDefault().isDevelopMode()) {
				e.printStackTrace();
			}
		}

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

	private Button createToolBar(final Composite parent) {
		Button createWorkButton = new Button(parent, SWT.PUSH);
		createWorkButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_CREATEWORK_24));
		createWorkButton.setToolTipText("添加变更计划");
		createWorkButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");
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

		// Button createDeliverableButton = new Button(parent, SWT.PUSH);
		// createDeliverableButton.setImage(BusinessResource
		// .getImage(BusinessResource.IMAGE_DELIVERABLECREATE_24));
		// createDeliverableButton.setToolTipText("添加变更对象");
		// createDeliverableButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");
		// createDeliverableButton.addSelectionListener(new SelectionListener()
		// {
		//
		// @Override
		// public void widgetSelected(SelectionEvent e) {
		// createDeliverable();
		// }
		//
		// @Override
		// public void widgetDefaultSelected(SelectionEvent e) {
		//
		// }
		// });
		// fd = new FormData();
		// createDeliverableButton.setLayoutData(fd);
		// fd.top = new FormAttachment(0, 2);
		// fd.left = new FormAttachment(createWorkButton, 2);

		final Menu menu = new Menu(parent.getShell(), SWT.POP_UP);
		MenuItem menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("从项目中选择...");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createDeliverableWithProject();
			}
		});
		menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("从文档库中选择...");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createDeliverableWithVault();
			}
		});

		menuItem = new MenuItem(menu, SWT.NONE);
		menuItem.setText("链接PDM...");
		menuItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createDeliverableWithPDM(parent);
			}

		});

		final Button createDeliverableButton = new Button(parent, SWT.PUSH);
		createDeliverableButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_DELIVERABLECREATE_24));
		createDeliverableButton.setToolTipText("添加变更对象");
		createDeliverableButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");
		createDeliverableButton.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Point point = createDeliverableButton.toDisplay(0,
						createDeliverableButton.getBounds().height);
				menu.setLocation(point);
				menu.setVisible(true);
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
		editWorkButton.setToolTipText("编辑变更计划");
		editWorkButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");
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
		deleteButton.setToolTipText("删除");
		deleteButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");
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
		refreshButton.setToolTipText("刷新");
		refreshButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");
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
			MessageUtil.showToast("请选择删除的对象", SWT.ICON_WARNING);
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
					DataObjectDialog.openDialog(work, "edit.work.plan.4",
							false, handler);
					workListCreater.refresh();
					setDirty(true);
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
				return;
			}
		}
		MessageUtil.showToast("请选择变更计划", SWT.ICON_WARNING);
	}

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
						"project.documents.released", "选择变更对象") {
					@Override
					protected void doOK(IStructuredSelection is) {
						Document doc = (Document) is.getFirstElement();
						try {
							workListCreater.createDeliverable(work, doc);
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
							"project", context);
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
		MessageUtil.showToast("请选择变更计划", SWT.ICON_WARNING);

	}

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
						"vault.document.selector", "选择变更对象") {
					@Override
					protected void doOK(IStructuredSelection is) {
						Document doc = (Document) is.getFirstElement();
						try {
							workListCreater.createDeliverable(work, doc);
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
		MessageUtil.showToast("请选择变更计划", SWT.ICON_WARNING);

	}

	protected void createDeliverableWithPDM(Composite parent) {

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
				List<?> docContainer = getDocumentAndDrawingContainerCode();
				List<?> partContainer = getPartContainerCode();
				if (docContainer == null) {
					MessageUtil.showToast("您所在的组织尚未确定PDM系统中可使用图文档容器。",
							SWT.ICON_ERROR);
					return;
				}
				PDMObjectSelector selector = new PDMObjectSelector(parent.getShell(),
						docContainer, partContainer);
				int ret = selector.open();
				if (ret == PDMObjectSelector.OK) {
					String[] select = selector.getSelection();
					if (select.length == 0) {
						return;
					} else {
						Document doc = getDocument(select[0]);
						workListCreater.createDeliverable(work, doc);
						setDirty(true);
					}
				}

			}
           return;
		}
		MessageUtil.showToast("请选择变更计划", SWT.ICON_WARNING);
	}

	protected void createWork() {
		NavigatorSelector ns = new NavigatorSelector(
				"organization.navigaor.projectfunctionorg", "选择变更计划") {

			@Override
			protected void doOK(IStructuredSelection is) {
				WorkDefinition workd = (WorkDefinition) is.getFirstElement();
				try {
					Work work = workd.makeStandloneWork(null, context);
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
			taskform.setValue(eca, workListCreater.getInput());
		}
		setDirty(false);

	}

	@Override
	public void setFocus() {
		workListCreater.setFocus();
	}

	private List<?> getDocumentAndDrawingContainerCode() {
		String userId = new CurrentAccountContext().getConsignerId();
		User user = UserToolkit.getUserById(userId);

		Organization org = user.getOrganization();

		while (org != null) {
			List<?> code = org.getDocumentAndDrawingContainerCode();
			if (code != null) {
				return (List<?>) code;
			} else {
				org = (Organization) org.getParentOrganization();
			}
		}
		return null;
	}

	private List<?> getPartContainerCode() {
		String userId = new CurrentAccountContext().getConsignerId();
		User user = UserToolkit.getUserById(userId);

		Organization org = user.getOrganization();

		while (org != null) {
			List<?> code = org.getPartContainerCode();
			if (code != null) {
				return (List<?>) code;
			} else {
				org = (Organization) org.getParentOrganization();
			}
		}
		return null;
	}

	private Document getDocument(String ouid) {
		Document document = null;
		try {
			DOSChangeable pdmObject = Starter.dos.get(ouid);
			boolean buildDocument = true;
			String documentNumber = (String) pdmObject.get("md$number");
			if (documentNumber != null) {
				DBCollection col = DBActivator.getCollection(
						IModelConstants.DB, IModelConstants.C_DOCUMENT);
				DBObject dbo = col.findOne(new BasicDBObject().append(
						Document.F_DOCUMENT_NUMBER, documentNumber));
				if (dbo != null) {
					document = ModelService.createModelObject(dbo,
							Document.class);
					buildDocument = false;
					return document;
				}
			}
			if (buildDocument) {
				ImportData ip = new ImportData() {
					@Override
					protected String getNamespace() {
						return "vault_file";
					}

					@Override
					protected DB getDB() {
						OrganizationDistributeFileBase filebase = new OrganizationDistributeFileBase();
						return DBActivator.getDB(filebase.getDB());
					}

					@Override
					protected String getClassOuid() {
						return null;
					}
				};

				ip.syncItem(ouid, document);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return document;
	}
}
