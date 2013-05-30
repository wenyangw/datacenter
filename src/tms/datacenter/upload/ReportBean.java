package tms.datacenter.upload;

public class ReportBean {
	private String name;
	private Double sumJan;
	private Double sumFeb;
	private Double sumMar;
	private Double sumApr;
	private Double sumMay;
	
	
	
	
	public ReportBean(String name) {
		super();
		this.name = name;
	}
	public ReportBean(String name, Double sumJan, Double sumFeb, Double sumMar,
			Double sumApr, Double sumMay) {
		this.name = name;
		this.sumJan = sumJan;
		this.sumFeb = sumFeb;
		this.sumMar = sumMar;
		this.sumApr = sumApr;
		this.sumMay = sumMay;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSumJan() {
		return sumJan;
	}
	public void setSumJan(Double sumJan) {
		this.sumJan = sumJan;
	}
	public Double getSumFeb() {
		return sumFeb;
	}
	public void setSumFeb(Double sumFeb) {
		this.sumFeb = sumFeb;
	}
	public Double getSumMar() {
		return sumMar;
	}
	public void setSumMar(Double sumMar) {
		this.sumMar = sumMar;
	}
	public Double getSumApr() {
		return sumApr;
	}
	public void setSumApr(Double sumApr) {
		this.sumApr = sumApr;
	}
	public Double getSumMay() {
		return sumMay;
	}
	public void setSumMay(Double sumMay) {
		this.sumMay = sumMay;
	}
	
}
