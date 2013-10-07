package com.sg.business.commons;

import org.eclipse.ui.IMemento;

import com.mobnut.commons.util.Utils;

public class DirectOpenMemento implements IMemento {

	private String _id;
	private String _class;
	private String _editable;
	private String _edittype;
	private String _editor;

	public DirectOpenMemento(String _id, String _class, String _editable,
			String _edittype, String _editor) {
		this._id = _id;
		this._class = _class;
		this._editable = _editable;
		this._edittype = _edittype;
		this._editor = _editor;
	}

	@Override
	public IMemento createChild(String type) {
		return null;
	}

	@Override
	public IMemento createChild(String type, String id) {
		return null;
	}

	@Override
	public IMemento getChild(String type) {
		return null;
	}

	@Override
	public IMemento[] getChildren(String type) {
		return null;
	}

	@Override
	public Float getFloat(String key) {
		return null;
	}

	@Override
	public String getType() {
		return null;
	}

	@Override
	public String getID() {
		return null;
	}

	@Override
	public Integer getInteger(String key) {
		if(key.equals(_edittype)){
			return Utils.getIntegerValue(_edittype);
		}
		return null;
	}

	@Override
	public String getString(String key) {
		if(key.equals("id")){
			return _id;
		}else if(key.equals("editor")){
			return _editor;
		}else if(key.equals("class")){
			return _class;
		}
		return null;
	}

	@Override
	public Boolean getBoolean(String key) {
		if(key.equals("editable")){
			return "true".equalsIgnoreCase(_editable);
		}
		return null;
	}

	@Override
	public String getTextData() {
		return null;
	}

	@Override
	public String[] getAttributeKeys() {
		return null;
	}

	@Override
	public void putFloat(String key, float value) {

	}

	@Override
	public void putInteger(String key, int value) {

	}

	@Override
	public void putMemento(IMemento memento) {

	}

	@Override
	public void putString(String key, String value) {

	}

	@Override
	public void putBoolean(String key, boolean value) {

	}

	@Override
	public void putTextData(String data) {

	}

}
