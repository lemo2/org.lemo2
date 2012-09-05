package de.lemo.dms.processing.questions;

import static de.lemo.dms.processing.parameter.MetaParam.COURSE_IDS;
import static de.lemo.dms.processing.parameter.MetaParam.END_TIME;
import static de.lemo.dms.processing.parameter.MetaParam.START_TIME;
import static de.lemo.dms.processing.parameter.MetaParam.USER_IDS;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.lemo.dms.core.ServerConfigurationHardCoded;
import de.lemo.dms.db.EQueryType;
import de.lemo.dms.db.IDBHandler;
import de.lemo.dms.db.miningDBclass.AssignmentLogMining;
import de.lemo.dms.db.miningDBclass.ForumLogMining;
import de.lemo.dms.db.miningDBclass.QuestionLogMining;
import de.lemo.dms.db.miningDBclass.QuizLogMining;
import de.lemo.dms.db.miningDBclass.ResourceLogMining;
import de.lemo.dms.db.miningDBclass.ScormLogMining;
import de.lemo.dms.db.miningDBclass.WikiLogMining;
import de.lemo.dms.db.miningDBclass.abstractions.ILogMining;
import de.lemo.dms.processing.Question;
import de.lemo.dms.processing.parameter.Interval;
import de.lemo.dms.processing.parameter.MetaParam;
import de.lemo.dms.processing.parameter.Parameter;
import de.lemo.dms.processing.resulttype.ResultListUserLogObject;
import de.lemo.dms.processing.resulttype.UserLogObject;



@Path("userloghistory")
public class QUserLogHistory extends Question {
 

    @Override
    protected List<MetaParam<?>> createParamMetaData() {
        List<MetaParam<?>> parameters = new LinkedList<MetaParam<?>>();

        Session session = dbHandler.getMiningSession();
        List<?> latest = dbHandler.performQuery(session,EQueryType.SQL, "Select max(timestamp) from resource_log");
        dbHandler.closeSession(session); 
        Long now = System.currentTimeMillis() / 1000;

        if(latest.size() > 0)
            now = ((BigInteger) latest.get(0)).longValue();

        Collections.<MetaParam<?>>
                addAll(parameters,
                       Interval.create(Long.class, START_TIME, "Start time", "", 0L, now, 0L),
                       Parameter.create(USER_IDS, "Users", "List of users-ids."),
                       Parameter.create(COURSE_IDS, "Courses", "List of course-ids."),
                       Interval.create(Long.class, END_TIME, "End time", "", 0L, now, now)
                );
        return parameters;
    }

    /**
     * Returns all logged events matching the requirements given by the parameters.
     * 
     * @param courseIds		List of course-identifiers
     * @param userIds		List of user-identifiers
     * @param startTime		LongInteger time stamp  
     * @param endTime		LongInteger	time stamp 
     * @return
     */
    @POST
    public ResultListUserLogObject compute(
            @FormParam(COURSE_IDS) List<Long> courseIds,
            @FormParam(USER_IDS) List<Long> userIds,
            @FormParam(START_TIME) Long startTime,
            @FormParam(END_TIME) Long endTime){

        /*
         * This is the first usage of Criteria API in the project and therefore a bit more documented than usual, to
         * serve as example implementation for other analyses.
         */

        if(endTime == null) {
            endTime = new Date().getTime();
        }

        if(startTime >= endTime || userIds.isEmpty()) {
            return null;
        }


        /* A criteria is created from the session. */
        IDBHandler dbHandler = ServerConfigurationHardCoded.getInstance().getDBHandler();
        Session session = dbHandler.getMiningSession();

        /*
         * HQL-like equivalent: "Select from ILogMining". ILogMining is an interface, so Hibernate will load all classes
         * implementing it. The string argument is an alias.
         */
        Criteria criteria = session.createCriteria(ILogMining.class, "log");

        /*
         * Restrictions equivalent to HQL where:
         * 
         * where course in ( ... ) and timestamp between " + startTime + " AND " + endTime;
         */
        criteria.add(Restrictions.between("log.timestamp", startTime, endTime))
                .add(Restrictions.in("log.user.id", userIds));
        if(courseIds != null && courseIds.size() > 0)
        	criteria.add(Restrictions.in("log.course.id", courseIds));

        /* Calling list() eventually performs the query */
        @SuppressWarnings("unchecked")
        List<ILogMining> logs = criteria.list();
        
      //HashMap for all user-histories
        HashMap<Long, List<UserLogObject>> userPaths = new HashMap<Long, List<UserLogObject>>();
        
        //Iterate through all found log-items for saving log data into UserPathObjects
        for(int i = 0; i < logs.size(); i++)
        {
        	
        	String title ="";
    		String type ="";
    		ILogMining ilm = null;
    		
    		//the log entry has to be cast to its respective class to get its title
    		if(logs.get(i).getClass().equals(AssignmentLogMining.class) && ((AssignmentLogMining)logs.get(i)).getAssignment() != null)
    		{
    			ilm = (AssignmentLogMining)logs.get(i);
    			type = "assignment";
    			title = ((AssignmentLogMining)ilm).getAssignment().getTitle();
    		}
    		if(logs.get(i).getClass().equals(ForumLogMining.class) && ((ForumLogMining)logs.get(i)).getForum() != null)
    		{
    			ilm = (ForumLogMining)logs.get(i);
    			type = "forum";
    			title = ((ForumLogMining)ilm).getForum().getTitle();
    		}
    		
    		if(logs.get(i).getClass().equals(QuizLogMining.class) && ((QuizLogMining)logs.get(i)).getQuiz() != null)
    		{
    			ilm = (QuizLogMining)logs.get(i);
    			type = "quiz";
    			title = ((QuizLogMining)ilm).getQuiz().getTitle();
    		}
    		if(logs.get(i).getClass().equals(QuestionLogMining.class) && ((QuestionLogMining)logs.get(i)).getQuestion() != null)
    		{
    			ilm = (QuestionLogMining)logs.get(i);
    			type = "question";
    			title = ((QuestionLogMining)ilm).getQuestion().getTitle();
    		}
    		if(logs.get(i).getClass().equals(ResourceLogMining.class) && ((ResourceLogMining)logs.get(i)).getResource() != null)
    		{
    			ilm = (ResourceLogMining)logs.get(i);
    			type = "resource";
    			title = ((ResourceLogMining)ilm).getResource().getTitle();
    		}
    		if(logs.get(i).getClass().equals(WikiLogMining.class) && ((WikiLogMining)logs.get(i)).getWiki() != null)
    		{
    			ilm = (WikiLogMining)logs.get(i);
    			type = "wiki";
    			title = ((WikiLogMining)ilm).getWiki().getTitle();
    		}

    		if(logs.get(i).getClass().equals(ScormLogMining.class) && ((ScormLogMining)logs.get(i)).getScorm() != null)
    		{
    			ilm = (ScormLogMining)logs.get(i);
    			type = "scorm";
    			title = ((ScormLogMining)ilm).getScorm().getTitle();
    		}
    		if(ilm != null)
	        	if(userPaths.get(logs.get(i).getUser().getId()) == null)
	        	{
	        		ArrayList<UserLogObject> uP = new ArrayList<UserLogObject>();
	        		//If the user isn't already in the map, create new entry and insert the UserPathObject
	        		uP.add(new UserLogObject(ilm.getUser().getId(), ilm.getTimestamp(), title, ilm.getId(), type, ilm.getCourse().getId(), "" ));
	        		userPaths.put(logs.get(i).getUser().getId(), uP);
	        	}
	        	else
	        		//If the user is known, just add the UserPathObject to the user's history
	        		userPaths.get(ilm.getUser().getId()).add(new UserLogObject(ilm.getUser().getId(), ilm.getTimestamp(), title, ilm.getId(), type, ilm.getCourse().getId(), "" ));
    		else
    			System.out.println();
        }

        //List for UserPathObjects
        List<UserLogObject> l = new ArrayList<UserLogObject>();
        //Insert all entries of all user-histories to the list
        for(Iterator<List<UserLogObject>> iter = userPaths.values().iterator(); iter.hasNext();)
        	l.addAll(iter.next());
        //Sort the list (first by user and time stamp)
        Collections.sort(l);
        for(int i = 0; i < l.size(); i++)
        	System.out.println(l.get(i).getType());

        ResultListUserLogObject rlupo = new ResultListUserLogObject(l);
        
        return rlupo;
    }
}



