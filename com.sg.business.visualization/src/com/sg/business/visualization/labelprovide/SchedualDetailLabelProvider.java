package com.sg.business.visualization.labelprovide;

import java.util.Date;
import java.util.List;

import com.sg.business.model.ILifecycle;
import com.sg.business.model.Project;
import com.sg.business.model.Work;

public class SchedualDetailLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		if(ILifecycle.STATUS_FINIHED_VALUE.equals(project.getLifecycleStatus())){
			return "";
		}
		
		List<Work> works = project.getMileStoneWorks();
		//取正在进行的为中心，前后各一行
		int index = -1;
		for(int i=0;i<works.size();i++){
			Work work = works.get(i);
			if(ILifecycle.STATUS_WIP_VALUE.equals(work.getLifecycleStatus())){
				index  = i;
				break;
			}
		}
		
		String line1;
		String line2;
		String line3;
		
		if(index==-1){
			return "";
		}else if(index==0){
			line1 = getWorkLabel(works.get(index),true);
			line2 = works.size()>1?getWorkLabel(works.get(1),false):"";
			line3 = works.size()>2?getWorkLabel(works.get(2),false):"";
		}else{
			line1 = getWorkLabel(works.get(index-1),false);
			line2 = getWorkLabel(works.get(index),true);
			line3 = works.size()>(index+1)?getWorkLabel(works.get(index+1),false):"";
		}
		
		
		StringBuffer sb = new StringBuffer();
		sb.append(line1);
		sb.append("<br/>");
		sb.append(line2);
		sb.append("<br/>");
		sb.append(line3);
		return sb.toString();
	}

	private String getWorkLabel(Work work, boolean em) {
//		Date ps = work.getPlanStart();
		Date pf = work.getPlanFinish();
//		Date as = work.getActualStart();
		Date af = work.getActualFinish();

//		String start = as == null ? (ps == null ? "" : String.format("%tF%n",
//				ps)) : String.format("%tF%n", as);
		String finish = af == null ? (pf == null ? "" : String.format("%tF%n",
				pf)) : String.format("%tF%n", af);
		
		StringBuffer sb = new StringBuffer();
		if(em){
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:8pt;'>");
			sb.append(work.getLabel());
//			sb.append(" ");
//			sb.append(start);
//			sb.append("~");
			sb.append(finish);
			sb.append(" 进行");
			sb.append("</span>");
		}else{
			sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:8pt;color:#a0a0a0'>");
			sb.append(work.getLabel());
//			sb.append(" ");
//			sb.append(start);
//			sb.append("~");
			sb.append(finish);
			sb.append("</span>");
		}
		
		
		return sb.toString();
	}

}
