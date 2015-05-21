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

      data = ActiveRecord::Base.connection.select_all("SELECT ST_X(ST_AsEWKT(bp.position)) as longitude, ST_Y(ST_AsEWKT(bp.position)) as latitude, bp.speed, b.bus_number FROM bus_positions bp join buses b on b.id = bp.bus_id WHERE bp.line_id = #{line.id}")

      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :columns => data.columns, :data => data.rows}

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

      data = ActiveRecord::Base.connection.select_all("SELECT ST_X(ST_AsEWKT(bp.position)) as longitude, ST_Y(ST_AsEWKT(bp.position)) as latitude, bp.speed, l.line_number FROM bus_positions bp join lines l on l.id = bp.line_id WHERE bp.bus_id = #{bus.id}")

      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :columns => data.columns, :data => data.rows}

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
      data = ActiveRecord::Base.connection.select_all("SELECT ST_X(ST_AsEWKT(bp.position)) as longitude, ST_Y(ST_AsEWKT(bp.position)) as latitude, bp.speed, b.bus_number FROM bus_positions bp join lines l on l.id = bp.line_id inner join buses b on b.id = bp.bus_id WHERE ST_DWithin(bp.position, 'POINT(#{params[:long]} #{params[:lat]})', #{params[:rad]})")

      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :columns => data.columns, :data => data.rows}

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

      positions = ActiveRecord::Base.connection.select_all("SELECT ST_X(ST_AsEWKT(line_positions.position)) as longitude, ST_Y(ST_AsEWKT(line_positions.position)) as latitude FROM line_positions WHERE line_positions.line_id = #{line.id} ORDER BY line_positions.shape_id ASC, line_positions.sequence_number ASC")

      stops = ActiveRecord::Base.connection.select_all("SELECT ST_X(ST_AsEWKT(line_stops.position)) as longitude, ST_Y(ST_AsEWKT(line_stops.position)) as latitude FROM line_stops WHERE line_stops.line_id = #{line.id} ORDER BY line_stops.sequence_number ASC")


      if (params[:diff])
        query = QueryHelper.union_query("count", line.id)
        query += QueryHelper.count_query(line.id)
        query = QueryHelper.append(query, params, "bp")
        query += " group by lp.position, lp.sequence_number, lp.shape_id"
        query += ") as q
 group by seq_num, shape, longitude, latitude
 order by shape, seq_num"

        query2 = QueryHelper.union_query("count", line.id)
        query2 += QueryHelper.count_query(line.id)
        query2 = QueryHelper.append(query2, params[:diff], "bp")
        query2 += " group by lp.position, lp.sequence_number, lp.shape_id"
        query2 += ") as q
 group by seq_num, shape, longitude, latitude
 order by shape, seq_num"

        query = "SELECT q1.longitude, q1.latitude, (q2.count - q1.count) as count, q1.seq_num, q1.shape from (#{query}) q1 INNER JOIN (#{query2}) q2 on q1.shape = q2.shape and q2.seq_num = q1.seq_num "
      else
        query = QueryHelper.append("SELECT ST_X(ST_AsEWKT(bus_positions.position)) as longitude, ST_Y(ST_AsEWKT(bus_positions.position)) as latitude FROM bus_positions WHERE bus_positions.line_id = #{line.id}", params)
      end
      data = ActiveRecord::Base.connection.select_all(query)

      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :columns => data.columns, :data => data.rows, :positions => positions, :stops => stops}

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

      positions = ActiveRecord::Base.connection.select_all("SELECT ST_X(ST_AsEWKT(line_positions.position)) as longitude, ST_Y(ST_AsEWKT(line_positions.position)) as latitude FROM line_positions WHERE line_positions.line_id = #{line.id} ORDER BY line_positions.shape_id ASC, line_positions.sequence_number ASC") || []

      stops = ActiveRecord::Base.connection.select_all("SELECT ST_X(ST_AsEWKT(line_stops.position)) as longitude, ST_Y(ST_AsEWKT(line_stops.position)) as latitude FROM line_stops WHERE line_stops.line_id = #{line.id} ORDER BY line_stops.sequence_number ASC") || []

      query = QueryHelper.union_query("speed", line.id)
      query += QueryHelper.avg_speed_query(line.id)
      query = QueryHelper.append(query, params, "bp")
      query += " group by lp.position, lp.sequence_number, lp.shape_id"
      query += ") as q
 group by seq_num, shape, longitude, latitude
 order by shape, seq_num"

      if (params[:diff])
        query2 = QueryHelper.union_query("speed", line.id)
        query2 += QueryHelper.avg_speed_query(line.id)
        query2 = QueryHelper.append(query2, params[:diff], "bp")
        query2 += " group by lp.position, lp.sequence_number, lp.shape_id"
        query2 += ") as q
 group by seq_num, shape, longitude, latitude
 order by shape, seq_num"

        query = "SELECT q1.longitude, q1.latitude, (q2.speed - q1.speed) as speed, q1.seq_num, q1.shape from (#{query}) q1 INNER JOIN (#{query2}) q2 on q1.shape = q2.shape and q2.seq_num = q1.seq_num "
      end

      data = ActiveRecord::Base.connection.select_all(query) || []

      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :columns => data.columns, :data => data.rows, :stops => stops}

      File.open("#{params[:line]}-#{params[:date]}.json","w") do |f|
        f.write(resp.to_json)
      end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end


end
