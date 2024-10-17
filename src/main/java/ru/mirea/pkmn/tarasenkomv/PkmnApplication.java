package ru.mirea.pkmn.tarasenkomv;

import ru.mirea.pkmn.*;
import java.io.IOException;

public class PkmnApplication {

    public static void main(String[] args) throws IOException {
        CardImport cardImport = new CardImport();
        CardExport cardExport = new CardExport();
        Card card = cardImport.importCard("C:/Users/g8987/IdeaProjects/Pkmn/src/main/resources/my_card.txt");
        System.out.println(card);

        //cardExport.exportCard(card);

            Card anothercard = cardImport.deserializeCard("C:/Users/g8987/IdeaProjects/Pkmn/src/main/resources/Morgrem.crd");
        System.out.println(anothercard);
    }

}
