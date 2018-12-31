package org.onap.vid.mso.rest;

import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.RestObjectWithRequestInfo;

/**
 * Created by pickjonathan on 26/06/2017.
 */
public interface RestInterface {

    /**
     * Gets the.
     *
     * @param <T> the generic type
     * @param t the t
     * @param path the path
     * @param restObject the rest object
     * @param warpException
     * @throws Exception the exception
     */
    <T> RestObjectWithRequestInfo<T> Get(T t, String path, RestObject<T> restObject, boolean warpException);

    /**
     * Delete.
     *
     * @param <T> the generic type
     * @param t the t
     * @param r the r
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    <T> void Delete(T t, Object r, String path, RestObject<T> restObject);

    /**
     * Post.
     *
     * @param t the t
     * @param r the r
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    void Post(String t, Object r, String path, RestObject<String> restObject);

    /**
     * Put.
     *
     * @param <T> the generic type
     * @param t the t
     * @param r the r
     * @param path the path
     * @param restObject the rest object
     * @throws Exception the exception
     */
    <T> void Put(T t, RequestDetailsWrapper r, String path, RestObject<T> restObject);

    <T> RestObject<T> GetForObject(String path, Class<T> clazz);

}
