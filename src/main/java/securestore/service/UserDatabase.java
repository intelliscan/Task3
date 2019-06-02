package securestore.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class UserDatabase {
  private File usersFile = new File("loginData/users.json");
  private Map<String, String> data;

  public UserDatabase() throws IOException {
    createUsersFile();
    Gson gson = new Gson();
    Type mapType = new TypeToken<Map<String, String>>() {
    }.getType();
    data = gson.fromJson(new FileReader(usersFile), mapType);
    if (data == null)
      data = new HashMap<String, String>();
  }

  private void createUsersFile() {
    try {
      if (usersFile.createNewFile()) {
        FileWriter writer = new FileWriter(usersFile);
        writer.write("{}\n");
        writer.flush();
        writer.close();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public boolean contains(String name) {
    return data.containsKey(name.toLowerCase());
  }

  public String getPassword(String name) {
    return data.get(name);
  }

  public void storeUser(String name, String password) throws IOException {
    Gson gson = new Gson();
    FileWriter writer = new FileWriter(usersFile);

    data.put(name.toLowerCase(), securestore.service.SecureHasher.hash(password));
    gson.toJson(data, writer);

    writer.flush();
    writer.close();

    createLoginDataFile(name);
  }

  private void createLoginDataFile(String user) throws IOException {
    File dataFile = new File(loginDataFile(user));
    if (dataFile.createNewFile()) {
      FileWriter writer = new FileWriter(dataFile);
      writer.write("[]\n");
      writer.flush();
      writer.close();
    }
  }

  private String loginDataFile(String user) {
    return "loginData/" + user + ".json";
  }

  public void storeNewLoginData(String user, LoginData data) throws IOException {
    List<String> list = getEncodedLoginData(user);
    list.add(encodeLoginData(data));
    Gson gson = new Gson();
    FileWriter writer = new FileWriter(loginDataFile(user));
    gson.toJson(list, writer);
    writer.flush();
    writer.close();
  }

  private List<String> getEncodedLoginData(String user) throws FileNotFoundException {
    Gson gson = new Gson();
    File dataFile = new File(loginDataFile(user));
    Type listType = new TypeToken<List<String>>() {
    }.getType();
    List<String> list = gson.fromJson(new FileReader(dataFile), listType);
    return list == null ? new ArrayList<String>() : list;
  }

  private String encodeLoginData(LoginData data) {
    int webSiteLength = data.getWebsite().length();
    int nameLength = data.getName().length();
    int passwordLegth = data.getPassword().length();

    StringBuilder str = new StringBuilder();
    str.append(webSiteLength);
    str.append("-");
    str.append(nameLength);
    str.append("-"); 
    str.append(passwordLegth);
    str.append(":");
    String prefix = str.toString();
    String s = prefix + data.getWebsite() + data.getName() + data.getPassword();
    return encrypt(s);
  }

  public List<LoginData> getLoginData(String user) throws FileNotFoundException {
    List<String> list = getEncodedLoginData(user);
    List<LoginData> data = new ArrayList<LoginData>();
    for (String s : list) {
      data.add(decodeLoginData(s));
    }
    return data;
  }

  private LoginData decodeLoginData(String input) {
    String s = decrypt(input);
    List<String> parts = new LinkedList<String>(Arrays.asList(s.split(":")));
    String prefix = parts.get(0);
    parts.remove(0);
    String rest = String.join("", parts);

    String[] prefixParts = prefix.split("-");
    Integer webSiteLength = Integer.parseInt(prefixParts[0]);
    Integer nameLength = Integer.parseInt(prefixParts[1]);
    Integer passwordLegth = Integer.parseInt(prefixParts[2]);

    Integer passwordStart = webSiteLength + nameLength;

    return new LoginData(rest.substring(0, webSiteLength), rest.substring(webSiteLength, passwordStart),
        rest.substring(passwordStart, passwordStart + passwordLegth));
  }

  private Cipher getCipher() {
    try {
      return Cipher.getInstance("DES/CBC/PKCS5Padding");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String encrypt(String s) {
    try {
      Cipher cipher = getCipher();
      IvParameterSpec ivspec=getIvspec();
      Key key = getKey();
      cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);
      byte[] encrypted = cipher.doFinal(s.getBytes());
      return Base64.getEncoder().encodeToString(encrypted);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String decrypt(String s) {
    try {
      Cipher cipher = getCipher();
      Key key = getKey();
      IvParameterSpec ivspec=getIvspec();
      cipher.init(Cipher.DECRYPT_MODE, key, ivspec);
      byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(s));
      return new String(decrypted);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private IvParameterSpec getIvspec()
  {
    byte[] iv = new byte[8];
    return new IvParameterSpec(iv);
  }
  private Key getKey() {
    return new SecretKeySpec("Parsimon".getBytes(), "DES");
  }

}