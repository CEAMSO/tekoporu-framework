package ${package}.configs;

import org.ticpy.tekoporu.configuration.Configuration;

@Configuration(resource = "frontend")
public class FrontEndConfig {
	private String hostClientDomain;
	private String hostClientProtocol;
	private int hostClientPort;
	private String clientUrl;

	
	public String getHostClientDomain() {
		return hostClientDomain;
	}
	
	public void setHostClientDomain(String hostClientDomain) {
		this.hostClientDomain = hostClientDomain;
	}


	public String getHostClientProtocol() {
		return hostClientProtocol;
	}

	public void setHostClientProtocol(String hostClientProtocol) {
		this.hostClientProtocol = hostClientProtocol;
	}

	public int getHostClientPort() {
		return hostClientPort;
	}

	public void setHostClientPort(int hostClientPort) {
		this.hostClientPort = hostClientPort;
	}


	public String getHostClient() {
		return hostClientProtocol + "://" + hostClientDomain + ":" + hostClientPort + clientUrl;
	}
	
	public String getClientUrl() {
		return clientUrl;
	}

	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}

}
