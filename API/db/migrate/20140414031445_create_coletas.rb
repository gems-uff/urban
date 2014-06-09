class CreateColetas < ActiveRecord::Migration
  def up
    create_table :coletas do |t|
      t.datetime :data_hora_inicio, :null => false
      t.datetime :data_hora_fim
      t.integer :status
      t.string :erros
      t.string :filename
      t.timestamps
    end
    add_index :coletas, [:data_hora_inicio, :filename], :unique => true
  end

  def down
    drop_table :coletas
  end
end
