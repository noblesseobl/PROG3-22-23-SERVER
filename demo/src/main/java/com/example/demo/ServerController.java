package com.example.demo;


import javax.json.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;




public class ServerController implements Initializable {
    @FXML
    private ListView listLog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //creerai un thread pool
        try{
            ServerSocket s= new ServerSocket(4445);
            System.out.println("ciao");
            while(true) {
                System.out.println("ciao");
                Socket socket = s.accept();
                System.out.println("Accettato socket " + socket);
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());//prendo l'input dal socket (la mail e la action)
                ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());//prendo l'output del socket

                try {



                    String[] user = "tizio@gmail.com".split("@");
                    ArrayList<Email> casella = leggiCasella(user[0]);
                    outStream.writeObject(casella);
                    outStream.flush();
                } finally {
                    socket.close();
                    outStream.close();
                    inStream.close();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private ArrayList<Email> leggiCasella(String email) throws IOException {
        ArrayList<Email> casella= new ArrayList<>();

        File file = new File( "");
        int ll= (int) file.length();
        if(ll!=0){
            InputStream fis = new FileInputStream(file);
            JsonReader jsonReader=Json.createReader(fis);
            JsonArray jsonArray= jsonReader.readArray();

            fis.close();

            for(int i=0; i<jsonArray.size(); i++){
                JsonObject mail=jsonArray.getJsonObject(i);
                JsonArray destArray= mail.getJsonArray("destinatari");
                List<String> dests=new ArrayList<>();
                for (JsonValue s : destArray) {//scompongo l'array json di destinatari
                    dests.add(s.toString());
                }
                Email e = new Email(mail.getString("mittente"), dests, mail.getString("oggetto"), mail.getString("testo"),
                        mail.getString("data"));
                e.setId(mail.getInt("id"));
                casella.add(e);
            }
            jsonReader.close();
        }


        return casella;


    }

    private void scriviCasella(String email, Email lettera){

    }

    //nella initialize crea un thread pool, in cui esegui il server socket/sochet accept while e sticazzi
    //crea un metodo per scrivere in una cassella
    //crea un metodo per leggere tutte le mail da una cassella


}