package controller

import exception._
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import javax.ws.rs.core.Request
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.ResponseBuilder
import javax.ws.rs.core.CacheControl
import javax.ws.rs.core.EntityTag
import org.joda.convert.FromString
import org.joda.convert.ToString
import org.joda.convert
import org.joda.time.DateTime
import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation._
import org.springframework.http.HttpStatus
import java.util.ArrayList
import scala.util.control.Breaks
import collection.mutable.{Set, Map, HashMap, MultiMap}
import scala.collection.JavaConversions._
import org.springframework.context.annotation.Configuration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.beans.factory.annotation.Autowired 
import javax.validation.Valid
import org.springframework.validation.BindingResult
import javax.servlet.http.HttpServletRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.BasicQuery
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import javax.annotation.Resource
import org.springframework.transaction.annotation.Transactional
import org.springframework.data.mongodb.core.mapping.DBRef
import org.bson.types.ObjectId


//import jetcd._
import java.net._
import com.google.common.util.concurrent.ListenableFuture;
import com.justinsb.etcd.EtcdClient;
import com.justinsb.etcd.EtcdClientException;
import com.justinsb.etcd.EtcdResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



import pojo._
import main.scala.wallet.SpringMongoConfig 
import main.scala.wallet.ApplicationTests

@Configuration
@EnableAutoConfiguration
@ComponentScan
@RestController
@Autowired
class WalletController{


  var etag: EntityTag = null
  val counter = new AtomicLong()
  val count = new AtomicLong()
  val logincount = new AtomicLong()
  val bankcount = new AtomicLong()
  val currentTime = DateTime.now

/*===========================================================================*/
  var client: EtcdClient = new EtcdClient(URI.create("http://54.173.91.98:4001"))
  var countValue:EtcdResult=_
/*===========================================================================*/

  val context = new AnnotationConfigApplicationContext(classOf[SpringMongoConfig])
  val mongoOperations = context.getBean("mongoTemplate").asInstanceOf[MongoOperations]


/********		Counter GET 	*********/

@RequestMapping(value = Array("api/v1/counter"), method = Array(RequestMethod.GET))
@ResponseBody
def setCounter():String = {

	
	var appkey: String = "/009431799/counter"
	var result:EtcdResult=this.client.get(appkey)
        var counter_response:String =""
	//var flag = false
          if(result==null){
             this.client.set(appkey,"1") 
             var initValue:EtcdResult=this.client.get(appkey)
           counter_response=initValue.node.value           
          }
         else{
           var res=result.node.value;
           var int_count=res.toInt;
           var setvalue_count=int_count+1;
           this.client.set(appkey,setvalue_count.toString) 
           var countValue:EtcdResult=this.client.get(appkey)
           counter_response=countValue.node.value
         }
	 return(counter_response)
       
        //println(result.node.value)
	/*try{
		println("Sending: " + appkey)
	var result:EtcdResult = this.client.get(appkey)
		println("Received: " + result)
	}
	catch{
		
		case e : Exception => println("Exception: " + e)

		var keyval = this.client.set(appkey, "0")
		flag = true
		
	}
		if(!flag)
		{
			var res:EtcdResult = this.client.get(appkey)
                         println(res)
			//var intCount = (res.value).toInt
			//var finalCount = intCount + 1
			//var update = this.client.set(appkey, finalCount.toString)
			//countValue=  this.client.get(appkey)
 		}
		
         */
	//return countValue

}


/****           USER  POST            ********/

  @RequestMapping(value = Array("api/v1/users"), method = Array(RequestMethod.POST), headers = Array("content-type=application/json"), consumes = Array("application/json"))
  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  def addUser(@Valid @RequestBody user: User, result: BindingResult): User = {
   
  	
    if (result.hasErrors()) {
      throw new ParameterMissingException(result.toString)
    } 
    else 
    {   	
      var userId = counter.incrementAndGet()
      user.setId("U-"+userId.toString())		
      user.setCreatedAt(currentTime.toString)

      println(user)
      mongoOperations.insert(user)
     
      return user
    }
  }

/****           USER  PUT            ********/

    @RequestMapping(value = Array("/api/v1/users/{id}"), method=Array(RequestMethod.PUT), headers = Array("content-type=application/json"), consumes=Array("application/json"))
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	def updateUser(@Valid @RequestBody user:User, result: BindingResult, @PathVariable id: String):User = {

			var userObj: User = null

			if(result.hasErrors())
			{
				throw new ParameterMissingException(result.toString)	
			}
			else
			{
				var query = new Query()
				query.addCriteria(Criteria.where("_id").is(id))
				userObj = mongoOperations.findOne(query, classOf[User])
				
				if(userObj == null)
				{
						throw new UserNotFoundException("User with user_id "+id+" not found")
				}

				userObj.setEmail(user.email)
				userObj.setpassword(user.password)
				userObj.setCreatedAt(currentTime.toString)
				mongoOperations.save(userObj)

			}
			return userObj;
	}//user put


