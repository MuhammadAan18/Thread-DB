package model;

public class Meja {
	private int idMeja;
	private String code;
	private String status;
	private int hourlyRate;

	public Meja (int idMeja, String kode, String status, int hargaPerJam) {
		this.idMeja = idMeja;
		this.code = kode;
		this.status = status;
		this.hourlyRate = hargaPerJam;
	}

	public int getId () {
		return idMeja;
	}

	public String getCode () {
		return code;
	}

	public String getStatus () {
		return status;
	}

	public int getHourlyRate () {
		return hourlyRate;
	}

	public void setStatus (String status) {
		this.status = status;
	}
}
