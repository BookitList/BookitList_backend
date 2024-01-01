package cotato.bookitlist.config;

import cotato.bookitlist.book.service.AladinService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public AladinService aladinService() {
        RestClient restClient = RestClient.builder().baseUrl("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx").build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(AladinService.class);
    }
}
