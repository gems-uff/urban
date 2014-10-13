#encoding: utf-8
class Bus < ActiveRecord::Base
  #attr_accessible :bus_number

  validates :bus_number, :uniqueness => true, :presence => true

end
