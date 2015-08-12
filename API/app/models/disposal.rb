#encoding: utf-8
class Disposal < ActiveRecord::Base
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
    [:time, :latitude, :longitude, :speed, :bus_number, :line_number, :loaded_file_id, :disposal_reason]
  end

  def self.from_week(line)
    d = DateTime.now
    where(:line_id => line.id).where('time BETWEEN ? AND ?', d.at_beginning_of_week, d).order(:time)
  end

  def self.in_radius(params = {})
    where("ST_DWithin(position, 'POINT(#{params[:long]} #{params[:lat]})', #{params[:rad]})")
  end


end
