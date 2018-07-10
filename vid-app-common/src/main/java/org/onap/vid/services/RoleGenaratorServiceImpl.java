package org.onap.vid.services;

import jline.internal.Log;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.ServiceSubscription;
import org.onap.vid.aai.Services;
import org.onap.vid.model.ModelConstants;
import org.onap.vid.model.Subscriber;
import org.onap.vid.model.SubscriberList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RoleGenaratorServiceImpl implements RoleGeneratorService {

    public static final String ROLE_ID_COLUMN = "ROLE_ID";
    @Autowired
    AaiClientInterface client;

    public static final String DB_NAME =  "vid_portal";
    public static final String TBL_NAME = "fn_role";
    public static final String TEMP_DELIMITER ="***";
    public static final String OLD_DELIMITER = "_";

    @Override
    public String generateRoleScript(Boolean firstRun) {
        String query =  "USE " + DB_NAME + ";\r\n" +
                "SET SQL_SAFE_UPDATES = 0;\r\n";
        try {
            AaiResponse<SubscriberList> subscribers = client.getAllSubscribers();
            if (firstRun) {
                query += replaceRolesToTempDelimiter("subscriber",buildSubscribersValuesForMappingsTable(subscribers.getT()));
            }
            query += addAvailableRolesCombination(firstRun, subscribers);

        }
        catch (Exception e) {
            Log.error("There was an error in updating roles "+e.getMessage());
        }
        return query;
    }

    private String addAvailableRolesCombination(Boolean firstRun, AaiResponse<SubscriberList> subscribers) {
        String query, availableRoles="";
        HashMap<String,String> servicesNames = new HashMap<String,String>();
        for (Subscriber subscriber: subscribers.getT().customer) {
            AaiResponse<Services> subscriberResponse = client.getSubscriberData(subscriber.globalCustomerId);
            for(ServiceSubscription service: subscriberResponse.getT().serviceSubscriptions.serviceSubscription) {
                servicesNames.put(service.serviceType,"");
                String roleName = "'" + subscriber.subscriberName + ModelConstants.ROLE_DELIMITER + service.serviceType + "'";
                availableRoles += "("+roleName+"),";


            }
        }
        availableRoles = availableRoles.substring(0,availableRoles.length()-1);
        query = createTemporaryTableAvailableRoles(availableRoles);
        if (firstRun){
            query += replaceRolesToTempDelimiter("service",buildServicesValuesForMappingsTable(servicesNames));
            query += replaceToNewDelimiter();
            query += deleteWrongRecords();

        }
        query += insertAvailableRolesToFnRole();
        query += dropTemporaryTable("available_roles");
        return query;
    }

    private String buildSubscribersValuesForMappingsTable(SubscriberList subscribers){
        String query="";
        for (Subscriber subscriber : subscribers.customer) {
            String subscriberName = subscriber.subscriberName.contains(OLD_DELIMITER) ? subscriber.subscriberName.replace(OLD_DELIMITER, TEMP_DELIMITER) : subscriber.subscriberName;
            query = query + "('" + subscriber.globalCustomerId + "','" + subscriberName + "') ,";
        }
        if(query.length() > 0)
            query = query.substring(0, query.length()-1) + ";\r\n";
        return query;
    }

    private String buildServicesValuesForMappingsTable(HashMap<String,String> servicesNames){
        final String[] query = {""};
        servicesNames.forEach((k,v)->{
            if (k.contains(OLD_DELIMITER)) {
                query[0] += "('" + k + "' ,'" + k.replace(OLD_DELIMITER, TEMP_DELIMITER) +"'),";
            }
        });
        if(query[0].length() > 0)
            query[0] = query[0].substring(0, query[0].length()-1) + ";\r\n";
        return query[0];
    }

    private String replaceRolesToTempDelimiter(String entityName, String valuesForMappingsTable ) {

        AaiResponse<Services> services = client.getServices();
        String query = "";
        if (valuesForMappingsTable.length() > 0) {
            query = "CREATE TEMPORARY TABLE IF NOT EXISTS " + entityName + "Mappings(mapKey VARCHAR(255),mapValue VARCHAR(255));\r\n" +
                    "INSERT INTO " + entityName + "Mappings VALUES ";
            query += valuesForMappingsTable;
            query += "UPDATE " + TBL_NAME + "\r\n" +
                    "INNER JOIN " + entityName + "Mappings ON role_name LIKE concat('%',mapKey, '%')\r\n" +
                    "SET ROLE_NAME = REPLACE(ROLE_NAME, mapKey, mapValue) ;  \r\n" +
                    dropTemporaryTable(entityName + "Mappings");
        }
        return query;
    }

    private String replaceToNewDelimiter(){
        String query =  "UPDATE " + TBL_NAME + "\r\n" +
                "SET ROLE_NAME = REPLACE(ROLE_NAME, '" + OLD_DELIMITER + "', '" + ModelConstants.ROLE_DELIMITER + "');\r\n" ;
        query += "UPDATE fn_role\r\n" +
                "SET ROLE_NAME = REPLACE(ROLE_NAME, '" + TEMP_DELIMITER + "', '" + OLD_DELIMITER + "');\r\n" ;
        return query;
    }

    private String insertAvailableRolesToFnRole(){
        String query="INSERT INTO fn_role (ROLE_NAME, ACTIVE_YN, PRIORITY)\r\n" +
                "SELECT RNAME, 'Y', 5\r\n" +
                "FROM available_roles\r\n" +
                "WHERE NOT EXISTS (SELECT ROLE_NAME\r\n" +
                "FROM fn_role \r\n" +
                "where RNAME = ROLE_NAME);\r\n";
        return query;
    }



    private String createTemporaryTableAvailableRoles(String availableRoles) {
        String query = "CREATE TEMPORARY TABLE IF NOT EXISTS available_roles(rname VARCHAR(255));\r\n";
        query += "INSERT INTO available_roles VALUES "+availableRoles+";\r\n";
                return query;
    }

    private String deleteWrongRecords(){
        String query ="CREATE TEMPORARY TABLE IF NOT EXISTS wrong_roles(roleID INT);\r\n" +
                "INSERT INTO wrong_roles (roleID)\r\n" +
                "SELECT ROLE_ID FROM fn_role LEFT JOIN available_roles ON role_name LIKE concat(rname, '%')\r\n" +
                "WHERE available_roles.rname IS NULL AND ROLE_ID NOT IN (1,16);\r\n";
        query += deleteCascade();
        query += dropTemporaryTable("wrong_roles");
        return query;
    }

    private String deleteCascade() {
        String query = deleteFromTableByRoles("fn_role_composite", "PARENT_ROLE_ID");
        query = query.substring(0, query.length()-1);
        query += " OR wrong_roles.ROLEID = fn_role_composite.CHILD_ROLE_ID;\r\n";
        query += deleteFromTableByRoles("fn_role_function", ROLE_ID_COLUMN)+ "\r\n";
        query += deleteFromTableByRoles("fn_user_role", ROLE_ID_COLUMN)+ "\r\n";
        query += deleteFromTableByRoles(TBL_NAME, ROLE_ID_COLUMN)+ "\r\n";
        return query;
    }

    private String deleteFromTableByRoles(String table, String column) {
        String query = "DELETE FROM " + table + "\r\n";
        query += "using  " + table + " inner join wrong_roles\r\n" +
                "where wrong_roles.ROLEID = " + table + "." + column + ";";
        return query;
    }

    private String dropTemporaryTable(String table) {
        return "DROP TEMPORARY TABLE IF EXISTS " + table + ";\r\n";
    }
}
