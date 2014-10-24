package ${package}.util;


import java.util.List;


public class PagedList<T> {

	private List<T> list;

	private long total;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}
}