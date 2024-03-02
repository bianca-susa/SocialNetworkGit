package ro.ubb.scs.ir.map.socialnetworkgit.repository;

import ro.ubb.scs.ir.map.socialnetworkgit.PasswordHashing;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity;
import ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Page;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Pageable;
//import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.PagingRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Page;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.Pageable;
import ro.ubb.scs.ir.map.socialnetworkgit.repository.paging.PagingRepository;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

import java.sql.*;
import java.util.*;

public class UtilizatorDBRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> implements PagingRepository<Long, Utilizator> {
    private String url;
    private String username;
    private String password;

    public UtilizatorDBRepository(Validator<E> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional findOne(ID id) {
        //return super.findOne(id);
        return findOne_DB(id);
    }


    public Optional findOne_DB(ID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from \"users&pass\" " +
                     "where id_user = ?");

        ) {
            statement.setInt(1, Math.toIntExact((Long) id));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator u = new Utilizator(firstName, lastName);
                u.setId((Long) id);
                return Optional.ofNullable(u);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        findAll_DB();
        updatePrietenii();
        return this.entities.values();
    }


    public Iterable<E> findAll_DB() {
        Set<Utilizator> users = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"users&pass\"");
             ResultSet resultSet = statement.executeQuery();
             PreparedStatement statement_p = connection.prepareStatement("SELECT \"Prietenii\".\"Utilizator1\", \"Prietenii\".\"Utilizator2\" \n" +
                     "FROM users\n" +
                     "JOIN \"Prietenii\"  ON \"Prietenii\".\"Utilizator1\" = \"users&pass\".id_user\n" +
                     "WHERE id_user = ?;");
        ) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id_user");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                Utilizator user = new Utilizator(firstName, lastName);
                user.setId(id);
                users.add(user);
                this.entities.put((ID) user.getId(), (E) user);
                //users.putIfAbsent((ID) user.getId(), (E) user);

            }
            return (Iterable<E>) users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updatePrietenii() {
        for (Map.Entry<ID, E> entry : entities.entrySet()) {
            Long id = (Long) entry.getKey();
            Utilizator u1 = (Utilizator) entry.getValue();
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 //PreparedStatement statement = connection.prepareStatement("select * from Users");
                 //ResultSet resultSet = statement.executeQuery();
                 PreparedStatement statement_p = connection.prepareStatement("SELECT \"Prietenii\".\"Utilizator1\", \"Prietenii\".\"Utilizator2\" \n" +
                         "FROM \"users&pass\"\n" +
                         "JOIN \"Prietenii\"  ON \"Prietenii\".\"Utilizator1\" = \"users&pass\".id_user\n" +
                         "WHERE id_user = ?;");
            ) {
                statement_p.setLong(1, id);
                ResultSet resultSet = statement_p.executeQuery();

                Utilizator u2 = null;
                while (resultSet.next()) {
                    Long id2 = resultSet.getLong("Utilizator2");
                    Optional optional = this.findOne((ID) id2);
                    u2 = (Utilizator) optional.orElse(new Utilizator("Default", "User"));
                    u1.addFriend(u2);
                    u2.addFriend(u1);
                }
                //return (Iterable<E>) users;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        for (Map.Entry<ID, E> entry : entities.entrySet()) {
            Long id = (Long) entry.getKey();
            Utilizator u1 = (Utilizator) entry.getValue();
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement_p = connection.prepareStatement("SELECT \"Prietenii\".\"Utilizator1\", \"Prietenii\".\"Utilizator2\" \n" +
                         "FROM \"users&pass\"\n" +
                         "JOIN \"Prietenii\"  ON \"Prietenii\".\"Utilizator2\" = \"users&pass\".id_user\n" +
                         "WHERE id_user = ?;");
            ) {
                statement_p.setLong(1, id);
                ResultSet resultSet = statement_p.executeQuery();

                Utilizator u2 = null;
                while (resultSet.next()) {
                    Long id2 = resultSet.getLong("Utilizator1");
                    Optional optional = this.findOne((ID) id2);
                    u2 = (Utilizator) optional.orElse(new Utilizator("Default", "User"));
                    u1.addFriend(u2);
                    u2.addFriend(u1);
                }
                //return (Iterable<E>) users;

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public Optional<E> save(E entity) {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"users&pass\" (first_name, last_name, password) VALUES (?,?,?)");

        ) {
            statement.setString(1, ((Utilizator) entity).getFirst_name());
            statement.setString(2, ((Utilizator) entity).getLast_name());
            statement.setString(3, ((Utilizator) entity).getPassword());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return Optional.of(entity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }



    @Override
    public Optional<E> delete(ID id) {
        Optional<E> found = findOne(id);
        if (found.isPresent()) {
            super.delete(id);

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM \"users&pass\" WHERE id_user = ?");

            ) {
                statement.setLong(1, (Long) id);

                int rows = statement.executeUpdate();

                return (rows > 0) ? found : Optional.empty();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else return Optional.empty();
    }


    @Override
    public Optional<E> update(E entity) {
        super.update(entity);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE  \"users&pass\"  SET first_name=?, last_name=? WHERE id_user = ?");

        ) {
            statement.setLong(3, Math.toIntExact((Long) entity.getId()));
            statement.setString(1, ((Utilizator) entity).getFirst_name());
            statement.setString(2, ((Utilizator) entity).getLast_name());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return Optional.of(entity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public void savePassword(String user_password, E entity)
    {
        String hashedPasswordString = PasswordHashing.hashPassword(user_password);
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE public.\"users&pass\"\n" +
                     "\tSET password=?\n" +
                     "\tWHERE id_user = ?;");

        ) {
            statement.setString(1, hashedPasswordString);
            statement.setLong(2, Math.toIntExact((Long) entity.getId()));

            int rows = statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserPassword(Utilizator utilizator)
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT password FROM \"users&pass\" WHERE id_user = ?;");

        ) {
            statement.setLong(1, Math.toIntExact(utilizator.getId()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String password = resultSet.getString("password");

                return password;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Page<Utilizator> findAllOnPage(Pageable pageable) {
        List<Utilizator> movies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pageStatement = connection.prepareStatement("SELECT * FROM \"users&pass\" LIMIT ? OFFSET ?");
             PreparedStatement countStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM \"users&pass\"")
        ){
            pageStatement.setInt(1, pageable.getPageSize());
            pageStatement.setInt(2, pageable.getPageSize() * pageable.getPageNumber());

            try (
                    ResultSet pageResultSet = pageStatement.executeQuery();
                    ResultSet countResultSet = countStatement.executeQuery();
            ) {
                int count = 0;
                if (countResultSet.next()) {
                    count = countResultSet.getInt("count");
                }

                while(pageResultSet.next()){
                    Long id = pageResultSet.getLong("id_user");
                    String nume = pageResultSet.getString("first_name");
                    String prenume = pageResultSet.getString("last_name");

                    Utilizator u = new Utilizator(nume, prenume);
                    u.setId(id);

                    movies.add(u);
                }
                return new Page<>(movies, count);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
