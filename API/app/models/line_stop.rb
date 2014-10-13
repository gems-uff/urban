#encoding: utf-8
class LineStop < ActiveRecord::Base
  include Hashable
  include Positionable

  self.rgeo_factory_generator = RGeo::Geos.factory_generator

  set_rgeo_factory_for_column(:position, RGeo::Geographic.spherical_factory(:srid => 4326))

  belongs_to :line
  belongs_to :loaded_file

  delegate :line_number, to: :line, :allow_nil => true

  def self.get_fields
    [:line_number, :latitude, :longitude, :sequence_number, :description, :company, :loaded_file_id]
  end

end
