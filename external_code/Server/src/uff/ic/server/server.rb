require 'em-websocket'
require 'json'
require_relative 'handler'

class Server
  @@UPDATE_MODULES = 'cp ../../../../../out/artifacts/*/*.jar ../../../../../modules/'

  def initialize
    @handler = Handler.new
    `#{@@UPDATE_MODULES}`
  end

  def connect
    EM.run {
      EM::WebSocket.run(:host => '0.0.0.0', :port => 8080) do |ws|
        ws.onopen { puts 'WebSocket connection open' }

        ws.onclose { puts 'Connection closed' }

        ws.onmessage { |request|
          response = @handler.request(JSON.parse(request))
          ws.send(JSON.generate(response))
        }
      end
    }
  end
end
