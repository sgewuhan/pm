package com.sg.business.commons.flow;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.sg.business.commons.CommonsActivator;
import com.sg.business.commons.flow.model.Activity;
import com.sg.business.commons.flow.model.ParallelActivity;
import com.sg.business.commons.flow.model.SequentialActivity;

/**
 * Handles the creation of the palette for the Flow Editor.
 * 
 * @author Daniel Lee
 */
public class FlowEditorPaletteFactory {

	private static List<PaletteEntry> createCategories(PaletteRoot root) {
		List<PaletteEntry> categories = new ArrayList<PaletteEntry>();
		categories.add(createControlGroup(root));
		categories.add(createComponentsDrawer());
		return categories;
	}

	private static PaletteContainer createComponentsDrawer() {

		PaletteDrawer drawer = new PaletteDrawer("Components", null);

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();

		CombinedTemplateCreationEntry combined = new CombinedTemplateCreationEntry(
				"Activity", "Create a new Activity Node", Activity.class,
				new SimpleFactory(Activity.class),
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/gear16.gif"), ImageDescriptor.createFromFile(
						Activity.class, "image/gear16.gif"));
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry("Sequential Activity",
				"Create a Sequential Activity", SequentialActivity.class,
				new SimpleFactory(SequentialActivity.class),
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/sequence16.gif"),
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/sequence16.gif"));
		entries.add(combined);

		combined = new CombinedTemplateCreationEntry("Parallel Activity",
				"Create a  Parallel Activity", ParallelActivity.class,
				new SimpleFactory(ParallelActivity.class),
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/parallel16.gif"),
				ImageDescriptor.createFromFile(CommonsActivator.class,
						"image/parallel16.gif"));
		entries.add(combined);

		drawer.addAll(entries);
		return drawer;
	}

	private static PaletteContainer createControlGroup(PaletteRoot root) {
		PaletteGroup controlGroup = new PaletteGroup("Control Group");

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();

		ToolEntry tool = new SelectionToolEntry();
		entries.add(tool);
		root.setDefaultEntry(tool);

		tool = new MarqueeToolEntry();
		entries.add(tool);

		PaletteSeparator sep = new PaletteSeparator(
				"com.sg.business.commons.flow.flowplugin.sep2");
		sep.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		entries.add(sep);

		tool = new ConnectionCreationToolEntry("Connection Creation",
				"Creating connections", null, ImageDescriptor.createFromFile(
						CommonsActivator.class, "image/connection16.gif"),
				ImageDescriptor.createFromFile(Activity.class,
						"image/connection16.gif"));
		entries.add(tool);
		controlGroup.addAll(entries);
		return controlGroup;
	}

	/**
	 * Creates the PaletteRoot and adds all Palette elements.
	 * 
	 * @return the root
	 */
	public static PaletteRoot createPalette() {
		PaletteRoot flowPalette = new PaletteRoot();
		flowPalette.addAll(createCategories(flowPalette));
		return flowPalette;
	}

}
