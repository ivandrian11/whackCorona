package coronawhacks;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Random;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PlayGame extends JFrame implements ActionListener, Music{
    Creature[] creatures; // objek kotak
    static int creaturesFilled; // jumlah kotak yang tidak kosong dari 
    private static int maxCreatures = 4; // jumlah kotak yang berisi virus
    private static int numOfCreatures = 6; // jumlah kotak awal
    private static int targetScore = 400; // target skor awal
    private static int score; // skor yang sedang dimainkan
    private static int level = 1; // level yang sedang dimainkan
    private static int totalScore = 0; // total skor untuk disimpan dalam database
    private static final int MAX_LEVEL = 4; // banyak level
    private final double LENGTH_OF_GAME = 15000.0; // durasi game per level
    private Random rand = new Random(); // membuat objek random
    private JLabel scoreLabel, timeLabel, levelLabel;
    private static Timestamp timestamp;
    
    public PlayGame(){
        score = 0;
        creaturesFilled = 0;
        initGUI();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        if(level == 1){
            FloatControl gainControl = (FloatControl) Start.clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
            timestamp = new Timestamp(System.currentTimeMillis()); // ambil waktu mulai main
        }
    }
    
    public static void startGame(){
        PlayGame this_game = new PlayGame();
        // tampil pesan
        JOptionPane.showMessageDialog(this_game, "Instruksi: Perhatikan deskripsi poin dari virus.\n"+
                "Tedapat " + MAX_LEVEL + " level. Setiap level berdurasi 15 detik.\n"+
                "Semoga berhasil.");
        
        // perulangan sampai game selesai
        while(true){
	// tetap bermain sampai player gagal mencapai target skor atau level akhir telah diseleaikan
            while(level <= MAX_LEVEL) {
                // pengumuman perihal rincian level
                JOptionPane.showMessageDialog(this_game, "Level " + level + "\n"+
                        "Target skor: "+ targetScore + "\n"+
                        "Jumlah kotak: " + numOfCreatures + "\n"+
                        "Tekan OK untuk mulai.");
                
                this_game.playing(); // mainkan
                
                // gagal mencapai target skor
                if(score < targetScore) {
                    JOptionPane.showMessageDialog(this_game, "Skor: " + score +
                            "\nKamu tidak mencapai " + targetScore + " poin.  Game Over!");
                    break; 
                }
                
                // lanjut ke level berikutnya
                if(level < MAX_LEVEL)
                    JOptionPane.showMessageDialog(this_game, "Selamat, kamu berhasil lanjut ke level selanjutnya!");
                nextLevel();
                
                // mulai level selanjutnya lagi
                if(level <= MAX_LEVEL) {
                    this_game.dispose();
                    totalScore += score;
                    this_game = new PlayGame();
                }
            }
            
            // jika player telah menyelesaikan level terakhir
            if(level > MAX_LEVEL)
		JOptionPane.showMessageDialog(this_game, "Selamat, kamu berhasil menamatkan game ini!");
            
            // tambah ke database
            totalScore += score;
            String name = "";
            do{
                name = JOptionPane.showInputDialog(this_game, "Masukkan namamu?");
                if(name == null || name.equals(""))
                    JOptionPane.showMessageDialog(null, 
                              "NAMA TIDAK BOLEH KOSONG!!", 
                              "PERINGATAN", 
                              JOptionPane.WARNING_MESSAGE);
            } while(name == null || name.equals(""));
            DatabaseConnect.addScore(timestamp, name, totalScore);
            
            // game selesai, dan menanyakan apakah ingin main lagi
            int response = JOptionPane.showConfirmDialog(this_game, "Total Skor: "+totalScore+
                "\nTerima kasih telah bermain, "+name+"\nApakah kamu ingin bermain lagi?",
                "Bermain Kembali?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            // jika memilih tidak maka akan kembali ke menu utama
            if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION)
                break; 
			
            // jika iya maka akan kembali bermain lagi dari level 1
            resetLevel(); 
            this_game.dispose();
            this_game = new PlayGame();
	}
	// keluar dari game
	this_game.dispose();
    }
    
    private void playing() {
	double startTime = new Date().getTime(); // waktu dimulainya game
	double currentTime; // waktu saat ini yang dirender dari setiap frame
	double timeRemaining = LENGTH_OF_GAME; // sisa waktu di game

	// bermain selama 15 detik
	while(( LENGTH_OF_GAME - timeRemaining) < LENGTH_OF_GAME){
            reviveCreatures(); 
            updateCreatures(); 
			
            // sleep selama 32 ms untuk membantu update waktu
            try{
		Thread.sleep(32);
            } catch(InterruptedException e){}
			
            // mengupdate waktu saat ini
            currentTime = new Date().getTime(); 
			
            // menghitung waktu tersisa
            timeRemaining = LENGTH_OF_GAME - (currentTime - startTime);
			
            // update waktu
            updateTime(timeRemaining);
        }
    }

    private void updateCreatures(){
        // menambah life_count dari creature, jika sudah mencapai final_life maka ganti creature tersebut
	for(int x = 0; x < creatures.length; x++)
            creatures[x].update();
    }
    
    private void reviveCreatures(){
        // mengecek apakah terdapat kotak yang masih kosong dari kotak yang seharusnya(maxCreatures)
	if(creaturesFilled < maxCreatures){ 
            // mengacak tempat kemunculan virus
            int randomCreature = rand.nextInt(numOfCreatures);
            // jika tempat teracak kosong, maka tambahkan
            if(!creatures[randomCreature].getIsFilled()) {
		creatures[randomCreature].revive(); 
		creaturesFilled++;
            }
        }
    }
    
    private void updateTime(double timeRemaining) {
	// update label waktu
	timeLabel.setText("TIME: " + NumberFormat.getInstance().format(timeRemaining/1000));
        // jika waktunya mencapai di bawah 5 detik maka warnai merah
        if(timeRemaining <= 5000.0){
            timeLabel.setForeground(new Color(255, 0, 0));
            playMusic("\\build\\classes\\coronawhacks\\Assets\\beep-29.wav");
        }
    }
    
    private void updateScore(Creature check) {
        // akumulasi skor dengan melihat warna virus
        if(check.color.equals("black"))
            score += 50;
        else if(check.color.equals("red"))
            score -= 5;
        else if(check.color.equals("green"))
            score += 10;
        else
            score -= 30;
        // skor berwarna merah jika kurang dari target dan jika sudah maka berwarna hijau
        if(score < targetScore)
            scoreLabel.setForeground(new Color(255, 0, 0));
        else
            scoreLabel.setForeground(new Color(0, 255, 0));
        // update label skor
	scoreLabel.setText("SCORE: " + score);
    }
    
    private static void nextLevel() {
        level++; // menambah levle
        numOfCreatures += 3; // menambah 3 kotak
        maxCreatures += 1; // menambah batas kotak terisi
        targetScore += 50; // menambah target skor
    }
    
    private static void resetLevel() {
	// mengulang seperti peraturan awal
	level = 1; 
	numOfCreatures = 6;
        targetScore = 400;
        totalScore = 0;
        maxCreatures = 4;
    }
    
    public static int getLevel(){
        return level;
    }
    
    private void initGUI(){
        // mengatur JFrame PlayGame
	setSize(505, 525);
	setLayout(null); 
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setTitle("Whack Corona");
        
        // menambahkan JPanel
        JPanel contentPane = new JPanel();
        contentPane.setLayout(null);
        setContentPane(contentPane);
        
        // menambahkan ket skor
        scoreLabel = new JLabel();
        scoreLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        contentPane.add(scoreLabel);
        scoreLabel.setBounds(20, 10, 170, 30);
        scoreLabel.setFont(new Font("Cambria Math", Font.BOLD, 24));
        
        // menambahkan ket waktu
        timeLabel = new JLabel();
        timeLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        contentPane.add(timeLabel);
        timeLabel.setBounds(20, 50, 170, 30);
        timeLabel.setFont(new Font("Cambria Math", Font.BOLD, 24));
        
        // menambahkan ket level
        levelLabel = new JLabel();
        levelLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        contentPane.add(levelLabel);
        levelLabel.setBounds(20, 90, 170, 30);
        levelLabel.setFont(new Font("Cambria Math", Font.BOLD, 24));
	
        // menambahkan deskripsi
        JLabel description = new JLabel();
        description.setIcon(new javax.swing.ImageIcon(getClass().getResource("Assets/description.png"))); // NOI18N
        contentPane.add(description);
        description.setBounds(200, 10, 280, 110);
        
        // membuat panel bermain
	JPanel field = initField(); 
        field.setBounds(15, 140, 460, 340);
	contentPane.add(field);
        
        // menambahkan background
        JLabel bg = new JLabel();
        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("Assets/bg-game.png")));
        contentPane.add(bg);
        bg.setBounds(0, 0, 500, 500);
        
        field.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
            new ImageIcon(this.getClass().getResource("Assets/pointer.png")).getImage(),
            new Point(0,0),"custom cursor1"));

        // tampilkan label skor dan level awal
        scoreLabel.setText("SCORE: " + score);
        levelLabel.setText("LEVEL: " + level);
    }
    
    private JPanel initField() {
	// membuat panel untuk ruang bermain
	JPanel field = new JPanel(); 
	field.setLayout(new GridLayout(3, 3, 5, 5));
        field.setOpaque(false); // supaya transparan terhadap background
	// inisialisasi kotak berisi virus
	creatures = new Creature[numOfCreatures];
	for(int x = 0; x < creatures.length; x++) {
            creatures[x] = new Creature();
            creatures[x].addActionListener(this);
            field.add(creatures[x]);
        }
        return field;
    }
    
    public void playMusic(String location) {
	try {
            File musicPath = new File(System.getProperty("user.dir")+location);
            if(musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.out.println("Cannot fint the Audio File");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    public void actionPerformed(ActionEvent event){
        // jika pemain klik creature yang terisi maka poin didapatkan
	Creature clickedCreature = (Creature) event.getSource();
	if(clickedCreature.getIsFilled()){
            playMusic("\\build\\classes\\coronawhacks\\Assets\\spray.wav");
            clickedCreature.kill();
            updateScore(clickedCreature);
        }
    }
}