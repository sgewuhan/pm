package com.sg.business.commons.page;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.mobnut.db.model.DocumentModelDefinition;
import com.mobnut.db.model.ModelService;
import com.mobnut.db.model.PrimaryObject;
import com.mongodb.DBObject;
import com.sg.business.model.IReferenceContainer;
import com.sg.widgets.MessageUtil;
import com.sg.widgets.UIConstants;
import com.sg.widgets.part.editor.DataObjectDialog;
import com.sg.widgets.part.editor.DataObjectEditor;
import com.sg.widgets.part.editor.DataObjectWizard;
import com.sg.widgets.part.editor.PrimaryObjectEditorInput;
import com.sg.widgets.part.editor.page.AbstractFormPageDelegator;
import com.sg.widgets.registry.config.BasicPageConfigurator;

public class ReferenceListPage extends AbstractFormPageDelegator {

	@Override
	public Composite createPageContent(Composite parent,
			PrimaryObjectEditorInput input, BasicPageConfigurator conf) {
		IReferenceContainer message = (IReferenceContainer) input.getData();
		Composite content = new Composite(parent,SWT.NONE);
		content.setLayout(new FillLayout());
		BasicBSONList targetList = message.getTargetList();
		TableViewer viewer = new TableViewer(content,SWT.FULL_SELECTION);
		viewer.getTable().setLinesVisible(true);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof DBObject){
					DBObject dbObject = (DBObject) element;
					String targetClass = (String) dbObject.get(IReferenceContainer.SF_TARGET_CLASS);
					ObjectId targetId = (ObjectId) dbObject.get(IReferenceContainer.SF_TARGET);
					DocumentModelDefinition md = ModelService.getDocumentModelDefinition(targetClass);
					PrimaryObject po = ModelService.createModelObject(md.getModelClass(), targetId);
					String typeName = po.getTypeName();
					if(typeName!=null&&typeName.length()>0){
						return typeName+":" +po.getLabel(); //$NON-NLS-1$
					}else{
						return po.getLabel();
					}
				}
				return ""; //$NON-NLS-1$
			}
			
			@Override
			public Image getImage(Object element){
				if(element instanceof DBObject){
					DBObject dbObject = (DBObject) element;
					String targetClass = (String) dbObject.get(IReferenceContainer.SF_TARGET_CLASS);
					ObjectId targetId = (ObjectId) dbObject.get(IReferenceContainer.SF_TARGET);
					DocumentModelDefinition md = ModelService.getDocumentModelDefinition(targetClass);
					PrimaryObject po = ModelService.createModelObject(md.getModelClass(), targetId);
					return po.getImage();
				}
				return null;
			}
		});
		viewer.setInput(targetList);
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection is = (IStructuredSelection) event.getSelection();
				if(is!=null&&!is.isEmpty()){
					DBObject dbObject = (DBObject)is.getFirstElement();
					openTarget(dbObject);
				}
			}
		});
		return content;
	}

	protected void openTarget(DBObject dbObject) {
		String targetClass = (String) dbObject.get(IReferenceContainer.SF_TARGET_CLASS);
		String editorId = (String) dbObject.get(IReferenceContainer.SF_TARGET_EDITOR);
		boolean editable = Boolean.TRUE.equals(dbObject.get(IReferenceContainer.SF_TARGET_EDITABLE));
		ObjectId targetId = (ObjectId) dbObject.get(IReferenceContainer.SF_TARGET);
		Integer type = (Integer) dbObject.get(IReferenceContainer.SF_TARGET_EDITING_TYPE);
		DocumentModelDefinition md = ModelService.getDocumentModelDefinition(targetClass);
		PrimaryObject po = ModelService.createModelObject(md.getModelClass(), targetId);
		try {
			if(type==null||type.intValue()==UIConstants.EDITING_BY_EDITOR){
				DataObjectEditor.open(po, editorId, editable, null);
			}else if(type.intValue()==UIConstants.EDITING_BY_DIALOG){
				DataObjectDialog.openDialog(po, editorId, editable, null);
			}else if(type.intValue()==UIConstants.EDITING_BY_WIZARD){
				DataObjectWizard.open(po, editorId, editable, null);
			}
		} catch (Exception e) {
			MessageUtil.showToast(e);
		}
	}

	@Override
	public void commit(boolean onSave) {
	}

	@Override
	public void setFocus() {
		
	}


}
