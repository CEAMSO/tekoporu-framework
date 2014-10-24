package ${package}.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This filter validates the CAS service ticket to authenticate the user.  It must be configured on the URL recognized
 * by the CAS server.  For example, in {@code shiro.ini}:
 * <pre>
 * [main]
 * casFilter = org.apache.shiro.cas.CasFilter
 * ...
 *
 * [urls]
 * /shiro-cas = casFilter
 * ...
 * </pre>
 * (example : http://host:port/mycontextpath/shiro-cas)
 *
 * @since 1.2
 */
public class CasFilter extends AuthenticatingFilter {
    
    private static Logger logger = LoggerFactory.getLogger(CasFilter.class);
    
    
    // the url where the application is redirected if the CAS service ticket validation failed (example : /mycontextpatch/cas_error.jsp)
    private String failureUrl;
    
    /**
     * The token created for this authentication is a CasToken containing the CAS service ticket received on the CAS service url (on which
     * the filter must be configured).
     * 
     * @param request the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String username = httpRequest.getParameter("username");
        return new UsernameToken(username);
    }
    
    /**
     * Execute login by creating {@link #createToken(javax.servlet.ServletRequest, javax.servlet.ServletResponse) token} and logging subject
     * with this token.
     * 
     * @param request the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return executeLogin(request, response);
    }
    
    /**
     * Returns <code>false</code> to always force authentication (user is never considered authenticated by this filter).
     * 
     * @param request the incoming request
     * @param response the outgoing response
     * @param mappedValue the filter-specific config value mapped to this filter in the URL rules mappings.
     * @return <code>false</code>
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }
    
    /**
     * If login has been successful, redirect user to the original protected url.
     * 
     * @param token the token representing the current authentication
     * @param subject the current authenticated subjet
     * @param request the incoming request
     * @param response the outgoing response
     * @throws Exception if there is an error processing the request.
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {
        issueSuccessRedirect(request, response);
        return false;
    }
    
    /**
     * If login has failed, redirect user to the CAS error page (no ticket or ticket validation failed) except if the user is already
     * authenticated, in which case redirect to the default success url.
     * 
     * @param token the token representing the current authentication
     * @param ae the current authentication exception
     * @param request the incoming request
     * @param response the outgoing response
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
        // is user authenticated or in remember me mode ?
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try {
                issueSuccessRedirect(request, response);
            } catch (Exception e) {
                logger.error("Cannot redirect to the default success url", e);
            }
        } else {
            try {
                WebUtils.issueRedirect(request, response, failureUrl);
            } catch (IOException e) {
                logger.error("Cannot redirect to failure url : {}", failureUrl, e);
            }
        }
        return false;
    }
    
    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }
    
    
    /**
     * Acquires the currently executing {@link #getSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse) subject},
     * a potentially Subject or request-specific
     * {@link #getRedirectUrl(javax.servlet.ServletRequest, javax.servlet.ServletResponse, org.apache.shiro.subject.Subject) redirectUrl},
     * and redirects the end-user to that redirect url.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @return {@code false} always as typically no further interaction should be done after user logout.
     * @throws Exception if there is any error.
     */
    
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    	HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String username = (String) httpRequest.getAttribute("username");
        Subject _subject = null;
       
        if (username != null) { 
        	System.out.println("======== Autenticando User:" + username);
        	
    		boolean failure = false;
    		try {
    			_subject = SecurityUtils.getSubject();
    			if (_subject != null) {
    				if (_subject.isAuthenticated()) {
    					if (username.compareTo((String) _subject.getPrincipal()) != 0) {
            	    		System.out.println("Hay un usuario distinto autenticado");
            	    		_subject.logout();
            	    		
            	    	}
        	    		System.out.println("ya está autenticado");
        	    		return true;
        	    	}
        	    	
    				_subject.login(new UsernameToken(username));
    			}
    		} catch (Exception e) {
    			failure = true;
    			System.out.println("======== Error al autenticar: " + username);
    			e.printStackTrace();
    		}
        }  
        
        return true;
        //return super.preHandle(request, response);
    }
}


