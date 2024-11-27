create trigger emprestimo_tg on SisBib.Emprestimo
instead of insert
as
begin
	declare @idExemplar int;

	select @idExemplar = idExemplar from inserted

	if exists(select 1 from SisBib.Emprestimo
			  where idExemplar = @idExemplar
			  and devolucaoEfetiva is null)
		throw 50001, 'Esse exemplar ainda não foi devolvido, não será possível fazer o empréstimo', 1
	else
	begin
		insert into SisBib.Emprestimo(idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista)
		select idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista from inserted
	end
end