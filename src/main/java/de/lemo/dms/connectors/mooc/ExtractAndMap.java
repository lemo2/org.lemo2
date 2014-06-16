/**
 * File ./src/main/java/de/lemo/dms/connectors/iversity/ExtractAndMap.java
 * Lemo-Data-Management-Server for learning analytics.
 * Copyright (C) 2013
 * Leonard Kappe, Andreas Pursian, Sebastian Schwarzrock, Boris Wenzlaff
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/

/**
 * File ./main/java/de/lemo/dms/connectors/iversity/ExtractAndMap.java
 * Date 2013-01-24
 * Project Lemo Learning Analytics
 */

package de.lemo.dms.connectors.mooc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import de.lemo.dms.connectors.IConnector;
import de.lemo.dms.core.Clock;
import de.lemo.dms.core.config.ServerConfiguration;
import de.lemo.dms.db.DBConfigObject;
import de.lemo.dms.db.EQueryType;
import de.lemo.dms.db.IDBHandler;
import de.lemo.dms.db.mapping.CollaborationLog;
import de.lemo.dms.db.mapping.CollaborationType;
import de.lemo.dms.db.mapping.Course;
import de.lemo.dms.db.mapping.CourseCollaboration;
import de.lemo.dms.db.mapping.CourseLearning;
import de.lemo.dms.db.mapping.CourseAssessment;
import de.lemo.dms.db.mapping.CourseUser;
import de.lemo.dms.db.mapping.LearningObj;
import de.lemo.dms.db.mapping.LearningLog;
import de.lemo.dms.db.mapping.Platform;
import de.lemo.dms.db.mapping.CollaborationObj;
import de.lemo.dms.db.mapping.Config;
import de.lemo.dms.db.mapping.LearningType;
import de.lemo.dms.db.mapping.Role;
import de.lemo.dms.db.mapping.Assessment;
import de.lemo.dms.db.mapping.AssessmentLog;
import de.lemo.dms.db.mapping.AssessmentType;
import de.lemo.dms.db.mapping.AssessmentUser;
import de.lemo.dms.db.mapping.User;

/**
 * The main class of the extraction process.
 * Inherit from this class to make an extract class for a specific LMS.
 * Contains bundle of fields as container for LMS objects,
 * which are used for linking the tables.
 */
public abstract class ExtractAndMap {

	// lists of object tables which are new found in LMS DB
	/** A List of new entries in the course table found in this run of the process. */

	protected Map<Long, Course> courseMining;
	protected Map<Long, Platform> platformMining;
	protected Map<Long, LearningObj> learningObjectMining;
	protected Map<Long, Assessment> assessmentMining;
	protected Map<Long, CollaborationObj> collaborativeObjectMining;
	protected Map<Long, User> userMining;
	protected Map<Long, Role> roleMining;
	
	
	protected Map<Long, AssessmentUser> assessmentUserMining;
	protected Map<Long, CourseLearning> courseLearningObjectMining;
	protected Map<Long, CourseCollaboration> courseCollaborativeObjectMining;
	protected Map<Long, CourseAssessment> courseAssessmentMining;
	protected Map<Long, CourseUser> courseUserMining;
	
	protected Map<String, AssessmentType> assessmentTypeMining;
	protected Map<String, CollaborationType> collaborativeObjectTypeMining;
	protected Map<String, LearningType> learningObjectTypeMining;
	
	protected Map<Long, Course> oldCourseMining;
	protected Map<Long, Platform> oldPlatformMining;
	protected Map<Long, LearningObj> oldLearningObjectMining;
	protected Map<Long, CollaborationObj> oldCollaborativeObjectMining;
	protected Map<Long, Assessment> oldAssessmentMining;
	protected Map<Long, User> oldUserMining;
	protected Map<Long, Role> oldRoleMining;


	
	
