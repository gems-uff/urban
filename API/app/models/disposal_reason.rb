#encoding: utf-8
class DisposalReason < ActiveRecord::Base
  self.primary_key = "id"

  has_many :disposals

end
