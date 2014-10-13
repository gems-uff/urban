class CreateLoadedFiles < ActiveRecord::Migration
  def up
    create_table :loaded_files do |t|
      t.datetime :start_time, :null => false
      t.datetime :end_time
      t.integer :status
      t.string :errors
      t.integer :type
      t.string :filename
      t.timestamps
    end
    add_index :loaded_files, [:start_time, :filename], :unique => true
  end

  def down
    drop_table :loaded_files
  end
end
