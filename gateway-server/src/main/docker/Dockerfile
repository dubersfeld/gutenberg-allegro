FROM openjdk:8-jdk-alpine
VOLUME /tmp

ADD zuul-server-0.1.0-SNAPSHOT.jar app.jar

EXPOSE 5555

ADD wrapper.sh wrapper.sh
RUN chmod +x wrapper.sh

ENTRYPOINT ["/bin/sh", "/wrapper.sh"]
