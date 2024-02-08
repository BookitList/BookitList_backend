package cotato.bookitlist.book.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Component
@HttpExchange
public interface AladinComponent {

    @GetExchange("/ItemSearch.aspx")
    String findAllByQuery(@RequestParam String TTBKey, @RequestParam String Query, @RequestParam String Output, @RequestParam int Start, @RequestParam int MaxResults, @RequestParam int Version);

    @GetExchange("/ItemLookUp.aspx")
    String findByIsbn13(@RequestParam String TTBKey, @RequestParam String ItemId, @RequestParam String ItemIdType, @RequestParam String Output, @RequestParam int Version);

}
