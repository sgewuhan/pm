package com.sg.business.work.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.sg.business.model.Work;
import com.sg.business.resource.BusinessResource;

public class OverloadLabelProvider extends ColumnLabelProvider {


	@Override
	public Image getImage(Object element) {
		Work work = (Work)element;
		try {
			double count = work.getOverloadCount();
			if(count<0.5){//严重分配不足
				return BusinessResource.getImage(BusinessResource.IMAGE_BALL_GRAY_1_16);
			}else if(count>=0.5&&count<8){//分配不足
				return BusinessResource.getImage(BusinessResource.IMAGE_BALL_BLUE_1_16);
			}else if(count>=0.8 && count< 1.5){//基本正常
				return BusinessResource.getImage(BusinessResource.IMAGE_BALL_GREEN_1_16);
			}else if(count>=1.5 && count<2){//超量分配
				return BusinessResource.getImage(BusinessResource.IMAGE_BALL_YELLOW_1_16);
			}else{//严重超量
				return BusinessResource.getImage(BusinessResource.IMAGE_BALL_RED_1_16);
			}
		} catch (Exception e) {
		}
		return null;
	}
	
}
