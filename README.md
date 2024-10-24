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

## Set up SSL

```
DIR=$PWD/certs
mkdir -p ${DIR}

# Create CA certificate
openssl req -new -nodes -out ${DIR}/ca.csr -keyout ${DIR}/ca.key -subj "/CN=@making/O=LOL.MAKI/C=JP"
chmod og-rwx ${DIR}/ca.key

cat <<EOF > ${DIR}/ext_ca.txt
basicConstraints=CA:TRUE
keyUsage=digitalSignature,keyCertSign
EOF

openssl x509 -req -in ${DIR}/ca.csr -days 3650 -signkey ${DIR}/ca.key -out ${DIR}/ca.crt -extfile ${DIR}/ext_ca.txt

cat <<EOF > ${DIR}/ext.txt
basicConstraints=CA:FALSE
keyUsage=digitalSignature,dataEncipherment,keyEncipherment,keyAgreement
extendedKeyUsage=serverAuth,clientAuth
EOF

# Create Server certificate signed by CA
openssl req -new -nodes -out ${DIR}/server.csr -keyout ${DIR}/server.key -subj "/CN=localhost"
chmod og-rwx ${DIR}/server.key
openssl x509 -req -in ${DIR}/server.csr -days 3650 -CA ${DIR}/ca.crt -CAkey ${DIR}/ca.key -CAcreateserial -out ${DIR}/server.crt -extfile ${DIR}/ext.txt

# Create Server certificate signed by CA
openssl req -new -nodes -out ${DIR}/127.0.0.1.csr -keyout ${DIR}/127.0.0.1.key -subj "/CN=127.0.0.1"
chmod og-rwx ${DIR}/127.0.0.1.key
openssl x509 -req -in ${DIR}/127.0.0.1.csr -days 3650 -CA ${DIR}/ca.crt -CAkey ${DIR}/ca.key -CAcreateserial -out ${DIR}/127.0.0.1.crt -extfile ${DIR}/ext.txt
```

```
./mvnw install -pl autoconfigure
./mvnw spring-boot:run -pl backend -Dspring-boot.run.arguments="--spring.docker.compose.file=$(pwd)/compose.yml --server.ssl.bundle=127-0-0-1 --server.ssl.server-name-bundles[0].server-name=localhost --server.ssl.server-name-bundles[0].bundle=self-signed --spring.ssl.bundle.pem.127-0-0-1.keystore.certificate=$(pwd)/certs/127.0.0.1.crt --spring.ssl.bundle.pem.127-0-0-1.keystore.private-key=$(pwd)/certs/127.0.0.1.key --spring.ssl.bundle.pem.127-0-0-1.reload-on-update=true --spring.ssl.bundle.pem.self-signed.keystore.certificate=$(pwd)/certs/server.crt --spring.ssl.bundle.pem.self-signed.keystore.private-key=$(pwd)/certs/server.key --spring.ssl.bundle.pem.self-signed.reload-on-update=true"
./mvnw spring-boot:run -pl frontend -Dspring-boot.run.arguments="--spring.docker.compose.file=$(pwd)/compose.yml --server.ssl.bundle=self-signed --spring.ssl.bundle.pem.self-signed.keystore.certificate=$(pwd)/certs/server.crt --spring.ssl.bundle.pem.self-signed.keystore.private-key=$(pwd)/certs/server.key --spring.ssl.bundle.pem.self-signed.reload-on-update=true --spring.ssl.bundle.pem.client.truststore.certificate=$(pwd)/certs/ca.crt --server.port=8443 --backend.url=https://localhost:8082"
```


```
curl https://localhost:8082 --cacert certs/ca.crt
curl https://127.0.0.1:8082 --cacert certs/ca.crt
```