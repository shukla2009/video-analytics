CREATE KEYSPACE IF NOT EXISTS assign WITH replication = {'class': 'SimpleStrategy',
        'replication_factor': '1'}  AND durable_writes = true;

USE assign;

CREATE TABLE IF NOT EXISTS video_analytics (
    video_id text,
    user_id text,
    duration double,
    ts bigint,
    PRIMARY KEY (video_id, user_id));


CREATE MATERIALIZED VIEW IF NOT EXISTS mv_video_analytics
AS SELECT * FROM video_analytics
WHERE video_id IS NOT NULL AND user_id IS NOT NULL AND duration IS NOT NULL
PRIMARY KEY (video_id,duration,user_id);
