package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.BulletinBoard;

/**
 * �����˱�ǩ
 * @author gdiyang
 *
 */
public class PublisherLabelProvider extends ColumnLabelProvider {

	/**
	 * ���ص�ǰ����ķ����˱�ǩ
	 */
	@Override
	public String getText(Object element) {
		BulletinBoard po = (BulletinBoard)element;
		return po.getPublisherLabel();
	}

}
