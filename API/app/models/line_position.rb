#encoding: utf-8
class LinePosition < ActiveRecord::Base
  include Hashable
  include Positionable

  self.rgeo_factory_generator = RGeo::Geos.factory_generator

  set_rgeo_factory_for_column(:position, RGeo::Geographic.spherical_factory(:srid => 4326))

  belongs_to :line
  belongs_to :loaded_file

  delegate :line_number, to: :line, :allow_nil => true

  def self.get_fields
    [:line_number, :latitude, :longitude, :sequence_number, :description, :company, :loaded_file_id, :shape_id]
  end

  def self.bounding_box(line_id)
    query = "select max(st_ymax(ST_GeomFromWKB(position))) as maxLat, min(st_ymin(ST_GeomFromWKB(position))) as minLat, max(st_xmax(ST_GeomFromWKB(position))) as maxLong,
min(st_xmin(ST_GeomFromWKB(position))) as minLong
 from line_positions lp where lp.line_id = #{line_id}"
    ActiveRecord::Base.connection.execute(query).values
  end

end
