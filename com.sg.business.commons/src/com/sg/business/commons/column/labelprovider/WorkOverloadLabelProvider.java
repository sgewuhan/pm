package com.sg.business.commons.column.labelprovider;

import org.eclipse.swt.graphics.Image;

import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;
import com.sg.widgets.commons.labelprovider.ConfiguratorColumnLabelProvider;

public class WorkOverloadLabelProvider extends ConfiguratorColumnLabelProvider {


	@Override
	public String getText(Object element) {
		return ""; //$NON-NLS-1$
	}
	
	public Image getImage(Object element) {
		Work work = (Work) element;
		try {
			double count = work.getOverloadCount();
			if (count < 0.5) {// ���ط��䲻��
				return BusinessResource
						.getImage(BusinessResource.IMAGE_BALL_GRAY_1_16);
			} else if (count >= 0.5 && count < 8) {// ���䲻��
				return BusinessResource
						.getImage(BusinessResource.IMAGE_BALL_BLUE_1_16);
			} else if (count >= 0.8 && count < 1.5) {// ��������
				return BusinessResource
						.getImage(BusinessResource.IMAGE_BALL_GREEN_1_16);
			} else if (count >= 1.5 && count < 2) {// ��������
				return BusinessResource
						.getImage(BusinessResource.IMAGE_BALL_YELLOW_1_16);
			} else {// ���س���
				return BusinessResource
						.getImage(BusinessResource.IMAGE_BALL_RED_1_16);
			}
		} catch (Exception e) {
		}
		return null;
	}

}
