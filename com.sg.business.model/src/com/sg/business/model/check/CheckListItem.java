package com.sg.business.model.check;

import com.mobnut.db.model.PrimaryObject;

public class CheckListItem implements ICheckListItem{

	private String title;
	private String message;
	private int type;
	private String key;
	private PrimaryObject selection;
	private String editorId;
	private String pageId;
	private Object data;

	public CheckListItem(String title, String message, int type) {
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

	public void setData(Object data) {
		this.data = data;
	}
	
	public Object getData(){
		return data;
	}

	
	
}
