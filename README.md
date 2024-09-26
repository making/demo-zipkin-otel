# Demo Zipkin OTEL


```
./mvnw install -pl autoconfigure
./mvnw spring-boot:run -pl backend -Dspring-boot.run.arguments=--spring.docker.compose.file=$(pwd)/compose.yml
./mvnw spring-boot:run -pl frontend -Dspring-boot.run.arguments=--spring.docker.compose.file=$(pwd)/compose.yml
```

Go to http://localhost:8080

Then go to Zipkin (http://localhost:9411)

<img width="1024" alt="image" src="https://github.com/user-attachments/assets/57d07fb0-99e4-4ec5-bb13-353ba04df24d">

<img width="1024" alt="image" src="https://github.com/user-attachments/assets/357d0c8e-eaaf-4b4e-a6ba-1467a7fde778">