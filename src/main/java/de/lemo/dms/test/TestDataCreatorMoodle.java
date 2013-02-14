/**
 * File ./main/java/de/lemo/dms/test/TestDataCreatorMoodle.java
 * Date 2013-01-24
 * Project Lemo Learning Analytics
 * Copyright TODO (INSERT COPYRIGHT)
 */

package de.lemo.dms.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Assignment_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Assignment_submissions_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.ChatLog_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Chat_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Context_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.CourseCategories_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Course_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Forum_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Forum_discussions_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Forum_posts_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Grade_grades_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Grade_items_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Groups_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Groups_members_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Log_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Question_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Question_states_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Quiz_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Quiz_grades_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Quiz_question_instances_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Resource_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Role_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Role_assignments_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Scorm_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.User_LMS;
import de.lemo.dms.connectors.moodleNumericId.moodleDBclass.Wiki_LMS;
import de.lemo.dms.core.config.ServerConfiguration;
import de.lemo.dms.db.IDBHandler;
import de.lemo.dms.db.miningDBclass.AssignmentLogMining;
import de.lemo.dms.db.miningDBclass.AssignmentMining;
import de.lemo.dms.db.miningDBclass.ChatLogMining;
import de.lemo.dms.db.miningDBclass.ChatMining;
import de.lemo.dms.db.miningDBclass.CourseAssignmentMining;
import de.lemo.dms.db.miningDBclass.CourseForumMining;
import de.lemo.dms.db.miningDBclass.CourseGroupMining;
import de.lemo.dms.db.miningDBclass.CourseLogMining;
import de.lemo.dms.db.miningDBclass.CourseMining;
import de.lemo.dms.db.miningDBclass.CourseQuizMining;
import de.lemo.dms.db.miningDBclass.CourseResourceMining;
import de.lemo.dms.db.miningDBclass.CourseScormMining;
import de.lemo.dms.db.miningDBclass.CourseUserMining;
import de.lemo.dms.db.miningDBclass.CourseWikiMining;
import de.lemo.dms.db.miningDBclass.LevelCourseMining;
import de.lemo.dms.db.miningDBclass.LevelMining;
import de.lemo.dms.db.miningDBclass.LevelAssociationMining;
import de.lemo.dms.db.miningDBclass.ForumLogMining;
import de.lemo.dms.db.miningDBclass.ForumMining;
import de.lemo.dms.db.miningDBclass.GroupMining;
import de.lemo.dms.db.miningDBclass.GroupUserMining;
import de.lemo.dms.db.miningDBclass.QuestionLogMining;
import de.lemo.dms.db.miningDBclass.QuestionMining;
import de.lemo.dms.db.miningDBclass.QuizLogMining;
import de.lemo.dms.db.miningDBclass.QuizMining;
import de.lemo.dms.db.miningDBclass.QuizQuestionMining;
import de.lemo.dms.db.miningDBclass.QuizUserMining;
import de.lemo.dms.db.miningDBclass.ResourceLogMining;
import de.lemo.dms.db.miningDBclass.ResourceMining;
import de.lemo.dms.db.miningDBclass.RoleMining;
import de.lemo.dms.db.miningDBclass.ScormLogMining;
import de.lemo.dms.db.miningDBclass.ScormMining;
import de.lemo.dms.db.miningDBclass.UserMining;
import de.lemo.dms.db.miningDBclass.WikiLogMining;
import de.lemo.dms.db.miningDBclass.WikiMining;
import de.lemo.dms.db.miningDBclass.abstractions.ILogMining;

/**
 * Class to test the moodle data creator
 * @author Sebastian Schwarzrock
 *
 */
public class TestDataCreatorMoodle {

	private ArrayList<AssignmentMining> assignmentList;
	private ArrayList<AssignmentLogMining> assignmentLogList;
	private ArrayList<ChatMining> chatList;
	private ArrayList<ChatLogMining> chatLogList;
	private ArrayList<CourseMining> courseList;
	private ArrayList<CourseLogMining> courseLogList;
	private ArrayList<CourseAssignmentMining> courseAssignmentList;
	private ArrayList<CourseForumMining> courseForumList;
	private ArrayList<CourseGroupMining> courseGroupList;
	private ArrayList<CourseQuizMining> courseQuizList;
	private ArrayList<CourseResourceMining> courseResourceList;
	private ArrayList<CourseScormMining> courseScormList;
	private ArrayList<CourseUserMining> courseUserList;
	private ArrayList<CourseWikiMining> courseWikiList;
	private ArrayList<ForumMining> forumList;
	private ArrayList<ForumLogMining> forumLogList;
	private ArrayList<GroupMining> groupList;
	private ArrayList<GroupUserMining> groupUserList;
	private ArrayList<QuestionMining> questionList;
	private ArrayList<QuestionLogMining> questionLogList;
	private ArrayList<QuizMining> quizList;
	private ArrayList<QuizLogMining> quizLogList;
	private ArrayList<QuizQuestionMining> quizQuestionList;
	private ArrayList<QuizUserMining> quizUserList;
	private ArrayList<ResourceLogMining> resourceLogList;
	private ArrayList<ResourceMining> resourceList;
	private ArrayList<RoleMining> roleList;
	private ArrayList<ScormMining> scormList;
	private ArrayList<ScormLogMining> scormLogList;
	private ArrayList<UserMining> userList;
	private ArrayList<WikiMining> wikiList;
	private ArrayList<WikiLogMining> wikiLogList;
	private ArrayList<LevelMining> departmentList;
	private ArrayList<LevelMining> degreeList;
	private ArrayList<LevelAssociationMining> departmentDegreeList;
	private ArrayList<LevelCourseMining> degreeCourseList;

