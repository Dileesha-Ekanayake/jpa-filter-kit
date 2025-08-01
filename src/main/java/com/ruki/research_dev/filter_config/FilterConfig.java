package com.ruki.research_dev.filter_config;

import lk.dileesha.jpafilter.SpecificationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

@Configuration
public class FilterConfig {

    @Bean
    public SpecificationBuilder userSpecificationBuilder() {
        return SpecificationBuilder.builder()
                .withNestedField("genderid", "gender.id")
                .withDateField("dobirth", "dobirth")
                .withNestedField("roleid", "userRoles.role.id")
                .withNumericFields(Arrays.asList("id", "count"))
                .withDateSuffixes(Map.of("-from", true, "-to", false))
                .build();
    }
}
