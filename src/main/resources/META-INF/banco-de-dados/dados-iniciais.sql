insert into produto (data_criacao, nome, preco, descricao) values (DATE('2022-02-01'), 'Kindle', 799.0, 'Conheça o novo Kindle, agora com iluminação embutida ajustável, que permite que você leia em ambientes abertos ou fechados, a qualquer hora do dia.');
insert into produto (data_criacao, nome, preco, descricao) values (DATE('2022-02-01'), 'Câmera GoPro Hero 7', 1400.0, 'Desempenho 2x melhor.');
insert into produto (data_criacao, nome, preco, descricao) values (DATE('2022-02-01'), 'Câmera Canon 80D', 3500, 'melhor foco');

insert into cliente (nome, data_criacao, cpf) values('Fernando Medeiros', Date('2022-02-01'), '00000000001');
insert into cliente (nome, data_criacao, cpf) values('Marcos Mariano', Date('2022-02-01'), '00000000002');

insert into cliente_detalhe (sexo, data_nascimento, cliente_id) values('MASCULINO', DATE('1980-06-15'), 1);

insert into cliente_detalhe (sexo, data_nascimento, cliente_id) values('MASCULINO', DATE('1987-06-15'), 2);

insert into pedido (data_criacao, cliente_id, total, status) values (DATE('2022-02-01'),  1,  3798, 'AGUARDANDO');

insert into pedido (data_criacao, cliente_id, total, status) values (DATE('2022-01-01'),  1,  2800, 'AGUARDANDO');

insert into pedido (data_criacao, cliente_id, total, status) values (DATE('2021-12-20'),  2,  7000, 'AGUARDANDO');

insert into pedido (data_criacao, cliente_id, total, status) values (DATE('2021-12-20'),  1,  799, 'PAGO');

insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 1, 1, 2, 499);
insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 1, 2, 2, 1400);

insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 2, 2, 2, 1400);

insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 3, 3, 2, 3500);

insert into item_pedido (pedido_id, produto_id, quantidade, preco_produto) values ( 4, 1, 1, 799);

insert into pagamento (pedido_id, status, tipo_pagamento, numero_cartao, codigo_barra, data_criacao) values (1, 'PROCESSANDO', 'cartao', '123', null, DATE('2022-02-01'));

insert into pagamento (pedido_id, status, tipo_pagamento, numero_cartao, codigo_barra, data_criacao) values (2, 'PROCESSANDO', 'cartao', '4567', null, DATE('2022-02-01'));

insert into pagamento (pedido_id, status, tipo_pagamento, numero_cartao, codigo_barra, data_criacao) values (3, 'RECEBIDO', 'boleto', null, '8910', DATE('2022-02-01'));

insert into pagamento (pedido_id, status, tipo_pagamento, numero_cartao, codigo_barra, data_criacao) values (4, 'PROCESSANDO', 'cartao', '1112', null, DATE('2022-02-01'));

insert into nota_fiscal (pedido_id, data_emissao, xml) values(1, DATE('2022-02-01'), '<xml\>');

insert into categoria (data_criacao, nome) values (DATE('2022-02-01'), "Eletrônicos");
insert into categoria (nome, data_criacao) values ('Eletrodomésticos', DATE('2022-02-01'));
insert into categoria (nome, data_criacao) values ('Livros', DATE('2022-02-01'));
insert into categoria (nome, data_criacao) values ('Esportes', DATE('2022-02-01'));
insert into categoria (nome, data_criacao) values ('Futebol', DATE('2022-02-01'));
insert into categoria (nome, data_criacao) values ('Natação', DATE('2022-02-01'));
insert into categoria (nome, data_criacao) values ('Notebooks', DATE('2022-02-01'));
insert into categoria (nome, data_criacao) values ('Smartphones',DATE('2022-02-01'));

insert into produto_categoria (produto_id, categoria_id) values(1, 1);

insert into produto_categoria (produto_id, categoria_id) values(2, 2);
insert into produto_categoria (produto_id, categoria_id) values(3, 2);





