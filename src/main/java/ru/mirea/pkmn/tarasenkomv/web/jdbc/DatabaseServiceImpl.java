package ru.mirea.pkmn.tarasenkomv.web.jdbc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.mirea.pkmn.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;


public class DatabaseServiceImpl implements DatabaseService {
    private final Connection connection;
    private final Properties databaseProperties;
    public DatabaseServiceImpl() throws SQLException, IOException {
        databaseProperties = new Properties();
        databaseProperties.load(new FileInputStream("src/main/resources/database.properties"));


        connection = DriverManager.getConnection(
                databaseProperties.getProperty("database.url"),
                databaseProperties.getProperty("database.user"),
                databaseProperties.getProperty("database.password")
        );
        System.out.println("Connection is "+(connection.isValid(0) ? "up" : "down"));
    }

    @Override
    public Card getCardFromDatabase(String cardName) throws SQLException, JsonProcessingException {
        Card card = null;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM card WHERE \"name\" = '" + cardName + "' AND pokemon_owner IS NOT NULL;");

        if (!resultSet.next()) {
            resultSet = statement.executeQuery("SELECT * FROM card WHERE \"id\" = '" + cardName + "';");
        }

        if (resultSet.next()) {

            PokemonStage pokemonStage = null;
            if (!resultSet.getString("stage").equals("null")) {
                pokemonStage = PokemonStage.valueOf(resultSet.getString("stage")); }

            String name = resultSet.getString("name");

            int hp = Integer.parseInt(resultSet.getString("hp"));

            EnergyType pokemonType = null;
            System.out.println(resultSet.getString("evolves_from"));
            if (!resultSet.getString("pokemon_type").equals("null")) {
                pokemonType = EnergyType.valueOf(resultSet.getString("pokemon_type")); }

            Card evolvesFrom = null;
            if (resultSet.getString("evolves_from") != null && !resultSet.getString("evolves_from").equals("null")) {
                evolvesFrom = getCardFromDatabase(resultSet.getString("evolves_from")); }

            String attack_skill = resultSet.getString("attack_skills");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(attack_skill);
            List<AttackSkill> skills = new ArrayList<>();
            for (JsonNode attackNode : jsonNode) {
                AttackSkill attack = new AttackSkill(
                        attackNode.path("name").asText(),
                        attackNode.path("cost").asText(),
                        attackNode.path("description").asText(),
                        attackNode.path("damage").asInt()
                );
                skills.add(attack);
            }

            EnergyType weaknessType = null;
            if (!resultSet.getString("weakness_type").equals("null")) {
                weaknessType = EnergyType.valueOf(resultSet.getString("weakness_type")); }

            EnergyType resistanceType = null;
            if (!resultSet.getString("resistance_type").equals("null")) {
                resistanceType = EnergyType.valueOf(resultSet.getString("resistance_type")); }

            String retreatCost = resultSet.getString("retreat_cost");

            String gameSet = resultSet.getString("game_set");

            char regulationMark = resultSet.getString("regulation_mark").charAt(0);

            String number = resultSet.getString("card_number");

            Student pokemonOwner = null;
            if (resultSet.getString("pokemon_owner") != null && !resultSet.getString("pokemon_owner").equals("null")) {
            pokemonOwner = getStudentFromDatabaseId(resultSet.getString("pokemon_owner")); }

            return new Card(pokemonStage, name, hp, pokemonType, evolvesFrom, skills,
                    weaknessType, resistanceType, retreatCost, gameSet, regulationMark, pokemonOwner, number);
        }
        statement.close();
        return card;
    }

    public Student getStudentFromDatabaseId(String id) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM student WHERE \"id\" = '" + id + "';");
        Student student = null;
        if (resultSet.next()) {
            String surName = resultSet.getString("familyName");
            String firstName = resultSet.getString("firstName");
            String familyName = resultSet.getString("patronicName");
            String group = resultSet.getString("group");

            student = new Student(surName, firstName, familyName, group);
        }
        statement.close();
        return student;
    }

    public void getAllStudents() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM student;");
        ResultSetMetaData res = resultSet.getMetaData();
        while (resultSet.next()) {
            for (int i = 1; i <= res.getColumnCount(); i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + res.getColumnName(i));
            }
            System.out.println("");
        }
        statement.close();
    }

    public void getAllCards() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM card;");
        ResultSetMetaData res = resultSet.getMetaData();
        while (resultSet.next()) {
            for (int i = 1; i <= res.getColumnCount(); i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(columnValue + " " + res.getColumnName(i));
            }
            System.out.println("");
        }
        statement.close();
    }

    @Override
    public Student getStudentFromDatabase(String studentFullName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM student WHERE \"familyName\" = '" + studentFullName + "';");
        Student student = null;
        if (resultSet.next()) {
            String surName = resultSet.getString("familyName");
            String firstName = resultSet.getString("firstName");
            String familyName = resultSet.getString("patronicName");
            String group = resultSet.getString("group");

            student = new Student(surName, firstName, familyName, group);
        }
        statement.close();
        return student;
    }

    @Override
    public void saveCardToDatabase(Card card) throws SQLException {
        Statement statement = connection.createStatement();
        Gson gson = new GsonBuilder().create();
        try {
            statement.execute("INSERT INTO card VALUES(gen_random_uuid(), '" +
                    card.getName() + "', '" +
                    card.getHp() + "', " +
                    "(SELECT id FROM card WHERE \"name\" = '" + card.getEvolvesFrom().getName() + "'  limit 1), '" +
                    card.getGameSet() + "', " +
                    "(SELECT id FROM student WHERE \"familyName\" = '" + card.getPokemonOwner().getSurName() + "'  limit 1), '" +
                    card.getPokemonStage() + "', '" +
                    card.getRetreatCost() + "', '" +
                    card.getWeaknessType() + "', '" +
                    card.getResistanceType() + "', '" +
                    gson.toJson(card.getSkills()) + "', '" +
                    card.getPokemonType() + "', '" +
                    card.getRegulationMark() + "', '" +
                    card.getNumber() + "');");
        } catch (Exception e) {
            statement.execute("INSERT INTO card(id, name, hp, game_set, stage, retreat_cost, " +
                    "weakness_type, resistance_type, attack_skills, pokemon_type, regulation_mark,  card_number)" +
                            " VALUES(gen_random_uuid(), '" +
                    card.getName() + "', '" +
                    card.getHp() + "', '" +
                    card.getGameSet() + "', '" +
                    card.getPokemonStage() + "', '" +
                    card.getRetreatCost() + "', '" +
                    card.getWeaknessType() + "', '" +
                    card.getResistanceType() + "', '" +
                    gson.toJson(card.getSkills()) + "', '" +
                    card.getPokemonType() + "', '" +
                    card.getRegulationMark() + "', '" +
                    card.getNumber() + "');");
        }
        statement.close();
    }

    @Override
    public void createPokemonOwner(Student owner) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO student VALUES(gen_random_uuid(), '" +
                owner.getSurName() + "', '" +
                owner.getFirstName() + "', '" +
                owner.getFamilyName() + "', '" +
                owner.getGroup() + "');");
        statement.close();
    }
}
