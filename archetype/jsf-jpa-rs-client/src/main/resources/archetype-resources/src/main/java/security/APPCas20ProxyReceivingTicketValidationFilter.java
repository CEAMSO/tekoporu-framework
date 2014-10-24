package ${package}.security;

/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;

/**
 * Creates either a CAS20ProxyTicketValidator or a CAS20ServiceTicketValidator depending on whether any of the
 * proxy parameters are set.
 * <p>
 * This filter can also pass additional parameters to the ticket validator.  Any init parameter not included in the
 * reserved list {@link org.jasig.cas.${package}.validation.Cas20ProxyReceivingTicketValidationFilter#RESERVED_INIT_PARAMS}.
 *
 *
 */
public class APPCas20ProxyReceivingTicketValidationFilter extends Cas20ProxyReceivingTicketValidationFilter {
    
    /**
     * Template method that gets executed if ticket validation succeeds.  Override if you want additional behavior to occur
     * if ticket validation succeeds.  This method is called after all ValidationFilter processing required for a successful authentication
     * occurs.
     *
     * @param request the HttpServletRequest.
     * @param response the HttpServletResponse.
     * @param assertion the successful Assertion from the server.
     */
    protected void onSuccessfulValidation(final HttpServletRequest request, final HttpServletResponse response, final Assertion assertion) {
    	//HttpSession session = request.getSession(false);
    	//Assertion ass = (Assertion) (session == null ? servletRequest.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION));
    	String username = null;
    	if (assertion != null) {
    		username = assertion.getPrincipal().toString();
    	}
    	
    	request.setAttribute("username", username);
    	
    	System.out.println("Validacion correcta +++++++++++++++++++++++++++++++++++++++++ " + username );                                                                                         
    }
}
