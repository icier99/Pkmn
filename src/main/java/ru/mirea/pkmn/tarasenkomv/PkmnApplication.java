package ru.mirea.pkmn.tarasenkomv;

import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.pkmn.*;
import ru.mirea.pkmn.tarasenkomv.web.http.PkmnHttpClient;
import ru.mirea.pkmn.tarasenkomv.web.jdbc.DatabaseServiceImpl;

import java.io.IOException;
import java.sql.SQLException;


public class PkmnApplication {

    public static void main(String[] args) throws IOException, SQLException {
        CardImport cardImport = new CardImport();
        CardExport cardExport = new CardExport();
        Card card = cardImport.importCard("src/main/resources/my_card.txt");
        System.out.println(card);

        //cardExport.exportCard(card);

        Card anothercard = cardImport.deserializeCard("src/main/resources/Corvisquire.crd");
        System.out.println(anothercard);

        //PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
        //JsonNode card1 = pkmnHttpClient.getPokemonCard(card.getName(), card.getNumber());
        //System.out.println(card1.toPrettyString());

        DatabaseServiceImpl db = new DatabaseServiceImpl();
        //db.getAllStudents();
        db.getAllCards();
        //db.createPokemonOwner(card.getPokemonOwner());
        //System.out.println(db.getStudentFromDatabase(card.getPokemonOwner().getSurName()));
        //System.out.println(db.getCardFromDatabase("Impidimp"));

        //db.saveCardToDatabase(card);


        System.exit(0);
    }
}
