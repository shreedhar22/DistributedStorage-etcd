package pojo;

import javax.validation.constraints.NotNull
import scala.collection.JavaConversions._
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


class BankAccount{

	@Id
	var bank_id:String = _

	var accountname:String = _ 

	@NotNull
    var accountnumber:String = _

	@NotNull
	var routingnumber:String = _

	var userid : String = _

	def setUserid(userid: String)
	{
		this.userid = userid
	}

	def getUserid(): String = userid


	def getBankid():String = bank_id

	def getAccountname():String = accountname

	def getAccountnumber():String = accountnumber

	def getRoutingnumber():String = routingnumber

	def setBankid(bank_id: String) = {
		this.bank_id = bank_id
	}

	def setAccountname(accountname: String) = {
		this.accountname = accountname
	}

	def setAccountnumber(accountnumber: String) = {
		this.accountnumber = accountnumber
	}

	def setRoutingnumber(routingnumber:String)={
		this.routingnumber = routingnumber
	}
}