package cotato.bookitlist.book.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Service
@HttpExchange
public interface AladinService {

    @GetExchange
    String findAllByQuery(@RequestParam String TTBKey, @RequestParam String Query, @RequestParam String Output, @RequestParam int Start);

}
