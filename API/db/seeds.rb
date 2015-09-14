# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
puts 'importando disposal_reasons'
ActiveRecord::Base.connection.execute File.read(File.expand_path('../seeds/disposal_reasons.sql', __FILE__))
puts 'importando lines'
ActiveRecord::Base.connection.execute File.read(File.expand_path('../seeds/lines.sql', __FILE__))
puts 'importando loaded files'
ActiveRecord::Base.connection.execute File.read(File.expand_path('../seeds/loaded_files.sql', __FILE__))
puts 'importando line stops'
ActiveRecord::Base.connection.execute File.read(File.expand_path('../seeds/line_stops.sql', __FILE__))
puts 'importando line positions'
ActiveRecord::Base.connection.execute File.read(File.expand_path('../seeds/line_positions1.sql', __FILE__))
puts '25%'
ActiveRecord::Base.connection.execute File.read(File.expand_path('../seeds/line_positions2.sql', __FILE__))
puts '50%'
ActiveRecord::Base.connection.execute File.read(File.expand_path('../seeds/line_positions3.sql', __FILE__))
puts '75%'
ActiveRecord::Base.connection.execute File.read(File.expand_path('../seeds/line_positions4.sql', __FILE__))
puts '100%'

