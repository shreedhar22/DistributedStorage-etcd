package main.scala.wallet

import javax.net.ssl._
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import scala.collection.JavaConversions._

object DisableSSLCertificateCheckUtil {

  private class NullX509TrustManager extends X509TrustManager {

    def checkClientTrusted(chain: Array[X509Certificate], authType: String) {
      println()
    }

    def checkServerTrusted(chain: Array[X509Certificate], authType: String) {
      println()
    }

    def getAcceptedIssuers(): Array[X509Certificate] = Array.ofDim[X509Certificate](0)
  }

  private class NullHostnameVerifier extends HostnameVerifier {

    def verify(hostname: String, session: SSLSession): Boolean = true
  }

  def disableChecks() {
   /* try {
      new URL("https://0.0.0.0/").getContent
    } catch {
      case e: IOException => 
    }*/
    try {
      var sslc: SSLContext = null
      sslc = SSLContext.getInstance("TLS")
      val trustManagerArray: Array[TrustManager] = Array(new NullX509TrustManager())
      sslc.init(null, trustManagerArray, null)
      HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory)
      HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier())
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}