	private static List<Log_LMS> logLms = new ArrayList<Log_LMS>();
	private static List<Resource_LMS> resourceLms = new ArrayList<Resource_LMS>();
	private static List<Course_LMS> courseLms = new ArrayList<Course_LMS>();
	private static List<Forum_LMS> forumLms = new ArrayList<Forum_LMS>();
	private static List<Wiki_LMS> wikiLms = new ArrayList<Wiki_LMS>();
	private static List<User_LMS> userLms = new ArrayList<User_LMS>();
	private static List<Quiz_LMS> quizLms = new ArrayList<Quiz_LMS>();
	private static List<Quiz_question_instances_LMS> quizQuestionInstancesLms = new ArrayList<Quiz_question_instances_LMS>();
	private static List<Question_LMS> questionLms = new ArrayList<Question_LMS>();
	private static List<Groups_LMS> groupLms = new ArrayList<Groups_LMS>();
	private static List<Groups_members_LMS> groupMembersLms = new ArrayList<Groups_members_LMS>();
	private static List<Question_states_LMS> questionStatesLms = new ArrayList<Question_states_LMS>();
	private static List<Forum_posts_LMS> forumPostsLms = new ArrayList<Forum_posts_LMS>();
	private static List<Role_LMS> roleLms = new ArrayList<Role_LMS>();
	private static List<Context_LMS> contextLms = new ArrayList<Context_LMS>();
	private static List<Role_assignments_LMS> roleAssignmentsLms = new ArrayList<Role_assignments_LMS>();
	private static List<Assignment_LMS> assignmentLMS = new ArrayList<Assignment_LMS>();
	private static List<Assignment_submissions_LMS> assignmentSubmissionLms = new ArrayList<Assignment_submissions_LMS>();
	private static List<Quiz_grades_LMS> quizGradesLms = new ArrayList<Quiz_grades_LMS>();
	private static List<Forum_discussions_LMS> forumDiscussionsLms = new ArrayList<Forum_discussions_LMS>();
	private static List<Scorm_LMS> scormLms = new ArrayList<Scorm_LMS>();
	private static List<Grade_grades_LMS> gradeGradesLms = new ArrayList<Grade_grades_LMS>();
	private static List<Grade_items_LMS> gradeItemsLms = new ArrayList<Grade_items_LMS>();
	private static List<Chat_LMS> chatLms = new ArrayList<Chat_LMS>();
	private static List<ChatLog_LMS> chatLogLms = new ArrayList<ChatLog_LMS>();
	private static List<CourseCategories_LMS> courseCategoriesLms = new ArrayList<CourseCategories_LMS>();

	private HashMap<Long, CourseMining> couAssMap;
	private HashMap<Long, CourseMining> couForMap;
	private HashMap<Long, CourseMining> couGroMap;
	private HashMap<Long, CourseMining> couQuiMap;
	private HashMap<Long, CourseMining> couUseMap;
	private HashMap<Long, CourseMining> couResMap;
	private HashMap<Long, CourseMining> couScoMap;
	private HashMap<Long, CourseMining> couWikMap;

	private HashMap<Long, LevelMining> degCouMap;
	private HashMap<Long, LevelMining> depDegMap;

	private void generateUserLMS()
	{
		for (final UserMining item : this.userList)
		{
			final User_LMS lms = new User_LMS();

			lms.setId(item.getId());
			lms.setCurrentlogin(item.getCurrentLogin());
			lms.setLastaccess(item.getLastAccess());
			lms.setFirstaccess(item.getFirstAccess());
			lms.setLastlogin(item.getLastLogin());

			TestDataCreatorMoodle.userLms.add(lms);
		}
	}

	private void generateRoleLMS()
	{
		for (final RoleMining item : this.roleList)
		{
			final Role_LMS lms = new Role_LMS();

			lms.setId(item.getId());
			lms.setName(item.getName());
			lms.setShortname(item.getShortname());
			lms.setSortorder(item.getSortOrder());
			lms.setDescription(item.getDescription());

			TestDataCreatorMoodle.roleLms.add(lms);
		}
	}

	private void generateWikiLMS()
	{
		for (final WikiMining item : this.wikiList)
		{
			final Wiki_LMS lms = new Wiki_LMS();
			lms.setId(item.getId());
			lms.setName(item.getTitle());
			lms.setTimemodified(item.getTimeModified());
			lms.setSummary(item.getSummary());
			lms.setCourse(this.couWikMap.get(item.getId()).getId());

			TestDataCreatorMoodle.wikiLms.add(lms);
		}
	}

	private void generateScormLMS()
	{
		for (final ScormMining item : this.scormList)
		{
			final Scorm_LMS lms = new Scorm_LMS();
			lms.setId(item.getId());
			lms.setName(item.getTitle());
			lms.setTimemodified(item.getTimeModified());
			lms.setMaxgrade(item.getMaxGrade());
			lms.setTimemodified(item.getTimeModified());
			lms.setCourse(this.couScoMap.get(item.getId()).getId());

			TestDataCreatorMoodle.scormLms.add(lms);
		}
	}

