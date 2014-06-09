# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20140414031449) do

  create_table "coletas", :force => true do |t|
    t.datetime "data_hora_inicio", :null => false
    t.datetime "data_hora_fim"
    t.integer  "status"
    t.string   "erros"
    t.string   "filename"
    t.datetime "created_at",       :null => false
    t.datetime "updated_at",       :null => false
    t.index ["data_hora_inicio", "filename"], :name => "index_coletas_on_data_hora_inicio_and_filename", :unique => true
  end

  create_table "linhas", :force => true do |t|
    t.string   "linha"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
    t.index ["linha"], :name => "index_linhas_on_linha", :unique => true
  end

  create_table "ordens", :force => true do |t|
    t.string   "ordem"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
    t.index ["ordem"], :name => "index_ordens_on_ordem", :unique => true
  end

  create_table "dados_rj", :force => true do |t|
    t.datetime "data_hora",  :null => false
    t.integer  "ordem_id"
    t.integer  "linha_id"
    t.integer  "coleta_id"
    t.float    "latitude",   :null => false
    t.float    "longitude",  :null => false
    t.float    "velocidade", :null => false
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
    t.index ["coleta_id"], :name => "fk__dados_rj_coleta_id"
    t.index ["data_hora", "ordem_id"], :name => "index_dados_rj_on_data_hora_and_ordem_id", :unique => true
    t.index ["linha_id"], :name => "fk__dados_rj_linha_id"
    t.index ["ordem_id"], :name => "fk__dados_rj_ordem_id"
    t.foreign_key ["coleta_id"], "coletas", ["id"], :on_update => :restrict, :on_delete => :restrict, :name => "fk_dados_rj_coleta_id"
    t.foreign_key ["linha_id"], "linhas", ["id"], :on_update => :restrict, :on_delete => :restrict, :name => "fk_dados_rj_linha_id"
    t.foreign_key ["ordem_id"], "ordens", ["id"], :on_update => :restrict, :on_delete => :restrict, :name => "fk_dados_rj_ordem_id"
  end

end
