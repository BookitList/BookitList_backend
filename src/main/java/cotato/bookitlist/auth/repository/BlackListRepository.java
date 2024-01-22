package cotato.bookitlist.auth.repository;

import cotato.bookitlist.auth.domain.BlackList;
import org.springframework.data.repository.CrudRepository;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
}
