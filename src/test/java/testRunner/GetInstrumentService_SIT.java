package testRunner;
import io.restassured.response.Response;
import org.junit.Assert;
import utilityFiles.UtilityClass;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Properties;

public class GetInstrumentService_SIT extends UtilityClass
{
    Properties prop= new Properties();
    Response response;
    @Test
   public void InstrumentService() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String InstrumentId=getValue("SITItemId");
        String SecretKey=getValue("SITSecretKey");
        System.out.println(SecretKey);
        String signature = signatureUtilGenerator(SecretKey);
        disableSSLVerification();
       Response res= given()
               .baseUri("https://localhost:443")
               .log().all().header("host","localhost")
               .header("x-timestamp", System.getProperty("xTimestamp"))
                .header("x-request-method","GET")
                .header("x-request-path","/instrument-details/instruments/"+InstrumentId)
                .header("x-key-Id",getValue("SITKeyID"))
                .header("x-signature",signature)
                .relaxedHTTPSValidation("TLS")
                .when().get("/instrument-details/credit-card/"+InstrumentId)
                .then().log().all().extract().response();
        int statusCode = res.getStatusCode();
        System.out.println(200);
        Assert.assertEquals(200,statusCode);

    }

}