	private void generateContextLMS()
	{

		final HashMap<Long, Context_LMS> depIdMap = new HashMap<Long, Context_LMS>();
		final HashMap<Long, Context_LMS> degIdMap = new HashMap<Long, Context_LMS>();
		final HashMap<Long, Context_LMS> couIdMap = new HashMap<Long, Context_LMS>();

		// Create entries (context, course_categories) for all departments
		for (final LevelMining item : this.departmentList)
		{
			final Context_LMS lms = new Context_LMS();
			final CourseCategories_LMS lms2 = new CourseCategories_LMS();

			lms2.setId(TestDataCreatorMoodle.courseCategoriesLms.size() + 1);
			lms2.setTitle(item.getTitle());
			lms2.setDepth(1);
			lms2.setPath("/" + item.getId());

			lms.setContextlevel(40);
			lms.setDepth(2);
			lms.setInstanceid(lms2.getId());
			lms.setId(TestDataCreatorMoodle.contextLms.size() + 1);
			lms.setPath("/1/" + lms.getId());

			TestDataCreatorMoodle.courseCategoriesLms.add(lms2);

			TestDataCreatorMoodle.contextLms.add(lms);
			depIdMap.put(item.getId(), lms);
		}

		// Create entries (context, course_categories) for all degrees
		for (final LevelMining item : this.degreeList)
		{
			final CourseCategories_LMS lms2 = new CourseCategories_LMS();

			final LevelMining dm = this.depDegMap.get(item.getId());

			lms2.setId(TestDataCreatorMoodle.courseCategoriesLms.size() + 1);
			lms2.setTitle(item.getTitle());
			lms2.setDepth(2);
			lms2.setPath("/" + depIdMap.get(dm.getId()).getId() + "/" + lms2.getId());

			final Context_LMS lms = new Context_LMS();
			lms.setContextlevel(40);
			lms.setDepth(3);
			lms.setInstanceid(lms2.getId());
			lms.setId(TestDataCreatorMoodle.contextLms.size() + 1);
			final Context_LMS cl = depIdMap.get(dm.getId());
			lms.setPath(cl.getPath() + "/" + lms.getId());

			degIdMap.put(item.getId(), lms);
			TestDataCreatorMoodle.contextLms.add(lms);

			TestDataCreatorMoodle.courseCategoriesLms.add(lms2);

		}
		// Create entries (context) for all courses
		for (final LevelCourseMining item : this.degreeCourseList)
		{
			final Context_LMS lms = new Context_LMS();

			lms.setId(TestDataCreatorMoodle.contextLms.size() + 1);
			lms.setDepth(4);
			lms.setContextlevel(50);
			lms.setInstanceid(item.getCourse().getId());
			final Context_LMS cl = degIdMap.get(item.getLevel().getId());
			lms.setPath(cl.getPath() + "/" + lms.getId());

			couIdMap.put(item.getId(), lms);

			TestDataCreatorMoodle.contextLms.add(lms);
		}
		// Create entries (role_assignments) for all users
		for (final CourseUserMining item : this.courseUserList)
		{
			final Role_assignments_LMS lms2 = new Role_assignments_LMS();

			lms2.setId(item.getId());
			lms2.setRoleid(item.getRole().getId());
			lms2.setUserid(item.getUser().getId());
			lms2.setTimeend(item.getEnrolend());
			lms2.setTimestart(item.getEnrolstart());
			lms2.setContextid(couIdMap.get(item.getCourse().getId()).getId());

			TestDataCreatorMoodle.roleAssignmentsLms.add(lms2);
		}

	}

