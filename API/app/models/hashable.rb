#encoding: utf-8
module Hashable
  def self.get_fields
    []
  end

  def to_hash(fields = [])
    resp = {}
    fields.each do |f|
      resp[f] = self.try(f)
    end
    resp
  end
end
