class CreateLinePositions < ActiveRecord::Migration
  def up
    create_table :line_positions do |t|
      t.integer :sequence_number
      t.integer :line_id, :references => Line
      t.string :description
      t.string :company
      t.integer :loaded_file_id, :references => LoadedFile
      t.point :position, :geographic => true
      t.integer :shape_id, :references => nil
      t.timestamps
    end
    add_index :line_positions, :position
    add_index :line_positions, :line_id
  end

  def down
    drop_table :line_positions
  end
end
