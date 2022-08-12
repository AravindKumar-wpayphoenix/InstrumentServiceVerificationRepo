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

import javax.net.ssl.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Instant;

public class getInstrumentService extends signatureGeneratorUtil
{
    Response response;
    @Test
   public void InstrumentService() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
       String signature = signatureUtilGenerator();
       String KeyId= getGlobalValue("UAT");
        disableSSLVerification();
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
                .relaxedHTTPSValidation("TLS")
                .when().get("/instrument-details/credit-card/279408")
                .then().log().all().extract().response();
        int statusCode = response.getStatusCode();
       assertEquals(statusCode,200);

    }

    public void disableSSLVerification(){
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
