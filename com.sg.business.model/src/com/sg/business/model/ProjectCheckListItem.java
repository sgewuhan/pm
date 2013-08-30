package com.sg.business.model;

import com.mobnut.db.model.PrimaryObject;

public class ProjectCheckListItem implements ICheckListItem{

	private String title;
	private String message;
	private int type;
	private Project project;
	private String key;
	private PrimaryObject selection;
	private String editorId;
	private String pageId;

	public ProjectCheckListItem(String title, String message, int type) {
		this.title = title;
		this.message = message;
		this.type = type;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public int getType() {
		return type;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setEditorId(String editorId) {
		this.editorId = editorId;
	}

	public void setSelection(PrimaryObject selection) {
		this.selection = selection;
	}

	public String getTitle() {
		return title;
	}

	public Project getProject() {
		return project;
	}

	public String getKey() {
		return key;
	}

	public PrimaryObject getSelection() {
		return selection;
	}

	public String getEditorId() {
		return editorId;
	}
	
	public String getPageId(){
		return pageId;
	}

	public void setEditorPageId(String pageId) {
		this.pageId = pageId;
	}

	
	
}
