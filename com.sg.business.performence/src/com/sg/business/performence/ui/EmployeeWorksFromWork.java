package com.sg.business.performence.ui;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

import com.mobnut.commons.util.Utils;
import com.mobnut.db.model.PrimaryObject;
import com.mobnut.design.ICSSConstants;
import com.sg.business.model.DummyWork;
import com.sg.business.model.Work;
import com.sg.business.performence.model.EmployeeWorksDataSet;
import com.sg.business.performence.model.WorkWorksNode;
import com.sg.business.performence.model.WorksNode;
import com.sg.business.resource.nls.Messages;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.part.CurrentAccountContext;
import com.sg.widgets.part.IRefreshablePart;
import com.sg.widgets.part.editor.DataObjectDialog;

public class EmployeeWorksFromWork extends ViewPart implements IRefreshablePart {

	// public class WorkEditingSupport extends EditingSupport {

	/*
	 * private double works;
	 * 
	 * public WorkEditingSupport(double works) { super(treeViewer);
	 * this.works=works; }
	 * 
	 * @Override protected CellEditor getCellEditor(Object element) { return
	 * editor; }
	 * 
	 * @Override protected boolean canEdit(Object element) { WorksNode
	 * worksNode=(WorksNode) element; return !worksNode.getChildren().isEmpty();
	 * }
	 * 
	 * @Override protected Object getValue(Object element) {
	 * worksNode=(WorksNode) element; double works = worksNode.getWorks();
	 * return works; }
	 * 
	 * @Override protected void setValue(Object element, Object value) {
	 * 
	 * }
	 */
	// }

	private EmployeeWorksDataSet dataSet;
	private TreeViewer treeViewer;
	private CurrentAccountContext context;
	private int year;
	private int month;
	private boolean onlyProjectWorks;
	private boolean onlyOwnerDepartmentWorks;
	private Label label;
	private Locale locale;
	private Action menuEdit;

	public EmployeeWorksFromWork() {
	}

	@Override
	public void createPartControl(Composite parent) {
		// 默认

		context = new CurrentAccountContext();
		locale = RWT.getLocale();
		Calendar today = Calendar.getInstance();
		today.set(Calendar.DATE, 1);
		today.add(Calendar.MONTH, -1);
		int year = today.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH);
		setInput(year, month);
		setOnlyProjectWorks(false);
		setOnlyOwnerDepartmentWorks(false);

		Composite composite = new Composite(parent, SWT.NONE);

		composite.setLayout(new FormLayout());

		Control control = createLabel(composite);
		FormData fd = new FormData();
		control.setLayoutData(fd);
		fd.top = new FormAttachment(0, 10);
		fd.left = new FormAttachment(0);
		fd.right = new FormAttachment(100);
		fd.height = 32;

		Control control2 = createTreeView(composite);
		fd = new FormData();
		control2.setLayoutData(fd);
		fd.top = new FormAttachment(control, 10);
		fd.left = new FormAttachment();
		fd.right = new FormAttachment(100);
		fd.bottom = new FormAttachment(100);

