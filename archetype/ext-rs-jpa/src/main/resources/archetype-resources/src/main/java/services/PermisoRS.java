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


package ${package}.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.ticpy.tekoporu.util.JSONParameterArray;
import org.ticpy.tekoporu.util.PagedList;

import ${package}.business.PermisoBC;
import ${package}.domain.Permiso;
import ${package}.domain.Rol;



@Path("/permiso")
public class PermisoRS {
	@Inject
	private PermisoBC permisoBC;

	@GET
	@Path("/")
	@RequiresPermissions("permiso:read")
	@Produces(MediaType.APPLICATION_JSON)
	public PagedList<Permiso> getRoles(@QueryParam("page") int page,
			@QueryParam("start") int start, @QueryParam("limit") int limit,
			@QueryParam("sort") JSONParameterArray sort) {

		System.out.println("=========Entro al GET del servicio rest /permiso\n\npage: "
						+ page + " start: " + start + " limit: " + limit);
		List<Permiso> finalList = new ArrayList<Permiso>();
		PagedList<Permiso> lista = new PagedList<Permiso>();
		lista.setList(new ArrayList<Permiso>());

		// paginar o no
		if (page != 0) {
			permisoBC.setPaginated(true);
			permisoBC.setPage(page - 1, limit);
		} else {
			permisoBC.setPaginated(false);
		}
		// setear order by
		if (sort != null) {
			String stringOrderBy = " ";
			int i = 0;

			for (Map<String, Object> sorter : sort.getArray()) {
				if (i != 0) {
					stringOrderBy += " , ";
				}
				
				String sortProperty = (String) sorter.get("property");
				String sortDirection = (String) sorter.get("direction");
				stringOrderBy += " " + sortProperty + " " + sortDirection + " ";
				i++;
			}

			finalList = permisoBC.findAllOrderBy(stringOrderBy);

		} else {
			finalList = permisoBC.findAll();
		}
		
		// setear total
		if (this.permisoBC.getPagination() != null) {
			lista.setTotal(this.permisoBC.getPagination().getTotalResults());
		} else {
			lista.setTotal(finalList.size());
		}
		lista.setList(finalList);

		System.out.print("============== Lista: " + lista.getList().toString()
				+ " tam: " + lista.getList().size() + "\n");

		return lista;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPermiso(@PathParam("id") Long id) {
		System.out.println("================ Get Permiso: " + id + " ");
		Permiso p = permisoBC.findById(id);
		if (p == null) {
			return Response.ok(new HashMap<Object, Object>()).build();
		} else {
			return Response.ok(p).build();
		}
	}

	@DELETE
	@RequiresPermissions("permiso:delete")
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createPermiso(@PathParam("id") Long id) {
		System.out.println("================ Delete Permiso: " + id + " ");
		permisoBC.delete(id);
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("success", true);
		return Response.ok(root).build();
	}

	@POST
	@RequiresPermissions("permiso:create")
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePermiso(Permiso permiso) {
		permisoBC.update(permiso);
		System.out.println("================ Create Permiso: "
				+ permiso.getClave() + " ");
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("success", true);
		return Response.ok(root).build();
	}

	@PUT
	@Path("/{id}")
	@RequiresPermissions("permiso:update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("id") Long id, Permiso permiso) {
		permisoBC.update(permiso);
		System.out.println("================ Created or Updated Permiso: "
				+ permiso.getClave() + " ");
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("success", true);
		return Response.ok(root).build();
	}
	
	
	@GET
	@Path("/unAssigned")
	@RequiresPermissions("permiso:read")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUnAssignedPermissions(@QueryParam("rolId") Long id ) {

		System.out.println("======Entro a unassigned permiso ");
		
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("success", true);
		
		List<Permiso> finalList = permisoBC.findUnAssignedByRol(id);
		
		root.put("permisos", finalList);

		return Response.ok(root).build();
	}
	
}
