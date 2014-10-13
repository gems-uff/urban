class CreateBuses < ActiveRecord::Migration
  def up
    create_table :buses do |t|
      t.string :bus_number, :unique => true
      t.timestamps
    end
    add_index :buses, [:bus_number], :unique => true
  end

  def down
    drop_table :buses
  end
end
