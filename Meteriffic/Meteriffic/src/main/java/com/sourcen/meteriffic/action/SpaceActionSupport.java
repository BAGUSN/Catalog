package com.sourcen.meteriffic.action;

import com.opensymphony.xwork2.ActionSupport;
import com.sourcen.meteriffic.security.AuthenticationProvider;
import com.sourcen.meteriffic.service.ApplicationManager;
import com.sourcen.meteriffic.service.EmailManager;
import com.sourcen.meteriffic.service.RegistrationManager;
import com.sourcen.meteriffic.service.UserManager;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SpaceActionSupport extends ActionSupport implements SessionAware,
		ServletRequestAware, ServletResponseAware {

	public static Logger log = Logger.getLogger(SpaceActionSupport.class);
	/**
	 * 
	 */
	protected UserManager userManager = null;
	protected EmailManager emailManager = null;
	protected RegistrationManager registrationManager = null;
	protected ApplicationManager applicationManager;
	
	private static final long serialVersionUID = 1L;
	protected Map<String, Object> session;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	public static final String CANCEL = "cancel";
	public short   tabIndex =0;
	public short tab = 0;
	
	
	protected AuthenticationProvider authProvider;
	 
	public Map<String, Object> getSession() {
		return session;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setSession(Map session) {
		this.session = session;

	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

	}

	public void setServletResponse(HttpServletResponse response) {

		this.response = response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public AuthenticationProvider getAuthProvider() {
		return authProvider;
	}

	public void setAuthProvider(AuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}
   
	
	public String getBaseUrl(){
		
		String BaseUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		return BaseUrl;
	}

	public short getTabIndex() {
		return tabIndex;
	}

	public void setTabIndex(short tabIndex) {
		this.tabIndex = tabIndex;
	}

	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	/**
	 * @return the tab
	 */
	public short getTab() {
		return tab;
	}

	/**
	 * @param tab the tab to set
	 */
	public void setTab(short tab) {
		this.tab = tab;
	}

	


}
