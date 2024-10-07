/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package CRUD;

import CRUD.TelaPagamento;
import CONEXAO_BANCO.Banco_dados;
import MODULO_INICIAL.Admin;
import MODULO_INICIAL.Home;
import java.awt.Component;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author reg3jvl
 */
public class Vendas extends javax.swing.JDialog {

    /**
     * Creates new form Vendas
     */
    public Vendas(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    Banco_dados bd = new Banco_dados();
    boolean eh_valido, eh_cliente;
    float quantidade, quantidadeEstoque, quantidadeUpdadeBanco;
   
    private void pesquisaCliente(){
        if(bd.getConnection()){
            try{
                String cliente="";
                String query = "SELECT * FROM cliente WHERE nome_cli LIKE ?";
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1, "%"+jTNameCliS.getText()+"%");
                ResultSet rs = smtp.executeQuery();
                
                while(rs.next()){
                    String add1 = rs.getString("id_cli");
                    jTCodeCliS.setText(add1);
                    
                    
                    String add2 = rs.getString("cpf_cli");
                    jTCpfCliS.setText(add2);
                    
                    String add3 = rs.getString("nome_cli");
                    jTNameCliS.setText(add3);
                };
                smtp.close();
                bd.conexao.close();
            }catch(SQLException e){
                System.out.println("Error when searching... "+e.toString());
            }
        }
    }
    
