package view;

/**
 * Created by zhuhan on 14/12/18.
 */
public class PlagueWorld {
    public static void main(String[] args){
        PlagueWorldGUI gui = new PlagueWorldGUI();
        PlagueWorld plagueWorld = new PlagueWorld();
        gui.init(plagueWorld);
    }

    public String infectSource;
    public int stepTime;

    public void loadDatabase(){

    }

    public void infectCountry(String country){
        this.infectSource = country;
    }

    public void go(int time){
        this.stepTime = time;
    }
}
