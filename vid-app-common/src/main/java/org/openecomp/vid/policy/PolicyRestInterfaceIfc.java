
package org.openecomp.vid.policy;

import org.json.simple.JSONObject;
import org.openecomp.vid.policy.rest.RequestDetails;

public interface PolicyRestInterfaceIfc {	
	/**
	 * Inits the rest client.
	 */
	public void initRestClient();
	
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
	public <T> void Post(T t, JSONObject r, String sourceID, String path, RestObject<T> restObject) throws Exception;
	
	/***
	 * Log request.
	 *
	 * @param r the r
	 */
	public void logRequest ( RequestDetails r  );
	
}