package com.chd;

import com.chd.util.MD5Utils;
import org.junit.Test;


import java.util.Scanner;

public class TestMD5 {
    /**
     * MD5算法特点：
     * 1.原文加密成秘闻，生成固定长度16字节，我们可以将每一个字节分解除高四位和低四位
     * 2.原文不变，加密密文相同，，不可逆，只能加密，不能解密，可以通过彩虹表进行碰撞破解
     * 3.已经被破解，直接使用不安全，要连续使用15次以上的MD5加密，不易被破解，或者采用加盐的方式进行加密
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(MD5Utils.md5("123"));
        //202cb962ac59075b964b07152d234b70
    }

    @Test
    public void test() {
        double firsthight;
        int n;
        Scanner in = new Scanner (System.in);//从键盘读取两个数据
        System.out.println("请输入从多少米落下：");
        firsthight = in.nextDouble();
        System.out.println("请输入第几次落下：");
        n = in.nextInt();
        System.out.println("从"+firsthight+"m"+"第"+n+"次落地经过的距离"+countTotalLong(n,firsthight)+"m");
        System.out.println("从"+firsthight+"m"+"第"+n+"次反弹高度"+countHeight(n,firsthight)+"m");
    }
    public static double countTotalLong(int n,double firsthight){//计算总的路径
        double totalLong =firsthight;
        for(int i = 0;i<n-1;i++){
            totalLong += firsthight/(double)(Math.pow(2,i));
        }
        return totalLong;
    }
    public static double countHeight(int n,double firsthight){ //计算每次反弹高度
        return firsthight/(double)(Math.pow(2,n));

    }

}
