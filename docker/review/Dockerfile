FROM review-server:0.0.1-SNAPSHOT

USER root
RUN apt-get update && apt-get install -y netcat

ADD wrapper.sh wrapper.sh

RUN chmod +x wrapper.sh

EXPOSE 8082

ENTRYPOINT ["/bin/sh", "/wrapper.sh"]

