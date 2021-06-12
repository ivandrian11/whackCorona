package coronawhacks;

import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

public class HighScore extends JFrame{
    private javax.swing.JButton backButton;
    private javax.swing.JLabel bg;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableScore;
    
    public HighScore() {
        initComponents();
        setSize(505, 525);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        getHighScore();
    }
    
    private void getHighScore(){
        try{
            ResultSet rs = DatabaseConnect.executeReadQuery("select * from player order by skor desc limit 10");
            DefaultTableModel table = (DefaultTableModel) tableScore.getModel();
            table.setRowCount(0);
            int peringkat[]={1,2,3,4,5,6,7,8,9,10};
            int i=0;
            while(rs.next()){
                table.addRow(new Object[]{ peringkat[i] , (rs.getString("nama")), (rs.getInt("skor"))});
                i++;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private void initComponents() {
        backButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableScore = new javax.swing.JTable();
        bg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        backButton.setIcon(new javax.swing.ImageIcon("E:\\NGODING\\Java\\pbo\\CoronaWhacks\\src\\coronawhacks\\Assets\\backButton.png")); // NOI18N
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        getContentPane().add(backButton);
        backButton.setBounds(30, 420, 160, 49);

        tableScore.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tableScore.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "NO", "NAME", "SCORE"
            }
        ));
        tableScore.setFocusable(false);
        tableScore.setInheritsPopupMenu(true);
        tableScore.setRowHeight(20);
        jScrollPane1.setViewportView(tableScore);
        if (tableScore.getColumnModel().getColumnCount() > 0) {
            tableScore.getColumnModel().getColumn(0).setMaxWidth(30);
        }

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(30, 200, 440, 130);

        bg.setIcon(new javax.swing.ImageIcon("E:\\NGODING\\Java\\pbo\\CoronaWhacks\\src\\coronawhacks\\Assets\\bg-hg.png")); // NOI18N
        getContentPane().add(bg);
        bg.setBounds(0, 0, 500, 500);
    }
    
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        new Start();
        this.dispose();
    }
}