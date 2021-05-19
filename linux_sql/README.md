# Introduction
This project is designed to manage a Linux cluster of a number of servers running on CentOS 7. The connection among the servers is via a switch and internal IPv4 addressing is used for internal communication. Here, the hardware specifications and usage data of each node is calculated and stored in a database which can be further queried to analyze the data for future references.<br/>
This project design is suitable for small companies with their own network of servers which need to be continuously monitored for acquiring information about resource usage. A VM instance is created in GCP and connected via SSH. VNC viewer is used to remotely control the desktop environment. Bash scripting is used for all the source codes. A docker engine is used for easier implementation purposes. Postgresql is used for the database management on docker container. 



# Quick Start
- Start a psql instance using psql\_docker.sh <br />
 ```bash ./scripts/psql\_docker.sh start jrvs-psql password```
- Create tables using psql_docker.sh <br />
 ```psql -h localhost -U postgres -d host_agent -f ./sql/ddl.sql```
- Insert hardware specs data into the database using host_info.sh <br />
 ```bash ./scripts/host_info.sh localhost 5432 host_agent postgres password```
- Insert hardware usage data into the database using host_usage.sh <br />
 ```bash ./scripts/host_usage.sh localhost 5432 host_agent postgres password```
- Crontab setup <br />
 - add crontab <br />
   ```crontab -e```
 - crontab content <br />
   ```\* \* \* \* \* bash /home/centos/dev/jarvis_data_eng_Shambhabi/linux_sql/scripts/host_usage.sh localhost 5432 host_agent postgres password &> /tmp/host_usage.log```
 - check crontab list <br />
   ```crontab -l```
   
   
# Implementation
Three shell scripts namely psql_docker.sh, host_info.sh and host_usage.sh have been implemented for purposes described below. ddl.sql and queries.sql are the postgresql files made use for the database management. 
## Architecture
https://github.com/jarviscanada/jarvis_data_eng_ShambhabiPoudyal/blob/develop/linux_sql/assets/architecture.png

## Scripts
- psql_docker.sh
 <br/>This script creates a docker container, if it is not existing, with the database username and password passed as command line arguments. <br />
```./scripts/psql_docker.sh create db_username db_password``` <br />
Then, the script provisions to start or stop the docker container as per the command passed from the command line.<br />
```./scripts/psql_docker.sh start db_username db_password``` <br />
```./scripts/psql_docker.sh stop db_username db_password``` <br />

- host_info.sh
 <br/>This script is used to collect the hardware specifications data. ```meminfo``` and ```lscpu``` are used to extract the information about the CPU and memory. The output of these commands are parsed and formatted to obtain the desired data in formats acceptable by the corresponding database tables and inserts the data into the same. <br/>
```bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password```

- host_usage.sh
<br/>This script collects the CPU and memory usage data, parses them and formats to desired forms and inserts into corresponding database table. <br/>
```bash scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password```

- crontab <br/>
A crontab has been managed to view the usage data every minute.<br/>

- queries.sql <br/>
This sql file uses sql queries to answer differnt business questions that might arise. This script answers questions like the occassion of host failure or the average memory used by each host over 5mins interval or groupping the hosts based on the hardware information. <br/>
 ```psql -h psql_host -U psql_user -d db_name -f ./sql/queries.sql```

## Database Modeling
The schema for the database table are as shown below\:
- `host_info` <br/>

|host_info|||
|:---:|:---:|:---:| 
| id| SERIAL  |PK|
|  hostname  | VARCHAR ||
| cpu_number    |    INTEGER || 
| cpu_architecture |  VARCHAR| |
| cpu_model| VARCHAR  ||
|  cpu_mhz |  REAL  ||
| l2_cache|  INTEGER || 
| total_mem|INTEGER   ||
|  "timestamp" |   TIMESTAMP  | |



- `host_usage` <br/>

| host_usage   |  |  | 
| :-------------: | :----------: |:----------: |
|  "timestamp" |TIMESTAMP ||
 |       host_id | SERIAL| FK|
  |      memory_free | INTEGER| |
       | cpu_idle |INTEGER||
        |cpu_kernel| INTEGER| |
        |disk_io| INTEGER ||
       |disk_available| INTEGER | |



# Test
- psql_docker.sh
<br/> ```bash ./scripts/psql\_docker.sh create jrvs-psql password```
<br/> Output\: displays docker volume name and docker container id
<br/> ```bash ./scripts/psql\_docker.sh start jrvs-psql password```
<br/> Output\: displays docker container name
<br/> ```bash ./scripts/psql\_docker.sh stop jrvs-psql password```
<br/> Output\: displays docker container name
- host_info.sh
<br/> ```bash ./scripts/host_info.sh localhost 5432 host_agent postgres password```
<br/> Output\: displays INSERT 0 1 representing (INSERT oid count) psql output, where count is the number of rows inserted (here 1)
- host_usage.sh
<br/>```bash ./scripts/host_usage.sh localhost 5432 host_agent postgres password```
<br/> Output\: displays INSERT 0 1 representing (INSERT oid count) psql output, where count is the number of rows inserted (here 1)
- queries.sql
<br/> ```psql -h localhost -U postgres -d host_agent -f ./sql/queries.sql```
<br/> Output\: displays ...connected to database engine "host_agent" as user "postgres"
<br/> A table displaying cpu_number, id and total_mem sorted by the memory size in descending order
<br/> Second table displaying id, hostname, rount_t, avg_used_mem_percentage over 5mins interval for each host
<br/> Third table displaying host_id, timestamp, num_data_points: verified the output by checking the last entries which showed less than 3 data points because the server is running and the 5mins interval is not yet completed which can be visualized as case when the server has stopped working thereby detecting host failure

#   Improvements
- create multiple hosts and collect information for each host 
- create dummy database test set to visualize the output properly 
- handle hardware update more efficiently


