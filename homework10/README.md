# Projector HSA Homework_10

## Run postgres and mysql

```sh
docker-compose up
```

## Summary table with isolation levels and anomalies

### MySQL
|                     | READ UNCOMMITTED | READ COMMITED | REPEATABLE READ | SERIALIZABLE |
| ------------------- | :--------------: | :-----------: | :-------------: | :----------: |
| Dirty Read          |        ✅        |      ❌       |       ❌        |      ❌      |
| Non-repeatable read |        ✅        |      ✅       |       ❌        |      ❌      |
| Phantom read        |        ✅        |      ✅       |       ❌        |      ❌      |
| Lost update         |        ✅        |      ✅       |       ❌        |      ❌      |
|                     |                  |               |                 |              |

### Postgres
|                     | READ UNCOMMITTED | READ COMMITED | REPEATABLE READ | SERIALIZABLE |
| ------------------- | :--------------: | :-----------: | :-------------: | :----------: |
| Dirty Read          |        ❌        |      ❌       |       ❌        |      ❌      |
| Non-repeatable read |        ✅        |      ✅       |       ❌        |      ❌      |
| Phantom read        |        ✅        |      ✅       |       ❌        |      ❌      |
| Lost update         |        ✅        |      ✅       |       ❌        |      ❌      |
|                     |                  |               |                 |              |


## Testing MySQL

```sh
docker run --network homework10_default -it --rm mysql mysql -hhomework10-mysql-1 -uroot -ppassword
```

### READ UNCOMMITTED
```sql
transaction1> SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
transaction2> SET SESSION TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

transaction1> start transaction;
transaction2> start transaction;

transaction1> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction1> insert into users values(4, 'user4', 7000, now());
transaction1> delete from users where name = 'user2';
transaction1> update users set balance = 9000 where name = 'user1';

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    9000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
|  4 | user4 |    7000 | 2023-07-29 12:58:53 |
+----+-------+---------+---------------------+
3 rows in set (0.01 sec)

transaction1> rollback;
transaction2> rollback;
```

**Dirty Read:** Before `transaction1` commit, `transaction2` can see all updated data.

### READ COMMITTED

```sql
transaction1> SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;
transaction2> SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

transaction1> start transaction;
transaction2> start transaction;

transaction1> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction1> insert into users values(4, 'user4', 7000, now());
transaction1> delete from users where name = 'user2';
transaction1> update users set balance = 9000 where name = 'user1';

transaction1> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    9000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
|  4 | user4 |    7000 | 2023-07-29 13:28:43 |
+----+-------+---------+---------------------+
3 rows in set (0.01 sec)

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction1> commit;

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    9000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
|  4 | user4 |    7000 | 2023-07-29 13:28:43 |
+----+-------+---------+---------------------+
3 rows in set (0.01 sec)

transaction2> rollback;
```

`transaction2` can't see updated data before `transaction1` commit them. After commit `transaction2` can see all updated data.

**Non-repeatable read:** `transaction2` can see deleted `user2` and updated balance for `user1` after second select

**Phantom read:** `transaction2` can see new row with `user4` after second select

### REPEATABLE READ

```sql
transaction1> SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;
transaction2> SET SESSION TRANSACTION ISOLATION LEVEL REPEATABLE READ;

transaction1> start transaction;
transaction2> start transaction;

transaction1> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction1> insert into users values(4, 'user4', 7000, now());
transaction1> delete from users where name = 'user2';
transaction1> update users set balance = 9000 where name = 'user1';

transaction1> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    9000 | 2023-07-29 13:39:20 |
|  3 | user3 |     500 | 2023-07-29 13:39:20 |
|  4 | user4 |    7000 | 2023-07-29 13:40:32 |
+----+-------+---------+---------------------+
3 rows in set (0.01 sec)

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 13:39:20 |
|  2 | user2 |    2000 | 2023-07-29 13:39:20 |
|  3 | user3 |     500 | 2023-07-29 13:39:20 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction2> update users set balance = 200 where name = 'user1'; -- BLOCKS until transaction1 finishes

transaction1> commit;

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |     200 | 2023-07-29 13:39:20 |
|  2 | user2 |    2000 | 2023-07-29 13:39:20 |
|  3 | user3 |     500 | 2023-07-29 13:39:20 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction2> commit;

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |     200 | 2023-07-29 13:39:20 |
|  3 | user3 |     500 | 2023-07-29 13:39:20 |
|  4 | user4 |    7000 | 2023-07-29 13:40:32 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction1> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |     200 | 2023-07-29 13:39:20 |
|  3 | user3 |     500 | 2023-07-29 13:39:20 |
|  4 | user4 |    7000 | 2023-07-29 13:40:32 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)
```

`transaction2` doesn't see changes from `transaction1` even after commit. So `Non-repeatable read`  and `Phantom read` don't appears in this isolation level.


### SERIALIZABLE
```sql
transaction1> SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;
transaction2> SET SESSION TRANSACTION ISOLATION LEVEL SERIALIZABLE;

transaction1> start transaction;
transaction2> start transaction;

transaction1> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 12:06:04 |
|  2 | user2 |    2000 | 2023-07-29 12:06:04 |
|  3 | user3 |     500 | 2023-07-29 12:06:04 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction1> update users set balance = 9000 where name = 'user1'; -- Blocks because we cant change data that was selected in other transaction

transaction2> commit;

transaction1> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    9000 | 2023-07-29 13:49:53 |
|  2 | user2 |    2000 | 2023-07-29 13:49:53 |
|  3 | user3 |     500 | 2023-07-29 13:49:54 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction2> select * from users;
+----+-------+---------+---------------------+
| id | name  | balance | created_at          |
+----+-------+---------+---------------------+
|  1 | user1 |    1000 | 2023-07-29 13:49:53 |
|  2 | user2 |    2000 | 2023-07-29 13:49:53 |
|  3 | user3 |     500 | 2023-07-29 13:49:54 |
+----+-------+---------+---------------------+
3 rows in set (0.00 sec)

transaction1> rollback;
```

