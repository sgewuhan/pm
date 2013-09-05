package com.sg.business.project.editor.page;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.bpm.workflow.model.NodeAssignment;
import com.sg.business.model.IProcessControlable;
import com.sg.business.model.User;
import com.sg.business.model.Work;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class WorkExecuteProcessAssignmentPage extends AbstractFormPageDelegator {

	private ProcessViewer processViewer;
	private DBObject processActors;
	private Work work;

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		work = (Work) input.getData();
		processActors = work.getProcessActorsMap(Work.F_WF_EXECUTE);
		if(processActors==null){
			processActors = new BasicDBObject();
		}
		
		processViewer = new ProcessViewer(parent, Work.F_WF_EXECUTE, work) {
			@Override
			protected void processAssignmentUpdated() {
				setDirty(true);
			}
		};
		
		DroolsProcessDefinition pd = work.getProcessDefinition(Work.F_WF_EXECUTE);
		if(pd!=null){
			List<NodeAssignment> na = pd.getNodesAssignment();
			processViewer.setInput(na);
		}
		// 创建设定执行人的列
		createProcessActorColumn();

		
		
		return processViewer.getTable();
	}

	private TableViewerColumn createProcessActorColumn() {
		TableViewerColumn column;
		column = new TableViewerColumn(processViewer, SWT.LEFT);
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
					User user = User.getUserById(userId);
					if (user != null) {
						return user.getLabel();
					}
				}
				return "";

			}

		});

		EditingSupport editingSupport = new EditingSupport(processViewer) {

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
				return User.getUserById(userid);
			}

			@Override
			protected void setValue(Object element, Object value) {
				NodeAssignment na = (NodeAssignment) element;
				String nodeActorParameter = na.getNodeActorParameter();
				if(value instanceof User){
					processActors.put(nodeActorParameter, ((User)value).getUserid());
				}else{
					processActors.removeField(nodeActorParameter);
				}
				work.setValue(Work.F_WF_EXECUTE + IProcessControlable.POSTFIX_ACTORS,
						processActors);
				setDirty(true);
				processViewer.refresh();
			}

		};
		column.setEditingSupport(editingSupport);
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
