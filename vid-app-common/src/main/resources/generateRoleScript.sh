firstRun=${1-false}
url=http://127.0.0.1:8080/generateRoleScript/$firstRun
wget -O roles.sql $url