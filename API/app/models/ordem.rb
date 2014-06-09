#encoding: utf-8
class Ordem < ActiveRecord::Base
  attr_accessible :ordem

  validates :ordem, :uniqueness => true, :presence => true

end
