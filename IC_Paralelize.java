import mpi.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class IC_Paralelize extends javax.swing.JFrame {
    private JButton selectButton;
    private JLabel imageLabel;
    private File selectedFile;
    public long pixelCount;
    
    public IC_Paralelize() {
        initComponents();
        setTitle("Image Processor");
        setLocationRelativeTo(null);
        setResizable(false);
        
        jProgressBar1.setValue(0);
        jProgressBar1.setStringPainted(true); 
        jTextArea4.setLineWrap(true);
        jTextArea4.setWrapStyleWord(true);

        selectButton = new JButton("Select Image");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    imageLabel.setText("Selected File: " + selectedFile.getName());

                    // Start MPI processing
                    try {
                        runMPIProcess(selectedFile.getAbsolutePath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        imageLabel = new JLabel("", SwingConstants.CENTER);
        jPanel1.add(selectButton);
        jPanel1.add(imageLabel);
    }
    

    private void runMPIProcess(String filePath) {
    SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
        @Override
        protected Void doInBackground() throws Exception {
            String mpjHome = System.getenv("MPJ_HOME");
            if (mpjHome == null) {
                throw new RuntimeException("MPJ_HOME environment variable is not set");
            }

            // Assuming mpjrun.bat is in the MPJ_HOME directory
            String mpjRunBat = mpjHome + File.separator + "bin" + File.separator + "mpjrun.bat";

            String[] command = {
                    "cmd.exe", "/c",
                    "cd /d " + System.getProperty("user.dir") + " && mpjrun.bat " + filePath
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.directory(new File(mpjHome));
            processBuilder.environment().put("MPJ_HOME", mpjHome);

            // Update jTextArea4 with debugging output
            publish("\n\nRunning MPI process with command: " + String.join(" ", command) + "\n");
            publish("Working directory: " + processBuilder.directory().getAbsolutePath() + "\n");

            long startTime = System.currentTimeMillis();

            Process process = processBuilder.start();

            // Outputting the result of the command
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    publish(line + "\n");
                }
            }

            process.waitFor();

            long endTime = System.currentTimeMillis();
            long totalExecutionTime = endTime - startTime;
            publish("MPI process completed in " + totalExecutionTime + " milliseconds.\n");
            
            // Calculate total pixels and update GUI
            pixelCount = calculateTotalPixels(filePath); // Update pixelCount variable
            
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            // Update GUI with chunks of output received from doInBackground
            for (String chunk : chunks) {
                jTextArea4.append(chunk);
            }
        }

        @Override
        protected void done() {
            // Optionally, you can perform any final GUI updates or actions here
        }
    };

    worker.execute();
}
    private long calculateTotalPixels(String filePath) {
    try {
        BufferedImage image = ImageIO.read(new File(filePath));
        if (image != null) {
            return (long) image.getWidth() * (long) image.getHeight();
        } else {
            return 0;
        }
    } catch (IOException e) {
        e.printStackTrace();
        return 0;
    }
}
    
    private boolean isImageFile(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jTextField2 = new javax.swing.JTextField();
        jProgressBar1 = new javax.swing.JProgressBar();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea4.setColumns(20);
        jTextArea4.setFont(new java.awt.Font("Comic Sans MS", 2, 12)); // NOI18N
        jTextArea4.setRows(5);
        jTextArea4.setText("OUTPUT :");
        jTextArea4.setMaximumSize(new java.awt.Dimension(100, 100));
        jTextArea4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextArea4FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextArea4FocusLost(evt);
            }
        });
        jScrollPane4.setViewportView(jTextArea4);

        jTextField2.setText("PIXEL :");
        jTextField2.setPreferredSize(new java.awt.Dimension(90, 30));
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });

        jButton3.setText("UPLOAD");
        jButton3.setMinimumSize(new java.awt.Dimension(72, 30));
        jButton3.setPreferredSize(new java.awt.Dimension(75, 40));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        jLabel1.setMaximumSize(new java.awt.Dimension(200, 200));
        jLabel1.setMinimumSize(new java.awt.Dimension(200, 200));
        jLabel1.setPreferredSize(new java.awt.Dimension(164, 164));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextArea4FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea4FocusGained
    if(jTextArea4.getText().equals("OUTPUT :"))
        {
            jTextArea4.setText("");
        }
    }//GEN-LAST:event_jTextArea4FocusGained

    private void jTextArea4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextArea4FocusLost
    if(jTextArea4.getText().equals(""))
        {
            jTextArea4.setText("OUTPUT :");
        }
    }//GEN-LAST:event_jTextArea4FocusLost

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
    if(jTextField2.getText().equals("PIXEL :"))
        {
            jTextField2.setText("");
        }
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
    if(jTextField2.getText().equals(""))
        {
            jTextField2.setText("PIXEL :");
        }
    }//GEN-LAST:event_jTextField2FocusLost

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Action when UPLOAD button is clicked
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            // Check if the file is an image
            
            if (!isImageFile(selectedFile)) {
                JOptionPane.showMessageDialog(this, "The selected file is not an image.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Set label to show selected image
            ImageIcon imageIcon = new ImageIcon(filePath);
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(410, 330, Image.SCALE_SMOOTH);
            jLabel1.setIcon(new ImageIcon(scaledImage));

            // Run MPI process with selected file
            try {
                pixelCount = calculateTotalPixels(filePath);
                runMPIProcess(filePath);
                jTextField2.setText("TOTAL PIXEL : "+pixelCount);
                jProgressBar1.setValue(100);
            } catch (Exception ex) {
                ex.printStackTrace();
                // Handle error if MPI process fails to start
                JOptionPane.showMessageDialog(this, "Failed to start MPI process.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    

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
            java.util.logging.Logger.getLogger(IC_Paralelize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IC_Paralelize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IC_Paralelize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IC_Paralelize.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IC_Paralelize().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
