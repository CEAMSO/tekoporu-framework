package ${package}.configs;

import org.ticpy.tekoporu.configuration.Configuration;

@Configuration(resource = "backend")
public class BackEndConfig {
	private String hostServerDomain;
	private String hostServerProtocol;
	private int hostServerPort;
	private String serverApiUrl;
	private String serverUser;
	private String serverPassword;


	
	public String getHostServerDomain() {
		return hostServerDomain;
	}
	
	public void setHostServerDomain(String hostServerDomain) {
		this.hostServerDomain = hostServerDomain;
	}

	
	public String getHostServerProtocol() {
		return hostServerProtocol;
	}

	public void setHostServerProtocol(String hostServerProtocol) {
		this.hostServerProtocol = hostServerProtocol;
	}


	public int getHostServerPort() {
		return hostServerPort;
	}

	public void setHostServerPort(int hostServerPort) {
		this.hostServerPort = hostServerPort;
	}


	public String getHostServer() {
		return hostServerProtocol + "://" + hostServerDomain + ":" + hostServerPort + serverApiUrl;
	}

	
	public String getServerApiUrl() {
		return serverApiUrl;
	}

	public void setServerApiUrl(String serverApiUrl) {
		this.serverApiUrl = serverApiUrl;
	}


	public String getServerUser() {
		return serverUser;
	}

	public void setServerUser(String serverUser) {
		this.serverUser = serverUser;
	}

	public String getServerPassword() {
		return serverPassword;
	}

	public void setServerPassword(String serverPassword) {
		this.serverPassword = serverPassword;
	}

}
