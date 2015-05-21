class Api::V1::ContatosController < ActionController::Base

  respond_to :json, :xml

  def all
    begin
      contatos = Contato.all

      respond_with(contatos)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  def fetch
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'id'}) and return unless params[:id]
    begin
      contato = Contato.find(params[:id])

      respond_with(contato)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  def insert
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'name'}) and return unless params[:name]
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'telephone'}) and return unless params[:telephone]
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'email'}) and return unless params[:email]
    begin
      contato = Contato.new
      contato.name = params[:name]
      contato.telephone = params[:telephone]
      contato.email = params[:email]

      contato.save!

      respond_with(contato)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end
  def atualiza
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'name'}) and return unless params[:name]
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'id'}) and return unless params[:id]
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'telephone'}) and return unless params[:telephone]
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'email'}) and return unless params[:email]
    begin
      contato = Contato.find(params[:id])
      contato.update_attributes(name: params[:name], telephone: params[:telephone], email: params[:email])

      respond_with(contato)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

end