	/****           USER  GET            ********/


	@RequestMapping(value = Array("/api/v1/users/{user_id}"), method=Array(RequestMethod.GET))
	@ResponseBody
	def getUser(@PathVariable user_id:String, @RequestHeader(value = "If-None-Match", required= false) ETag: String):ResponseEntity[_]={
	

		var httpresponseHeader:HttpHeaders = new HttpHeaders
		var cc: CacheControl = new CacheControl()
		//var user: User = getUserInfo(user_id)

		val query = new Query()
		query.addCriteria(Criteria.where("_id").is(user_id))
		val user = mongoOperations.findOne(query, classOf[User])

		if(user == null) 
		{
			throw new UserNotFoundException("User with user_id "+user_id+" not found")
		}

		cc.setMaxAge(86400)
		httpresponseHeader.setCacheControl(cc.toString())

		var tag: String = ETag
		println(tag)

		println("header1 :"+httpresponseHeader.toString())
		 // val cardmap = new HashMap[String, Set[IDCard]] with MultiMap[String, IDCard]

		etag = new EntityTag(Integer.toString(user.hashCode()))
		httpresponseHeader.add("Etag", etag.getValue())
		println("header2 :"+httpresponseHeader.toString())

		
		if(etag.getValue().equalsIgnoreCase(tag))
		{
         	System.out.println("foo")
        	return new ResponseEntity[String]( null, httpresponseHeader, HttpStatus.NOT_MODIFIED )
        } 
        else 
        {
        	return new ResponseEntity[User](user, httpresponseHeader, HttpStatus.OK )  
        }
		
	}

/****           IDCARD  POST            ********/

	@RequestMapping(value = Array("/api/v1/users/{userid}/idcards"), method = Array(RequestMethod.POST),headers=Array("content-type=application/json"), consumes=Array("application/json"))
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	def createIDCard(@Valid @RequestBody card:IDCard, result: BindingResult, @PathVariable userid:String):IDCard={
	 				
		if (result.hasErrors()) 
		{
      		throw new ParameterMissingException(result.toString)
       	}
       else
       {
       		if(!UserExists(userid))
		{
			throw new UserNotFoundException("User with user_id "+userid+" not found")
		}

       		var cardid = count.incrementAndGet()
       		card.setCardid("C-"+cardid.toString())
       		card.setUserid(userid)
       		mongoOperations.insert(card)
			//for upsert
       		//mongoOperation.findAndModify(query, args, FindAndModifyOptions.options().upsert(true), classOf[User]);
       				
       		return card
       }
	}

	
/**************      ID Card GET         ***********/
	@RequestMapping(value = Array("/api/v1/users/{userid}/idcards"), method = Array(RequestMethod.GET), headers = Array("content-type=application/json"))
	def getIDCards(@PathVariable userid:String) : java.util.List[IDCard] = 
	{

		if(!UserExists(userid))
		{
			throw new UserNotFoundException("User with user_id "+userid+" not found")
		}

		var queryCard: Query = new Query()
		queryCard.addCriteria(Criteria.where("userid").is(userid)) 
		var card = mongoOperations.find(queryCard, classOf[IDCard])
	
		println(card)

		return card

    }



/************    ID Card DELETE ***********/
	@RequestMapping(value=Array("/api/v1/users/{userid}/idcards/{card_id}"), method=Array(RequestMethod.DELETE),headers=Array("content-type=application/json"))
	@ResponseStatus(HttpStatus.NO_CONTENT)
	def deleteIdCard(@PathVariable userid:String, @PathVariable card_id:String)
	{
	
		if(!UserExists(userid))
		{
			throw new UserNotFoundException("User with user_id "+userid+" not found")
		}
		else
		{
			var queryCard: Query = new Query()
			queryCard.addCriteria(Criteria.where("_id").is(card_id))
			var usercard = mongoOperations.findAndRemove(queryCard, classOf[IDCard])

			if(usercard == null)
			{
				throw new CardNotFoundException("Card with card_id "+card_id+" not found")
			}
			println("User deleted: " + usercard)
		}
	}

/**********        Weblogins POST     *************/
	@RequestMapping(value=Array("/api/v1/users/{user_id}/weblogins"), method=Array(RequestMethod.POST), headers=Array("content-type=application/json"), consumes=Array("application/json"))
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	def addWebLogin(@Valid @RequestBody weblogin:WebLogin, result:BindingResult, @PathVariable user_id:String): WebLogin ={

		if(result.hasErrors)
		{
			throw new ParameterMissingException(result.toString())
		}
		else
		{
	 		if(!UserExists(user_id))
			{
				throw new UserNotFoundException("User with user_id "+user_id+" not found")
			}

			var login_id = logincount.incrementAndGet()
			weblogin.setLoginid("l-" + login_id.toString())
			weblogin.setUserid(user_id)
			mongoOperations.insert(weblogin)
			
		}

		return weblogin

	}//addWebLogin
	
	
/***************     Weblogins GET  ****************/	
	@RequestMapping(value=Array("/api/v1/users/{user_id}/weblogins"), method=Array(RequestMethod.GET), headers=Array("content-type=application/json"))
	def getWebLogins(@PathVariable user_id:String): java.util.List[WebLogin]=
	{
		if(!UserExists(user_id))
		{
			throw new UserNotFoundException("User with user_id "+user_id+" not found")
		}

		var queryWeblogin : Query = new Query()
		queryWeblogin.addCriteria(Criteria.where("userid").is(user_id)) 
		var weblogin = mongoOperations.find(queryWeblogin, classOf[WebLogin])
	
		println(weblogin)

		return weblogin
	}



/**************
*    WebLogins DELETE   ************/

