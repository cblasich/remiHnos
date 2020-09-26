//Store
var maxViaIdInicial = $.trim($("#maxViaIdInicial").val());
localStorage.setItem("LSMaxViaId", maxViaIdInicial); //en el localStorage guardo el ultimo IdViaje al momento inicial de cargar grilla.
document.getElementById("localStorage").value = localStorage.getItem("LSMaxViaId");

setInterval(start, 20000);

function start() {
	getMaxViaje()
	.then(res => {
		return res;
	})
	.then(mensaje => {
		console.log(mensaje);
	})
}

const getMaxViaje = () => {
	return new Promise((resolve, reject) => {
		// Trae ultimo Id generado de viaje.   
	    $.ajax({  
	        type : 'GET',  
	        url : "/getUltVia",  
	        success: function(data){
	            $('#maxViaIdActual').val(data);
	            maxViaIdActual = $.trim($("#maxViaIdActual").val());
	            console.log('1. RESOLVE. calculo idActual')
	            resolve(comparaIds(maxViaIdActual));
	        }
	    });
	});
};

const comparaIds = maxViaje => {
	return new Promise((resolve, reject) => {
		console.log('2. comparaIds.   MaxViaIdActual es: '+maxViaje);
		if(maxViaIdInicial < maxViaje) {
			console.log('3.a. Hay nuevo viaje. Y maxViaIdInicial es '+maxViaIdInicial +' y maxViaIdActual es ' +maxViaje);
			notify();
		} else {
			console.log('3.b. Son iguales y el actual es ' +maxViaIdInicial);
		}
	});
};

/* ESTO FUNCIONA
function getMaxViaIdActual() {
	// Trae ultimo Id generado de viaje.   
    $.ajax({  
        type : 'GET',  
        url : "/getUltVia",  
        success: function(data){
            //alert(data);
            $('#maxViaIdActual').val(data);
            maxViaIdActual = $.trim($("#maxViaIdActual").val());
            console.log('1. calculo idActual')
        }
    });
    //alert('comparo ids');
    comparaIds();
}

function comparaIds() {
	console.log('2. comparaIds.   MaxViaIdActual es: '+maxViaIdActual);
	if(maxViaIdInicial < maxViaIdActual) {
		console.log('3.a. Hay nuevo viaje. Y maxViaIdInicial es '+maxViaIdInicial +' y maxViaIdActual es ' +maxViaIdActual);
		notify();
	} else {
		console.log('3.b. Son iguales y el actual es ' +maxViaIdInicial);
	}
} */

function notify() {
	// Verificar que el navegador soporta notificaciones
	if(!("Notification" in window)) {  
		alert("Tu navegador no soporta notificaciones.");
	} 
	else if(Notification.permission === "granted") {
		//lanzar notificacion si ya esta autorizado el servicio
		showNotif(); 
	} 
	else if(Notification.permission !== "denied") {
		Notification.requestPermission(function(permission) {
			if(Notification.permission === "granted") {
				//lanzar notificacion si ya esta autorizado el servicio
				showNotif();
				//var notification = new Notification("Hola mundo !");
			}
		});
	}
	$("#notifViaje").removeClass("d-none");
}

function showNotif() {
    var notification = new Notification("Hay un nuevo pedido. Clic aquí para actualizar lista. O actualice luego.");
    notification.onclick = function(){
    	location.reload();
    };
}