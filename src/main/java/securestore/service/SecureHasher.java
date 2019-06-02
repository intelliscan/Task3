package securestore.service;

import java.security.MessageDigest;

public class SecureHasher {

  private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

  private static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
  }

  public static String hash(String s) {
    try {
      MessageDigest md;
      String salt = "saltandpepper";
      String combined = s + salt;
      byte[] hashed = combined.getBytes();
      md = MessageDigest.getInstance("SHA-512");
      hashed = md.digest();
      return bytesToHex(hashed);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
