class HttpResponse

  CODE_SUCCESS = 0
  CODE_ERROR_MISSING_PARAMETER = 1
  CODE_UNKNOWN_ERROR = 2
  CODE_LINE_NOT_FOUND = 3
  CODE_BUS_NOT_FOUND = 4

  def self.code_msg(code, message = nil)
    case(code)
      when CODE_SUCCESS
        I18n.t('messages.errors.http_response.success')
      when CODE_ERROR_MISSING_PARAMETER
        I18n.t('messages.errors.http_response.error_mis_param', {:params => message})
      when CODE_UNKNOWN_ERROR
        I18n.t('messages.errors.http_response.unknow_error', {:error => message})
      when CODE_LINE_NOT_FOUND
        I18n.t('messages.errors.http_response.line_not_found')
      when CODE_BUS_NOT_FOUND
        I18n.t('messages.errors.http_response.bus_not_found')
    end
  end

end
