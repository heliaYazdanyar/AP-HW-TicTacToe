package models;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Player {
    String username;
    int wins;
    int loses;
    int score;

    public String getUsername(){
        return username;
    }
    public int getWins(){
        return wins;
    }
    public int getLoses(){
        return loses;
    }
    public  int getScore(){
        return score;
    }


    public void plusWins(){
        wins++;
    }
    public void plusLoses(){
        loses++;
    }

    public void setScore() {
        this.score = wins-loses;
    }

    public static Player getFromJson(String username){
        String json = "";
        try {
            List<String> list= Files.readAllLines(Paths.get(System.getProperty("user.dir")+ File.separator+
                    "resources"+File.separator+"users"+File.separator+username+".txt"));
            json=list.get(0);
        }catch (IOException e){
            e.printStackTrace();
        }

        Gson gson=new Gson();
        Player player= gson.fromJson(json, Player.class);
        return player;
    }
}
