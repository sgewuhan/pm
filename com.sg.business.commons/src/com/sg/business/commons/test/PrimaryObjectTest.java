package com.sg.business.commons.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.util.Util;

import com.mobnut.db.model.PrimaryObject;

public class PrimaryObjectTest extends PropertyTester {

	@Override
	public boolean test(Object receiver, String properties, Object[] args,
			Object expectedValue) {
//		System.out.println(receiver);
//		System.out.println(properties);
//		System.out.println((args!=null&&args.length>0)?args[0]:"");
//		System.out.println(expectedValue);
//		System.out.println(">>>>>>>>");

		if (receiver instanceof PrimaryObject) {
			if ("properties".equals(properties)) { //$NON-NLS-1$
				PrimaryObject po = (PrimaryObject) receiver;
				if (args != null && args.length > 0) {
					try {
						Object value = po.getValue("" + args[0]); //$NON-NLS-1$
						return Util.equals(expectedValue, value);
					} catch (Exception e) {
					}
				}
			} else if ("method".equals(properties)) { //$NON-NLS-1$
				if (args != null && args.length > 0) {
					PrimaryObject po = (PrimaryObject) receiver;
					Method[] methods = po.getClass().getMethods();
					for (int i = 0; i < methods.length; i++) {
						String methodName = methods[i].getName();
						if (methodName.equals(args[0])) {
							try {
								Object result = methods[i].invoke(po, new Object[]{});
								return Util.equals(expectedValue, result);
							} catch (IllegalAccessException e) {
							} catch (IllegalArgumentException e) {
							} catch (InvocationTargetException e) {
							}
						}
					}
				}
			}
		}
		return false;
	}

}
