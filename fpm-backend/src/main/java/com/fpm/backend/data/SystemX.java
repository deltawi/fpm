package com.fpm.backend.data;

import java.util.Set;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


public class SystemX {

	@NotNull
    private int id = -1;
	private Set<Category> category;
	public String systemXName; 
	@Min(0)
	public double p1,p2,p3;
	public double AnomalyIndex = 1;
	public String AnomalousField = ""; 
	public Ata ata = Ata.PNEUMATICS;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<Category> getCategory() {
		return category;
	}
	public void setCategory(Set<Category> category) {
		this.category = category;
	}
	public String getsystemXName() {
		return systemXName;
	}
	public void setsystemName(String systemName) {
		systemXName = systemName;
	}
	public double getP1() {
		return p1;
	}
	public String getSystemXName() {
		return systemXName;
	}
	public void setSystemXName(String systemXName) {
		this.systemXName = systemXName;
	}
	public void setP1(double p1) {
		this.p1 = p1;
	}
	public double getP2() {
		return p2;
	}
	public void setP2(double p2) {
		this.p2 = p2;
	}
	public double getP3() {
		return p3;
	}
	public void setP3(double p3) {
		this.p3 = p3;
	}
	public Ata getAta() {
		return ata;
	}
	public void setAta(Ata ata) {
		this.ata = ata;
	}
	public double getAnomalyIndex() {
		return AnomalyIndex;
	}
	public void setAnomalyIndex(double anomalyIndex) {
		AnomalyIndex = anomalyIndex;
	}
	public String getAnomalousField() {
		return AnomalousField;
	}
	public void setAnomalousField(String anoumalousField) {
		AnomalousField = anoumalousField;
	}
	
	
	
}
