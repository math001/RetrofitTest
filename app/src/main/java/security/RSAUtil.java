package security;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * RSA加解密
 * 
 * @author changlu.liu
 * 
 */
public final class RSAUtil {
	// public static final String PRIVATE_KEY = "privateKey";
	// public static final String PUBLIC_KEY = "publicKey";
	// public static final String MODULUS = "modulus";

	private static final String RSA = "RSA";
	/** 指定加密算法为DESede */
	private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

	/** 指定key的大小 */
	// private static final int KEYSIZE = 1024;
	/** 默认编码 */
	private static final String UTF8 = "UTF-8";

	// public static void main(String[] args)throws Exception{
	// //用来加解密随机对称密钥
	// String cryptograph =
	// "ZEDAI/zPDOjdKCT67k6ZiAOb9hc3wjaADKxaUKushN7dgVDelasjdIfT2rVe8lcwSjdd8yqHRHJC2Ji2PfOdM7ggNgg1jc+JPjDBMNcXtLTR8fuM/yaM5IjATMD4Cd3BUgHNJ/jb8xn+zqPDwZd1UkZb+XppBgr7zrJvvSUwb4g=";
	// String privateKey =
	// "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL0cYqD8ioYkexYK3eLjcxPrrgCDLtm8u2NWj0C96smIbkJl2rpXQW/ms75Ku3hJG0lfppEZm+QP+EZtZuPdLJi8oXNm4o1jIFxRihQXnS+l+mEZb9mj6VO32wr399JiJizs+Dg0zhppu7yiIDNy9j7vqYDSc4EP3bPbfakXLtmxAgMBAAECgYEAsUhw9baKLiL4+LsLM+5CLYSdxIE2ZIzXptK4zNq9xlyN8NUHbfTqtXqzOktY3/S5DdoLjq9RQ0XtXCinciof6RfPG0Nn+OK2gJFQedZGGPyI/I7h1f6lCYnWwxfOnYft053vQJzpJ5+lbABvLjRnDpgXpf+6Ao0PN6RcZ7PeOBkCQQD6GN/Cr80VslU828gvnU7MzIueRWCtsbSwKuQuil1WtgeyOCEKJhIv+TPtfTJjI6Xe7UBLw3G/whhYaepAVNZrAkEAwZMFyN6J9MeKuj1/Ekr3mLGXi9tCCHJn26CHy8Me/3lDqA8B6Cv+FrAPU8dWBCzja9zAsxbSAagAM0yhYPs/UwJANYdbZSURhSJNQiBcYWyO8CCbhX2d6q9NWCNmAVwWDDgbv5Zp4+0wGVqCNcOFJFj96I/pIg4r63oUkd/hhnFiDwJAVNgRIALSatSPLKRJrwXm0il86BL5NWSXuv74pknmq0lGa0HwrwehPeZ7QVhMjlXydFN0rspgz9MIC3QaA7z7+wJAInyeVIBBJtthdplcm5AgDKmtlmCiW4uS23KaW8vSYDVtQ326D4qKJAULnk94pVK4b3oUXibY0rtv9sIi8Z2HeQ==, ";
	//
	// System.out.println(RSAUtil.decrypt(cryptograph,privateKey));
	// }

	// /**
	// * 生成密钥对
	// */
	// public static Map<String, String> generateKeyPair() throws Exception {
	// /** RSA算法要求有一个可信任的随机数源 */
	// SecureRandom sr = new SecureRandom();
	// /** 为RSA算法创建一个KeyPairGenerator对象 */
	// KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
	// /** 利用上面的随机数据源初始化这个KeyPairGenerator对象 */
	// kpg.initialize(KEYSIZE, sr);
	// /** 生成密匙对 */
	// KeyPair kp = kpg.generateKeyPair();
	// /** 得到公钥 */
	// Key publicKey = kp.getPublic();
	// byte[] publicKeyBytes = publicKey.getEncoded();
	// String pub = new String(Base64.encodeBase64(publicKeyBytes), UTF8);
	// /** 得到私钥 */
	// Key privateKey = kp.getPrivate();
	// byte[] privateKeyBytes = privateKey.getEncoded();
	// String pri = new String(Base64.encodeBase64(privateKeyBytes), UTF8);
	//
	// Map<String, String> returnMap = new HashMap<String, String>();
	// returnMap.put(PUBLIC_KEY, pub);
	// returnMap.put(PRIVATE_KEY, pri);
	//
	// RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
	// BigInteger bint = rsp.getModulus();
	//
	// byte[] b = bint.toByteArray();
	// byte[] deBase64Value = Base64.encodeBase64(b);
	//
	// String retValue = new String(deBase64Value);
	// returnMap.put(MODULUS, retValue);
	//
	// return returnMap;
	// }

	/**
	 * 加密方法 source： 源数据
	 */
	public static String encrypt(String source, String publicKey) throws Exception {
		Key key = getPublicKey(publicKey);
		/** 得到Cipher对象来实现对源数据的RSA加密 */
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] b = source.getBytes();
		/** 执行加密操作 */
		byte[] b1 = cipher.doFinal(b);

		return new String(Base64.encodeBase64(b1), UTF8);
	}

	// /**
	// * 解密算法 cryptograph:密文
	// */
	// public static String decrypt(String cryptograph, String privateKey)
	// throws Exception {
	// Key key = getPrivateKey(privateKey);
	// /** 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
	// Cipher cipher = Cipher.getInstance(ALGORITHM);
	// cipher.init(Cipher.DECRYPT_MODE, key);
	// byte[] b1 = Base64.decodeBase64(cryptograph.getBytes());
	// /** 执行解密操作 */
	// byte[] b = cipher.doFinal(b1);
	//
	// return new String(b);
	// }

	/**
	 * 得到公钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(key.getBytes()));
		KeyFactory keyFactory = KeyFactory.getInstance(RSA);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);

		return publicKey;
	}

	// /**
	// * 得到私钥
	// *
	// * @param key
	// * 密钥字符串（经过base64编码）
	// * @throws Exception
	// */
	// public static PrivateKey getPrivateKey(String key) throws Exception {
	// PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
	// Base64.decodeBase64(key.getBytes()));
	// KeyFactory keyFactory = KeyFactory.getInstance(RSA);
	// PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
	//
	// return privateKey;
	// }

	public static String rsaBase64(String rsaPublicKey, String duichenKey) throws Exception, UnsupportedEncodingException {
		String jiamiKEy = RSAUtil.encrypt(duichenKey, rsaPublicKey);
		// jiamiKEy = new
		// String(Base64.encodeBase64(jiamiKEy.getBytes()),"UTF-8");
		return jiamiKEy;
	}

}
