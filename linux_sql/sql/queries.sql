\c host_agent

--using window function over(partition by...order by...)
SELECT cpu_number, 
	id, 
	total_mem 
FROM host_info
GROUP BY cpu_number, id
ORDER BY total_mem DESC
LIMIT 5;

--avg memory % using group by, function for rounding timestamp
CREATE OR REPLACE FUNCTION round5(ts timestamp) RETURNS timestamp AS
$$
BEGIN 
	RETURN date_trunc('hour', ts) + date_part('minute', ts):: int/5*interval '5min';
END;
$$
LANGUAGE PLPGSQL;

SELECT id, hostname, round_t, avg_used_mem_percentage
FROM (
	SELECT host_id, round5(host_usage.timestamp) AS round_t, AVG((total_mem - memory_free*1024)/total_mem) AS avg_used_mem_percentage
	FROM host_usage 
	INNER JOIN host_info ON host_usage.host_id = host_info.id
	GROUP BY round_t, host_id
)t INNER JOIN host_info ON t.host_id=host_info.id
LIMIT 5;

--host failure detection
SELECT host_id, timestamp,
	COUNT(timestamp) OVER(PARTITION BY round5(timestamp)) AS num_data_points 
FROM host_usage
ORDER BY timestamp DESC
LIMIT 5;
