package sentiments;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Paw
 *
 */
@Entity
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer uid;

	private String text;

	private String language;

	private Timestamp crdate;

	private Timestamp tmstamp;

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

	public String getLanguage() { return language; }

	public void setLanguage(String language) { this.language = language;}

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

}
