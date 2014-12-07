package pojo

import javax.validation.constraints.NotNull
import scala.collection.JavaConversions._
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "cards")
class IDCard(var name: String, var number: String, var expdate: String)
{	
	@Id
	var cardid : String =_
	
	@NotNull
	var cardname : String = name
	
	@NotNull
	var cardnumber : String = number
	
	var expirydate : String = _

	//@org.springframework.data.mongodb.core.mapping.DBRef
	var userid : String = _

	def setUserid(userid: String)
	{
		this.userid = userid
	}

	def getUserid(): String = userid

	def getCardid():String = cardid

	def getCardname(): String = cardname

	def getCardnumber(): String = cardnumber

	def getExpirydate():String = expirydate

	def setCardid(cardid:String)
	{
		this.cardid = cardid
	}

	def setCardname(cardname:String)
	{
		this.cardname = cardname
	}

	def setCardno(cardnumber:String)
	{
		this.cardnumber = cardnumber
	}

	def setExpirydate(expirydate:String)
	{
		this.expirydate = expirydate
	}

	def this()={
		this(null, null, null)
	}
	
	/*override def toString(): String = {"[cardid: " + cardid + ", cardname: " + cardname + ", cardnumber: " + cardnumber + 
      ", expirydate: " + expirydate + "]" + "\n"
  	}*/

  	override def toString(): String ={
  		return getCardid()
  	}
}