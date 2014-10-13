#encoding: utf-8
class LoadedFile < ActiveRecord::Base
  #attr_accessible :start_time, :end_time, :status, :errors

  STATUS_STARTED = 1
  STATUS_FINISHED_SUCCESSFULLY = 2
  STATUS_FINISHED_WITH_ERRORS = 3

  STATUS = [STATUS_STARTED, STATUS_FINISHED_SUCCESSFULLY, STATUS_FINISHED_WITH_ERRORS]

  has_many :bus_position

  validates :start_time, :uniqueness => true, :presence => true
  validates :status, :presence => true, :inclusion => {:in => STATUS}
  validates :errors, :presence => true, :if => Proc.new {|c| c.status == STATUS_FINISHED_WITH_ERRORS}

end
