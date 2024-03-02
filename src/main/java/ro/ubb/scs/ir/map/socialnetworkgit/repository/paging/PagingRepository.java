package ro.ubb.scs.ir.map.socialnetworkgit.repository.paging;


import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.Repository;

public interface PagingRepository<ID, E extends Entity<ID>> {
    Page<E> findAllOnPage(Pageable pageable);
}
