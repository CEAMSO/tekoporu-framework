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

package ${package}.domain;

import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	@Column
	private Long id;

	@Column
	private boolean activo;

	@Column(length = 60)
	private String apellido;

	@Column(length = 100)
	private String email;

	@Column(length = 60)
	private String nombre;

	@Column(length = 64)
	private String pwd;

	@Column(length = 20)
	private String telefono;

	@Column(length = 25)
	private String username;

	@ManyToMany
	private Set<Rol> roles;

	public Usuario() {
		super();
	}

	public Usuario(boolean activo, String apellido, String email,
			String nombre, String pwd, String telefono, String username) {

		this.activo = activo;
		this.apellido = apellido;
		this.email = email;
		this.nombre = nombre;
		this.pwd = pwd;
		this.telefono = telefono;
		this.username = username;

	}

	public Usuario(Long id, String username, String nombre, String apellido,
			String telefono, String email) {

		this.id = id;
		this.username = username;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.email = email;

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public static Usuario valueOf(String json) {
		ObjectMapper mapper = new ObjectMapper();
		Usuario usuario = null;

		try {
			usuario = mapper.readValue(json, Usuario.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return usuario;
	}

	public String toString() {
		ObjectMapper mapper = new ObjectMapper();
		StringWriter json = new StringWriter();

		try {
			mapper.writeValue(json, this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return json.toString();
	}

}
