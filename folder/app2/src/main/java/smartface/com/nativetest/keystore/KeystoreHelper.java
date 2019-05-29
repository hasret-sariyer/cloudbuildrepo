package smartface.com.nativetest.keystore;

import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;

public interface KeystoreHelper  {

    void generateKeyPair() throws Exception;

    Key getKey();

    byte[] encryptData(String plainText)throws  Exception;

    byte[] decyptData() throws  Exception;

    KeyStore initializeKeyStore() throws Exception;


    void deleteEntry();
}
