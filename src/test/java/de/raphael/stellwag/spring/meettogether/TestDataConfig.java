package de.raphael.stellwag.spring.meettogether;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

import java.io.IOException;

@Configuration
class TestDataConfig {
    @Value("classpath:data/event.json")
    private Resource eventData;

    @Value("classpath:data/message.json")
    private Resource messageData;

    @Value("classpath:data/person.json")
    private Resource personData;

    @Value("classpath:data/TimePlaceSuggestion.json")
    private Resource timePlaceSuggestionData;

    @Value("classpath:data/UserInEvent.json")
    private Resource userInEventData;

    @Value("classpath:data/UserInTimePlaceSuggestion.json")
    private Resource userInTimePlaceSuggestionData;

    @Autowired
    private ObjectMapper om;

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator(ObjectMapper OobjectMapper) throws IOException {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        // inject your Jackson Object Mapper if you need to customize it:
        factory.setMapper(om);

        Resource[] array = {eventData, messageData, personData, timePlaceSuggestionData, userInEventData, userInTimePlaceSuggestionData};
        factory.setResources(array);

        return factory;
    }
}