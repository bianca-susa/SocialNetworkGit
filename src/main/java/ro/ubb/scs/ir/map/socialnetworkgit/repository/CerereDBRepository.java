package ro.ubb.scs.ir.map.socialnetworkgit.repository;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.*;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CerereDBRepository<E extends ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity<ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple<ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator, ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator>>> extends PrieteniiRepository<E> {
    private String url;
    private String username;
    private String password;

    public CerereDBRepository(Validator<E> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Iterable<E> findAll() {
        Set<ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere> cereri = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from cereri");
             ResultSet resultSet = statement.executeQuery();
             PreparedStatement statement_u1 = connection.prepareStatement("SELECT users.id_user, users.first_name, users.last_name\n" +
                     "FROM cereri\n" +
                     "JOIN users ON users.id_user = cereri.u1\n" +
                     "WHERE users.id_user = ?\n" +
                     "GROUP BY users.id_user;\n");

             PreparedStatement statement_u2 = connection.prepareStatement("SELECT users.id_user, users.first_name, users.last_name\n" +
                     "FROM cereri\n" +
                     "JOIN users ON users.id_user = cereri.u2\n" +
                     "WHERE users.id_user = ?\n" +
                     "GROUP BY users.id_user;\n");
        ) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("u1");

                statement_u1.setLong(1, id1);
                ResultSet result_u1 = statement_u1.executeQuery();

                ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u1 = null;
                if (result_u1.next()) {
                    String first_name = result_u1.getString("first_name");
                    String last_name = result_u1.getString("last_name");

                    u1 = new ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator(first_name, last_name);
                    u1.setId(id1);
                }

                Long id2 = resultSet.getLong("u2");

                statement_u2.setLong(1, id2);
                ResultSet result_u2 = statement_u2.executeQuery();

                ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u2 = null;
                if (result_u2.next()) {
                    String first_name1 = result_u2.getString("first_name");
                    String last_name1 = result_u2.getString("last_name");

                    u2 = new ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator(first_name1, last_name1);
                    u2.setId(id2);
                }

                String status = resultSet.getString("status");

                ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere cerere = new ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere();
                cerere.setId(new ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple<>(u1,u2));
                cerere.setStatus(status);

                cereri.add(cerere);
                super.save((E) cerere);


            }
            return (Iterable<E>) cereri;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public E save(E entity) {
        super.save(entity);

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO cereri (u1,u2,status) VALUES (?,?,?)");

        ) {
            statement.setLong(1, entity.getId().getLeft().getId());
            statement.setLong(2, entity.getId().getRight().getId());
            statement.setString(3, ((ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere) entity).getStatus());

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        //return Optional.of(entity);
                        return entity;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //return Optional.empty();
        return null;
    }

    @Override
    public E findOne(ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u1, ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u2) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM cereri WHERE (u1 = ? AND u2 = ?)")) {

            statement.setLong(1, u1.getId());
            statement.setLong(2, u2.getId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere cerere = new ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere();
                cerere.setId(new ro.ubb.scs.ir.map.socialnetworkgit.domain.Tuple<>(u1, u2));
                cerere.setStatus(resultSet.getString("status"));
                return (E) cerere;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public E updateRequest(E entity)
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("UPDATE cereri\n" +
                     "SET status = ? \n" +
                     "WHERE u1=? AND u2=?;")) {

            statement.setString(1, ((ro.ubb.scs.ir.map.socialnetworkgit.domain.Cerere) entity).getStatus());
            statement.setLong(2, entity.getId().getLeft().getId());
            statement.setLong(3, entity.getId().getRight().getId());

            int rows = statement.executeUpdate();

            if (rows > 0) {

                return entity;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