	private void generateLogLMS()
	{
		final List<ILogMining> logs = new ArrayList<ILogMining>();
		logs.addAll(this.resourceLogList);
		logs.addAll(this.assignmentLogList);
		logs.addAll(this.forumLogList);
		logs.addAll(this.courseLogList);
		logs.addAll(this.quizLogList);
		logs.addAll(this.wikiLogList);
		logs.addAll(this.scormLogList);

		Collections.sort(logs);

		for (int i = 0; i < logs.size(); i++)
		{
			logs.get(i).setId(i + 1);
		}

		for (final ResourceLogMining item : this.resourceLogList)
		{
			final Log_LMS lms = new Log_LMS();

			lms.setId(item.getId());
			lms.setCourse(item.getCourse().getId());
			lms.setModule("resource");
			lms.setTime(item.getTimestamp());
			lms.setAction(item.getAction());
			lms.setInfo(item.getResource().getId() + "");
			lms.setUserid(item.getUser().getId());

			TestDataCreatorMoodle.logLms.add(lms);
		}
		final HashMap<Long, Forum_discussions_LMS> forDisSet = new HashMap<Long, Forum_discussions_LMS>();
		for (final ForumLogMining item : this.forumLogList)
		{
			final Log_LMS lms = new Log_LMS();
			final Forum_posts_LMS lms2 = new Forum_posts_LMS();

			lms2.setId(TestDataCreatorMoodle.forumPostsLms.size() + 1);
			lms2.setMessage(item.getMessage());
			lms2.setSubject(item.getSubject());
			lms2.setUserid(item.getUser().getId());
			lms2.setCreated(item.getTimestamp());
			lms2.setModified(item.getTimestamp());

			TestDataCreatorMoodle.forumPostsLms.add(lms2);

			if ((item.getAction().equals("add discussion") || item.getAction().equals("view discussion")))
			{
				final Forum_discussions_LMS lms3 = new Forum_discussions_LMS();

				lms3.setId(item.getForum().getId());
				lms3.setForum(item.getForum().getId());

				forDisSet.put(lms3.getId(), lms3);

			}
			lms.setId(item.getId());
			lms.setAction(item.getAction());
			lms.setModule("forum");
			lms.setInfo(item.getForum().getId() + "");
			lms.setTime(item.getTimestamp());
			lms.setCourse(item.getCourse().getId());
			lms.setUserid(item.getUser().getId());

			TestDataCreatorMoodle.logLms.add(lms);
		}
		TestDataCreatorMoodle.forumDiscussionsLms.addAll(forDisSet.values());
		for (final AssignmentLogMining item : this.assignmentLogList)
		{
			final Log_LMS lms = new Log_LMS();

			lms.setId(item.getId());
			lms.setAction(item.getAction());
			lms.setModule("assignment");
			lms.setInfo(item.getAssignment().getId() + "");
			lms.setTime(item.getTimestamp());
			lms.setCourse(item.getCourse().getId());
			lms.setUserid(item.getUser().getId());

			TestDataCreatorMoodle.logLms.add(lms);

			if (item.getAction().equals("upload"))
			{
				final Assignment_submissions_LMS lms2 = new Assignment_submissions_LMS();

				lms2.setGrade(item.getGrade().longValue());
				lms2.setAssignment(item.getAssignment().getId());
				lms2.setUserid(item.getUser().getId() + "");
				lms2.setTimemodified(item.getTimestamp());
				lms2.setId(TestDataCreatorMoodle.assignmentSubmissionLms.size() + 1);

				TestDataCreatorMoodle.assignmentSubmissionLms.add(lms2);
			}
		}
		for (final QuizLogMining item : this.quizLogList)
		{
			final Log_LMS lms = new Log_LMS();
			lms.setId(item.getId());
			lms.setAction(item.getAction());
			lms.setModule("quiz");
			lms.setInfo(item.getQuiz().getId() + "");
			lms.setTime(item.getTimestamp());
			lms.setCourse(item.getCourse().getId());
			lms.setUserid(item.getUser().getId());

			if (!item.getAction().equals("review"))
			{
				final Quiz_grades_LMS lms2 = new Quiz_grades_LMS();
				lms2.setGrade(item.getGrade());
				lms2.setTimemodified(item.getTimestamp());
				lms2.setUserid(item.getUser().getId() + "");
				lms2.setQuiz(item.getQuiz().getId());
				lms2.setId(TestDataCreatorMoodle.quizGradesLms.size() + 1);

				TestDataCreatorMoodle.quizGradesLms.add(lms2);
			}

			TestDataCreatorMoodle.logLms.add(lms);
		}
		/*
		 * for(QuestionLogMining item : questionLogList)
		 * {
		 * Log_LMS lms = new Log_LMS();
		 * lms.setId(item.getId());
		 * lms.setAction(item.getAction());
		 * lms.setModule("forum");
		 * lms.setInfo(item.getQuestion().getId()+"");
		 * lms.setTime(item.getTimestamp());
		 * lms.setCourse(item.getCourse().getId());
		 * lms.setUserid(item.getUser().getId()+"");
		 * log_lms.add(lms);
		 * }
		 */
		for (final ScormLogMining item : this.scormLogList)
		{
			final Log_LMS lms = new Log_LMS();
			lms.setId(item.getId());
			lms.setAction(item.getAction());
			lms.setModule("scorm");
			lms.setInfo(item.getScorm().getId() + "");
			lms.setTime(item.getTimestamp());
			lms.setCourse(item.getCourse().getId());
			lms.setUserid(item.getUser().getId());

			TestDataCreatorMoodle.logLms.add(lms);
		}
		for (final WikiLogMining item : this.wikiLogList)
		{
			final Log_LMS lms = new Log_LMS();
			lms.setId(item.getId());
			lms.setAction(item.getAction());
			lms.setModule("wiki");
			lms.setInfo(item.getWiki().getId() + "");
			lms.setTime(item.getTimestamp());
			lms.setCourse(item.getCourse().getId());
			lms.setUserid(item.getUser().getId());
			lms.setCmid(item.getWiki().getId());

			TestDataCreatorMoodle.logLms.add(lms);
		}
		for (final CourseLogMining item : this.courseLogList)
		{
			final Log_LMS lms = new Log_LMS();
			lms.setId(item.getId());
			lms.setAction(item.getAction());
			lms.setModule("course");
			lms.setTime(item.getTimestamp());
			lms.setCourse(item.getCourse().getId());
			lms.setUserid(item.getUser().getId());

			TestDataCreatorMoodle.logLms.add(lms);
		}
	}

