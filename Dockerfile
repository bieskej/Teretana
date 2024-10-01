version: '3.8'
services:
db:
image: mysql:8.0
container_name:
my_database
environment:
MYSQL_ROOT_PASSWORD:
MYSQL_DATABASE:
mydb:
MYSQL_USER: user
MYSQL_PASSWORD: userpassword
ports:      - "3306:3306"
volumes:      - db_data:/var/lib/mysql      - ./backup.sql:/docker-entrypoint-initdb.d/backup.sqlvolumes:
