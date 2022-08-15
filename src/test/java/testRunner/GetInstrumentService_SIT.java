package testRunner;
import io.restassured.response.Response;
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

public class GetInstrumentService_SIT extends UtilityClass
{
    Response response;
    @Test
   public void InstrumentService() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
       String signature = signatureUtilGenerator();
       String KeyId= getGlobalValue("UAT");
       String InstrumentId=getGlobalValue("SITItemId");
       disableSSLVerification();
       Response res= given()
               .baseUri("https://localhost:443")
               .log().all().header("host","localhost")
                .header("x-timestamp", Instant.now().toEpochMilli())
                .header("x-request-method","GET")
                .header("x-request-path","/instrument-details/instruments/"+InstrumentId)
                .header("x-key-Id","ltasD+JmKhIoVPQRzpyuP8V8nP1S9dUrI930yeg53t0=")
                .header("x-signature",signature)
                .relaxedHTTPSValidation("TLS")
                .when().get("/instrument-details/credit-card/"+InstrumentId)
                .then().log().all().extract().response();
        int statusCode = res.getStatusCode();
        System.out.println(200);
       assertEquals(statusCode,200);

    }

}
