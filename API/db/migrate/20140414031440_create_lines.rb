class CreateLines < ActiveRecord::Migration
  def up
    create_table :lines do |t|
      t.string :line_number, :unique => true, :limit => 10
      t.timestamps
    end
    add_index :lines, [:line_number], :unique => true
    change_column :lines, :id, :integer, :limit => 2
  end

  def down
    drop_table :lines
  end
end
