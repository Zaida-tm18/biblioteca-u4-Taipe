package ec.edu.uteq.appweb.biblioteca.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * Infraestructura de cache con Redis. Viene de la Unidad III y ya funciona.
 *
 * El namespace "libros" tiene un TTL corto porque el catalogo cambia con frecuencia.
 * El namespace "openlibrary" tiene TTL de 24 horas porque los metadatos bibliograficos
 * de un ISBN son practicamente inmutables: se usa en el TODO-U4-4.
 */
@Configuration
public class CacheConfig {

    public static final String CACHE_LIBROS = "libros";
    public static final String CACHE_OPENLIBRARY = "openlibrary";

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory conexion) {
        RedisCacheConfiguration porDefecto = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String, RedisCacheConfiguration> porNombre = new HashMap<>();
        porNombre.put(CACHE_LIBROS, porDefecto.entryTtl(Duration.ofMinutes(2)));
        porNombre.put(CACHE_OPENLIBRARY, porDefecto.entryTtl(Duration.ofHours(24)));

        return RedisCacheManager.builder(conexion)
                .cacheDefaults(porDefecto)
                .withInitialCacheConfigurations(porNombre)
                .build();
    }
}
