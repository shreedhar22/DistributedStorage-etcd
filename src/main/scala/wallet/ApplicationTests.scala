package main.scala.wallet

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List

import exception._


@RunWith(classOf[SpringJUnit4ClassRunner])
@SpringApplicationConfiguration(classes = Array(classOf[Application]))
@WebAppConfiguration
class ApplicationTests {

  @Test
  def testRoute(route: String): String = {

  	DisableSSLCertificateCheckUtil.disableChecks()
    
    val restTemplate = new RestTemplate()
    val URL = "https://www.routingnumbers.info/api/data.json?rn="+route

    val response = restTemplate.getForEntity(URL, classOf[String])
    var objectMapper: ObjectMapper = new ObjectMapper();
   	var responseJson: JsonNode = objectMapper.readTree(response.getBody());

   	var statusCode = responseJson.get("code").asInt()


    if (statusCode == 200) {
    		var name = responseJson.get("customer_name").asText()
    		return name
	} 
	else {
			throw new BankRecordNotFoundException("Not found")
	}
	return ""
  }

}