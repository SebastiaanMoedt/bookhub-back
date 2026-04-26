package fr.eni.bookhub_back.config;

import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// -------------------------------------------------------------------------------------------
// Jackson est la bibliothèque Java qui convertit tes objets en JSON et vice versa.
// SpringBoot l'intègre et l'utilise automatiquement dans tous les @RestController
// Quand Hibernate charge une entité en LAZY, il ne retourne pas directement l'objet — il retourne un proxy (un faux objet) qui contient des propriétés internes comme hibernateLazyInitializer et handler.
// Hibernate6Module = traducteur entre Hibernate et Jackson
// dit à Jackson : Si tu rencontres un proxy Hibernate, ignore hibernateLazyInitializer et handler,
// et sérialise uniquement les vraies propriétés de l'objet"
// -------------------------------------------------------------------------------------------

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer hibernateModule() {
        return builder -> {
            Hibernate6Module module = new Hibernate6Module();
            module.disable(Hibernate6Module.Feature.USE_TRANSIENT_ANNOTATION);
            builder.modulesToInstall(module);
        };
    }
}