/*
        Set<Long> users = Sets.newHashSet();
        for(ILogMining log : logs) {
            UserMining user = log.getUser();
            if(user == null)
                continue;
            users.add(user.getId());
        }

        logger.info("Found " + users.size() + " actions. " + +stopWatch.elapsedTime(TimeUnit.SECONDS));

        Criteria exdendedCriteria = session.createCriteria(ILogMining.class, "log");
        exdendedCriteria.add(Restrictions.in("log.user.id", users))
                .add(Restrictions.between("log.timestamp", startTime, endTime))
                .add(Restrictions.eq("log.action", "view"));

        @SuppressWarnings("unchecked")
        List<ILogMining> extendedLogs = exdendedCriteria.list();

        Map<Long, List<Long>> userPaths = Maps.newHashMap();

        logger.info("Paths fetched: " + extendedLogs.size() + ". " + stopWatch.elapsedTime(TimeUnit.SECONDS));

        for(ILogMining log : extendedLogs) {
            UserMining user = log.getUser();
            if(user == null || log.getCourse() == null)
                continue;
            long userId = log.getUser().getId();

            List<Long> courseIDs = userPaths.get(userId);
            if(courseIDs == null) {
                courseIDs = Lists.newArrayList();
                userPaths.put(userId, courseIDs);
            }
            courseIDs.add(log.getCourse().getId());
        }

        logger.info("userPaths: " + userPaths.size());

        Map<Long, List<JSONObject>> coursePaths = Maps.newHashMap();

        for(Entry<Long, List<Long>> userEntry : userPaths.entrySet()) {

            JSONObject lastPath = null;
            Long userID = userEntry.getKey();

            for(Long courseID : userEntry.getValue()) {
                List<JSONObject> edge = coursePaths.get(courseID);
                if(edge == null) {
                    edge = Lists.newArrayList();
                    coursePaths.put(courseID, edge);
                }
                JSONObject path = new JSONObject();

                path.put("user", userID);

                if(lastPath != null) {
                    lastPath.put("to", courseID);
                }
                lastPath = path;
            }
        }
        stopWatch.stop();
        logger.info("coursePaths: " + coursePaths.size());
        logger.info("Total Fetched log entries: " + (logs.size() + extendedLogs.size()) + " log entries."
                + stopWatch.elapsedTime(TimeUnit.SECONDS));


        JSONObject result = new JSONObject();
        JSONArray nodes = new JSONArray();
        for(Entry<Long, List<JSONObject>> courseEntry : coursePaths.entrySet()) {
            JSONObject node = new JSONObject();
            node.put("id", courseEntry.getKey());
            node.put("wheight", courseEntry.getValue().size());
            node.put("edges", new JSONArray(courseEntry.getValue()));
            if(courseIds.contains(courseEntry.getKey()))
            	node.put("groupId", 0);
            else
            	node.put("groupId", 1);
            nodes.put(node);
        }

        result.put("nodes", nodes);
 */