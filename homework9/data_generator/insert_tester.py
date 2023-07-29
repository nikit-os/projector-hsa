import mysql.connector
import time
import sys;
from faker import Faker

table_name = sys.argv[1]
fake = Faker()

def perform_inserts(commit_mode):
    db = mysql.connector.connect(
      user='root', 
      password='password',
      port=3306,
      host='127.0.0.1', 
      database='homework9_db'
    )

    cursor = db.cursor()

    # Set the innodb_flush_log_at_trx_commit value
    cursor.execute(f"SET GLOBAL innodb_flush_log_at_trx_commit = {commit_mode}")

    start_time = time.time()

    for i in range(10000):  # Adjust the number of inserts as needed
        name = fake.name()
        birth_date = fake.date_of_birth(minimum_age=18, maximum_age=80)
        
        insert_query_part = f"INSERT INTO {table_name} (name, birthday)"
        insert_query = insert_query_part + " VALUES (%s, %s)"
        values = (name, birth_date)
        cursor.execute(insert_query, values)
        db.commit()

    end_time = time.time()
    elapsed_time = end_time - start_time

    cursor.close()
    db.close()

    return elapsed_time

if __name__ == "__main__":
    for mode in [0, 1, 2]:
        time_taken = perform_inserts(mode)
        print(f"innodb_flush_log_at_trx_commit={mode}: {time_taken:.4f} seconds")
