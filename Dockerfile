FROM clojure

WORKDIR /usr/src/app

COPY project.clj .

RUN lein deps

COPY . .

RUN mv "$(lein ring uberjar | sed -n 's/^Created \(.*standalone\.jar\)/\1/p')" app-standalone.jar


FROM openjdk:alpine

RUN apk --no-cache add ca-certificates

WORKDIR /root/

COPY --from=0 /usr/src/app/app-standalone.jar app-standalone.jar

EXPOSE 3000

CMD ["java", "-jar", "app-standalone.jar"]
