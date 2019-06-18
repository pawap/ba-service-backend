package sentiments;

import java.sql.Timestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Document
@MappedSuperclass
public class AbstractTweet {

	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer uid;
	private String text;
	private Timestamp crdate;
	private Timestamp tmstamp;
	private boolean offensive;

	public AbstractTweet() {
	}
	public AbstractTweet(Integer uid, String text, Timestamp crdate, Timestamp tmstamp){
		this.uid = uid;
		this.text =text;
		this.crdate = crdate;
		this.tmstamp = tmstamp;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Timestamp getCrdate() {
		return crdate;
	}

	public void setCrdate(Timestamp crdate) {
		this.crdate = crdate;
	}

	public Timestamp getTmstamp() {
		return tmstamp;
	}

	public void setTmstamp(Timestamp tmstamp) {
		this.tmstamp = tmstamp;
	}

	public boolean isOffensive() {
		return offensive;
	}

	public void setOffensive(boolean offensive) {
		this.offensive = offensive;
	}
}