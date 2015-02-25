class Handler
  @@REQUEST_TYPES = %w(route stuff)
  @@ANALYSER = '../../../../../modules/Analyser.jar'

  def request(request)
    command = "java -jar #{@@ANALYSER} #{request['request']}"
    if request['request'] === @@REQUEST_TYPES[1]
      command << " #{request['grid_size']} #{request['route']}"
    end
    `#{command}`
  end
end