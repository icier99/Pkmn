package ru.mirea.pkmn.tarasenkomv;

import ru.mirea.pkmn.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class CardImport {

    public Card importCard(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            PokemonStage pokemonStage = PokemonStage.valueOf(br.readLine().toUpperCase());
            String name = br.readLine();
            Integer hp = Integer.parseInt(br.readLine());
            EnergyType pokemonType = EnergyType.valueOf(br.readLine().toUpperCase());
            String evolvesFromName = br.readLine();
            Card evolvesForm;
            if (evolvesFromName.equals("-")) {
                evolvesForm = null;
            }
            else {
                evolvesForm = importCard(evolvesFromName);;
            }

            List<AttackSkill> skills = new ArrayList<>();
            line = br.readLine();
            if (line.contains(",")) {
                String[] attackParts = line.split(",");
                for (String attack : attackParts) {
                    String[] parts = attack.split("/");
                    AttackSkill skill = new AttackSkill(parts[0], parts[1], Integer.parseInt(parts[2]));
                    skills.add(skill);
                }
            } else {
                String[] parts = line.split("/");
                AttackSkill skill = new AttackSkill(parts[0], parts[1], Integer.parseInt(parts[2]));
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
            Character regulationMark = br.readLine().charAt(0);
            String pokemonOwner_1 = br.readLine();
            Student pokemonOwner;
            if (pokemonOwner_1.equals("-")) {
                pokemonOwner = null;
            }
            else {
                String[] ownerParts = pokemonOwner_1.split("/");
                pokemonOwner = new Student(ownerParts[0], ownerParts[1], ownerParts[2], ownerParts[3]);
            }
            return new Card(pokemonStage, name, hp, pokemonType, evolvesForm, skills,
                    weaknessType, resistanceType, retreatCost, gameSet, regulationMark, pokemonOwner);
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