package testRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utilityFiles.signatureGeneratorUtil;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class getInstrumentService extends signatureGeneratorUtil
{
    /**
     * Rigorous Test :-)
     */
    @Test
   public void InstrumentService() throws NoSuchAlgorithmException, InvalidKeyException {
       String signature = signatureUtilGenerator();
       System.out.println(signature);
        RestAssured.baseURI ="https://localhost:443";
        String res= given().log().all().header("host","localhost")
                .header("x-timestamp", Instant.now().toEpochMilli())
                .header("x-request-method","GET")
                .header("x-request-path","/instrument-details/instruments/279048")
                .header("x-key-Id","ltasD+JmKhIoVPQRzpyuP8V8nP1S9dUrI930yeg53t0=\n")
                .header("x-signature",signature)
                .when().get("/instrument-details/credit-card/279408")
                .then().log().all().extract().response().asString();
        System.out.println(res);

    }
}
