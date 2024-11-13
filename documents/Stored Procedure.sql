alter proc attSituacao_sp
@idLeitor int = null,
@estaSuspenso char(1) = null
as
	if(@idLeitor is null) or (@estaSuspenso is null)
		print 'Digite todos os campos'
	else
	begin
		if exists(select * from SisBib.Leitor where idLeitor = @idLeitor)
		begin
			if(select estaSuspenso from SisBib.Leitor where idLeitor = @idLeitor) = @estaSuspenso
				print 'A situação nova não pode ser igual a antiga'
			else
			begin
				set @estaSuspenso = upper(@estaSuspenso)
				update SisBib.Leitor
				set estaSuspenso = @estaSuspenso
				where idLeitor = @idLeitor	
			end
		end
	end