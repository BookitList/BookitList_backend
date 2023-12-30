package cotato.bookitlist.book.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Repository
@HttpExchange
public interface AladinRepository {

    @GetExchange
    String findAllByQuery(@RequestParam String TTBKey, @RequestParam String Query, @RequestParam String Output, @RequestParam int Start);

}
