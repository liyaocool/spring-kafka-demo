# 本项目简介

docker compose 部署 zookeeper, kafka内外网环境集群，nginx代理

# 运行环境 docker compose 部署内外网kafka集群和nginx反向代理

## 1. docker-compose.yml文件

```yml


version: "3.8"

services:
  zookeeper:
    container_name: zookeeper
    hostname: zookeeper
    restart: always
    image: 'bitnami/zookeeper:3.8.3'
    # 内网不再暴露端口，nginx接管
    # ports:
    #   - '2181:2181'
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes

  kafka_1:
    container_name: kafka_1
    hostname: kafka_1
    restart: always
    image: 'bitnami/kafka:3.6.0'
    # 内网不再暴露端口，nginx接管
    # ports:
    #   - '9093:9092'
    environment:
      - KAFKA_BROKER_ID=1
      - KAFKA_NUM_PARTITIONS=3
      - KAFKA_DEFAULT_REPLICATION_FACTOR=2
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      # 监听设置 内网为hostname:9092, 外网为localhost+不同端口，然后nginx接管并暴露外网端口
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=INTERNAL
      - KAFKA_CFG_LISTENERS=INTERNAL://:9092,EXTERNAL://0.0.0.0:9093
      - KAFKA_CFG_ADVERTISED_LISTENERS=INTERNAL://kafka_1:9092,EXTERNAL://localhost:9093
    volumes:
      - ./kafka_1:/bitnami/kafka
    depends_on:
      - zookeeper

  kafka_2:
    container_name: kafka_2
    hostname: kafka_2
    restart: always
    image: 'bitnami/kafka:3.6.0'
    # ports:
    #   - '9094:9092'
    environment:
      - KAFKA_BROKER_ID=2
      - KAFKA_NUM_PARTITIONS=3
      - KAFKA_DEFAULT_REPLICATION_FACTOR=2
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      # Listeners
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=INTERNAL
      - KAFKA_CFG_LISTENERS=INTERNAL://:9092,EXTERNAL://0.0.0.0:9094
      - KAFKA_CFG_ADVERTISED_LISTENERS=INTERNAL://kafka_2:9092,EXTERNAL://localhost:9094
    volumes:
      - ./kafka_2:/bitnami/kafka
    depends_on:
      - zookeeper

  kafka_3:
    container_name: kafka_3
    hostname: kafka_3
    restart: always
    image: 'bitnami/kafka:3.6.0'
    # ports:
    #   - '9095:9092'
    environment:
      - KAFKA_BROKER_ID=3
      - KAFKA_NUM_PARTITIONS=3
      - KAFKA_DEFAULT_REPLICATION_FACTOR=2
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      # Listeners
      - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT
      - KAFKA_CFG_INTER_BROKER_LISTENER_NAME=INTERNAL
      - KAFKA_CFG_LISTENERS=INTERNAL://:9092,EXTERNAL://0.0.0.0:9095
      - KAFKA_CFG_ADVERTISED_LISTENERS=INTERNAL://kafka_3:9092,EXTERNAL://localhost:9095

    volumes:
      - ./kafka_3:/bitnami/kafka
    depends_on:
      - zookeeper

  nginx:
    container_name: nginx
    hostname: nginx
    image: nginx
    ports:
      - '9093-9095:9093-9095'
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - kafka_1
      - kafka_2
      - kafka_3

```

## 2. nginx.conf 配置文件

```bash

user  nginx;
worker_processes  auto;

error_log  /var/log/nginx/error.log notice;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


stream {
    upstream stream_kafka_1    {
        server  kafka_1:9093;
    }

    upstream stream_kafka_2    {
        server  kafka_2:9094;
    }

    upstream stream_kafka_3    {
        server  kafka_3:9095;
    }

    server {
        listen  9093;
        proxy_pass  stream_kafka_1;
    }

    server {
        listen  9094;
        proxy_pass  stream_kafka_2;
    }

    server {
        listen  9095;
        proxy_pass  stream_kafka_3;
    }
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;

    include /etc/nginx/conf.d/*.conf;
}

```

# 启动项目 

- 访问调用生产者 `localhost:7070/p/send?msg=hello`
- 查看后台消费者记录