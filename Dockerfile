FROM maven:3.6.3-openjdk-11

COPY . /opt/app
WORKDIR /opt/app/Java
RUN mvn clean install -DskipTests
RUN mvn -pl recognizers-service install

CMD mvn -pl recognizers-service mn:run
