var webSocket;
function main() {
    webSocket = new WebSocket("ws://localhost:8080");
    webSocket.onopen = function (event) {
        console.log('Connection opened!');
        webSocket.send('{"request": "route"}');
    };
    webSocket.onmessage = function (event) {
        console.log(event.data);
        var response = JSON.parse(event.data);
        if (response.response === 'route') {
            var routes = response.route;
            var select = document.getElementById('route_select');
            routes.forEach(function (route) {
                var option = document.createElement('OPTION');
                option.text = route;
                option.value = route;
                select.add(option);
            })
        } else if (response.response === 'data') {
            console.log(response.data);
        }
    };
    webSocket.onclose = function (event) {
        console.log("Connection closed");
    };
}

function showHeatMap(status) {
    if (status) {
        console.log('mostrando algo super legal!')
    } else {
        console.log('não mostrando algo super legal!')
    }
}

function showPoints(status) {
    if (status) {
        console.log('mostrando algo super legal!')
    } else {
        console.log('não mostrando algo super legal!')
    }
}

function showGrid(status) {
    if (status) {
        console.log('mostrando algo super legal!')
    } else {
        console.log('não mostrando algo super legal!')
    }
}

function showRoute(status) {
    if (status) {
        console.log('mostrando algo super legal!')
    } else {
        console.log('não mostrando algo super legal!')
    }
}

function send() {
    var json = {
        request: 'data',
        route: document.getElementById('route_select').value,
        size: document.getElementById('grid_size').value
    };
    webSocket.send(JSON.stringify(json));
}

function closeSocket() {
    webSocket.close();
}

function start() {
}
