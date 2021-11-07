FROM bellsoft/liberica-openjdk-alpine-musl
COPY ./target/TinkoffStockService-0.0.1-SNAPSHOT.jar .
CMD ["java","-jar","TinkoffStockService-0.0.1-SNAPSHOT.jar"]