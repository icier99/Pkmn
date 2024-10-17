package ru.mirea.pkmn.tarasenkomv;

import ru.mirea.pkmn.Card;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CardExport {
    public void exportCard(Card card) throws IOException {
        String fileName = card.getName() + ".crd";
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(card);
        }
    }
}