	private void generateResourceLMS()
	{
		for (final ResourceMining item : this.resourceList)
		{
			final Resource_LMS lms = new Resource_LMS();
			lms.setId(item.getId());
			lms.setType(item.getType());
			lms.setName(item.getTitle());
			lms.setTimemodified(item.getTimeModified());
			if (this.couResMap.get(item.getId()) != null) {
				lms.setCourse(this.couResMap.get(item.getId()).getId());
			}

			TestDataCreatorMoodle.resourceLms.add(lms);
		}
	}

	private void generateForumLMS()
	{
		final HashMap<Long, Long> cFMap = new HashMap<Long, Long>();
		for (final Iterator<CourseForumMining> iter = this.courseForumList.iterator(); iter.hasNext();)
		{
			final CourseForumMining item = iter.next();
			cFMap.put(item.getForum().getId(), item.getCourse().getId());
		}
		for (final ForumMining item : this.forumList)
		{
			final Forum_LMS lms = new Forum_LMS();
			lms.setId(item.getId());
			lms.setTimemodified(item.getTimeModified());
			lms.setName(item.getTitle());
			lms.setIntro(item.getSummary());
			lms.setCourse(cFMap.get(lms.getId()));

			TestDataCreatorMoodle.forumLms.add(lms);

		}

	}

	private void generateCourseLMS()
	{

		for (final CourseMining item : this.courseList)
		{
			final Course_LMS lms = new Course_LMS();
			lms.setId(item.getId());
			lms.setFullname(item.getTitle());
			lms.setShortname(item.getShortname());
			lms.setTimemodified(item.getTimeModified());
			lms.setTimecreated(item.getTimeCreated());
			lms.setEnrolstartdate(item.getEnrolStart());
			lms.setEnrolenddate(item.getEnrolEnd());
			lms.setStartdate(item.getStartDate());

			TestDataCreatorMoodle.courseLms.add(lms);
		}
	}

	private void generateChatLogLMS()
	{
		final HashMap<Long, Long> couId = new HashMap<Long, Long>();

		for (final ChatLogMining item : this.chatLogList)
		{
			final ChatLog_LMS lms = new ChatLog_LMS();
			lms.setMessage(item.getMessage());

			lms.setTimestamp(item.getTimestamp());
			lms.setChat_Id(item.getChat().getId());
			lms.setUser_Id(item.getUser().getId());
			lms.setId(item.getId());

			couId.put(item.getChat().getId(), item.getCourse().getId());

			TestDataCreatorMoodle.chatLogLms.add(lms);
		}

		for (final ChatMining item : this.chatList)
		{
			final Chat_LMS lms = new Chat_LMS();
			lms.setId(item.getId());
			if (couId.get(item.getId()) != null) {
				lms.setCourse(couId.get(item.getId()));
			}
			lms.setTitle(item.getTitle());
			lms.setDescription(item.getDescription());
			lms.setChattime(item.getChatTime());

			TestDataCreatorMoodle.chatLms.add(lms);
		}
	}

	private void generateAssignmentLMS()
	{
		for (final AssignmentMining item : this.assignmentList)
		{
			final Assignment_LMS lms = new Assignment_LMS();
			lms.setId(item.getId());
			lms.setName(item.getTitle());
			lms.setTimeavailable(item.getTimeOpen());
			lms.setTimedue(item.getTimeClose());
			lms.setTimemodified(item.getTimeModified());
			lms.setDescription("description");
			if (this.couAssMap.get(item.getId()) != null) {
				lms.setCourse(this.couAssMap.get(item.getId()).getId());
			}

			final Grade_items_LMS lms2 = new Grade_items_LMS();
			lms2.setIteminstance(item.getId());
			lms2.setItemmodule("assignment");
			lms2.setGrademax(item.getMaxGrade());
			lms2.setId(TestDataCreatorMoodle.gradeItemsLms.size());

			TestDataCreatorMoodle.gradeItemsLms.add(lms2);
			TestDataCreatorMoodle.assignmentLMS.add(lms);
		}
	}

