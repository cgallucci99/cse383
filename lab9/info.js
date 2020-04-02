var URL="http://ceclnx01.cec.miamioh.edu/~johnsok9/cse383/ajax/index.php";
// counter vars
var usersRun = 0,
    loadRun = 0,
    networkRun = 0,
    logRun = 0;
// vars for computing bytes/sec for network
var prevTxBytes = 0,
    prevRxBytes = 0;

var prevTime = getTime();

getUsers();
getLoadavg();
getNetwork();
console.log(getTime());

function getUsers() {
    a = $.ajax( {
        url: URL + '/api/v1/who',
        method: "GET"
    }).done(function(data) {
        // update user counter
        usersRun++;
        $("#usersRun").html(usersRun);
        // clear past run
        $("#users").html("");
        // add new data from API call
        var len = data.who.length;
        for (var i = 0; i < len; i++) {
            $("#users").append("<tr><td>"+data.who[i].uid+"</td><td>"+data.who[i].ip+"</td></tr>")
        }
        // repeat every second
        setTimeout(getUsers, 1000);
    }).fail(function(error) {
        // update error log counter
        logRun++;
        $("#logRun").html(logRun);
        // add new error at top
        $("#log").prepend("who error " + new Date() + "<br>");
        // repeat every second
        setTimeout(getUsers, 1000);
    })
}

function getLoadavg() {
    a = $.ajax( {
        url: URL + '/api/v1/loadavg',
        method: "GET"
    }).done(function(data) {
        // update loadavg counter
        loadRun++;
        $("#loadRun").html(usersRun);
        // add new data from API call
        $("#onemin").html(data.loadavg.OneMinAvg);
        $("#fivemin").html(data.loadavg.FiveMinAvg);
        $("#fifteenmin").html(data.loadavg.FifteenMinAvg);
        $("#numRunning").html(data.loadavg.NumRunning);
        $("#ttlProc").html(data.loadavg.TtlProcesses);
        // repeat every second
        setTimeout(getLoadavg, 1000);
    }).fail(function(error) {
        // update error log counter
        logRun++;
        $("#logRun").html(logRun);
        // add new error at top
        $("#log").prepend("loadavg error " + new Date() + "<br>");
        // repeat every second
        setTimeout(getLoadavg, 1000);
    })
}

function getNetwork() {
    a = $.ajax( {
        url: URL + '/api/v1/network',
        method: "GET"
    }).done(function(data) {
        var curTime = getTime();
        var elapsed = curTime - prevTime;
        var newTxBytes = data.network.txbytes - prevTxBytes;
        var newRxBytes = data.network.rxbytes - prevRxBytes;

        // update network counter
        networkRun++;
        $("#networkRun").html(networkRun);
        // add new data from API call
        $("#txbytes").html(data.network.txbytes);
        $("#txavg").html(Math.round(newTxBytes/(elapsed/1000)));
        $("#rxbytes").html(data.network.rxbytes);
        $("#rxavg").html(Math.round(newRxBytes/(elapsed/1000)));
        // repeat every second
        setTimeout(getNetwork, 1000);
        prevTxBytes = data.network.txbytes;
        prevRxBytes = data.network.rxbytes;
        prevTime = getTime();
    }).fail(function(error) {
        // update error log counter
        logRun++;
        $("#logRun").html(logRun);
        // add new error at top
        $("#log").prepend("network error " + new Date() + "<br>");
        // repeat every second
        setTimeout(getNetwork, 1000);
    })
}

function getTime() {
    var d = new Date();
    var time = d.getTime()
    return time;
}