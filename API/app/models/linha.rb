#encoding: utf-8
class Linha < ActiveRecord::Base
  attr_accessible :linha

  validates :linha, :uniqueness => true, :presence => true

end
