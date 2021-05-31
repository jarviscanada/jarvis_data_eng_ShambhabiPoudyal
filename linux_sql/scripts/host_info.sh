#!/bin/bash

#assigning command line arguments to variables for future reference
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

# checking number of required fields
if [ "$#" -ne 5 ];then
        echo "Error invalid number of arguments"
fi

# assigning bash command for memory and CPU information to variables
memory=`cat /proc/meminfo`
hostname=$(hostname -f)
lscpu_out=`lscpu`

# formatting the data for inserting into database tables
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out"  | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out"  | egrep "^Model name:" | awk '{$1="";$2="";print}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "^CPU MHz:" | awk '{$1="";$2="";print}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "^L2 cache:" | awk '{$1="";$2="";print ($0+0)}' | xargs)
total_mem=$(echo "$memory"  | egrep "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=`date -u "+%Y-%m-%d %H:%M:%S"`


# populating the host_info table
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp) 
VALUES('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$total_mem', '$timestamp');"
#insert_stmt="select * from host_info"

# connecting to psql instance with given host(h), port number(p), user(U), database(d) and database command(c)
psql -h "$hostname" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"

exit $?
