package sentiments;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @author Paw
 *
 */
@Entity
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer uid;

	@Lob
	private String text;

	private Timestamp crdate;

	private Timestamp tmstamp;
	
	private boolean offensive;
	
	private boolean train;
	
	private boolean test;

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

	public boolean isTrain() {
		return train;
	}

	public void setTrain(boolean train) {
		this.train = train;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

}
