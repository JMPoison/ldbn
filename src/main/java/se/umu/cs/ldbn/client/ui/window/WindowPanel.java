/*
 * Copyright 2008 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package se.umu.cs.ldbn.client.ui.window;

import se.umu.cs.ldbn.client.Main;
import se.umu.cs.ldbn.client.utils.Common;

import com.allen_sauer.gwt.dnd.client.util.Location;
import com.allen_sauer.gwt.dnd.client.util.WidgetLocation;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class WindowPanel extends com.google.gwt.user.client.ui.FocusPanel {

	/**
	 * WindowPanel direction constant, used in
	 * {@link ResizeDragController#makeDraggable(com.google.gwt.user.client.ui.Widget, com.allen_sauer.gwt.dnd.demo.client.example.resize.WindowPanel.DirectionConstant)}.
	 */
	public static class DirectionConstant {

		public final int directionBits;

		public final String directionLetters;

		private DirectionConstant(int directionBits, String directionLetters) {
			this.directionBits = directionBits;
			this.directionLetters = directionLetters;
		}
	}

	/**
	 * Specifies that resizing occur at the east edge.
	 */
	public static final int DIRECTION_EAST = 0x0001;

	/**
	 * Specifies that resizing occur at the both edge.
	 */
	public static final int DIRECTION_NORTH = 0x0002;

	/**
	 * Specifies that resizing occur at the south edge.
	 */
	public static final int DIRECTION_SOUTH = 0x0004;

	/**
	 * Specifies that resizing occur at the west edge.
	 */
	public static final int DIRECTION_WEST = 0x0008;

	/**
	 * Specifies that resizing occur at the east edge.
	 */
	public static final DirectionConstant EAST = new DirectionConstant(DIRECTION_EAST, "e");

	/**
	 * Specifies that resizing occur at the both edge.
	 */
	public static final DirectionConstant NORTH = new DirectionConstant(DIRECTION_NORTH, "n");

	/**
	 * Specifies that resizing occur at the north-east edge.
	 */
	public static final DirectionConstant NORTH_EAST = new DirectionConstant(DIRECTION_NORTH | DIRECTION_EAST, "ne");

	/**
	 * Specifies that resizing occur at the north-west edge.
	 */
	public static final DirectionConstant NORTH_WEST = new DirectionConstant(DIRECTION_NORTH | DIRECTION_WEST, "nw");

	/**
	 * Specifies that resizing occur at the south edge.
	 */
	public static final DirectionConstant SOUTH = new DirectionConstant(DIRECTION_SOUTH, "s");

	/**
	 * Specifies that resizing occur at the south-east edge.
	 */
	public static final DirectionConstant SOUTH_EAST = new DirectionConstant(DIRECTION_SOUTH | DIRECTION_EAST, "se");

	/**
	 * Specifies that resizing occur at the south-west edge.
	 */
	public static final DirectionConstant SOUTH_WEST = new DirectionConstant(DIRECTION_SOUTH | DIRECTION_WEST, "sw");

	/**
	 * Specifies that resizing occur at the west edge.
	 */
	public static final DirectionConstant WEST = new DirectionConstant(DIRECTION_WEST, "w");

	protected static final int BORDER_THICKNESS = 4;

	protected static final String CSS_DEMO_RESIZE_EDGE = "demo-resize-edge";

	protected static final String CSS_DEMO_RESIZE_PANEL = "demo-WindowPanel";

	protected static final String CSS_DEMO_RESIZE_PANEL_HEADER = "demo-WindowPanel-header";

	protected static final String CSS_DEMO_RESIZE_EXAMPLE = "demo-WindowExample";

	protected int contentHeight;

	protected Widget contentOrScrollPanelWidget;

	protected int contentWidth;

	protected Widget eastWidget;

	protected Grid grid = new Grid(3, 3);
	
	private boolean initialLoad = false;

	protected Widget headerWidget;

	protected Widget northWidget;

	protected Widget southWidget;

	protected Widget westWidget;

	protected final WindowController windowController;

	protected Image closeButton;
	
	protected boolean resizable;
	
	public WindowPanel(String title) {
		this(title, true);
	}

	public WindowPanel(String title, boolean resizable) {
		super();
		Widget contentWidget = getContentWidget();
		boolean wrapContentInScrollPanel = useScrollPanel();
		this.windowController = Main.get().getWindowController();
		this.resizable = resizable;
		Grid header = new Grid(1, 2);
		header.setStyleName("dw-header");
		header.setWidth("100%");
		CellFormatter cf = header.getCellFormatter();
		//IE Bug - must set BG to transparent
		DOM.setStyleAttribute(cf.getElement(0, 0), "background", "transparent");
		cf.setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		title = title.replaceAll(" ", "&nbsp;");
		Label nameLabel = new HTML(title);
		nameLabel.setStyleName("csd-title");
		header.setWidget(0, 0, nameLabel);

		closeButton = new Image("img/window-button-close.png");
		header.setWidget(0, 1, closeButton);
		Common.setCursorPointer(closeButton);
		closeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		cf.setAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		DOM.setStyleAttribute(cf.getElement(0, 1), "width", "15px");

		this.headerWidget = header;

		contentOrScrollPanelWidget = wrapContentInScrollPanel ? new ScrollPanel(
				contentWidget)
				: contentWidget;

		nameLabel.addStyleName(CSS_DEMO_RESIZE_PANEL_HEADER);
		setStyleName(CSS_DEMO_RESIZE_EXAMPLE);
		windowController.getPickupDragController().makeDraggable(this,
				nameLabel);

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(header);
		verticalPanel.add(contentOrScrollPanelWidget);

		grid.setCellSpacing(0);
		grid.setCellPadding(0);
		
		add(grid);
		
		setupCell(0, 0, NORTH_WEST);
		northWidget = setupCell(0, 1, NORTH);
		setupCell(0, 2, NORTH_EAST);

		westWidget = setupCell(1, 0, WEST);
		grid.setWidget(1, 1, verticalPanel);
		eastWidget = setupCell(1, 2, EAST);

		setupCell(2, 0, SOUTH_WEST);
		southWidget = setupCell(2, 1, SOUTH);
		setupCell(2, 2, SOUTH_EAST);
		
		DOM.setStyleAttribute(this.getElement(), "background", "#F6F6F6");
		this.setVisible(false);
		
		/*
		 * We add the window in the same static contex as the Main.mainPanel. 
		 * Then we give each instance of WindowPanel z-index: 1, 
		 * thus ensuring the window is infront of the other content.
		 * This also ensures that the drag-and-drop proxies are not covered
		 * by instances of WindowPanel, since they are added to another
		 * static contex, namely RootPanel.get().
		 * 
		 * @see http://css-discuss.incutio.com/wiki/Overlapping_And_ZIndex
		 */
		this.getElement().getStyle().setZIndex(1);
		RootPanel.get(Main.MAIN_DIV_ID).add(this, 0, 0);
	}

	public int getContentHeight() {
		return contentHeight;
	}

	public int getContentWidth() {
		return contentWidth;
	}

	public void moveBy(int right, int down) {
		AbsolutePanel parent = (AbsolutePanel) getParent();
		Location location = new WidgetLocation(WindowPanel.this, parent);
		int left = location.getLeft() + right;
		int top = location.getTop() + down;
		// no static positions
		if(left == -1 && top == -1) { 
			left = -2;
		}
		parent.setWidgetPosition(WindowPanel.this, left, top);
	}

	public void setContentSize(int width, int height) {
		if (width != contentWidth) {
			contentWidth = width;
			headerWidget.setPixelSize(contentWidth, headerWidget
					.getOffsetHeight());
			northWidget.setPixelSize(contentWidth, BORDER_THICKNESS);
			southWidget.setPixelSize(contentWidth, BORDER_THICKNESS);
		}
		if (height != contentHeight) {
			contentHeight = height;
			int headerHeight = headerWidget.getOffsetHeight();
			westWidget.setPixelSize(BORDER_THICKNESS, contentHeight
					+ headerHeight);
			eastWidget.setPixelSize(BORDER_THICKNESS, contentHeight
					+ headerHeight);
		}
		contentOrScrollPanelWidget.setPixelSize(contentWidth, contentHeight);
	}

	protected abstract Widget getContentWidget();

	protected abstract boolean useScrollPanel();

	@Override
	protected void onLoad() {
		super.onLoad();
		if (!initialLoad && contentOrScrollPanelWidget.getOffsetHeight() != 0) {
			initialLoad = true;
			headerWidget.setPixelSize(headerWidget.getOffsetWidth(),
					headerWidget.getOffsetHeight());
			setContentSize(contentOrScrollPanelWidget.getOffsetWidth(),
					contentOrScrollPanelWidget.getOffsetHeight());
		}
	}

	protected Widget setupCell(int row, int col, DirectionConstant direction) {
		final FocusPanel widget = new FocusPanel();
		widget.setPixelSize(BORDER_THICKNESS, BORDER_THICKNESS);
		grid.setWidget(row, col, widget);
		if(resizable) {
			windowController.getResizeDragController().makeDraggable(widget,
					direction);
			grid.getCellFormatter().addStyleName(
					row,
					col,
					CSS_DEMO_RESIZE_EDGE + " demo-resize-"
							+ direction.directionLetters);
		} else {
			grid.getCellFormatter().addStyleName(
					row,
					col,
					CSS_DEMO_RESIZE_EDGE);
		}
		
		return widget;
	}
	
	
	//methods from the PopupPanel
	public void hide() {
		setVisible(false);
	}
	
	public void show() {
		//this method is implemented very badly, we should find a better solution
		setVisible(true);
		
		Scheduler.get().scheduleDeferred(new Command() {
			public void execute() {
				//offset size is only available after the Widget is attached to the DOM
				int ow = getOffsetWidth();
				int oh = getOffsetHeight();
				int sl = Window.getScrollLeft();
				int st = Window.getScrollTop();
				int wcw = Window.getClientWidth();
				int wch = Window.getClientHeight();
				int left = (wcw - ow) >> 1;
			    int top = (wch - oh) >> 1;
			    left -= Document.get().getBodyOffsetLeft();
			    top -= Document.get().getBodyOffsetTop();
			    AbsolutePanel parent = (AbsolutePanel) getParent();
			    parent.setWidgetPosition(WindowPanel.this, sl + left, st + top);
			}
		});
	}
	
	public void center() {
		show();
	}
}
