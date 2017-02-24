package security;

import android.annotation.SuppressLint;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created with IntelliJ IDEA. User: admin Date: 13-6-19 Time: 下午4:38 To change
 * this template use File | Settings | File Templates.
 */
public class ThreeDES {
	/**
	 * 对称密钥
	 */
	public static final String PUBLIC_KEY = "自定义";

	private static final String Algorithm = "DESede"; // 定义 加密算法,可用
														// DES,DESede,Blowfish

	// keybyte为加密密钥，长度为24字节
	// src为被加密的数据缓冲区（源）
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	// keybyte为加密密钥，长度为24字节
	// src为加密后的缓冲区
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	public static String orginalEncoded(String originalKeyString, String data) throws Exception {
		// 3des加密后的密文
		byte[] encoded = ThreeDES.encryptMode(originalKeyString.getBytes(), data.getBytes());
		// base64 编码 发送
		return new String(Base64.encodeBase64(encoded), "UTF-8");
	}

	/**
	 * 对称密钥加密
	 *
	 * @param data
	 * @param originalKey
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String desBase64(String data, String originalKey) throws UnsupportedEncodingException {
		byte[] encoded;
		String reqbase64str;
		/**
		 * 加密header
		 */
		encoded = ThreeDES.encryptMode(originalKey.getBytes(), data.getBytes());
		// base64 编码 发送
		reqbase64str = new String(Base64.encodeBase64(encoded), "UTF-8");
		return reqbase64str;
	}

	// 转换成十六进制字符串
	@SuppressLint("DefaultLocale")
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	/**
	 * 生成随机密钥
	 * 
	 * @param length
	 * @return
	 */
	public static String genrateRandomPassword(int length) {
		char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		int i;
		int pwd_len = length;
		int count = 0;
		int maxNum = str.length;
		StringBuffer pwd = new StringBuffer("");
		SecureRandom r = new SecureRandom();
		while (count < pwd_len) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < str.length) {
				pwd.append(str[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	// public static void main(String[] args) throws Exception
	// {
	// //添加新安全算法,如果用JCE就要把它添加进去
	// Security.addProvider(new com.sun.crypto.provider.SunJCE());
	//
	// final byte[] keyBytes = {0x11, 0x22, 0x4F, 0x58, (byte)0x88, 0x10, 0x40,
	// 0x38
	// , 0x28, 0x25, 0x79, 0x51, (byte)0xCB, (byte)0xDD, 0x55, 0x66
	// , 0x77, 0x29, 0x74, (byte)0x98, 0x30, 0x40, 0x36, (byte)0xE2}; //24字节的密钥
	//
	// String key = "womaikeywomaikwomaikeywo";
	//
	// System.out.println("======"+key.getBytes().length);
	//
	// String szSrc = "This is a 3DES test. 测试";
	//
	// System.out.println("加密前的字符串:" + szSrc);
	//
	// byte[] encoded = encryptMode(key.getBytes(), szSrc.getBytes());
	// System.out.println("加密后的字符串:" + new String(encoded));
	// System.out.println("Algorithm:DESede,"+"Miyao:"+key+",Miwen:"+new
	// String(Base64.encodeBase64(encoded),"UTF-8"));
	//
	// //解密
	// byte[] Miwen =
	// Base64.decodeBase64("sYZ1AeNRuyb5UotsGKOZfojhlvw93kaiKZk/WtoqpN4=".getBytes());
	//
	// byte[] srcBytes = decryptMode(key.getBytes(), Miwen);
	// System.out.println("解密后的字符串:" + (new String(srcBytes)));
	// }
}
