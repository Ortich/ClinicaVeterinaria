DROP DATABASE IF EXISTS clinicaufvet;
CREATE DATABASE IF NOT EXISTS clinicaufvet;
USE clinicaufvet;
CREATE TABLE IF NOT EXISTS cliente(
	dni varchar(9) NOT NULL PRIMARY KEY,
    nombre varchar(20) NOT NULL,
    apellido varchar(40) NOT NULL,
    direccion varchar(128) NOT NULL,
	cp int(5) NOT NULL,
    telefono int(9) NOT NULL,
    poblacion varchar(40) NOT NULL,
    email varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
    
CREATE TABLE IF NOT EXISTS veterinario(
	dni varchar(9) NOT NULL PRIMARY KEY,
    nombre varchar(20) NOT NULL,
    apellido varchar(40) NOT NULL,
    pass varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS mascota(
	chip int(15) NOT NULL PRIMARY KEY,
    nombre varchar(20) NOT NULL,
    sexo int(1) NOT NULL,
    especie varchar(32) NOT NULL,
    raza varchar(64) NOT NULL,
    fecha_nacimiento date DEFAULT NULL,
    cliente varchar(9) NOT NULL,
    img int(10) DEFAULT 0,
    CONSTRAINT cliente FOREIGN KEY (cliente) REFERENCES cliente (dni) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS cita(
	id mediumint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    fecha_cita date NOT NULL,
    descripcion text NOT NULL,
    mascota int(15) NOT NULL,
    veterinario varchar(9) NOT NULL,
    CONSTRAINT mascota FOREIGN KEY (mascota) REFERENCES mascota (chip) ON DELETE NO ACTION ON UPDATE CASCADE,
    CONSTRAINT veterinario FOREIGN KEY (veterinario) REFERENCES veterinario (dni) ON DELETE NO ACTION ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS tienda(
	id mediumint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre varchar(20) NOT NULL,
    precio decimal NOT NULL,
    stock int(10) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO cliente ( dni, nombre, apellido, direccion, cp, telefono, poblacion, email) VALUES
('00000001C','Antonio','Perez','Paseo de Las Humanidades, Modulo 3, Aula Android',00000,666666661,'UFV','email@email.com'),
('00000002C','Laura','Sanz','Paseo de Las Humanidades, Modulo 2, Aula 3.4',00001,666666662,'UFV','email2@email.es'),
('00000003C','Alexander','Enano','Paseo de Las Humanidades, Modulo 3, Aula Android',00002,666666663,'UFV','email3@email.es'),
('00000004C','Dano','Ortoch','Paseo de Las Humanidades, Modulo 3, Aula Android',00003,666666664,'UFV','email4@email.es'),
('00000005C','Marco','Lampiño','Paseo de Las Humanidades, Modulo 3, Aula Android',00004,666666665,'UFV','email5@email.es'),
('00000006C','Kalyan','Lazair','Paseo de Las Humanidades, Modulo 3, Aula Android',00005,666666666,'UFV','email6@email.es'),
('00000007C','Lincenciado','Cisneros','Paseo de Las Humanidades, Modulo 3, Aula Android',00006,666666667,'UFV','email7@email.es'),
('00000008C','Karel','Stanford','Paseo de Las Humanidades, Modulo 3, Aula Androide',00007,666666668,'UFV','email8@email.es'),
('00000009C','Cono','Conez','Paseo de Las Humanidades, Modulo 3, Aula Cono',00008,666666669,'UFV','email9@email.es'),
('00000010C','Bocata','Tortilla del Viernes','Cafeteria',00009,666666610,'UFV','email10@email.es')
;

INSERT INTO veterinario (dni, nombre, apellido, pass) VALUES
('00000003V','Chandra','Piromana','burniton'),
('00000004V','Nicol','Bolas','killemall'),
('00000005V','Jace','Beleren','discard'),
('00000007V','Admin','Admin','1234')
;

INSERT INTO mascota (chip, nombre, sexo, especie, raza, fecha_nacimiento, cliente, img) VALUES
(000000000000001,'Igor',2,'Caracol','InMidiusSiemprus','1998-04-08','00000001C',7),
(000000000000002,'Oker',1,'Perro','Dalmata','1997-07-07','00000002C',5),
(000000000000003,'Igor',1,'Pez','Clown','1998-04-07','00000002C',2),
(000000000000004,'DoomFist',1,'Gato','Thief','2018-05-09','00000004C',1),
(000000000000005,'D.Va',0,'Mecha','Korean','2016-11-05','00000003C',0),
(000000000000006,'Igor',0,'Perro','Serbio','1998-04-07','00000003C',5),
(000000000000007,'Igor',1,'Sifilis','Venerea','1988-04-07','00000004C',0),
(000000000000008,'Igor',1,'Cancer','Tumor','1998-02-07','00000005C',0),
(000000000000009,'Igor',2,'Medusa','Australiana','1998-03-11','00000006C',4),
(000000000000010,'Igor',1,'Cisneros','Programacion','1978-04-07','00000007C',0),
(000000000000011,'Igor',0,'Enchufe','NoTengoImaginacion','1938-09-02','00000008C',0),
(000000000000012,'Igor',0,'Cona','Oprimida','1999-04-07','00000009C',0),
(000000000000013,'Igor',0,'DelDia','DosLeuros','1994-01-28','00000010C',0),
(000000000000014,'Flash',0,'Perzoso','Lentus','1994-01-28','00000004C',8)
;

INSERT INTO cita (fecha_cita,descripcion,mascota,veterinario) VALUES
('2018-01-01','Le he intervenido el tumor cerebral que le hacía estar siempre en medio.',000000000000001,'00000004V'),
('2018-01-02','He tenido que sacrificarlo, no se quitaba de en medio.',000000000000001,'00000004V'),
('2001-09-11','Exceso de manchas, he tenido que quitarle algunas.',000000000000002,'00000003V'),
('2015-11-13','El animal ha perdido demasiadas manchas, he tenido que devolverle algunas.',000000000000002,'00000003V'),
('2014-10-03','El perro se pone demasiado en medio',000000000000006,'00000005V'),
('2012-01-11','El pez es muy poco payaso',000000000000003,'00000005V'),
('2010-12-13','Se pasa el dia intentado robar la cartera de su dueño',000000000000004,'00000003V'),
('2005-11-13','Tiene una adiccion a los doritos y al mountain dew',000000000000005,'00000003V'),
('2025-01-13','Cuidado con la mafia serbia',000000000000006,'00000004V'),
('2005-12-03','No me apetece pensar una descripcion de la cita',000000000000007,'00000004V'),
('2007-10-17','No tengo ganas de hacer descripciones',000000000000008,'00000005V'),
('2015-02-15','La medusa se mueve mucho',000000000000009,'00000005V'),
('2008-07-02','Hay que meterle Java intravenosa',0000000000000010,'00000003V'),
('2012-12-12','NoTengoImaginacion es muy manso',000000000000011,'00000003V'),
('2005-05-10','Pasando de escribir la descripcion de la cita',0000000000000012,'00000004V'),
('2025-10-10','Server Status, Client connections bla bla bla',000000000000013,'00000004V') 
;

INSERT INTO tienda (nombre,precio,stock) VALUES
('Comida Perro', 12.50, 10),
('Pelota de Goma', 0.50, 99),
('Comida de gato', 12.50, 10),
('Correa de perro', 6.50,8),
('Arena de gato', 24.50,5),
('Comida de peces', 2.50,20),
('Snack de perro', 4.50,15),
('Codingbat', 3.50,9),
('Pecera', 30.50,3),
('Vendaje', 9.50,20),
('Peluche', 7.50,6),
('Hueso', 1.50,30),
('Bozal', 2.50,12),
('Arnés de Hurón', 0.50,999),
('Bebedero', 18.50,5),
('Jaula', 72.20,2),
('Pienso', 6.50,8),
('Rueda de Hamster', 15.99,4)













