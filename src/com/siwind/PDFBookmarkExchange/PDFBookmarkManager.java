/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siwind.PDFBookmarkExchange;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author admin
 */
public class PDFBookmarkManager {
    static final String COMMENT_STR = "#";
    static final String CR_NL = System.getProperty("line.separator");
    static final String LEVEL_CHAR = "\t";
    static final String PAGENUM_SPACE = "    ";
    static final String FULL_SPACE = "　"; //全角空格
    static final int OUT_LINE_WIDTH = 76; //
    static final int MAX_NAME_LEN = 120;

    //remove no normal char from name.
    //replace more space to less space
    static final String regFilteName_FromPDF = "[\r\n\t]|   +|　　+|( |　){2,}|(　| ){2,}|#+";
    //space between Bookmark Name and PageNumber
    //at least 4 space or 3 space with at least 1 full space
    static final String regFilterName_ToPDF = "(    |   　|  　| 　 |　  |　　|\t)(　| |\t)*";

    static final String BASEPAGE_STRING = "basePage = ";

    static final String HEAD_STRING = "#==================================================" + CR_NL
            + "# Mail:  yinqingwang@gmail.com"  + CR_NL
            + "# Author: siwind" + CR_NL
            + "# Url: http://blog.csdn.net/yinqingwang" + CR_NL
            + "# 0) 每行格式: [多个空格键或者Tab键(可选)] [书签名称] [多个Tab键或者空格键] [页码] " + CR_NL
            + "# 1) 以#开头的部分为注释, 空行自动忽略 " + CR_NL
            + "# 2) 书签Text文件必须以UTF-8格式编码 " + CR_NL
            + "# 3) 书签的缩进以Tab键或者连续4个空格键或者中文全角空格标记, 每个Tab键或者每4个空格或者1个中文全角空格缩进一级, 依次类推 " + CR_NL
            + "# 4) 书签的名称部分不能含有Tab键(Tab键为分隔符),#字符或者连续3个空格或者连续2个全角空格及以上 " + CR_NL
            + "# 5) 书签的名称部分和页码部分的分隔符，以至少一个Tab键或者连续4个空格或者连续2个全角空格及以上做为分隔标记 " + CR_NL + CR_NL
            + "# 注: 可以使用文本编辑器的列选模式，先拷贝1个Tab键或者连续4空格，然后列选模式同时选择多列粘贴即可 " + CR_NL
            + CR_NL;

    private static String formatOutBookmark(ArrayList<BookmarkItem> items, int basePage) {
        String str = "";
        for (BookmarkItem item : items) {
            int level = item.getLevel();
            while (level > 0) {
                str += LEVEL_CHAR;
                level--;
            }
            String sName = StringUtil.StringFilter(item.getName(), regFilteName_FromPDF, " "); //
            int slen = StringUtil.StrLenCJK(sName);
            str += sName;
            for (int i = slen; i < OUT_LINE_WIDTH; i++) {
                str += " ";
            }
            str += PAGENUM_SPACE + item.getPageNumString(basePage) + CR_NL; //
        }
        return str;
    }

    /**
     *
     * @param strFile
     * @param basePage: 1,2,3,..., basePage = RealPageNum - BookmarkPageNum
     * @return
     */
    public static String getBookmarkStringFromPDF(String strFile, int basePage) {
        String strBookmark = "";
        ArrayList<BookmarkItem> items = new ArrayList<BookmarkItem>();
        if (PDFBoxUtil.getBookmarkFromFile(strFile, items)) {
            strBookmark = formatOutBookmark(items, basePage);

            strBookmark = HEAD_STRING + "# Export File: " + strFile + CR_NL
                    + "# Export Time: " + Util.getCurTimeString() + CR_NL
                    + "# " + BASEPAGE_STRING + (basePage + 1) + CR_NL + CR_NL
                    + strBookmark;
        }
        return strBookmark;
    }

