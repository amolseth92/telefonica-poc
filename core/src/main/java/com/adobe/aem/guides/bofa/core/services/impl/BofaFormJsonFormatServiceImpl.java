package com.adobe.aem.guides.bofa.core.services.impl;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.queries.function.valuesource.MultiFunction.Values;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.bofa.core.services.BofaFormJsonFormatService;
import com.adobe.aem.guides.bofa.core.servlets.BofaFormJsonFormatServlet;
import com.drew.lang.StringUtil;
import com.drew.metadata.StringValue;

/**
 * @author Amol
 *
 */
@Component(immediate = true, service = BofaFormJsonFormatService.class)
public class BofaFormJsonFormatServiceImpl implements BofaFormJsonFormatService{

	/**
	 * Logs for the service class is fetched using this.
	 */
	private static final Logger logger = LoggerFactory.getLogger(BofaFormJsonFormatServiceImpl.class);
	public static int listcount = 1;

	/**
	 * @param node
	 * @return
	 * @throws JSONException
	 * @throws RepositoryException
	 */
	/**
	 * @param node
	 * @return
	 * @throws JSONException
	 * @throws RepositoryException
	 * To create the data json of the form
	 */
	public JSONObject transformDataJson(Node node) throws JSONException, RepositoryException {
		NodeIterator nodeIterator;
		nodeIterator = node.getNodes();
		JSONObject firstlevel = new JSONObject();
		// firstlevel.put("dataBindingObject", "w9Data");
		JSONArray templates = new JSONArray();

		while (nodeIterator.hasNext()) {
			JSONObject secondlevel = new JSONObject();
			Node childnode = nodeIterator.nextNode();
			String nodePath = childnode.getPath();
			if (childnode.hasProperty("name") && childnode.hasProperty("guideNodeClass") == true) {
				if (!childnode.getProperty("name").getString().equals("formTitle")) {
					if (childnode.getProperty("guideNodeClass").getString().equals("guidePanel")) {
						JSONObject parentjsonobj = new JSONObject();
						parentjsonobj.put("templateId", childnode.getProperty("name").getString());
						Node itemnode = childnode.getNode("items");
						NodeIterator itemiterator;
						itemiterator = itemnode.getNodes();
						JSONArray listarr = new JSONArray();
						JSONObject listobj = new JSONObject();
						while (itemiterator.hasNext()) {
							Node itemchildnode = itemiterator.nextNode();
							JSONObject secondpanellevel = new JSONObject();
							if (itemchildnode.hasProperty("_value")
									&& itemchildnode.getProperty("name").getString().equalsIgnoreCase("header")) {
								listobj.put("header", itemchildnode.getProperty("_value").getString());
							} else {
								if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guideRadioButton")) {
									String header = itemchildnode.getProperty("jcr:title").getString();
									JSONObject dataObj = new JSONObject();
									String uid = "list" + String.valueOf(listcount);
									dataObj.put("UID", uid);
									++listcount;
									dataObj.put("header", header);
									JSONObject listoptarr = new JSONObject();
									Value[] values = itemchildnode.getProperty("options").getValues();
									for (int i = 0; i < values.length; i++) {
										listoptarr.put("Uid", values[0].getString()
												.substring(values[0].getString().lastIndexOf("=") + 1));
										listoptarr.put("text", values[0].getString()
												.substring(values[0].getString().lastIndexOf("=") + 1));
									}
									dataObj.put("list", listoptarr.toString());
									secondpanellevel.put("data", dataObj);
								} else if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guideTextBox")) {
									String templateID = itemchildnode.getProperty("name").getString();
									secondpanellevel.put("uid1", templateID);
									secondpanellevel.put("Hint", itemchildnode.getProperty("jcr:title").getString());
									secondpanellevel.put("Label",
											itemchildnode.getProperty("placeholderText").getString());
								} else if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guidecheckbox")) {
									String templateID = itemchildnode.getProperty("name").getString();
									secondpanellevel.put("uid1", templateID);
									secondpanellevel.put("Hint", itemchildnode.getProperty("jcr:title").getString());
									secondpanellevel.put("Label",
											itemchildnode.getProperty("placeholderText").getString());
								} else if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guidedatepicker")) {
									String templateID = itemchildnode.getProperty("name").getString();
									secondpanellevel.put("uid1", templateID);
									secondpanellevel.put("Hint", itemchildnode.getProperty("jcr:title").getString());
									secondpanellevel.put("Label",
											itemchildnode.getProperty("placeholderText").getString());
								} else if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guideNumericBox")) {
									String templateID = itemchildnode.getProperty("name").getString();
									secondpanellevel.put("uid1", templateID);
									secondpanellevel.put("Hint", itemchildnode.getProperty("jcr:title").getString());
									secondpanellevel.put("Label",
											itemchildnode.getProperty("placeholderText").getString());
								}
								listarr.put(secondpanellevel.toString());
							}
						}