	@SuppressWarnings("unchecked")
	public void getDataFromDB()
	{
		final IDBHandler dbHandler = ServerConfiguration.getInstance().getMiningDbHandler();

		// accessing DB by creating a session and a transaction using HibernateUtil
		final Session session = dbHandler.getMiningSession();
		session.clear();

		final Query assQuery = session.createQuery("from AssignmentMining x order by x.id asc");
		this.assignmentList = (ArrayList<AssignmentMining>) assQuery.list();

		final Query assLogQuery = session.createQuery("from AssignmentLogMining x order by x.id asc");
		this.assignmentLogList = (ArrayList<AssignmentLogMining>) assLogQuery.list();

		final Query chaQuery = session.createQuery("from ChatMining x order by x.id asc");
		this.chatList = (ArrayList<ChatMining>) chaQuery.list();

		final Query chaLogQuery = session.createQuery("from ChatLogMining x order by x.id asc");
		this.chatLogList = (ArrayList<ChatLogMining>) chaLogQuery.list();

		final Query couQuery = session.createQuery("from CourseMining x order by x.id asc");
		this.courseList = (ArrayList<CourseMining>) couQuery.list();

		final Query couLogQuery = session.createQuery("from CourseLogMining x order by x.id asc");
		this.courseLogList = (ArrayList<CourseLogMining>) couLogQuery.list();

		final Query couAssQuery = session.createQuery("from CourseAssignmentMining x order by x.id asc");
		this.courseAssignmentList = (ArrayList<CourseAssignmentMining>) couAssQuery.list();

		final Query couForumQuery = session.createQuery("from CourseForumMining x order by x.id asc");
		this.courseForumList = (ArrayList<CourseForumMining>) couForumQuery.list();

		final Query couGroupQuery = session.createQuery("from CourseGroupMining x order by x.id asc");
		this.courseGroupList = (ArrayList<CourseGroupMining>) couGroupQuery.list();

		final Query couQuizQuery = session.createQuery("from CourseQuizMining x order by x.id asc");
		this.courseQuizList = (ArrayList<CourseQuizMining>) couQuizQuery.list();

		final Query couResQuery = session.createQuery("from CourseResourceMining x order by x.id asc");
		this.courseResourceList = (ArrayList<CourseResourceMining>) couResQuery.list();

		final Query couScormQuery = session.createQuery("from CourseScormMining x order by x.id asc");
		this.courseScormList = (ArrayList<CourseScormMining>) couScormQuery.list();

		final Query couUserQuery = session.createQuery("from CourseUserMining x order by x.id asc");
		this.courseUserList = (ArrayList<CourseUserMining>) couUserQuery.list();

		final Query couWikiQuery = session.createQuery("from CourseWikiMining x order by x.id asc");
		this.courseWikiList = (ArrayList<CourseWikiMining>) couWikiQuery.list();

		final Query degQuery = session.createQuery("from DegreeMining x order by x.id asc");
		this.degreeList = (ArrayList<LevelMining>) degQuery.list();

		final Query degCouQuery = session.createQuery("from DegreeCourseMining x order by x.id asc");
		this.degreeCourseList = (ArrayList<LevelCourseMining>) degCouQuery.list();

		final Query depQuery = session.createQuery("from DepartmentMining x order by x.id asc");
		this.departmentList = (ArrayList<LevelMining>) depQuery.list();

		final Query depDegQuery = session.createQuery("from DepartmentDegreeMining x order by x.id asc");
		this.departmentDegreeList = (ArrayList<LevelAssociationMining>) depDegQuery.list();

		final Query forQuery = session.createQuery("from ForumMining x order by x.id asc");
		this.forumList = (ArrayList<ForumMining>) forQuery.list();

		final Query forLogQuery = session.createQuery("from ForumLogMining x order by x.id asc");
		this.forumLogList = (ArrayList<ForumLogMining>) forLogQuery.list();

		final Query groupQuery = session.createQuery("from GroupMining x order by x.id asc");
		this.groupList = (ArrayList<GroupMining>) groupQuery.list();

		final Query groupUserQuery = session.createQuery("from GroupUserMining x order by x.id asc");
		this.groupUserList = (ArrayList<GroupUserMining>) groupUserQuery.list();

		final Query queQuery = session.createQuery("from QuestionMining x order by x.id asc");
		this.questionList = (ArrayList<QuestionMining>) queQuery.list();

		final Query queLogQuery = session.createQuery("from QuestionLogMining x order by x.id asc");
		this.questionLogList = (ArrayList<QuestionLogMining>) queLogQuery.list();

		final Query quiLogQuery = session.createQuery("from QuizLogMining x order by x.id asc");
		this.quizLogList = (ArrayList<QuizLogMining>) quiLogQuery.list();

		final Query quiQuery = session.createQuery("from QuizMining x order by x.id asc");
		this.quizList = (ArrayList<QuizMining>) quiQuery.list();

		final Query quiQuestionQuery = session.createQuery("from QuizQuestionMining x order by x.id asc");
		this.quizQuestionList = (ArrayList<QuizQuestionMining>) quiQuestionQuery.list();

		final Query quiUserQuery = session.createQuery("from QuizUserMining x order by x.id asc");
		this.quizUserList = (ArrayList<QuizUserMining>) quiUserQuery.list();

		final Query resQuery = session.createQuery("from ResourceMining x order by x.id asc");
		this.resourceList = (ArrayList<ResourceMining>) resQuery.list();

		final Query resLogQuery = session.createQuery("from ResourceLogMining x order by x.id asc");
		this.resourceLogList = (ArrayList<ResourceLogMining>) resLogQuery.list();

		final Query roleQuery = session.createQuery("from RoleMining x order by x.id asc");
		this.roleList = (ArrayList<RoleMining>) roleQuery.list();

		final Query scormQuery = session.createQuery("from ScormMining x order by x.id asc");
		this.scormList = (ArrayList<ScormMining>) scormQuery.list();

		final Query scormLogQuery = session.createQuery("from ScormLogMining x order by x.id asc");
		this.scormLogList = (ArrayList<ScormLogMining>) scormLogQuery.list();

		final Query userQuery = session.createQuery("from UserMining x order by x.id asc");
		this.userList = (ArrayList<UserMining>) userQuery.list();

		final Query wikQuery = session.createQuery("from WikiMining x order by x.id asc");
		this.wikiList = (ArrayList<WikiMining>) wikQuery.list();

		final Query wikLogQuery = session.createQuery("from WikiLogMining x order by x.id asc");
		this.wikiLogList = (ArrayList<WikiLogMining>) wikLogQuery.list();

		this.couAssMap = new HashMap<Long, CourseMining>();
		this.couForMap = new HashMap<Long, CourseMining>();
		this.couGroMap = new HashMap<Long, CourseMining>();
		this.couQuiMap = new HashMap<Long, CourseMining>();
		this.couResMap = new HashMap<Long, CourseMining>();
		this.couScoMap = new HashMap<Long, CourseMining>();
		this.couUseMap = new HashMap<Long, CourseMining>();
		this.couWikMap = new HashMap<Long, CourseMining>();

		this.degCouMap = new HashMap<Long, LevelMining>();
		this.depDegMap = new HashMap<Long, LevelMining>();

		for (final CourseAssignmentMining ca : this.courseAssignmentList) {
			this.couAssMap.put(ca.getAssignment().getId(), ca.getCourse());
		}

		for (final CourseGroupMining ca : this.courseGroupList) {
			this.couGroMap.put(ca.getGroup().getId(), ca.getCourse());
		}

		for (final CourseQuizMining ca : this.courseQuizList) {
			this.couQuiMap.put(ca.getQuiz().getId(), ca.getCourse());
		}

		for (final CourseUserMining ca : this.courseUserList) {
			this.couUseMap.put(ca.getUser().getId(), ca.getCourse());
		}

		for (final CourseForumMining ca : this.courseForumList) {
			this.couForMap.put(ca.getForum().getId(), ca.getCourse());
		}

		for (final CourseResourceMining cr : this.courseResourceList) {
			this.couResMap.put(cr.getResource().getId(), cr.getCourse());
		}

		for (final CourseWikiMining cw : this.courseWikiList) {
			this.couWikMap.put(cw.getWiki().getId(), cw.getCourse());
		}

		for (final CourseScormMining cs : this.courseScormList) {
			this.couScoMap.put(cs.getScorm().getId(), cs.getCourse());
		}

		for (final LevelCourseMining dc : this.degreeCourseList) {
			this.degCouMap.put(dc.getCourse().getId(), dc.getLevel());
		}

		for (final LevelAssociationMining dd : this.departmentDegreeList) {
			this.depDegMap.put(dd.getLower().getId(), dd.getUpper());
		}

		session.clear();
		session.close();

	}

