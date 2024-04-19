package com.uhf.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import android.content.Context;

import com.uhf.uhfdemo.MyApp;

import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;

// Register Bouncy Castle provider
public class HttpRequestTask extends AsyncTask <String, Void, Void> {
    private final Context context;

    public HttpRequestTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        MLog.e("api: I am   here//////////////////////////////////////////");
        try {

            URL url = new URL("https://pkzyy4utja.execute-api.ap-northeast-1.amazonaws.com/dev/api/iforage/upload/data");
            // JSON payload to be sent in the request body
//            URL url = new URL("https://a39fzfc3avgs0g-ats.iot.ap-northeast-1.amazonaws.com:8443/topics/helloworld?qos=1");
//            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            conn.setSSLSocketFactory(getSSLContextWithCertAndPrivateKey(context).getSocketFactory());
            MLog.e("api:"+params[0]);
            String tid = params[0];
            String flight = params[1];
            String pnr = params[2];
            String status = "Check-in";
            String loc = "KLIA - Check-in";
            long time = System.currentTimeMillis();
            String requestBody = "{\"tid\": \""+tid+"\",\"flight_no\":\""+flight+"\",\"pnr\":\""+pnr+"\",\"status\":\""+status+"\",\"location\":\""+loc+"\",\"reader\":\"handheld001\",\"timestamp\":"+time+"}";
            // Open connection
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            //conn = (HttpsURLConnection) url.openConnection();
            //connection.setSSLSocketFactory(sslContext.getSocketFactory());
            //conn.setSSLSocketFactory(sslContext.getSocketFactory());

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check HTTP response code
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : " + responseCode);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }
            MLog.e("api: normal");
            conn.disconnect();

        } catch (MalformedURLException e) {
            MLog.e("api: error 1");
            e.printStackTrace();

        } catch (IOException e) {
            MLog.e("api: error 2"+e);
            e.printStackTrace();

        } catch (RuntimeException e) {
            // Handle the exception
            e.printStackTrace(); // Or log the error message
            // Perform appropriate action (e.g., show error message to user)
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
