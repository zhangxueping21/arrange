package com.arrange.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.Random;

/**
 * 密码加密方法
 * 默认密码不少于7位
 * @author kj
 */

@Component
public class PasswordUtil {
    @Value("${passwordutil.encryptablesalt}")
    private String encryptableSalt;
    @Value("${passwordutil.key}")
    private String KEY ;

    private PasswordUtil(){}
    /**
     * 加密
     * @param password 用户输入的密码
     * @return 初始密文，有待进一步加工
     */
    public String getPassword(String password){
        if(password == null || password == "")
            return "";
        char[] eng = {'p','d','c','f','j','k','e','h','b','u','a','r','s'
                ,'z','l','m','i','g','v','w','q','x','y','n','o','t'};
        char[] eng2 = {'u','f','d','c','r','a','s','q','k','t','z','h',
                'i','n','b','p','j','e','v','w','x','o','g','l','m','y'};
        password = changeEng(password+encryptableSalt,eng,password.length());
        int index1=(int) (Math.random()*(password.length()-3))+3;

        //生成17位随机字符串
        String s1 = getRandomString(17);
        //生成8位的盐
        String s2 = getRandomString(8);
        password = index1+getRandomString(1)+password.substring(0,index1)+s1+password.substring(index1)+changeEng(s2,eng2,13);
        String s3 = md5Encrypt(s2);
        int index2 = (int) (Math.random()*(password.length()-9));
        password = password.substring(0,index2)+s3+password.substring(index2);
        return password;
    }

    /**
     * 将密文还原成明文密码
     * @param ciphertext
     * @return
     */
    public String reEncrypt(String ciphertext){
        if(ciphertext == null || ciphertext == "")
            return "";
        if(ciphertext.length()<8)
            return "";
        try {
            char[] eng = {'p','d','c','f','j','k','e','h','b','u','a','r','s'
                    ,'z','l','m','i','g','v','w','q','x','y','n','o','t'};
            char[] eng2 = {'u','f','d','c','r','a','s','q','k','t','z','h',
                    'i','n','b','p','j','e','v','w','x','o','g','l','m','y'};
            String s3 = md5Encrypt(reChangeEng(ciphertext.substring(ciphertext.length() - 8), eng2, 13));
            String res = ciphertext.replaceFirst(s3, "");
            res = res.substring(0,res.length()-8);
            int index1 = 0,i;
            for(i = 0;i<res.length();i++){
                char ch = res.charAt(i);
                if(ch<'9'+1 && ch>'0'-1){
                    index1 = index1*10 + ch-'0';
                    continue;
                }
                break;
            }
            res = res.substring(i+1,index1+i+1)+res.substring(index1+i+1+17);

            res = reChangeEng(res,eng,res.length()-encryptableSalt.length());
            return res.substring(0,res.length()-encryptableSalt.length());
        }catch (Exception e){
            return "";
        }
    }



    /**
     * 检查密码是否正确
     * @param password 用户输入的密码
     * @param ciphertext 从数据库取出的密文
     * @return 如果密码正确则返回true，反之返回false
     */
    public boolean checkPassword(String password,String ciphertext){
        return password.equals(reEncrypt(ciphertext));
    }

    /**
     * 将密码中的英文部分对应字符表 向后移动 num 个单位
     * @param password 密码
     * @param eng 混乱字母表
     * @param num 移动位数
     * @return
     */
    private String changeEng(String password,char[] eng,int num){
        char[] chars = password.toCharArray();
        int a=0;
        for(int i=0;i<chars.length;i++){
            if(chars[i]<'z'+1&&chars[i]>'a'-1){
                a = (chars[i]-'a'+num)%26;
                chars[i] = (char) (eng[a]-'a'+'A');
                continue;
            }
            if(chars[i]<'Z'+1&&chars[i]>'A'-1){
                a = (chars[i]-'A'+num)%26;
                chars[i] = eng[a];
                continue;
            }
        }
        return String.valueOf(chars);
    }

    private String reChangeEng(String password,char[] eng,int num){
        char[] chars = password.toCharArray();
        int a=0;
        for(int i=0;i<chars.length;i++){
            if(chars[i]<'z'+1&&chars[i]>'a'-1){
                for(int j=0;j<eng.length;j++){
                    if(chars[i] == eng[j]){
                        a=j-num<0?j-num+26:j-num;
                        break;
                    }
                }
                chars[i] = (char) (a+'A');
                continue;
            }
            if(chars[i]<'Z'+1&&chars[i]>'A'-1){
                for(int j=0;j<eng.length;j++){
                    if(chars[i]+'a'-'A' == eng[j]){
                        a=j-num<0?j-num+26:j-num;
                        break;
                    }
                }
                chars[i] = (char) ('a'+a);
                continue;
            }
        }
        return String.valueOf(chars);
    }

    /**
     * 生成随机的字符串
     * @param length 指定字符串的长度
     * @return
     */
    private String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     *	加密方法
     *	对字符串进行加密，把一个字符串进行加密
     *	如果加密出异常了，返回null
     * @param ss 需要加密的明文
     * @return
     */
    private String md5Encrypt(String ss) {
        ss = ss==null?"":ss+KEY;
        char[] md5Digist = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f',};//加密字典
        try{
            MessageDigest md = MessageDigest.getInstance("MD5"); //用md5的加密算法进行加密
            byte[] ssarr = ss.getBytes();
            md.update(ssarr); //把明文放进加密类MessageDigest的对象实例去
            byte[] mssarr = md.digest(); //开始真正的加密

            int len = mssarr.length;
            char[] str = new char[len*2];
            int k = 0;	//计数
            for(int i=0;i<len;i++) {
                byte b = mssarr[i];
                str[k++] = md5Digist[b>>>4 & 0xf];
                str[k++] = md5Digist[b & 0xf];
            }
            return new String(str);
        }catch (Exception e) {
            e.printStackTrace();
        }
        //如果报异常了，返回null
        return null;
    }
}