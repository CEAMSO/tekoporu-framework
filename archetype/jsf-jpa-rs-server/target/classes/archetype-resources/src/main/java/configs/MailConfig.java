package ${package}.configs;

import org.ticpy.tekoporu.configuration.Configuration;

@Configuration(resource = "mail")
public class MailConfig {
	private String smtpHostName;
	private String smtpTrust;
	private Integer smtpPort;
	private Boolean smtpAuth;
	private Boolean smtpDebug;
	private Boolean smtpStarttls;
	private String emailFromAddress;
	private String emailFromPasswrd;
	private String sslFactory;
	private String root;
	private String pieDePagina;
	private String isoImage;

	public String getSmtpHostName() {
		return smtpHostName;
	}

	public void setSmtpHostName(String smtpHostName) {
		this.smtpHostName = smtpHostName;
	}

	public String getSmtpTrust() {
		return smtpTrust;
	}

	public void setSmtpTrust(String smtpTrust) {
		this.smtpTrust = smtpTrust;
	}

	public Integer getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}

	public Boolean getSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(Boolean smtpAuth) {
		this.smtpAuth = smtpAuth;
	}

	public Boolean getSmtpDebug() {
		return smtpDebug;
	}

	public void setSmtpDebug(Boolean smtpDebug) {
		this.smtpDebug = smtpDebug;
	}

	public Boolean getSmtpStarttls() {
		return smtpStarttls;
	}

	public void setSmtpStarttls(Boolean smtpStarttls) {
		this.smtpStarttls = smtpStarttls;
	}

	public String getEmailFromAddress() {
		return emailFromAddress;
	}

	public void setEmailFromAddress(String emailFromAddress) {
		this.emailFromAddress = emailFromAddress;
	}

	public String getEmailFromPasswrd() {
		return emailFromPasswrd;
	}

	public void setEmailFromPasswrd(String emailFromPasswrd) {
		this.emailFromPasswrd = emailFromPasswrd;
	}

	public String getSslFactory() {
		return sslFactory;
	}

	public void setSslFactory(String sslFactory) {
		this.sslFactory = sslFactory;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public String getPieDePagina() {
		return pieDePagina;
	}

	public void setPieDePagina(String pieDePagina) {
		this.pieDePagina = pieDePagina;
	}

	public String getIsoImage() {
		return isoImage;
	}

	public void setIsoImage(String isoImage) {
		this.isoImage = isoImage;
	}

}
