package testRunner;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.Scanner;

public class SignatureGeneratorUtil {

    public static void main(String ...args) throws Exception{
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter host, xRequestMethod, xRequestPath and secret :");

        String host = myObj.nextLine();
        String xRequestMethod = myObj.nextLine();
        String xRequestPath = myObj.nextLine();
        String secretKey = myObj.nextLine();

        long xTimestamp = Instant.now().toEpochMilli();
        System.out.println("host: " + host);
        System.out.println("xRequestMethod: " + xRequestMethod);
        System.out.println("xRequestPath: " + xRequestPath);
        System.out.println("xTimestamp: " + xTimestamp);
        System.out.println("secretKey: " + secretKey);

        System.out.println("Generating Signature.. ");
        String signature = generateSignature(host,xRequestMethod,xRequestPath,xTimestamp,secretKey);
        System.out.println("Generated Signature: " + signature);

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



}
