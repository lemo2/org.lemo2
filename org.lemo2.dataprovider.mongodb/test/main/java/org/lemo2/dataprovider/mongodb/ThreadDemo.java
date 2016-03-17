package org.lemo2.dataprovider.mongodb;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.lemo2.dataprovider.api.LA_Context;
import org.lemo2.dataprovider.mongodb.domain.MongoDB_Context;

public class ThreadDemo implements Runnable {

	Thread t;
	String operation;
	Integer contextID;
	List<Integer> initializedContexts;
	int countInitializedActivities;
	int countInitializedContexts;
	
	public ThreadDemo(String operation) {
		this.operation = operation;
	}
	
	public ThreadDemo(String operation, Integer contextID) {
		this.operation = operation;
		this.contextID = contextID;
	}
	
	@Override
	public void run() {
		try {
			if (contextID == null) {
				//while(!allInitialized()) {
				for (int i = 100; i > 0; i--) {
					if (operation.equals("CONTEXT")) {
						System.out.println("Thread: " + operation);
						loadAllCourses();
					}
					if (operation.equals("ACTIVITY")) {
				        System.out.println("Thread: " + operation);
				        // Let the thread sleep for a while.
				        loadAllActivities();
					}
					System.out.println("Initialzed activities: " + MongoDB_ActivityDataProvider.getSizeOfInitializedActivities());
					System.out.println("Initialized contexts: " + MongoDB_ContextDataProvider.getSizeOfInitializedContexts());
					
					Thread.sleep(50);
				//}
				}
			}
			else {
				while (countInitializedActivities < 58101) {
					if (operation.equals("CONTEXT")) {
						System.out.println("Thread: " + operation + " " + contextID);
						loadCourse(contextID);
					}
					if (operation.equals("ACTIVITY")) {
				        System.out.println("Thread: " + operation + " " + contextID);
				       loadCourseActivities(contextID);
					}
					
					countInitializedActivities = MongoDB_ActivityDataProvider.getSizeOfInitializedActivities();
					countInitializedContexts = MongoDB_ContextDataProvider.getSizeOfInitializedContexts();
					
					System.out.println("(206) Initialzed activities: " + countInitializedActivities);
					System.out.println("(206) Initialized contexts: " + countInitializedContexts);
					
					Thread.sleep(50);
				}
			}
		}
		catch (InterruptedException e) {
	         System.out.println("Thread " +  operation + " interrupted.");
	     }
		System.out.println("Initialzed activities: " + MongoDB_ActivityDataProvider.getSizeOfInitializedActivities());
		System.out.println("Initialized contexts: " + MongoDB_ContextDataProvider.getSizeOfInitializedContexts());
	}
	
	public void start ()
	   {
	      System.out.println("Starting " +  operation );
	      if (t == null)
	      {
	         t = new Thread (this, operation);
	         t.start ();
	      }
	   }
	
	private void loadAllCourses() {
		MongoDB_ContextDataProvider.getAllCourses2();
	}
	
	private void loadCourse(Integer contextID) {
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(contextID);
		context.getChildren();
	}
	
	private void loadCourseActivities(Integer contextID) {
		LA_Context context = MongoDB_ContextDataProvider.getContextByID(contextID);
		List<LA_Context> children = context.getChildren();
		
		context.getActivities();
		for (LA_Context child : children) {
			child.getActivities();
		}
	}
	
	private void loadAllActivities() {
		System.out.println("load activities");
		initializedContexts = MongoDB_ContextDataProvider.getInitializedIDs();
		
		//synchronized (initializedContexts) {
			for (Integer contextID : initializedContexts) {
				MongoDB_Context context = (MongoDB_Context) MongoDB_ContextDataProvider.getContextByID(contextID);
				
				//if (!context.isLocked()) {
					System.out.println("Get activities for: " + context.getName());
					context.getActivities();
				//}
			}
		//}
		
	}
	
	private boolean allInitialized() {
		List<Integer> idList = MongoDB_ActivityDataProvider.getAllLearningActivityIDs();
		int countAllActivities = idList.size();
		int countInitializedActivities = MongoDB_ActivityDataProvider.getSizeOfInitializedActivities();
		
		if (countAllActivities == countInitializedActivities) {
			return true;
		}
		else {
			System.out.println(countInitializedActivities + " of " + countAllActivities 
					+ " activities initialized");
			return false;
		}
		
		//return count == initializedContexts.size();
	}

}
