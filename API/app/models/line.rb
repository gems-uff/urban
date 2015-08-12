#encoding: utf-8
class Line < ActiveRecord::Base
  self.primary_key = "id"
  #attr_accessible :line_number

  validates :line_number, :uniqueness => true, :presence => true

  has_many :line_stops
  has_many :line_positions

end
