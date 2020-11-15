package com.arrange;

public class Test {
    @org.junit.jupiter.api.Test
    public void fun(){
        String string = "123243414";
        char[] s = string.toCharArray();
        for(int i = 0;i<s.length;i++)
            System.out.println(s[i]-'0');
    }
}
