package ${package}.configs;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class APPServerConfig {

	private String appPerfilesHostServerDomain;
	private String appPerfilesServerProtocol;
	private int appPerfilesServerPort;
	private String appPerfilesServerApiUrl;
	private String appPerfilesServerUser;
	private Boolean appPerfilesServerAuthenticated;
	private String appPerfilesServerPassword;
	private String appPerfilesUrl;
	
	private String appMenuHostServerDomain;
	private String appMenuServerProtocol;
	private int appMenuServerPort;
	private String appMenuServerApiUrl;
	private String appMenuServerUser;
	private Boolean appMenuServerAuthenticated;
	private String appMenuServerPassword;
	private String appMenuUrl;
	
	
	private PropertiesConfiguration config;
	
	public APPServerConfig() {
		try {
			config = new PropertiesConfiguration("app.properties");
		} catch (ConfigurationException e) {
			System.out.println("Problemas con el archivo de configuración app.properties");
			e.printStackTrace();
		}
		
		appPerfilesHostServerDomain = config.getString("app.perfiles.host.server.domain");
		appPerfilesServerProtocol = config.getString("app.perfiles.server.protocol");
		appPerfilesServerPort = config.getInt("app.perfiles.host.server.port");
		appPerfilesServerApiUrl = config.getString("app.perfiles.server.api.url");
		appPerfilesServerUser = config.getString("app.perfiles.server.user");
		appPerfilesServerPassword = config.getString("app.perfiles.server.password");
		appPerfilesUrl = config.getString("app.perfiles.url");
		appPerfilesServerAuthenticated = config.getBoolean("app.perfiles.server.authenticated");
		
		appMenuHostServerDomain = config.getString("app.menu.host.server.domain");
		appMenuServerProtocol = config.getString("app.menu.server.protocol");
		appMenuServerPort = config.getInt("app.menu.host.server.port");
		appMenuServerApiUrl = config.getString("app.menu.server.api.url");
		appMenuServerUser = config.getString("app.menu.server.user");
		appMenuServerPassword = config.getString("app.menu.server.password");
		appMenuUrl = config.getString("app.menu.url");
		appMenuServerAuthenticated = config.getBoolean("app.menu.server.authenticated");
		
	}
	
	public String getDncpPerfilesHostServer() {
		return appPerfilesServerProtocol + "://" + appPerfilesHostServerDomain + ":" + appPerfilesServerPort + appPerfilesServerApiUrl;
	}

	public String getDncpPerfilesServerProtocol() {
		return appPerfilesServerProtocol;
	}

	public void setDncpPerfilesServerProtocol(String appServerProtocol) {
		this.appPerfilesServerProtocol = appServerProtocol;
	}

	public int getDncpPerfilesServerPort() {
		return appPerfilesServerPort;
	}

	public void setDncpPerfilesServerPort(int appServerPort) {
		this.appPerfilesServerPort = appServerPort;
	}

	public String getDncpPerfilesHostServerDomain() {
		return appPerfilesHostServerDomain;
	}

	public void setDncpPerfilesHostServerDomain(String appHostServerDomain) {
		this.appPerfilesHostServerDomain = appHostServerDomain;
	}

	public String getDncpPerfilesServerApiUrl() {
		return appPerfilesServerApiUrl;
	}

	public void setDncpPerfilesServerApiUrl(String appServerApiUrl) {
		this.appPerfilesServerApiUrl = appServerApiUrl;
	}

	public String getDncpPerfilesServerUser() {
		return appPerfilesServerUser;
	}

	public void setDncpPerfilesServerUser(String appServerUser) {
		this.appPerfilesServerUser = appServerUser;
	}

	public String getDncpPerfilesServerPassword() {
		return appPerfilesServerPassword;
	}

	public void setDncpPerfilesServerPassword(String appServerPassword) {
		this.appPerfilesServerPassword = appServerPassword;
	}

	public String getDncpPerfilesUrl() {
		return appPerfilesUrl;
	}

	public void setDncpPerfilesUrl(String appPerfilesUrl) {
		this.appPerfilesUrl = appPerfilesUrl;
	}

	public Boolean getDncpPerfilesServerAuthenticated() {
		return appPerfilesServerAuthenticated;
	}

	public void setDncpPerfilesServerAuthenticated(Boolean appPerfilesServerAuthenticated) {
		this.appPerfilesServerAuthenticated = appPerfilesServerAuthenticated;
	}
	
	//Menu
	public String getDncpMenuHostServer() {
		return appMenuServerProtocol + "://" + appMenuHostServerDomain + ":" + appMenuServerPort + appMenuServerApiUrl;
	}

	public String getDncpMenuServerProtocol() {
		return appMenuServerProtocol;
	}

	public void setDncpMenuServerProtocol(String appServerProtocol) {
		this.appMenuServerProtocol = appServerProtocol;
	}

	public int getDncpMenuServerPort() {
		return appMenuServerPort;
	}

	public void setDncpMenuServerPort(int appServerPort) {
		this.appMenuServerPort = appServerPort;
	}

	public String getDncpMenuHostServerDomain() {
		return appMenuHostServerDomain;
	}

	public void setDncpMenuHostServerDomain(String appHostServerDomain) {
		this.appMenuHostServerDomain = appHostServerDomain;
	}

	public String getDncpMenuServerApiUrl() {
		return appMenuServerApiUrl;
	}

	public void setDncpMenuServerApiUrl(String appServerApiUrl) {
		this.appMenuServerApiUrl = appServerApiUrl;
	}

	public String getDncpMenuServerUser() {
		return appMenuServerUser;
	}

	public void setDncpMenuServerUser(String appServerUser) {
		this.appMenuServerUser = appServerUser;
	}

	public String getDncpMenuServerPassword() {
		return appMenuServerPassword;
	}

	public void setDncpMenuServerPassword(String appServerPassword) {
		this.appMenuServerPassword = appServerPassword;
	}

	public String getDncpMenuUrl() {
		return appMenuUrl;
	}

	public void setDncpMenuUrl(String appMenuUrl) {
		this.appMenuUrl = appMenuUrl;
	}

	public Boolean getDncpMenuServerAuthenticated() {
		return appMenuServerAuthenticated;
	}

	public void setDncpMenuServerAuthenticated(Boolean appMenuServerAuthenticated) {
		this.appMenuServerAuthenticated = appMenuServerAuthenticated;
	}
	
	
}
