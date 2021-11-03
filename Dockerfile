FROM bellsoft/liberica-openjdk-alpine-musl
COPY ./target/stockAPI-0.0.1-SNAPSHOT.jar .
CMD ["java","-jar","stockAPI-0.0.1-SNAPSHOT.jar"]