package ru.mirea.pkmn.tarasenkomv;


import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.pkmn.*;
import ru.mirea.pkmn.tarasenkomv.web.http.PkmnHttpClient;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.module.ModuleReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Set;
import java.util.stream.Collectors;

public class CardImport {

    public Card importCard(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            PokemonStage pokemonStage = PokemonStage.valueOf(br.readLine().toUpperCase());

            String name = br.readLine();

            int hp = Integer.parseInt(br.readLine());

            EnergyType pokemonType = EnergyType.valueOf(br.readLine().toUpperCase());

            String evolvesFromName = br.readLine();
            Card evolvesFrom;
            if (evolvesFromName.equals("-")) {
                evolvesFrom = null;
            }
            else {
                evolvesFrom = importCard(evolvesFromName);
            }

            List<AttackSkill> skills = new ArrayList<>();
            line = br.readLine();
            if (line.contains(",")) {
                String[] attackParts = line.split(",");
                for (String attack : attackParts) {
                    String[] parts = attack.split("/");
                    AttackSkill skill = new AttackSkill(parts[0], parts[1], null, Integer.parseInt(parts[2]));
                    skills.add(skill);
                }
            }
            else {
                String[] parts = line.split("/");
                AttackSkill skill = new AttackSkill(parts[0], parts[1], null, Integer.parseInt(parts[2]));
                skills.add(skill);
            }




            EnergyType weaknessType = EnergyType.valueOf(br.readLine().toUpperCase());

            EnergyType resistanceType;

            line = br.readLine();
            if (line.equals("-")) {
                resistanceType = null;
            }
            else {
                resistanceType = EnergyType.valueOf(line.toUpperCase());
            }

            String retreatCost = br.readLine();

            String gameSet = br.readLine();

            char regulationMark = br.readLine().charAt(0);

            String pokemonOwner_1 = br.readLine();

            Student pokemonOwner;
            if (pokemonOwner_1.equals("-")) {
                pokemonOwner = null;
            }
            else {
                String[] ownerParts = pokemonOwner_1.split("/");
                pokemonOwner = new Student(ownerParts[0], ownerParts[1], ownerParts[2], ownerParts[3]);
            }
            String number = br.readLine();

            PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
            JsonNode card1 = pkmnHttpClient.getPokemonCard(name, number);

            JsonNode attacksArray = card1.path("data").get(0).path("attacks");
            int i = 0;
            for (AttackSkill attackSkill : skills) {
                attackSkill.setDescription(attacksArray.get(i).path("text").asText());
                i++;
                i = i % attacksArray.size();
            }
            return new Card(pokemonStage, name, hp, pokemonType, evolvesFrom, skills,
                    weaknessType, resistanceType, retreatCost, gameSet, regulationMark, pokemonOwner, number);


        }
    }

    public Card deserializeCard(String filePath) {
        Card card = null;
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            card = (Card) objectInputStream.readObject();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return card;
    }
}
