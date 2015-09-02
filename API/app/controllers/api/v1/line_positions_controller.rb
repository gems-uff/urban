class Api::V1::LinePositionsController < ApplicationController

  respond_to :json, :xml

  def from_line
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]
    begin
      line = Line.where(:line_number => params[:line]).last

      respond_with({:code => HttpResponse::CODE_LINE_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_LINE_NOT_FOUND)}) and return unless line

      data = LinePosition.where(:line_id => line.id).order([:shape_id,:sequence_number])
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :data => []}
      data.each do |d|
        resp[:data] << d.to_hash(LinePosition.get_fields)
      end

      respond_with(resp)

    rescue Exception => e
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end

  def with_stops
    respond_with({:code => HttpResponse::CODE_ERROR_MISSING_PARAMETER, :message => HttpResponse.code_msg(HttpResponse::CODE_ERROR_MISSING_PARAMETER) + 'line'}) and return unless params[:line]
    begin
      line = Line.where(:line_number => params[:line]).last

      respond_with({:code => HttpResponse::CODE_LINE_NOT_FOUND, :message => HttpResponse.code_msg(HttpResponse::CODE_LINE_NOT_FOUND)}) and return unless line

      positions = LinePosition.where(:line_id => line.id).order([:shape_id,:sequence_number])
      bounding_box = LinePosition.bounding_box(line.id).first
      stops = LineStop.where(:line_id => line.id).order(:sequence_number)
      resp = {:code => HttpResponse::CODE_SUCCESS, :message => HttpResponse.code_msg(HttpResponse::CODE_SUCCESS), :positions => [], :stops => [], :bounding_box => {}}
      positions.each do |d|
        resp[:positions] << d.to_hash(LinePosition.get_fields)
      end
      stops.each do |d|
        resp[:stops] << d.to_hash(LineStop.get_fields)
      end
      gap = 0.001
      resp[:bounding_box] = {:max_lat => bounding_box[0].to_f + gap, :min_lat => bounding_box[1].to_f - gap,:max_long => bounding_box[2].to_f + gap , :min_long => bounding_box[3].to_f - gap}

      respond_with(resp)

    rescue Exception => e
      puts(e.message)
      respond_with({:code => HttpResponse::CODE_UNKNOWN_ERROR,
                    :message => HttpResponse.code_msg(HttpResponse::CODE_UNKNOWN_ERROR) + e.message})
    end
  end


end
