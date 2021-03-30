package com.remiNorte.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name="empresa")
public class Empresa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="empid")
	private Long empId;
	
	@Column(name = "emphrsdesde")
	@Min(value = 0, message = "El rango numérico de horas es de 00 a 23 (Mín: 00:00 / Máx: 23:59).")
    @Max(value = 23, message = "El rango numérico de horas es de 00 a 23 (Mín: 00:00 / Máx: 23:59).")
	private Short empHrsDesde; //hora desde (HH)
	
	@Column(name = "empmindesde")
	@Min(value = 0, message = "El rango numérico de minutos es de 00 a 59.")
    @Max(value = 59, message = "El rango numérico de minutos es de 00 a 59.")
	private Short empMinDesde; //minuto desde (mm)
	
	@Column(name = "emphrshasta")
	@Min(value = 0, message = "El rango numérico de horas es de 00 a 23 (Mín: 00:00 / Máx: 23:59).")
    @Max(value = 23, message = "El rango numérico de horas es de 00 a 23 (Mín: 00:00 / Máx: 23:59).")
	private Short empHrsHasta; //hora desde (HH)
	
	@Column(name = "empminhasta")
	@Min(value = 0, message = "El rango numérico de minutos es de 00 a 59.")
    @Max(value = 59, message = "El rango numérico de minutos es de 00 a 59.")
	private Short empMinHasta; //minuto desde (mm)
	
	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Short getEmpHrsDesde() {
		return empHrsDesde;
	}

	public void setEmpHrsDesde(Short empHrsDesde) {
		this.empHrsDesde = empHrsDesde;
	}

	public Short getEmpMinDesde() {
		return empMinDesde;
	}

	public void setEmpMinDesde(Short empMinDesde) {
		this.empMinDesde = empMinDesde;
	}

	public Short getEmpHrsHasta() {
		return empHrsHasta;
	}

	public void setEmpHrsHasta(Short empHrsHasta) {
		this.empHrsHasta = empHrsHasta;
	}

	public Short getEmpMinHasta() {
		return empMinHasta;
	}

	public void setEmpMinHasta(Short empMinHasta) {
		this.empMinHasta = empMinHasta;
	}

}
