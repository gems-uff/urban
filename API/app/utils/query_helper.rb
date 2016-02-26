class QueryHelper

  def self.append(query, params, table_name = "bus_positions")
    query += " AND EXTRACT(HOUR FROM #{table_name}.time) BETWEEN #{params[:time_begin]} AND #{params[:time_end]}" unless params[:time_begin].blank? || params[:time_end].blank?
    query += " AND EXTRACT(DOW FROM #{table_name}.time) = #{params[:dow]}" unless params[:dow].blank?
    query += " AND #{table_name}.time::timestamp::date = '#{params[:date]}'" unless params[:date].blank?
    query
  end

  def self.union_query(field, line_id)
    "SELECT longitude, latitude, sum(#{field}) as #{field}, seq_num, shape
    FROM
    (SELECT ST_X(ST_AsEWKT(lp.position)) as longitude, ST_Y(ST_AsEWKT(lp.position)) as latitude, -0.001 as #{field}, lp.sequence_number as seq_num, lp.shape_id as shape
    FROM line_positions lp
    WHERE lp.line_id = #{line_id}

    UNION

    "
  end

  def self.avg_speed_query(line_id)
    "SELECT ST_X(ST_AsEWKT(lp.position)) as longitude, ST_Y(ST_AsEWKT(lp.position)) as latitude , coalesce(AVG(bp.speed), 0) as speed, lp.sequence_number as seq_num, lp.shape_id as shape
	from line_positions lp
	right outer join
	bus_positions bp on
	bp.line_id = lp.line_id
	where (lp.line_id = #{line_id})
	and
	ST_DWithin(bp.position, ST_SetSRID(ST_MakePoint(ST_X(ST_AsEWKT(lp.position)), ST_Y(ST_AsEWKT(lp.position))),4326), 15)"
  end

  def self.count_query(line_id)
    "SELECT ST_X(ST_AsEWKT(lp.position)) as longitude, ST_Y(ST_AsEWKT(lp.position)) as latitude , coalesce(count(*), 0) as count, lp.sequence_number as seq_num, lp.shape_id as shape
	from line_positions lp
	right join
	bus_positions bp on
	bp.line_id = lp.line_id
	where (lp.line_id = #{line_id})
	and
	ST_DWithin(bp.position, ST_SetSRID(ST_MakePoint(ST_X(ST_AsEWKT(lp.position)), ST_Y(ST_AsEWKT(lp.position))),4326), 15)"
  end

  #:line_id, :frequency, :time_ago, :tol, :depart_time, :long, :lat, :rad
  def self.radius_time_week(params)
    query =  "SELECT bus_id, time, ST_Y(ST_AsEWKT(position)) as latitude, ST_X(ST_AsEWKT(position)) as longitude "
    query += "FROM bus_positions WHERE line_id = #{params[:line_id]} "
    if(params[:frequency] == "week")
      query += "AND ("
      for i in 1..params[:time_ago]+1
        days_ago = i * 7
        depart_time_minus_tol = ((params[:depart_time].to_datetime - days_ago.days) - params[:tol].minutes).strftime("%Y-%m-%d %H:%M:%S")
        depart_time_plus_tol  = ((params[:depart_time].to_datetime - days_ago.days) + params[:tol].minutes).strftime("%Y-%m-%d %H:%M:%S")
        query += "(time BETWEEN '#{depart_time_minus_tol}' AND '#{depart_time_plus_tol}') OR "
      end

      query += "(1 = 2))"
    end
    query += "AND ST_DWithin(position, 'POINT(#{params[:long]} #{params[:lat]})', #{params[:rad]}) ORDER BY bus_id, time "

  end
end
