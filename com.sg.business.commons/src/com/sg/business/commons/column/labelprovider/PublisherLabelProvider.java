package com.sg.business.commons.column.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.sg.business.model.BulletinBoard;

/**
 * 发布人标签
 * @author gdiyang
 *
 */
public class PublisherLabelProvider extends ColumnLabelProvider {

	/**
	 * 返回当前对象的发布人标签
	 */
	@Override
	public String getText(Object element) {
		BulletinBoard po = (BulletinBoard)element;
		return po.getPublisherLabel();
	}

}
