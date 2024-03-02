package ro.ubb.scs.ir.map.socialnetworkgit.repository.paging;

public class Pageable {
    private int pageNumber;

    private int pageSize;

    public Pageable(int pageNr, int pageSize) {
        this.pageNumber = pageNr;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }
}
