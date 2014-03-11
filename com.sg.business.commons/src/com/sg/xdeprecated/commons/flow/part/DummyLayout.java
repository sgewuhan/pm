/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.sg.xdeprecated.commons.flow.part;

import org.eclipse.draw2d.XYLayout;

/**
 * The dummy layout class does nothing during normal layouts. The Graph layout
 * is entirely performed in one place: {@link GraphLayoutManager}, on the
 * diagram's figure. During animation, THIS layout will playback the
 * intermediate steps between the two invocations of the graph layout.
 * 
 * @author hudsonr
 */
public class DummyLayout extends XYLayout {
	
	
}
