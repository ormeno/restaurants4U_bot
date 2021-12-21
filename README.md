# restaurants4U_bot Bot de Telegram en JAVA y MySQL.

Bot de Telegram que sirve como market de restaurantes. 

- El propietario del Bot da de alta todos los restaurantes que quiera.
- Los propietarios de los restaurantes podrán gestionar sus artículos, los pedidos, el stock, las notificaciones, etc.
- Los clientes de los restaurantes pueden hacer sus pedidos de comida. 

Todo desde el propio BOT, sin necesidad de salir de Telegram.

Usa el código para crear tu propio market en Telegram!!, adaptalo a cualquier otro negocio.
Si necesitas ayuda puedes localizarme en Telegram -> @rormeno75

Puedes ver como funciona el BOT en https://t.me/Restaurants4UBot

Pasos de instalación:

- clona el proyecto y lo abres en tu IDE favorito
- Importa las librerías del pom.xml con Maven/Update proyect..
- Crea tu propio bot con @Botfather y guarda el token.
- Crea la base de datos MySQL con los scripts de la carpeta sql.
  En la tabla Locales se definen las características propias de cada Restaurante (nombre, descripción, tipo local, país, ciudad,  idioma y formato numérico y de la divisa, impuesto, % impuesto, tipo de plan, datos a mostrar en envio, datos a mostrar en la confirmación del pago. 
- Actualiza las siguientes variables del fichero configuracion.properties.
	
	+ var.BotUsername= (valor devuelto por @Botfather) 
	+ var.BotToken= (valor devuelto por @Botfather) 
	+ variables de conexion con la base de datos:
		var.url= jdbc:mysql://<IP>:3306/restaurants4ubd?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
		var.user= (usuario de conexión con la BD)
		var.password= (psw de la BD)
	+ list.usersAdm = lista de user_id de los usuarios administradores
	+ list.catValidas= (Categorías de los distintos tipos de platos existentes)
	+ list.paises=ESPAÑA,MEXICO,ITALIA,COLOMBIA,NINGUNO (listado de países donde hay restaurantes)
	+ list.ciudadesESPAÑA=MADRID,BARCELONA,MALAGA,ZARAGOZA (listado de ciudades de España donde hay restaurantes)
	+ list.ciudadesITALIA=ROMA,MILÁN (listado de ciudades de Italia donde hay restaurantes)
	+ list.ciudadesCOLOMBIA= (se crean tantas variable de este tipo como países disponibles haya)
	+ list.estadosPedido = PENDIENTE,EN COCINA,PREPARADO,EN CAMINO,ENTREGADO,ANULADO (lista de estados definidos para un pedido)
	+ var.limMin=1 (límite inferior del precio de los artículos)
	+ var.limMax=8856 (límite superio del precio de los artículos)
	+ var.stock=SI (Variable para activar o no la gestión del stock de los artículos)
	+ var.imagenInline= URL donde se aloja la imagen inline del bot 
    
- Bot multiidioma. Si quieres definir un nuevo idioma deberas crear un nuevo fichero "configuracionXX.properties".
- Ejecuta la clase Main.java  (Text encoding -> UTF-8)
