FROM openjdk:11-jdk-slim as build

ADD maven/artifacts/vid.war /stage/
WORKDIR /vid
RUN jar -xf /stage/vid.war
RUN rm -f *.war

FROM tomcat:9-jdk11-openjdk-slim

# add vim and uncomment alias to speedup troubleshooting purpose
RUN apt-get update && apt-get install -y \
    curl \
    vim \
    net-tools \
&& rm -rf /var/lib/apt/lists/*

ARG user=onap
ARG group=onap

# Create a group and user
RUN groupadd -r $group && useradd -ms /bin/bash $user -g $group && \
    mkdir /opt/$user && \
    chown -R $user:$group /opt/$user &&  \
    mkdir /var/log/$user && \
    chown -R $user:$group /var/log/$user

# Kubernetes variables
# set to TRUE (in capital) if on Kubernetes
ENV ON_KUBERNETES="FALSE"

# MariaDB variables
ENV VID_MYSQL_HOST="vid-mariadb-docker-instance" \
  VID_MYSQL_PORT="3306" \
  VID_MYSQL_DBNAME="vid_portal" \
  VID_MYSQL_USER="vidadmin" \
  VID_MYSQL_PASS="Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U" \
  VID_MYSQL_MAXCONNECTIONS="5"

# General variables
ENV VID_APP_DISPLAY_NAME="VID" \
  VID_MYLOGIN_FEED_DIRECTORY="/tmp/MyLogins" \
  VID_ROLE_ACCESS_CENTRALIZED="local" \
  VID_DECRYPTION_KEY="AGLDdG4D04BKm2IxIWEr8o=" \
  VID_ECOMP_PORTAL_INBOX_NAME="ECOMP-PORTAL-INBOX"

ENV ROOT_DIR /opt/app/vid
ENV CACHE_DIRECTORY="$ROOT_DIR/cache"

# Keystore variables
ENV VID_KEYSTORE_FILENAME="${ROOT_DIR}/etc/org.onap.vid.jks" \
  VID_TRUSTSTORE_FILENAME="${ROOT_DIR}/etc/org.onap.vid.trust.jks" \
  VID_KEYSTORE_PASSWORD="ry1RLC\(\?M6\?2fQ\]1a2\)2y\{P:" \
  VID_TRUSTSTORE_PASSWORD="OBF:1dx01j0e1hs01t981mis1dws156s1ojc1qjc1zsx1pw31qob1qr71pyj1zst1qhy1ojq156i1dua1mm21tb61hvi1j0g1du2" \
  VID_TOMCAT_PATH="/usr/local/tomcat/conf/" \
  VID_ONAP_FEATURES_PROP="/usr/local/tomcat/conf/onap.features.properties"

# Logging variables
ENV VID_LOG_DIR="${ROOT_DIR}/logs" \
  VID_LOG_LEVEL="INFO" \
  LOG4J_LOGLEVEL="INFO" \
  LOG4J_APPENDER="rollingfile" \
  LOG4J_LOGS_DIRECTORY="${ROOT_DIR}/logs"

# UEB variables
ENV VID_UEB_URL_LIST="ueb.api.simpledemo.onap.org" \
  VID_UEB_CONSUMER_GROUP="VID" \
  VID_UEB_APP_KEY="2Re7Pvdkgw5aeAUD" \
  VID_UEB_APP_SECRET="S31PrbOzGgL4hg4owgtx47Da" \
  VID_UEB_APP_MAILBOX_NAME="ECOMP-PORTAL-OUTBOX-90" \
  VID_UEB_LISTENERS_ENABLE="false"

# SDC variables
ENV ASDC_CLIENT_REST_PROTOCOL="http" \
  ASDC_CLIENT_REST_HOST="c2.vm1.sdc.simpledemo.onap.org" \
  ASDC_CLIENT_REST_PORT="8080" \
  ASDC_CLIENT_REST_AUTH="Basic dmlkOktwOGJKNFNYc3pNMFdYbGhhazNlSGxjc2UyZ0F3ODR2YW9HR21KdlV5MlU=" \
  ASDC_CLIENT_MODEL_NAMESPACE="org.onap" \
  ASDC_CLIENT_TYPE="REST"

# AAI variables
ENV VID_AAI_URL="https://aai.api.simpledemo.onap.org:8443" \
  AAI_TRUSTSTORE_FILENAME="tomcat_keystore" \
  AAI_KEYSTORE_FILENAME="tomcat_keystore" \
  AAI_TRUSTSTORE_PASSWD_X="OBF:1ks51l8d1o3i1pcc1r2r1e211r391kls1pyj1z7u1njf1lx51go21hnj1y0k1mli1sop1k8o1j651vu91mxw1vun1mze1vv11j8x1k5i1sp11mjc1y161hlr1gm41m111nkj1z781pw31kku1r4p1e391r571pbm1o741l4x1ksp" \
  AAI_KEYSTORE_PASSWD_X="OBF:1ks51l8d1o3i1pcc1r2r1e211r391kls1pyj1z7u1njf1lx51go21hnj1y0k1mli1sop1k8o1j651vu91mxw1vun1mze1vv11j8x1k5i1sp11mjc1y161hlr1gm41m111nkj1z781pw31kku1r4p1e391r571pbm1o741l4x1ksp" \
  AAI_VID_PASSWD_X="OBF:1jm91i0v1jl9" \
  AAI_VID_UID="VID" \
  AAI_USE_CLIENT_CERT="false" \
  AAI_VNF_PROV_STATUS="PREPROV,NVTPROV,PROV,CAPPED"

# SO environment variables
ENV VID_MSO_SERVER_URL="http://vm1.mso.simpledemo.onap.org:8080/onap/so/infra" \
  MSO_DME2_SERVER_URL="http://localhost:8081" \
  VID_MSO_PASS="OBF:1ih71i271vny1yf41ymf1ylz1yf21vn41hzj1icz" \
  MSO_DME2_CLIENT_TIMEOUT="10000" \
  MSO_DME2_CLIENT_READ_TIMEOUT="10000" \
  MSO_POLLING_INTERVAL_MSECS="10000" \
  VID_MSO_USER="InfraPortalClient" \
  MSO_DME2_ENABLED="false"

# Portal variables
ENV VID_CONTACT_US_LINK="https://todo_contact_us_link.com" \
  VID_ECOMP_REDIRECT_URL="http://portal.api.simpledemo.onap.org:8989/ONAPPORTAL/login.htm" \
  VID_ECOMP_REST_URL="http://portal.api.simpledemo.onap.org:8989/ONAPPORTAL/auxapi" \
  VID_ECOMP_SHARED_CONTEXT_REST_URL="http://portal.api.simpledemo.onap.org:8989/ONAPPORTAL/context"

# Custom options
ENV JAVA_OPTS="-Xmx1536m -Xms1536m"

ADD maven/config/server.xml ${VID_TOMCAT_PATH}
ADD maven/config/onap.features.properties ${VID_ONAP_FEATURES_PROP}
ADD maven/scripts/*.sh /tmp/vid/
RUN chown $user:$group /tmp/vid /usr/local/tomcat -R

COPY --chown=$user:$group --from=build /vid /usr/local/tomcat/webapps/vid/

RUN chmod +x /tmp/vid/localize.sh
USER $user
CMD ["/tmp/vid/localize.sh"]