	protected Map<Long, AssessmentUser> oldAssessmentUserMining;
	protected Map<Long, CourseLearning> oldCourseLearningObjectMining;
	protected Map<Long, CourseCollaboration> oldCourseCollaborativeObjectMining;
	protected Map<Long, CourseAssessment> oldCourseAssessmentMining;
	protected Map<Long, CourseUser> oldCourseUserMining;
	
	protected Map<String, AssessmentType> oldAssessmentTypeMining;
	protected Map<String, LearningType> oldLearningObjectTypeMining;
	protected Map<String, CollaborationType> oldCollaborativeObjectTypeMining;


	/** A list of objects used for submitting them to the DB. */
	List<Collection<?>> updates;
	/** A list of timestamps of the previous runs of the extractor. */
	List<Timestamp> configMiningTimestamp;
	/** Designates which entries should be read from the LMS Database during the process. */
	private long starttime;

	/** Database-handler **/
	IDBHandler dbHandler;

	private final Logger logger = Logger.getLogger(this.getClass());

	private final IConnector connector;

	public ExtractAndMap(final IConnector connector) {
		this.connector = connector;
	}

	protected Long learnLogMax;

	protected Long collaborationLogMax;

	protected Long assessmentLogMax;
	
	protected Long learningObjectTypeMax;
	
	protected Long collaborativeObjectTypeMax;
	
	protected Long assessmentTypeMax;

	protected Long maxLog = 0L;
	
	private Clock c;

	/**
	 * Starts the extraction process by calling getLMS_tables() and saveMining_tables().
	 * A timestamp can be given as optional argument.
	 * When the argument is used the extraction begins after that timestamp.
	 * When no argument is given the program starts with the timestamp of the last run.
	 * 
	 * @param args
	 *            Optional arguments for the process. Used for the selection of the ExtractAndMap Implementation and
	 *            timestamp when the extraction should start.
	 **/
	public void start(final String[] args, final DBConfigObject sourceDBConf, List<Long> courses, List<String> logins) {

		this.dbHandler = ServerConfiguration.getInstance().getMiningDbHandler();
		this.c = new Clock();
		this.starttime = System.currentTimeMillis() / 1000;
		final Session session = this.dbHandler.getMiningSession();

		// get the status of the mining DB; load timestamp of last update and objects needed for associations
		long readingtimestamp = this.getMiningInitial();

		this.dbHandler.saveToDB(session, new Platform(this.connector.getPlatformId(), this.connector.getName(),
				this.connector.getPlattformType().toString(), this.connector.getPrefix()));
		this.platformMining = new HashMap<Long, Platform>();
		this.platformMining.put(this.connector.getPlatformId(), new Platform(this.connector.getPlatformId(), this.connector.getName(),
				this.connector.getPlattformType().toString(), this.connector.getPrefix()));
		logger.info("Initialized database in " + this.c.getAndReset());
		// default call without parameter
		if (args.length == 1)
		{
			// get the needed tables from LMS DB
			this.c.reset();
			this.getLMStables(sourceDBConf, readingtimestamp, courses, logins);
			logger.info("Loaded data from source in " + this.c.getAndReset());

			// create and write the mining database tables
			this.saveMiningTables();
		}
		// call with parameter timestamp
		else {
			if (args[1].matches("[0-9]+"))
			{
				readingtimestamp = Long.parseLong(args[1]);
				long readingtimestamp2 = Long.parseLong(args[1]) + 172800;
				final long currenttimestamp = this.starttime;
				this.logger.info("starttime:" + currenttimestamp);
				this.logger.info("parameter:" + readingtimestamp);

				// first read & save LMS DB tables from 0 to starttime for timestamps which are not set(which are 0)
				if (this.configMiningTimestamp.get(0) == null) {
					this.c.reset();
					this.getLMStables(sourceDBConf, 0, readingtimestamp, courses, logins);
					logger.info("Loaded data from source in " + this.c.getAndReset());
					// create and write the mining database tables
					this.saveMiningTables();
				}

				// read & save LMS DB in steps of 2 days
				for (long looptimestamp = readingtimestamp - 1; looptimestamp < currenttimestamp;)
				{
					this.logger.info("looptimestamp:" + looptimestamp);
					this.c.reset();
					this.getLMStables(sourceDBConf, looptimestamp + 1, readingtimestamp2, courses, logins);
					logger.info("Loaded data from source in " + this.c.getAndReset());
					looptimestamp += 172800;
					readingtimestamp2 += 172800;
					this.logger.info("currenttimestamp:" + currenttimestamp);
					this.saveMiningTables();
				}
			}
			else {
				// Fehlermeldung Datenformat
				this.logger.info("wrong data format in parameter:" + args[1]);
			}
		}

		// calculate running time of extract process
		final long endtime = System.currentTimeMillis() / 1000;
		final Config config = new Config();
		config.setLastModifiedLong(System.currentTimeMillis());
		config.setElapsedTime((endtime) - (this.starttime));
		config.setDatabaseModel("2.0");
		config.setPlatform(this.connector.getPlatformId());
		config.setLatestTimestamp(maxLog);
		this.dbHandler.saveToDB(session, config);
		this.logger.info("Elapsed time: " + (endtime - this.starttime) + "s");
		this.dbHandler.closeSession(session);
	}

