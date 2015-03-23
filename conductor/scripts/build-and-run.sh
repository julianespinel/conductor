cd ..
mvn clean compile test package install
java -jar target/conductor-0.1.jar server conductor-develop.yml &
