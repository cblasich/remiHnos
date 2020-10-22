package com.remiNorte.app.models.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="tarifas")
public class Tarifa {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="tarid")
	private Long tarId;
	
	@Column(name="tarfecvigdes")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date TarFecVigDes;
	
	@Column(name="tarfecvighas")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date tarFecVigHas;
	
	@Column(name="tarbajban")
	@NotNull(message = "La bajada de bandera debe ser mayor a cero.")
	@Positive(message = "El número debe ser positivo.")
	private Float TarBajBan;
	
	@Column(name="tarimp100m")
	@NotNull(message = "El importe debe ser mayor a cero.")
	@Positive(message = "El número debe ser positivo.")
	private Float TarImp100m;
	
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL,mappedBy="Tarifa")
	private List<Viaje> Viajes;
	
	public Tarifa() {
		Viajes = new ArrayList<Viaje>();
	}
	
	public Date getTarFecVigDes() {
		return TarFecVigDes;
	}

	public void setTarFecVigDes(Date tarFecVigDes) {
		TarFecVigDes = tarFecVigDes;
	}

	public Date getTarFecVigHas() {
		return tarFecVigHas;
	}

	public void setTarFecVigHas(Date inTarFecVigHas) {
		tarFecVigHas = inTarFecVigHas;
	}

	public Float getTarBajBan() {
		return TarBajBan;
	}

	public void setTarBajBan(Float tarBajBan) {
		TarBajBan = tarBajBan;
	}

	public Float getTarImp100m() {
		return TarImp100m;
	}

	public void setTarImp100m(Float tarImp100m) {
		TarImp100m = tarImp100m;
	}

	public Long getTarId() {
		return tarId;
	}
	
	public List<Viaje> getViajes() {
		return Viajes;
	}

	public void setViajes(List<Viaje> viajes) {
		Viajes = viajes;
	}
	
	public void addViaje(Viaje viaje) { 
		Viajes.add(viaje);
		//viaje.setTarifa(this);
	}
	
	public void removeViaje(Viaje viaje) {
		Viajes.remove(viaje);
		viaje.setTarifa(null);
	}
}
