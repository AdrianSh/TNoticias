insert into User (id, avatar, email, password, lname, login, name, profile_background, roles, pregunta_de_seguridad, respuesta_de_seguridad, enabled) values (1, 'http://lorempixel.com/100/100/people/10/','a@a.com', '$2a$10$VKaVr.za5JSB13iee/LY4eXrv.wOBBuPBh66p7xuOMrq0co7iRlv.','Sin apellido eheh','admin', 'Administrador', 'http://lorempixel.com/100/100/people/10/','user,admin,master', 'pregunta', 'respuesta', true);
insert into User (id, avatar, email, password, lname, login, name, profile_background, roles, pregunta_de_seguridad, respuesta_de_seguridad, enabled) values (2, 'http://lorempixel.com/100/100/people/10/','a@1.com', '$2a$10$EP.pbGMSB04b9XFysC3T1ubWQ3TIGAWSzuenM0ef2M8yW928Zvs5S','S.','AdrianSh', 'Adrian', 'http://lorempixel.com/100/100/people/10/','user,admin,master', 'pregunta', 'respuesta', true);
insert into User (id, avatar, email, password, lname, login, name, profile_background, roles, pregunta_de_seguridad, respuesta_de_seguridad, enabled) values (3, 'http://lorempixel.com/100/100/people/10/','a@b.com', '$2a$10$adwDmBTvdVFtnublHT/xw.NTby67ikxkl3kPhaSj3ZMSmS2tA3bcO','Sanchez','AdrianoMariano', 'Nombre', 'http://lorempixel.com/100/100/people/10/','admin', 'pregunta', 'respuesta', true);
insert into User (id, avatar, email, password, lname, login, name, profile_background, roles, pregunta_de_seguridad, respuesta_de_seguridad, enabled) values (4, 'http://lorempixel.com/100/100/people/10/','alo@a.com', '$2a$10$tS2Bk1FevKOmDD49poA/oOq0QGaZ.XCZbgRfhVkqIDN0dmLAwVz1y','lolo','Lolo', 'Sr', 'http://lorempixel.com/100/100/people/10/','admin', 'pregunta', 'respuesta', true);
INSERT INTO ARTICULO (ID, CONTENIDO, FECHA, IMAGE, RANKING, TIPO, TITULO, AUTOR_ID) VALUES (default, 'awdLa onda expansiva ha causado numerosos daños en inmuebles y vehículos del entorno y testigos de los destrozos han asegurado a este periódico que al menos 11 viviendas están completamente destruidas. Según declaraciones realizadas por el exalcalde de Tui, Enrique Cabaleiro, al periódico [i]La Voz de Galicia[/i], la pirotecnia relacionada con la explosión ocupaba un almacén clandestino que él había dado orden de clausurar hace años. La onda expansiva ha causado numerosos daños en inmuebles y vehículos del entorno y testigos de los destrozos han asegurado a este periódico que al menos 11 viviendas están completamente destruidas. Según declaraciones realizadas por el exalcalde de Tui, Enrique Cabaleiro, al periódico [i]La Voz de Galicia[/i], la pirotecnia relacionada con la explosión ocupaba un almacén clandestino que él había dado orden de clausurar hace años.', '2018-05-23 23:38:18.907000', 'http://lorempixel.com/200/300/', 1, 0, 'La gran explosion', 1);
INSERT INTO ARTICULO (ID, CONTENIDO, FECHA, IMAGE, RANKING, TIPO, TITULO, AUTOR_ID) VALUES (default, 'awdLa onda expansiva ha causado numerosos daños en inmuebles y vehículos del entorno y testigos de los destrozos han asegurado a este periódico que al menos 11 viviendas están completamente destruidas. Según declaraciones realizadas por el exalcalde de Tui, Enrique Cabaleiro, al periódico [i]La Voz de Galicia[/i], la pirotecnia relacionada con la explosión ocupaba un almacén clandestino que él había dado orden de clausurar hace años. La onda expansiva ha causado numerosos daños en inmuebles y vehículos del entorno y testigos de los destrozos han asegurado a este periódico que al menos 11 viviendas están completamente destruidas. Según declaraciones realizadas por el exalcalde de Tui, Enrique Cabaleiro, al periódico [i]La Voz de Galicia[/i], la pirotecnia relacionada con la explosión ocupaba un almacén clandestino que él había dado orden de clausurar hace años.', '2018-05-23 23:38:18.907000', 'http://lorempixel.com/200/300/', 2, 0, '...él había dado orden de clausurar hace años.', 2);
INSERT INTO ARTICULO (ID, CONTENIDO, FECHA, IMAGE, RANKING, TIPO, TITULO, AUTOR_ID) VALUES (default, 'awdLa onda expansiva ha causado numerosos daños en inmuebles y vehículos del entorno y testigos de los destrozos han asegurado a este periódico que al menos 11 viviendas están completamente destruidas. Según declaraciones realizadas por el exalcalde de Tui, Enrique Cabaleiro, al periódico [i]La Voz de Galicia[/i], la pirotecnia relacionada con la explosión ocupaba un almacén clandestino que él había dado orden de clausurar hace años. La onda expansiva ha causado numerosos daños en inmuebles y vehículos del entorno y testigos de los destrozos han asegurado a este periódico que al menos 11 viviendas están completamente destruidas. Según declaraciones realizadas por el exalcalde de Tui, Enrique Cabaleiro, al periódico [i]La Voz de Galicia[/i], la pirotecnia relacionada con la explosión ocupaba un almacén clandestino que él había dado orden de clausurar hace años.', '2018-05-23 23:38:18.907000', 'http://lorempixel.com/200/300/', 3, 0, 'La onda expansiva ha causado numerosos daños en inmuebles y vehículos del entorno ', 3);
insert into amigos (id, amigo_id, user_id) values (default, 1, 2);
INSERT INTO ACTIVIDAD (ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (1, '2018-05-24 00:02:06.036000', 'Ha visitado el perfil de Adrian S.', '2018-05-24 00:02:06.036000', 1);
INSERT INTO ACTIVIDAD (ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (2, '2018-05-24 00:02:15.286000', 'Ha visitado el perfil de Administrador Sin apellido eheh', '2018-05-24 00:02:15.286000', 1);
INSERT INTO ACTIVIDAD (ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (3, '2018-05-24 00:02:27.493000', 'Ha visitado el perfil de Administrador Sin apellido eheh', '2018-05-24 00:02:27.493000', 1);
INSERT INTO ACTIVIDAD (ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (4, '2018-05-24 00:02:37.633000', 'Ha visitado el perfil de Administrador Sin apellido eheh', '2018-05-24 00:02:37.633000', 2);
INSERT INTO ACTIVIDAD (ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (6, '2018-05-24 00:02:44.448000', 'Ha agregado como amigo a Administrador Sin apellido eheh', '2018-05-24 00:02:44.448000', 2);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (5, '2018-05-24 00:02:38.109000', 'Ha visitado el perfil de Administrador Sin apellido eheh', '2018-05-24 00:02:38.109000', 2);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (7, '2018-05-24 00:02:44.479000', 'Ha visitado el perfil de Administrador Sin apellido eheh', '2018-05-24 00:02:44.479000', 2);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (8, '2018-05-24 00:02:49.375000', 'Ha visitado el perfil de Administrador Sin apellido eheh', '2018-05-24 00:02:49.375000', 2);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (9, '2018-05-24 00:02:53.912000', 'Ha visitado el perfil de Administrador Sin apellido eheh', '2018-05-24 00:02:53.912000', 1);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (10, '2018-05-24 00:02:57.995000', 'Ha visitado el perfil de Adrian S.', '2018-05-24 00:02:57.995000', 1);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (11, '2018-05-24 00:02:58.157000', 'Ha visitado el perfil de Adrian S.', '2018-05-24 00:02:58.157000', 1);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (12, '2018-05-24 00:03:03.644000', 'Ha visitado el perfil de Adrian S.', '2018-05-24 00:03:03.644000', 1);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (13, '2018-05-24 00:03:18.140000', 'Ha visitado el perfil de Administrador Sin apellido eheh', '2018-05-24 00:03:18.140000', 2);
INSERT INTO ACTIVIDAD(ID, CREATED_AT, ESTADO, UPDATED_AT, USER_ID) VALUES (14, '2018-05-24 00:03:33.174000', 'Ha entrado a la administración', '2018-05-24 00:03:33.174000', 2);
INSERT INTO periodico (id, nombre, url) VALUES (1, 'ElPais', 'http://elpais.com');
INSERT INTO periodico (id, nombre, url) VALUES (2, 'ElMundo', 'http://www.elmundo.es/');
INSERT INTO periodico (id, nombre, url) VALUES (3, 'ElDiario', 'http://www.eldiario.es/');
INSERT INTO periodico (id, nombre, url) VALUES (4, 'ABC', 'http://www.abc.es/');
insert into tag (nombre, fecha) values ('Politica', CURRENT_TIMESTAMP);
insert into tag (nombre, fecha) values ('Comercio', CURRENT_TIMESTAMP);
insert into tag (nombre, fecha) values ('Economia', CURRENT_TIMESTAMP);
insert into tag_articulos (tags_nombre, articulos_id) values ('Politica', 1);
insert into tag_articulos (tags_nombre, articulos_id) values ('Economia', 2);
insert into tag_articulos (tags_nombre, articulos_id) values ('Economia', 3);