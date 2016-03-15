#encoding: utf-8
class BusPosition < ActiveRecord::Base
  include Hashable
  include Positionable

  self.rgeo_factory_generator = RGeo::Geos.factory_generator

  set_rgeo_factory_for_column(:position, RGeo::Geographic.spherical_factory(:srid => 4326))

  validates :time, :uniqueness => {:scope => [:bus_id]}
  validates :loaded_file_id, :presence => true

  belongs_to :bus
  belongs_to :line
  belongs_to :loaded_file

  delegate :bus_number, to: :bus
  delegate :line_number, to: :line, :allow_nil => true

  def self.get_fields
    [:time, :latitude, :longitude, :speed, :bus_number, :line_number, :loaded_file_id]
  end

  def self.statistics(line)
    BusPosition.where(:line_id => line.id).
      order("EXTRACT(DOW FROM bus_positions.time), EXTRACT(HOUR FROM bus_positions.time)").
      select('EXTRACT(DOW FROM bus_positions.time) as dow, EXTRACT(HOUR FROM bus_positions.time) as time, count(*) as count, avg(speed) as speed').
      group('EXTRACT(DOW FROM bus_positions.time), EXTRACT(HOUR FROM bus_positions.time)')
  end

  def self.in_radius(params = {})
    BusPosition.where("ST_DWithin(position, 'POINT(#{params[:long]} #{params[:lat]})', #{params[:rad]})")
  end


end
