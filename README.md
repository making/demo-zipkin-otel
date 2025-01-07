# Demo Zipkin OTEL


```
./mvnw install -pl autoconfigure
./mvnw spring-boot:run -pl backend -Dspring-boot.run.arguments=--spring.docker.compose.file=$(pwd)/compose.yml
./mvnw spring-boot:run -pl frontend -Dspring-boot.run.arguments=--spring.docker.compose.file=$(pwd)/compose.yml
```

Go to http://localhost:8080

Then go to Zipkin (http://localhost:9411)

<img width="1024" alt="image" src="https://github.com/user-attachments/assets/bd0e74b0-ab77-4d38-9caa-4a9610a79e04" />

<img width="1024" alt="image" src="https://github.com/user-attachments/assets/37cda939-47c3-45c0-98d4-df5ebcee4397" />
