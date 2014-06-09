class Api::V1::DadosRjController < ApplicationController

  respond_to :json, :xml

  #Entrada: número da linha

  #Saída:
  #{
  #linha: XXX,
  #segunda: [vel_1, ..., vel_24 ]
  #terça: [vel_1, ..., vel_24 ]
  #...
  #domingo: [vel_1, ..., vel_24 ]
  #}
  def from_current_week
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER, "line")}) and return unless params[:line]
    #begin
      line = Linha.where(:linha => params[:line]).last

      respond_with({:code => HttpResponse::CODE_LINE_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_LINE_NOT_FOUND)}) and return unless line

      dados =  BuscaDadoRj.media_dia_semana(line)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :line => params[:line]}
      dados.each do |d|
        wday = d.data_hora.strftime('%A').downcase
        resp[wday] = [[],[]] unless resp[wday]
        resp[wday][0] << d.avg_velocidade
	resp[wday][1] << d.count
      end

      respond_with(resp)

    #rescue Exception => e
     # respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
      #              :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    #end
  end

  def line_history
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]
    begin
      line = Linha.where(:linha => params[:line]).last

      respond_with({:code => HttpResponse::CODE_LINE_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_LINE_NOT_FOUND)}) and return unless line

      dados = DadoRj.where(:linha_id => line.id).includes(:ordem).limit(5)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :data => []}
      dados.each do |d|
        resp[:data] << d.to_hash(DadoRj::FIELDS - [:linha_id]) 
      end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  def bus_history
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'bus'}) and return unless params[:bus]
    begin
      bus = Ordem.where(:ordem => params[:bus]).last

      respond_with({:code => HttpResponse::CODE_BUS_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_BUS_NOT_FOUND)}) and return unless bus

      dados = DadoRj.where(:ordem_id => bus.id).includes(:linha).limit(5)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :data => []}
      dados.each do |d|
        resp[:data] << d.to_hash(DadoRj::FIELDS - [:ordem_id])  if resp[:data].size < 5
      end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end


end
