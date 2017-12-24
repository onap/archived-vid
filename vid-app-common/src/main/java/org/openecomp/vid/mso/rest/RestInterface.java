package org.openecomp.vid.mso.rest;

import org.openecomp.vid.mso.RestObject;

/**
 * Created by pickjonathan on 26/06/2017.
 */
public interface RestInterface {

    /**
     * Inits the rest client.
     */
    public void initMsoClient();

    /**
     * Gets the.
     *
     * @param <T> the generic type
     * @param t the t
     * @param sourceId the source id
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    public <T> void Get (T t, String sourceId, String path, RestObject<T> restObject ) throws Exception;

    /**
     * Delete.
     *
     * @param <T> the generic type
     * @param t the t
     * @param r the r
     * @param sourceID the source ID
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    public <T> void Delete(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) throws Exception;

    /**
     * Post.
     *
     * @param <T> the generic type
     * @param t the t
     * @param r the r
     * @param sourceID the source ID
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    public <T> void Post(T t, RequestDetails r, String sourceID, String path, RestObject<T> restObject) throws Exception;
    
    /**
     * Put.
     *
     * @param <T> the generic type
     * @param t the t
     * @param r the r
     * @param sourceID the source ID
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    public <T> void Put(T t, org.openecomp.vid.changeManagement.RequestDetailsWrapper r, String sourceID, String path, RestObject<T> restObject) throws Exception;


    /***
     * Log request.
     *
     * @param r the r
     */
    public void logRequest ( Object r  );
    /***
     * Log request.
     *
     * @param r the r
     */
    public void logRequest(org.openecomp.vid.changeManagement.RequestDetails r);


}
