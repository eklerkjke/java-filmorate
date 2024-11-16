package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.BaseRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User> implements UserStorage {
    private static final String QUERY_INSERT_USER = """
            INSERT INTO users(login, email, name, birthday)
            VALUES (:login, :login, :name, :birthday);
            """;

    private static final String QUERY_UPDATE_USER = """
            UPDATE users
            SET login = :login, email = :email, name = :name, birthday = :birthday
            WHERE id = :id;
            """;

    private static final String QUERY_GET_ALL = "SELECT * FROM users;";

    private static final String QUERY_GET_BY_ID = "SELECT * FROM users WHERE id = :id;";

    private static final String QUERY_INSERT_FRIEND = "INSERT INTO friends (userId, friendId) VALUES (:userId, :friendId)";

    private static final String QUERY_DELETE_FRIEND = "DELETE FROM friends WHERE userId = :userId AND friendId = :friendId";

    private static final String QUERY_GET_FRIENDS_BY_USER_ID = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
            "FROM users u " +
            "JOIN friends f ON u.id = f.friendId " +
            "WHERE f.userId = :userId";

    private static final String QUERY_GET_COMMON_FRIENDS = "SELECT u.id, u.email, u.login, u.name, u.birthday " +
            "FROM users u " +
            "JOIN friends f1 ON u.id = f1.friendId " +
            "JOIN friends f2 ON u.id = f2.friendId " +
            "WHERE f1.userId = :userId AND f2.userId = :friendId";

    public UserRepository(NamedParameterJdbcOperations jdbcOperations) {
        super(jdbcOperations);
    }

    @Override
    public User add(User user) {
        long id = insert(QUERY_INSERT_USER, toMapSqlParameterSource(user));
        user.setId(id);
        return user;
    }

    @Override
    public User update(User newUser) {
        SqlParameterSource params = toMapSqlParameterSource(newUser);
        update(QUERY_UPDATE_USER, params);
        return newUser;
    }

    @Override
    public Optional<User> getById(Long id) {
        return findOne(QUERY_GET_BY_ID, new MapSqlParameterSource("id", id));
    }

    @Override
    public List<User> getList() {
        return findAll(QUERY_GET_ALL);
    }

    @Override
    public void addFriend(Long targetUserId, Long friendUserId) {
        getById(targetUserId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        getById(friendUserId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", targetUserId)
                .addValue("friendId", friendUserId);

        jdbcOperations.update(QUERY_INSERT_FRIEND, params);
    }


    @Override
    public List<User> getListFriends(Long id) {
        getById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", id);
        return findMany(QUERY_GET_FRIENDS_BY_USER_ID, params);
    }

    @Override
    public void removeFriend(Long targetUserId, Long friendUserId) {
        getById(targetUserId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        getById(friendUserId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", targetUserId)
                .addValue("friendId", friendUserId);

        jdbcOperations.update(QUERY_DELETE_FRIEND, params);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", id)
                .addValue("friendId", otherId);

        return findMany(QUERY_GET_COMMON_FRIENDS, params);
    }

    @Override
    protected void modelMapping(MapSqlParameterSource params, User model) {
        if (model.getId() > 0) {
            params.addValue("id", model.getId());
        }

        params.addValue("login", model.getLogin());
        params.addValue("name", model.getName());
        params.addValue("birthday", model.getBirthday());
        params.addValue("email", model.getEmail());
    }

    @Override
    protected RowMapper<User> getRowMapper() {
        return (rs, rowNum) -> User
                .builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
