package server;

import models.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class Config {

    public static boolean signIn(String username,String password){
        List<String> users=readTxtFile("users");
        boolean possible=true;
        for (String name:
                users) {
            int m=0;
            for(int i=0;i<name.length();i++){
                if(name.charAt(i)==' ') {
                    m=i;
                    break;
                }
            }
            if(name.substring(0,m).equals(username)){
                possible=false;
                break;
            }
        }
        if(possible){
            List<String> text=new ArrayList<>();
            text.add(username+" "+password);
            //save name
            writeInTxtFile("users",text);

            try {
                Path p=Paths.get(System.getProperty("user.dir")+File.separator+
                        "resources"+File.separator+"users"+File.separator+username+".txt");
                Files.createFile(p);

                List<String>  list=new ArrayList<>();
                list.add("{'username':'"+username+"','wins':"+0+",'loses':"+0+",'score':"+0+"}");
                Files.write(p,list);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return possible;
    }
    public static boolean login(String username,String password){
        List<String> users=readTxtFile("users");
        for (String st:
                users) {
            if(st.equals(username+" "+password)){
                return true;
            }
        }
        return false;
    }

    public static void saveScore(int newGameScore,String username){
        Player player=Player.getFromJson(username);

        if(newGameScore==1){
            player.plusWins();
            player.setScore();
        }
        else{
            player.plusLoses();
            player.setScore();
        }

        String jsonResult="{'username':'"+player.getUsername()+"','wins':"+player.getWins()+",'loses':"+
                player.getLoses()+",'score':"+player.getScore()+"}";
        List<String> list=new ArrayList<>();
        list.add(jsonResult);

        try{
            String directory=System.getProperty("user.dir");
            Path p= Paths.get(directory+File.separator+"resources"+File.separator+"users"+File.separator+player.getUsername()+".txt");
            Files.write(p,list);
        }catch(IOException w){ w.printStackTrace(); }

    }


    public static int getPort(){
        List<String> list;
        try{
            String directory=System.getProperty("user.dir");
            Path p= Paths.get(directory+ File.separator+"resources"+File.separator+"port.txt");
            list= Files.readAllLines(p);
        }catch(IOException w){
            return 8000;
        }
        return Integer.parseInt(list.get(0));

    }

    public static List<String> getClients(){
        List<String> list=readTxtFile("users");
        List<String> result=new ArrayList<>();
        for (String name:
                list) {
            int index=name.indexOf(' ');
            result.add(name.substring(0,index));
        }
        return result;
    }

    private static List<String> readTxtFile(String filePath){
        List<String> result=new ArrayList<>();
        try{
            String directory=System.getProperty("user.dir");
            Path p= Paths.get(directory+ File.separator+"resources"+File.separator+filePath+".txt");
            result= Files.readAllLines(p);
        }catch(IOException w){ w.printStackTrace(); }
        return result;
    }

    private static void writeInTxtFile(String fileName,List<String> text){
        try{
            String directory=System.getProperty("user.dir");
            Path p= Paths.get(directory+File.separator+"resources"+File.separator+fileName+".txt");
            Files.write(p,text, StandardOpenOption.APPEND);
        }catch(IOException w){ w.printStackTrace(); }
    }
}
