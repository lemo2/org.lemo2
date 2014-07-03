package de.lemo.dms.connectors.mooc.mapping;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "comments")
public class Comments {
	
	private long id;
	private String commentableType;
	private long commentableId;
	private String content;
	private long timeCreated;
	private long timeModified;
	private long userId;
	
	/**
	 * @return the id
	 */
	@Id
	public long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the commentableType
	 */
	@Column(name="commentable_type")
	public String getCommentableType() {
		return commentableType;
	}
	/**
	 * @param commentableType the commentableType to set
	 */
	public void setCommentableType(String commentableType) {
		this.commentableType = commentableType;
	}
	/**
	 * @return the commentableId
	 */
	@Column(name="commentable_id")
	public long getCommentableId() {
		return commentableId;
	}
	/**
	 * @param commentableId the commentableId to set
	 */
	public void setCommentableId(long commentableId) {
		this.commentableId = commentableId;
	}
	/**
	 * @return the content
	 */
	@Column(name="content")
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the timeCreated
	 */
	@Column(name="created_at")
	public long getTimeCreated() {
		return timeCreated;
	}
	/**
	 * @param timeCreated the timeCreated to set
	 */
	public void setTimeCreated(long timeCreated) {
		this.timeCreated = timeCreated;
	}
	/**
	 * @return the timeModified
	 */
	@Column(name="updated_at")
	public long getTimeModified() {
		return timeModified;
	}
	/**
	 * @param timeModified the timeModified to set
	 */
	public void setTimeModified(long timeModified) {
		this.timeModified = timeModified;
	}
	/**
	 * @return the userId
	 */
	@Column(name="user_id")
	public long getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
}
