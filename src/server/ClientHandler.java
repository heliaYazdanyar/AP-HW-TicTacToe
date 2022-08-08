package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;

public class ClientHandler extends Thread{


    private int authToken;

    private Socket socket;
    private GameServer gameServer;
    private String clientName;
    private PrintStream printer;

    private boolean login;

    public ClientHandler(GameServer gameServer, Socket socket) {
        this.socket=socket;
        this.gameServer = gameServer;

        login=false;
    }

    public String getUsename(){
        return this.clientName;
    }
    public void setUsername(String username){
        this.clientName=username;
    }


    private void setAuthToken(int authToken){
        this.authToken=authToken;
    }
    private int getAuthToken(){
        return authToken;
    }
    private boolean checkToken(String st){
        if(Integer.parseInt(st)==authToken) return true;
        else return false;
    }

    public void send(String message){
        printer.println(message);
    }

    @Override
    public void run() {
        try {
            Scanner scanner = new Scanner(socket.getInputStream());
            printer = new PrintStream(socket.getOutputStream());
            while (true){
                String msg=scanner.nextLine();

                if(msg.equalsIgnoreCase("login")) {
                    gameServer.acceptClient(this,this.getUsename());
                    setAuthToken(gameServer.getToken(this));
                    send("authToken:"+gameServer.getToken(this));
                }

                else if(msg.contains("username:")){
                    setUsername(msg.substring(9));
                }

                else if(msg.contains("startGame")) {
                    int index=msg.indexOf("$");
                    String tokenMessage=msg.substring(0,index);
                    if(checkToken(tokenMessage))
                    gameServer.getGameRequest(this);
                }

                else if(msg.contains("signChosen")){
                    int index=msg.indexOf("$");
                    String tokenMessage=msg.substring(0,index);
                    if(checkToken(tokenMessage)) {
                        if (msg.contains("x")) gameServer.sendToOpponent(this, "YourSignIsO");
                        else gameServer.sendToOpponent(this, "YourSignIsX");
                    }
                }

                else if(msg.contains("startAll")){
                    int index=msg.indexOf("$");
                    String tokenMessage=msg.substring(0,index);
                    if(checkToken(tokenMessage))
                    gameServer.startGame();

                }

                else if(msg.contains("board:")){
                    int index=msg.indexOf("$");
                    String tokenMessage=msg.substring(0,index);
                    if(checkToken(tokenMessage)) {
                        gameServer.setTurns(this);
                        gameServer.sendToOpponent(this, msg.substring(index+1));
                    }
                }

                else if(msg.contains("winner")){
                    int index=msg.indexOf("$");
                    String tokenMessage=msg.substring(0,index);
                    if(checkToken(tokenMessage))
                    gameServer.weHaveWinner(this);
                }


            }

        } catch (IOException e) { e.printStackTrace(); }
    }


}
