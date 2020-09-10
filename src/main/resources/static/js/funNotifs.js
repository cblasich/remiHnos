// Initialize the service
var holis = document.getElementById("holis");
var ajax = document.getElementById("ajax");

// llamada ajax
function bye() {
    alert("Before Ajax..!");
    $.ajax({  
        type : 'GET',  
        url : "ViajeController/getUltVia",  
 
                });
                alert("onUnLoad Bye");
}


/*window.setInterval(function(){
  //busco ultimo Id de viaje generado
	
}, 30000);*/

//Store
var maxViaId = $.trim($("#maxViaId").val());
localStorage.setItem("LSMaxViaId", maxViaId);
document.getElementById("localStorage").value = localStorage.getItem("LSMaxViaId");



holis.addEventListener('click',function() {
	notify();
});
ajax.addEventListener('click',function() {
	bye();
});


function notify() {
	// Verificar que el navegador soporta notificaciones
	if(!("Notification" in window)) {
		
		alert("Tu navegador no soporta notificaciones.");
	
	} else if(Notification.permission === "granted") {
		//lanzar notificacion si ya esta autorizado el servicio
		show();
		//var notification = new Notification("mi primer notificacion. clic aqui ");
	
	} else if(Notification.permission !== "denied") {
		
		Notification.requestPermission(function(permission) {
			
			if(Notification.permission === "granted") {
				
				//lanzar notificacion si ya esta autorizado el servicio
				show();
				//var notification = new Notification("Hola mundo !");
				
			}
		});
	}
}

function show() {
    var notification = new Notification("Hay un nuevo pedido. Clic aquí para actualizar lista. O click to the x to close");
    notification.onclick = function(){
    	location.reload();
    };
};