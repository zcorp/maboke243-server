#!/bin/bash

fonctiondetest() {
   echo "Hello world !" $1
}

clean() {
    docker compose down -v &&
    docker image prune -a -f &&
    ./mvnw clean
}

run() {
    #clean
    docker build -t maboke-server . --target dev
    docker compose up
}

build() {
    docker build --platform=linux/amd64 -t dez28/maboke243-server:latest .
}

push() {
    build
    docker login -u dez28
    docker push dez28/maboke243-server:latest
}

test() {
    clean
    docker build -t maboke-server . --target test 
    docker compose up
}

db_connect() {
    mongosh mongodb://127.0.0.1:27018/maboke -u $MONGO_USER -p $MONGO_PASSWORD --authenticationDatabase admin
}

db_dump() {
    mongodump mongodb://127.0.0.1:27018/maboke -u $MONGO_USER -p $MONGO_PASSWORD --authenticationDatabase admin --out=./db/dump
}

db_restore() {
    mongorestore mongodb://127.0.0.1:27018/maboke --drop -u $MONGO_USER -p $MONGO_PASSWORD --authenticationDatabase admin ./db/dump/maboke
}

if [[ "$1" == "default" ]];then
fonctiondetest
elif [[ "$1" == "test" ]];then
test
elif [[ "$1" == "run" ]];then
run
elif [[ "$1" == "build" ]];then
build
elif [[ "$1" == "push" ]];then
push
elif [[ "$1" == "clean" ]];then
clean
elif [[ "$1" == "dbconnect" ]];then
db_connect
elif [[ "$1" == "dbdump" ]];then
db_dump
elif [[ "$1" == "dbrestore" ]];then
db_restore
fi
#run