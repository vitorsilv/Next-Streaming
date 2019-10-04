create table streamer (
    idStreamer int primary key identity (1,1),
    nome varchar (45) not null,
    cpf varchar (20) not null,
    telefone int not null,
    email varchar (100) not null,
    senha varchar (255) not null
);

create table endereco (
    idEndereco int primary key identity(1,1),
    logradouro varchar (80) not null,
    numero int not null,
    complemento varchar (30),
    bairro varchar (60) not null,
    cidade varchar (50) not null,
    uf char (2) not null,
    cep int not null
);

create table enderecoStreamer(
    idEnderecoStreamer int primary key identity(1,1),
    idEndereco int foreign key references endereco (idEndereco),
    idStreamer int foreign key references streamer (idStreamer)
);

create table maquina (
    idMaquina int primary key identity (1,1) not null,
    cpu float,
    ram float,
    disco float,
    gpu float,
    idStreamer int FOREIGN KEY REFERENCES streamer (idStreamer)
);

create table alerta (
    idAlerta int primary key identity (1,1),
    descricao varchar (80),
    dataHora datetime
);

create table monitoramento (
    idMonitoramento int primary key identity (1,1),
    cpu float,
    ram float,
    disco float,
    gpu float,
    dataHora datetime,
    idAlerta int foreign key references alerta (idAlerta),
    idMaquina int foreign key references maquina (idMaquina)
);

