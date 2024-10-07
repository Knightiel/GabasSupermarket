/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package CRUD;

import CONEXAO_BANCO.Banco_dados;
import MODULO_INICIAL.Home;
import java.awt.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author reg3jvl
 */
public class Produto extends javax.swing.JDialog {

    

    
    
    /**
     * Creates new form Cliente
     */
    public Produto(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        pesquisaProduto();
        initTableListener();        
    }
    Banco_dados bd = new Banco_dados();

    public Produto() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void cadastroProduto(){
        if(bd.getConnection()){
            try{
                String query = "INSERT produto (descricao_produto,marca_produto,"
                        + "valor_unitario_produto,categoria_produto, quantidade_estoque_produto) VALUES(?,?,?,?,?)";
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1, jTDescription.getText());
                
                String valorSelecionado = jCBrand.getSelectedItem().toString();
                smtp.setString(2,valorSelecionado);
                
                smtp.setString(3, jTUnitaryValue.getText());
                
                smtp.setString(4, jTCategory.getText());
                smtp.setString(5, jSStorage.getValue().toString());
                
                    
                smtp.executeUpdate();
                JOptionPane.showMessageDialog(null, "Registered!");
                smtp.close();
                bd.conexao.close();
                
            }catch(SQLException erro){
                JOptionPane.showMessageDialog(null,"Error! Try again."
                        +erro.toString());
            }
        }
    }
    
    private void pesquisaProduto(){
        if(bd.getConnection()){
            try{
                String query = "SELECT * FROM produto WHERE descricao_produto LIKE ?";
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1, "%"+jTPesquisar.getText()+"%");
                ResultSet rs = smtp.executeQuery();
                DefaultTableModel tabela = (DefaultTableModel) jTabelaPesquisaProduto.getModel();
                tabela.setNumRows(0);
                while(rs.next()){
                    tabela.addRow(new Object[]{
                    rs.getString("id_produto"),
                    rs.getString("descricao_produto"),
                    rs.getString("marca_produto"),
                    rs.getString("valor_unitario_produto"),
                    rs.getString("categoria_produto"),
                    rs.getString("quantidade_estoque_produto")
                });
                }
                smtp.close();
                bd.conexao.close();
            }catch(SQLException e){
                System.out.println("Error when searching... "+e.toString());
            }
        }
    }
    
    private void initTableListener(){
        jTabelaPesquisaProduto.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int selectedRow = jTabelaPesquisaProduto.getSelectedRow();
                if(selectedRow != -1){
                    jLId.setText(jTabelaPesquisaProduto.getValueAt(selectedRow, 0).toString());
                    jTDescription.setText(jTabelaPesquisaProduto.getValueAt(selectedRow, 1).toString());
                    jCBrand.setSelectedItem(jTabelaPesquisaProduto.getValueAt(selectedRow, 2).toString());           
                    jTUnitaryValue.setText(jTabelaPesquisaProduto.getValueAt(selectedRow, 3).toString());
                    jTCategory.setText(jTabelaPesquisaProduto.getValueAt(selectedRow, 4).toString());
                    jSStorage.setValue(Integer.valueOf(jTabelaPesquisaProduto.getValueAt(selectedRow, 5).toString()));
                    
                }
            }
        }); 
    }   
    
    private void alterarProduto(){
        if (bd.getConnection()){
            try{
                String query = "UPDATE produto SET descricao_produto = ?,"
                        + "marca_produto = ?,valor_unitario_produto = ?,"
                        +"categoria_produto = ?, quantidade_estoque_produto = ? WHERE id_produto = ?";
                PreparedStatement alterar= bd.conexao.prepareStatement(query);
                
                alterar.setString(1,jTDescription.getText());
                alterar.setString(2,jCBrand.getSelectedItem().toString());
                alterar.setString(3,jTUnitaryValue.getText());
                alterar.setString(4,jTCategory.getText());
                alterar.setString(5,jSStorage.getValue().toString());
                alterar.setString(6,jLId.getText());

                 
                alterar.executeUpdate();
                JOptionPane.showMessageDialog(null, "Altered!");
                alterar.close();
                bd.conexao.close();
                
            }catch(SQLException erro){
                JOptionPane.showMessageDialog(null,"Error! Try again."
                        +erro.toString());
                 
            }
        }
    }
    
    private void excluirProduto(){
        if(bd.getConnection()){
            try{
                String query = "DELETE FROM produto WHERE id_produto = ?";
                PreparedStatement excluir = bd.conexao.prepareStatement(query);
                String index = (String)jTabelaPesquisaProduto.getModel().getValueAt(jTabelaPesquisaProduto.getSelectedRow(), 0);
                excluir.setString(1, index);
                
                int escolha = JOptionPane.showConfirmDialog(null,"Do you want to delete the product?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if(escolha==JOptionPane.YES_OPTION){
                    int resultado = excluir.executeUpdate();
                    if(resultado>0){
                        JOptionPane.showMessageDialog(null, "Deleted!"); 
                    }else{
                        JOptionPane.showMessageDialog(null, "Error in deletion!");
                    }
                    excluir.close();
                    bd.conexao.close();
                }
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null,"Error! Try again."
                        +e.toString());
            }
        }
    }
    
    private void limparCampos(JPanel jPanel){
        Component[] componentes = jPanel3.getComponents();
        for(Component componente : componentes){
            if(componente instanceof JTextField){
                JTextField camposTF = (JTextField)componente;
                camposTF.setText("");
            }
        }
    }
    
    private void limparTabela(){
        DefaultTableModel table = (DefaultTableModel)jTabelaPesquisaProduto.getModel();
        table.setNumRows(0);
    }
    
    private void validaCamposPreenchidos(){
        if(jTDescription.getText().isEmpty() || jTUnitaryValue.getText().isEmpty() || jTCategory.getText().isEmpty()){
            jBCadastrar.setEnabled(false);
        }else{
            int number = Integer.parseInt(jSStorage.getValue().toString());
            if(number == 0){
                jBCadastrar.setEnabled(false);
            }else{
                jBCadastrar.setEnabled(true);
            }
            String valorSelecionado = jCBrand.getSelectedItem().toString();
            if(valorSelecionado.equals("Brand:")){
                jBCadastrar.setEnabled(false);
            }else{
                jBCadastrar.setEnabled(true);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField6 = new javax.swing.JTextField();
        jPanelProduto = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jBAlterar = new javax.swing.JButton();
        jBCancelar = new javax.swing.JButton();
        jBExcluir = new javax.swing.JButton();
        jTUnitaryValue = new javax.swing.JTextField();
        jBLimparCadastro = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jTDescription = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jCBrand = new javax.swing.JComboBox<>();
        jBCadastrar = new javax.swing.JButton();
        jLId = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jTCategory = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jSStorage = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabelaPesquisaProduto = new javax.swing.JTable();
        jBLimparTabela = new javax.swing.JButton();
        jTPesquisar = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        jTextField6.setText("jTextField6");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelProduto.setBackground(new java.awt.Color(0, 15, 93));

        jPanel3.setBackground(new java.awt.Color(255, 245, 217));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Product Registration"));

        jBAlterar.setBackground(new java.awt.Color(0, 204, 0));
        jBAlterar.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jBAlterar.setForeground(new java.awt.Color(255, 255, 255));
        jBAlterar.setText("Alter");
        jBAlterar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAlterarActionPerformed(evt);
            }
        });

        jBCancelar.setBackground(new java.awt.Color(51, 51, 51));
        jBCancelar.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jBCancelar.setForeground(new java.awt.Color(255, 255, 255));
        jBCancelar.setText("Cancel");
        jBCancelar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCancelarActionPerformed(evt);
            }
        });

        jBExcluir.setBackground(new java.awt.Color(255, 0, 0));
        jBExcluir.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jBExcluir.setForeground(new java.awt.Color(255, 255, 255));
        jBExcluir.setText("Delete");
        jBExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBExcluirActionPerformed(evt);
            }
        });

        jTUnitaryValue.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTUnitaryValueCaretUpdate(evt);
            }
        });

        jBLimparCadastro.setBackground(new java.awt.Color(255, 0, 204));
        jBLimparCadastro.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jBLimparCadastro.setForeground(new java.awt.Color(255, 255, 255));
        jBLimparCadastro.setText("Clean");
        jBLimparCadastro.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBLimparCadastro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBLimparCadastroActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jLabel2.setText("Description");

        jTDescription.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTDescriptionCaretUpdate(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jLabel6.setText("Unitary Value");

        jCBrand.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Brand:", "Bosch" }));
        jCBrand.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jCBrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBrandActionPerformed(evt);
            }
        });

        jBCadastrar.setBackground(new java.awt.Color(0, 0, 255));
        jBCadastrar.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jBCadastrar.setForeground(new java.awt.Color(255, 255, 255));
        jBCadastrar.setText("Register");
        jBCadastrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBCadastrar.setEnabled(false);
        jBCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBCadastrarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jLabel9.setText("Category");

        jTCategory.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTCategoryCaretUpdate(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jLabel10.setText("Quantity in stock");

        jSStorage.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        jSStorage.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSStorageStateChanged(evt);
            }
        });

        jLabel1.setText("  ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(77, 77, 77)
                        .addComponent(jBLimparCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jBCancelar))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jCBrand, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTUnitaryValue, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(43, 43, 43))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(80, 80, 80)
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(27, 27, 27)))
                                .addComponent(jLId, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSStorage)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                    .addComponent(jBCadastrar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBExcluir, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jBAlterar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jLabel6))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(131, 131, 131)
                        .addComponent(jLabel9))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(110, 110, 110)
                        .addComponent(jLabel10)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLId, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTUnitaryValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jSStorage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCadastrar)
                    .addComponent(jBExcluir)
                    .addComponent(jBAlterar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBCancelar)
                    .addComponent(jBLimparCadastro))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 245, 217));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Search"));
        jPanel4.setToolTipText("");
        jPanel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel8.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jLabel8.setText("Name:");

        jTabelaPesquisaProduto.setBackground(new java.awt.Color(255, 250, 250));
        jTabelaPesquisaProduto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Código", "Description", "Brand", "Unitary Value", "Category", "Quantity in stock"
            }
        ));
        jScrollPane1.setViewportView(jTabelaPesquisaProduto);

        jBLimparTabela.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jBLimparTabela.setText("Clean");
        jBLimparTabela.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBLimparTabela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBLimparTabelaActionPerformed(evt);
            }
        });

        jTPesquisar.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N

        jButton1.setFont(new java.awt.Font("Bosch Office Sans", 1, 12)); // NOI18N
        jButton1.setText("Search");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBLimparTabela)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jBLimparTabela))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelProdutoLayout = new javax.swing.GroupLayout(jPanelProduto);
        jPanelProduto.setLayout(jPanelProdutoLayout);
        jPanelProdutoLayout.setHorizontalGroup(
            jPanelProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProdutoLayout.createSequentialGroup()
                .addGroup(jPanelProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProdutoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelProdutoLayout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanelProdutoLayout.setVerticalGroup(
            jPanelProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProdutoLayout.createSequentialGroup()
                .addGroup(jPanelProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProdutoLayout.createSequentialGroup()
                        .addGap(662, 662, 662)
                        .addComponent(jLabel7))
                    .addGroup(jPanelProdutoLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelProduto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelProduto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       pesquisaProduto();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jBLimparTabelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimparTabelaActionPerformed
        limparTabela();
        
    }//GEN-LAST:event_jBLimparTabelaActionPerformed

    private void jBCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCadastrarActionPerformed
        cadastroProduto();
        limparCampos(jPanel3);
        pesquisaProduto();
        // limparDados();
    }//GEN-LAST:event_jBCadastrarActionPerformed

    private void jBLimparCadastroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBLimparCadastroActionPerformed
        limparCampos(jPanel3);
    }//GEN-LAST:event_jBLimparCadastroActionPerformed

    private void jBExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBExcluirActionPerformed
        excluirProduto();
        limparCampos(jPanel3);
        limparTabela();
        pesquisaProduto();
    }//GEN-LAST:event_jBExcluirActionPerformed

    private void jBCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBCancelarActionPerformed
        this.dispose();
        new Home().setVisible(true);
    }//GEN-LAST:event_jBCancelarActionPerformed

    private void jBAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAlterarActionPerformed
        alterarProduto();
        limparCampos(jPanel3);
        limparTabela();
        pesquisaProduto();
    }//GEN-LAST:event_jBAlterarActionPerformed

    private void jCBrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBrandActionPerformed
        validaCamposPreenchidos();
    }//GEN-LAST:event_jCBrandActionPerformed

    private void jTDescriptionCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTDescriptionCaretUpdate
        validaCamposPreenchidos();
    }//GEN-LAST:event_jTDescriptionCaretUpdate

    private void jTUnitaryValueCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTUnitaryValueCaretUpdate
        validaCamposPreenchidos();
    }//GEN-LAST:event_jTUnitaryValueCaretUpdate

    private void jTCategoryCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTCategoryCaretUpdate
        validaCamposPreenchidos();
    }//GEN-LAST:event_jTCategoryCaretUpdate

    private void jSStorageStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSStorageStateChanged
        validaCamposPreenchidos();
    }//GEN-LAST:event_jSStorageStateChanged

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
            java.util.logging.Logger.getLogger(Produto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Produto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Produto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Produto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Produto dialog = new Produto(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jBAlterar;
    private javax.swing.JButton jBCadastrar;
    private javax.swing.JButton jBCancelar;
    private javax.swing.JButton jBExcluir;
    private javax.swing.JButton jBLimparCadastro;
    private javax.swing.JButton jBLimparTabela;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jCBrand;
    private javax.swing.JLabel jLId;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelProduto;
    private javax.swing.JSpinner jSStorage;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTCategory;
    private javax.swing.JTextField jTDescription;
    private javax.swing.JTextField jTPesquisar;
    private javax.swing.JTextField jTUnitaryValue;
    private javax.swing.JTable jTabelaPesquisaProduto;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
