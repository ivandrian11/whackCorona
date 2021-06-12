package coronawhacks;

import javax.swing.JButton;

public abstract class Virus extends JButton {
    protected String[] arr = {"black", "red", "green", "blue"}; // kumpulan virus
    protected String color; // warna virus terpilih
    
    public abstract void kill();
    public abstract void revive();
    public abstract void update();
}