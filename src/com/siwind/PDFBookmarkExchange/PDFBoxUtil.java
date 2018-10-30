/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siwind.PDFBookmarkExchange;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSName;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDNamedDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;

/**
 *
 * @author admin
 */
public class PDFBoxUtil {

    private static void getBookmarkLevel(PDOutlineNode bknode, int level, ArrayList<BookmarkItem> items) throws IOException {
        PDOutlineItem current = bknode.getFirstChild();
        while (current != null) {
            BookmarkItem item = new BookmarkItem();
            item.setName(current.getTitle());
            item.setLevel(level);
            if (current.getDestination() instanceof PDPageDestination) {
                PDPageDestination pd = (PDPageDestination) current.getDestination();
                item.setPageNum(pd.retrievePageNumber()); //real page index start from 0
            }
            if (current.getAction() instanceof PDActionGoTo) {
                PDActionGoTo gta = (PDActionGoTo) current.getAction();
                PDDestination dst = gta.getDestination();
                if (dst instanceof PDPageDestination) {
                    PDPageDestination pd = (PDPageDestination) dst;
                    item.setPageNum(pd.retrievePageNumber()); //real page index start from 0
                } else if( dst instanceof PDNamedDestination ){ //can not get name destination's page number,javascript will be ok!
                    PDNamedDestination pd = (PDNamedDestination) dst;
                    //System.out.println("Named Destionation: " + pd.getNamedDestination());
                    //COSName csname = COSName.getPDFName(pd.getNamedDestination());
                    //System.out.println("Named Destionation: " + csname.toString());
                }else{//nothing to do!
                    
                }
            }
            items.add(item);
            getBookmarkLevel(current, level + 1, items); //sub level
            current = current.getNextSibling();
        }
    }

    public static boolean getBookmarkFromFile(String strFile, ArrayList<BookmarkItem> items) {
        boolean bOK = false;
        if (strFile.isEmpty()) {
            return bOK;
        }

        PDDocument document = null;
        try {
            document = PDDocument.load(new File(strFile));
            PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
            if (outline != null) {
                getBookmarkLevel(outline, 0, items); //sub level
                bOK = true;
            }
        } catch (IOException ex) {
            Logger.getLogger(PDFBoxUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException ex) {
                    Logger.getLogger(PDFBoxUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bOK;
    }

    private static PDOutlineItem makeBookmarkItem(BookmarkItem item) {
        PDOutlineItem bookmark = new PDOutlineItem();
        bookmark.setTitle(item.getName());

        if (item.isPageNumOK()) {//page set!
            PDPageFitDestination dst = new PDPageFitDestination(); //PDPageFitWidthDestination(); //PDPageFitDestination();
            dst.setFitBoundingBox(true);
            dst.setPageNumber(item.getPageNum());
            bookmark.setDestination(dst);
        }
        return bookmark;
    }

    private static int saveBookmarkLevel(PDOutlineNode bknode, int level, ArrayList<BookmarkItem> items, int index) throws IOException {
        while (index < items.size()) {
            BookmarkItem item = items.get(index);
            if (level > item.getLevel()) {
                break; //end now!
            } else if (level == item.getLevel()) {
                bknode.addLast(makeBookmarkItem(item)); //add bookmark
                index ++; //next 
            } else if (null == bknode.getLastChild()) {// Error here!
                bknode.addLast(makeBookmarkItem(item)); //add bookmark
                index++;
                break;
            } else {
                index = saveBookmarkLevel(bknode.getLastChild(), level + 1, items, index); //sub item
            }
        } //end of while
        return index;
    }

    public static boolean saveBookmarkToFile(String strFile, ArrayList<BookmarkItem> items, int basePage) throws Exception {
        boolean bOK = false;
        if (strFile.isEmpty()) {
            throw new Exception("Error: File Path is empty." + strFile);
        }
        PDDocument document = null;
        try {
            document = PDDocument.load(new File(strFile));
            if (document.isEncrypted()) {
                throw new Exception("Error: can not add bookmark to Encrypted PDF file."); //no support!
            }
            PDDocumentOutline outline = new PDDocumentOutline();
            document.getDocumentCatalog().setDocumentOutline(outline);

            saveBookmarkLevel(outline, 0, items, 0); //sub level

            document.save(strFile);

            bOK = true;

        } catch (Exception ex) {
            throw ex;
            //Logger.getLogger(PDFBoxUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException ex) {
                    //Logger.getLogger(PDFBoxUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bOK;
    }

    /**
     * This will print the documents data.
     *
     * @param args The command line arguments.
     *
     * @throws IOException If there is an error parsing the document.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            usage();
        } else {
            testPrintBookmark(args[0]); //
        }
    }

    /**
     * This will print the usage for this document.
     */
    private static void usage() {
        System.err.println("Usage: java " + PDFBoxUtil.class.getName() + " <input-pdf>");
    }

    /**
     * This will print the documents bookmarks to System.out.
     *
     * @param document The document.
     * @param bookmark The bookmark to print out.
     * @param indentation A pretty printing parameter
     *
     * @throws IOException If there is an error getting the page count.
     */
    public static void printBookmark(PDDocument document, PDOutlineNode bookmark, String indentation) throws IOException {
        PDOutlineItem current = bookmark.getFirstChild();
        while (current != null) {
            if (current.getDestination() instanceof PDPageDestination) {
                PDPageDestination pd = (PDPageDestination) current.getDestination();
                System.out.println(indentation + "Destination page: " + (pd.retrievePageNumber() + 1));
            }
            if (current.getAction() instanceof PDActionGoTo) {
                PDActionGoTo gta = (PDActionGoTo) current.getAction();
                if (gta.getDestination() instanceof PDPageDestination) {
                    PDPageDestination pd = (PDPageDestination) gta.getDestination();
                    System.out.println(indentation + "GoToAction Destination page: " + (pd.retrievePageNumber() + 1));
                }
            }
            System.out.println(indentation + current.getTitle());
            printBookmark(document, current, indentation + "    ");
            current = current.getNextSibling();
        }
    }

    public static void testPrintBookmark(String strFile) {
        if (strFile.isEmpty()) {
            return;
        }

        PDDocument document = null;
        try {
            document = PDDocument.load(new File(strFile));
            PDDocumentOutline outline = document.getDocumentCatalog().getDocumentOutline();
            if (outline != null) {
                printBookmark(document, outline, "");
            } else {
                System.out.println("This document does not contain any bookmarks");
            }
        } catch (IOException ex) {
            Logger.getLogger(PDFBoxUtil.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException ex) {
                    //Logger.getLogger(PDFBoxUtil.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
