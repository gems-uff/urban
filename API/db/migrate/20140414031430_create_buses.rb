class CreateBuses < ActiveRecord::Migration
  def up
    create_table :buses do |t|
      t.string :bus_number, :unique => true, :limit => 10
      t.timestamps
    end
    add_index :buses, [:bus_number], :unique => true
    change_column :buses, :id, :integer, :limit => 2
  end

  def down
    drop_table :buses
  end
end
