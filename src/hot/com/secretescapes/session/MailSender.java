package com.secretescapes.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;

/**
 * Component to send an email.
 * 
 * @author jose.darocha@gmail.com
 * 
 */
@Name("mailSender")
@Scope(ScopeType.CONVERSATION)
@AutoCreate
public class MailSender implements Serializable {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = -2390650343094703174L;

	@In(create = true)
	private Renderer renderer;

	private Map<String, String> receivers = new HashMap<String, String>();
	private String message;

	/**
	 * Send an email with the message defined in email.xhtml.
	 */
	public void sendMail(final String message) {
		try {
			setMessage(message);
			renderer.render("/pages/email.xhtml");
			StatusMessages.instance().addFromResourceBundle(Severity.INFO,
					"MailOK");
		} catch (Exception e) {
			StatusMessages.instance().addFromResourceBundle(Severity.ERROR,
					"MailError");
		}
	}

	/**
	 * Add a new receiver to the mail.
	 * 
	 * @param name
	 *            Receiver's name.
	 * @param email
	 *            Receiver's email.
	 */
	public void addReceiver(final String name, final String email) {
		receivers.put(name, email);
	}

	/** Accesors for the mail. **/

	public Map<String, String> getReceivers() {
		return receivers;
	}

	public void setReceivers(final Map<String, String> receivers) {
		this.receivers = receivers;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}