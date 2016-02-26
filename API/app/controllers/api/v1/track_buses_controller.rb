#encoding: utf-8
class Api::V1::TrackBusesController < ApplicationController
  respond_to :json, :xml

  def track
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'bus_id'}) and return unless params[:bus_id]
    respond_with({:code => HttpResponse::COD_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'from_time'}) and return unless params[:from_time]
    respond_with({:code => HttpResponse::COD_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'until_time'}) and return unless params[:until_time]

    begin

      p "=== Log === Searching bus with bus number"
      query = "SELECT ST_X(ST_AsEWKT(bus_positions.position)) as longitude, ST_Y(ST_AsEWKT(bus_positions.position)) as latitude, time FROM bus_positions JOIN buses ON bus_positions.bus_id = buses.id WHERE bus_number = '#{params[:bus_id]}' AND (time BETWEEN '#{params[:from_time]}' AND '#{params[:until_time]}') ORDER BY time"

      positions = ActiveRecord::Base.connection.select_all(query) || []

      #If doesn't find any result with bus number, and is a number, try with ID
      if(positions.rows.size <= 0 && /\A\d+\z/.match(params[:bus_id]))
        p "=== Log === Searching with bus ID"
        query = "SELECT ST_X(ST_AsEWKT(bus_positions.position)) as longitude, ST_Y(ST_AsEWKT(bus_positions.position)) as latitude, time FROM bus_positions WHERE bus_id = #{params[:bus_id]} AND (time BETWEEN '#{params[:from_time]}' AND '#{params[:until_time]}') ORDER BY time"
        positions = ActiveRecord::Base.connection.select_all(query) || []
      end

      p "=== Log === Found with #{positions.rows.size} results"

      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :positions => positions}
      respond_with(resp)

    rescue Exception => e
      p "=== Log Error === #{e.message}"
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  #line: line, lat_stop_orig: lat_stop_orig, long_stop_orig: long_stop_orig, lat_stop_dest: lat_stop_dest,
  #long_stop_dest: long_stop_dest, lat_next_stop_orig: lat_next_stop_orig, long_next_stop_orig: long_next_stop_orig,
  #depart_time: depart_time, #confidence: confidence, format: 'json' })

  #Make the prediciton using the origin values, looking for the destination
  def time_prediction_o_to_d

    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'depart_time'}) and return unless params[:depart_time]
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]

    begin
      line = Line.where(:line_number => params[:line]).last
      inf = "line #{params[:line]} id #{line.id}"
      p "=== Log === line #{params[:line]}"

      #:frequency, :time_ago, :tol, :depart_time, :long, :lat, :rad
      params_aux = {:frequency => "week",
                    :time_ago => 3,
                    :tol => 15,
                    :depart_time => params[:depart_time],
                    :long => params[:long_stop_orig],
                    :lat => params[:lat_stop_orig],
                    :rad => 700,
                    :line_id => line.id }

      query = QueryHelper.radius_time_week(params_aux)
      p "=== Log === Executing the query looking for buses in the origin "

      positions_in_range_orig = ActiveRecord::Base.connection.select_all(query).rows || []
      p "=== Log === returned with #{positions_in_range_orig.size} results"

      bus_id_checked = Array.new
      samples = Array.new
      i = 0

      while i < positions_in_range_orig.size

        pr = positions_in_range_orig[i] #PR[0]->ID  PR[1]->TIME  PR[2]->LAT  PR[3]->LONG

        p "=== Log === bus id #{pr[0]} #{Time.at(pr[1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}"
        unless (bus_id_checked.include? "#{pr[0]}#{Time.at(pr[1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}")
          bus_id_checked.push("#{pr[0]}#{Time.at(pr[1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}")

          #First, check if has another result with the same bus in the same day
          #Check if is going in the direction of his next stop comparing the distance
          #between the actual dot and the next one to the next bus stop
          directionOk = false
          if ((i != positions_in_range_orig.size-1 ) &&
              (bus_id_checked.include? "#{positions_in_range_orig[i+1][0]}#{Time.at(positions_in_range_orig[i+1][1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}"))
               dist_orig = distance [pr[2].to_f, pr[3].to_f],[params[:lat_next_stop_orig].to_f, params[:long_next_stop_orig].to_f]
               dist_orig_prox = distance [positions_in_range_orig[i+1][2].to_f, positions_in_range_orig[i+1][3].to_f],[params[:lat_next_stop_orig].to_f, params[:long_next_stop_orig].to_f]

               if(dist_orig > dist_orig_prox)
                 directionOk = true
                 p "=== Log === Direction Ok!"
                 inf += " id #{pr[0]} Direction OK! "
               else
                 p "=== Log === Wrong Direction"
                 inf += " id #{pr[0]} Direction NOK tempoOrig: #{pr[1]}"
               end
          else
              inf += " id #{pr[0]} Direction undefined "
          end

          if(directionOk)
            #Try to obtain the closest position in the origin, checking
            #One by one if the distance to the bus stop decrease
            while
              #Is not the end of the list
              (i != positions_in_range_orig.size-1 ) &&
              #And Still the same bus
              (bus_id_checked.include? "#{positions_in_range_orig[i+1][0]}#{Time.at(positions_in_range_orig[i+1][1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}") &&
              # And The distance of the next position is closer to the bus stop
              (distance([positions_in_range_orig[i+1][2].to_f, positions_in_range_orig[i+1][3].to_f], [params[:lat_stop_orig].to_f,params[:long_stop_orig].to_f]) > distance([pr[2].to_f, pr[3].to_f], [params[:lat_stop_orig].to_f,params[:long_stop_orig].to_f]))
            #DO
              i = i + 1
              pr = positions_in_range_orig[i]
              p "=== Log === Change origin to: bus id #{pr[0]} #{Time.at(pr[1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}"
            end

            positions_in_range_dest = BusPosition.where(:bus_id => pr[0]).
              where("ST_DWithin(position, 'POINT(#{params[:long_stop_dest]} #{params[:lat_stop_dest]})', #{params_aux[:rad]})").
              where("time > ?",pr[1]).
              order("time").
              first

            inf += "tempoOrig: #{pr[1]} "
            p "=== Log === Check if exists positions in destination"
            if(positions_in_range_dest)
              p "=== Log === Exists! orig #{pr[1].to_datetime} tempoDest: #{positions_in_range_dest.time}"

              aux = (positions_in_range_dest.time - pr[1].to_datetime).to_i
              #If is an position with less than one day, push in the sample array
              if(aux < 24*60*60)
                samples.push(aux)
                inf += " tempoDest: #{positions_in_range_dest.time} "
                aux = Time.at(aux).utc.strftime("%H:%M:%S")
                p "=== Log === time: #{aux}"
                inf += " |#{aux}|"
              end

            end
          end
        end
        i = i + 1
      end

      if(samples.size <= 1 )
        resp_text = "Sorry, not enough positions to make a prediction"
        p "=== Log === Doesn't find enough positions to make an prediction (#{samples.size})"
      else
        samples = Prediction.remove_outliers(samples)

        interval = Prediction.confidence_interval(samples,params[:confidence].to_i)
        li = Time.at(params[:depart_time].to_datetime.to_i + interval[:li]).utc.strftime("%H:%M:%S")
        ls = Time.at(params[:depart_time].to_datetime.to_i + interval[:ls]).utc.strftime("%H:%M:%S")
        p "=== Log === Avg: #{Time.at(Prediction.AVG(samples)).utc.strftime("%H:%M:%S")} | from #{interval[:li]} to #{interval[:ls]}"
        resp_text = "Arrival time from #{li} to #{ls}"
      end

      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :time => resp_text, :information =>inf}
      respond_with(resp)

    rescue Exception => e
      p "=== LOG === ERRO: #{e.message}"
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end


    #line: line, lat_stop_orig: lat_stop_orig, long_stop_orig: long_stop_orig, lat_stop_dest: lat_stop_dest,
    #long_stop_dest: long_stop_dest, lat_next_stop_dest: lat_next_stop_dest, long_next_stop_dest: long_next_stop_dest,
    #depart_time: depart_time, format: 'json' })

    def time_prediction_d_to_o

      respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'depart_time'}) and return unless params[:depart_time]
      respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]

      begin
        line = Line.where(:line_number => params[:line]).last
        inf = "line #{params[:line]} id #{line.id}"
        p "=== Log === line #{params[:line]}"

        params_aux = {:frequency => "week",
                      :time_ago => 3,
                      :tol => 15,
                      :depart_time => params[:depart_time],
                      :long => params[:long_stop_dest],
                      :lat => params[:lat_stop_dest],
                      :rad => 700,
                      :line_id => line.id }

        query = QueryHelper.radius_time_week(params_aux)
        p "=== Log === Executing the query looking for buses in the origin "

        positions_in_range_dest = ActiveRecord::Base.connection.select_all(query).rows || []
        p "=== Log === returned with #{positions_in_range_dest.size} results"

        bus_id_checked = Array.new
        samples = Array.new
        i = 0

        while i < positions_in_range_dest.size

          pr = positions_in_range_dest[i] #PR[0]->ID  PR[1]->TIME  PR[2]->LAT  PR[3]->LONG

          p "=== Log === onibus id #{pr[0]} #{Time.at(pr[1].to_datetime.to_i).utc.strftime("%Y-%m-%d %H:%M:%S")}"
          unless (bus_id_checked.include? "#{pr[0]}#{Time.at(pr[1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}")
            bus_id_checked.push("#{pr[0]}#{Time.at(pr[1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}")

            #First, check if has another result with the same bus in the same day
            #Check if is going in the direction of his next stop comparing the distance
            #between the actual dot and the next one to the next bus stop
            directionOk = false
            if ((i != positions_in_range_dest.size-1 ) &&
                (bus_id_checked.include? "#{positions_in_range_dest[i+1][0]}#{Time.at(positions_in_range_dest[i+1][1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}"))
                 dist_dest = distance [pr[2].to_f, pr[3].to_f],[params[:lat_next_stop_dest].to_f, params[:long_next_stop_dest].to_f]
                 dist_dest_prox = distance [positions_in_range_dest[i+1][2].to_f, positions_in_range_dest[i+1][3].to_f],[params[:lat_next_stop_dest].to_f, params[:long_next_stop_dest].to_f]

                 if(dist_dest < dist_dest_prox)
                   directionOk = true
                   p "=== Log === Ok! Right direction"
                   inf += " id #{pr[0]} Direcao OK! "
                 else
                   p "=== Log === Wrong direction"
                   inf += " id #{pr[0]} Direction NOK tempoDest: #{pr[1]}"
                 end
            else
                inf += " id #{pr[0]} Direction undefinied "
            end

            if(directionOk)
              #Try to obtain the closest position in the destination, checking
              #One by one if the distance to the bus stop decrease
              while
                #Is not the end of the list
                (i != positions_in_range_dest.size-1 ) &&
                #And Still the same bus
                (bus_id_checked.include? "#{positions_in_range_dest[i+1][0]}#{Time.at(positions_in_range_dest[i+1][1].to_datetime.to_i).utc.strftime("%Y-%m-%d")}") &&
                # And The distance of the next position is closer to the bus stop
                (distance([positions_in_range_dest[i+1][2].to_f, positions_in_range_dest[i+1][3].to_f], [params[:lat_stop_dest].to_f,params[:long_stop_dest].to_f]) < distance([pr[2].to_f, pr[3].to_f], [params[:lat_stop_dest].to_f,params[:long_stop_dest].to_f]))
              #DO
                i = i + 1
                pr = positions_in_range_dest[i]
                p "=== Log === Change destination to: bus id #{pr[0]} #{Time.at(pr[1].to_datetime.to_i).utc.strftime("%Y-%m-%d %H:%M:%S")}"
              end

              positions_in_range_orig = BusPosition.where(:bus_id => pr[0]).
                where("ST_DWithin(position, 'POINT(#{params[:long_stop_orig]} #{params[:lat_stop_orig]})', #{params_aux[:rad]})").
                where("time < ?",pr[1]).
                order("time").
                last

              inf += "tempoDest: #{pr[1]} "
              p "=== Log === Check if exists positions in origin"
              if(positions_in_range_orig)
                p "=== Log === Exists! dest #{pr[1].to_datetime} tempoOrig: #{positions_in_range_orig.time.to_datetime}"

                aux = (positions_in_range_orig.time - pr[1].to_datetime ).to_i
                aux = aux * (0-1)

                if(aux < 24*60*60)
                  samples.push(aux)

                  inf += " tempoOrig: #{positions_in_range_orig.time} "
                  aux = Time.at(aux).utc.strftime("%H:%M:%S")
                  p "=== Log === tempo: #{aux}"
                  inf += " |#{aux}|"
                end
              end
            end
          end
          i = i + 1
        end

        if(samples.size <= 1 )
          resp_text = "Sorry, not enough positions to make a prediction"
          p "=== Log === Doesn't find enough positions to make an prediction (#{samples.size})"
        else
          samples = Prediction.remove_outliers(samples)

          interval = Prediction.confidence_interval(samples,params[:confidence].to_i)
          li = Time.at(params[:depart_time].to_datetime.to_i - interval[:li]).utc.strftime("%H:%M:%S")
          ls = Time.at(params[:depart_time].to_datetime.to_i - interval[:ls]).utc.strftime("%H:%M:%S")
          resp_text = "Depart time from #{li} to #{ls}"
          p "=== Log === Avg: #{Time.at(Prediction.AVG(samples)).utc.strftime("%H:%M:%S")} | from #{interval[:li]} to #{interval[:ls]}"
        end

        resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :time => resp_text, :information =>inf}
        respond_with(resp)

      rescue Exception => e
        p "=== LOG === ERROR: #{e.message}"
        respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                      :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
      end
    end


  #Code by http://stackoverflow.com/questions/12966638/how-to-calculate-the-distance-between-two-gps-coordinates-without-using-google-m
  def distance loc1, loc2
    rad_per_deg = Math::PI/180  # PI / 180
    rkm = 6371                  # Earth radius in kilometers
    rm = rkm * 1000             # Radius in meters

    dlat_rad = (loc2[0]-loc1[0]) * rad_per_deg  # Delta, converted to rad
    dlon_rad = (loc2[1]-loc1[1]) * rad_per_deg

    lat1_rad, lon1_rad = loc1.map {|i| i * rad_per_deg }
    lat2_rad, lon2_rad = loc2.map {|i| i * rad_per_deg }

    a = Math.sin(dlat_rad/2)**2 + Math.cos(lat1_rad) * Math.cos(lat2_rad) * Math.sin(dlon_rad/2)**2
    c = 2 * Math::atan2(Math::sqrt(a), Math::sqrt(1-a))

    rm * c # Delta in meters
  end

end