    private static int getLevelFromStr(String str) {
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                len++;
            } else if (str.charAt(i) == LEVEL_CHAR.charAt(0)) {
                len += 4;
            } else if (str.charAt(i) == FULL_SPACE.charAt(0)) {
                len += 4;
            } else {
                break;
            }
        }
        return (len + 3) / 4; //
    }

    private static ArrayList<BookmarkItem> readBookmarkFromString(String str, int basePage) throws Exception {
        String[] lines = StringUtil.StringSplit(str, "[\r\n]+");
        ArrayList<BookmarkItem> items = new ArrayList<BookmarkItem>();
        for (String line : lines) { //process every line!
            line = StringUtil.removeComment(line, COMMENT_STR); //
            if (StringUtil.StringFilter(line, FULL_SPACE, " ").isEmpty()) {
                continue; //with full space line!
            }
            BookmarkItem item = new BookmarkItem();
            item.setLevel(getLevelFromStr(line));

            line = StringUtil.StringFilter(line, "^( |　)+|( |　)+$", ""); //trim head and tail of white space
            String[] ss = StringUtil.StringSplit(line, regFilterName_ToPDF); //split to find name and pagenumber
            if (ss.length >= 1) {
                String sName = StringUtil.StringFilter(ss[0], regFilteName_FromPDF, " ");
                item.setName(sName); //name
            }
            if (ss.length >= 2) {
                String s = StringUtil.filterNumber(ss[1],"/\\").trim(); // 去掉头尾的/和\字符(能否去掉非数字字符, 留下数字页码呢)
                if (StringUtil.isInteger(s) ) { //
                    item.setPageNum(Integer.valueOf(s) + basePage - 1); //real page number start from 0.
                } else{
                    throw new Exception("Incorrect Bookmark: \t" + line);
                }
            }
            items.add(item);//
        }//end for
        return items;
    }

    /**
     *
     * @param strFile
     * @param str
     * @param basePage
     */
    public static void saveBookmarkStringToPDF(String strFile, String str, int basePage) throws Exception {
        ArrayList<BookmarkItem> items = readBookmarkFromString(str, basePage);
        String strError = checkBookmarkError(items, basePage);
        //System.out.println(strError);
        if (!strError.isEmpty()) {
            JOptionPane.showMessageDialog(null, strError, "Warning", JOptionPane.WARNING_MESSAGE);
        }
        PDFBoxUtil.saveBookmarkToFile(strFile, items, basePage);//
    }

    public static int getBasePage(String bkmPage, String realPage) {
        return Integer.parseInt(realPage) - Integer.parseInt(bkmPage);
    }

    private static String checkBookmarkError(ArrayList<BookmarkItem> items, int basePage) {
        int max_line = 20;
        String strError = "";
        int cur = 0, line = 0;
        for (BookmarkItem item : items) {
            boolean bFalse = true;
            if (cur < item.getLevel() - 1) {
                strError += "Sub-Level indent incorrect: \t" + item.getName() + "\t" + item.getPageNumString(basePage) + CR_NL;
            } else if (item.getName().isEmpty()) {
                strError += "Bookmark Name is empty: \t" + item.getName() + "\t" + item.getPageNumString(basePage) + CR_NL;
            } else if (item.getName().length() > MAX_NAME_LEN) {
                strError += "Bookmark Name too long: \t" + item.getName() + "\t" + item.getPageNumString(basePage) + CR_NL;
            } else if (!item.isPageNumOK()) {
                strError += "Bookmark Page not set: \t" + item.getName() + "\t" + item.getPageNumString(basePage) + CR_NL;
            } else {
                bFalse = false;
            }
            if (bFalse) {
                line++;
                if( line >= max_line ){
                    strError += "......";
                    break;
                }
            }
            cur = item.getLevel();//update level
        }
        return strError;
    }

    /**
     *
     * @param str
     * @return -1 if not found!
     */
    public static int findBasePageFromString(String str) {
        int basePage = -1;
        if (!str.isEmpty()) {
            int i = str.indexOf(BASEPAGE_STRING);
            if (-1 != i) {
                i += BASEPAGE_STRING.length();
                int j = str.indexOf(CR_NL, i);
                if (-1 == j) {
                    j = str.length();
                }
                basePage = Integer.parseInt(str.substring(i, j));
            }
        }
        return basePage;
    }
}

class BookmarkItem {

    private static final int INVALID_PAGENUM = Integer.MIN_VALUE;
    private static final String INVALID_PAGESTRING = "";
    private String name = "";
    private int level = 0;  //top level is 0, and sub-level is 1,2,...
    private int pageNum = INVALID_PAGENUM; //page number

    public BookmarkItem() {

    }

    public BookmarkItem(int level, String name, int pageNum) {
        this.level = level;
        this.name = name;
        this.pageNum = pageNum;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * @return the pageNum
     */
    public int getPageNum() {
        return pageNum;
    }

    public boolean isPageNumOK() {
        return this.pageNum != INVALID_PAGENUM;
    }

    /**
     *
     * @param basePage basePage = RealPageNum - BookmarkPageNum
     * @return
     */
    public String getPageNumString(int basePage) {
        if (isPageNumOK()) {
            return String.valueOf(this.pageNum + 1 - basePage);
        }
        return INVALID_PAGESTRING;
    }

    /**
     * @param pageNum the pageNum to set
     */
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
