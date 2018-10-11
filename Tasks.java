package app;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tasks {
    private static final String URL = "jdbc:mysql://localhost:3306/minions_db?serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {

        try (
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        ) {

//            getVillainsNames(connection);
//            getMinionNames(connection);
//            addMinion(connection);
//            changeTownCasing(connection);
//            deleteVillain(connection);
//            getMinionsNamesShifted(connection);
//            increaseMinionAge(connection);
            increaseAgeProcedure(connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void increaseAgeProcedure(Connection connection) {
        String uspIncreaseAgeQuery = "{CALL usp_get_older(?)}";
        String selectQuery = "SELECT m.name, m.age FROM minions AS m ORDER BY m.minion_id";

        try (
                Scanner scanner = new Scanner(System.in);
                Statement selectStatement = connection.createStatement();
                CallableStatement uspIncreaseAgeStatement = connection.prepareCall(uspIncreaseAgeQuery);
                ){

            int id = Integer.parseInt(scanner.nextLine());
            uspIncreaseAgeStatement.setInt(1, id);

            boolean hadResult = uspIncreaseAgeStatement.execute();
            if (hadResult) {
                ResultSet selectSet = selectStatement.executeQuery(selectQuery);
                if (selectSet.next()) {
                    ResultSet set = uspIncreaseAgeStatement.getResultSet();
                    set.next();
                    System.out.println(selectSet.getString("name") + " " + selectSet.getInt("age"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void increaseMinionAge(Connection connection) {
        String selectMinionsQuery = "SELECT m.minion_id, m.name, m.age " +
                "FROM minions AS m " +
                "ORDER BY m.minion_id;";
        String increaseAgeQuery = "UPDATE minions " +
                "SET age = age + 1 " +
                "WHERE minion_id = ?;";
        String alterNameQuery = "UPDATE minions " +
                "SET name = CONCAT(LEFT(UPPER(name), 1), SUBSTRING(name, 2)) " +
                "WHERE minion_id = ?;";
        String selectMinionsForResultQuery = "SELECT m.name, m.age FROM minions AS m;";

        try (
                Scanner scanner = new Scanner(System.in);
                PreparedStatement selectMinionsStatement = connection.prepareStatement(selectMinionsQuery);
                PreparedStatement increaseAgeStatement = connection.prepareStatement(increaseAgeQuery);
                PreparedStatement alterNameStatement = connection.prepareStatement(alterNameQuery);
                PreparedStatement selectMinionsForResultStatement = connection.prepareStatement(selectMinionsForResultQuery);
                ){
            List<Integer> ids = Arrays.stream(scanner.nextLine().split("\\s+")).map(Integer::parseInt).sorted().collect(Collectors.toList());
            ResultSet set = selectMinionsStatement.executeQuery();
            while (set.next()) {
                for (Integer id : ids) {
                    if (id == (set.getInt("minion_id"))) {
                        increaseAgeStatement.setInt(1, id);
                        increaseAgeStatement.executeUpdate();
                        alterNameStatement.setInt(1, id);
                        alterNameStatement.executeUpdate();
                    }
                }
            }

            ResultSet finalSet = selectMinionsForResultStatement.executeQuery();
            while (finalSet.next()) {
                System.out.println(finalSet.getString("name") + " " + finalSet.getInt("age"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getMinionsNamesShifted(Connection connection) {
        String selectOddsQuery = "SELECT m.minion_id, m.name " +
                "FROM minions AS m " +
                "WHERE m.minion_id % 2 != 0 " +
                "ORDER BY minion_id;";
        String selectEvensQuery = "SELECT m.minion_id, m.name " +
                "FROM minions AS m " +
                "WHERE m.minion_id % 2 = 0 " +
                "ORDER BY minion_id DESC;";
        String selectQuery = "SELECT m.name FROM minions AS m;";

        try (
                Statement selectStatement = connection.createStatement();
                PreparedStatement oddsStatement = connection.prepareStatement(selectOddsQuery);
                PreparedStatement evensStatement = connection.prepareStatement(selectEvensQuery);
                ){
            ResultSet set  = selectStatement.executeQuery(selectQuery);
            List<String> resultList = new ArrayList<>();
            while (set.next()) {
                resultList.add(set.getString("name"));
            }

            List<String> arrangedList = new ArrayList<>();
            while (resultList.size() > 0) {
                arrangedList.add(resultList.get(0));
                resultList.remove(0);
                if (resultList.size() > 0) {
                    arrangedList.add(resultList.get(resultList.size() - 1));
                    resultList.remove(resultList.size() - 1);
                }
            }

//            ResultSet oddsResult = oddsStatement.executeQuery();
//            ResultSet evensResult = evensStatement.executeQuery();
//
//            for (; oddsResult.next() && evensResult.next(); ) {
//                resultList.add(oddsResult.getString("name"));
//                resultList.add(evensResult.getString("name"));
//            }
//            if (oddsResult.next()) {
//                resultList.add(oddsResult.getString("name"));
//            }

            arrangedList    .forEach(s -> System.out.println(s));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteVillain(Connection connection) {
        String selectVillainQuery = "SELECT * FROM villains;";
        String removeVilConstraint = "DELETE FROM `villains_minions` " +
                "WHERE villain_id = ?;";
        String removeVillain = "DELETE FROM `villains` " +
                "WHERE villain_id = ?";

        try (
                Scanner scanner = new Scanner(System.in);
                PreparedStatement removeStatement = connection.prepareStatement(removeVilConstraint);
                PreparedStatement selectStatement = connection.prepareStatement(selectVillainQuery);
                PreparedStatement removeVillainStatement = connection.prepareStatement(removeVillain);
                ){

            int id = Integer.parseInt(scanner.nextLine());

            ResultSet selectResult = selectStatement.executeQuery();
            String villainName = null;
            while (selectResult.next()) {
                if (selectResult.getInt("villain_id") == id) {
                    villainName = selectResult.getString("name");
                    break;
                }
            }

            removeStatement.setInt(1, id);
            int releasedMinions = removeStatement.executeUpdate();

            removeVillainStatement.setInt(1, id);
            int removed = removeVillainStatement.executeUpdate();
            if (removed <= 0) {
                System.out.println("No such villain was found");
            } else {
                System.out.println(villainName+" was deleted\r\n"+releasedMinions+" minions released");
            }

            //TODO this whole thing should be 1 transaction

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void changeTownCasing(Connection connection) {
        String selectQuery = "SELECT * FROM towns;";
        String updateQuery = "UPDATE towns " +
                "SET name=UPPER(name) " +
                "WHERE country = ?;";

        try (
                Scanner scanner = new Scanner(System.in);
                Statement statement = connection.createStatement();
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                ){
            String country = scanner.nextLine();
            preparedStatement.setString(1, country);

            ResultSet set = statement.executeQuery(selectQuery);
            int changeCount = 0;
            List<String> countries = new ArrayList<>();
            while (set.next()) {
                String currentCountry = set.getString("country");
                String currentTown = set.getString("name");
                if (currentCountry.equals(country)) {
                    changeCount++;
                    countries.add(currentTown.toUpperCase());
                    preparedStatement.execute();
                }
            }

            if (changeCount == 0) {
                System.out.println("No town names were affected.");
            } else {
                System.out.println(countries.stream().collect(Collectors.joining(", ", "[", "]")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addMinion(Connection connection) {
        String selectQuery = "SELECT v.villain_id, v.name AS v_name, m.minion_id, m.name AS m_name, m.town_id, t.name AS town_name " +
                "FROM villains AS v " +
                "INNER JOIN villains_minions AS vm " +
                "   ON v.villain_id = vm.villain_id " +
                "INNER JOIN minions AS m " +
                "   ON vm.minion_id = m.minion_id " +
                "INNER JOIN towns AS t " +
                "   ON m.town_id = t.town_id " +
                "ORDER BY v.name, m.name;";

        try (
                PreparedStatement statement = connection.prepareStatement(selectQuery);
        ) {

            Scanner scanner = new Scanner(System.in);
            String[] minionInfo = scanner.nextLine().split("\\s+");
            String[] villainInfo = scanner.nextLine().split("\\s+");

            String villain = villainInfo[1];
            int villain_id = addMinion_insertVillainIfNotExist(villain, connection);

            String town = minionInfo[3];
            int town_id = addMinion_insertTownIfNotExist(town, connection);

//            String minionName = minionInfo[1];
//            int minion_id = -1;

//            ResultSet result = statement.executeQuery();
//            while (result.next()) {
//                if (result.getString("town_name").equals(town)) {
//                    town_id = result.getInt("town_id");
//                }
//                if (result.getString("m_name").equals(minionName)) {
//                    minion_id = result.getInt("minion_id");
//                }
//                if (result.getString("v_name").equals(villain)) {
//                    villain_id = result.getInt("villain_id");
//                }
//                if (town_id != -1 && minion_id != -1 && villain_id != -1) {
//                    break;
//                }
//            }

            String insertMinionQuery = "INSERT INTO `minions`(name, age, town_id) " +
                    "VALUES (?, ?, ?);";
            String insertMinVilQuery = "INSERT INTO `villains_minions`(villain_id, minion_id) " +
                    "VALUES (?, ?);";
            try (
                    PreparedStatement insertMinionStatement = connection.prepareStatement(insertMinionQuery);
                    PreparedStatement insertMinVilStatement = connection.prepareStatement(insertMinVilQuery);
            ) {
                insertMinionStatement.setString(1, minionInfo[1]);
                insertMinionStatement.setInt(2, Integer.parseInt(minionInfo[2]));
                insertMinionStatement.setInt(3, town_id);
                insertMinionStatement.execute();

                Statement getMinionIdStatement = connection.createStatement();
                ResultSet set = getMinionIdStatement.executeQuery("select count(m.name) as count from minions as m;");
                int minion_id = 1;
                while (set.next()) {
                    minion_id = set.getInt("count");
                }

                insertMinVilStatement.setInt(1, villain_id);
                insertMinVilStatement.setInt(2, minion_id);
                insertMinVilStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("Successfully added " + minionInfo[1] + " to be minion of " + villain);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static int addMinion_insertTownIfNotExist(String town, Connection connection) {
        String townsSelect = "SELECT * FROM towns;";
        String addTownQuery = "INSERT INTO `towns`(name, country) " +
                "VALUES (?, 'Bulgaria');";
        try (
                PreparedStatement townStatement = connection.prepareStatement(townsSelect);
                PreparedStatement addTownStatement = connection.prepareStatement(addTownQuery);
        ) {

            ResultSet townResults = townStatement.executeQuery();
            int baseIndex = 1;
            while (townResults.next()) {
                String availableTown = townResults.getString("name");
                if (town.equals(availableTown)) {
                    return townResults.getInt("town_id");
                    //TODO town exists
                }
                baseIndex++;
            }
            addTownStatement.setString(1, town);
            addTownStatement.execute();
            System.out.println("Town " + town + " was added to the database.");
            return baseIndex;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static int addMinion_insertVillainIfNotExist(String villain, Connection connection) {
        String villainSelect = "SELECT * FROM villains";
        String addVillainQuery = "INSERT INTO `villains`(name, evilness) " +
                "VALUES (?, 'evil');";
        try (
                PreparedStatement villainStatement = connection.prepareStatement(villainSelect);
                PreparedStatement addVillainStatement = connection.prepareStatement(addVillainQuery);
        ) {

            ResultSet villainResults = villainStatement.executeQuery();
            int baseIndex = 1;
            while (villainResults.next()) {
                String availableVillain = villainResults.getString("name");
                if (villain.equals(availableVillain)) {
                    return villainResults.getInt("villain_id");
                    //TODO villain exists
                }
                baseIndex++;
            }
            addVillainStatement.setString(1, villain);
            addVillainStatement.execute();
            System.out.println("Villain " + villain + " was added to the database.");
            return baseIndex;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static void getMinionNames(Connection connection) {
        String villainAndMinionsSelect = "SELECT v.name AS v_name, v.villain_id, m.name AS m_name, m.age " +
                "FROM villains AS v " +
                "INNER JOIN villains_minions AS vm " +
                "   ON v.villain_id = vm.villain_id " +
                "INNER JOIN minions AS m " +
                "   ON vm.minion_id = m.minion_id " +
                "ORDER BY v.name, m.name;";

        try (
                PreparedStatement statement = connection.prepareStatement(villainAndMinionsSelect);
        ) {

            ResultSet result = statement.executeQuery();

            Scanner scanner = new Scanner(System.in);
            int index = Integer.parseInt(scanner.nextLine());
            boolean hasFound = false;

            while (result.next()) {
                int villainId = result.getInt("villain_id");
                if (villainId == index) {
                    hasFound = true;

                    String vName = result.getString("v_name");
                    String mName = result.getString("m_name");
                    int age = result.getInt("age");

                    System.out.println("Villain: " + vName);
                    if (mName == null) {
                        System.out.println("<no minions>");
                    } else {
                        System.out.println("1. " + mName + " " + age);
                        int i = 2;
                        while (result.next() && result.getInt("villain_id") == index) {
                            mName = result.getString("m_name");
                            age = result.getInt("age");
                            System.out.println(i + ". " + mName + " " + age);
                            i++;
                        }
                        break;
                    }
                }
            }

            if (!hasFound) {
                System.out.println("No villain with ID " + index + " exists in the database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getVillainsNames(Connection connection) {
        String villainsAndMinionsSelect = "SELECT v.name, COUNT(vm.minion_id) AS 'minion_count'" +
                " FROM villains AS v" +
                " INNER JOIN villains_minions AS vm" +
                " ON v.villain_id = vm.villain_id" +
                " GROUP BY vm.villain_id" +
                " ORDER BY minion_count DESC, v.name;";

        try (
                PreparedStatement statement = connection.prepareStatement(villainsAndMinionsSelect);
        ) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String name = result.getString("name");
                int count = result.getInt("minion_count");
                System.out.println(String.format("%s %d", name, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
