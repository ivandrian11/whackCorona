package coronawhacks;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

public class Creature extends Virus {
    private int width; // lebar kotak
    private final int MIN_LIFE = 10; // min lama hidup
    private final int MAX_LIFE = 50;  // max lama hidup
    Random rand = new Random(); // objek random
    private boolean isFilled; // kondisi kotak terisi
    private int lifeCount; // lamanya kemunculan kotak
    private int finalLife; //stores how long the creature will be alive for this life
     
    public Creature() {
	isFilled = false; 
	this.setIcon(loadImage("Assets/frame.png"));
	lifeCount = 0;
    }
    
    public ImageIcon loadImage(String path){
        // fungsi untuk load gambar
        Image image = new ImageIcon(this.getClass().getResource(path)).getImage();
        if(PlayGame.getLevel() == 1)
            width = 227;
        else if(PlayGame.getLevel() == 2)
            width = 150;
        else if(PlayGame.getLevel() == 3)
            width = 111;
        else
            width = 88;
        Image scaledImage = image.getScaledInstance(width, 110, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
    
    public boolean getIsFilled() {
	return isFilled; // mengecek apakah terisi
    }

    public void revive() {
        finalLife = MIN_LIFE + rand.nextInt(MAX_LIFE - MIN_LIFE + 1); // lama kemunculan
        isFilled = true; // kotak terisi
        color = arr[rand.nextInt(4)]; // mengacak warna virus terpilih
        if(color.equals("black"))
            this.setIcon(loadImage("Assets/black.png"));
        else if(color.equals("red"))
            this.setIcon(loadImage("Assets/red.png"));
        else if(color.equals("green"))
            this.setIcon(loadImage("Assets/green.png"));
        else
            this.setIcon(loadImage("Assets/blue.png"));
    }

    public void kill() {
        // fungsi untuk menghilangkan kotak yang terisi virus
	isFilled = false;
	this.setIcon(loadImage("Assets/frame.png"));
	lifeCount = 0; 
	PlayGame.creaturesFilled--;
    }

    public void update() {
        // fungsi untuk update durasi hidup kotak
	if(isFilled){
            lifeCount++; 
            // kondisi jika telah mencapai durasi lama kemunculan
            if(lifeCount == finalLife) 
		this.kill(); // basmi
        }
    }
}