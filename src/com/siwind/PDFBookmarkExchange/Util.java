/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siwind.PDFBookmarkExchange;

import java.text.SimpleDateFormat;

/**
 *
 * @author admin
 */
public class Util {

    public static String getCurTimeString() {
        SimpleDateFormat time_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return time_formatter.format(System.currentTimeMillis());
    }
}
