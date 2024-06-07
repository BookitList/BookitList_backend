package cotato.bookitlist.config;

import cotato.bookitlist.book.service.AladinComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public AladinComponent aladinService() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://www.aladin.co.kr/ttb/api")
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(AladinComponent.class);
    }
}
