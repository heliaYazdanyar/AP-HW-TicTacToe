package server;

import client.Client;
import models.Player;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;

public class GameServer extends  Thread{

    private UsersList usersList;

    private ArrayList<ClientHandler> clients;
    private ArrayList<Player> allClients;
    private HashMap<String, Integer> authTokens;

    private boolean player1Waiting;
    private boolean gameHappening;
    private ClientHandler inGame1;
    private ClientHandler inGame2;

    private ServerSocket serverSocket;

    public GameServer(int serverPort) throws IOException {
        clients=new ArrayList<>();
        allClients=new ArrayList<>();

        authTokens=new HashMap<>();

        serverSocket=new ServerSocket(serverPort);
        usersList=new UsersList(this);
        usersList.start();

        player1Waiting=false;
        gameHappening=false;

        initAllClients();

        System.out.println("Server Started at: " + serverPort);
    }

    public void initAllClients(){
        allClients.removeAll(allClients);
        List<String> usernames=Config.getClients();
        for(String username:
                usernames){
            allClients.add(Player.getFromJson(username));
        }
    }

    public void sendOnlineList(){
        clients.removeIf(client -> !client.isAlive());

        List<String> onlines=new ArrayList<>();
        onlines.addAll(usersList.getOnlinePLayers());
        String message="onlines:";

        if(clients.size()>0) {
            for (int i = 0; i < onlines.size() - 1; i++) {
                message = message + onlines.get(i) + ",";
            }
            message = message + onlines.get(onlines.size() - 1);
        }

        for (ClientHandler client :
                clients) {
            client.send(message);

        }

    }
    public void sendOfflineList(){
        List<String> offlines=new ArrayList<>();
        offlines.addAll(usersList.getOffLinePlayers());
        String message="offlines:";

        for(int i=0;i<offlines.size()-1;i++){
            message=message+offlines.get(i)+",";
        }
        message=message+offlines.get(offlines.size()-1);

        for (ClientHandler client :
                clients) {
            client.send(message);

        }
    }

    public void acceptClient(ClientHandler client,String username){
        initAllClients();

        SecureRandom sr=new SecureRandom();
        int authToken=sr.nextInt();
        authTokens.put(username,authToken);
    }

    public int getToken(ClientHandler client){
        return authTokens.get(client.getUsename());
    }

    public List<ClientHandler> getClients(){
        return clients;
    }

    public void getGameRequest(ClientHandler client){
        if(gameHappening){
            if(!inGame1.isAlive() ||!inGame2.isAlive()) gameHappening=false;
        }
        if(!gameHappening) {
            if (player1Waiting) {
                if (client != inGame1) {
                    inGame2 = client;
                    givingSigns();
                    gameHappening=true;
                }
            } else {
                player1Waiting = true;
                inGame1 = client;
            }
        }
    }

    public void givingSigns(){
        Random r=new Random();
        if(r.nextInt(2)==0) {
            inGame1.send("startingXO");
        }
        else{
            inGame2.send("startingXO");
        }
    }

    public void startGame(){
        //set first player
        Random r=new Random();
        if(r.nextInt(2)==0){
            inGame1.send("yourTurn");
            inGame2.send("notYourTurn");
        }
        else{
            inGame2.send("yourTurn");
            inGame1.send("notYourTurn");
        }

        inGame2.send("gameStarted");
        inGame1.send("gameStarted");
        gameHappening=true;
    }

    public void setTurns(ClientHandler notHisTurn){
        if(notHisTurn==inGame1){
            inGame2.send("yourTurn");
            inGame1.send("notYourTurn");
        }
        else{
            inGame1.send("yourTurn");
            inGame2.send("notYourTurn");
        }

    }

    public void sendToOpponent(ClientHandler from,String msg){
        if(from == inGame1){
            inGame2.send(msg);
        }
        else{
            inGame1.send(msg);
        }
    }

    public void weHaveWinner(ClientHandler client){
        if(client==inGame1){
            inGame2.send("youLost");
            Config.saveScore(-1,inGame2.getUsename());
            inGame1.send("youWon");
            Config.saveScore(1,inGame1.getUsename());

        }
        else{
            inGame1.send("youLost");
            Config.saveScore(-1,inGame1.getUsename());
            inGame2.send("youWon");
            Config.saveScore(1,inGame2.getUsename());
        }
        player1Waiting=false;
        gameHappening=false;
        inGame2=null;
        inGame1=null;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(this, socket);
                clients.add(clientHandler);
                System.out.println("Client at: " + socket.getRemoteSocketAddress().toString() + " is connected.");

                clientHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