Each transaction behave like there is no other transactions. But it achieves by locks, so performance is bad

## Testing Postgres

```sh
docker run -it --rm --network homework10_default postgres psql -h homework10-postgres-1 -U postgres
```

### READ UNCOMMITTED / READ COMMITTED
```sql
transaction1> BEGIN TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;
transaction2> BEGIN TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

transaction1> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 12:06:01.357222
  2 | user2 |    2000 | 2023-07-29 12:06:01.357716
  3 | user3 |     500 | 2023-07-29 12:06:01.358009
(3 rows)

transaction2> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 12:06:01.357222
  2 | user2 |    2000 | 2023-07-29 12:06:01.357716
  3 | user3 |     500 | 2023-07-29 12:06:01.358009
(3 rows)

transaction1> insert into users values(4, 'user4', 7000, now());
transaction1> delete from users where name = 'user2';
transaction1> update users set balance = 9000 where name = 'user1';

transaction1> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  3 | user3 |     500 | 2023-07-29 12:06:01.358009
  4 | user4 |    7000 | 2023-07-29 14:05:45.710793
  1 | user1 |    9000 | 2023-07-29 12:06:01.357222
(3 rows)

transaction2> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 12:06:01.357222
  2 | user2 |    2000 | 2023-07-29 12:06:01.357716
  3 | user3 |     500 | 2023-07-29 12:06:01.358009
(3 rows)

transaction1> commit;

transaction2> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  3 | user3 |     500 | 2023-07-29 12:06:01.358009
  4 | user4 |    7000 | 2023-07-29 14:08:47.800158
  1 | user1 |    9000 | 2023-07-29 12:06:01.357222
(3 rows)

transaction2> rollback;
```

In posgres `READ UNCOMMITTED` equals `READ COMMITTED` isolation level, so we don't see dirty reads, but we can see non-repeatable reads and phantom reads.

### REPEATABLE READ

```sql
transaction1> BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ;
transaction2> BEGIN TRANSACTION ISOLATION LEVEL REPEATABLE READ;

transaction1> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 14:11:03.042821
  2 | user2 |    2000 | 2023-07-29 14:11:03.055674
  3 | user3 |     500 | 2023-07-29 14:11:03.057052

transaction2> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 14:11:03.042821
  2 | user2 |    2000 | 2023-07-29 14:11:03.055674
  3 | user3 |     500 | 2023-07-29 14:11:03.057052

transaction1> insert into users values(4, 'user4', 7000, now());
transaction1> delete from users where name = 'user2';
transaction1> update users set balance = 9000 where name = 'user1';

transaction1> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  3 | user3 |     500 | 2023-07-29 14:11:03.057052
  4 | user4 |    7000 | 2023-07-29 14:11:15.665877
  1 | user1 |    9000 | 2023-07-29 14:11:03.042821

transaction2> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 14:11:03.042821
  2 | user2 |    2000 | 2023-07-29 14:11:03.055674
  3 | user3 |     500 | 2023-07-29 14:11:03.057052

transaction2> update users set balance = 200 where name = 'user1'; -- BLOCKS until transaction1 finishes

transaction1> commit;

transaction2> ERROR:  could not serialize access due to concurrent update

transaction2> rollback
```

`ERROR:  could not serialize access due to concurrent update`: It indicates that the UPDATE or DELETE statement was queued behind another UPDATE/DELETE statement on the same row. That other statement finished, and due to the guarantees of the REPEATABLE READ isolation level, the statement in this session was cancelled.

### SERIALIZABLE
```sql
transaction1> BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;
transaction2> BEGIN TRANSACTION ISOLATION LEVEL SERIALIZABLE;

transaction1> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 14:17:02.323145
  2 | user2 |    2000 | 2023-07-29 14:17:02.327754
  3 | user3 |     500 | 2023-07-29 14:17:02.331869

transaction2> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 14:17:02.323145
  2 | user2 |    2000 | 2023-07-29 14:17:02.327754
  3 | user3 |     500 | 2023-07-29 14:17:02.331869

transaction1> update users set balance = 9000 where name = 'user1'; 

transaction1> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  2 | user2 |    2000 | 2023-07-29 14:17:02.327754
  3 | user3 |     500 | 2023-07-29 14:17:02.331869
  1 | user1 |    9000 | 2023-07-29 14:17:02.323145

transaction2> select * from users;
 id | name  | balance |         created_at
----+-------+---------+----------------------------
  1 | user1 |    1000 | 2023-07-29 14:17:02.323145
  2 | user2 |    2000 | 2023-07-29 14:17:02.327754
  3 | user3 |     500 | 2023-07-29 14:17:02.331869

transaction2> update users set balance = 8000 where name = 'user1'; -- Blocks

transaction1> commit;

transaction2> ERROR:  could not serialize access due to concurrent update
```

Postgres aborts `transaction2` because it tries to update data that `transaction1` updating.
