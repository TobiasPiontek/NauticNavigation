//dummy geojson objekt
var myLines;
var address = "http://localhost:8004/NauticNavigation";
var accesstoken = "pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw";
//Use this variable to set line style
var myStyle = {
    "color": "#2e86c1",
    "weight": 5,
    "opacity": 1.00
}

document.getElementById("back").disabled = true;
document.getElementById("next").disabled = false;
document.getElementById("compute").disabled = true;

//the map element
var mymap = L.map('mapcontainer').setView([0.0, 0.0], 2);

//marker
var firstmarker;
var secondmarker;

//state of gui
var state = 0;

//helper variables to store up coordinates
var x1;
var y1;
var x2;
var y2;
var startset = false;
var targetset = false;

//Object that represents the ways later
var weg;

//loading of the leaflet map
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=' + accesstoken , {
    maxZoom: 6,
    id: 'mapbox.light'
}).addTo(mymap);

//function to test marker functionality
mymap.on('click', function(e) {

    var k = e.latlng;
    console.log(k);
    //check state and set markers accordingly
    if (state == 0) {
        //delete old marker and set new one
        if (firstmarker) {
            mymap.removeLayer(firstmarker);
        }
        firstmarker = new L.Marker(k).addTo(mymap);
        x1 = k.lat;
        y1 = k.lng;
        //write coordinates to html
        document.getElementById("lat1").value = x1;
        document.getElementById("long1").value = y1;
		checkFirst();
		compute();
		next();

    } else {
        if (secondmarker) {
            mymap.removeLayer(secondmarker);
        }
        secondmarker = new L.Marker(k).addTo(mymap);
        x2 = k.lat;
        y2 = k.lng;
        document.getElementById("lat2").value = x2;
        document.getElementById("long2").value = y2;
		checkSecond();
		compute();
		back();
    }
});

//hooks for buttons
function next() {
    if (state == 0) {
        state = 1;
        document.getElementById("back").disabled = false;
        document.getElementById("next").disabled = true;
    }
};

function back() {
    if (state == 1) {
        state = 0;
        document.getElementById("back").disabled = true;
        document.getElementById("next").disabled = false;
    }
};

function checkComuputeState(){
  if(startset && targetset){
    document.getElementById("compute").disabled = false;
  }
}

function compute() {
    if ((firstmarker != null) && (secondmarker != null)) {
        if (weg) {
            mymap.removeLayer(weg);
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                console.log(this.responseText);
                myLines = JSON.parse(this.responseText);
                console.log(myLines);
                weg = L.geoJSON(myLines, { style: myStyle }).addTo(mymap);
            }
        };
        xhttp.open("POST", address, true);
        xhttp.send("calculateRoute;" + document.getElementById("lat1").value + "," + document.getElementById("long1").value + ";" + document.getElementById("lat2").value + "," + document.getElementById("long2").value);
    }
};

function checkFirst() {
    if (firstmarker != null) {
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                var response = this.responseText;
                var split = response.split(",");
                document.getElementById("lat1").value = split[0];
                document.getElementById("long1").value = split[1];
                if (firstmarker) {
                    mymap.removeLayer(firstmarker);
                }
                firstmarker = new L.Marker([split[0], split[1]]).addTo(mymap);
            }
        };
        xhttp.open("POST", address, true);
        xhttp.send("SetNode" + ";" + document.getElementById("lat1").value + "," + document.getElementById("long1").value);
        startset = true;
        checkComuputeState();
    }
};

function checkSecond() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var response = this.responseText;
            var split = response.split(",");
            document.getElementById("lat2").value = split[0];
            document.getElementById("long2").value = split[1];
            if (secondmarker) {
                mymap.removeLayer(secondmarker);
            }
            secondmarker = new L.Marker([split[0], split[1]]).addTo(mymap);
        }
    };
    xhttp.open("POST", address, true);
    xhttp.send("SetNode" + ";" + document.getElementById("lat2").value + "," + document.getElementById("long2").value);
    targetset = true;
    checkComuputeState();
};
