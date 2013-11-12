package com.tmt.tb.editor.page;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.IContext;
import com.mobnut.db.model.ModelService;
import com.mobnut.portal.Portal;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.sg.business.commons.ui.part.WorkListCreater;
import com.sg.business.model.Deliverable;
import com.sg.business.model.Document;
import com.sg.business.model.Project;
import com.sg.business.model.TaskForm;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.commons.selector.NavigatorSelector;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class EngineeringChangePlan extends AbstractFormPageDelegator {

	private WorkListCreater workListCreater;
	private IContext context;

	public EngineeringChangePlan() {
		// TODO Auto-generated constructor stub
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
		List<Work> workList = new ArrayList<Work>();
		try {

			Object value = taskform.getProcessInstanceVarible("ecn",
					new CurrentAccountContext());
			if (value instanceof String) {
				Object jsonValue = JSON.parse((String) value);
				if (jsonValue instanceof List<?>) {
					List<?> list = (List<?>) jsonValue;
					for (Object dbo : list) {
						Work work = ModelService.createModelObject(
								(DBObject) dbo, Work.class);
						workList.add(work);
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

		workListCreater.setInput(workList);
		return workListCreater;
	}

	private Button createToolBar(Composite parent) {
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

		Button createDeliverableButton = new Button(parent, SWT.PUSH);
		createDeliverableButton.setImage(BusinessResource
				.getImage(BusinessResource.IMAGE_DELIVERABLECREATE_24));
		createDeliverableButton.setToolTipText("添加变更对象");
		createDeliverableButton.setData(RWT.CUSTOM_VARIANT, "whitebutton");
		createDeliverableButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				createDeliverable();
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
					DataObjectDialog.openDialog(work, "edit.work.plan.4", true, null);
					workListCreater.refresh();
				} catch (Exception e) {
					MessageUtil.showToast(e);
				}
				return;
			}
		}
		MessageUtil.showToast("请选择变更计划", SWT.ICON_WARNING);
	}

	protected void createDeliverable() {
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
				// 彈出選擇

				NavigatorSelector ns = new NavigatorSelector(
						"project.documents", "选择变更对象") {
					@Override
					protected void doOK(IStructuredSelection is) {
						Document doc = (Document) is.getFirstElement();
						try {
							workListCreater.createDeliverable(work, doc);
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
		workListCreater.commit();
	}

	@Override
	public void setFocus() {
		workListCreater.setFocus();
	}

}
