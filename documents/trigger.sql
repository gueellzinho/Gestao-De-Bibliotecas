create trigger SisBib.emprestimo_tg on SisBib.Emprestimo
instead of insert
as
declare @idExemplar int, @devolucao DateTime

select @idExemplar = idExemplar from inserted
if exists(select * from SisBib.Emprestimo where idExemplar = @idExemplar)
begin
	select @devolucao = devolucaoEfetiva from SisBib.Emprestimo where idExemplar = @idExemplar
	if @devolucao is null
	begin
		print 'Esse exemplar ainda n�o foi devolvido, n�o ser� poss�vel fazer o empr�stimo'
		rollback transaction
	end
	else
		print 'Empr�stimo conclu�do'
end
