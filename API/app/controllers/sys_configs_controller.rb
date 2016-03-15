class SysConfigsController < ApplicationController
  before_action :set_sys_config, only: [:show, :edit, :update, :destroy]

  # GET /sys_configs/1/edit
  def edit
  end

  # PATCH/PUT /sys_configs/1
  # PATCH/PUT /sys_configs/1.json
  def update
    respond_to do |format|
      if @sys_config.update(sys_config_params)
        format.html { redirect_to root_path, notice: 'Configuration was successfully updated.' }
      else
        format.html { render :edit }
      end
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_sys_config
      @sys_config = SysConfig.config
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def sys_config_params
      params.require(:sys_config).permit(:positions_hm_range_1, :positions_hm_range_2, :positions_hm_range_3, :positions_hm_range_4,
                :speed_hm_diff_range_1, :speed_hm_diff_range_2, :speed_hm_diff_range_3, :speed_hm_diff_range_4,
                :speed_hm_query_range_1, :speed_hm_query_range_2, :speed_hm_query_range_3, :speed_hm_query_range_4,
                :bounding_box_gap, :search_radius)
    end
end
