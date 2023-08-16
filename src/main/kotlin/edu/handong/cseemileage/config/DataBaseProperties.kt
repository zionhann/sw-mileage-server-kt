package edu.handong.cseemileage.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "spring.datasource")
class DataBaseProperties(val url: String, val username: String, val password: String)
