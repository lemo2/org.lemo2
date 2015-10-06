package org.lemo2.dataprovider.jdbc;

import java.io.File;
import java.io.PrintStream;
import java.util.*;

import org.lemo2.dataprovider.api.*;

public class Test_JDBC_DataProvider {
	
	static private final String URI = "jdbc:mysql://localhost:3306/d4la_iversity";
	static private final String USER = "root";
	static private final String PASSWORD = "lemo";
	static private final String LOGFILE = "/Users/forte/projekt/lemo2/d4la/jdbc.log";
	static private PrintStream PS;
	
	public static void main(String[] args) throws Exception {
//		LA_DataProvider dp = new JDBC_DataProvider(URI, USER, PASSWORD);
		PS = new PrintStream(new File(LOGFILE));
		LA_DataProvider dp = new JDBC_DataProvider(URI, USER, PASSWORD, true, PS);
		for ( LA_Context context : dp.getCourses() ) {
			printContext(context, "");
		}
	}
	
	static private void printContext(LA_Context context, String prefix) {
		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append("CONTEXT: ");
		sb.append(context.getName());
		Set<String> attr = context.getExtNames();
		if ( attr != null ) {
			for ( String name : attr ) {
				sb.append("  ");
				sb.append(name);
				sb.append(": ");
				sb.append(context.getExtValue(name));
			}
		}
		
		println(new String(sb));
		prefix += "    ";
		printFirstPerson(context.getInstructors(), "INSTRUCTOR", prefix);
		printFirstPerson(context.getStudents(), "STUDENT", prefix);
		printFirstObject(context.getObjects(), prefix);
		printFirstActivity(context.getActivities(), prefix);
		List<LA_Context> children = context.getChildren();
		if ( children == null ) return;
		for ( LA_Context child : children ) {
			printContext((JDBC_Context) child, prefix);
		}
	}
	
	private static void printFirstPerson(List<LA_Person> persons, String role, String prefix) {
		if ( persons == null ) return;
		StringBuffer sb = new StringBuffer();
		for ( LA_Person person : persons ) {
			sb.append(prefix);
			sb.append("FIRST ");
			sb.append(role);
			sb.append(" (1/");
			sb.append(persons.size());
			sb.append("):  name: ");
			sb.append(person.getName());
			Set<String> attr = person.getExtNames();
			if ( attr != null ) {
				for ( String name : attr ) {
					sb.append("  ");
					sb.append(name);
					sb.append(": ");
					sb.append(person.getExtValue(name));
				}
			}
			println(new String(sb));
			return;
		}
	}
	
	private static void printFirstObject(List<LA_Object> objects, String prefix) {
		if ( objects == null ) return;
		StringBuffer sb = new StringBuffer();
		for ( LA_Object object : objects ) {
			sb.append(prefix);
			sb.append("FIRST OBJECT (1/");
			sb.append(objects.size());
			sb.append("):  name: ");
			sb.append(object.getName());
			sb.append("  type: ");
			sb.append(object.getType());
			Set<String> attr = object.getExtNames();
			if ( attr != null ) {
				for ( String name : attr ) {
					sb.append("  ");
					sb.append(name);
					sb.append(": ");
					sb.append(object.getExtValue(name));
				}
			}
			println(new String(sb));
			if ( object.getChildren() == null ) return;
			printFirstObject(object.getChildren(), prefix + "    ");
			return;
		}
	}
		
	private static void printFirstActivity(List<LA_Activity> activities, String prefix) {
		if ( activities == null ) return;
		StringBuffer sb = new StringBuffer();
		for ( LA_Activity activity : activities ) {
			sb.append(prefix);
			sb.append("FIRST ACTIVITY (1/");
			sb.append(activities.size());
			sb.append("):  time: ");
			sb.append(activity.getTime());
			sb.append("  action: ");
			sb.append(activity.getAction());
			sb.append("  info: ");
			sb.append(activity.getInfo());
			sb.append("  reference: ");
			sb.append(activity.getReference());
//			sb.append("  person: ");
//			sb.append(activity.getPerson());
//			sb.append("  object: ");
//			sb.append(activity.getObject());
			Set<String> attr = activity.getExtNames();
			if ( attr != null ) {
				for ( String name : attr ) {
					sb.append("  ");
					sb.append(name);
					sb.append(": ");
					sb.append(activity.getExtValue(name));
				}
			}
			println(new String(sb));
			return;
		}
	}
	
	private static void println(String str) {
		System.out.println(str);
		PS.println(str);
	}
	
}