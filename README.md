# java-filmorate
Template repository for Filmorate project.

![diagram](/er-diagram.png) 

**SELECT * FROM users** - возвращает всех пользователей

**SELECT * FROM films** - возвращает все фильмы

**SELECT fr.id as id, fr.name as name
FROM user_friends 
WHERE user_friends.user_id = #ID пользователя#
INNER JOIN users AS us ON us.user_id = user_friends.friend_user_id** - возвращает список друзей пользователя

**SELECT COUNT(id)
FROM film_likes
WHERE film_likes.film_id = #ID фильма#** - возвращает количество лайков у фильма
