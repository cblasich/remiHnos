// Initialize the service
var dire, ok, oricalle, orinum, descalle, desnum, direOri, direDes, pre100m, bajban, preEst;
const geocoderService = platform.getGeocodingService();	
const routerService = platform.getRoutingService();
const geocoder = query => {
	return new Promise((resolve, reject) => {
		geocoderService.geocode(
			{
				"searchtext": query
			},
			success => {
				resolve(success.Response.View[0].Result[0].Location.DisplayPosition);
			},
			error => {
				reject(error);
			}
		);
	});
}
const calculateRoute = (start, finish) => {
	return new Promise((resolve,reject) => {
		const params = {
			mode: "fastest;car;traffic:enabled",
			waypoint0: start.Latitude + "," + start.Longitude,
			waypoint1: finish.Latitude + "," + finish.Longitude,
			representation: "display",
			routeAttributes: "summary"
		};
	 	routerService.calculateRoute(params, success => {
	 		resolve(success.response.route[0].shape);
	 	}, error => {
	 		reject(error);
	 	});
	});		
}
const calculateDistance = (start, finish) => {
	return new Promise((resolve,reject) => {
		const params = {
			mode: "fastest;car;traffic:enabled",
			waypoint0: start.Latitude + "," + start.Longitude,
			waypoint1: finish.Latitude + "," + finish.Longitude,
			representation: "display",
			routeAttributes: "summary"
		};
	 	routerService.calculateRoute(params, success => {
	 		resolve(success.response.route[0].summary.distance);
	 	}, error => {
	 		reject(error);
	 	});
	});		
}
const start = async () => {
	document.getElementById("mapContainer").innerHTML="";
	dire = "Formosa, Argentina";
	const origen = await geocoder(direOri);
	const destino = await geocoder(direDes);
	// Instantiate (and display) a map object:
	const map = new H.Map(
	    document.getElementById('mapContainer'),
	    defaultLayers.vector.normal.map,
	    {
	      zoom: 15,
	      center: { lat: origen.Latitude, lng: origen.Longitude }    
	    }
    );
	const mapEvents = new H.mapevents.MapEvents(map);
	const behavior = new H.mapevents.Behavior(mapEvents);
	//marcadores punto origen y destino
	const origenMarker = new H.map.Marker({ lat: origen.Latitude , lng: origen.Longitude });
	const destinoMarker = new H.map.Marker({ lat: destino.Latitude , lng: destino.Longitude });
	//ruta entre puntos
	const ruta = await calculateRoute(origen, destino);
	const rutaLineString = new H.geo.LineString();
	ruta.forEach(points => {
		let parts = points.split(",");
		rutaLineString.pushPoint({
			lat: parts[0],
			lng: parts[1]
		});
	});
	const rutaPolyline = new H.map.Polyline(
		rutaLineString,
		{
			style: {
				lineWidth: 6
			}
		}
	);
	const distance = await calculateDistance(origen, destino);
	dist100 =  Math.ceil(distance/100);
	map.addObjects([origenMarker,destinoMarker,rutaPolyline]);
	pre100m = $("#pre100m").val();
	bajban = $("#bajban").val();
	preEst = Math.ceil(pre100m*dist100+parseFloat(bajban));
	document.getElementById("monEst").value = preEst;
	var cos = 'Costo estimado del viaje: $' + preEst + '.'; 
	$("#lblMonEst").text(cos);
}

$("#btnVerPrecio").click(function() {
	ok = 1;
	ValidarOriDes();
	if (ok==1) { 
		preEst = 0;
		direOri = oricalle.concat(' ',orinum,' , Formosa, Argentina');
		direDes = descalle.concat(' ',desnum,' , Formosa, Argentina');
		start();
		ori1 = 'Origen: ';
		ori2 = ori1.concat(oricalle,' ',orinum,'.');   
		des1 = 'Destino: ';
		des2 = des1.concat(descalle,' ',desnum,'.');
		$("#espacioSup").addClass("d-none d-sm-block");
		$("#contOrigen").addClass("d-none d-sm-block");
		$("#contDestino").addClass("d-none d-sm-block");
		$("#contBtnPreEst").addClass("d-none d-sm-block");  
		$("#lblOrigen").text(ori2);
		$("#lblDestino").text(des2);
		$("#contMapa").removeClass("d-none");
		$("#contMapa").addClass("superponerSM");
	} 
});
$("#btnModif").click(function() {
	$("#espacioSup").removeClass("d-none d-sm-block");
	$("#contOrigen").removeClass("d-none d-sm-block");
	$("#contDestino").removeClass("d-none d-sm-block");
	$("#contBtnPreEst").removeClass("d-none");  
	$("#contMapa").addClass("d-none");
});
function ValidarOriDes() {
	oricalle = $.trim($("#OriCalle").val());
	orinum = $.trim($("#OriNum").val());
	descalle = $.trim($("#DesCalle").val());
	desnum = $.trim($("#DesNum").val());
	
	if(oricalle == '' || oricalle == null){
	  	$("#OriCalle").val(null);
	  	ok = 0;
	  	$("#errOriCalle").removeClass("d-none");
	} else {
		$("#errOriCalle").addClass("d-none");
		$("#errOriCalle2").addClass("d-none");
	}
	if(orinum == '' || orinum == null){
	  	$("#OriNum").val(null);
	  	ok = 0;
	  	$("#errOriNum").removeClass("d-none");
	} else {
		$("#errOriNum").addClass("d-none");
		$("#errOriNum2").addClass("d-none");
	}
	if(descalle == '' || descalle == null){
	  	$("#DesCalle").val(null);
	  	ok = 0;
	  	$("#errDesCalle").removeClass("d-none");
	} else {
		$("#errDesCalle").addClass("d-none");
	}
	if(desnum == '' || desnum == null){
	  	$("#DesNum").val(null);
	  	ok = 0;
	  	$("#errDesNum").removeClass("d-none");
	} else {
		$("#errDesNum").addClass("d-none");
	}
	return ok;
}