	/**
	 * Reads the Mining Database.
	 * Initial informations needed to start the process of updating are collected here.
	 * The Timestamp of the last run of the extractor is read from the config table
	 * and the objects which might been needed to associate are read and saved here.
	 * 
	 * @return The timestamp of the last run of the extractor. If this is the first run it will be set 0.
	 **/
	public long getMiningInitial() {

		final Session session = this.dbHandler.getMiningSession();

		List<?> t;

		Long readingtimestamp;
		readingtimestamp = (Long) session.createQuery("Select max(latestTimestamp) from Config where platform=" + this.connector.getPlatformId()).uniqueResult();
		
		if(readingtimestamp == null)
		{
			readingtimestamp = -1L;
		}

		// load objects which are already in Mining DB for associations

		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from Course x where x.platform="
				+ this.connector.getPlatformId() + " order by x.id asc");
		this.oldCourseMining = new HashMap<Long, Course>();
		for (int i = 0; i < t.size(); i++) {
			this.oldCourseMining.put(((Course) (t.get(i))).getId(), (Course) t.get(i));
		}
		logger.info("Loaded " + this.oldCourseMining.size() + " Course objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from User x "
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldUserMining = new HashMap<Long, User>();
		for (int i = 0; i < t.size(); i++) {
			this.oldUserMining.put(((User) (t.get(i))).getId(), (User) t.get(i));
		}
		logger.info("Loaded " + this.oldUserMining.size() + " User objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from LearningObject x "
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldLearningObjectMining = new HashMap<Long, LearningObj>();
		for (int i = 0; i < t.size(); i++) {
			this.oldLearningObjectMining.put(((LearningObj) (t.get(i))).getId(), (LearningObj) t.get(i));
		}
		logger.info("Loaded " + this.oldLearningObjectMining.size() + " Resource objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from CollaborativeObject x "
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldCollaborativeObjectMining = new HashMap<Long, CollaborationObj>();
		for (int i = 0; i < t.size(); i++) {
			this.oldCollaborativeObjectMining.put(((CollaborationObj) (t.get(i))).getId(), (CollaborationObj) t.get(i));
		}
		logger.info("Loaded " + this.oldCollaborativeObjectMining.size() + " Collaborative objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from Task x "
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldAssessmentMining = new HashMap<Long, Assessment>();
		for (int i = 0; i < t.size(); i++) {
			this.oldAssessmentMining.put(((Assessment) (t.get(i))).getId(), (Assessment) t.get(i));
		}
		logger.info("Loaded " + this.oldAssessmentMining.size() + " Task objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from Role x "
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldRoleMining = new HashMap<Long, Role>();
		for (int i = 0; i < t.size(); i++) {
			this.oldRoleMining.put(((Role) (t.get(i))).getId(), (Role) t.get(i));
		}
		logger.info("Loaded " + this.oldRoleMining.size() + " Role objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from Platform x "
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldPlatformMining = new HashMap<Long, Platform>();
		for (int i = 0; i < t.size(); i++) {
			this.oldPlatformMining.put(((Platform) (t.get(i))).getId(), (Platform) t.get(i));
		}
		logger.info("Loaded " + this.oldPlatformMining.size() + " Platform objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from LearningObjectType x "
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldLearningObjectTypeMining = new HashMap<String, LearningType>();
		for (int i = 0; i < t.size(); i++) {
			this.oldLearningObjectTypeMining.put(((LearningType) (t.get(i))).getType(), (LearningType) t.get(i));
		}
		logger.info("Loaded " + this.oldLearningObjectTypeMining.size() + " ResourceType objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from CollaborativeObjectType x "
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldCollaborativeObjectTypeMining = new HashMap<String, CollaborationType>();
		for (int i = 0; i < t.size(); i++) {
			this.oldCollaborativeObjectTypeMining.put(((CollaborationType) (t.get(i))).getType(), (CollaborationType) t.get(i));
		}
		logger.info("Loaded " + this.oldCollaborativeObjectTypeMining.size() + " CollaborativeType objects from the mining database.");
		
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from TaskType x"
				//+ "where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldAssessmentTypeMining = new HashMap<String, AssessmentType>();
		for (int i = 0; i < t.size(); i++) {
			this.oldAssessmentTypeMining.put(((AssessmentType) (t.get(i))).getType(), (AssessmentType) t.get(i));
		}
		logger.info("Loaded " + this.oldAssessmentTypeMining.size() + " TaskType objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from CourseLearningObject x"
				//+ " where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldCourseLearningObjectMining = new HashMap<Long, CourseLearning>();
		for (int i = 0; i < t.size(); i++) {
			this.oldCourseLearningObjectMining.put(((CourseLearning) (t.get(i))).getId(), (CourseLearning) t.get(i));
		}
		logger.info("Loaded " + this.oldCourseLearningObjectMining.size() + " CourseResource objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from CourseTask x"
				//+ " where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldCourseAssessmentMining = new HashMap<Long, CourseAssessment>();
		for (int i = 0; i < t.size(); i++) {
			this.oldCourseAssessmentMining.put(((CourseAssessment) (t.get(i))).getId(), (CourseAssessment) t.get(i));
		}
		logger.info("Loaded " + this.oldCourseAssessmentMining.size() + " CourseTask objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from CourseUser x"
				//+ " where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldCourseUserMining = new HashMap<Long, CourseUser>();
		for (int i = 0; i < t.size(); i++) {
			this.oldCourseUserMining.put(((CourseUser) (t.get(i))).getId(), (CourseUser) t.get(i));
		}
		logger.info("Loaded " + this.oldCourseUserMining.size() + " CourseUser objects from the mining database.");
		
		t = this.dbHandler.performQuery(session, EQueryType.HQL, "from TaskUser x"
				//+ " where x.platform=" + this.connector.getPlatformId() 
				+ " order by x.id asc");
		this.oldAssessmentUserMining = new HashMap<Long, AssessmentUser>();
		for (int i = 0; i < t.size(); i++) {
			this.oldAssessmentUserMining.put(((AssessmentUser) (t.get(i))).getId(), (AssessmentUser) t.get(i));
		}
		logger.info("Loaded " + this.oldAssessmentUserMining.size() + " TaskUser objects from the mining database.");
		
		

		Criteria criteria = session.createCriteria(LearningLog.class);
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.max("id"));
		criteria.setProjection(pl);
		this.learnLogMax = (Long) criteria.list().get(0);
		if (this.learnLogMax == null) {
			this.learnLogMax = 0L;
		}
		
		criteria = session.createCriteria(CollaborationLog.class);
		criteria.setProjection(pl);
		this.collaborationLogMax = (Long) criteria.list().get(0);
		if (this.collaborationLogMax == null) {
			this.collaborationLogMax = 0L;
		}
		
		criteria = session.createCriteria(AssessmentLog.class);
		criteria.setProjection(pl);
		this.assessmentLogMax = (Long) criteria.list().get(0);
		if (this.assessmentLogMax == null) {
			this.assessmentLogMax = 0L;
		}
	
		criteria = session.createCriteria(LearningType.class);
		criteria.setProjection(pl);
		this.learningObjectTypeMax = (Long) criteria.list().get(0);
		if (this.learningObjectTypeMax == null) {
			this.learningObjectTypeMax = 0L;
		}
		
		criteria = session.createCriteria(CollaborationType.class);
		criteria.setProjection(pl);
		this.collaborativeObjectTypeMax = (Long) criteria.list().get(0);
		if (this.collaborativeObjectTypeMax == null) {
			this.collaborativeObjectTypeMax = 0L;
		}
		
		criteria = session.createCriteria(AssessmentType.class);
		criteria.setProjection(pl);
		this.assessmentTypeMax = (Long) criteria.list().get(0);
		if (this.assessmentTypeMax == null) {
			this.assessmentTypeMax = 0L;
		}
		
		//this.dbHandler.closeSession(session);
		return readingtimestamp;
	}

	/**
	 * Has to read the LMS Database.
	 * It starts reading elements with timestamp readingtimestamp and higher.
	 * It is supposed to be used for frequently and small updates.
	 * For a greater Mass of Data it is suggested to use getLMS_tables(long, long);
	 * If Hibernate is used to access the LMS DB too,
	 * it is suggested to write the found tables into lists of
	 * hibernate object model classes, which have to
	 * be created as global variables in this class.
	 * So they can be used in the generate methods of this class.
	 * 
	 * @param readingfromtimestamp
	 *            Only elements with timestamp readingtimestamp and higher are read here.
	 *            *
	 * @return the lM stables
	 */
	public abstract void getLMStables(DBConfigObject dbConf, long readingfromtimestamp, List<Long> courses, List<String> logins);

	/**
	 * Has to read the LMS Database.
	 * It reads elements with timestamp between readingfromtimestamp and readingtotimestamp.
	 * This method is used to read great DB in a step by step procedure.
	 * That is necessary for a great mass of Data when handeling an existing DB for example.
	 * If Hibernate is used to access the LMS DB too,
	 * it is suggested to write the found tables into lists of
	 * hibernate object model classes, which have to
	 * be created as global variables in this class.
	 * So they can be used in the generate methods of this class.
	 * 
	 * @param readingfromtimestamp
	 *            Only elements with timestamp readingfromtimestamp and higher are read here.
	 * @param readingtotimestamp
	 *            Only elements with timestamp readingtotimestamp and lower are read here.
	 *            *
	 * @return the lM stables
	 */
	public abstract void getLMStables(DBConfigObject dbConf, long readingfromtimestamp, long readingtotimestamp, List<Long> courses, List<String> logins);

	/**
	 * Has to clear the lists of LMS tables*.
	 */
	public abstract void clearLMStables();

	/**
	 * Clears the lists of mining tables.
	 **/
	public void clearMiningTables() {

		this.courseMining.clear();
		this.learningObjectMining.clear();
		this.collaborativeObjectMining.clear();
		this.userMining.clear();
		this.roleMining.clear();
		this.assessmentMining.clear();
		this.platformMining.clear();
		this.learningObjectTypeMining.clear();
		this.assessmentTypeMining.clear();
		this.courseLearningObjectMining.clear();
		this.courseCollaborativeObjectMining.clear();
		this.courseAssessmentMining.clear();
		this.assessmentUserMining.clear();
	}

	/**
	 * Only for successive readings. This is meant to be done, when the gathered mining data has already
	 * been saved and before the mining tables are cleared for the next iteration.
	 */
	public void prepareMiningData()
	{
		this.oldCourseMining.putAll(this.courseMining);
		this.oldLearningObjectMining.putAll(this.learningObjectMining);
		this.oldCollaborativeObjectMining.putAll(this.collaborativeObjectMining);
		this.oldUserMining.putAll(this.userMining);
		this.oldRoleMining.putAll(this.roleMining);
		this.oldCourseCollaborativeObjectMining.putAll(this.courseCollaborativeObjectMining);
		this.oldCourseLearningObjectMining.putAll(this.courseLearningObjectMining);
		this.oldCourseAssessmentMining.putAll(this.courseAssessmentMining);
		this.oldCourseUserMining.putAll(this.courseUserMining);
		this.oldLearningObjectTypeMining.putAll(this.learningObjectTypeMining);
		this.oldCollaborativeObjectTypeMining.putAll(this.collaborativeObjectTypeMining);
		this.oldAssessmentMining.putAll(this.assessmentMining);
		this.oldAssessmentTypeMining.putAll(this.assessmentTypeMining);
		this.oldAssessmentUserMining.putAll(this.assessmentUserMining);
	}

	/**
	 * Generates and save the new tables for the mining DB.
	 * We call the genereate-methods for each mining table to get the new entries.
	 * At last we create a Transaction and save the new entries to the DB.
	 **/
	public void saveMiningTables() {

		// generate & save new mining tables
		this.updates = new ArrayList<Collection<?>>();

		Long objects = 0L;

		// generate mining tables
		if (this.userMining == null) {

			this.c.reset();
			logger.info("\nObject tables:\n");

			this.courseMining = this.generateCourseMining();
			objects += this.courseMining.size();
			logger.info("Generated " + this.courseMining.size() + " Course entries in "
					+ this.c.getAndReset() + " s. ");
			this.updates.add(this.courseMining.values());

			this.assessmentTypeMining = new HashMap<String, AssessmentType>();
			
			this.learningObjectTypeMining = new HashMap<String, LearningType>();
			
			this.collaborativeObjectTypeMining = new HashMap<String, CollaborationType>();

			this.assessmentMining = this.generateAssessmentMining();
			
			this.collaborativeObjectMining = this.generateCollaborativeObjectMining();

			this.learningObjectMining = this.generateLearningObjectMining();
			
			this.updates.add(this.generateAssessmentTypeMining().values());
			objects += this.updates.get(this.updates.size() - 1).size();
			logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
					+ " TaskType entries in " + this.c.getAndReset() + " s. ");
			
			this.updates.add(this.generateLearningObjectTypeMining().values());
			objects += this.updates.get(this.updates.size() - 1).size();
			logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
					+ " LearningObjectType entries in " + this.c.getAndReset() + " s. ");
			
			this.updates.add(this.generateCollaborativeObjectTypeMining().values());
			objects += this.updates.get(this.updates.size() - 1).size();
			logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
					+ " CollaborativeObjectType entries in " + this.c.getAndReset() + " s. ");
			

			this.roleMining = this.generateRoleMining();
			objects += this.roleMining.size();
			logger.info("Generated " + this.roleMining.size() + " Role entries in " + this.c.getAndReset()
					+ " s. ");
			this.updates.add(this.roleMining.values());

			this.userMining = this.generateUserMining();
			objects += this.userMining.size();
			logger.info("Generated " + this.userMining.size() + " User entries in " + this.c.getAndReset()
					+ " s. ");
			this.updates.add(this.userMining.values());


			logger.info("\nAssociation tables:\n");
			
			
			objects += this.learningObjectMining.size();
			logger.info("Generated " + this.learningObjectMining.size() + " LearningObject entries in "
					+ this.c.getAndReset() + " s. ");
			this.updates.add(this.learningObjectMining.values());
			
			objects += this.assessmentMining.size();
			this.updates.add(this.assessmentMining.values());
			logger.info("Generated " + this.assessmentMining.size() + " Task entries in " + this.c.getAndReset()
					+ " s. ");
			
			objects += this.collaborativeObjectMining.size();
			this.updates.add(this.collaborativeObjectMining.values());
			logger.info("Generated " + this.collaborativeObjectMining.size() + " Collaborative entries in " + this.c.getAndReset()
					+ " s. ");

			this.updates.add(this.generateCourseLearningObjectMining().values());
			objects += this.updates.get(this.updates.size() - 1).size();
			logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
					+ " CourseLearningObject entries in " + this.c.getAndReset() + " s. ");
			
			this.updates.add(this.generateCourseAssessmentMining().values());
			objects += this.updates.get(this.updates.size() - 1).size();
			logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
					+ " CourseTask entries in " + this.c.getAndReset() + " s. ");
			
			this.updates.add(this.generateCourseCollaborativeObjectMining().values());
			objects += this.updates.get(this.updates.size() - 1).size();
			logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
					+ " CourseCollaborativeObject entries in " + this.c.getAndReset() + " s. ");
			



		}

		this.updates.add(this.generateCourseUserMining().values());
		objects += this.updates.get(this.updates.size() - 1).size();
		logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
				+ " CourseUser entries in " + this.c.getAndReset() + " s. ");

		this.updates.add(this.generateAssessmentUserMining().values());
		objects += this.updates.get(this.updates.size() - 1).size();
		logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
				+ " TaskUser entries in " + this.c.getAndReset() + " s. ");
		
		logger.info("\nLog tables:\n");

		this.updates.add(this.generateLearningLogMining().values());
		objects += this.updates.get(this.updates.size() - 1).size();
		logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
				+ " ViewLog entries in " + this.c.getAndReset() + " s. ");
		
		this.updates.add(this.generateCollaborativeLogMining().values());
		objects += this.updates.get(this.updates.size() - 1).size();
		logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
				+ " CollaborativeLog entries in " + this.c.getAndReset() + " s. ");
		
