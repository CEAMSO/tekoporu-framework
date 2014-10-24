package ${package}.clientrest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import ${package}.configs.APPServerConfig;


public class AuthServiceCR  {
	private static final long serialVersionUID = 1L;

	
	public ResteasyWebTarget getTargetPerfiles(String username) {
		APPServerConfig config = new APPServerConfig();
		DefaultHttpClient httpClient = new DefaultHttpClient();
		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);
		
		if (config.getDncpPerfilesServerAuthenticated()) {
			httpClient.getCredentialsProvider().setCredentials(
	                new AuthScope(config.getDncpPerfilesHostServerDomain(), config.getDncpPerfilesServerPort()),
	                new UsernamePasswordCredentials(config.getDncpPerfilesServerUser(), config.getDncpPerfilesServerPassword()));
		}
		
		ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();
        ResteasyWebTarget target = client.target(config.getDncpPerfilesHostServer() + config.getDncpPerfilesUrl()).path("/{user}").resolveTemplate("user", username);;

        return target;
	}
	
	/*
	public ResteasyWebTarget getTarget(String username) {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.getCredentialsProvider().setCredentials(
                new AuthScope("localhost", 8088),
                new UsernamePasswordCredentials("admin", "secret"));
		ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpClient);
		ResteasyClient client = new ResteasyClientBuilder().httpEngine(engine).build();
        ResteasyWebTarget target = client.target("http://localhost:8088/stje/rest" + "/tahachi/perfiles").path("/{user}").resolveTemplate("user", username);
        return target;
        //Si se tienen queryparams agregarlos con algunos de los métodos .queryParam[s] en el target 
	}
	*/
	
	public List<Map<String, String>> getPermisos(String username) {
        Response response = getTargetPerfiles(username).request().get();
        
        /* Usando Jackson
        String json = response.readEntity(String.class);
        response.close();
  
        ObjectMapper mapper = new ObjectMapper();
        PagedList<Usuario> root = null;
        try {
			root = mapper.readValue(json, new TypeReference<PagedList<Usuario>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
        */
        List<Map<String, String>> root;
        try {
        	 GenericType<List<Map<String, String>>> listType = new GenericType<List<Map<String, String>>>() {};
             root = response.readEntity(listType);
		} catch (Exception e) {
			e.printStackTrace();
			root = new ArrayList<Map<String, String>>();
			root.add(new HashMap<String, String>());
		}
       


        return root;
	}
	
	
}
