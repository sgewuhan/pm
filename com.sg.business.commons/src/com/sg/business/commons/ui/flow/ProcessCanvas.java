package com.sg.business.commons.ui.flow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.types.BasicBSONList;
import org.drools.definition.process.Connection;
import org.drools.definition.process.NodeContainer;
import org.drools.definition.process.Process;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.CompoundDirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.draw2d.graph.Subgraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.core.node.Join;
import org.jbpm.workflow.core.node.Split;
import org.jbpm.workflow.core.node.StartNode;

import com.mongodb.DBObject;
import com.sg.bpm.workflow.model.DroolsProcessDefinition;
import com.sg.business.commons.ui.flow.part.SimpleNodeLabel;
import com.sg.widgets.Widgets;

public class ProcessCanvas extends FigureCanvas {

	private CompoundDirectedGraph graphic;

	private ListenerList listeners = new ListenerList();

	private BasicBSONList history;

	public ProcessCanvas(Composite parent,int style) {
		super(parent, style);
		getViewport().setContentsTracksHeight(true);
		getViewport().setContentsTracksWidth(true);
	}
	
	public ProcessCanvas(Composite parent) {
		this(parent,SWT.BORDER);
	}

	public void setInput(DroolsProcessDefinition procDefinition,
			BasicBSONList procHistory) {
		graphic = buildGraphicContent(procDefinition);
		this.history = procHistory;
		if(graphic!=null){
			setContents(buildGraph(graphic));
		}
	}
	
	public void setInput(DroolsProcessDefinition procDefinition) {
		setInput(procDefinition,null);
	}
	

	@SuppressWarnings("unchecked")
	private void buildGraphic(NodeContainer nodeContainer,
			CompoundDirectedGraph graph) {
		NodeList _nodes = new NodeList();
		EdgeList _edges = new EdgeList();

		Subgraph parentGraph = new Subgraph(nodeContainer);
		_nodes.add(parentGraph);

		// 1.构造节点
		Map<org.drools.definition.process.Node, Node> nodemap = new HashMap<org.drools.definition.process.Node, Node>();

		org.drools.definition.process.Node[] nodes = nodeContainer.getNodes();
		for (int i = 0; i < nodes.length; i++) {
			Node node = new Node(nodes[i], parentGraph);

			nodemap.put(nodes[i], node);
			_nodes.add(node);
		}

		// 2.构造连接
		for (int i = 0; i < nodes.length; i++) {
			Map<String, List<Connection>> conns = nodes[i]
					.getIncomingConnections();
			Node _to = nodemap.get(nodes[i]);
			if (_to == null) {
				continue;
			}
			if (conns != null) {
				Collection<List<Connection>> vs = conns.values();
				if (vs != null) {
					Iterator<List<Connection>> iter = vs.iterator();
					while (iter.hasNext()) {
						List<Connection> sublist = iter.next();
						if (sublist == null) {
							continue;
						}
						for (int j = 0; j < sublist.size(); j++) {
							Connection connection = sublist.get(j);
							org.drools.definition.process.Node from = connection
									.getFrom();
							Node _from = nodemap.get(from);
							if (_from == null) {
								continue;
							}
							_edges.add(new Edge(_from, _to));
						}
					}
				}
			}
		}

		graph.nodes = _nodes;
		graph.edges = _edges;

	}

	protected CompoundDirectedGraph buildGraphicContent(
			DroolsProcessDefinition procDefinition) {
		if(procDefinition == null){
			return null;
		}
		
		Process process = procDefinition.getProcess();
		if (!(process instanceof NodeContainer)) {
			return null;
		}
		CompoundDirectedGraph graph = new CompoundDirectedGraph();

		NodeContainer nodeContainer = (NodeContainer) process;
		buildGraphic(nodeContainer, graph);

		new CompoundDirectedGraphLayout().visit(graph);
		return graph;
	}

	public Figure buildGraph(CompoundDirectedGraph graph) {
		Figure contents = new Panel();
		contents.setFont(new Font(null, "微软雅黑", 12, 0));
		contents.setBackgroundColor(ColorConstants.white);
		contents.setLayoutManager(new XYLayout());

		for (int i = 0; i < graph.subgraphs.size(); i++) {
			Subgraph s = (Subgraph) graph.subgraphs.get(i);
			buildSubgraphFigure(contents, s);
		}

		for (int i = 0; i < graph.nodes.size(); i++) {
			Node node = graph.nodes.getNode(i);
			buildNodeFigure(contents, node);
		}

		for (int i = 0; i < graph.edges.size(); i++) {
			Edge edge = graph.edges.getEdge(i);
			buildEdgeFigure(contents, edge);
		}
		return contents;
	}

