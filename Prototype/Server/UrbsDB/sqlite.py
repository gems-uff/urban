import csv
import json
import sqlite3 as mySqlite

def creteTable():
   
    myConn = mySqlite.connect('hubway.db')
        
    with myConn: 
        
        myFile = open('stations.csv')
        with myFile:
            
            data = csv.reader(myFile)
            header = next(data)
            print(header)
                    
            cur = myConn.cursor()    
            cur.execute("DROP TABLE Stations" )
            cur.execute("CREATE TABLE IF NOT EXISTS Stations(id INT, terminalName TEXT, name TEXT, installed TEXT, locked TEXT, temporary TEXT, lat REAL, lng REAL)" )
    
            for row in data:
                cur.execute("INSERT INTO Stations VALUES(?,?,?,?,?,?,?,?)", row)
                print(row)
            
            cur.close()
    
    myConn.close()
                
def queryJson():
    
    myConn = mySqlite.connect('hubway.db')
    with myConn: 

        cur = myConn.cursor()    
        cur.execute("SELECT lat, lng FROM Stations")
        
        myJson = json.dumps(cur.fetchall())
        
        return myJson
    
    myConn.close()
