/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siwind.PDFBookmarkExchange;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author admin
 */
public class StringUtil {

    // 根据Unicode编码完美的判断中文汉字和符号
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    public static boolean isKorean(final char c) {
        if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_JAMO
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HANGUL_SYLLABLES) {
            return true;
        }
        return false;
    }

    public static boolean isJapan(final char c) {
        if (Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HIRAGANA
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.KATAKANA
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || Character.UnicodeBlock.of(c) == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION) {
            return true;
        }
        return false;
    }

    //准确判断是否CJK字符(中文/韩文/日文)
    public static boolean isCharCJK(final char c) {
        return isChinese(c) || isJapan(c) || isKorean(c); //
    }

    /**
     *
     * @param str
     * @return
     */
    public static int StrLenCJK(String str) {
        int len = 0;
        char[] ch = str.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            if (isCharCJK(ch[i])) {
                len++;
            }
            len++;
        }
        return len;
    }

    public static String StringFilter(String str, String regExp, String replace) {
    	String res = "";
        if (str.isEmpty()) {
            return res;
        }
        Pattern pattern = Pattern.compile(regExp);//
        String[] split = pattern.split(str);
        for( String s : split) {
        	if( s.trim().isEmpty() ) continue;
        	res += s.trim() + replace;
        }
        //Matcher matcher = pattern.matcher(str);
        //return matcher.replaceAll(replace).trim();
        int i = res.lastIndexOf(replace);
        if( i!= -1 ) res = res.substring(0, i);
        return res;
    }
    /**
     * Split string and trim total white space line.
     * @param str
     * @param regExp
     * @return 
     */
    public static String[] StringSplit(String str, String regExp) {
    	String[] res = new String[0];
    	if( str.isEmpty() ) return res;
    
    	Pattern pattern = Pattern.compile(regExp);//
        String[] split = pattern.split(str);
        
        ArrayList<String> arr = new ArrayList<String>();
        for( String s : split) {
        	if( s.trim().isEmpty() ) continue;
        	arr.add(s);
        }
        return arr.toArray(res);
    }
    
    public static String removeComment(String str, String strComment){
        if( str.isEmpty() ) return str;
        int i = str.indexOf(strComment);
        if( -1 == i ) return str;
        return str.substring(0, i); //
    }
    
    public static boolean isInteger(String input){  
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);  
        return mer.find();  
    }  
}