	private void buildNodeFigure(Figure contents, Node node) {
		Label label = null;
		// label = new Label();
		// label.setBackgroundColor(ColorConstants.lightBlue);
		// label.setOpaque(true);
		// label.setBorder(new LogicFlowBorder());
		// if (node.incoming.isEmpty())
		// label.setBorder(new LineBorder(2));

		if (node.data instanceof org.drools.definition.process.Node) {
			org.drools.definition.process.Node _node = (org.drools.definition.process.Node) node.data;
			if (_node instanceof StartNode) {
				label = new SimpleNodeLabel(_node);
				//
				contents.add(label, new Rectangle(node.x, node.y, node.width,
						node.height));
			} else if (_node instanceof EndNode) {
				label = new SimpleNodeLabel(_node);
				contents.add(label, new Rectangle(node.x, node.y, node.width,
						node.height));
			} else {
				label = new SimpleNodeLabel(_node);
				if (_node instanceof Join) {
					contents.add(label, new Rectangle(node.x, node.y,
							node.width, node.height));
				} else if (_node instanceof Split) {
					contents.add(label, new Rectangle(node.x, node.y,
							node.width, node.height));
				} else if (_node instanceof HumanTaskNode) {
					if (isComplete(_node)) {
						label.setForegroundColor(Widgets.getColor(getDisplay(),
								0, 0, 0));
					} else if (isReserved(_node)) {
						label.setForegroundColor(Widgets.getColor(getDisplay(),
								0, 0x88, 0));
					} else {
						label.setForegroundColor(Widgets.getColor(getDisplay(),
								0, 0xcc, 0xcc));
					}
					contents.add(label, new Rectangle(node.x, node.y, 120,
							node.height));
				} else {
					label.setForegroundColor(Widgets.getColor(getDisplay(),
							0xcc, 0xcc, 0xcc));
					contents.add(label, new Rectangle(node.x, node.y, 120,
							node.height));
				}
			}
			// }else if(node.data instanceof WorkflowProcess){
			// WorkflowProcess workflow = (WorkflowProcess) node.data;
			// label = new Label();
			// label.setText(workflow.getName());
			// contents.add(label, new Rectangle(node.x, node.y, 120,
			// node.height));

		} else if (node.data instanceof String) {
			// label = new Label();
			// label.setText( (String) node.data);
			// contents.add(label, new Rectangle(node.x, node.y, 120,
			// node.height));
		} else {
			// label = new Label();
			// label.setText( ""+node.data);
			// contents.add(label, new Rectangle(node.x, node.y, 120,
			// node.height));
		}

		if (label != null) {
			label.setRequestFocusEnabled(true);
			label.addMouseListener(new MouseListener() {

				@Override
				public void mousePressed(MouseEvent me) {
					fireNodeSelected(me.getSource());
				}

				@Override
				public void mouseReleased(MouseEvent me) {
				}

				@Override
				public void mouseDoubleClicked(MouseEvent me) {

				}

			});
		}

	}

	private boolean isReserved(org.drools.definition.process.Node _node) {
		if (history == null) {
			return true;
		}
		String nodeName = _node.getName();
		for (int i = 0; i < history.size(); i++) {
			DBObject historyItem = (DBObject) history.get(i);
			if (nodeName.equals(historyItem.get("taskname"))
					&& "start".equals(historyItem.get("form_action"))) {
				return true;
			}
		}

		return false;
	}

	private boolean isComplete(org.drools.definition.process.Node _node) {
		if (history == null) {
			return true;
		}
		String nodeName = _node.getName();
		for (int i = 0; i < history.size(); i++) {
			DBObject historyItem = (DBObject) history.get(i);
			if (nodeName.equals(historyItem.get("taskname"))
					&& "complete".equals(historyItem.get("form_action"))) {
				return true;
			}
		}

		return false;
	}

	protected void fireNodeSelected(Object source) {
		Object[] listenerList = listeners.getListeners();
		for (int i = 0; i < listenerList.length; i++) {
			INodeSelectListener lis = (INodeSelectListener) listenerList[i];
			lis.select(source);
		}
	}

	private void buildEdgeFigure(Figure contents, Edge edge) {
		PolylineConnection conn = connection(edge);
		conn.setForegroundColor(ColorConstants.gray);
		PolygonDecoration dec = new PolygonDecoration();
		conn.setTargetDecoration(dec);
		conn.setPoints(edge.getPoints());
		contents.add(conn);
	}

	private PolylineConnection connection(Edge e) {
		PolylineConnection conn = new PolylineConnection();
		conn.setConnectionRouter(new BendpointConnectionRouter());
		List<AbsoluteBendpoint> bends = new ArrayList<AbsoluteBendpoint>();
		NodeList nodes = e.vNodes;
		if (nodes != null) {
			for (int i = 0; i < nodes.size(); i++) {
				Node n = nodes.getNode(i);
				int x = n.x;
				int y = n.y;
				bends.add(new AbsoluteBendpoint(x, y));
				bends.add(new AbsoluteBendpoint(x, y + n.height));
			}
		}
		conn.setRoutingConstraint(bends);
		return conn;
	}

	private void buildSubgraphFigure(Figure contents, Subgraph s) {
		Figure figure = new Figure();
		// figure.setBorder(new LineBorder(ColorConstants.blue, s.insets.left));
		contents.add(figure, new Rectangle(s.x, s.y, s.width, s.height));
	}

	public void addNodeSelectListener(INodeSelectListener listener) {
		listeners.add(listener);
	}

	public void removeNodeSelectListener(INodeSelectListener listener) {
		listeners.remove(listener);
	}

}
