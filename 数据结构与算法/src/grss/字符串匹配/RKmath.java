package grss.字符串匹配;

/**
 * 韩永发
 *
 * @Date 17:56 2022/5/14
 */
public class RKmath {
  public static boolean isMatch(String str,String patten){
    int v = stringToHash(patten);
    for (int i = 0; i <= str.length()-patten.length(); i++) {
      if (v==stringToHash(str.substring(i,i+patten.length())))
        return true;
    }
    return false;
  }
  public static int stringToHash(String src){
    int hash=0;
    for (int i = 0; i < src.length(); i++) {
      //26进制，小写字母a-z
      hash*=26;
      hash+=src.charAt(i)-97;
    }
    return hash;
  }
  
  public static void main(String[] args) {
    System.out.println(isMatch("aab", "ab"));
  }
}
