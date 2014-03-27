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

package org.ticpy.tekoporu.message;

import javax.enterprise.inject.Alternative;

import org.ticpy.tekoporu.util.Beans;
import org.ticpy.tekoporu.util.ResourceBundle;
import org.ticpy.tekoporu.util.Strings;

/**
 * @author SERPRO
 */
@Alternative
public class DefaultMessage implements Message {

	private final String originalText;

	private String parsedText;

	private final SeverityType severity;

	private final Object[] params;

	private final ResourceBundle bundle;

	public static final SeverityType DEFAULT_SEVERITY = SeverityType.INFO;

	public DefaultMessage(String text, SeverityType severity, Object... params) {
		this.originalText = text;
		this.severity = (severity == null ? DEFAULT_SEVERITY : severity);
		this.params = params;
		this.bundle = Beans.getReference(ResourceBundle.class);
	}

	public DefaultMessage(String text, Object... params) {
		this(text, null, (Object[]) params);
	}

	public String getText() {
		initParsedText();
		return parsedText;
	}

	private void initParsedText() {
		if (parsedText == null) {
			if (Strings.isResourceBundleKeyFormat(originalText)) {
				parsedText = bundle.getString(Strings.removeBraces(originalText));

			} else if (originalText != null) {
				parsedText = new String(originalText);
			}

			parsedText = Strings.getString(parsedText, params);
		}
	}

	public SeverityType getSeverity() {
		return severity;
	}

	@Override
	public String toString() {
		initParsedText();
		return Strings.toString(this);
	}
}
