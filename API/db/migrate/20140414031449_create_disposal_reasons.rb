class CreateDisposalReasons < ActiveRecord::Migration
  def change
    create_table :disposal_reasons do |t|
      t.string :name, :limit => 50
      t.timestamps
    end
    change_column :disposal_reasons, :id, :integer, :limit => 2
  end
end
