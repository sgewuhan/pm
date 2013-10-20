package com.sg.business.commons.ui.flow;

import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;

import com.sg.business.commons.flow.model.ActivityDiagram;
import com.sg.business.commons.flow.parts.ActivityPartFactory;

public class ProcessViewer extends ScrollingGraphicalViewer {

	public ProcessViewer() {
		setRootEditPart(new ScalableRootEditPart());
		setEditPartFactory(new ActivityPartFactory());
	}
	
	public void setInput(){
		ActivityDiagram diagram = new ActivityDiagram();
		setContents(diagram);
	}
	
	
}
