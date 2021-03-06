package se.umu.cs.ldbn.client.ui.home;

import se.umu.cs.ldbn.client.io.Login;
import se.umu.cs.ldbn.client.io.LoginListener;
import se.umu.cs.ldbn.client.ui.DisclosureWidget;
import se.umu.cs.ldbn.client.ui.HeaderWidget;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;

public class HomeWidget extends Composite implements LoginListener  {
	
	private final static int VIEWER_PX_HEIGHT = 630;
	
	private static HomeWidget inst;
	
	public static HomeWidget get() {
		if(inst == null) {
			inst = new HomeWidget();
		}
		return inst;
	}
	
	private AbsolutePanel mainPanel;
	private UserLoginWidget login;
	private UserLogoutWidget logout;
	private HeaderWidget hw;
	
	private HomeWidget() {
		mainPanel = new AbsolutePanel();
		mainPanel.setWidth("100%");
		initWidget(mainPanel);
		login = new UserLoginWidget();
		hw = new HeaderWidget();
		hw.add(login);
		mainPanel.add(hw);
		Frame viewer = new Frame("info/about.html"); 
		viewer.setWidth("100%");
		viewer.setHeight(VIEWER_PX_HEIGHT+"px");
		//DOM.setStyleAttribute(viewer.getElement(), "overflow", "hidden");
		DisclosureWidget dw = new DisclosureWidget("About", viewer);
		mainPanel.add(dw);
		Login.get().addListener(this);
	}

	public void onLoginSuccess() {
		login.clear();
		login.removeFromParent();
		if(logout == null) {
			logout = new UserLogoutWidget();
		}
		hw.add(logout);
	}

	public void onSessionKilled() {
		logout.removeFromParent();
		hw.add(login);
	}
}
