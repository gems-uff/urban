#encoding: utf-8
class Coleta < ActiveRecord::Base
  attr_accessible :data_hora_inicio, :data_hora_fim, :status, :erros, :filename

  STATUS_INICIADA = 1
  STATUS_ENCERRADA_COM_SUCESSO = 2
  STATUS_ENCERRADA_COM_ERROS = 3

  STATUS = [STATUS_INICIADA, STATUS_ENCERRADA_COM_SUCESSO, STATUS_ENCERRADA_COM_ERROS]

  has_many :dados_rj

  validates :data_hora_inicio, :uniqueness => true, :presence => true
  validates :status, :presence => true, :inclusion => {:in => STATUS}
  validates :erros, :presence => true, :if => Proc.new {|c| c.status == STATUS_ENCERRADA_COM_ERROS}

end
