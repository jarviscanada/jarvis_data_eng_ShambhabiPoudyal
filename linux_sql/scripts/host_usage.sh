#!/bin/bash

#assigning command line arguments to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#checking the valid number of arguments
if [ "$#" -ne 5 ];then
        echo "Error invalid number of arguments"
	exit 1
fi

#vm usage data
hostname=$(hostname -f)
usage=`vmstat -s --unit M`
usage2=`vmstat`
usage_disk=`vmstat -D`
usage_disk_space=`df -BM /`

#formatting as per host_usage table
memory_free=$(echo "$usage"  | egrep "free memory" | awk '{print $1}')
cpu_idle=$(echo "$usage2" | awk '{ for (i=1; i<=NF; i++) if ($i=="id") { getline; print $i }}')
cpu_kernel=$(echo "$usage2" | awk '{ for (i=1; i<=NF; i++) if ($i=="sy") { getline; print $i }}')
disk_io=$(echo "$usage_disk" | grep 'inprogress IO' | awk '{print $1}')
disk_available=$(echo "$usage_disk_space" | awk '{ for (i=1; i<=NF; i++) if ($i=="Available") { getline; print ($i+0) }}')
timestamp=`date -u "+%Y-%m-%d %H:%M:%S"`

#populating the table
insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
VALUES ('$timestamp', (SELECT distinct id FROM host_info where host_info.hostname = hostname limit 1) , '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

export PGPASSWORD='password'
psql -h "$hostname" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"

exit $?
