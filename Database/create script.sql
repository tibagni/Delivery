-- Table: admin

-- DROP TABLE admin;

CREATE TABLE admin
(
  user_name character varying NOT NULL,
  password character varying NOT NULL,
  CONSTRAINT admin_pk PRIMARY KEY (user_name )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE admin
  OWNER TO postgres;

-- Table: categoria

-- DROP TABLE categoria;

CREATE TABLE categoria
(
  cod_categoria serial NOT NULL,
  categoria_cod_categoria integer,
  nome character varying NOT NULL,
  CONSTRAINT categoria_pkey PRIMARY KEY (cod_categoria ),
  CONSTRAINT categoria_categoria_cod_categoria_fkey FOREIGN KEY (categoria_cod_categoria)
      REFERENCES categoria (cod_categoria) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE categoria
  OWNER TO postgres;

  -- Table: conta_cliente

-- DROP TABLE conta_cliente;

CREATE TABLE conta_cliente
(
  cpf bigint NOT NULL,
  nome character varying NOT NULL,
  telefone character varying NOT NULL,
  email character varying NOT NULL,
  senha character varying,
  CONSTRAINT cliente_pkey PRIMARY KEY (cpf ),
  CONSTRAINT unique_email UNIQUE (email )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conta_cliente
  OWNER TO postgres;

 -- Table: endereco

-- DROP TABLE endereco;

CREATE TABLE endereco
(
  rua character varying NOT NULL,
  cod_endereco serial NOT NULL,
  bairro character varying NOT NULL,
  cidade character varying NOT NULL,
  uf character varying NOT NULL,
  complemento character varying,
  numero integer,
  conta_cliente_cpf bigint,
  cep character varying,
  CONSTRAINT endereco_pkey PRIMARY KEY (cod_endereco ),
  CONSTRAINT conta_cliente_cpf_fk FOREIGN KEY (conta_cliente_cpf)
      REFERENCES conta_cliente (cpf) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE endereco
  OWNER TO postgres;

-- Index: fki_conta_cliente_cpf_fk

-- DROP INDEX fki_conta_cliente_cpf_fk;

CREATE INDEX fki_conta_cliente_cpf_fk
  ON endereco
  USING btree
  (conta_cliente_cpf );

-- Table: entregador

-- DROP TABLE entregador;

CREATE TABLE entregador
(
  cod_entregador serial NOT NULL,
  senha character varying NOT NULL,
  token character varying,
  nome character varying,
  CONSTRAINT cod_entregador_pk PRIMARY KEY (cod_entregador )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE entregador
  OWNER TO postgres;


-- Table: item_pedido

-- DROP TABLE item_pedido;

CREATE TABLE item_pedido
(
  cod_item_pedido serial NOT NULL,
  pedido_cod_pedido integer NOT NULL,
  produto_cod_produto integer NOT NULL,
  valor double precision,
  tamanhos_cod_tamanho integer,
  CONSTRAINT item_pedido_pkey PRIMARY KEY (cod_item_pedido , pedido_cod_pedido ),
  CONSTRAINT item_pedido_pedido_cod_pedido_fkey FOREIGN KEY (pedido_cod_pedido)
      REFERENCES pedido (cod_pedido) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT item_pedido_produto_cod_produto_fkey FOREIGN KEY (produto_cod_produto)
      REFERENCES produto (cod_produto) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tamanhos_cod_tamanho_fk FOREIGN KEY (tamanhos_cod_tamanho)
      REFERENCES tamanhos (cod_tamanho) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE item_pedido
  OWNER TO postgres;

-- Index: fki_tamanhos_cod_tamanho_fk

-- DROP INDEX fki_tamanhos_cod_tamanho_fk;

CREATE INDEX fki_tamanhos_cod_tamanho_fk
  ON item_pedido
  USING btree
  (tamanhos_cod_tamanho );


-- Table: item_pedido_has_opcional

-- DROP TABLE item_pedido_has_opcional;

CREATE TABLE item_pedido_has_opcional
(
  item_pedido_cod_item_pedido integer NOT NULL,
  opcional_cod_opcional integer NOT NULL,
  item_pedido_pedido_cod_pedido integer NOT NULL,
  CONSTRAINT item_pedido_has_opcional_pkey PRIMARY KEY (item_pedido_cod_item_pedido , opcional_cod_opcional , item_pedido_pedido_cod_pedido ),
  CONSTRAINT item_pedido_has_opcional_opcional_cod_opcional_fkey FOREIGN KEY (opcional_cod_opcional)
      REFERENCES opcional (cod_opcional) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tem_pedido_has_opcional_item_pedido_fky FOREIGN KEY (item_pedido_cod_item_pedido, item_pedido_pedido_cod_pedido)
      REFERENCES item_pedido (cod_item_pedido, pedido_cod_pedido) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE item_pedido_has_opcional
  OWNER TO postgres;


-- Table: item_pedido_has_sabor

-- DROP TABLE item_pedido_has_sabor;

CREATE TABLE item_pedido_has_sabor
(
  item_pedido_pedido_cod_pedido integer NOT NULL,
  item_pedido_cod_item_pedido integer NOT NULL,
  sabor_cod_sabor integer NOT NULL,
  CONSTRAINT item_pedido_has_sabor_pkey PRIMARY KEY (item_pedido_pedido_cod_pedido , item_pedido_cod_item_pedido , sabor_cod_sabor ),
  CONSTRAINT item_pedido_has_sabor_item_pedido_fkey FOREIGN KEY (item_pedido_cod_item_pedido, item_pedido_pedido_cod_pedido)
      REFERENCES item_pedido (cod_item_pedido, pedido_cod_pedido) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT item_pedido_has_sabor_sabor_cod_sabor_fkey FOREIGN KEY (sabor_cod_sabor)
      REFERENCES sabor (cod_sabor) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE item_pedido_has_sabor
  OWNER TO postgres;


-- Table: opcional

-- DROP TABLE opcional;

CREATE TABLE opcional
(
  cod_opcional serial NOT NULL,
  produto_cod_produto integer NOT NULL,
  descricao character varying,
  valor_acrescimo double precision NOT NULL,
  CONSTRAINT opcional_pkey PRIMARY KEY (cod_opcional ),
  CONSTRAINT opcional_produto_cod_produto_fkey FOREIGN KEY (produto_cod_produto)
      REFERENCES produto (cod_produto) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE opcional
  OWNER TO postgres;

-- Table: pagamento

-- DROP TABLE pagamento;

CREATE TABLE pagamento
(
  cod_pagamento bigserial NOT NULL,
  pedido_cod_pedido bigint NOT NULL,
  ref character varying NOT NULL,
  status integer NOT NULL DEFAULT 0,
  urlpagamento character varying,
  valorcombinado double precision,
  CONSTRAINT pagamento_pkey PRIMARY KEY (cod_pagamento ),
  CONSTRAINT pagamento_pedido_cod_pedido_fkey FOREIGN KEY (pedido_cod_pedido)
      REFERENCES pedido (cod_pedido) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unique_ref UNIQUE (ref )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pagamento
  OWNER TO postgres;

-- Table: pedido

-- DROP TABLE pedido;

CREATE TABLE pedido
(
  cod_pedido bigserial NOT NULL,
  entregador_cod_entregador integer,
  endereco_cod_endereco integer NOT NULL,
  cliente_cpf bigint NOT NULL,
  situacao integer,
  valor double precision NOT NULL,
  data_pedido timestamp without time zone,
  CONSTRAINT pedido_pkey PRIMARY KEY (cod_pedido ),
  CONSTRAINT endereco_cod_endereco_fk FOREIGN KEY (endereco_cod_endereco)
      REFERENCES endereco (cod_endereco) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT pedido_cliente_cpf_fkey FOREIGN KEY (cliente_cpf)
      REFERENCES conta_cliente (cpf) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pedido
  OWNER TO postgres;

-- Index: fki_endereco_cod_endereco_fk

-- DROP INDEX fki_endereco_cod_endereco_fk;

CREATE INDEX fki_endereco_cod_endereco_fk
  ON pedido
  USING btree
  (endereco_cod_endereco );


-- Table: preco

-- DROP TABLE preco;

CREATE TABLE preco
(
  sabor_cod_sabor integer NOT NULL,
  tamanhos_cod_tamanho integer NOT NULL,
  preco double precision NOT NULL,
  CONSTRAINT preco_pkey PRIMARY KEY (sabor_cod_sabor , tamanhos_cod_tamanho ),
  CONSTRAINT preco_sabor_cod_sabor_fkey FOREIGN KEY (sabor_cod_sabor)
      REFERENCES sabor (cod_sabor) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT preco_tamanhos_cod_tamanho_fkey FOREIGN KEY (tamanhos_cod_tamanho)
      REFERENCES tamanhos (cod_tamanho) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE preco
  OWNER TO postgres;


-- Table: produto

-- DROP TABLE produto;

CREATE TABLE produto
(
  cod_produto serial NOT NULL,
  categoria_cod_categoria integer NOT NULL,
  nome character varying NOT NULL,
  descricao character varying NOT NULL,
  qtde_sabores_perm integer DEFAULT 0,
  qtde_opcionais_perm integer,
  foto character varying NOT NULL,
  CONSTRAINT produto_pkey PRIMARY KEY (cod_produto ),
  CONSTRAINT produto_categoria_cod_categoria_fkey FOREIGN KEY (categoria_cod_categoria)
      REFERENCES categoria (cod_categoria) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE produto
  OWNER TO postgres;

-- Table: rating

-- DROP TABLE rating;

CREATE TABLE rating
(
  "userId" bigint NOT NULL,
  "itemId" integer NOT NULL,
  "ratingValue" integer NOT NULL,
  CONSTRAINT rating_pk PRIMARY KEY ("userId" , "itemId" , "ratingValue" )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE rating
  OWNER TO postgres;


-- Table: sabor

-- DROP TABLE sabor;

CREATE TABLE sabor
(
  cod_sabor serial NOT NULL,
  produto_cod_produto integer NOT NULL,
  nome character varying NOT NULL,
  descricao character varying NOT NULL,
  foto character varying NOT NULL,
  CONSTRAINT sabor_pkey PRIMARY KEY (cod_sabor ),
  CONSTRAINT sabor_produto_cod_produto_fkey FOREIGN KEY (produto_cod_produto)
      REFERENCES produto (cod_produto) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sabor
  OWNER TO postgres;


-- Table: similarity

-- DROP TABLE similarity;

CREATE TABLE similarity
(
  count integer NOT NULL DEFAULT 0,
  sum integer NOT NULL DEFAULT 0,
  "ItemID1" integer NOT NULL DEFAULT 0,
  "ItemID2" integer NOT NULL DEFAULT 0,
  CONSTRAINT similarity_pk PRIMARY KEY ("ItemID1" , "ItemID2" )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE similarity
  OWNER TO postgres;


-- Table: tamanhos

-- DROP TABLE tamanhos;

CREATE TABLE tamanhos
(
  cod_tamanho serial NOT NULL,
  nome character varying NOT NULL,
  produto_cod_produto integer NOT NULL,
  CONSTRAINT tamanhos_pkey PRIMARY KEY (cod_tamanho ),
  CONSTRAINT tamanhos_produto_cod_produto_fkey FOREIGN KEY (produto_cod_produto)
      REFERENCES produto (cod_produto) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE tamanhos
  OWNER TO postgres;