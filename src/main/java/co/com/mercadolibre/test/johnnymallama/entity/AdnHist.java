package co.com.mercadolibre.test.johnnymallama.entity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "adn_hist")
public class AdnHist implements Serializable {

	private String adn;
	private int valid;

	public AdnHist(String adn, int valid) {
		this.adn = adn;
		this.valid = valid;
	}

	@Id
	public String getAdn() {
		return adn;
	}

	public void setAdn(String adn) {
		this.adn = adn;
	}

	public int getValid() {
		return valid;
	}

	public void setValid(int valid) {
		this.valid = valid;
	}

}
