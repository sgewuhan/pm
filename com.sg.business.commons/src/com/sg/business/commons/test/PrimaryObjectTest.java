package com.sg.business.commons.test;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.util.Util;

import com.mobnut.db.model.PrimaryObject;

public class PrimaryObjectTest extends PropertyTester {


	@Override
	public boolean test(Object receiver, String properties, Object[] args,
			Object expectedValue) {
		if (receiver instanceof PrimaryObject) {
			PrimaryObject po = (PrimaryObject) receiver;
			if(args!=null&&args.length>0){
				try{
					Object value = po.getValue(""+args[0]);
					return Util.equals(expectedValue, value);
				}catch(Exception e){}
			}
		}
		return false;
	}

}
