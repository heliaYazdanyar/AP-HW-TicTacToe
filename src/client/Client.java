package client;

import swing.Board;
import swing.MainFrame;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class Client extends  Thread{

    private Socket socket;
    private int serverPort;
    private String serverIP="localhost";

    private String username;
    private String password;

    public int authToken;

    private MainFrame mainFrame;

    private boolean inGame;
    public boolean login;
    private Board board;
    private int[][] currentBoard;
    private boolean isMyTurn;
    private char sign;

    private List<String> onlineNames;
    private List<String> offlineNames;

    private GameTransmitter transmitter;

    InputStream socketInputStream ;
    PrintStream socketPrinter;

    Client(String serverIP,int serverPort) throws IOException {
        this.serverIP=serverIP;
        this.serverPort=serverPort;

        socket = new Socket(serverIP, serverPort);
        System.out.println("Connected to Server at: " + serverIP + ":" + serverPort);

        this.socketInputStream = socket.getInputStream();
        this.socketPrinter = new PrintStream(socket.getOutputStream());
        onlineNames=new ArrayList<>();
        offlineNames=new ArrayList<>();

        inGame=false;
        login=false;
    }

    public void login(String username,String password) {
        this.username=username;
        this.password=password;
        login=true;

        socketPrinter.println("username:"+username);

        socketPrinter.println("login");
    }

    public void setOnlineNames(String message){
        onlineNames.removeAll(onlineNames);
        String[] result=message.substring(8).split(",");
        for(int i=0;i<result.length;i++){
            onlineNames.add(result[i]);
        }
    }
    public void setOfflineNames(String message){
        offlineNames.removeAll(offlineNames);
        String[] result=message.substring(9).split(",");
        for(int i=0;i<result.length;i++){
            offlineNames.add(result[i]);
        }
    }

    public List<String> getOnlineNames(){
        return onlineNames;
    }
    public List<String> getOfflineNames(){
        return offlineNames;
    }

    public void takeToken(int authToken){
        this.authToken=authToken;
    }
    public int getAuthToken(){
        return authToken;
    }

    public void requestForGame(){
        socketPrinter.println(getAuthToken()+"$"+"startGame");
    }

    public void chooseSign(){
        mainFrame.setCurrentPanel("chooseSign");
    }

    public void setSign(char c,boolean chooser){
        this.sign=c;
        if(chooser) socketPrinter.println(getAuthToken()+"$"+"signChosen-"+c);
        else socketPrinter.println(getAuthToken()+"$"+"startAll");
    }
    public char getPlayersSign(){
        return this.sign;
    }

    public void startGame() {
        this.inGame=true;
        board=new Board(this,mainFrame);
        mainFrame.setCurrentPanel("play");
    }

    public  void setTurn(boolean turn){
        this.isMyTurn=turn;
    }
    public boolean isMyTurn(){
        return isMyTurn;
    }

    public Board getBoard(){
        return board;
    }

    public void sendNewBoardToServer(int[][] newBoard){
        if(ifIWon(newBoard)){
            socketPrinter.println(getAuthToken()+"$"+"winner");
        }else {
            String result = "board:";
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    result = result + newBoard[i][j] + ",";
                }
                result = result + newBoard[i][6] + ";";
            }
            socketPrinter.println(getAuthToken()+"$"+result);
        }
    }
    public void getNewBoardFromServer(String boardString){
        this.isMyTurn=true;
        String[][] currentBoard=new String[7][7];
        boardString=boardString.substring(6);
        String[] lines=boardString.split(";");
        for(int i=0;i<7;i++){
            currentBoard[i]=lines[i].split(",");
        }

        this.currentBoard=new int[7][7];
        for(int i=0;i<7;i++){
            for(int j=0;j<7;j++){
                this.currentBoard[i][j]=Integer.parseInt(currentBoard[i][j]);
            }
        }

        refreshBoard(true);

    }

    public void endTurn(int[][] currentBoard){
        //send current board to server
        sendNewBoardToServer(currentBoard);
    }
    public void refreshBoard(boolean turn){
        board.update(this.currentBoard,turn);
    }

    public boolean ifIWon(int[][] theBoard){
        if(sign=='x') {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    if(theBoard[i][j]==1){
                        //ofoghi
                        if(i<4)
                        for(int cnt=1;cnt<4;cnt++){
                            if(theBoard[i+cnt][j]==1){
                                if(cnt==3){
                                    return true;
                                }
                            }
                            else break;
                        }
                        //amodi
                        if(j<4)
                            for(int cnt=1;cnt<4;cnt++){
                                if(theBoard[i][j+cnt]==1){
                                    if(cnt==3){
                                        return true;
                                    }
                                }
                                else break;
                            }
                        //orib 1
                        if(i<4 && j<4)

                        for(int cnt=1;cnt<4;cnt++){
                            if(theBoard[i+cnt][j+cnt]==1){
                                if(cnt==3){
                                    return true;
                                }
                            }
                            else break;
                        }
                        //orib 2
                        if(i>2 && j<4 )
                            for(int cnt=1;cnt<4;cnt++){
                                if(theBoard[i-cnt][j+cnt]==1){
                                    if(cnt==3){
                                        return true;
                                    }
                                }
                                else break;
                            }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    if(theBoard[i][j]==3){
                        //ofoghi
                        if(i<4)
                            for(int cnt=1;cnt<4;cnt++){
                                if(theBoard[i+cnt][j]==3){
                                    if(cnt==3){
                                        return true;
                                    }
                                }
                                else break;
                            }
                        //amodi
                        if(j<4)
                            for(int cnt=1;cnt<4;cnt++){
                                if(theBoard[i][j+cnt]==3){
                                    if(cnt==3){
                                        return true;
                                    }
                                }
                                else break;
                            }
                        //orib 1
                        if(i<4 && j<4)
                            for(int cnt=1;cnt<4;cnt++){
                                if(theBoard[i+cnt][j+cnt]==3){
                                    if(cnt==3){
                                        return true;
                                    }
                                }
                                else break;
                            }
                        //orib 2

                        if(i>2 && j<4 )
                            for(int cnt=1;cnt<4;cnt++){
                                if(theBoard[i-cnt][j+cnt]==3){
                                    if(cnt==3){
                                        return true;
                                    }
                                }
                                else break;
                            }

                    }
                }
            }

        }
        return false;
    }

    public void endGame(boolean winner,boolean loser,boolean equal){
        if(winner){
            board.setGameState("winner");
            refreshBoard(false);
        }
        else if(loser) board.setGameState("loser");
        else if(equal) board.setGameState("equal");
    }

    @Override
    public void run() {
        mainFrame=new MainFrame(this);
        mainFrame.setVisible(true);

        transmitter=new GameTransmitter(socketInputStream,socketPrinter,this);

        transmitter.start();

        while (isStillAlive()) {
            try {
                Thread.sleep(100);

            } catch (InterruptedException ignore) { }
        }

        transmitter.interrupt();
    }

    public boolean isStillAlive(){
        return (socket.isConnected() && transmitter.isAlive());
    }

    public String getUsername(){
        return username;
    }



}