    private void venda(){
        if(bd.getConnection()){
            try{
                String query = "INSERT vendas(cliente_idCliente) VALUES(?)";
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1, jTCodeCliS.getText());
                smtp.executeUpdate();
                eh_cliente = true;
                smtp.close();
                bd.conexao.close();
                
            }catch(SQLException e){
                eh_cliente = false;
                adicionarIdNaoClientes();
                
            }
        }
    }

    private void adicionarIdNaoClientes(){
        if(!eh_cliente){
            if(bd.getConnection()){
                try{
                    String query = "INSERT vendas(cliente_idCliente) VALUES(?)";
                    PreparedStatement smtp = bd.conexao.prepareStatement(query);
                    smtp.setString(1, null);
                    smtp.executeUpdate();
                    smtp.close();
                    bd.conexao.close();
                }catch(SQLException e){
                    System.out.println("Error when searching... "+e.toString());
                }
            }
        }
    }
    private void pesquisaProduto(){
        if(bd.getConnection()){
               try{
                String query = "SELECT * FROM produto WHERE id_produto LIKE ?";
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1, "%"+jTCodeProductS.getText()+"%");
                ResultSet rs = smtp.executeQuery();
                
                while(rs.next()){
                    String add1 = rs.getString("id_produto");
                    jTCodeProductS.setText(add1);
                    
                    String add2 = rs.getString("descricao_produto");
                    jTProductS.setText(add2);
                    
                    String add3 = rs.getString("valor_unitario_produto");
                    jTUnitaryValueS.setText(add3);
                    
                    String add4 = rs.getString("quantidade_estoque_produto");
                    jTQuantityStockS.setValue(Integer.valueOf(add4));
                    
                }
                smtp.close();
                bd.conexao.close();
            }catch(SQLException e){
                System.out.println("Error when searching... "+e.toString());
            }
        }
    }
    
    private void idVenda(){
        if(bd.getConnection()){
            try{
                String query = "SELECT MAX(id_vendas) AS id_vendas FROM vendas";
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                ResultSet rs = smtp.executeQuery();
                while (rs.next()){
                    String add1 = rs.getString("id_vendas");
                    jTNoteFisS.setText(add1);
                }
                smtp.close();
                bd.conexao.close();
            }catch(SQLException erro){
                JOptionPane.showMessageDialog(null,"Error! Try again."
                        +erro.toString());
            }
        }
    }
    private void verificarQuantidadeVendida(){
        quantidade = Float.parseFloat(jTQuantityS.getText());
        quantidadeEstoque = Float.parseFloat(jTQuantityStockS.getValue().toString());
        if(quantidadeEstoque>=quantidade){
            calcularPrecoProduto();
        }else{
            JOptionPane.showMessageDialog(null,"Error! Insufficient quantity of items in stock.");
        }
        
    }
    
    private void calcularPrecoProduto(){
        quantidade = Float.parseFloat(jTQuantityS.getText());
        float valor = Float.parseFloat(jTUnitaryValueS.getText());
        float total = quantidade * valor;
        jTValueS.setText(String.valueOf(total));
    }
    
    private void adicionarItensVendas(){
        if(bd.getConnection()){
            try{
                String query = "INSERT venda_has_produto (fk_id_vendas, fk_id_produto, valor_vp, total_vp,"
                        + "quantidade_vp) VALUES (?,?,?,?,?)";
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1,jTNoteFisS.getText());
                smtp.setString(2, jTCodeProductS.getText());
                smtp.setString(3, jTUnitaryValueS.getText());
                smtp.setString(4, jTValueS.getText());
                smtp.setString(5, jTQuantityS.getText());
                smtp.executeUpdate();
                smtp.close();
                bd.conexao.close();

            }catch(SQLException erro){
                JOptionPane.showMessageDialog(null,"Error! Try again."
                        +erro.toString());
            }
        }
    }
    
    private void limparCampos(JPanel jPanel){
        Component[] componentes = jPanel.getComponents();
        for(Component componente : componentes){
            if(componente instanceof JTextField){
                JTextField camposTF = (JTextField)componente;
                camposTF.setText("");
            }
        }
    }
    
    private void somarNotaFiscal(){
        if(bd.getConnection()){
            try{
                String query = "SELECT SUM(total_vp) AS total FROM venda_has_produto "
                        + "WHERE fk_id_vendas = ?";
                
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1,jTNoteFisS.getText());
                ResultSet rs;
                rs = smtp.executeQuery();
                if(rs.next()){
                    String add1 = rs.getString("total");
                    jTTotalValueSales.setText(add1);
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
    
    private void consultarNotaFiscal(){
        if(bd.getConnection()){
            try{
                String query = "SELECT vp.fk_id_produto, "
                        + "produto.descricao_produto,"
                        + "vp.valor_vp,"
                        + "vp.quantidade_vp,"
                        + "vp.total_vp "
                        + "FROM venda_has_produto AS vp "
                        + "INNER JOIN produto ON vp.fk_id_produto = produto.id_produto "
                        + "WHERE vp.fk_id_vendas = ?";
                
                PreparedStatement smtp = bd.conexao.prepareStatement(query);
                smtp.setString(1,jTNoteFisS.getText());
                ResultSet rs;
                rs = smtp.executeQuery();
                DefaultTableModel model = (DefaultTableModel)jTabelaNotaFiscalV.getModel();
                model.setNumRows(0);
                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getString("fk_id_produto"),
                        rs.getString("descricao_produto"),
                        rs.getString("valor_vp"),
                        rs.getString("quantidade_vp"),
                        rs.getString("total_vp")});
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
    
    public void updateQuantidadeBanco(){
        if(bd.getConnection()){
            try{
                float result = quantidadeEstoque - quantidade;
                String query = "UPDATE produto SET quantidade_estoque_produto = ? WHERE id_produto = ?";
                PreparedStatement alterar= bd.conexao.prepareStatement(query);
                
                alterar.setFloat(1, result);
                alterar.setString(2, jTCodeProductS.getText());
                jTQuantityStockS.setValue(result);
                
                alterar.executeUpdate();
                JOptionPane.showMessageDialog(null, "Changed quantity!");
                alterar.close();
                bd.conexao.close();
            }catch(SQLException erro){
                JOptionPane.showMessageDialog(null,"Error! Try again."
                        +erro.toString());
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

        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanelClienteV = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTCpfCliS = new javax.swing.JTextField();
        jTCodeCliS = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTNameCliS = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanelProdutoV = new javax.swing.JPanel();
        jTProductS = new javax.swing.JTextField();
        jBStartSalling = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTQuantityS = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTValueS = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jBAdd = new javax.swing.JButton();
        jTCodeProductS = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTNoteFisS = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTUnitaryValueS = new javax.swing.JTextField();
        jTQuantityStockS = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabelaNotaFiscalV = new javax.swing.JTable();
        jBNewSelling = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jBFinishSalling = new javax.swing.JButton();
        jTTotalValueSales = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();

        jMenu1.setText("jMenu1");

        jMenu2.setText("jMenu2");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 15, 93));

        jPanel2.setBackground(new java.awt.Color(255, 245, 217));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Sales");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(428, 428, 428)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanelClienteV.setBackground(new java.awt.Color(255, 245, 217));
        jPanelClienteV.setBorder(javax.swing.BorderFactory.createTitledBorder("Customer Data"));

        jLabel2.setText("Code:");

        jLabel4.setText("CPF:");

        jTNameCliS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTNameCliSActionPerformed(evt);
            }
        });

        jLabel3.setText("Name:");

        javax.swing.GroupLayout jPanelClienteVLayout = new javax.swing.GroupLayout(jPanelClienteV);
        jPanelClienteV.setLayout(jPanelClienteVLayout);
        jPanelClienteVLayout.setHorizontalGroup(
            jPanelClienteVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClienteVLayout.createSequentialGroup()
                .addGroup(jPanelClienteVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelClienteVLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTNameCliS, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelClienteVLayout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addGroup(jPanelClienteVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelClienteVLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel4))
                    .addComponent(jTCpfCliS, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelClienteVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTCodeCliS, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelClienteVLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanelClienteVLayout.setVerticalGroup(
            jPanelClienteVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelClienteVLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelClienteVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelClienteVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTCodeCliS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTCpfCliS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTNameCliS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanelProdutoV.setBackground(new java.awt.Color(255, 245, 217));
        jPanelProdutoV.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Product Data", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        jBStartSalling.setBackground(new java.awt.Color(0, 204, 0));
        jBStartSalling.setText("Start Selling");
        jBStartSalling.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBStartSalling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBStartSallingActionPerformed(evt);
            }
        });

        jLabel7.setText("Product:");

        jLabel8.setText("Quantity in stock:");

        jTQuantityS.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                jTQuantitySCaretUpdate(evt);
            }
        });
        jTQuantityS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTQuantitySActionPerformed(evt);
            }
        });

        jLabel9.setText("Amount:");

        jTValueS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTValueSActionPerformed(evt);
            }
        });

        jLabel6.setText("Code:");

        jBAdd.setText("ADD");
        jBAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBAddActionPerformed(evt);
            }
        });

        jTCodeProductS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTCodeProductSActionPerformed(evt);
            }
        });

        jLabel5.setText("Invoice:");

        jLabel12.setText("Value:");

        jLabel10.setText("Unitary value:");

        jTQuantityStockS.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));

        javax.swing.GroupLayout jPanelProdutoVLayout = new javax.swing.GroupLayout(jPanelProdutoV);
        jPanelProdutoV.setLayout(jPanelProdutoVLayout);
        jPanelProdutoVLayout.setHorizontalGroup(
            jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel6)
                                .addGap(84, 84, 84)
                                .addComponent(jLabel7))
                            .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTNoteFisS, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jBStartSalling))
                                .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                                    .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTCodeProductS, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jTQuantityStockS, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProdutoVLayout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addGap(80, 80, 80)
                                                .addComponent(jLabel12)
                                                .addGap(28, 28, 28))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProdutoVLayout.createSequentialGroup()
                                                .addComponent(jTQuantityS, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTValueS, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                                            .addComponent(jTProductS, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTUnitaryValueS, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                                                    .addGap(13, 13, 13)
                                                    .addComponent(jLabel10)))))))))
                    .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jBAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelProdutoVLayout.setVerticalGroup(
            jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTNoteFisS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBStartSalling))
                .addGap(24, 24, 24)
                .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                        .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTCodeProductS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTProductS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanelProdutoVLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(8, 8, 8)
                        .addComponent(jTUnitaryValueS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanelProdutoVLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTQuantityS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTValueS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTQuantityStockS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jBAdd)
                .addContainerGap())
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText(" Value:  R$");

        jTabelaNotaFiscalV.setBackground(new java.awt.Color(255, 250, 250));
        jTabelaNotaFiscalV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Code", "Description", "Unitary Value", "Amount", "Value"
            }
        ));
        jScrollPane1.setViewportView(jTabelaNotaFiscalV);

        jBNewSelling.setBackground(new java.awt.Color(0, 204, 51));
        jBNewSelling.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBNewSelling.setForeground(new java.awt.Color(255, 255, 255));
        jBNewSelling.setText("New Selling");
        jBNewSelling.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBNewSelling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBNewSellingActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 0, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Cancel");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jBFinishSalling.setBackground(new java.awt.Color(255, 0, 153));
        jBFinishSalling.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jBFinishSalling.setForeground(new java.awt.Color(255, 255, 255));
        jBFinishSalling.setText("Finalize Sale");
        jBFinishSalling.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jBFinishSalling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBFinishSallingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jBNewSelling, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBFinishSalling, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelClienteV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelProdutoV, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTTotalValueSales, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanelClienteV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanelProdutoV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jBFinishSalling, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jBNewSelling, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTTotalValueSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        setJMenuBar(jMenuBar1);

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

    private void jTCodeProductSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTCodeProductSActionPerformed
        pesquisaProduto();
    }//GEN-LAST:event_jTCodeProductSActionPerformed

    private void jTNameCliSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTNameCliSActionPerformed
        pesquisaCliente();
    }//GEN-LAST:event_jTNameCliSActionPerformed

    private void jBStartSallingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBStartSallingActionPerformed
        venda();
        idVenda();
    }//GEN-LAST:event_jBStartSallingActionPerformed

    private void jBAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBAddActionPerformed
        adicionarItensVendas();
        updateQuantidadeBanco();
        consultarNotaFiscal();
        somarNotaFiscal();
    }//GEN-LAST:event_jBAddActionPerformed

    private void jTQuantitySActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTQuantitySActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTQuantitySActionPerformed

    private void jBNewSellingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBNewSellingActionPerformed

        limparCampos(jPanelClienteV);
        limparCampos(jPanelProdutoV);
    }//GEN-LAST:event_jBNewSellingActionPerformed

    private void jTValueSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTValueSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTValueSActionPerformed

    private void jBFinishSallingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBFinishSallingActionPerformed
        if(eh_cliente){
            TelaPagamentoCliente telaPagamentoCliente = new TelaPagamentoCliente(null, true);
            telaPagamentoCliente.setVisible(true);

        }else{
            TelaPagamento telaPagamento = new TelaPagamento(null, true);
            telaPagamento.setVisible(true);
            

        }
        
    }//GEN-LAST:event_jBFinishSallingActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        new Admin().setVisible(true);

        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTQuantitySCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jTQuantitySCaretUpdate
        verificarQuantidadeVendida();// TODO add your handling code here:
    }//GEN-LAST:event_jTQuantitySCaretUpdate

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
            java.util.logging.Logger.getLogger(Vendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vendas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Vendas dialog = new Vendas(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jBAdd;
    private javax.swing.JButton jBFinishSalling;
    private javax.swing.JButton jBNewSelling;
    private javax.swing.JButton jBStartSalling;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelClienteV;
    private javax.swing.JPanel jPanelProdutoV;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTCodeCliS;
    private javax.swing.JTextField jTCodeProductS;
    private javax.swing.JTextField jTCpfCliS;
    private javax.swing.JTextField jTNameCliS;
    private javax.swing.JTextField jTNoteFisS;
    private javax.swing.JTextField jTProductS;
    private javax.swing.JTextField jTQuantityS;
    private javax.swing.JSpinner jTQuantityStockS;
    private javax.swing.JTextField jTTotalValueSales;
    private javax.swing.JTextField jTUnitaryValueS;
    private javax.swing.JTextField jTValueS;
    private javax.swing.JTable jTabelaNotaFiscalV;
    // End of variables declaration//GEN-END:variables
}