	private void generateGroupLMS()
	{
		for (final GroupMining item : this.groupList)
		{
			final Groups_LMS lms = new Groups_LMS();
			lms.setId(item.getId());
			lms.setCourseid(this.couGroMap.get(item.getId()).getId());
			lms.setTimecreated(item.getTimeCreated());
			lms.setTimemodified(item.getTimeModified());

			TestDataCreatorMoodle.groupLms.add(lms);

		}
	}

	private void generateGroupMembersLMS()
	{
		for (final GroupUserMining item : this.groupUserList)
		{
			final Groups_members_LMS lms = new Groups_members_LMS();

			lms.setId(item.getId());
			lms.setGroupid(item.getGroup().getId());
			lms.setUserid(item.getUser().getId());
			lms.setTimeadded(item.getTimestamp());

			TestDataCreatorMoodle.groupMembersLms.add(lms);
		}
	}

	private void generateQuizLMS()
	{
		final HashMap<Long, Long> qQMap = new HashMap<Long, Long>();
		for (final Iterator<CourseQuizMining> iter = this.courseQuizList.iterator(); iter.hasNext();)
		{
			final CourseQuizMining item = iter.next();
			qQMap.put(item.getQuiz().getId(), item.getCourse().getId());
		}
		for (final QuizMining item : this.quizList)
		{
			final Quiz_LMS lms = new Quiz_LMS();
			final Grade_items_LMS lms2 = new Grade_items_LMS();

			lms.setId(item.getId());
			lms.setCourse(qQMap.get(lms.getId()));
			lms.setName(item.getTitle());
			lms.setTimeopen(item.getTimeOpen());
			lms.setTimeclose(item.getTimeClose());
			lms.setTimecreated(item.getTimeCreated());
			lms.setTimemodified(item.getTimeModified());

			lms2.setId(TestDataCreatorMoodle.gradeItemsLms.size() + 1);
			lms2.setGrademax(item.getMaxGrade());
			lms2.setIteminstance(item.getId());
			lms2.setItemmodule("quiz");

			TestDataCreatorMoodle.gradeItemsLms.add(lms2);
			TestDataCreatorMoodle.quizLms.add(lms);
		}
	}

	private void generateQuestionLMS()
	{
		for (final QuestionMining item : this.questionList)
		{
			final Question_LMS lms = new Question_LMS();

			lms.setId(item.getId());
			lms.setName(item.getTitle());
			lms.setQuestiontext(item.getText());
			lms.setQtype(item.getType());
			lms.setTimecreated(item.getTimeCreated());
			lms.setTimemodified(item.getTimeModified());

			TestDataCreatorMoodle.questionLms.add(lms);
		}
	}

	private void generateQuizQuestionInstancesLMS()
	{
		// HashMap<Long, Quiz_question_instances_LMS> tempMap = new HashMap<Long, Quiz_question_instances_LMS>();
		for (final QuizQuestionMining item : this.quizQuestionList)
		{
			final Quiz_question_instances_LMS lms = new Quiz_question_instances_LMS();

			lms.setId(item.getId());
			lms.setQuestion(item.getQuestion().getId());
			lms.setQuiz(item.getQuiz().getId());

			// tempMap.put(lms.getId(), lms);
			TestDataCreatorMoodle.quizQuestionInstancesLms.add(lms);

		}
		// quiz_question_instances_lms.addAll(tempMap.values());
	}

