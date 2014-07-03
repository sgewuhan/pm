package com.sg.business.commons.column.labelprovider;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;

import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.IWorkCloneFields;
import com.sg.business.model.Project;
import com.sg.business.model.ProjectTemplate;
import com.sg.business.model.Work;
import com.sg.business.model.WorkDefinition;
import com.sg.business.model.WorkTimeProgram;
import com.sg.widgets.viewer.CTreeViewer;

public class ParaXLabelProvider extends ColumnLabelProvider {

	private CTreeViewer viewer;

	public ParaXLabelProvider() {
	}

	@Override
	public String getText(Object element) {
		if (element instanceof PrimaryObject) {
			PrimaryObject po = (PrimaryObject) element;
			return getParaXLabel(po);
		}

		return "";
	}

	private String getParaXLabel(PrimaryObject po) {
		String result = "";
		BasicBSONList paraXs = (BasicBSONList) po
				.getValue(IWorkCloneFields.F_WORKTIME_PARAX);
		if (paraXs != null) {
			for (int i = 0; i < paraXs.size(); i++) {
				String workTimeLabel = getWorkTimeLabel((DBObject) paraXs
						.get(i));
				if (workTimeLabel != null) {
					if (result.isEmpty()) {
						result += workTimeLabel;
					} else if (i == 1) {
						result += "," + workTimeLabel;
					} else if (i == 2) {
						result += "..";
						break;
					}
				}
			}
		}
		return result;
	}

	private String getWorkTimeLabel(DBObject paraX) {
		ObjectId typeId = (ObjectId) paraX
				.get(IWorkCloneFields.F_WORKTIME_PARAX_ID);
		ObjectId programId = (ObjectId) paraX
				.get(IWorkCloneFields.F_WORKTIME_PARAX_PROGRAM_ID);
		WorkTimeProgram program = ModelService.createModelObject(
				WorkTimeProgram.class, programId);
		String programDesc = program.getDesc();
		BasicBSONList paraXList = (BasicBSONList) program
				.getValue(WorkTimeProgram.F_WORKTIME_PARA_X);
		for (int j = 0; j < paraXList.size(); j++) {
			ObjectId paraXId = (ObjectId) ((DBObject) paraXList.get(j))
					.get(WorkTimeProgram.F__ID);
			if (paraXId.equals(typeId)) {
				String paraXDesc = (String) ((DBObject) paraXList.get(j))
						.get(WorkTimeProgram.F_DESC);
				return programDesc + "|" + paraXDesc;
			}
		}
		return null;
	}

	@Override
	protected void initialize(ColumnViewer viewer, ViewerColumn column) {
		this.viewer = (CTreeViewer) viewer;
		super.initialize(viewer, column);
	}

	@Override
	public void update(ViewerCell cell) {

		Object element = cell.getElement();
		PrimaryObject master = viewer.getViewerControl().getMaster();
		try {
			if (element instanceof WorkDefinition) {
				((WorkDefinition) element).workTimeValidate((ProjectTemplate) master);
			}else if(element instanceof Work){
//				if(((Work) element).getDesc().equals("签订技术协议")){
//					System.out.println();
//				}
				((Work) element).workTimeValidate((Project) master);
			}
			cell.setText(getText(element));
		} catch (Exception e) {
			cell.setText("N/A");
			cell.setBackground(cell.getControl().getDisplay()
					.getSystemColor(SWT.COLOR_RED));
		}
	}

	@Override
	public String getToolTipText(Object element) {
		try {
			PrimaryObject master = viewer.getViewerControl().getMaster();
			if(element instanceof WorkDefinition){
				((WorkDefinition) element)
				.workTimeValidate((ProjectTemplate) master);
			}else if(element instanceof Work){
				((Work) element).workTimeValidate((Project) master);
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		return super.getToolTipText(element);
	}
}
