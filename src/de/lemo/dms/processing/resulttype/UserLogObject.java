package de.lemo.dms.processing.resulttype;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class UserLogObject implements Comparable<UserLogObject>{
	
	private Long userId;
	private Long timestamp;
	private String title;
	private Long objectId;
	private Long group;
	private String type;
	private String info;
	
	@XmlElement
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement
	public Long getGroup() {
		return group;
	}

	public void setGroup(Long group) {
		this.group = group;
	}

	public UserLogObject()
	{
		
	}
	
	public UserLogObject(Long userId, long timestamp, String title, Long objectId, String type, Long group, String info)
	{
		this.userId = userId;
		this.timestamp = timestamp;
		this.objectId = objectId;
		this.title = title;
		this.group = group;
		this.type = type;
		this.setInfo(info);
		
	}
	
	@XmlElement
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@XmlElement
	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	@XmlElement
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public int compareTo(UserLogObject arg0) {
		UserLogObject s;
		try{
			s = (UserLogObject)arg0;
		}catch(Exception e)
		{
			return 0;
		}
		if(this.getUserId() > s.getUserId())
			return 1;
		if(this.getUserId() < s.getUserId())
			return -1;
		if(this.getUserId() == s.getUserId())
		{
			if(this.getTimestamp() > s.getTimestamp())
				return 1;
			if(this.getTimestamp() < s.getTimestamp())
				return -1;
		}
		return 0;
	}

	@XmlElement
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

}
