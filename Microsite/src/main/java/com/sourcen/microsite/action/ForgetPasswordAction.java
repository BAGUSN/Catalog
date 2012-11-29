package com.sourcen.microsite.action;



import com.sourcen.microsite.model.EmailMessage;
import com.sourcen.microsite.model.Property;
import com.sourcen.microsite.model.User;
import com.sourcen.microsite.model.EmailMessage.EmailAddress;
import com.sourcen.microsite.service.EmailManager;
import com.sourcen.microsite.service.EmailTemplateManager;
import com.sourcen.microsite.service.RegistrationManager;

import javassist.NotFoundException;



public class ForgetPasswordAction extends SourcenActionSupport {
	private static final long serialVersionUID = 1L;

	private String username;
	private String keycode;


	RegistrationManager registrationManager = null;

	EmailManager emailManager = null;
	
	EmailTemplateManager et = null;

	public EmailManager getEmailManager() {
		return emailManager;
	}

	public void setEmailManager(EmailManager emailManager) {
		this.emailManager = emailManager;
	}

	public RegistrationManager getRegistrationManager() {
		return registrationManager;
	}

	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String input() {

		if (!registrationManager.isPasswordResetEnabled())
			return "unavailable";
		
		return INPUT;

	}

	public String execute() {

		
		if (!registrationManager.isPasswordResetEnabled())
			return "unavailable";

		try {

			User user = this.userManager.getUser(username);
			EmailMessage msg = new EmailMessage();
			msg.addRecipient(new EmailAddress(user.getUsername(), user
					.getEmail()));
			msg.setSubject("Password Reset � Accept Search");
			String token =null;
			
			try {
				token=this.applicationManager.getProperty(username).getValue();
			} catch (NotFoundException e) {				
				token=applicationManager.getStringToken();
			}

			this.getApplicationManager().saveProperty(
					new Property(username, token));
			if(this.getTemplateManager() == null)
				System.out.println("asdf");
			msg.setHtmlBody(this.getTemplateManager().getEmailTemplate("passwordReset.email").getBody());

			msg.setSender(new EmailAddress("Support",
							this.registrationManager.getFeedbackMailFromAddress()));

			emailManager.send(msg);
			
		} catch (NotFoundException e) {
				addFieldError("username", getText("error.username.not.found"));
			return INPUT;
		}
		this.addActionMessage(getText("forgot.password.success"));
		return SUCCESS;

	}

	public void validate() {

		if (getUsername() == null || getUsername().length() < 5) {
			addFieldError("username", getText("error.username.required"));

		}
		String tempKeycode=null;
		
		if (registrationManager.isHumanValidationEnabled() &&(this.getSession().containsKey(nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY)))	{ 
			tempKeycode = (String) this.getSession().get(nl.captcha.servlet.Constants.SIMPLE_CAPCHA_SESSION_KEY);
			if(tempKeycode == null  || !tempKeycode.equals(keycode))				
				addFieldError("keycode",getText("error.human.validation"));
		}	
	
		
		
		super.validate();
	}

	public String getKeycode() {
		return keycode;
	}

	public void setKeycode(String keycode) {
		this.keycode = keycode;
	}

	/**
	 * @return the et
	 */
	public EmailTemplateManager getEt() {
		return et;
	}

	/**
	 * @param et the et to set
	 */
	public void setEt(EmailTemplateManager et) {
		this.et = et;
	}

	

}
