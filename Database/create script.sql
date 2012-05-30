CREATE TABLE Categoria (
  cod_categoria SERIAL NOT NULL PRIMARY KEY,
  Categoria_cod_categoria Integer references Categoria (cod_categoria),
  nome VARCHAR NOT NULL
);

CREATE TABLE Endereco (
  cep BIGINT NOT NULL,
  PRIMARY KEY(cep)
);

CREATE TABLE Cliente (
  cpf BIGINT NOT NULL,
  Endereco_cep BIGINT NOT NULL references Endereco (cep),
  
  PRIMARY KEY(cpf)
);

CREATE TABLE Entregador (
  cod_entregador INTEGER NOT NULL,
  PRIMARY KEY(cod_entregador)
);

CREATE TABLE Produto (
  cod_produto SERIAL NOT NULL,
  Categoria_cod_categoria INTEGER NOT NULL references Categoria (cod_categoria),
  nome VARCHAR NOT NULL,
  descricao VARCHAR NOT NULL,
  qtde_sabores_perm INTEGER NULL DEFAULT 0,
  qtde_opcionais_perm INTEGER NULL,
  
  PRIMARY KEY(cod_produto)
);

CREATE TABLE Tamanhos (
  cod_tamanho SERIAL NOT NULL,
  Produto_cod_produto INTEGER NOT NULL references Produto (cod_produto),
  nome VARCHAR NOT NULL,
  
  PRIMARY KEY(cod_tamanho)
);

CREATE TABLE Sabor (
  cod_sabor SERIAL NOT NULL,
  Produto_cod_produto INTEGER NOT NULL references Produto (cod_produto),
  nome VARCHAR NOT NULL,
  descricao VARCHAR NOT NULL,
  
  PRIMARY KEY(cod_sabor)
);

CREATE TABLE Preco (
  Sabor_cod_sabor INTEGER NOT NULL references Sabor (cod_sabor),
  tamanho INTEGER NOT NULL references Tamanhos (cod_tamanho),
  nome VARCHAR NOT NULL,
  descricao VARCHAR NOT NULL,
  
  PRIMARY KEY(cod_sabor)
);

CREATE TABLE Pedido (
  cod_pedido SERIAL NOT NULL,
  Entregador_cod_entregador INTEGER NOT NULL references Entregador (cod_entregador),
  Endereco_cep BIGINT NOT NULL references Endereco (cep),
  Cliente_cpf BIGINT NOT NULL references Cliente (cpf),
  situacao INTEGER NULL,
  
  PRIMARY KEY(cod_pedido)
);

CREATE TABLE Item_pedido (
  cod_item_pedido SERIAL NOT NULL,
  Pedido_cod_pedido Integer NOT NULL references Pedido (cod_pedido),
  Produto_cod_produto Integer NOT NULL references Produto (cod_produto),
  valor FLOAT NULL,
  
  PRIMARY KEY(cod_item_pedido, Pedido_cod_pedido)
);

CREATE TABLE Opcional (
  cod_opcional SERIAL NOT NULL,
  Produto_cod_produto Integer NOT NULL references Produto (cod_produto),
  descricao VARCHAR NULL,
  valor_acrescimo FLOAT NOT NULL,
  
  PRIMARY KEY(cod_opcional)
);

CREATE TABLE item_pedido_has_opcional
(
  item_pedido_cod_item_pedido integer NOT NULL,
  opcional_cod_opcional integer NOT NULL,
  item_pedido_pedido_cod_pedido integer NOT NULL,
  CONSTRAINT item_pedido_has_opcional_pkey PRIMARY KEY (item_pedido_cod_item_pedido, opcional_cod_opcional, item_pedido_pedido_cod_pedido),
  CONSTRAINT item_pedido_has_opcional_opcional_cod_opcional_fkey FOREIGN KEY (opcional_cod_opcional)
      REFERENCES opcional (cod_opcional) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tem_pedido_has_opcional_item_pedido_fky FOREIGN KEY (item_pedido_cod_item_pedido, item_pedido_pedido_cod_pedido)
      REFERENCES item_pedido (cod_item_pedido, pedido_cod_pedido) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE item_pedido_has_sabor
(
  item_pedido_pedido_cod_pedido integer NOT NULL,
  item_pedido_cod_item_pedido integer NOT NULL,
  sabor_cod_sabor integer NOT NULL,
  CONSTRAINT item_pedido_has_sabor_pkey PRIMARY KEY (item_pedido_pedido_cod_pedido, item_pedido_cod_item_pedido, sabor_cod_sabor),
  CONSTRAINT item_pedido_has_sabor_item_pedido_fkey FOREIGN KEY (item_pedido_cod_item_pedido, item_pedido_pedido_cod_pedido)
      REFERENCES item_pedido (cod_item_pedido, pedido_cod_pedido) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT item_pedido_has_sabor_sabor_cod_sabor_fkey FOREIGN KEY (sabor_cod_sabor)
      REFERENCES sabor (cod_sabor) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE Pagamento (
  cod_pagamento SERIAL NOT NULL,
  Pedido_cod_pedido INTEGER NOT NULL references Pedido (cod_pedido),
  
  PRIMARY KEY(cod_pagamento)
);