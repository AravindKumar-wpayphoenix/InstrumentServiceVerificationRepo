package testRunner;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import sun.security.timestamp.TSResponse;
import utilityFiles.signatureGeneratorUtil;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class getInstrumentService extends signatureGeneratorUtil
{
    Response response;
    @Test
   public void InstrumentService() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers()
                    {
                        return null;
                    }
                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                        //No need to implement.
                    }
                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                    {
                        //No need to implement.
                    }
                }
        };

// Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
       String signature = signatureUtilGenerator();
       String KeyId= getGlobalValue("UAT");
       //RestAssured.baseURI ="https://localhost:443";
        // RestAssured.useRelaxedHTTPSValidation();
       Response res= given()
               .config(RestAssured.config().sslConfig(
                       new SSLConfig().allowAllHostnames()))
               .baseUri("https://localhost:443")
               .log().all().header("host","localhost")
                .header("x-timestamp", Instant.now().toEpochMilli())
                .header("x-request-method","GET")
                .header("x-request-path","/instrument-details/instruments/279048")
                .header("x-key-Id",KeyId)
                .header("x-signature",signature)
                .relaxedHTTPSValidation("SSL")
                .when().get("/instrument-details/credit-card/279408")
                .then().log().all().extract().response();
        int statusCode = response.getStatusCode();
       assertEquals(statusCode,200);

    }

}
