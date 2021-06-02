package com.adobe.aem.guides.bofa.core.services;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents the service for creating the form json into Bofa Format Json.
 * 
 * @author Amol
 *
 */
public interface BofaFormJsonFormatService {
	
	/**
	 * @param node
	 * @return it returns the template data json.
	 * @throws RepositoryException 
	 * @throws JSONException 
	 */
	JSONObject transformDataJson(Node node) throws JSONException, RepositoryException;
	
	/**
	 * @param node
	 * @return returns the form flow json binded with template data json.
	 * @throws RepositoryException 
	 * @throws JSONException 
	 */
	JSONObject transformFlowJson(Node node) throws JSONException, RepositoryException;
	
	/**
	 * @param action
	 * @param template
	 * @param predicate
	 * @return returns the action json for flow json
	 * @throws JSONException 
	 */
	JSONArray createTriggerAction(String action, String template, String predicate) throws JSONException;

}
