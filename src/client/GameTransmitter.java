package client;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class GameTransmitter extends Thread{

    private  InputStream inputStream;
    private PrintStream printStream;

    private Client client;


    GameTransmitter(InputStream inputStream, PrintStream printStream,Client client){
        this.printStream=printStream;
        this.inputStream=inputStream;

        this.client=client;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(inputStream);
        while (!isInterrupted()){
            String message=scanner.next();

            if(message.equalsIgnoreCase("startingXO")){
                client.chooseSign();
            }
            else if(message.startsWith("authToken:")){
                client.takeToken(Integer.parseInt(message.substring(10)));
            }
            else if(message.equalsIgnoreCase("YourSignIsX")){
                client.setSign('x',false);
            }
            else if(message.equalsIgnoreCase("YourSignIsO")){
                client.setSign('o',false);
            }
            else if(message.equalsIgnoreCase("gameStarted")){
                client.startGame();
            }
            else if(message.equalsIgnoreCase("yourTurn")){
                client.setTurn(true);
            }
            else if(message.equalsIgnoreCase("notYourTurn")){
                client.setTurn(false);
            }
            else if(message.startsWith("board:")){
                client.getNewBoardFromServer(message);
            }
            else if(message.equalsIgnoreCase("youLost")){
                client.endGame(false,true,false);
            }
            else if(message.equalsIgnoreCase("youWon")){
                client.endGame(true,false,false);
            }
            else if(message.startsWith("onlines:")){
                client.setOnlineNames(message);
            }
            else if(message.startsWith("offlines:")){
                client.setOfflineNames(message);
            }


        }
    }


}
