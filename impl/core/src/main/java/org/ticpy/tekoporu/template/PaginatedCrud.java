package org.ticpy.tekoporu.template;

import java.util.List;

import org.ticpy.tekoporu.pagination.Pagination;

public interface PaginatedCrud<T, I> extends Crud<T, I> {
	
	void setPaginated(boolean paginated);
	
	boolean isPaginated();
	
	void setPage(int currentPage, int pageSize);
	
	List<T> findAllOrderBy(String sortProperty, String sortDirection);

	Pagination getPagination();
}
