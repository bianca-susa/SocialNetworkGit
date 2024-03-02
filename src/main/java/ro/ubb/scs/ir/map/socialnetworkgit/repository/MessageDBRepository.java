package ro.ubb.scs.ir.map.socialnetworkgit.repository;

import ro.ubb.scs.ir.map.socialnetworkgit.domain.*;
import ro.ubb.scs.ir.map.socialnetworkgit.validator.Validator;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDBRepository<ID, E extends ro.ubb.scs.ir.map.socialnetworkgit.domain.Entity<ID>> extends ro.ubb.scs.ir.map.socialnetworkgit.repository.InMemoryRepository<ID,E> {
    private String url;
    private String username;
    private String password;

    public MessageDBRepository(Validator<E> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Iterable<E> findAll() {
        findAll_DB();
        try {
            getReplies();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return (Iterable<E>) this.entities.values();
    }

    public Iterable<E> findAll_DB() {
        Set<ro.ubb.scs.ir.map.socialnetworkgit.domain.Message> messages = new HashSet<>();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages");
             ResultSet resultSet = statement.executeQuery();
             PreparedStatement statement_from = connection.prepareStatement("SELECT users.id_user, users.first_name, users.last_name\n" +
                     "FROM messages\n" +
                     "JOIN users ON users.id_user = messages.from \n" +
                     "WHERE id_user = ?\n" +
                     "GROUP BY users.id_user;");

             PreparedStatement statement_to = connection.prepareStatement("SELECT u.*\n" +
                     "FROM \"messagesUsers\" mu\n" +
                     "JOIN users u ON mu.id_user = u.id_user\n" +
                     "WHERE mu.id_message = ?;");
        ) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long id_u = resultSet.getLong("from");
                String text = resultSet.getString("text");
                //Date data = resultSet.getDate("data");
                Timestamp timestamp = resultSet.getTimestamp("data");
                LocalDateTime localDateTime = timestamp.toLocalDateTime();

                Long reply = resultSet.getLong("reply");

                statement_from.setLong(1, id_u);
                ResultSet result_from = statement_from.executeQuery();

                ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u1 = null;
                if (result_from.next()) {
                    String first_name = result_from.getString("first_name");
                    String last_name = result_from.getString("last_name");

                    u1 = new ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator(first_name, last_name);
                    u1.setId(id_u);
                }

                statement_to.setLong(1, id);
                ResultSet result_to = statement_to.executeQuery();

                List<ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator> to_users = new ArrayList<ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator>();
                while (result_to.next()) {
                    ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator to;
                    Long id_user = result_to.getLong("id_user");
                    String first_name1 = result_to.getString("first_name");
                    String last_name1 = result_to.getString("last_name");

                    to = new ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator(first_name1, last_name1);
                    to.setId(id_user);

                    to_users.add(to);
                }

                ro.ubb.scs.ir.map.socialnetworkgit.domain.Message message = new ro.ubb.scs.ir.map.socialnetworkgit.domain.Message(u1, to_users, text, localDateTime);
                message.setReply(null);
                message.setId(id);

                messages.add(message);
                super.save((E) message);

            }
            return this.entities.values();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<ro.ubb.scs.ir.map.socialnetworkgit.domain.Message> getMessages(ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u1, ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator u2)
    {
        findAll();
        List<ro.ubb.scs.ir.map.socialnetworkgit.domain.Message> messages = new ArrayList<ro.ubb.scs.ir.map.socialnetworkgit.domain.Message>();
        for(Map.Entry<ID, E> entry : entities.entrySet()) {
            Long id = (Long) entry.getKey();
            ro.ubb.scs.ir.map.socialnetworkgit.domain.Message m = (ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entry.getValue();

            if(m.getFrom().getId()== u1.getId() && m.getTo().contains(u2)) {
                if (!messages.contains(m))
                    messages.add(m);
                if (m.getReply() != null && m.getReply().getFrom().getId() == u2.getId())
                    if (!messages.contains(m.getReply()))
                        messages.add(m.getReply());
            }

            if(m.getFrom().getId()== u2.getId() && m.getTo().contains(u1)) {
                if (!messages.contains(m))
                    messages.add(m);
                if (m.getReply() != null && m.getReply().getFrom().getId() == u1.getId())
                    if (!messages.contains(m.getReply()))
                        messages.add(m.getReply());
            }
        }

        messages.sort(Comparator.comparing(ro.ubb.scs.ir.map.socialnetworkgit.domain.Message::getData));
        return messages;
    }

    private void getReplies() throws SQLException {

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select * from messages where id = ?");

        ) {
            for(Map.Entry<ID, E> entry : entities.entrySet()) {
                Long id = (Long) entry.getKey();
                ro.ubb.scs.ir.map.socialnetworkgit.domain.Message m = (ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entry.getValue();
                statement.setLong(1, id);
                ResultSet resultSet = statement.executeQuery();

                while(resultSet.next()) {
                    Long reply = resultSet.getLong("reply");
                    if (reply != 0) {
                        Optional o = this.findOne((ID) reply);
                        ro.ubb.scs.ir.map.socialnetworkgit.domain.Message replied = (ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) o.orElseThrow(() -> new NoSuchElementException("Nu exista utilizator!"));
                        m.setReply(replied);
                    }
                }
            }

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<E> save(E entity) {
        List<ro.ubb.scs.ir.map.socialnetworkgit.domain.Message> messages = getMessages(((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message)entity).getFrom(), ((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message)entity).getTo().get(((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message)entity).getTo().size()-1));
        ro.ubb.scs.ir.map.socialnetworkgit.domain.Message message = null;
        if(messages.size()!=0)
            message = messages.get(messages.size() -1);

        Optional o = save_DB(entity);
        ro.ubb.scs.ir.map.socialnetworkgit.domain.Message m;
        try {
            m = (ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) o.orElseThrow(()->new NoSuchElementException("No element!"));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        saveMessagesUsers((E) m);


        if(messages.size() != 0)
        {
            message.setReply(m);
            setReply_DB((E) message, (E) m);
        }

        return o;
    }

    private void saveMessagesUsers(E entity)
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);) {
            PreparedStatement statement1 = connection.prepareStatement("INSERT INTO public.\"messagesUsers\"(\n" +
                    "\tid_message, id_user)\n" +
                    "\tVALUES (?, ?);");

            statement1.setInt(1, Math.toIntExact(((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).getId()));
            List<ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator> toUsers = ((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).getTo();
            ro.ubb.scs.ir.map.socialnetworkgit.domain.Utilizator lastUser = toUsers.get(toUsers.size() - 1);

            statement1.setInt(2, Math.toIntExact(lastUser.getId()));

            statement1.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setReply_DB(E entity, E reply)
    {
        try (Connection connection = DriverManager.getConnection(url, username, password);) {
            PreparedStatement statement1 = connection.prepareStatement("UPDATE messages \n" +
                    "SET reply = ?\n" +
                    "WHERE id=?;");

            statement1.setInt(1, Math.toIntExact((Long) reply.getId()));

            statement1.setInt(2, Math.toIntExact(((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).getId()));

            statement1.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Optional<E> save_DB(E entity) {
        //super.save(entity);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO public.messages(\n" +
                     "\t\"from\", text, data, reply)\n" +
                     "\tVALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setInt(1, Math.toIntExact(((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).getFrom().getId()));
            statement.setString(2,((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).getMessage());

            LocalDateTime localDateTime = ((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).getData();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);
            statement.setTimestamp(3, timestamp);

            if(((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).getReply() != null)
                statement.setInt(4, Math.toIntExact(((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).getReply().getId()));
            else statement.setNull(4, Types.INTEGER);

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {

                        long generatedId = generatedKeys.getLong(1);

                        ((ro.ubb.scs.ir.map.socialnetworkgit.domain.Message) entity).setId(generatedId);
                        return Optional.of(entity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.empty();

    }
}

