package org.openecomp.vid.services;

import jline.internal.Log;
import org.junit.Test;
import org.openecomp.vid.aai.*;
import org.openecomp.vid.model.ModelConstants;
import org.openecomp.vid.model.Subscriber;
import org.openecomp.vid.model.SubscriberList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RoleGenaratorServiceImpl implements RoleGeneratorService {

    @Autowired
    AaiClientInterface client;

    public static final String dbName =  "vid_portal";
    public static final String  tblName = "fn_role";
    public static final String tempDelimiter ="***";
    public static final String oldDelimiter = "_";

    @Override
    public String generateRoleScript(Boolean firstRun) {
        String query =  "USE " + dbName + ";\r\n" +
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
            String subscriberName = subscriber.subscriberName.contains(oldDelimiter) ? subscriber.subscriberName.replace(oldDelimiter, tempDelimiter) : subscriber.subscriberName;
            query = query + "('" + subscriber.globalCustomerId + "','" + subscriberName + "') ,";
        }
        if(query.length() > 0)
            query = query.substring(0, query.length()-1) + ";\r\n";
        return query;
    }

    private String buildServicesValuesForMappingsTable(HashMap<String,String> servicesNames){
        final String[] query = {""};
        servicesNames.forEach((k,v)->{
            if (k.contains(oldDelimiter)) {
                query[0] += "('" + k + "' ,'" + k.replace(oldDelimiter, tempDelimiter) +"'),";
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
            query += "UPDATE " + tblName + "\r\n" +
                    "INNER JOIN " + entityName + "Mappings ON role_name LIKE concat('%',mapKey, '%')\r\n" +
                    "SET ROLE_NAME = REPLACE(ROLE_NAME, mapKey, mapValue) ;  \r\n" +
                    dropTemporaryTable(entityName + "Mappings");
        }
        return query;
    }

    private String replaceToNewDelimiter(){
        String query =  "UPDATE " + tblName + "\r\n" +
                "SET ROLE_NAME = REPLACE(ROLE_NAME, '" + oldDelimiter + "', '" + ModelConstants.ROLE_DELIMITER + "');\r\n" ;
        query += "UPDATE fn_role\r\n" +
                "SET ROLE_NAME = REPLACE(ROLE_NAME, '" + tempDelimiter + "', '" + oldDelimiter + "');\r\n" ;
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
        query += deleteFromTableByRoles("fn_role_function", "ROLE_ID")+ "\r\n";
        query += deleteFromTableByRoles("fn_user_role", "ROLE_ID")+ "\r\n";
        query += deleteFromTableByRoles("fn_role", "ROLE_ID")+ "\r\n";
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