		this.updates.add(this.generateAssessmentLogMining().values());
		objects += this.updates.get(this.updates.size() - 1).size();
		logger.info("Generated " + this.updates.get(this.updates.size() - 1).size()
				+ " TaskLog entries in " + this.c.getAndReset() + " s. ");
		
		if (objects > 0)
		{
			final Session session = this.dbHandler.getMiningSession();
			logger.info("Writing to DB");
			this.dbHandler.saveCollectionToDB(session, this.updates);
		}

		this.clearLMStables();
		this.updates.clear();

	}

	// methods for create and fill the mining-table instances
	/**
	 * Has to create and fill the course_user table.
	 * This table describes which user is enrolled in which course in which timesspan.
	 * The attributes are described in the documentation of the course_user_mining class.
	 * Please use the getter and setter predefined in the course_user_mining class to fill the tables within this
	 * method.
	 * 
	 * @return A list of instances of the course_user table representing class.
	 **/
	abstract Map<Long, CourseUser> generateCourseUserMining();

	/**
	 * Has to create and fill the course_forum table.
	 * This table describes which forum is used in which course.
	 * The attributes are described in the documentation of the course_forum_mining class.
	 * Please use the getter and setter predefined in the course_forum_mining class to fill the tables within this
	 * method.
	 * 
	 * @return A list of instances of the course_forum table representing class.
	 **/
	abstract Map<Long, CourseAssessment> generateCourseAssessmentMining();
	
	abstract Map<Long, CourseCollaboration> generateCourseCollaborativeObjectMining();

	/**
	 * Has to create and fill the course table.
	 * This table describes the courses in the LMS.
	 * The attributes are described in the documentation of the course_mining class.
	 * Please use the getter and setter predefined in the course_mining class to fill the tables within this method.
	 * 
	 * @return A list of instances of the course table representing class.
	 **/
	abstract Map<Long, Course> generateCourseMining();

	/**
	 * Has to create and fill the course_group table.
	 * This table describes which groups are used in which courses.
	 * The attributes are described in the documentation of the course_group_mining class.
	 * Please use the getter and setter predefined in the course_group_mining class to fill the tables within this
	 * method.
	 * 
	 * @return A list of instances of the course_group table representing class.
	 **/
	abstract Map<Long, CourseLearning> generateCourseLearningObjectMining();


	/**
	 * Has to create and fill the course_scorm table.
	 * This table describes which scorm packages are used in which courses.
	 * The attributes are described in the documentation of the course_scorm_mining class.
	 * Please use the getter and setter predefined in the course_scorm_mining class to fill the tables within this
	 * method.
	 * 
	 * @return A list of instances of the course_scorm table representing class.
	 **/
	abstract Map<Long, AssessmentUser> generateAssessmentUserMining();
	
	
	/**
	 * Has to create and fill the course_log table.
	 * This table contains the actions which are done on courses.
	 * The attributes are described in the documentation of the course_log_mining class.
	 * Please use the getter and setter predefined in the course_log_mining class to fill the tables within this method.
	 * 
	 * @return A list of instances of the course_log table representing class.
	 **/
	abstract Map<Long, LearningLog> generateLearningLogMining();


	/**
	 * Has to create and fill the forum_log table.
	 * This table contains the actions which are done on forums.
	 * The attributes are described in the documentation of the forum_log_mining class.
	 * Please use the getter and setter predefined in the forum_log_mining class to fill the tables within this method.
	 * 
	 * @return A list of instances of the forum_log table representing class.
	 **/
	abstract Map<Long, CollaborationLog> generateCollaborativeLogMining();

	/**
	 * Has to create and fill the forum table.
	 * This table describes the forums in the LMS.
	 * The attributes are described in the documentation of the forum_mining class.
	 * Please use the getter and setter predefined in the forum_mining class to fill the tables within this method.
	 * 
	 * @return A list of instances of the forum table representing class.
	 **/
	abstract Map<Long, LearningObj> generateLearningObjectMining();
	
	abstract Map<Long, CollaborationObj> generateCollaborativeObjectMining();

	/**
	 * Has to create and fill the group table.
	 * This table describes the groups in the LMS.
	 * The attributes are described in the documentation of the group_mining class.
	 * Please use the getter and setter predefined in the group_mining class to fill the tables within this method.
	 * 
	 * @return A list of instances of the group table representing class.
	 **/
	abstract Map<Long, User> generateUserMining();

	/**
	 * Has to create and fill the quiz_log table.
	 * This table contains the actions which are done on quiz.
	 * The attributes are described in the documentation of the quiz_log_mining class.
	 * Please use the getter and setter predefined in the quiz_log_mining class to fill the tables within this method.
	 * 
	 * @return A list of instances of the quiz_log table representing class.
	 **/
	abstract Map<Long, Assessment> generateAssessmentMining();

	/**
	 * Has to create and fill the assignment_log table.
	 * This table contains the actions which are done on assignment.
	 * The attributes are described in the documentation of the assignment_log_mining class.
	 * Please use the getter and setter predefined in the assignment_log_mining class to fill the tables within this
	 * method.
	 * 
	 * @return A list of instances of the assignment_log table representing class.
	 **/
	abstract Map<Long, Role> generateRoleMining();

	/**
	 * Has to create and fill the scorm_log table.
	 * This table contains the actions which are done on scorm.
	 * The attributes are described in the documentation of the scorm_log_mining class.
	 * Please use the getter and setter predefined in the scorm_log_mining class to fill the tables within this method.
	 * 
	 * @return A list of instances of the scorm_log table representing class.
	 **/
	abstract Map<Long, AssessmentLog> generateAssessmentLogMining();

	/**
	 * Has to create and fill the quiz_user table.
	 * This table describes which user gets which grade in which quiz.
	 * The attributes are described in the documentation of the quiz_user_mining class.
	 * Please use the getter and setter predefined in the quiz_user_mining class to fill the tables within this method.
	 * 
	 * @return A list of instances of the quiz_user table representing class.
	 **/

	abstract Map<String, LearningType> generateLearningObjectTypeMining();
	
	abstract Map<String, CollaborationType> generateCollaborativeObjectTypeMining();
	
	abstract Map<String, AssessmentType> generateAssessmentTypeMining();

}