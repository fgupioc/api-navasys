package com.fgupioc.api_novasys.security.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class BeansConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }

    /*
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
    */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        // 🔹 Fechas en ISO-8601 (NO timestamps)
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 🔹 Ignorar propiedades desconocidas (APIs externas)
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // 🔹 No serializar nulls
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // 🔹 Manejo correcto de Java 8 Date/Time
        mapper.registerModule(new JavaTimeModule());

        // 🔹 Pretty print SOLO si es necesario
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }

}
