/**
 * File ./src/main/java/de/lemo/dms/db/mapping/Resource.java
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
 * File ./main/java/de/lemo/dms/db/mapping/Resource.java
 * Date 2014-02-04
 * Project Lemo Learning Analytics
 */

package de.lemo.dms.db.mapping;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.lemo.dms.db.mapping.abstractions.ILearningObject;
import de.lemo.dms.db.mapping.abstractions.IMapping;

/** 
 * This class represents the table resource. 
 * @author Sebastian Schwarzrock
 */
@Entity
@Table(name = "lemo_collaborative_object")
public class CollaborativeObject implements IMapping, ILearningObject{

	private long id;
	private String title;
	private CollaborativeObjectType type;
	
	private Set<CourseCollaborativeObject> courseCollaborativeObjects = new HashSet<CourseCollaborativeObject>();	
	private Set<CollaborativeObjectLog> collaborativeLogs = new HashSet<CollaborativeObjectLog>();
	
	public boolean equals(final IMapping o) {
		if (!(o instanceof LearningObject)) {
			return false;
		}
		if ((o.getId() == this.getId()) && (o instanceof LearningObject)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) id;
	}


	
	@Id
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@Lob
	@Column(name="title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="type_id")
	public CollaborativeObjectType getType() {
		return type;
	}
	public void setType(CollaborativeObjectType type) {
		this.type = type;
	}
	
	/**
	 * standard setter for the attribute course_resource.
	 * 
	 * @param courseLearningObjects
	 *            a set of entries in the course_resource table which relate the resource to the courses
	 */
	public void setCourseCollaborativeObjects(final Set<CourseCollaborativeObject> courseCollaborativeObjects) {
		this.courseCollaborativeObjects = courseCollaborativeObjects;
	}

	/**
	 * standard getter for the attribute.
	 * 
	 * @return a set of entries in the course_resource table which relate the resource to the courses
	 */
	@OneToMany(mappedBy="collaborativeObject")
	public Set<CourseCollaborativeObject> getCourseCollaborativeObjects() {
		return this.courseCollaborativeObjects;
	}

	/**
	 * standard add method for the attribute course_resource.
	 * 
	 * @param courseLearningObject
	 *            this entry will be added to the list of course_resource in this resource
	 */
	public void addCourseCollaborativeObject(final CourseCollaborativeObject courseCollaborativeObject) {
		this.courseCollaborativeObjects.add(courseCollaborativeObject);
	}

	/**
	 * @return the collaborativeLogs
	 */
	@OneToMany(mappedBy="collaborativeObject")
	public Set<CollaborativeObjectLog> getCollaborativeLogs() {
		return collaborativeLogs;
	}

	/**
	 * @param collaborativeLogs the collaborativeLogs to set
	 */
	public void setCollaborativeLogs(Set<CollaborativeObjectLog> collaborativeLogs) {
		this.collaborativeLogs = collaborativeLogs;
	}
	
	public void addCollaborativeLog(CollaborativeObjectLog collaboritiveLog)
	{
		this.collaborativeLogs.add(collaboritiveLog);
	}

	
	public void setType(final String title, final Map<String, CollaborativeObjectType> collaborativeObjectTypes,
			final Map<String, CollaborativeObjectType> oldCollaborativeObjectTypes) {

		if (collaborativeObjectTypes.get(title) != null)
		{
			this.type = collaborativeObjectTypes.get(title);
			collaborativeObjectTypes.get(title).addCollaborativeObject(this);
		}
		if ((this.type == null) && (oldCollaborativeObjectTypes.get(title) != null))
		{
			this.type = oldCollaborativeObjectTypes.get(title);
			oldCollaborativeObjectTypes.get(title).addCollaborativeObject(this);
		}
	}

	@Override
	@Transient
	public String getLOType() {
		return this.getType().getType();
	}
}