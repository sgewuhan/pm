package com.sg.bpm.workflow.utils;

import java.util.Collection;

import org.drools.KnowledgeBase;
import org.drools.definition.process.Process;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.mobnut.commons.util.Utils;
import com.sg.bpm.service.BPM;


public class ProcessSelectorDialog extends Dialog implements ISelectionChangedListener {

	
	private Process selection;
	private TableViewer viewer;
	private String kname;

	public ProcessSelectorDialog(Shell shell,String kname) {
		super(shell);
		this.kname = kname;
	}

	
	@Override
	protected Control createContents(Composite parent) {
		getShell().setText("ÇëÑ¡ÔñÁ÷³Ì...");
		parent.setLayout(new GridLayout());
		
		
		viewer = new TableViewer(parent, SWT.FULL_SELECTION|SWT.BORDER);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				if(element instanceof Process){
					return ((Process) element).getName()+"-"+((Process) element).getId();
				}
				return super.getText(element);
			}
			
		});
		viewer.addSelectionChangedListener(this);
		
		KnowledgeBase kbase = BPM.getBPMService().getKnowledgeBase(kname);
		Collection<Process> processList = kbase.getProcesses();
		viewer.setInput(processList);
		
		GridData gd = new GridData(SWT.FILL,SWT.FILL,true,true);
		gd.heightHint = 400;
		gd.widthHint = 400;
		viewer.getControl().setLayoutData(gd);

		
		return super.createContents(parent);
	}
	

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection isel = event.getSelection();
		if(Utils.isNullOrEmpty(isel)){
			selection = null;
		}else{
			selection = (Process)((IStructuredSelection)isel).getFirstElement();
		}
	}

	public Process getSelection(){
		return selection;
	}

}
