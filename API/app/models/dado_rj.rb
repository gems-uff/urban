#encoding: utf-8
class DadoRj < ActiveRecord::Base
  attr_accessible :data_hora, :ordem, :linha, :latitude, :longitude, :velocidade, :ordem_id, :linha_id, :coleta_id

  validates :data_hora, :uniqueness => {:scope => [:ordem_id]}
  validates :coleta_id, :presence => true

  belongs_to :ordem
  belongs_to :linha
  belongs_to :coleta

  FIELDS = [:data_hora, :latitude, :longitude, :velocidade, :ordem_id, :linha_id, :coleta_id]

  def self.media_dia_semana(linha)
    DadoRj.where(:linha_id => linha.id).order(:data_hora).select("avg(velocidade), count(*)").group_by("DAYOFWEEK(data_hora), HOUR(data_hora)")
  end

  def to_hash(fields = [])
    resp = {}
    fields = FIELDS if fields.empty?
if :ordem_id.in?(fields)
    resp[:ordem] = self.ordem.ordem
 fields = fields - [:ordem_id] 
end
if :linha_id.in?(fields)
    resp[:linha] = self.linha.linha
fields = fields - [:linha_id] 
end
    fields.each do |f|
      resp[f] = self.try(f)
    end
    resp
  end
end
