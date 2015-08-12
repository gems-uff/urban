#encoding: utf-8
class LineBoundingBox

  attr_accessor :max_lat, :max_long, :min_lat, :min_long

  GAP = 0.001

  def initialize(params)

    query = "select max(st_ymax(ST_GeomFromEWKB(position))) as maxLat, min(st_ymin(ST_GeomFromEWKB(position))) as minLat, max(st_xmax(ST_GeomFromEWKB(position))) as maxLong,
min(st_xmin(ST_GeomFromEWKB(position))) as minLong
 from line_positions lp where lp.line_id = #{params[:line_id]}"
    values = ActiveRecord::Base.connection.execute(query).values
    bounding_box = values.first

    @max_lat = bounding_box[0].to_f + GAP
    @min_lat = bounding_box[1].to_f - GAP
    @max_long = bounding_box[2].to_f + GAP
    @min_long = bounding_box[3].to_f - GAP
  end

  def to_hash
    {:max_lat => @max_lat, :min_lat => @min_lat,:max_long => @max_long , :min_long => @min_long}
  end
end
