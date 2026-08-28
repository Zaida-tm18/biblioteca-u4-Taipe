package ec.edu.uteq.appweb.biblioteca.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * Cliente HTTP del lado del servidor con tiempos de espera acotados.
 *
 * Los timeouts NO son opcionales: sin ellos, una API externa lenta bloquea los
 * hilos del servidor y termina tumbando la aplicacion propia. Es el fallo en
 * cascada que describe Nygard en Release It!.
 *
 * API de Spring Boot 4: HttpClientSettings (paquete org.springframework.boot.http.client,
 * disponible desde 3.5.0) combinado con ClientHttpRequestFactoryBuilder.detect().
 * Como alternativa global existen las propiedades spring.http.clients.connect-timeout
 * y spring.http.clients.read-timeout, que aplican a todos los clientes a la vez.
 */
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClientExterno(RestClient.Builder builder,
                                        @Value("${app.api-externa.base-url}") String baseUrl,
                                        @Value("${app.api-externa.connect-timeout-ms:3000}") long conexionMs,
                                        @Value("${app.api-externa.read-timeout-ms:5000}") long lecturaMs) {
        HttpClientSettings ajustes = HttpClientSettings.defaults()
                .withTimeouts(Duration.ofMillis(conexionMs), Duration.ofMillis(lecturaMs));
        ClientHttpRequestFactory fabrica = ClientHttpRequestFactoryBuilder.detect().build(ajustes);
        return builder
                .baseUrl(baseUrl)
                .requestFactory(fabrica)
                .build();
    }
}
