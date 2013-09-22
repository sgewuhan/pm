package com.sg.business.commons.page;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.IProcessControlable;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.business.model.toolkit.UserToolkit;
import com.sg.widgets.ImageResource;
import com.sg.widgets.Widgets;
import com.sg.widgets.commons.editingsupport.EditorActivationStrategy;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class WorkExecuteProcessAssignmentPage extends AbstractFormPageDelegator {

	private ProcessViewer processViewer;
	private DBObject processActors;
	private Work work;
	private boolean editable;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		work = (Work) input.getData();
		editable = input.isEditable();
		IProcessControlable pc = (IProcessControlable) work
				.getAdapter(IProcessControlable.class);
		processActors = pc.getProcessActorsData(Work.F_WF_EXECUTE);
		if (processActors == null) {
			processActors = new BasicDBObject();
		}

		processViewer = new ProcessViewer(parent, Work.F_WF_EXECUTE, work,
				editable) {

			@Override
			protected void createColumns(ProcessViewer viewer) {
				createActionNameColumn(viewer);

				// createAssignmentTypeColumn();
				//
				// createRuleColumn();
				//
				// createParameterColumn();

				createActorRoleColumn(viewer);

				// 创建设定执行人的列
				createProcessActorColumn(viewer);
			}

			@Override
			protected void processAssignmentUpdated() {
				setDirty(true);
			}
		};

		DroolsProcessDefinition pd = pc.getProcessDefinition(Work.F_WF_EXECUTE);
		if (pd != null) {
			List<NodeAssignment> na = pd.getNodesAssignment();
			processViewer.setInput(na);
		}

		return processViewer.getTable();
	}

	private TableViewerColumn createProcessActorColumn(ProcessViewer viewer) {
		TableViewerColumn column;
		column = new TableViewerColumn(viewer, SWT.LEFT);
		column.getColumn().setText("执行人");
		column.getColumn().setWidth(120);
		column.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				NodeAssignment na = (NodeAssignment) element;
				String ap = na.getNodeActorParameter();
				Object value = processActors.get(ap);
				if (value instanceof String) {
					String userId = (String) value;
					User user = UserToolkit.getUserById(userId);
					if (user != null) {
						return user.getLabel();
					}
				}
				return "";

			}

		});

		if (editable) {
			EditingSupport editingSupport = new EditingSupport(viewer) {

				@Override
				protected CellEditor getCellEditor(Object element) {
					NodeAssignment na = (NodeAssignment) element;
					List<User> userList = work.getPermittedUserOfWorkflowActor(
							Work.F_WF_EXECUTE, na.getNodeActorParameter());
					ComboBoxViewerCellEditor combo = new ComboBoxViewerCellEditor(
							processViewer.getTable(), SWT.READ_ONLY);
					combo.setContentProvider(ArrayContentProvider.getInstance());
					combo.setInput(userList);
					return combo;
				}

				@Override
				protected boolean canEdit(Object element) {
					NodeAssignment na = (NodeAssignment) element;
					return na.isNeedAssignment();
				}

				@Override
				protected Object getValue(Object element) {
					NodeAssignment na = (NodeAssignment) element;
					String userid = (String) processActors.get(na
							.getNodeActorParameter());
					return UserToolkit.getUserById(userid);
				}

				@Override
				protected void setValue(Object element, Object value) {
					NodeAssignment na = (NodeAssignment) element;
					String nodeActorParameter = na.getNodeActorParameter();
					if (value instanceof User) {
						processActors.put(nodeActorParameter,
								((User) value).getUserid());
					} else {
						processActors.removeField(nodeActorParameter);
					}
					work.setValue(Work.F_WF_EXECUTE
							+ IProcessControlable.POSTFIX_ACTORS, processActors);
					setDirty(true);
					processViewer.refresh();
				}
			};
			column.setEditingSupport(editingSupport);
			column.getColumn()
					.setImage(Widgets.getImage(ImageResource.EDIT_12));

			ColumnViewerEditorActivationStrategy activationStrategy = new EditorActivationStrategy(
					viewer);
			FocusCellOwnerDrawHighlighter highlighter = new FocusCellOwnerDrawHighlighter(
					viewer);
			int feature = ColumnViewerEditor.TABBING_HORIZONTAL
					| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR;
			TableViewerFocusCellManager focusManager = new TableViewerFocusCellManager(
					(TableViewer) viewer, highlighter);
			TableViewerEditor.create((TableViewer) viewer, focusManager,
					activationStrategy, feature);
		}
		return column;

	}

	@Override
	public void commit(boolean onSave) {
		setDirty(false);
	}

	@Override
	public void setFocus() {
	}

}
