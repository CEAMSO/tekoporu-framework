/*
 * CEAMSO-USAID
 * Copyright (C) 2014 Governance and Democracy Program
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParameterArray {
	private List<Map<String, Object>> array;
	
	public JSONParameterArray() {
		super();
		this.array = new ArrayList<Map<String, Object>>();
	}

	public static JSONParameterArray valueOf(String json) {
		JSONParameterArray root = new JSONParameterArray();
		ObjectMapper mapper = new ObjectMapper();
		try {
			JsonFactory f = mapper.getFactory();
			//JsonNode actualObj = mapper.readTree(json);
			JsonParser jp = f.createParser(json);

			jp.nextToken(); // retorna START_ARRAY
			while (jp.nextToken() == JsonToken.START_OBJECT) {
				root.getArray().add(mapper.readValue(jp, Map.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return root;
	}

	public List<Map<String, Object>> getArray() {
		return array;
	}

	public void setArray(List<Map<String, Object>> array) {
		this.array = array;
	}

	
}
