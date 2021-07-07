#!/bin/bash
#docker container rm jrvs-psql

container_status=$(docker container ls -a -f name=jrvs-psql | wc -l)
docker_command=$1
db_username=$2
db_password=$3

if [ $# -ne 3 ]; then
  echo "Error: invalid number of arguments"
  exit 1
fi
 
sudo systemctl status docker || systemctl start docker

case $docker_command in
create)
  if [ "$container_status" == "" ]; then
    echo "Error: container already created"
    exit 1
  elif [ "$db_username" == "" ]||[ "$db_password" == "" ]; then 
    echo "Error: username/password not passed"
    exit 1
  fi

  docker volume create pgdata
  export PGPASSWORD='password'
  docker run --name jrvs-psql -e POSTGRES_PASSWORD=${db_password} -e POSTGRES_USER=${db_username} -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
  exit $?
;;

start)
  if [ $container_status -ne 2 ]; then
    echo "Error:container not created"
    exit 1
  else
    docker container start jrvs-psql
    exit $?
  fi
;;

stop)
  if [ $container_status -ne 2 ]; then
    echo "Error:container not created"
    exit 1
  else
    docker container stop jrvs-psql
    exit $?
  fi
;;

*)
  echo "Error: unknown command"
  exit 1  
;;
  
esac





