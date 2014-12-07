package pojo

import org.joda.time.DateTime
import javax.validation.constraints.NotNull
//remove if not needed
import scala.collection.JavaConversions._
import java.io._
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import collection.mutable.{Set, Map, HashMap, MultiMap}
import java.util.ArrayList

object User{

}

//@SerialVersionUID(15L)
@Document(collection = "users")
    class User(var mail:String, var pass: String){

  	@Id
  	var user_id: String = _
 
  	@NotNull
  	var email: String = mail

  	@NotNull
  	var password: String = pass
  
  	var createdAt: String = _

	

   	def getId(): String = user_id

	  def getEmail():String = email

	  def getPassword():String = password

	  def getCreatedAt():String = createdAt

  	def setId(user_id: String) {
    	this.user_id = user_id
  	}

	def setEmail(email:String){
		this.email = email;
	}

	def setpassword(password:String){
		this.password = password;
	}
	
	def setCreatedAt(createdAt:String){
		this.createdAt = createdAt;
	}


   	override def toString(): String = {"[user_id: " + user_id + ", email: " + email + ", password: " + password + 
      ", createdAt: " + createdAt.toString + "]" + "\n"
  	}

  	//auxillary
  	def this()={
  		this(null, null)
  	}

 
}



