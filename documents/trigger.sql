create trigger emprestimo_tg on SisBib.Emprestimo
instead of insert
as
begin
	declare @idExemplar int, @devolucaoEfetiva date;

	select @idExemplar = idExemplar from inserted

	select @devolucaoEfetiva  = devolucaoEfetiva from SisBib.Emprestimo
	where idExemplar = @idExemplar and devolucaoEfetiva is null

	if(@devolucaoEfetiva is null)
	begin
		insert into SisBib.Emprestimo(idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista)
		select idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista from inserted
	end
	else
		throw 50001, 'Esse exemplar ainda não foi devolvido, não será possível fazer o empréstimo', 1
end
