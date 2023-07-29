from faker import Faker
import mysql.connector
import sys

num_fake_records = 4000000
# create a new Faker instance
fake = Faker()
table_name = sys.argv[1]

# create a connection to the MySQL database
cnx = mysql.connector.connect(
      user='root', 
      password='password',
      port=3306,
      host='127.0.0.1', 
      database='homework9_db'
    )
cursor = cnx.cursor()

def generate_fake_data(num_records):
    batch_size = 100000
    batches = num_records // batch_size

    for batch in range(batches):
        records = []
        for _ in range(batch_size):
            name = fake.name()
            birth_date = fake.date_of_birth(minimum_age=18, maximum_age=80)
            # Add more fields as needed

            # Modify the SQL query based on your table structure
            records.append((name, birth_date))

        insert_query_part = f"INSERT INTO {table_name} (name, birthday)"
        sql = insert_query_part + " VALUES (%s, %s)"
        cursor.executemany(sql, records)
        cnx.commit()
        print("Inserted %d from %d" % (batch*batch_size, num_fake_records))

    cursor.close()
    cnx.close()

generate_fake_data(num_fake_records)