World.load("Testing Map", "tiles/basic.json");

// A simple jQuery-like wrapper
var $ = function (x) { 
    return World.$(x);
};

// Handle logic for the first trigger
(function () {
    var $trigger1 = $('#trigger1');
    if ($trigger1 === null) {
        return;
    }

    $trigger1.touch(function (e) {
        if (e.is(':player')) {
            e.health--;
        }
    });
})();