		doRefresh();
	}

	public void setOnlyOwnerDepartmentWorks(boolean b) {
		onlyOwnerDepartmentWorks = b;
	}

	public void setOnlyProjectWorks(boolean b) {
		onlyProjectWorks = b;
	}

	public boolean isOnlyProjectWorks() {
		return onlyProjectWorks;
	}

	public boolean isOnlyOwnerDepartmentWorks() {
		return onlyOwnerDepartmentWorks;
	}

	private Control createLabel(Composite parent) {
		label = new Label(parent, SWT.None);
		label.setData(RWT.CUSTOM_VARIANT, ICSSConstants.METRO_GRAY_ACTIVE);
		return label;
	}

	private Control createTreeView(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		// 1.第一列，显示名称
		TreeViewerColumn vColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		vColumn.getColumn().setWidth(200);
		vColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((WorksNode) element).getLabel();
			}
		});
		// 2. 第二列，显示附加信息
		vColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
		vColumn.getColumn().setWidth(200);
		vColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((WorksNode) element).getAdditionInfomation();
			}
		});
		// 3. 第三列，显示工时
		vColumn = new TreeViewerColumn(treeViewer, SWT.NONE | SWT.RIGHT);
		vColumn.getColumn().setWidth(100);
		vColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				double works = ((WorksNode) element).getWorks();
				DecimalFormat df = new DecimalFormat(Utils.NF_NUMBER_P2);
				return df.format(works);
			}
		});
		// 2014.6.25 工时统计员可以修改实际工时
		createActions();
		Tree tree = treeViewer.getTree();
		MenuManager menuManager = new MenuManager("popup");
		menuManager.createContextMenu(tree);
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				IStructuredSelection sel = (IStructuredSelection) treeViewer
						.getSelection();
				if (sel != null && !sel.isEmpty()) {
					Object element = sel.getFirstElement();
					if (element instanceof WorkWorksNode) {
						manager.add(menuEdit);
					}
				}
			}
		});

		tree.setMenu(menuManager.getMenu());

		/*
		 * context = new CurrentAccountContext(); String userId =
		 * context.getAccountInfo().getConsignerId(); User user =
		 * UserToolkit.getUserById(userId); List<PrimaryObject> orgs =
		 * user.getRoleGrantedInFunctionDepartmentOrganization
		 * (Role.ROLE_WORKS_STATISTICS_ID); boolean isWorksAdmin=false;
		 * if(orgs!=null){ isWorksAdmin=true; if(isWorksAdmin){
		 * vColumn.setEditingSupport(new WorkEditingSupport(get)); } }
		 */

		// 4. content provider
		treeViewer.setContentProvider(new ITreeContentProvider() {

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean hasChildren(Object element) {
				return !((WorksNode) element).getChildren().isEmpty();
			}

			@Override
			public Object getParent(Object element) {
				return ((WorksNode) element).getParent();
			}

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof EmployeeWorksDataSet) {
					return ((EmployeeWorksDataSet) inputElement).getNodes()
							.toArray();
				}
				return new Object[0];
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return ((WorksNode) parentElement).getChildren().toArray();
			}
		});

		return treeViewer.getControl();
	}

	private void createActions() {
		// 2014.6.23 参数编辑菜单
		menuEdit = new Action(Messages.get().EditWork_6) {
			@Override
			public void run() {
				IStructuredSelection sel = (IStructuredSelection) treeViewer
						.getSelection();
				if (sel != null && !sel.isEmpty()) {
					Object element = sel.getFirstElement();
					if (element instanceof WorkWorksNode) {
						WorkWorksNode worksNode = (WorkWorksNode) element;
						PrimaryObject po = worksNode.getData();
						if (po instanceof Work) {
							Work work = (Work) po;
							DummyWork dummyWork = work
									.getAdapter(DummyWork.class);
							try {
								DataObjectDialog.openDialog(dummyWork,
										"work.employeeworks", true, null);
								treeViewer.refresh();
							} catch (Exception e) {
								MessageUtil.showMessage(treeViewer.getControl()
										.getShell(), "异常", "请选择工作进行编辑",
										SWT.NONE);
							}
						}
					}
				}
			}
		};
	}

	@Override
	public void setFocus() {

	}

	public void setInput(int year, int month) {
		this.year = year;
		this.month = month;
	}

	public void expandAll() {
		treeViewer.expandAll();
	}

	public void collapse() {
		treeViewer.collapseAll();
	}

	@Override
	public boolean canRefresh() {
		return true;
	}

	@Override
	public void doRefresh() {
		// 设置标签的显示
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		String _month = cal.getDisplayName(Calendar.MONTH, Calendar.LONG,
				locale);

		StringBuffer sb = new StringBuffer();
		sb.append(year);
		sb.append("  ");
		sb.append(_month);
		if (isOnlyOwnerDepartmentWorks()) {
			sb.append(Messages.get().OnlyOwnerDepartmentWorks);
		}

		if (isOnlyProjectWorks()) {
			sb.append(Messages.get().OnlyProjectWorks);
		}
		label.setText(sb.toString());

		// 设置数据的显示
		String userId = context.getConsignerId();
		dataSet = EmployeeWorksDataSet.getInstance(year, month,
				onlyProjectWorks, onlyOwnerDepartmentWorks, userId);
		treeViewer.setInput(dataSet);
	}

	public void doExport() {

	}

}
