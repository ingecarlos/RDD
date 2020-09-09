use odk_prod;

#store procedure para validar el login 
delimiter //
create procedure odkapi_login(codigo int, password varchar(5))
begin
		select c.Codigo codigo,c.Nombre nombre, c.Tipo tipo, c.Departamento departamento, c.Sede sede, c.Especialidad especialidad
		from acompanante c
		where c.Codigo=codigo and c.password=md5(password);
end//
delimiter ;
call odkapi_login(105,'2312');


call odkapi_getCentro("03-01-0052-45");

#store procedure retornar un centro basado en un codigo
delimiter //
create procedure odkapi_getCentro(codigo varchar(14))
begin
		select c.Codigo codigo,c.Nombre nombre,m.nombre_muni municipio, c.Direccion direccion, c.latitud latitud, c.longitud longitud
		from centroEducativo c
        join municipio m on (c.Municipio = m.id_muni)
		where c.Codigo=codigo and c.Departamento = m.id_depto
        and c.latitud is not null;
end//
delimiter ;

#store procedure retornar la lista de departamentos
delimiter //
create procedure odkapi_getDepartamentos()
begin
		select d.id_depto id, d.nombre_depto departamento 
		from departamento d;
end//
delimiter ;
call odkapi_getDepartamentos();

#store procedure retornar la lista de municipios de un departamento 
delimiter //
create procedure odkapi_getMunicipios(id_depto integer)
begin
		select m.id_depto id_depto,m.id_muni id_muni, m.nombre_muni municipio
		from municipio m
        where cast(m.id_depto as unsigned) = id_depto;
end//
delimiter ;
call odkapi_getMunicipios(16);


#store procedure retornar la lista de niveles de un departamento/municipio
delimiter //
create procedure odkapi_getNiveles(id_depto integer,id_muni integer)
begin
		select Distinct n.idNivel id_nivel, n.nivel nivel
		from centroEducativo c
		join nivelCentro n on (n.idNivel = c.nivel)
		where c.Departamento=id_depto
		and c.Municipio = id_muni
        and c.latitud is not null;
end//
delimiter ;
call odkapi_getNiveles(16,2);

drop procedure odkapi_getSectores;
#store procedure retornar la lista de sectores de un departamento/municipio/nivel
delimiter //
create procedure odkapi_getSectores(id_depto integer,id_muni integer,id_nivel integer)
begin
		select Distinct s.idSector id_sector, s.sector sector
		from centroEducativo c
		join sectorCentro s on (s.idSector = c.sector)
		where c.Departamento=id_depto
		and c.Municipio = id_muni
        and c.nivel = id_nivel
        and c.latitud is not null;
end//
delimiter ;
call odkapi_getSectores(16,2,1);


#store procedure retornar la lista de centros filtrados
delimiter //
create procedure odkapi_getCentros(id_depto integer, id_mun integer, nivel integer, sector integer)
begin
		if sector = -1 then 
			select c.Codigo codigo,c.Nombre nombre,m.nombre_muni municipio, c.Direccion direccion, c.latitud latitud, c.longitud longitud
			from centroEducativo c
			join municipio m on (c.Municipio = m.id_muni)
			where c.Departamento = m.id_depto 
            and  cast(c.Departamento as unsigned)=id_depto 
            and cast(c.Municipio as unsigned)= id_mun
            and c.nivel=nivel
            and c.latitud is not null;
		else
			select c.Codigo codigo,c.Nombre nombre,m.nombre_muni municipio, c.Direccion direccion, c.latitud latitud, c.longitud longitud
			from centroEducativo c
			join municipio m on (c.Municipio = m.id_muni)
			where c.Departamento = m.id_depto 
            and  cast(c.Departamento as unsigned)=id_depto 
            and cast(c.Municipio as unsigned)= id_mun
            and c.nivel=nivel
            and c.sector=sector
            and c.latitud is not null;
		end if;
end//
delimiter ;

call odkapi_getCentros(3,1,1,1);


#store procedure retornar la lista de centros filtrados por depto y municipio
delimiter //
create procedure rdd_getCentros(id_depto integer, id_mun integer, nivel integer)
begin
	select c.Codigo codigo,c.Nombre nombre,m.nombre_muni municipio, c.Direccion direccion, c.latitud latitud, c.longitud longitud
	from centroEducativo c
	join municipio m on (c.Municipio = m.id_muni)
	where c.Departamento = m.id_depto 
		and  cast(c.Departamento as unsigned)=id_depto 
        and cast(c.Municipio as unsigned)= id_mun
        and c.nivel=nivel
        and c.latitud is not null;
end//
delimiter ;
select * from centroEducativo;
call rdd_getCentros(3,1);

select * from centroEducativo;


select count( *) from centroEducativo;

alter table centroEducativo
add column latitud double(17,11) null,
add column longitud double(17,11) null,
add column precisionCoordenada double (17,11) null;

create table coordenada(
	codigo varchar(15) null,
    latitud double(17,11) null,
    longitud double(17,11) null,
    precisionCoordenada double(17,11) null
);


LOAD DATA 
INFILE 'C:/Users/Carlos/Desktop/FHI Desktop/Coordenadas centros educativos.csv' 
INTO TABLE coordenada
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

select * from coordenada;
select * from centroEducativo;

/*pasar coordenadas a tabla de centro educativo*/
update centroEducativo centro
inner join coordenada on centro.Codigo = coordenada.codigo
set centro.latitud=coordenada.latitud,
	centro.longitud=coordenada.longitud, 
    centro.precisionCoordenada=coordenada.precisionCoordenada;
    
select count(*) from coordenada;

create table nivelCentro(
	idNivel int not null primary key auto_increment,
    nivel  varchar(20) not null
);
create table sectorCentro(
	idSector int not null primary key auto_increment,
    sector  varchar(30) not null
);

insert into nivelCentro(nivel) values ("Básico");
insert into sectorCentro(sector) values("Oficial");
select * from centroEducativo;


alter table centroEducativo
add column nivel int null,
add column sector int null;

alter table centroEducativo
add constraint fk_centro_nivel foreign key (nivel) references nivelCentro(idNivel),
add constraint fk_centro_sector foreign key (sector) references sectorCentro(idSector);

update centroEducativo 
set nivel = 1, 
	sector = 1;
    



create table centroSector(
	codigo varchar(15),
    sector varchar(15)
    );
    
    
    
    select * from centroSector;

    
    insert into sectorCentro(sector)
    select distinct sector from centroSector;
		
    select * from sectorCentro;
    
    select * from centroEducativo;
    update centroEducativo c
    join centroSector cs on c.codigo = cs.codigo
    join sectorCentro sc on cs.sector = sc.sector
    set c.sector = sc.idSector;
    
