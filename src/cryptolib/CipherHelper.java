package cryptolib;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author eewest
 */
public class CipherHelper {

    private Cipher cipher = null;
    
    private boolean useCipherSalt = true;
    private boolean usePasswordSalt = true;
    
    private String cipherAlgorithm = AES_ALGORITHM;
    
    private String cipherMode = CBC_MODE;
    
    public static final String CBC_MODE = "/CBC/PKCS5Padding";
    public static final String ECB_MODE = "/ECB/PKCS5Padding";
    
    public static final String AES_ALGORITHM = "AES";
    public static final String DES_ALGORITHM = "DES";

    
    
    public CipherHelper() {
        try {
            //prepare cipher to use AES with Cipher Block Chaining and Padding
            cipher = Cipher.getInstance(cipherAlgorithm + cipherMode);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(CipherHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Takes in a byte[] of encryptedData and encrypts it using a user generated
     * password randomly generated 16 bytes of encryptedData to salt the
     * password.The output for this is a byte[] containing in order salt(0-15),
     * iv(16-31), and the encrypted encryptedData(32-END).
     *
     * @param data byte array to be encrypted
     * @param key String turned byte array to be used for password
     * @return
     */
    public byte[] encryptData(byte[] data, byte[] key) {
        try {
            
            //generate a salt to add to password
            byte[] salt = generateSalt();
            //Take key and salt to generate a SecretKeySpec to use for cipher
            SecretKeySpec secretKey = generateSecretKey(key, salt);
            
            if (secretKey == null) {
                //do something
                return null;
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] encryptedData = cipher.doFinal(data);
                
                byte[] iv = (useCipherSalt) ? cipher.getIV() : null;
                byte[] combinedData = new byte[encryptedData.length + ((useCipherSalt && iv != null) ? iv.length : 0) + salt.length];

                //copy salt + iv + cipherText to a combined array
                System.arraycopy(salt, 0, combinedData, 0, salt.length);
                if(useCipherSalt && iv != null){
                    System.arraycopy(iv, 0, combinedData, salt.length, iv.length);
                }
                System.arraycopy(encryptedData, 0, combinedData, salt.length + ((useCipherSalt && iv != null) ? iv.length : 0), encryptedData.length);
                return combinedData;
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            //Logger.getLogger(CipherHelper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            System.out.println("Cipher is NULL: " + (cipher == null));
            System.out.println("Algorithm: " + cipher.getAlgorithm());
            System.out.println("Block Size: " + cipher.getBlockSize());
        }
        return null;
    }

    /**
     * Decrypt data using a password and salt as a key. Outputs the decrypted
     * data as a byte[].
     *
     * @param encryptedData
     * @param key
     * @param iv Initialization Vector for cipher
     * @param salt
     * @return
     */
    public byte[] decryptData(byte[] data, byte[] key, byte[] iv, byte[] salt) {
        try {
            
            SecretKeySpec secretKey = generateSecretKey(key, salt);

            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            if (secretKey == null) {
                //do something
                return null;
            } else {
                if(this.getUseCipherSalt()){
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
                }else{
                    cipher.init(Cipher.DECRYPT_MODE, secretKey);
                }
                byte[] decryptedData = cipher.doFinal(data);

                return decryptedData;
            }
        } catch (InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(CipherHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Generates a SecretKeySpec to use for encryption by using a key and salt
     *
     * @param key
     * @param salt
     * @return
     */
    private SecretKeySpec generateSecretKey(byte[] key, byte[] salt) {
        try {
            //
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(new String(key).toCharArray(), salt, 6000, cipher.getBlockSize() * 8);
            SecretKey temp = keyFactory.generateSecret(spec);
            if(cipherAlgorithm.equals(AES_ALGORITHM)){
                return new SecretKeySpec(temp.getEncoded(), "AES");
            }else{
                return new SecretKeySpec(temp.getEncoded(), "DES");
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(CipherHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private byte[] generateSalt() {
        try {
            SecureRandom rng = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            rng.nextBytes(salt);
            return salt;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(CipherHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    
    /**
     * Sets the algorithm of the cipher. The only supported alogrithms for this
     * project are the two listed as static variables AES_ALGORITHM and 
     * DES_ALGORITHM. Returns true if successful and false otherwise.
     * @param algorithm
     * @return true for success
     */
    public final boolean setAlgorithm(String algorithm){
        if(algorithm.equals(AES_ALGORITHM) || algorithm.equals(DES_ALGORITHM)){
            try {
                cipherAlgorithm = algorithm;
                cipher = Cipher.getInstance(cipherAlgorithm + cipherMode);
                return true;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
                Logger.getLogger(CipherHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    /**
     * Sets the mode of the cipher. The only supported versions are those listed
     * in the project, CBC_MODE and ECB_MODE. The functions will return true if 
     * the mode was successfully changed and false otherwise.
     * @param mode
     * @return 
     */
    private final boolean setCipherMode(String mode){
        if(mode.equals(CBC_MODE) || mode.equals(ECB_MODE)){
            try {
                cipherMode = mode;
                cipher = Cipher.getInstance(cipherAlgorithm + cipherMode);
                return true;
            } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
                Logger.getLogger(CipherHelper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public final int getBlockSize(){return cipher.getBlockSize();}
    
    public final void setUseCipherSalt(boolean  useSalt){
        useCipherSalt = useSalt;
        if(useCipherSalt){
            setCipherMode(CBC_MODE);
        }else{
            setCipherMode(ECB_MODE);
        }
    }
    
    public final boolean getUseCipherSalt(){
        return useCipherSalt;
    }
}
