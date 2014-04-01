package com.tmt.pdm.dcpdm.label;

import java.io.File;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.tmt.pdm.client.Starter;
import com.tmt.pdm.dcpdm.handler.DCPDMUtil;

import dyna.framework.service.dos.DOSChangeable;

public class DCPDMObjectLabelProvider extends ColumnLabelProvider {

	public DCPDMObjectLabelProvider() {
	}

	@Override
	public String getText(Object element) {
		if (element instanceof String) {
			try {
				DOSChangeable dosObj = Starter.dos.get((String) element);
				if (dosObj != null) {
					String classOuid = Starter.dos.getClassOuid((String) element);
					DOSChangeable classObj = Starter.dos.getClass(classOuid);
					Object title = classObj.get("title");
					title = title == null ? "" : title;
					Object num = dosObj.get("md$number");
					num = num == null ? "" : num;
					Object desc = dosObj.get("md$description");
					desc = desc == null ? "" : desc;
					return ""+title +": "+ num + "|" + desc;
				}
			} catch (Exception e) {
			}
		}else if(element instanceof Map){
			@SuppressWarnings("rawtypes")
			Map map = (Map) element;
			String desc = (String) map.get("md$description");
			String filePath = (String) map.get("md$path");
			String userName = (String) map.get("md$user.name");
			Object date = map.get("md$checkedin.date");
			File file = new File(desc);
			StringBuffer sb = new StringBuffer();
			String url = DCPDMUtil.getDownloadURL(filePath, file.getName());
			sb.append("<a href='" + url //$NON-NLS-1$ 
					+ "'>"); //$NON-NLS-1$
			sb.append(file.getName());
			sb.append("</a>");//$NON-NLS-1$
			sb.append("<br/><small>");
			sb.append(userName);
			sb.append(" ");
			sb.append(date);
			sb.append("</small>");

			return sb.toString();
		}
		
		return "";
	}

}
