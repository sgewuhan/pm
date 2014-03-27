package com.tmt.pdm.dcpdm.selector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.tmt.pdm.client.Starter;

import dyna.framework.service.dos.DOSChangeable;

public class RelationDCDPMObjectContentProvider implements ITreeContentProvider {

	private Set<String> allContainers;

	public RelationDCDPMObjectContentProvider(Set<String> allContainers) {
		this.allContainers = allContainers;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return (Object[]) inputElement;
	}

	@Override
	public Object[] getChildren(Object parent) {
		DOSChangeable data = (DOSChangeable) parent;
		String ouid = (String) data.get("ouid");
		Set<String> resultOuids = new HashSet<String>();
		try {
			String classOuid = Starter.dos.getClassOuid(ouid);
			ArrayList<?> asso = Starter.dos.listAssociationOfClass(classOuid);
			for (Iterator<?> iterator = asso.iterator(); iterator.hasNext();) {
				DOSChangeable dos = (DOSChangeable) iterator.next();
				String end1Class = (String) dos.get("end1.ouid@class");
				String end2Class = (String) dos.get("end2.ouid@class");
				String assClass = (String) dos.get("ouid@class");
				if (classOuid.equals(end1Class)
						&& allContainers.contains(end2Class)) {// 包含在范围内的
					HashMap<String, String> filter = new HashMap<String, String>();
					filter.put("list.mode", "aggregation"); // you must set this
															// parameter
					filter.put("version.condition.type", "wip"); // you must set
																	// this
																	// parameter
					filter.put("ouid@association.class", assClass); // you can
																	// find
																	// association
																	// ouid in
																	// OM
					Starter.dos.setWorkingModel("80001764");
					ArrayList<?> result = Starter.dos
							.listLinkFrom(ouid, filter);
					if (result != null) {
						for (int i = 0; i < result.size(); i++) {
							ArrayList<?> item = (ArrayList<?>) result.get(i);
							if (item != null&&item.size()>0) {
								String subItemOuid = (String) item.get(0);
								resultOuids.add(subItemOuid);
							}
						}
					}
				}
			}
			Object[] children = new Object[resultOuids.size()];
			int i = 0;
			Iterator<String> iter = resultOuids.iterator();
			while (iter.hasNext()) {
				String childOuid = (String) iter.next();
				children[i] = Starter.dos.get(childOuid);
				i++;
			}
			return children;
		} catch (Exception e) {
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return true;
	}

}
