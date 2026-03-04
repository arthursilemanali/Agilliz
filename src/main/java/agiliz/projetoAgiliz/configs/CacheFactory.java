package agiliz.projetoAgiliz.configs;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CacheFactory {

    public <K,V> Cache<K,V> criarCache(int duracaoAteExpiracao, TimeUnit unidadeDeTempo, int tamanhoMaximoCache) {
        return Caffeine.newBuilder()
                .expireAfterWrite(duracaoAteExpiracao, unidadeDeTempo)
                .maximumSize(tamanhoMaximoCache)
                .build();
    }
}