	private void generateGradeGradesLMS()
	{
		for (final QuizUserMining item : this.quizUserList)
		{
			final Grade_grades_LMS lms = new Grade_grades_LMS();

			lms.setId(item.getId());
			lms.setRawgrade(item.getRawGrade());
			lms.setFinalgrade(item.getFinalGrade());
			lms.setUserid(item.getUser().getId());
			lms.setTimemodified(item.getTimeModified());
			lms.setItemid(TestDataCreatorMoodle.gradeItemsLms.size() + 1);

			final Grade_items_LMS lms2 = new Grade_items_LMS();
			lms2.setCourseid(item.getCourse().getId());
			lms2.setIteminstance(item.getQuiz().getId());
			lms2.setId(lms.getItemid());

			TestDataCreatorMoodle.gradeItemsLms.add(lms2);
			TestDataCreatorMoodle.gradeGradesLms.add(lms);
		}
	}

	private void generateQuestionStatesLMS()
	{
		for (final QuestionLogMining item : this.questionLogList)
		{
			final Question_states_LMS lms = new Question_states_LMS();

			lms.setId(item.getId());
			lms.setAnswer(item.getAnswers());
			lms.setQuestion(item.getQuestion().getId());
			lms.setPenalty(item.getPenalty());
			lms.setTimestamp(item.getTimestamp());

			if (item.getAction().equals("OPEN")) {
				lms.setEvent((short) 0);
			} else if (item.getAction().equals("NAVIGATE")) {
				lms.setEvent((short) 1);
			} else if (item.getAction().equals("SAVE")) {
				lms.setEvent((short) 2);
			} else if (item.getAction().equals("GRADE")) {
				lms.setEvent((short) 3);
			} else if (item.getAction().equals("DUPLICATE")) {
				lms.setEvent((short) 4);
			} else if (item.getAction().equals("VALIDATE")) {
				lms.setEvent((short) 5);
			} else if (item.getAction().equals("CLOSEANDGRADE")) {
				lms.setEvent((short) 6);
			} else if (item.getAction().equals("SUBMIT")) {
				lms.setEvent((short) 7);
			} else if (item.getAction().equals("CLOSE")) {
				lms.setEvent((short) 8);
			} else if (item.getAction().equals("MANUALGRADE")) {
				lms.setEvent((short) 9);
			}

			TestDataCreatorMoodle.questionStatesLms.add(lms);
		}
	}

	public void writeSourceDB()
	{
		final List<Collection<?>> all = new ArrayList<Collection<?>>();

		this.generateUserLMS();
		this.generateAssignmentLMS();
		this.generateResourceLMS();
		this.generateForumLMS();
		this.generateCourseLMS();
		this.generateGroupLMS();
		this.generateRoleLMS();

		this.generateScormLMS();
		this.generateWikiLMS();
		this.generateGroupMembersLMS();
		this.generateQuizLMS();
		this.generateQuestionLMS();
		this.generateQuizQuestionInstancesLMS();
		this.generateGradeGradesLMS();
		this.generateQuestionStatesLMS();

		this.generateLogLMS();
		this.generateContextLMS();
		this.generateChatLogLMS();

		all.add(TestDataCreatorMoodle.userLms);
		all.add(TestDataCreatorMoodle.quizLms);
		all.add(TestDataCreatorMoodle.quizQuestionInstancesLms);
		all.add(TestDataCreatorMoodle.questionLms);
		all.add(TestDataCreatorMoodle.assignmentLMS);
		all.add(TestDataCreatorMoodle.chatLms);
		all.add(TestDataCreatorMoodle.groupMembersLms);
		all.add(TestDataCreatorMoodle.groupLms);
		all.add(TestDataCreatorMoodle.resourceLms);
		all.add(TestDataCreatorMoodle.roleLms);

		all.add(TestDataCreatorMoodle.forumLms);
		all.add(TestDataCreatorMoodle.courseLms);
		all.add(TestDataCreatorMoodle.scormLms);
		all.add(TestDataCreatorMoodle.wikiLms);
		all.add(TestDataCreatorMoodle.chatLogLms);
		all.add(TestDataCreatorMoodle.logLms);
		all.add(TestDataCreatorMoodle.forumPostsLms);
		all.add(TestDataCreatorMoodle.forumDiscussionsLms);
		all.add(TestDataCreatorMoodle.contextLms);
		all.add(TestDataCreatorMoodle.courseCategoriesLms);
		all.add(TestDataCreatorMoodle.roleAssignmentsLms);
		all.add(TestDataCreatorMoodle.gradeItemsLms);
		all.add(TestDataCreatorMoodle.gradeGradesLms);
		all.add(TestDataCreatorMoodle.questionStatesLms);
		all.add(TestDataCreatorMoodle.quizGradesLms);
		all.add(TestDataCreatorMoodle.assignmentSubmissionLms);

		final IDBHandler dbHandler = new HibernateDBHandler();
		final Session session = dbHandler.getMiningSession();
		dbHandler.saveCollectionToDB(session, all);
		dbHandler.closeSession(session);
	}
}
