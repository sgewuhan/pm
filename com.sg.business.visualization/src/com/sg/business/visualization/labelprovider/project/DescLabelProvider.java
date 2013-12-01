package com.sg.business.visualization.labelprovider.project;

import java.util.List;

import org.eclipse.swt.internal.widgets.MarkupValidator;

import com.mobnut.commons.util.Utils;
import com.mobnut.commons.util.file.FileUtil;
import com.mobnut.db.file.RemoteFile;
import com.mobnut.db.model.PrimaryObject;
import com.sg.business.model.Organization;
import com.sg.business.model.Project;
import com.sg.business.model.User;

@SuppressWarnings("restriction")
public class DescLabelProvider extends AbstractProjectLabelProvider {

	@Override
	protected String getProjectText(Project project) {
		String desc = project.getDesc();
		try{
			MarkupValidator.getInstance().validate(desc);
		}catch(Exception e){
			desc = Utils.getPlainText(desc);
		}
		
		List<RemoteFile> images = project.getCoverImages();
		String url;
		if(images.size()>0){
			RemoteFile rf = images.get(0);
			url = FileUtil.getImageURL(rf.getOutputRefData());
		}else{
			url = null;
//			url = FileUtil.getImageURL(BusinessResource.IMAGE_24_BLANK, BusinessResource.PLUGIN_ID);
		}
		
		List<PrimaryObject> org = project.getLaunchOrganization();
		String orgText = "";
		for (int i = 0; i < org.size(); i++) {
			
			Organization primaryObject = (Organization) org.get(i);
			String path = primaryObject.getPath(2);
			if(i==0){
				orgText += path;
			}else if(i<=2){
				orgText += ", "+path;
			}else{
				orgText += " ..";
				break;
			}
		}
		
		User charger = project.getCharger();
		Object chargerText = charger==null?"?":charger.getUsername();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<span style='FONT-FAMILY:微软雅黑;font-size:10pt;margin-left:0;word-break : break-all; white-space:normal; display:block; width=1000px'>");
		//显示项目封面
		if(url!=null){
			sb.append("<img src='"+ url+ "' style='float:left; left:0; top:0; display:block;' width='48' height='48' />");
		}
		//显示项目名称
		sb.append("<b>");
		sb.append(desc);
		sb.append("</b>");
		sb.append("<small style='color=#006789'>");
		sb.append("<br/>");
		//显示承担组织
		sb.append(orgText);
		//显示负责人
		sb.append("</small>");
		sb.append(" ");
		sb.append("<small>");
		sb.append(chargerText);
		sb.append("</small>");
		
		sb.append("</span>");
		
		toolsForOpenProject(project,sb,"desc");
		
		return sb.toString();
	}
	
	
}
