package app.backend

import app.model.TodoJacksonModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun todoJacksonModule() = TodoJacksonModule
}

