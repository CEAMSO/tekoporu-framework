package ${package}.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

	public String crearEnlaceAcceso(String toHash) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.reset();
		md.update(toHash.getBytes());
		byte[] b = md.digest();
		String hash = toHexadecimal(b);

		return hash;
	}

	private String toHexadecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}

	public static String md5(String pwd) {

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(pwd.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {

				hashtext = "0" + hashtext;

			}

			return hashtext;

		} catch (NoSuchAlgorithmException e) {

			throw new RuntimeException(e);

		}
	}
}
