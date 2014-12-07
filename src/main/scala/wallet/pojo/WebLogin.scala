package pojo

import javax.validation.constraints.NotNull
import scala.collection.JavaConversions._
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


class WebLogin{
	
	@Id
	var login_id:String = _

	@NotNull
	var url:String = _
	
	@NotNull
	var login:String = _
	
	@NotNull
	var password:String = _

	var userid : String = _

	def setUserid(userid: String)
	{
		this.userid = userid
	}

	def getUserid(): String = userid

	def getLoginid():String = login_id

	def getUrl():String  = url

	def getLogin():String = login

	def getPassword():String = password

	def setLoginid(login_id:String)
	{
		this.login_id = login_id
	}

	def setUrl(url:String)
	{
		this.url = url
	}

	def setLogin(login:String)
	{
		this.login = login
	}

	def setPassword(password:String)
	{
		this.password = password
	}

}