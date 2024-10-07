/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package CRUD;

import CONEXAO_BANCO.Banco_dados;
import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author reg3jvl
 */
public class TelaPagamentoCliente extends javax.swing.JDialog {
    
    /**
     * Creates new form TelaPagamento
     */
    public TelaPagamentoCliente(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        idVenda();
    }
    Banco_dados bd = new Banco_dados();
    float valorTotal, desconto, valorFinal,troco, dif, descontoNew;
    boolean pagamentoInvalido, ehValidoRetiradaEstoque;
    
    private void idVenda(){
        if(bd.getConnection()){
            try{
                String query = "SELECT MAX(id_vendas) AS id_vendas FROM vendas";
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                ResultSet rs = smtp.executeQuery();
                while (rs.next()){
                    String add1 = rs.getString("id_vendas");
                    jTNotaFiscal.setText(add1);
                }
                smtp.close();
                bd.conexao.close();
            }catch(SQLException erro){
                JOptionPane.showMessageDialog(null,"Error! Try again."
                        +erro.toString());
            }
        }
    }
    
    private void somarNotaFiscal(){
        if(bd.getConnection()){
            try{
                String query = "SELECT SUM(total_vp) AS total FROM venda_has_produto "
                        + "WHERE fk_id_vendas = ?";
                
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1,jTNotaFiscal.getText());
                ResultSet rs;
                rs = smtp.executeQuery();
                if(rs.next()){
                    String add1 = rs.getString("total");
                    jLsoma.setText(add1);
                }
                rs.close();
                smtp.close();
                bd.conexao.close();
                
            }catch(SQLException erro){
                JOptionPane.showMessageDialog(null,"Error! Try again."
                        +erro.toString());
            }
        }
    }
    
    private void calcularDesconto(){
        valorTotal = Float.valueOf(jLsoma.getText());
        desconto= Float.valueOf(jTdesconto.getText());
        descontoNew = (desconto/100);
        valorFinal = valorTotal - (valorTotal*descontoNew);
        jLvalorFinal.setText(String.valueOf(valorFinal));
    }
    
    private void receberTroco(){
        dif = Float.valueOf(jTpagamento.getText());
        troco = dif-valorFinal;
        jLtroco.setText(String.valueOf(troco));
        if(dif<=valorFinal){
            pagamentoInvalido=true;
        }
    }
    
    private void escolhaPagamento(){
        String tipo = jCforma.getSelectedItem().toString();
        if("Money".equals(tipo)){
            jTdesconto.setText("10");
        }else{
            if("Debit Card".equals(tipo)){
                jTdesconto.setText("5");
            }else{
                if("Credit Card".equals(tipo)){
                    jTdesconto.setText("0");
                }
            }
        }
    }
    
    private void limparCampoPagamento(){
        jTpagamento.setText("");
        
    }
    
    private void caixa(){
            if(bd.getConnection()){
                try{
                    String query = "INSERT caixa (fk_id_vendas,valor_total_caixa,desconto,valor_final_caixa,forma_pagamento_caixa) VALUES (?,?,?,?,?)";
                    PreparedStatement smtp = bd.conexao.prepareStatement(query);
                    smtp.setString(1, jTNotaFiscal.getText());
                    smtp.setString(2, jLsoma.getText());
                    smtp.setString(3, jTdesconto.getText());
                    smtp.setString(4, jLvalorFinal.getText());
                    smtp.setString(5, jCforma.getSelectedItem().toString());
                    if(!pagamentoInvalido){
                        smtp.executeUpdate();
                        smtp.close();
                        bd.conexao.close();
                        ehValidoRetiradaEstoque = true;
                    
                    }else{
                        JOptionPane.showMessageDialog(null,"Error! Pay the required amount.");
                    } 
                }catch(SQLException erro){
                    JOptionPane.showMessageDialog(null,"Error! Try again."
                            +erro.toString());
                }
            }
            ehValidoRetiradaEstoque= false;
    }
    
    public boolean validaRetiradaEstoque(){
        return ehValidoRetiradaEstoque;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTNotaFiscal = new javax.swing.JTextField();
        jButtonFinish = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jCforma = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLsoma = new javax.swing.JLabel();
        jTdesconto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLtroco = new javax.swing.JLabel();
        jLvalorFinal = new javax.swing.JLabel();
        jTpagamento = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 15, 93));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Payment:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Fiscal Note Nº:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jTNotaFiscal.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTNotaFiscalCaretUpdate(evt);
            }
        });

        jButtonFinish.setText("Finish Sales");
        jButtonFinish.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButtonFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinishActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 245, 217));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel9.setText("Payment:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setText("Payment Method: ");

        jCforma.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choice", "Money", "Debit Card", "Credit Card" }));
        jCforma.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCforma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCformaActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel4.setText("Total Payment:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setText("Discount:");

        jLsoma.setBackground(new java.awt.Color(255, 255, 255));
        jLsoma.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N

        jTdesconto.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTdescontoCaretUpdate(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setText("Final Value:");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setText("Change:");

        jLtroco.setBackground(new java.awt.Color(255, 255, 255));
        jLtroco.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLtroco.setText("  ");

        jLvalorFinal.setBackground(new java.awt.Color(255, 255, 255));
        jLvalorFinal.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLvalorFinal.setText("  ");

        jTpagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTpagamentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jTdesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCforma, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTpagamento))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLsoma, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLvalorFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLtroco, javax.swing.GroupLayout.PREFERRED_SIZE, 263, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLsoma, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCforma, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTdesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLvalorFinal))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jTpagamento))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLtroco))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTNotaFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(148, 148, 148)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(123, 123, 123)
                        .addComponent(jButtonFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTNotaFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jButtonFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
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
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTNotaFiscalCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTNotaFiscalCaretUpdate
        somarNotaFiscal();
    }//GEN-LAST:event_jTNotaFiscalCaretUpdate

    private void jCformaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCformaActionPerformed
        escolhaPagamento();
    }//GEN-LAST:event_jCformaActionPerformed

    private void jButtonFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinishActionPerformed
        caixa();
        this.dispose();

    }//GEN-LAST:event_jButtonFinishActionPerformed

    private void jTdescontoCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTdescontoCaretUpdate
        calcularDesconto();// TODO add your handling code here:
    }//GEN-LAST:event_jTdescontoCaretUpdate

    private void jTpagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTpagamentoActionPerformed
        receberTroco();// TODO add your handling code here:
    }//GEN-LAST:event_jTpagamentoActionPerformed

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
            java.util.logging.Logger.getLogger(TelaPagamentoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaPagamentoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaPagamentoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaPagamentoCliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TelaPagamentoCliente dialog = new TelaPagamentoCliente(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonFinish;
    private javax.swing.JComboBox<String> jCforma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLsoma;
    private javax.swing.JLabel jLtroco;
    private javax.swing.JLabel jLvalorFinal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTNotaFiscal;
    private javax.swing.JTextField jTdesconto;
    private javax.swing.JTextField jTpagamento;
    // End of variables declaration//GEN-END:variables
}
