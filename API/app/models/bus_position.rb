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

  def self.from_week(line)
    d = DateTime.now
    BusPosition.where(:line_id => line.id).where('time BETWEEN ? AND ?', d.at_beginning_of_week, d).order(:time)
  end

  def self.in_radius(params = {})
    BusPosition.where("ST_DWithin(position, 'POINT(#{params[:long]} #{params[:lat]})', #{params[:rad]})")
  end

  #Params:long, lat, rad, time_begin, time_end, Line
  def self.in_radius_time_line(long, lat, rad, time_begin, time_end, line)
    BusPosition.where(:line_id => line.id).
      where('time BETWEEN ? AND ?',time_begin, time_end).
      where("ST_DWithin(position, 'POINT(#{long} #{lat})', #{rad})")
  end

  def self.track_bus_id_in_time(bus_id,time_begin, time_end )
    BusPosition.where(:bus_id => bus_id).
      where('time BETWEEN ? AND ?',time_begin, time_end).
      order(:time)
  end


end