	@RequestMapping(value=Array("/api/v1/users/{user_id}/weblogins/{loginid}"), method = Array(RequestMethod.DELETE), headers=Array("content-type=application/json"))
	@ResponseStatus(HttpStatus.NO_CONTENT)
	def deleteWebLogin(@PathVariable user_id:String, @PathVariable loginid:String)
	{
		if(!UserExists(user_id))
		{
			throw new UserNotFoundException("User with user_id "+user_id+" not found")
		}
		else
		{
			var query: Query = new Query()
			query.addCriteria(Criteria.where("_id").is(loginid))
			var login = mongoOperations.findAndRemove(query, classOf[WebLogin])
			println("login deleted: " + login)
		}
	}
	

	/********* Bank Account POST ***********/

	@RequestMapping(value=Array("/api/v1/users/{user_id}/bankaccounts"), method=Array(RequestMethod.POST), headers=Array("content-type=application/json"), consumes=Array("application/json"))
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	def addBankAccount(@Valid @RequestBody bankaccount: BankAccount, result:BindingResult, @PathVariable user_id: String): BankAccount=
	{
		if(result.hasErrors())
		{
			throw new ParameterMissingException(result.toString())
		}
		else
		{
			if(!UserExists(user_id))
			{
				throw new UserNotFoundException("User with user_id "+user_id+" not found")
			}		

			var routingno = bankaccount.getRoutingnumber()

			var dotest: ApplicationTests = new ApplicationTests()
  	        var customer = dotest.testRoute(routingno)

  	        bankaccount.setAccountname(customer)
			var bankid = bankcount.incrementAndGet()
			bankaccount.setBankid("b-"+bankid)
			bankaccount.setUserid(user_id)
			mongoOperations.insert(bankaccount)

		}

		return bankaccount
	}

	

	/*********** Bank Account GET **********************/

	@RequestMapping(value=Array("/api/v1/users/{user_id}/bankaccounts"), method = Array(RequestMethod.GET), headers=Array("content-type=application/json"))
	def getBankAccounts(@PathVariable user_id:String) : java.util.List[BankAccount]=
	{
		if(!UserExists(user_id))
		{
			throw new UserNotFoundException("User with user_id "+user_id+" not found")
		}

		var queryBank : Query = new Query()
		queryBank.addCriteria(Criteria.where("userid").is(user_id)) 
		var bankaccount = mongoOperations.find(queryBank, classOf[BankAccount])
	
		println(bankaccount)

		return bankaccount
	}


	/************* Bank Account Delete ****************/

	@RequestMapping(value=Array("/api/v1/users/{userid}/bankaccounts/{bank_id}"), method=Array(RequestMethod.DELETE), headers=Array("content-type=application/json"))
	@ResponseStatus(HttpStatus.NO_CONTENT)
	def deleteBankAccount(@PathVariable userid:String, @PathVariable bank_id:String)
	{

		if(!UserExists(userid))
		{
			throw new UserNotFoundException("User with user_id "+userid+" not found")
		}
		else
		{
			var query: Query = new Query()
			query.addCriteria(Criteria.where("_id").is(bank_id))
			var bankaccount = mongoOperations.findAndRemove(query, classOf[BankAccount])
			println("bankaccount deleted: " + bankaccount)
		}
	}

	
	def UserExists(userid: String): Boolean = {

		var queryUser: Query = new Query()
		queryUser.addCriteria(Criteria.where("_id").is(userid)) 
		var user = mongoOperations.findOne(queryUser, classOf[User])

		if(user == null)
		{
			return false
		}
		return true
	}

}//WalletController
