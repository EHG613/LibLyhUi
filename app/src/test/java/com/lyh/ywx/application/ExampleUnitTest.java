package com.lyh.ywx.application;

import org.junit.Test;

import static org.junit.Assert.*;

import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testTim(){
//        ILIMManager manager= TIMManager
        try {
            String url = "https://doctor.liangyihui.net/#/doc/93597%C3%9C(%EE%8B%ACz%E8%9D%808%E3%88%80";
            System.out.println(url);

//            String encodeUrl = URLEncoder.encode(url, "utf-8");
            String encodeUrl = url;
            System.out.println(encodeUrl);

            //服务器端自动进行的解码操作
            encodeUrl = URLDecoder.decode(encodeUrl, "ISO8859-1");
            System.out.println(encodeUrl);

            //转码操作
            String decodeUrl = new String(encodeUrl.getBytes("ISO8859-1"), "utf-8");
            System.out.println(decodeUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}