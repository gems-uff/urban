class Api::V1::BusPositionsController < ApplicationController

  respond_to :json, :xml

  #Entrada: número da linha

  #Saída:
  #{
  #line: XXX,
  #monday: [speed_1, ..., speed_24 ]
  #tuesday: [speed_1, ..., speed_24 ]
  #...
  #sunday: [speed_1, ..., speed_24 ]
  #}
  def from_current_week
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]
    begin
      line = Line.where(:line_number => params[:line]).last

      respond_with({:code => HttpResponse::CODE_LINE_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_LINE_NOT_FOUND)}) and return unless line

      data = BusPosition.from_week(line)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :line => params[:line]}
      data.each do |d|
        wday = d.time.strftime('%A')
        resp[wday] = [] unless resp[wday]
        resp[wday] << d.speed
      end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  def line_history
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]
    begin
      line = Line.where(:line_number => params[:line]).last

      respond_with({:code => HttpResponse::CODE_LINE_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_LINE_NOT_FOUND)}) and return unless line

      data = BusPosition.where(:line_id => line.id).includes(:bus)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :data => []}
      data.each do |d|
        resp[:data] << d.to_hash(BusPosition.get_fields - [:line_id])
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
      bus = Bus.where(:bus_number => params[:bus]).last

      respond_with({:code => HttpResponse::CODE_BUS_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_BUS_NOT_FOUND)}) and return unless bus

      data = BusPosition.where(:bus_id => bus.id).includes(:line)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :data => []}
      data.each do |d|
        resp[:data] << d.to_hash(BusPosition.get_fields - [:bus_id])
      end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  def on_radius

    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'lat'}) and return unless params[:lat]
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'long'}) and return unless params[:long]
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'rad'}) and return unless params[:rad]

    begin
      data = BusPosition.in_radius(params)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :data => []}
      data.each do |d|
        resp[:data] << d.to_hash(BusPosition.get_fields)
      end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  def with_line_stops_and_positions
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]
    begin
      line = Line.where(:line_number => params[:line]).last

      respond_with({:code => HttpResponse::CODE_LINE_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_LINE_NOT_FOUND)}) and return unless line


      positions = ActiveRecord::Base.connection.execute("SELECT ST_X(ST_AsEWKT(line_positions.position)) as longitude, ST_Y(ST_AsEWKT(line_positions.position)) as latitude FROM line_positions WHERE line_positions.line_id = #{line.id} ORDER BY line_positions.shape_id ASC, line_positions.sequence_number ASC")

      #LinePosition.where(:line_id => line.id).order([:shape_id,:sequence_number])
      stops = ActiveRecord::Base.connection.execute("SELECT ST_X(ST_AsEWKT(line_stops.position)) as longitude, ST_Y(ST_AsEWKT(line_stops.position)) as latitude FROM line_stops WHERE line_stops.line_id = #{line.id} ORDER BY line_stops.sequence_number ASC")



      #LineStop.where(:line_id => line.id).order(:sequence_number)


      data = ActiveRecord::Base.connection.execute("SELECT ST_X(ST_AsEWKT(bus_positions.position)) as longitude, ST_Y(ST_AsEWKT(bus_positions.position)) as latitude FROM bus_positions WHERE bus_positions.line_id = #{line.id}")

      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :data => data, :positions => positions, :stops => stops}
      #resp[:data] = data
      #
      #positions.each do |d|
      #  resp[:positions] << d.to_hash(LinePosition.get_fields)
      #end
      #stops.each do |d|
      #  resp[:stops] << d.to_hash(LineStop.get_fields)
      #end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  def with_line_stops_positions_and_speed_avg
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]
    begin
      line = Line.where(:line_number => params[:line]).last

      respond_with({:code => HttpResponse::CODE_LINE_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_LINE_NOT_FOUND)}) and return unless line


      positions = LinePosition.where(:line_id => line.id).order([:shape_id,:sequence_number])
      stops = LineStop.where(:line_id => line.id).order(:sequence_number)
      data = BusPosition.where(:line_id => line.id).includes(:bus)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :data => [], :positions => [], :stops => []}
      data.each do |d|
        resp[:data] << d.to_hash(BusPosition.get_fields)
      end
      positions.each do |d|
        resp[:positions] << d.to_hash(LinePosition.get_fields)
      end
      stops.each do |d|
        resp[:stops] << d.to_hash(LineStop.get_fields)
      end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end


end
