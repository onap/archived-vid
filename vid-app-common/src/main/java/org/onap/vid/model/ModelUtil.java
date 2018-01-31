/**
 * 
 */
package org.onap.vid.model;

/**
 * The Class ModelUtil.
 *
 */
public class ModelUtil {
	/**
	 * Gets the tags for the given element according to the configured namespace
	 * @param namespaces the namespace list from the configuration
	 * @param constantValue the constant portion of the tag name, i.e. resource.vf...
	 * @return the tags
	 */
	public static String[] getTags ( String[] namespaces, String constantValue ) {
		String[] tags;
		if ( namespaces == null || namespaces.length == 0 ) {
			return null;
		}
		int le = namespaces.length;
		tags = new String[le];
		for ( int i = 0; i < le; i++ ) {
			tags[i] = namespaces[i] + constantValue;
		}
		return (tags);
	}
	/**
	 * Determine if a note template type matches a set of configurable tags
	 * @param type the node template type
	 * @param tags the model configurable namespaces
	 * @return true if type starts with a tag in the array, false otherwise
	 */
	public static boolean isType ( String type, String[] tags ) {
		if ( (tags != null) && (tags.length > 0) ) {
			for ( int i = 0; i < tags.length; i++ ) {
				if ( type.startsWith (tags[i]) ) {
					return (true);
				}
			}
		}
		return (false);
	}
}
