class CreateDadosRj < ActiveRecord::Migration
  def up
    create_table :dados_rj do |t|
      t.datetime :data_hora, :null => false
      t.integer :ordem_id, :references => Ordem
      t.integer :linha_id, :references => Linha
      t.integer :coleta_id, :references => Coleta
      t.float :latitude, :null => false
      t.float :longitude, :null => false
      t.float :velocidade, :null => false
      t.timestamps
    end
    add_index :dados_rj, [:data_hora, :ordem_id], :unique => true
  end

  def down
    drop_table :dados_rj
  end
end
