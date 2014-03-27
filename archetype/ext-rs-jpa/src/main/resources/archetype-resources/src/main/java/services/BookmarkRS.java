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

import ${package}.business.BookmarkBC;
import ${package}.domain.Bookmark;

@Path("/bookmark")
public class BookmarkRS {

	@Inject
	private BookmarkBC bookmarkBC;

	@GET
	@Path("/")
	@RequiresPermissions("bookmark:read")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> getBooks(@QueryParam("page") int page,
			@QueryParam("start") int start, @QueryParam("limit") int limit,
			@QueryParam("sort") JSONParameterArray sort) {

		// System.out.println("===============================Entro al GET del servicio rest /books\n\npage: "+
		// page + " start: " + start + " limit: " + limit);
		Map<String, Object> root = new HashMap<String, Object>();
		List<Bookmark> finalList = new ArrayList<Bookmark>();

		// paginar o no
		if (page != 0) {
			bookmarkBC.setPaginated(true);
			bookmarkBC.setPage(page - 1, limit);
		} else {
			bookmarkBC.setPaginated(false);
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

			finalList = bookmarkBC.findAllOrderBy(stringOrderBy);

		} else {
			finalList = bookmarkBC.findAll();
		}

		// setear total
		if (this.bookmarkBC.getPagination() != null) {
			root.put("total", this.bookmarkBC.getPagination().getTotalResults());
		} else {
			root.put("total", finalList.size());
		}
		root.put("list", finalList);

		return root;
	}

	@DELETE
	@RequiresPermissions("bookmark:destroy")
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createBook(@PathParam("id") String id) {
		System.out.println("================ Delete Bookmark id: " + id + " ");
		bookmarkBC.delete(Long.parseLong(id));
		return Response.ok(true).build();
	}

	@POST
	@RequiresPermissions("bookmark:create")
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBooks(Bookmark book) {
		bookmarkBC.update(book);
		System.out.println("================ Create Bookmark id: "
				+ book.getId() + " ");
		return Response.ok(true).build();
	}

	@PUT
	@Path("/{id}")
	@RequiresPermissions("bookmark:update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateBook(@PathParam("id") String id, Bookmark book) {
		bookmarkBC.update(book);
		System.out.println("================ Created or Updated Bookmark id: "
				+ book.getId() + " ");
		return Response.ok(true).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBookmark(@PathParam("id") Long id) {
		System.out.println("================ Get User: " + id + " ");
		Bookmark b = bookmarkBC.findById(id);
		if (b == null) {
			return Response.ok(new HashMap<Object, Object>()).build();
		} else {
			return Response.ok(b).build();
		}	 
	}
}
