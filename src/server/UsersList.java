package server;

import models.Player;

import java.util.ArrayList;
import java.util.List;

public class UsersList extends Thread{

    private GameServer gameServer;

    private List<String> onlineNames;
    private List<String> offlineNames;

    UsersList(GameServer gameServer){
        this.gameServer=gameServer;

        onlineNames=new ArrayList<>();
        offlineNames=new ArrayList<>();
        update();
    }

    public void update(){
        onlineNames.removeAll(onlineNames);
        List<ClientHandler> clients=gameServer.getClients();
        for (ClientHandler client:
            clients) {
            onlineNames.add(client.getUsename());
        }

        offlineNames.removeAll(offlineNames);
        offlineNames.addAll(Config.getClients());
        offlineNames.removeAll(onlineNames);
    }

    public List<String> getOffLinePlayers(){
        return offlineNames;
    }
    public List<String > getOnlinePLayers(){
        return onlineNames;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(3000);
                update();
                gameServer.sendOnlineList();
                gameServer.sendOfflineList();
            } catch (InterruptedException e) { e.printStackTrace(); }

        }
    }


}
