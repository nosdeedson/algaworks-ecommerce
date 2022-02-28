insert into produto (nome, preco, descricao) values ('Kindle', 499.0, 'Conheça o novo Kindle, agora com iluminação embutida ajustável, que permite que você leia em ambientes abertos ou fechados, a qualquer hora do dia.');
insert into produto (nome, preco, descricao) values ('Câmera GoPro Hero 7', 1400.0, 'Desempenho 2x melhor.');

insert into cliente (nome, data_criacao) values('Fernando Medeiros', Date('2022-02-01'));
insert into cliente (nome, data_criacao) values('Marcos Mariano', Date('2022-02-01'));

insert into cliente_detalhe (sexo, data_nascimento, cliente_id) values('MASCULINO', DATE('1980-06-15'), 1);

insert into cliente_detalhe (sexo, data_nascimento, cliente_id) values('MASCULINO', DATE('1987-06-15'), 2);

insert into pedido (data_criacao, cliente_id, total, status) values (DATE('2022-02-01'),  1,  10.0, 'AGUARDANDO');

insert into pedido (data_criacao, cliente_id, total, status) values (DATE('2022-02-01'),  1,  10.0, 'AGUARDANDO');

insert into pedido (data_criacao, cliente_id, total, status) values (DATE('2022-02-01'),  1,  10.0, 'AGUARDANDO');

insert into item_pedido (pedido_id, produto_id, quantidade) values ( 1, 1, 2);

insert into categoria ( nome) values ("Eletrônicos");






