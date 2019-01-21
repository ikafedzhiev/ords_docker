FROM java:8
MAINTAINER ika@melexis.com

USER root

ADD ords /app/ords
ADD configs/ords /app/ords/ords
ADD configs/params /app/ords/params
ADD templates /app/templates
ADD ords_run.sh /app/ords_run.sh

CMD ["/app/ords_run.sh"]

