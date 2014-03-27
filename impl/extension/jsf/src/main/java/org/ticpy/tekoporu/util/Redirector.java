/*
 * TICPY Framework
 * Copyright (C) 2012 Plan Director TICs
 *
----------------------------------------------------------------------------
 * Originally developed by SERPRO
 * Demoiselle Framework
 * Copyright (C) 2010 SERPRO
 *
----------------------------------------------------------------------------
 * This file is part of TICPY Framework.
 *
 * TICPY Framework is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License version 3
 * along with this program; if not,  see <http://www.gnu.org/licenses/>
 * or write to the Free Software Foundation, Inc., 51 Franklin Street,
 * Fifth Floor, Boston, MA  02110-1301, USA.
 *
----------------------------------------------------------------------------
 * Este archivo es parte del Framework TICPY.
 *
 * El TICPY Framework es software libre; Usted puede redistribuirlo y/o
 * modificarlo bajo los términos de la GNU Lesser General Public Licence versión 3
 * de la Free Software Foundation.
 *
 * Este programa es distribuido con la esperanza que sea de utilidad,
 * pero sin NINGUNA GARANTÍA; sin una garantía implícita de ADECUACION a cualquier
 * MERCADO o APLICACION EN PARTICULAR. vea la GNU General Public Licence
 * más detalles.
 *
 * Usted deberá haber recibido una copia de la GNU Lesser General Public Licence versión 3
 * "LICENCA.txt", junto con este programa; en caso que no, acceda a <http://www.gnu.org/licenses/>
 * o escriba a la Free Software Foundation (FSF) Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
 */

package org.ticpy.tekoporu.util;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;

public class Redirector implements Serializable {

	private static final long serialVersionUID = 1L;

	public static void redirect(String viewId) {
		redirect(viewId, null);
	}

	public static void redirect(String viewId, Map<String, Object> params) {
		try {
			if (viewId != null && !viewId.isEmpty()) {
				FacesContext facesContext = Beans.getReference(FacesContext.class);
				ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
				String url = viewHandler.getBookmarkableURL(facesContext, viewId, parse(params), params == null ? false
						: !params.isEmpty());

				facesContext.getExternalContext().redirect(url);
			}

		} catch (NullPointerException cause) {
			throw new PageNotFoundException(viewId);

		} catch (IOException cause) {
			throw new FacesException(cause);
		}
	}

	private static Map<String, List<String>> parse(Map<String, Object> map) {
		Map<String, List<String>> result = null;

		if (map != null) {
			ArrayList<String> list;
			result = new HashMap<String, List<String>>();

			for (String key : map.keySet()) {
				list = new ArrayList<String>();
				list.add(map.get(key).toString());
				result.put(key, list);
			}
		}

		return result;
	}
}
