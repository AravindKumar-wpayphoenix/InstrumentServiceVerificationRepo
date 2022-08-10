package utilityFiles;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;
public class signatureGeneratorUtil
{
    public String signatureUtilGenerator() throws NoSuchAlgorithmException, InvalidKeyException {

        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter host, xRequestMethod, xRequestPath and secret :");
        String host = "localhost";
        String xRequestMethod = "GET";
        String xRequestPath = "/instrument-details/credit-card/279408";
        String secretKey = "ltasD+JmKhIoVPQRzpyuP8V8nP1S9dUrI930yeg53t0=";
        long xTimestamp = Instant.now().toEpochMilli();
        System.out.println("host: " + host);
        System.out.println("xRequestMethod: " + xRequestMethod);
        System.out.println("xRequestPath: " + xRequestPath);
        System.out.println("xTimestamp: " + xTimestamp);
        System.out.println("secretKey: " + secretKey);
        System.out.println("Generating Signature.. ");
        String signature = generateSignature(host,xRequestMethod,xRequestPath,xTimestamp,secretKey);
        System.out.println("Generated Signature: " + signature);
        return signature;
    }
    public static String generateSignature(String host,String xRequestMethod,String xRequestPath,long xTimestamp,String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {

        String signatureStr= host + //host
                xTimestamp + //x-timestamp
                xRequestMethod + //x-request-method
                xRequestPath;//x-request-path
        SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "HmacSHA256");
        Mac aKeyId = Mac.getInstance("HmacSHA256");
        aKeyId.init(secretKeySpec);
        aKeyId.update(signatureStr.getBytes());
        byte[] aHeaders = aKeyId.doFinal();
        return Base64.getEncoder().encodeToString(aHeaders);
    }

    public  String getGlobalValue(String Key) throws IOException, IOException {
        Properties prop =new Properties();
        FileInputStream fis =new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\propertyFile\\config.properties");
        prop.load(fis);
        return prop.getProperty(Key);

    }

    }
