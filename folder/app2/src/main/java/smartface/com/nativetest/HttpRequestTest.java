package smartface.com.nativetest;

import android.net.SSLCertificateSocketFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.internal.Util;
import okhttp3.internal.Version;

/**
 * Created by smartface on 13.09.2018.
 */

public class HttpRequestTest extends AppCompatActivity {

    String url= "https://teknosa-proxy-alperozisik.c9users.io/mobileauth/token2cookie";
    String tokenUrl = "https://teknosa-proxy-alperozisik.c9users.io/authorizationserver/oauth/token";
    int timeout = 10000;

    CookieJar cookieJar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.httrequest);

        cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));

        Button btn = (Button) findViewById(R.id.requestID);

        Button authBtn = (Button) findViewById(R.id.authBtn);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("onclick !");
                try {
                    sendHttpRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        authBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("onclick !");
                try {
                   ((PersistentCookieJar) cookieJar).clear();
                    sendAuthHttpRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Call  sendHttpRequest() throws Exception {

        OkHttpClient.Builder clientBuilder =new OkHttpClient.Builder();

        OkHttpClient client = clientBuilder
                .cookieJar(cookieJar)
                .build();



        OkHttpCallbackClass callback = new OkHttpCallbackClass();

        Request rs = createRequest();
        Call newCall = client.newCall(rs);
        newCall.enqueue(callback);

        return newCall;

    }

    public Request createRequest( ){

        Request.Builder request = new Request.Builder();
        request.url(url);
        request.addHeader("Authorization", "Bearer e68a9c88-0cd0-418e-aac9-c1847d1a7be2");
        request.addHeader("Referer","/mobileauth/auth");
        request.addHeader("Cache-Control","no-cache");
//        request.cacheControl(CacheControl.FORCE_NETWORK);


        request.method("GET",null);

        return request.build();
    }

    public OkHttpClient.Builder createOwnSSLCertificate() throws  Exception{
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        KeyStore keyStore = readKeyStore(); //your method to obtain KeyStore
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "keystore_pass".toCharArray());
        sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(), new SecureRandom());
        client.sslSocketFactory(sslContext.getSocketFactory() , (X509TrustManager) trustManagerFactory.getTrustManagers()[0]);

        return client;
    }

    public KeyStore readKeyStore() throws Exception {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        // get user password and file input stream
        char[] password = "123456".toCharArray();

        java.io.FileInputStream fis = null;
        try {
//            fis = new FileInputStream("keyStoreName");
            ks.load(null, password);
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            if (fis != null) {
                fis.close();
            }
        }
        return ks;
    }

    public OkHttpClient.Builder getUnSafeHttpsClient(){
      try {
          final TrustManager[] trustManagers = new TrustManager[]{
                  new X509TrustManager() {
                      @Override
                      public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                      }

                      @Override
                      public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                      }

                      @Override
                      public X509Certificate[] getAcceptedIssuers() {
                          return new X509Certificate[]{};
                      }
                  }
          };

          final SSLContext sslContext  = SSLContext.getInstance("SSL");
          sslContext.init(null, trustManagers , new java.security.SecureRandom());

          final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

          OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
          clientBuilder.sslSocketFactory(sslSocketFactory);
          clientBuilder.hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

          return clientBuilder;

      } catch (Exception e) {
          throw new RuntimeException(e);
      }
    };

    public class MyCookieJar implements CookieJar {

        private List<Cookie> cookies;

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            this.cookies =  cookies;
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            if (cookies != null)
                return cookies;
            return new ArrayList<Cookie>();

        }
    }


    public class OkHttpCallbackClass implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            System.out.println("onFailure " +  e.getMessage());
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            System.out.println("response " +  response.body().string() + "  message   " + response.headers().toString());
        }
    }


    public Call  sendAuthHttpRequest() throws Exception {

        OkHttpClient.Builder clientBuilder =new OkHttpClient.Builder();

        OkHttpClient client = clientBuilder
                .build();


        OkHttpCallbackClass callback = new OkHttpCallbackClass();

        Request rs = createAuthRequest();
        Call newCall = client.newCall(rs);
        newCall.enqueue(callback);

        return newCall;

    }

    public Request createAuthRequest( ){

        Request.Builder request = new Request.Builder();
        request.url(tokenUrl);
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");


        RequestBody formBody = new FormBody.Builder()
                .add("client_id", "mobile_client")
                .add("client_secret","m0b!l3Cl!3nt" )
                .add("grant_type", "password")
                .add("username","furkan.arabaci@smartface.io" )
                .add("password","123456")
                .build();

        request.method("POST",formBody);

        return request.build();
    }


}


