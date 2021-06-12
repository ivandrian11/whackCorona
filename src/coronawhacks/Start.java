package coronawhacks;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Start extends JFrame implements Music {
    private JButton ScoreButton = new JButton(); 
    private JLabel bg = new JLabel();
    private JButton playButton = new JButton();
    private JButton quitButton = new JButton();
    public static boolean play;
    public static Clip clip;
    
    public Start() {
        initComponents();
        setSize(505, 525);
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        play = false;
    }

    public void playMusic(String location){
        try {
            File musicPath = new File(System.getProperty("user.dir")+location);
            if(musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.out.println("Cannot fint the Audio File");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void quitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        System.exit(0);
    }

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {                                           
        this.dispose();
        play = true;
    }
    
    private void ScoreButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        new HighScore();
        this.dispose();
    }
    
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(null);
        getContentPane().setLayout(null);

        quitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("Assets/quitButton.png"))); // NOI18N
        quitButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        quitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitButtonActionPerformed(evt);
            }
        });
        getContentPane().add(quitButton);
        quitButton.setBounds(170, 360, 190, 70);

        playButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("Assets/playButton.png"))); // NOI18N
        playButton.setBorderPainted(false);
        playButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });
        getContentPane().add(playButton);
        playButton.setBounds(170, 160, 190, 70);

        ScoreButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("Assets/scoreButton.png"))); // NOI18N
        ScoreButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ScoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ScoreButtonActionPerformed(evt);
            }
        });
        getContentPane().add(ScoreButton);
        ScoreButton.setBounds(170, 260, 190, 70);

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("Assets/bg-menu.png"))); // NOI18N
        getContentPane().add(bg);
        bg.setBounds(0, 0, 500, 500);
    }
    
    public static void main(String[] args){
        new Start().playMusic("\\build\\classes\\coronawhacks\\Assets\\Carnival-Games.wav");
        while (play != true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }
        PlayGame.startGame();
    }
}