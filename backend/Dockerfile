FROM maven:3.9.6-amazoncorretto-17-al2023

COPY pom.xml .
COPY src ./src

RUN mvn clean install

CMD ["java", "-jar", "target/KhNURE-timetable-synchronizer-0.0.1-SNAPSHOT.jar"]