insert into produto (data_criacao, nome, preco, descricao, ativo, versao) values (DATE('2022-02-01'), 'Kindle', 799.0, 'Conheça o novo Kindle, agora com iluminação embutida ajustável, que permite que você leia em ambientes abertos ou fechados, a qualquer hora do dia.', 'Sim', 0);
insert into produto (data_criacao, nome, preco, descricao, ativo, versao) values (DATE('2022-02-01'), 'Câmera GoPro Hero 7', 1400.0, 'Desempenho 2x melhor.', 'Nao', 0);
insert into produto (data_criacao, nome, preco, descricao, ativo, versao) values (DATE('2022-02-01'), 'Câmera Canon 80D', 3500, 'melhor foco', 'Sim', 0);

insert into cliente (nome, data_criacao, cpf, versao) values('Fernando Medeiros', Date('2022-02-01'), '00000000001', 0);
insert into cliente (nome, data_criacao, cpf, versao) values('Marcos Mariano', Date('2022-02-01'), '00000000002', 0);

insert into cliente_detalhe (sexo, data_nascimento, cliente_id) values('MASCULINO', DATE('1980-06-15'), 1);

insert into cliente_detalhe (sexo, data_nascimento, cliente_id) values('MASCULINO', DATE('1987-06-15'), 2);

insert into pedido (data_criacao, cliente_id, total, status, versao) values (DATE('2022-02-01'),  1,  3798, 'AGUARDANDO', 0);

insert into pedido (data_criacao, cliente_id, total, status, versao) values (DATE('2022-01-01'),  1,  2800, 'AGUARDANDO', 0);

insert into pedido (data_criacao, cliente_id, total, status, versao) values (DATE('2021-12-20'),  2,  7000, 'AGUARDANDO', 0);

insert into pedido (data_criacao, cliente_id, total, status, versao) values (DATE('2021-12-20'),  1,  799, 'PAGO', 0);

insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 1, 1, 2, 499);
insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 1, 2, 2, 1400);

insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 2, 2, 2, 1400);

insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 3, 3, 2, 3500);

insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 4, 1, 1, 799);

insert into pagamento (pedido_id, status, tipo_pagamento, numero_cartao, codigo_barra, data_criacao, versao) values (1, 'PROCESSANDO', 'cartao', '123', null, DATE('2022-02-01'), 0);

insert into pagamento (pedido_id, status, tipo_pagamento, numero_cartao, codigo_barra, data_criacao, versao) values (2, 'PROCESSANDO', 'cartao', '4567', null, DATE('2022-02-01'), 0);

insert into pagamento (pedido_id, status, tipo_pagamento, numero_cartao, codigo_barra, data_criacao, versao) values (3, 'RECEBIDO', 'boleto', null, '8910', DATE('2022-02-01'), 0);

insert into pagamento (pedido_id, status, tipo_pagamento, numero_cartao, codigo_barra, data_criacao, versao) values (4, 'PROCESSANDO', 'cartao', '1112', null, DATE('2022-02-01'), 0);

insert into nota_fiscal (pedido_id, data_emissao, xml) values(1, DATE('2022-02-01'), '<xml\>');

insert into categoria (data_criacao, nome, versao) values (DATE('2022-02-01'), "Eletrônicos", 0);
insert into categoria (nome, data_criacao, versao) values ('Eletrodomésticos', DATE('2022-02-01'), 0);
insert into categoria (nome, data_criacao, versao) values ('Livros', DATE('2022-02-01'), 0);
insert into categoria (nome, data_criacao, versao) values ('Esportes', DATE('2022-02-01'), 0);
insert into categoria (nome, data_criacao, versao) values ('Futebol', DATE('2022-02-01'), 0);
insert into categoria (nome, data_criacao, versao) values ('Natação', DATE('2022-02-01'), 0);
insert into categoria (nome, data_criacao, versao) values ('Notebooks', DATE('2022-02-01'), 0);
insert into categoria (nome, data_criacao, versao) values ('Smartphones',DATE('2022-02-01'), 0);

insert into produto_categoria (produto_id, categoria_id) values(1, 1);

insert into produto_categoria (produto_id, categoria_id) values(2, 2);
insert into produto_categoria (produto_id, categoria_id) values(3, 2);





