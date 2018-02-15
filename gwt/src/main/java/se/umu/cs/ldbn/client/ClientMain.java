package se.umu.cs.ldbn.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import org.fusesource.restygwt.client.Defaults;
import se.umu.cs.ldbn.client.i18n.I18N;
import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.io.LoginListener;
import se.umu.cs.ldbn.client.ui.GlassPanel;
import se.umu.cs.ldbn.client.ui.admin.AdministratorWidget;
import se.umu.cs.ldbn.client.ui.ca.CreateAssignmentWidget;
import se.umu.cs.ldbn.client.ui.home.HomeWidget;
import se.umu.cs.ldbn.client.ui.sa.SolveAssignmentWidget;
import se.umu.cs.ldbn.client.model.UserModel;
import se.umu.cs.ldbn.client.ui.window.WindowController;
import se.umu.cs.ldbn.client.utils.Common;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class ClientMain implements EntryPoint,
	SelectionHandler<Integer>, LoginListener {

	public static String VERSION = "1.3-160723";

	public static final String MAIN_DIV_ID = "gwtapp";

	public static final String LOADING_DIV_ID = "loading";

	public static final int WIDTH_PX;

	static {
		Defaults.setServiceRoot(GWT.getHostPageBaseURL() + "rest/");

		//set the application width in pixels to fit to the screen
		int appWidth = Common.getScreenWidth();
		appWidth -= 60;
		int min = 1024-60;
		int max = 1920-60;
		if (appWidth < min) {
			appWidth = min;
		} else if (appWidth > max) {
			appWidth = max;
		}

		//see if the model has not defined a different screen width
		String w = Location.getParameter("w");
		if (w != null && !"".equals(w.trim())) {
			try {
				int tmp = Integer.parseInt(w);
				if (tmp >= 640 && tmp <= 4000) {
					appWidth = tmp;
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		WIDTH_PX = appWidth;
	}

	public static final String REPLACE_LOADING =
		"<table width=\"" + WIDTH_PX + "px\" border=\"0\" style=\"color: gray; text-decoration:none;\"><tr>" +
		"<td style=\"text-align:left; font-size:x-small\">Copyright &copy; 2008 - 2016 " +
		"<a href=\"mailto:nikolay@georgiev.email\" style=\"color: gray; text-decoration:none;\">" +
		"Nikolay Georgiev</a></td>"+
		"<td style=\"text-align: right; font-size:x-small\">Version: " + VERSION + "</td>"+
		"</tr></table>";


	private static ClientMain instance;

	public static ClientMain get() {
		// instance is created in the onModuleLoad2() method,
		// which should be called only once at the begining
		if (instance == null) {
			throw new IllegalArgumentException("onModuleLoad method must be called first.");
		}
		return instance;
	}

	private PickupDragController dragControll;
	private WindowController windowControll;

	private AbsolutePanel mainPanel;

	private GlassPanel glass;

	private TabPanel tabs;

	private AbsolutePanel tabSA;
	private boolean isTabSALoaded;
	private AbsolutePanel tabCA;
	private boolean isTabCALoaded;
	private boolean isTabAdminLoaded;

	private ClientMain() {
		super();
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Log.setUncaughtExceptionHandler();
		//used by the logger!
		Scheduler.get().scheduleDeferred(() -> onModuleLoad2());
	}

	private void onModuleLoad2() {
	    if (Log.isDebugEnabled()) {
	    	Log.debug("Debug log started.");
	    }
		if (instance == null) {
			instance = this;
		}

		//init
		AdministratorWidget.get();
		Login.get().addListener(this);

		mainPanel = new AbsolutePanel();
		mainPanel.setWidth(WIDTH_PX+"px");

		// TODO add a provider for the drag controller
		dragControll = new PickupDragController(RootPanel.get(), false);
		dragControll.setBehaviorDragProxy(true);

		windowControll = new WindowController(RootPanel.get());

		//tabs
		tabSA = new AbsolutePanel();
		isTabSALoaded = false;
		tabCA = new AbsolutePanel();
		isTabCALoaded = false;
		isTabAdminLoaded = false;

		tabs = new TabPanel();
		tabs.add(HomeWidget.get(), I18N.constants().homeTab());
		tabs.add(tabSA, I18N.constants().solveTab());
		tabs.add(tabCA, I18N.constants().createTab());
		tabs.add(ClientInjector.INSTANCE.getLicenceWidget(), I18N.constants().licenseTab());
		tabs.addSelectionHandler(this);

		tabs.setWidth("100%");
		tabs.selectTab(0);
		mainPanel.add(tabs);
		glass = new GlassPanel(mainPanel);
		DOM.setInnerHTML(RootPanel.get(LOADING_DIV_ID).getElement(), REPLACE_LOADING);
		Panel rp = RootPanel.get(MAIN_DIV_ID);
		/*
		 * add(widget, int, int) method requires the parent elements
		 * also be positioned elements, i.e. they must have a property: position.
		 * This is used by the WindowPanel class in order to add the window in the same
		 * static contex as the mainPanel. Then we give each instance of WindowPanel
		 * z-index: 1, thus ensuring the window is infront of the other content.
		 *
		 * @see http://css-discuss.incutio.com/wiki/Overlapping_And_ZIndex
		 */
		rp.getElement().getStyle().setPosition(Position.RELATIVE);
		rp.add(mainPanel);
	}


	public PickupDragController getDragController() {
		return dragControll;
	}

	public WindowController getWindowController() {
		return windowControll;
	}

	public void showGlassPanelLoading() {
		glass.setLoadingAnimationNextTime(true);
		glass.show();
	}

	public void showGlassPanel() {
		glass.show();
	}

	public void hideGlassPanel() {
		glass.hide();
	}

	public void loadSATab() {
		if (!isTabSALoaded) {
			tabSA.add(SolveAssignmentWidget.get());
			isTabSALoaded = true;
		}
	}

	public void onLoginSuccess() {
		if (ClientInjector.INSTANCE.getUserModel().isAdmin()) {
			tabs.insert(AdministratorWidget.get(), "Administrators", 3);
			isTabAdminLoaded = true;
		}

	}

	public void onSessionKilled() {
		if (isTabAdminLoaded) {
			tabs.remove(3);
			isTabAdminLoaded = false;
		}
	}

	@Override
	public void onSelection(SelectionEvent<Integer> event) {
		int tabIndex = event.getSelectedItem();
		if(tabIndex == 1) {
			loadSATab();
		} else if (tabIndex == 2 && !isTabCALoaded) {
			tabCA.add(CreateAssignmentWidget.get());
			isTabCALoaded = true;
		}
	}
}