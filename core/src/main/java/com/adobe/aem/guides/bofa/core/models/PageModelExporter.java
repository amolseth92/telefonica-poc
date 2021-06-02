package com.adobe.aem.guides.bofa.core.models;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables=Resource.class, resourceType="/libs/fd/af/components/page2/aftemplatedpage")
@Exporter(name="jackson", extensions = "json")
public class PageModelExporter {

	@Self
	private Resource resource;
	
	@Inject @Named("jcr:title")
	String title;
	
	public String getTitle() {
		return title;
	}

	@PostConstruct
	private void init() {
		
	}
	

}
