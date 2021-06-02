package com.adobe.aem.guides.bofa.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.bofa.core.services.BofaFormJsonFormatService;
import com.adobe.aem.guides.bofa.core.services.impl.BofaFormJsonFormatServiceImpl;

import org.apache.sling.api.servlets.HttpConstants;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "= Form Json Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET, "sling.servlet.resourceTypes=" + "bofa/w9-form",
		"sling.servlet.extensions=" + "json" })
public class BofaFormJsonFormatServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(BofaFormJsonFormatServlet.class);

	/**
	 * Reference of service class to create the form json flow.
	 */
	@Reference
	BofaFormJsonFormatService service;

	/**
	 *
	 */
	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {

		// response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		logger.info("outside the try block the child node of items");
		try {
			final PrintWriter out = response.getWriter();
			final ResourceResolver resolver = request.getResourceResolver();
			final Resource resource = resolver
					.getResource("/content/forms/af/boa/w-9-form/jcr:content/guideContainer/rootPanel/items");
			final Node node = resource.adaptTo(Node.class);

			out.print(service.transformFlowJson(node));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
