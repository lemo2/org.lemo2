package org.lemo2.analysis.activitytime;

import java.io.ByteArrayOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;

import org.lemo2.analysis.activitytime.returntypes.ActivityTimeResult;
import org.lemo2.analysis.activitytime.returntypes.ResultListHashMapObject;
import org.lemo2.analysis.activitytime.returntypes.ResultListLongObject;
import org.lemo2.dataprovider.api.DataProvider;
import org.lemo2.dataprovider.api.LA_Activity;
import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.analysis.api.WebResource;


@Component
@Provides
@Instantiate
@Path("tools/activitytime")
public class ActivityTimeWebResource implements WebResource{

	@Requires
	private DataProvider dataProvider;
	
    @Context
    private SecurityContext securityContext;
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityTimeWebResource.class);
	private double intervall;
	
	@GET
	@PermitAll
	public String getResult(@Context SecurityContext securityContext){
		Principal principal = securityContext.getUserPrincipal(); 
		logger.info("Principal: "+ principal);
//		for (String header : securityContext.getRequestHeaders().keySet()) {
//			logger.info("This header was set: " + header);
//		}
		String json = "";
		String xml = "";
		Long startDate = 1434025148950L;

		Long startDate2 = new Long(1432001445);
		Long endDate2 = new Long(1432091445);
		ResultListHashMapObject resultListHashMap = computeActivities(Arrays.asList(1L),null,startDate2,
				endDate2,1000L,Arrays.asList("test"),Arrays.asList(1L,2L),null);
		
		
		ActivityTimeResult result = new ActivityTimeResult(resultListHashMap, startDate, intervall);
		try {
			JAXBContext jc = JAXBContext.newInstance(ActivityTimeResult.class);
			Marshaller m = jc.createMarshaller();
			ByteArrayOutputStream bost = new ByteArrayOutputStream();
			m.marshal( result, bost );
			xml = bost.toString();
	        JSONObject xmlJSONObj = XML.toJSONObject(xml);
	        json = xmlJSONObj.toString(); 
		} catch (Exception e) {
			logger.error("Error converting result list to json", e);
		}
		return json;
	}

	private LA_Context getDemoContext() {
		List<LA_Context> contexts = dataProvider.getCourses();
		LA_Context context = null;
	    for (Iterator<LA_Context> it = contexts.iterator(); it.hasNext(); ) {
	    	context = it.next();
	    }
		return context;
	}

	public ResultListHashMapObject computeActivities(final List<Long> courses,
			List<Long> users,
			final Long startTime,
			final Long endTime,
			final Long resolution,
			final List<String> resourceTypes,
			List<Long> gender,
			List<Long> learningObjects){

		final Map<Long, ResultListLongObject> result = new HashMap<Long, ResultListLongObject>();
		final Map<Long, HashMap<Integer, Set<Long>>> userPerResStep = new HashMap<Long, HashMap<Integer, Set<Long>>>();

		validateTimestamps(startTime, endTime, resolution);

		// Calculate size of time intervalls
		this.intervall = (endTime - startTime) / (resolution);
		
		initializeVariables(userPerResStep,result,dataProvider.getCourses(),resolution,resourceTypes);
		
		sumActivities(userPerResStep,result,dataProvider.getCourses(),resolution,resourceTypes, startTime, intervall);
		
		copyUserPerResStepIntoResult(userPerResStep,result,dataProvider.getCourses(),resolution);
		
		final ResultListHashMapObject resultObject = createReturnObject(result);
		
		// Alias keys from StudentHelper class are removed.

		return resultObject;
	}
	
	private ResultListHashMapObject createReturnObject(Map<Long, ResultListLongObject> result) {
		final ResultListHashMapObject resultObject = new ResultListHashMapObject(result);
		if ((resultObject != null) && (resultObject.getElements() != null)) {
			final Set<Long> keySet = resultObject.getElements().keySet();
			final Iterator<Long> it = keySet.iterator();
			while (it.hasNext()) 
			{
				final Long learnObjectTypeName = it.next();
				this.logger.info("Result Course IDs: " + learnObjectTypeName);
			}

		} else {
			this.logger.info("Returning empty resultset.");
		}
		return resultObject;
	}

	private void copyUserPerResStepIntoResult(Map<Long, HashMap<Integer, Set<Long>>> userPerResStep, 
			Map<Long, ResultListLongObject> result, 
			List<LA_Context> list, 
			Long resolution) {
		for (final LA_Context context : list)
		{
			try{
			long contextHash = (long)context.hashCode();
			for (int i = 0; i < resolution; i++)
			{
				if (userPerResStep.get(contextHash).get(i) == null)
				{
					result.get(contextHash).getElements().add(0L);
				} else {
					result.get(contextHash).getElements().add(Long.valueOf(userPerResStep.get(contextHash).get(i).size()));
				}
			}
			} catch(Exception e){
				logger.error("UserPerRes copy error ", e);
			}
		}		
	}

	private void sumActivities(Map<Long, HashMap<Integer, Set<Long>>> userPerResStep, 
			Map<Long, ResultListLongObject> result, 
			List<LA_Context> list, 
			Long resolution, 
			List<String> resourceTypes, 
			Long startTime, 
			double intervall) {

		LA_Context context = getDemoContext();
		
		for (LA_Activity activity : context.getActivities())
		{
			logger.info("Activity: "+ activity.getObject()+ " at "+activity.getTime()+ " added.");
			if(activity.getObject()!=null && activity.getPerson()!=null){
			boolean isInRT = false;
			if ((resourceTypes != null) && (resourceTypes.size() > 0) && resourceTypes.contains(activity.getObject().getType()))
			{
				isInRT = true;
			}
			if ((resourceTypes == null) || (resourceTypes.size() == 0) || isInRT)
			{
				Integer pos = new Double((activity.getTime() - startTime) / intervall).intValue();
				if (pos > (resolution - 1)) {
					pos = resolution.intValue() - 1;
				}
				long contextHash = (long)context.hashCode();
				result.get(contextHash).getElements()
							.set(pos, result.get(contextHash).getElements().get(pos) + 1);
				if (userPerResStep.get(contextHash).get(pos) == null)
				{
					final Set<Long> s = new HashSet<Long>();
					s.add((long)activity.getPerson().hashCode());
					userPerResStep.get(contextHash).put(pos, s);
				} else {
					userPerResStep.get(contextHash).get(pos).add((long)activity.getPerson().hashCode());
				}
			}
		}
		}
	}

	private void initializeVariables(
			Map<Long, HashMap<Integer, Set<Long>>> userPerResStep,
			Map<Long, ResultListLongObject> result, 
			List<LA_Context> list, 
			Long resolution, 
			List<String> resourceTypes ) {
		
		// Create and initialize array for results
		int j=0;
		for (LA_Context context:list)
		{			
			final Long[] resArr = new Long[resolution.intValue()];
			for (int i = 0; i < resArr.length; i++)
			{
				resArr[i] = 0L;
			}
			final List<Long> l = new ArrayList<Long>();
			Collections.addAll(l, resArr);
			result.put((long)context.hashCode(), new ResultListLongObject(l));
			j++;
		}

		for (final LA_Context context : list) {
			userPerResStep.put((long)context.hashCode(), new HashMap<Integer, Set<Long>>());
		}

		for (String resourceType : resourceTypes) {
			this.logger.debug("Course Activity Request - CA Selection: " + resourceType);
		}
		if (resourceTypes.isEmpty()) {
			this.logger.debug("Course Activity Request - CA Selection: NO Items selected ");
		}
		
	}

	private void validateTimestamps(Long startTime, Long endTime, Long resolution) {
		validateTimestamps(startTime, endTime);
		if (resolution == null) {
			throw new BadRequestException("Missing resolution parameter.");
		}
		if (resolution <= 0) {
			throw new BadRequestException("Invalid resolution: resolution is a negative value.");
		}
	}
	
	protected void validateTimestamps(Long startTime, Long endTime) {
		if (startTime == null) {
			throw new BadRequestException("Missing start time parameter.");
		}
		if (endTime == null) {
			throw new BadRequestException("Missing end time parameter.");
		}
		if (startTime >= endTime) {
			throw new BadRequestException("Invalid start time: start time exceeds end time.");
		}
	}
}
