package ${package}.security;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.session.SingleSignOutHandler;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.AbstractConfigurationFilter;
import org.jasig.cas.client.validation.Assertion;

public class APPSingleSignOutFilter extends AbstractConfigurationFilter {
	//org.jasig.cas.${package}.authentication.AuthenticationFilter;
	//org.jasig.cas.${package}.validation.Cas20ProxyReceivingTicketValidationFilter;
    private static final SingleSignOutHandler handler = new SingleSignOutHandler();

    public void init(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            handler.setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
            handler.setLogoutParameterName(getPropertyFromInitParams(filterConfig, "logoutParameterName", "logoutRequest"));
        }
        handler.init();
    }

    public void setArtifactParameterName(final String name) {
        handler.setArtifactParameterName(name);
    }
    
    public void setLogoutParameterName(final String name) {
        handler.setLogoutParameterName(name);
    }

    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        handler.setSessionMappingStorage(storage);
    }
    
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (handler.isTokenRequest(request)) {
            handler.recordSession(request);
        } else if (handler.isLogoutRequest(request)) {
            handler.destroySession(request);
            // Do not continue up filter chain
            //ServletContext sc = 
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            
            Enumeration e = httpRequest.getAttributeNames();
            	
            System.out.println("atributos enviados al logout ++++++");
            while(e.hasMoreElements()){
            	String param = (String) e.nextElement();
            	System.out.println(param);
            }
            //httpResponse.sendRedirect("/logout");
            System.out.println("Mandando al logout logoutRequest ++++++"  + httpRequest.getParameter("logoutRequest"));
            System.out.println("uri del ss out  ++++++"  + httpRequest.getRequestURI());
            request.setAttribute("SSOut", Boolean.TRUE);
            HttpSession session = request.getSession(false);
        	Assertion ass = (Assertion) (session == null ? request.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION));
        	String username = null;
        	if (ass != null) {
        		username = ass.getPrincipal().toString();
        	}
            System.out.println("Remote user: " + username);
            
            //WebUtils.issueRedirect(servletRequest, servletResponse, "/rest/logout");
            
            /*
            Subject subject = SecurityUtils.getSubject();

            try {
                subject.logout();
            } catch (SessionException ise) {
            	System.out.println("error en el Single Sign Out");
            }
            */
            
            //return;
        } else {
            log.trace("Ignoring URI " + request.getRequestURI());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        // nothing to do
    }
    
    protected static SingleSignOutHandler getSingleSignOutHandler() {
        return handler;
    }
}