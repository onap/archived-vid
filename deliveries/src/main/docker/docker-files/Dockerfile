FROM tomcat:8.0-jre8
# add vim and uncomment alias to speedup troubleshooting purpose
RUN apt-get update && apt-get install -y \
	openjdk-8-jdk vim

RUN sed -i 's/# alias/alias/g' /root/.bashrc


ENV ROOT_DIR /opt/app/vid

ENV ASDC_CLIENT_TYPE REST
ENV ASDC_CLIENT_REST_HOST c2.vm1.sdc.simpledemo.openecomp.org
#old creds 1/9/2017 ENV ASDC_CLIENT_REST_AUTH Basic dmlkOnZpZG9z
ENV ASDC_CLIENT_REST_AUTH Basic dmlkOktwOGJKNFNYc3pNMFdYbGhhazNlSGxjc2UyZ0F3ODR2YW9HR21KdlV5MlU=
ENV ASDC_CLIENT_REST_PROTOCOL http
ENV ASDC_CLIENT_REST_PORT 8080
ENV ASDC_CLIENT_MODEL_NAMESPACE org.openecomp

ENV CACHE_DIRECTORY $ROOT_DIR/cache

ENV LOG4J_LOGLEVEL INFO
ENV LOG4J_APPENDER rollingfile
ENV LOG4J_LOGS_DIRECTORY ${ROOT_DIR}/logs

ENV VID_AAI_HOST aai.api.simpledemo.openecomp.org
ENV VID_AAI_PORT 8443
ENV VID_APP_DISPLAY_NAME VID
ENV VID_ECOMP_SHARED_CONTEXT_REST_URL https://portal.api.simpledemo.openecomp.org:8080/onapportal/context
ENV VID_MSO_SERVER_URL http://vm1.mso.simpledemo.openecomp.org:8080
ENV VID_TRUSTSTORE_FILE ${ROOT_DIR}/etc/vid_keystore.jks
ENV VID_MYLOGIN_FEED_DIRECTORY /tmp/MyLogins
ENV VID_TRUSTSTORE_PASS 5c84906b14912dbe96154e09b1e1db90e31f8dedb08e3e6c9f17da3f926cb67b33e4229cc1fe0f59c996d2e38b8ded42
ENV VID_MSO_USER InfraPortalClient
ENV VID_MSO_PASS OBF:1ih71i271vny1yf41ymf1ylz1yf21vn41hzj1icz
ENV MSO_DME2_CLIENT_TIMEOUT 10000
ENV MSO_DME2_CLIENT_READ_TIMEOUT 10000
ENV MSO_DME2_SERVER_URL http://localhost:8081
ENV MSO_DME2_ENABLED false
ENV MSO_POLLING_INTERVAL_MSECS 10000
ENV AAI_TRUSTSTORE_FILENAME tomcat_keystore
ENV AAI_TRUSTSTORE_PASSWD_X OBF:1ks51l8d1o3i1pcc1r2r1e211r391kls1pyj1z7u1njf1lx51go21hnj1y0k1mli1sop1k8o1j651vu91mxw1vun1mze1vv11j8x1k5i1sp11mjc1y161hlr1gm41m111nkj1z781pw31kku1r4p1e391r571pbm1o741l4x1ksp
ENV AAI_KEYSTORE_FILENAME tomcat_keystore
ENV AAI_KEYSTORE_PASSWD_X OBF:1ks51l8d1o3i1pcc1r2r1e211r391kls1pyj1z7u1njf1lx51go21hnj1y0k1mli1sop1k8o1j651vu91mxw1vun1mze1vv11j8x1k5i1sp11mjc1y161hlr1gm41m111nkj1z781pw31kku1r4p1e391r571pbm1o741l4x1ksp
ENV AAI_VID_UID VID
ENV AAI_VID_PASSWD_X OBF:1jm91i0v1jl9
ENV AAI_USE_CLIENT_CERT false
ENV AAI_VNF_PROV_STATUS PREPROV,NVTPROV,PROV,CAPPED
ENV VID_ECOMP_REDIRECT_URL  http://portal.api.simpledemo.openecomp.org:8989/ONAPPORTAL/login.htm
ENV VID_ECOMP_REST_URL http://portal.api.simpledemo.openecomp.org:8989/ONAPPORTAL/auxapi
ENV VID_ECOMP_SHARED_CONTEXT_REST_URL http://portal.api.simpledemo.openecomp.org:8989/ONAPPORTAL/context
ENV VID_CONTACT_US_LINK https://todo_contact_us_link.com

ENV VID_DECRYPTION_KEY AGLDdG4D04BKm2IxIWEr8o=
ENV VID_UEB_URL_LIST ueb.api.simpledemo.openecomp.org 
ENV VID_UEB_CONSUMER_GROUP VID
ENV VID_ECOMP_PORTAL_INBOX_NAME ECOMP-PORTAL-INBOX
ENV VID_UEB_APP_KEY 2Re7Pvdkgw5aeAUD
ENV VID_UEB_APP_SECRET S31PrbOzGgL4hg4owgtx47Da
ENV VID_UEB_APP_MAILBOX_NAME ECOMP-PORTAL-OUTBOX-90
ENV VID_UEB_LISTENERS_ENABLE false

ENV VID_LOG_DIR ${ROOT_DIR}/logs
ENV VID_LOG_LEVEL INFO

ENV VID_MYSQL_HOST vid-mariadb-docker-instance
ENV VID_MYSQL_PORT 3306
ENV VID_MYSQL_DBNAME vid_portal
ENV VID_MYSQL_USER vidadmin
ENV VID_MYSQL_MAXCONNECTIONS 5
ENV VID_MYSQL_PASS Kp8bJ4SXszM0WXlhak3eHlcse2gAw84vaoGGmJvUy2U

ADD maven/scripts/*.sh /tmp/vid/
ADD maven/artifacts/vid.war /tmp/vid/stage/

RUN chmod +x /tmp/vid/localize.sh

CMD ["/tmp/vid/localize.sh"]
