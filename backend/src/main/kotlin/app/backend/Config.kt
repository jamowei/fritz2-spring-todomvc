package app.backend

import app.backend.data.ToDoModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Bean
    fun todoModule() = ToDoModule
}

