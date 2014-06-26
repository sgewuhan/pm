package com.sg.business.performence.model;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Work;

public abstract class WorksNode extends PrimaryObject{
	
	private WorksNode parent;
	
	private PrimaryObject data;
	
	private List<WorksNode> children=new ArrayList<WorksNode>();

	private ObjectId id;

	public WorksNode(WorksNode parent,PrimaryObject data) {
		setParent(parent);
		setData(data);
		id = data.get_id();
	}
	
	public WorksNode getParent() {
		return parent;
	}

	public void setParent(WorksNode parent) {
		this.parent = parent;
	}

	public List<WorksNode> getChildren() {
		return children;
	}

	public void setChildren(List<WorksNode> children) {
		this.children = children;
	}
	
	public void addChild(WorksNode child){
		children.add(child);
	}

	public String getAdditionInfomation() {
		return "";
	}

	public String getLabel() {
		return getData().getLabel();
	}
	
	public double getWorks(){
		double summary = 0d;
		List<WorksNode> children = getChildren();
		if(children!=null&&!children.isEmpty()){
			for (int i = 0; i < children.size(); i++) {
				WorksNode child = children.get(i);
				summary+=child.getWorks();
			}
		}else{
			PrimaryObject data = getData();
			if(data instanceof Work){
				Double value=((Work) data).getActualWorks();
				if(value!=null){
					summary=value.doubleValue();
				}
			}
		}
		return summary;
	}

	public PrimaryObject getData() {
		return data;
	}

	public void setData(PrimaryObject data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorksNode other = (WorksNode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
}
