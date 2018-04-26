DROP DATABASE IF EXISTS clinicaufvet;
CREATE DATABASE IF NOT EXISTS clinicaufvet;
USE clinicaufvet;
CREATE TABLE IF NOT EXISTS cliente (
	dni varchar(9) NOT NULL PRIMARY KEY,
    nombre varchar(20) NOT NULL,
    apellido varchar(40) NOT NULL,
    direccion varchar(128) NOT NULL,
	cp int(5) NOT NULL,
    telefono int(9) NOT NULL
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

INSERT INTO cliente ( dni, nombre, apellido, direccion, cp, telefono) VALUES
('00000001C','Antonio','Perez','Paseo de Las Humanidades, Modulo 3, Aula Android',00000,666666666),
('00000002C','Laura','Sanz','Paseo de Las Humanidades, Modulo 2, Aula 3.4',00001,666666667)
;

INSERT INTO veterinario (dni, nombre, apellido, pass) VALUES
('00000003V','Chandra','Piromana','burniton'),
('00000004V','Nicol','Bolas','killemall')
;

INSERT INTO mascota (chip, nombre, sexo, especie, raza, fecha_nacimiento, cliente) VALUES
(000000000000001,'Igor',2,'Caracol','InMidiusSiemprus','1998-04-08','00000001C'),
(000000000000002,'Oker',1,'Perro','Dalmata','1997-07-07','00000002C')
;

INSERT INTO cita (fecha_cita,descripcion,mascota,veterinario) VALUES
('2018-01-01','Le he intervenido el tumor cerebral que le hacía estar siempre en medio.',000000000000001,'00000004V'),
('2018-01-02','He tenido que sacrificarlo, no se quitaba de en medio.',000000000000001,'00000004V'),
('2001-09-11','Exceso de manchas, he tenido que quitarle algunas.',000000000000002,'00000003V'),
('2015-11-13','El animal ha perdido demasiadas manchas, he tenido que devolverle algunas.',000000000000002,'00000003V')
;