						listobj.put("list", listarr);
						parentjsonobj.put("data", listobj);
						templates.put(parentjsonobj);
					} else {
						if (childnode.getProperty("guideNodeClass").getString().equals("guideRadioButton")) {
							String templateID = childnode.getProperty("name").getString();
							String header = childnode.getProperty("jcr:title").getString();
							secondlevel.put("templateID", templateID);
							// secondlevel.put("layout", "singleSelectListTemplate");
							JSONObject dataObj = new JSONObject();
							dataObj.put("UID", "listview");
							dataObj.put("header", header);
							JSONObject listarr = new JSONObject();
							Value[] values = childnode.getProperty("options").getValues();
							for (int i = 0; i < values.length; i++) {
								listarr.put("Uid",
										values[0].getString().substring(values[0].getString().lastIndexOf("=") + 1));
								listarr.put("text",
										values[0].getString().substring(values[0].getString().lastIndexOf("=") + 1));
							}
							dataObj.put("list", listarr.toString());
							secondlevel.put("data", dataObj);
						} else if (childnode.getProperty("guideNodeClass").getString().equals("guideTextBox")) {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "textInputTemplate");
						} else if (childnode.getProperty("guideNodeClass").getString().equals("guidecheckbox")) {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "multiSelectTemplate");
						} else if (childnode.getProperty("guideNodeClass").getString().equals("guidedatepicker")) {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "calendarTemplate");
						} else if (childnode.getProperty("guideNodeClass").getString().equals("guideNumericBox")) {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "textInputTemplate");
						} else {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", templateID);

						}
						templates.put(secondlevel);
					}

				}
			}
		}
		firstlevel.put("templates", templates);

		return firstlevel;
	}

	/**
	 * @param node
	 * @return
	 * @throws JSONException
	 * @throws RepositoryException
	 * To create the flow json of the form
	 */
	public JSONObject transformFlowJson(Node node) throws JSONException, RepositoryException {
		NodeIterator nodeIterator;
		nodeIterator = node.getNodes();
		JSONObject firstlevel = new JSONObject();
		firstlevel.put("dataBindingObject", transformDataJson(node));
		JSONArray templates = new JSONArray();

		while (nodeIterator.hasNext()) {
			JSONObject secondlevel = new JSONObject();
			Node childnode = nodeIterator.nextNode();
			String nodePath = childnode.getPath();
			if (childnode.hasProperty("name") && childnode.hasProperty("guideNodeClass") == true) {
				if (!childnode.getProperty("name").getString().equals("formTitle")) {
					if (childnode.getProperty("guideNodeClass").getString().equals("guidePanel")) {
						JSONObject parentjsonobj = new JSONObject();
						parentjsonobj.put("templateId", childnode.getProperty("name").getString());
						Node itemnode = childnode.getNode("items");
						NodeIterator itemiterator;
						itemiterator = itemnode.getNodes();
						String templateType= null;
						while (itemiterator.hasNext()) {
							Node itemchildnode = itemiterator.nextNode();
							if (!itemchildnode.getProperty("name").getString().equalsIgnoreCase("header")) {
								if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guideRadioButton")) {
									templateType = "singleSelectListTemplate";
									parentjsonobj.put("layout", "singleSelectListTemplate");

								} else if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guideTextBox")) {
									String templateID = itemchildnode.getProperty("name").getString();
									templateType = "textInputTemplate";
									parentjsonobj.put("layout", "textInputTemplate");
									logger.error("outside ifffffffffffffffffffffffff"+itemchildnode.hasNode("fd:rules"));
									if (itemchildnode.hasNode("fd:rules")) {
										logger.error("hello inside ifffffffffffffffffffffffff"+itemchildnode.hasNode("fd:rules"));
										Value[] temparr = itemchildnode.getNode("fd:rules").getProperty("fd:valueCommit")
												.getValues();
										JSONObject ruleobj = new JSONObject(temparr[0].getString());
										
										
										//ruleobj.get("items");
										JSONArray rootarr = new JSONArray(ruleobj.get("items"));
										logger.error("value of rules is @@@@@@@@@@@@@@@@@@@@@@@@"+rootarr.getString(0));
									}
								} else if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guidecheckbox")) {
									String templateID = itemchildnode.getProperty("name").getString();
									templateType = "multipleSelectTemplate";
									parentjsonobj.put("layout", "multipleSelectTemplate");
								} else if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guidedatepicker")) {
									String templateID = itemchildnode.getProperty("name").getString();
									templateType = "calendarTemplate";
									parentjsonobj.put("layout", "calendarTemplate");
								} else if (itemchildnode.getProperty("guideNodeClass").getString()
										.equals("guideNumericBox")) {
									templateType = "textInputTemplate";
									parentjsonobj.put("layout", "textInputTemplate");
								}
							}
						}

						templates.put(parentjsonobj);
					} else {
						String templateType;
						if (childnode.getProperty("guideNodeClass").getString().equals("guideRadioButton")) {
							String templateID = childnode.getProperty("name").getString();
							templateType = "singleSelectListTemplate";
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "singleSelectListTemplate");
							JSONObject triggerObjects = new JSONObject();
							JSONArray triggerArr = new JSONArray();
							triggerObjects.put("UID", "listview" + String.valueOf(listcount));
							triggerObjects.put("event", "click");
							triggerObjects.put("outcomes", createTriggerAction("collect", templateID, null));
							triggerArr.put(triggerObjects);
							JSONObject predObjects = new JSONObject();
							if (childnode.hasNode("fd:scripts")) {
								Value[] temparr = childnode.getNode("fd:scripts").getProperty("fd:valueCommit")
										.getValues();
								String content = null;

								for (int i = 0; i < temparr.length; i++) {
									if (temparr[i].getString().contains("content")) {
										JSONObject tobj = new JSONObject(temparr[i].getString());
										JSONObject contentobj = new JSONObject(tobj.get("script").toString());
										content = contentobj.get("content").toString();
										break;
									}
								}
								JSONArray predicatesarr = new JSONArray();
								predObjects.put("UID", "listview" + String.valueOf(listcount));
								predObjects.put("event", "click");
								predObjects.put("outcomes", predicatesarr.put(content));
								triggerArr.put(predObjects);
							}

							secondlevel.put("triggers", triggerArr);

						} else if (childnode.getProperty("guideNodeClass").getString().equals("guideTextBox")) {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "textInputTemplate");
						} else if (childnode.getProperty("guideNodeClass").getString().equals("guidecheckbox")) {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "multiSelectTemplate");
						} else if (childnode.getProperty("guideNodeClass").getString().equals("guidedatepicker")) {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "calendarTemplate");
						} else if (childnode.getProperty("guideNodeClass").getString().equals("guideNumericBox")) {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", "textInputTemplate");
						} else {
							String templateID = childnode.getProperty("name").getString();
							secondlevel.put("templateID", templateID);
							secondlevel.put("layout", templateID);

						}
						templates.put(secondlevel);
					}

				}
			}
		}
		firstlevel.put("templates", templates);

		return firstlevel;

	}

	/**
	 * @param action
	 * @param template
	 * @param predicate
	 * @return Returns the action json of the form
	 * @throws JSONException
	 */
	public JSONArray createTriggerAction(String action, String template, String predicate) throws JSONException {

		JSONArray finalarr = new JSONArray();
		if (template != null) {
			JSONObject actions = new JSONObject();
			JSONArray arr = new JSONArray();
			JSONObject obj = new JSONObject();
			JSONArray widgets = new JSONArray();
			obj.put("action", action);
			JSONObject widget = new JSONObject();
			widget.put("dataId", template);
			widgets.put(widget);
			obj.put("widgets", widgets);
			arr.put(obj);
			actions.put("actions", arr);
			finalarr.put(actions);
		} else if (predicate != null) {
			JSONObject pred = new JSONObject(predicate);
			finalarr.put(pred);
		}
		return finalarr;
	}
}
