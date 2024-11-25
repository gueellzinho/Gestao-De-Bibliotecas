create trigger SisBib.emprestimo_tg on SisBib.Emprestimo
instead of insert
as
declare @idExemplar int, @devolucao DateTime

select @idExemplar = idExemplar from inserted
if exists(select devolucaoEfetiva from SisBib.Emprestimo where idExemplar = @idExemplar)
begin
	select @devolucao = devolucaoEfetiva from SisBib.Emprestimo where idExemplar = @idExemplar
	if @devolucao is null
	begin
		print 'Esse exemplar ainda não foi devolvido, não será possível fazer o empréstimo'
		rollback transaction
	end
	else
	begin
		insert into SisBib.Emprestimo (idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista)
		select idLeitor, idExemplar, dataEmprestimo, devolucaoPrevista from inserted
		print 'Empréstimo concluído'
	end
end
