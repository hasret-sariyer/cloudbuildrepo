package smartface.com.nativetest.keystore;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

import smartface.com.nativetest.R;

public class KeyStoreTest extends AppCompatActivity{

    Button encryptBtn;
    Button decryptBtn;
    Button removeBtn;
    TextView baseStringTextView;
    EditText keyEditText;
    final String key = "TEST_KEY69";
    final String default_text = "DEFAULTONE";
    Context parentContext;
//    WeakReference<AsyncTask> asyncTaskWeakReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout rootView =(LinearLayout) getLayoutInflater().inflate(R.layout.keystorelayout, null);

        setContentView(rootView);

        parentContext = this;

        encryptBtn = rootView.findViewById(R.id.encryptBtn);
        decryptBtn = rootView.findViewById(R.id.decryptBtn);
        removeBtn = rootView.findViewById(R.id.removeBtn);
        baseStringTextView  = rootView.findViewById(R.id.encodedString);
        keyEditText = rootView.findViewById(R.id.keyEditText);

        if("".isEmpty()){

        }else{

        }

        KeystoreHelperClass keystoreHelperClass = null;
        try {
            keystoreHelperClass = new KeystoreHelperClass(this,"test25");
        } catch (Exception e) {
            e.printStackTrace();
        }


        final KeystoreHelperClass finalKeystoreHelperClass = keystoreHelperClass;
        encryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String secretData = keyEditText.getText().toString().isEmpty() ? default_text : keyEditText.getText().toString();
                final String encryptedText;
                try {
                    finalKeystoreHelperClass.encryptData("TEST");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                  finalKeystoreHelperClass.decryptData();
//                    String decryptByteText = new String(decryptByte, "UTF-8");

//                    Log.d("decodedText", "Text " + "Decrypted Text  " + decodedText + "  length " + decodedText.length() );
//                  baseStringTextView.setText("Decrypted Text  " + decryptByteText );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalKeystoreHelperClass.deleteEntry();
            }
        });


        this.getLifecycle().addObserver(keystoreHelperClass);
    }


    private class KeystoreHelperClass implements LifecycleObserver {

        private String alias;
        private KeyStore keyStore;
        private final String AndroidKeyStore = "AndroidKeyStore";
        private final String RSA_MODE = "RSA/ECB/PKCS1Padding";
        private Context activityContext;
        private byte[]  toDecrypt = null;


        KeystoreHelperClass(Context context,String key) throws Exception {
            this.alias = key;
            this.activityContext = context;
            generateKeyPair();
        }

        public void generateKeyPair() throws Exception {
            keyStore = initializeKeyStore();
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, AndroidKeyStore);
            if(!keyStore.containsAlias(alias)){

                if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    Calendar end = Calendar.getInstance();
                    end.add(Calendar.YEAR, 30);
                    KeyPairGeneratorSpec specs = new KeyPairGeneratorSpec.Builder(activityContext)
                            .setAlias(alias)
                            .setSubject(new X500Principal("CN=" + alias))
                            .setStartDate(Calendar.getInstance().getTime())
                            .setEndDate(end.getTime())
                            .setSerialNumber(BigInteger.TEN)
                            .build();

                    keyPairGenerator.initialize(specs);
                    keyPairGenerator.generateKeyPair();
                }else {
                    KeyGenParameterSpec specs = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_DECRYPT
                            | KeyProperties.PURPOSE_ENCRYPT)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                            .setRandomizedEncryptionRequired(false)
                            .build();

                    keyPairGenerator.initialize(specs);
                    keyPairGenerator.generateKeyPair();
                }
            }
        }

        public void encryptData(String plainText) {
           AsyncTask<String, String , String> asyncTask = new AsyncTask<String, String , String>(){
                @Override
                protected String doInBackground(String... strings) {
                    try {
                        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
                        Cipher chiperText = Cipher.getInstance(RSA_MODE);
                        chiperText.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        CipherOutputStream cipherOutputStream = new CipherOutputStream(byteArrayOutputStream, chiperText);
                        cipherOutputStream.write(strings[0].getBytes());
                        cipherOutputStream.close();
                        toDecrypt = byteArrayOutputStream.toByteArray();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parentContext);
                        String base64 = Base64.encodeToString(toDecrypt, Base64.DEFAULT);
                        preferences.edit().putString("ENCRYPTDATA", base64).commit();
                    }catch (Exception e){
                        e.printStackTrace();
                        String errorMessage = e.getMessage() + "\n" + e.getStackTrace();
                        cancel(true);
                        return  errorMessage;
                    }
                    return "";
                }

               @Override
               protected void onCancelled(String s) {
                  String test = s;
               }

               @Override
                protected void onPostExecute(String s) {
                   Toast.makeText(parentContext, " encryptData over ", Toast.LENGTH_SHORT).show();
                }
           };
            asyncTask.execute(plainText);
        }

        public void decryptData(){

            AsyncTask<Void,Void, Void> asyncTask =
                    new AsyncTask<Void,Void, Void>(){
                @Override
                protected Void doInBackground(Void... strings) {
                    try {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(parentContext);
                        toDecrypt = Base64.decode(preferences.getString("ENCRYPTDATA", ""), Base64.DEFAULT);
                        if(toDecrypt == null)
                            throw new Exception("Secure value is not found");
                        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);

                        Cipher cipherText = Cipher.getInstance(RSA_MODE);
                        cipherText.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

                        CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(toDecrypt), cipherText);

                        ArrayList<Byte> byteArrayList = new ArrayList<>();

                        int nextByte;
                        while ((nextByte = cipherInputStream.read()) != -1) {
                            byteArrayList.add((byte) nextByte);
                        }

                        byte[] bytes = new byte[byteArrayList.size()];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = byteArrayList.get(i).byteValue();
                        }
//                        return  new String(bytes, "UTF-8");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void encryptedText) {
                    Toast.makeText(parentContext, " decryptData over ", Toast.LENGTH_SHORT).show();
                }
            };
            asyncTask.execute();
        }

        public KeyStore initializeKeyStore() throws Exception {
            KeyStore keyStore = KeyStore.getInstance(AndroidKeyStore);
            keyStore.load(null);

            return  keyStore;
        }

        public void deleteEntry() {
            try{
                keyStore.deleteEntry(alias);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void releaseTasks(){
//           if(asyncTaskWeakReference != null && (asyncTaskWeakReference.get().getStatus() != AsyncTask.Status.FINISHED )){
//           }
        }

    }
}
