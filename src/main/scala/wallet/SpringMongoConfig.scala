package main.scala.wallet

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.authentication._
import com.mongodb.MongoClient
//remove if not needed
import scala.collection.JavaConversions._

@Configuration
class SpringMongoConfig {

  @Bean
  def mongoTemplate(): MongoTemplate = {
    val mongoTemplate = new MongoTemplate(new MongoClient("ds047720.mongolab.com:47720"), "digitalwallet", new UserCredentials("ashishsjsu","ashishsjsu"))
    mongoTemplate
  }
}