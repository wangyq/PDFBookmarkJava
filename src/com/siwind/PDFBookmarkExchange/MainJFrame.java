/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.siwind.PDFBookmarkExchange;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author admin
 */
public class MainJFrame extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8708725591147571438L;
	
	enum RFile { //Recently  used file
        PDFOPEN(0), PDFSAVE(1), TXTOPEN(2), TXTSAVE(3);
        
        //
        private static final int NUM = 2;
        private static String[] files = new String[4];
        private int value;

        RFile(int value) {
            this.value = value;
        }

        /**
         * 
         * @param v
         * @return 
         */
        private static String getFile( int v){
            String strFile = ".";
            if( null == files[v] || files[v].isEmpty() ){
                int a = v/NUM, b=((v%2)==0)?1:0;
                int[] c = new int[3];
                c[0] = 2*a + b;
                a = (a==0)?1:0;
                c[1] = 2*a ; c[2] = 2*a+1;
                for(int i=0;i<c.length;i++){
                    int u = c[i];
                    if( null != files[u] && !files[u].isEmpty() ){
                        strFile = files[u];
                        break;
                    }
                }
            }else{// not empty
                strFile = files[v];
            }
            return strFile;
        }
        String getFile() {
            return getFile(this.value);
        }

        void setFile(String strFile) {
            files[this.value] = strFile;
        }
    };

    private static final String TITLE_PROGRAM = "PDFBookmark v1.0.2-final (Import/Export Bookmark)";

    /**
     * Creates new form MainJFrame
     */
    public MainJFrame() {
        initComponents();
        init();
    }

    /**
     * custom init here!
     */
    public void init() {
        setNumericOnly(this.jTextPDFPage);
        setNumericOnly(this.jTexBkmPage);
        this.setTitle(TITLE_PROGRAM);
    }

    public void checkBasePage(String str) {
        if (str.isEmpty()) {
            return;
        }
        int basePage = PDFBookmarkManager.findBasePageFromString(str);
        if (-1 != basePage) {
            this.jTexBkmPage.setText("1");
            this.jTextPDFPage.setText(String.valueOf(basePage));
        }
    }

    public static void setNumericOnly(javax.swing.JTextField jText) {
        jText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if ((!Character.isDigit(c)
                        || (c == KeyEvent.VK_BACK_SPACE)
                        || (c == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });
    }

    private static String FileChooser(Component parent, String strDesc, String strSuffix, boolean bOpen, String strTitle) {
        return FileChooser(parent,strDesc,strSuffix,bOpen,strTitle,".");
    }
    private static String FileChooser(Component parent, String strDesc, String strSuffix, boolean bOpen, String strTitle,String curPath) {
        String strFile = "";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(curPath));
        fileChooser.setAcceptAllFileFilterUsed(false);

        //FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT file (UTF-8 encode)(*.txt)", "txt");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(strDesc, strSuffix);

        fileChooser.setFileFilter(filter); //选择文件过滤器
        int returnVal = 0;

        if (strTitle.isEmpty()) {
            if (bOpen) {
                returnVal = fileChooser.showOpenDialog(parent);
            } else {
                returnVal = fileChooser.showSaveDialog(parent);
            }
        } else {
            fileChooser.setDialogTitle(strTitle);
            if (bOpen) {
                returnVal = fileChooser.showDialog(parent, "Open");
            } else {
                returnVal = fileChooser.showDialog(parent, "Save");
            }
        }
        if (returnVal == JFileChooser.APPROVE_OPTION) { //判断是否为打开的按钮

            strFile = fileChooser.getSelectedFile().getPath();// + "--" + fileChooser.getSelectedFile().getName();
            //System.out.println("You chose to open this file: " + strFile);

            int index = strFile.lastIndexOf("." + strSuffix);
            if (-1 == index) {
                strFile += "." + strSuffix;
            }
        }
        return strFile;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnOpen = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextBookmark = new javax.swing.JTextArea();
        btnBKMSave = new javax.swing.JButton();
        btnBKMLoad = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTexBkmPage = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextPDFPage = new javax.swing.JTextField();
        jLabelWeb = new javax.swing.JLabel();
        jLabelVersion = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        btnOpen.setText("Open ...");
        btnOpen.setToolTipText("Open from TXT file");
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        btnSave.setText("Store ...");
        btnSave.setToolTipText("Store to TXT file");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jTextBookmark.setColumns(20);
        jTextBookmark.setRows(5);
        jTextBookmark.setTabSize(4);
        jScrollPane1.setViewportView(jTextBookmark);

        btnBKMSave.setText("Save...");
        btnBKMSave.setToolTipText("Save Bookmark to PDF file");
        btnBKMSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBKMSaveActionPerformed(evt);
            }
        });

        btnBKMLoad.setText("Load...");
        btnBKMLoad.setToolTipText("Load Bookmak from PDF file");
        btnBKMLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBKMLoadActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.setToolTipText("Clear Content.");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        jLabel1.setText("Bookmark PageNum:");

        jTexBkmPage.setText("1");
        jTexBkmPage.setToolTipText("Bookmark Page Number.");

        jLabel2.setText("Real PDF PageNum: ");

        jTextPDFPage.setText("1");
        jTextPDFPage.setToolTipText("Real Page Number in PDF file.");

        jLabelWeb.setText("http://blog.csdn.net/yinqingwang");

        jLabelVersion.setText("PDFBookmark-Exchanger(ver 1.0.2)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBKMLoad)
                                .addGap(37, 37, 37)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBKMSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTexBkmPage))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnOpen)
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextPDFPage)
                                .addGap(19, 19, 19)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelWeb, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(jLabelVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTexBkmPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTextPDFPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear))
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOpen)
                    .addComponent(btnSave)
                    .addComponent(btnBKMSave)
                    .addComponent(btnBKMLoad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelWeb)
                    .addComponent(jLabelVersion)))
        );

        jLabelWeb.getAccessibleContext().setAccessibleName("http://haha");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        try {
            RFile rFile = RFile.TXTOPEN;
            
            // TODO add your handling code here:
            String strFile = FileChooser(this.getContentPane(), "TXT file (UTF-8 encode)(*.txt)", "txt", true, "Open Text File",rFile.getFile());
            if (strFile.isEmpty()) {
                return;
            }

            Scanner inFile = new Scanner(new File(strFile), "UTF-8"); //要指定文件的编码格式，否则可能读不了数据
            this.jTextBookmark.setText("");
            while (inFile.hasNext()) {
                String line = inFile.nextLine();
                this.jTextBookmark.append(line + System.getProperty("line.separator"));
                //System.out.println(line);
            }
            this.checkBasePage(this.jTextBookmark.getText()); //reset basePage
            
            rFile.setFile(strFile); //
            
        } catch (Exception ex) {

        } finally {
        	
        }
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        PrintWriter out = null;
        try {
            // TODO add your handling code here:
            RFile rFile = RFile.TXTSAVE;
            
            String strFile = FileChooser(this.getContentPane(), "TXT file (UTF-8 encode)(*.txt)", "txt", false, "Save Text File",rFile.getFile());
            if (strFile.isEmpty()) {
                return;
            }

            out = new PrintWriter(new File(strFile), "UTF-8");
            out.println(this.jTextBookmark.getText());
            JOptionPane.showMessageDialog(null, "Save finished! \n\n\t" + strFile);
            rFile.setFile(strFile); //
        } catch (Exception ex) {
        } finally {
            try {
                out.close();
            } finally {
            }
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnBKMLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBKMLoadActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            RFile rFile = RFile.PDFOPEN;
            String strFile = FileChooser(this.getContentPane(), "PDF file (*.pdf)", "pdf", true, "Load PDF File",rFile.getFile());
            if (strFile.isEmpty()) {
                return;
            }
            int basePage = PDFBookmarkManager.getBasePage(this.jTexBkmPage.getText(), this.jTextPDFPage.getText());
            String str = PDFBookmarkManager.getBookmarkStringFromPDF(strFile, basePage);
            if (str.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No bookmark found! \n\n\t" + strFile);
                return;
            }
            this.jTextBookmark.setText(str);
            rFile.setFile(strFile); //
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnBKMLoadActionPerformed

    private void btnBKMSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBKMSaveActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:
            String bkmPage = this.jTexBkmPage.getText();
            String pdfPage = this.jTextPDFPage.getText();
            int basePage = PDFBookmarkManager.getBasePage(bkmPage, pdfPage);
            //JOptionPane.showMessageDialog(null, "Page Number Offset is: \t\t" + (basePage+1));
            RFile rFile = RFile.PDFSAVE;
            String strFile = FileChooser(this.getContentPane(), "PDF file (*.pdf)", "pdf", false, "Save PDF File",rFile.getFile());
            if (strFile.isEmpty()) {
                return;
            }
            String str = this.jTextBookmark.getText();
            if (str.isEmpty()) {
                throw new Exception("Content of Bookmark is empty!");
            }

            PDFBookmarkManager.saveBookmarkStringToPDF(strFile, str, basePage);
            JOptionPane.showMessageDialog(null, "Save PDF finished!");
            rFile.setFile(strFile); //
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }//GEN-LAST:event_btnBKMSaveActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        this.jTextBookmark.setText("");
        this.jTexBkmPage.setText("1");
        this.jTextPDFPage.setText("1");
    }//GEN-LAST:event_btnClearActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.jTextBookmark.requestFocus();
    }//GEN-LAST:event_formWindowOpened

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainJFrame frame = new MainJFrame();
                frame.setVisible(true);
                frame.setResizable(false);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBKMLoad;
    private javax.swing.JButton btnBKMSave;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnSave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabelVersion;
    private javax.swing.JLabel jLabelWeb;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTexBkmPage;
    private javax.swing.JTextArea jTextBookmark;
    private javax.swing.JTextField jTextPDFPage;
    // End of variables declaration//GEN-END:variables
}
