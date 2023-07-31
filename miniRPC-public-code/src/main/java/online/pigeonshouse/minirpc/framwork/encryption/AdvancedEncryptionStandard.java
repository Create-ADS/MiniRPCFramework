package online.pigeonshouse.minirpc.framwork.encryption;

import online.pigeonshouse.minirpc.api.framwork.encryption.EncryptionStandard;

import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public final class AdvancedEncryptionStandard implements EncryptionStandard {
    final String ALGORITHM = "AES";
    Charset charset = StandardCharsets.UTF_8;
    private static SecretKey KEY;

    public AdvancedEncryptionStandard(String keyPath) {
        File file = new File(keyPath);
        if (!file.exists()){
            try(ObjectOutputStream out  = new ObjectOutputStream(Files.newOutputStream(Paths.get(file.getAbsolutePath())))){
                KEY = generateKey();
                out.writeObject(KEY);
            }catch (IOException | NoSuchAlgorithmException e){
                throw new RuntimeException(e);
            }
        }else {
            KEY = getDown(file);
        }
    }

    public SecretKey getKEY() {
        return KEY;
    }

    public SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator secretGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = new SecureRandom();
        secretGenerator.init(secureRandom);
        return secretGenerator.generateKey();
    }

    /**
     * 加密
     */
    @Override
    public byte[] encrypt(String content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return aes(content.getBytes(charset),Cipher.ENCRYPT_MODE,KEY);
    }

    @Override
    public byte[] encrypt(byte[] content) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return aes(content,Cipher.ENCRYPT_MODE,KEY);
    }

    /**
     * 解密
     */
    @Override
    public String decryptToString(byte[] contentArray) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        byte[] result =  aes(contentArray,Cipher.DECRYPT_MODE,KEY);
        return new String(result,charset);
    }

    @Override
    public byte[] decryptToCharArray(byte[] contentArray) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        return aes(contentArray,Cipher.DECRYPT_MODE,KEY);
    }

    private byte[] aes(byte[] contentArray, int mode, SecretKey secretKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, secretKey);
        return cipher.doFinal(contentArray);
    }

    private SecretKey getDown(File file){
        try(ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file.toPath()))){
            return (SecretKey) in.readObject();
        }catch (IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
}
