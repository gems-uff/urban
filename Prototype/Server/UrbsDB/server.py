from sqlite import creteTable, queryJson

import http.server

class HTTPHandler(http.server.BaseHTTPRequestHandler):

    def do_GET(self):
        if 'api?stations' in self.path:
            jsonData = queryJson()

            self.send_response(200)
            self.send_header("Access-Control-Allow-Origin", "*")
            self.send_header('Content-type', 'text/json')
            self.end_headers()
            self.wfile.write(jsonData.encode(encoding='utf_8'));
            
            return
       
def init():
    # configure httpd parameters
    server_addr = ('localhost', 8000)
    
    # instantiate a server object
    httpd = http.server.HTTPServer(server_addr, HTTPHandler)
    
    # start serving pages
    print("Server started...")
    httpd.serve_forever ()
        
if __name__ == "__main__":
    creteTable()
    init()
