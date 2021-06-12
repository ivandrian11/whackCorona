package coronawhacks;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DatabaseConnect {
    private static Connection conn = connectDatabase();
    private static Statement stm;
    
    private static Connection connectDatabase(){
        try {
            String url ="jdbc:mysql://localhost/whack_corona";
            String user="root";
            String pass="";
            Class.forName("com.mysql.jdbc.Driver");
            conn =DriverManager.getConnection(url,user,pass);
            stm = conn.createStatement();
            System.out.println("Koneksi berhasil.");
        } catch (Exception e) {
            System.err.println("Koneksi Gagal: " +e.getMessage());
        }
        return conn;
    }
    
    public static ResultSet executeReadQuery(String query) {
      ResultSet rs = null;
      try {
         Statement stt = conn.createStatement();
         rs = stt.executeQuery(query);

      } catch (Exception e) {
         e.printStackTrace();
      }
      return rs;
   }
    
    public static void addScore(Timestamp ts, String name,int score){
        try{
            String sql = "insert into player (started_at,nama,skor) values ("+"'"+ts+"'"+","+"'"+name+"'"+","+"'"+score+"'"+")";
            java.sql.PreparedStatement pstm = conn.prepareStatement(sql);
            pstm.execute();
        } catch(HeadlessException | SQLException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}