package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class InitialSetup {
    private static final String URL = "jdbc:mysql://localhost:3306?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        initializeTables();

    }

    private static void initializeTables() {
        String createBaseQuery = "CREATE DATABASE IF NOT EXISTS minions_db;";
        String useQuery = "USE minions_db;";
        String createTownsTable = "CREATE TABLE IF NOT EXISTS towns(" +
                "town_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(50), " +
                "country VARCHAR(50)" +
                ");";
        String createMinionsTable = "CREATE TABLE IF NOT EXISTS minions(" +
                "minion_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(50), " +
                "age INT, " +
                "town_id INT," +
                "CONSTRAINT `minion_town` FOREIGN KEY (town_id) REFERENCES towns(town_id)" +
                ");";
        String createVillainsTable = "CREATE TABLE IF NOT EXISTS villains(" +
                "villain_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(50), " +
                "evilness VARCHAR(10)" +
                ");";
        String createTableVillainsMinions = "CREATE TABLE IF NOT EXISTS villains_minions(" +
                "villain_id INT, " +
                "minion_id INT, " +
                "CONSTRAINT `pk_villain_minion` PRIMARY KEY(villain_id, minion_id)," +
                "CONSTRAINT `fk_villain_minion` FOREIGN KEY(villain_id) REFERENCES villains(villain_id), " +
                "CONSTRAINT `fk_minion_villain` FOREIGN KEY(minion_id) REFERENCES minions(minion_id)" +
                ");";

        String insertIntoTowns = "INSERT INTO towns(name, country) " +
                "VALUES ('Sofia','Bulgaria'), ('Las Vegas','USA'), ('Moscow','Russia'), ('London','England'), ('Draginovo','Bulgaria');";
        String insertIntoMinions = "INSERT INTO minions(name, age, town_id) " +
                "VALUES ('Pesho', 7, 1), ('Gosho', 10, 2), ('Tosho', 7, 3), ('Fosho', 13, 5), ('Losho', 141, 5);";
        String insertIntoVillains = "INSERT INTO villains(name, evilness) " +
                "VALUES ('Mojojojo', 'evil'), ('Kiro', 'super evil'), ('Bashta ti', 'good'), ('Grudge', 'evil'), ('Ali', 'bad');";
        String insertIntoMapper = "INSERT INTO villains_minions(villain_id, minion_id) " +
                "VALUES (1, 2), (1, 3), (2, 4), (2, 5), (3, 1), (4, 1), (4, 5), (5, 1), (5, 2), (5, 3), (5, 4), (5, 5);";

        try (
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(createBaseQuery);
            statement.executeQuery(useQuery);
            statement.executeUpdate(createTownsTable);
            statement.executeUpdate(createMinionsTable);
            statement.executeUpdate(createVillainsTable);
            statement.executeUpdate(createTableVillainsMinions);

            statement.executeUpdate(insertIntoTowns);
            statement.executeUpdate(insertIntoMinions);
            statement.executeUpdate(insertIntoVillains);
            statement.executeUpdate(insertIntoMapper);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
