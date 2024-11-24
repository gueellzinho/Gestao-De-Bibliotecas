create view SisBib.AtrasosLivros
as
select ex.codLivro, l.titulo, le.idLeitor, le.nome, e.idExemplar, e.devolucaoPrevista,
	   DATEDIFF(DAY, e.devolucaoPrevista, GETDATE()) * 5 as Multa	--multa a ser paga
from SisBib.Emprestimo e
inner join SisBib.Exemplar ex
	on  e.idExemplar = ex.idExemplar
inner join SisBib.Livro l
	on ex.codLivro = l.codLivro
inner join SisBib.Leitor le
	on e.idLeitor = le.idLeitor
where e.devolucaoEfetiva is null and e.devolucaoPrevista < GETDATE()