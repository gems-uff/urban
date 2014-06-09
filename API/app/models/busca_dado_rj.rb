#encoding: utf-8
class BuscaDadoRj
  attr_accessor :avg_velocidade, :count, :data_hora

  def self.media_dia_semana(linha)
    query = ActiveRecord::Base.connection.execute(
            "SELECT avg(velocidade), count(*), data_hora FROM `dados_rj` GROUP BY DAYOFWEEK(data_hora), HOUR(data_hora) ORDER BY DAYOFWEEK(data_hora), HOUR(data_hora)")
    result = []

    query.each_with_index do |rs, i|
	d = BuscaDadoRj.new
	d.avg_velocidade = rs[0]
	d.count = rs[1]
	puts "rs #{i}: #{rs.inspect}"
	d.data_hora = rs[2].to_datetime   	
	result << d
    end
    result
    
  end

end
